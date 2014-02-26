package com.thu.stlgm.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import serializable.GroupMsg;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SemonCat on 2014/2/20.
 */
public class PollHandler {

    public interface OnMessageReceive{
        void OnMissionReceive(String quizid,String taskid,String groupid);
    }
    private static final String TAG = PollHandler.class.getName();
    private static final int time = 3000;

    private String gid;

    TimerTask mTimerTask;
    Timer mPollTimer;

    private static final String ServerIP = "http://54.214.24.26:8080/StudentSquare_server";

    private static AsyncHttpClient client = new AsyncHttpClient();


    private OnMessageReceive mListener;

    public PollHandler(String gid) {
        this.gid = gid;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                RunTask();
            }
        };

        mPollTimer = new Timer();


    }

    private void RunTask(){
        String URL = ServerIP+"/GroupMessageBox?gid=" + gid;
        client.get(URL,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String message = new String(responseBody);

                try {
                    ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(responseBody));
                    GroupMsg gMsg = (GroupMsg) objIn.readObject();

                    // print
                    Map<String, ArrayList<String>> msgMap = gMsg.getMsg();
                    for (String key : msgMap.keySet()) {
                        ArrayList<String> myMsg = msgMap.get(key);

                        if (myMsg.size() == 0) {
                            continue;
                        }

                        String tmp = key + ":";
                        for (int i = 0; i < myMsg.size(); i++) {
                            tmp += myMsg.get(i) + " ";
                        }

                        Log.d(TAG,"Message:"+tmp);
                        Log.d(TAG,"Key:"+key);
                        Log.d(TAG,"Key=1:"+msgMap.get("1"));
                    }

                    List<String> Mission = msgMap.get("1");
                    if (Mission!=null){
                        String Message = Mission.get(0);
                        Log.d(TAG,"Receive Message:"+Message);
                        String[] Messages = Message.split(",");


                        if (mListener!=null)
                            mListener.OnMissionReceive(Messages[1],Messages[2],Messages[3]);

                    }
                }catch (IOException mIOException){
                    Log.d(TAG, "Error:" + mIOException.getMessage());
                }catch (ClassNotFoundException mClassNotFoundException){
                    mClassNotFoundException.printStackTrace();
                    Log.d(TAG, "Error:" + mClassNotFoundException.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d(TAG, "Error Result:" + error.toString());

            }
        });
    }

    public void start(){
        mPollTimer.scheduleAtFixedRate(mTimerTask,time,time);
    }

    public void cancel(){
        mPollTimer.cancel();
    }


    public void setListener(OnMessageReceive mListener) {
        this.mListener = mListener;
    }
}
