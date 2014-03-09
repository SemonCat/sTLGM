package com.thu.stlgm.bean;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.facebook.android.R;
import com.thu.stlgm.component.BloodView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by SemonCat on 2014/2/18.
 */
public class StudentBean extends AccountBean implements Runnable, Parcelable {
    public interface OnBloodChangeListener{
        public void OnBloodChangeEvent(int blood);
    }

    private static final String TAG = StudentBean.class.getName();

    private String SID;
    private String Department;
    private int Blood;
    private String GroupID;
    private String ImgUrl = "";

    public final static int MAX_BLOOD = 100;

    private ScheduledExecutorService scheduledThreadPool;

    private boolean pause;

    private ScheduledFuture mFuture;

    private OnBloodChangeListener mListener;

    private int mInterval;

    private Handler mHandler;

    public StudentBean() {
        super();
        init();
    }

    public StudentBean(String AccessToken) {
        super(AccessToken);
        init();
    }

    private void init(){
        mHandler = new Handler();
        Blood = MAX_BLOOD;
        pause = false;
        if (scheduledThreadPool!=null)
            scheduledThreadPool.shutdown();
        scheduledThreadPool = Executors.newScheduledThreadPool(1);


    }

    public void startHpService(long deadTime,int Interval){
        if (mFuture!=null){
            mFuture.cancel(true);
        }

        //將Interval單位轉為毫秒
        this.mInterval = Interval*60*1000;

        int time = (int)(Interval*60/(float)MAX_BLOOD*1000);



        int blood = calcBlood(deadTime,Interval, MAX_BLOOD);

        Log.d(TAG, "startHpService,SID:"+getSID()+".Interval:" + time+", Blood:"+blood+",");

        setBlood(blood);
        pause = false;
        mFuture = scheduledThreadPool.scheduleAtFixedRate(this, time, time, TimeUnit.MILLISECONDS);
    }

    public void pasueHpService(){
        pause = true;
        if (mFuture!=null){
            mFuture.cancel(true);
        }

    }

    public void stopHpService(){
        pause = true;
        if (mFuture!=null){
            mFuture.cancel(true);
        }
    }

    @Override
    public void run() {
        if (!mFuture.isCancelled() && !pause && Blood!=0){
            setBlood(Blood - 1);
        }

    }

    @Override
    public int hashCode() {

        return getSID().hashCode();
    }

    @Override
    public boolean equals(Object o) {


        String token1 = ((StudentBean) o).getSID();
        String token2 = this.getSID();

        if (token1==null || token2==null)
            return false;
        return token1.equals(token2);
    }


    /**
     * 計算血量
     * @param Interval 間隔，單位：分鐘。
     * @param deadTime 死亡時間。
     * @param maxBlood 最高血量。
     * @return
     */
    public static int calcBlood(long deadTime,int Interval,int maxBlood){

        //將Interval轉成MILLIS
        long intervalMillis = Interval*60*1000;
        long leftTime = deadTime - System.currentTimeMillis();
        //Log.d(TAG,"Time:"+date+",deadTime:"+deadTime+",leftTime:"+leftTime);
        return (int)(maxBlood*(leftTime/(float)intervalMillis));
    }


    /**
     * 轉換目標血量至所需時間，單位毫秒。
     * @param targetBlood
     * @param Interval
     * @param maxBlood
     * @return
     */
    public static int calcBloodTime(int targetBlood,int Interval,int maxBlood){
        long intervalMillis = Interval*60*1000;
        return (int)(intervalMillis*(targetBlood/(float)maxBlood));
    }

    public void addBlood(int blood){
        setBlood(Blood+blood);
    }

    /**getter and setter**/

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public int getBlood() {
        return Blood;
    }

    public void setBlood(final int blood) {


        Blood = blood;


        if (mListener!=null)
            mListener.OnBloodChangeEvent(Blood);
    }


    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }


    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public void setBloodChangeListener(OnBloodChangeListener mListener) {
        this.mListener = mListener;
    }

    public int getInterval(){
        return mInterval;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SID);
        dest.writeString(this.Department);
        dest.writeInt(this.Blood);
        dest.writeString(this.GroupID);
        dest.writeString(this.id);
        dest.writeString(this.AccessToken);
        dest.writeString(this.Name);
        dest.writeString(this.ImgUrl);
    }

    private StudentBean(Parcel in) {
        this.SID = in.readString();
        this.Department = in.readString();
        this.Blood = in.readInt();
        this.GroupID = in.readString();

        this.id = in.readString();
        this.AccessToken = in.readString();
        this.Name = in.readString();
        this.ImgUrl = in.readString();

        init();

    }

    public static Parcelable.Creator<StudentBean> CREATOR = new Parcelable.Creator<StudentBean>() {
        public StudentBean createFromParcel(Parcel source) {
            return new StudentBean(source);
        }

        public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
}
