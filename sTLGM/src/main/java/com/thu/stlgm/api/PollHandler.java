package com.thu.stlgm.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.bean.StudentBean;

import serializable.GroupMsg;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        void OnMissionSuccess(int GetCoin,int AllCoin);

        void OnHpStart(long Time,int Interval);
        void OnHpInfo(int blood);
        void OnHpPause();

        //獲得金創藥
        void getAdditional();

    }
    private static final String TAG = PollHandler.class.getName();
    private static final int time = 3000;

    private String gid;

    TimerTask mTimerTask;
    Timer mPollTimer;

    private static final String ServerIP = "http://54.214.24.26:8080/StudentSquare_server";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private List<StudentBean> mStudentList;

    private OnMessageReceive mListener;

    //常數
    private static final String RECEIVE_QUSETION = "$1";
    private static final String QUSETION_RESULT = "$3";

    private static final String HP_START = "~1";
    private static final String HP_INFO = "~2";
    private static final String HP_PASUE = "~6";


    public PollHandler(String gid) {
        this.gid = gid;

        mStudentList = new ArrayList<StudentBean>();

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

                    }

                    //尋找任務
                    List<String> Mission = msgMap.get("1");
                    if (Mission!=null){
                        String Message = Mission.get(0);
                        Log.d(TAG,"Receive Message:"+Message);
                        String[] Messages = Message.split(",");

                        String Type = Messages[0];

                        if (Type.equals(RECEIVE_QUSETION)){
                            if (mListener!=null)
                                mListener.OnMissionReceive(Messages[1],Messages[2],Messages[3]);
                        }else if (Type.equals(QUSETION_RESULT)){
                            if (mListener!=null)
                                mListener.OnMissionSuccess(Integer.parseInt(Messages[1]),Integer.parseInt(Messages[2]));
                        }
                    }

                    //尋找個人訊息
                    for (StudentBean mStudent : mStudentList){
                        List<String> StudentMessageList = msgMap.get(mStudent.getSID());
                        for (String mStudentMessageArray : StudentMessageList){
                            String Messages[] = mStudentMessageArray.split(" ");
                            for (String Message : Messages){
                                String SplitMessage[] = Message.split(",");

                                String Type = SplitMessage[0];

                                if (Type.equals(HP_START)){
                                    try {
                                        Date date = new SimpleDateFormat("HH:mm:ss").parse(SplitMessage[1]);

                                        if (mListener!=null)
                                            mListener.OnHpStart(date.getTime(),Integer.parseInt(SplitMessage[2]));
                                    }catch (ParseException mParseException){
                                        mParseException.printStackTrace();
                                    }
                                }else if (Type.equals(HP_INFO)){

                                    if (SplitMessage.length==2){

                                        if (mListener!=null)
                                            mListener.OnHpInfo(Integer.parseInt(SplitMessage[1].replace("%","")));
                                    }else if (SplitMessage.length==3){
                                        Log.d(TAG,"additional");
                                        if (mListener!=null)
                                            mListener.getAdditional();
                                    }

                                }else if (Type.equals(HP_PASUE)){
                                    if (mListener!=null)
                                        mListener.OnHpPause();
                                }
                            }
                        }
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

    public void addStudent(StudentBean mStudent){
        mStudentList.add(mStudent);
    }


    public void setListener(OnMessageReceive mListener) {
        this.mListener = mListener;
    }
}
