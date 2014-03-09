package com.thu.stlgm.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.util.ConstantUtil;

import serializable.GroupMsg;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SemonCat on 2014/2/20.
 */
public class PollHandler {

    public interface OnMessageReceive{
        /**
         * 接收到任務事件
         * @param quizid
         * @param taskid
         * @param groupid
         */
        void OnMissionReceive(String quizid,String taskid,String groupid);

        /**
         * 任務回答成功
         * @param GetCoin
         * @param AllCoin
         */
        void OnMissionSuccess(int GetCoin,int AllCoin);

        /**
         * 血量伺服器啟動
         * @param Time 死亡時間
         * @param Interval 死亡間隔
         */
        void OnHpStart(StudentBean mStudent,long Time,int Interval);

        /**
         * 血量警示
         * @param blood 血量數據
         */
        void OnHpInfo(StudentBean mStudent,int blood);

        /**
         * 血量暫停
         */
        void OnHpPause();

        /**
         * 獲得金創藥
         * @param blood 補血量
         */
        void getAdditional(StudentBean mTarget,int blood);

    }
    private static final String TAG = PollHandler.class.getName();
    private static final int time = 3000;

    private String gid;

    TimerTask mTimerTask;
    Timer mPollTimer;

    private static final String ServerIP = ConstantUtil.ServerIP;

    private static AsyncHttpClient client = new AsyncHttpClient();

    private List<StudentBean> mStudentList;

    private OnMessageReceive mListener;

    //常數
    private static final String RECEIVE_QUESTION = "$1";
    private static final String QUESTION_RESULT = "$3";

    private static final String HP_START = "~1";
    private static final String HP_INFO = "~2";
    private static final String HP_PAUSE = "~6";


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

                        //Log.d(TAG,"Message:"+tmp);
                        //Log.d(TAG,"Key:"+key);

                    }

                    //尋找任務
                    List<String> Mission = msgMap.get("0");
                    if (Mission!=null){
                        String Message = Mission.get(0);
                        Log.d(TAG,"Receive Message:"+Message);
                        String[] Messages = Message.split(",");

                        String Type = Messages[0];

                        if (Type.equals(RECEIVE_QUESTION)){
                            if (mListener!=null)
                                mListener.OnMissionReceive(Messages[1],Messages[2],Messages[3]);
                        }else if (Type.equals(QUESTION_RESULT)){
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
                                        //轉換日期
                                        Date date = new SimpleDateFormat("HH:mm:ss").parse(SplitMessage[1]);
                                        Calendar mCalender = Calendar.getInstance();
                                        Calendar nowCalender = Calendar.getInstance();
                                        mCalender.setTime(date);
                                        mCalender.set(nowCalender.get(Calendar.YEAR),
                                                nowCalender.get(Calendar.MONTH),
                                                nowCalender.get(Calendar.DAY_OF_MONTH));
                                        if (mListener!=null)
                                            mListener.OnHpStart(mStudent,mCalender.getTimeInMillis(),Integer.parseInt(SplitMessage[2]));
                                    }catch (ParseException mParseException){
                                        mParseException.printStackTrace();
                                    }
                                }else if (Type.equals(HP_INFO)){

                                    if (SplitMessage.length==2){

                                        if (mListener!=null)
                                            mListener.OnHpInfo(mStudent,Integer.parseInt(SplitMessage[1].replace("%","")));
                                    }else if (SplitMessage.length==3){
                                        if (mListener!=null)
                                            mListener.getAdditional(mStudent,Integer.parseInt(SplitMessage[2].replace("%", "")));
                                    }

                                }else if (Type.equals(HP_PAUSE)){
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
