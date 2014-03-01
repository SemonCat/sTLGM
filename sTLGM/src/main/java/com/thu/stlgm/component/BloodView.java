package com.thu.stlgm.component;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by SemonCat on 2014/3/2.
 */
public class BloodView extends TextView implements Runnable{

    private static final String TAG = BloodView.class.getName();

    private int blood;
    private final static int MAX_BLOOD = 100;
    private Handler mHandler;

    private ScheduledExecutorService scheduledThreadPool;

    private boolean pause;

    public BloodView(Context context) {
        super(context);
        init();
    }

    public BloodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BloodView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        blood = MAX_BLOOD;
        mHandler = new Handler();
        pause = false;
        if (scheduledThreadPool!=null)
            scheduledThreadPool.shutdown();
        scheduledThreadPool = Executors.newScheduledThreadPool(5);

        setBlood(MAX_BLOOD);
    }

    public void startHpService(int Interval){
        int time = (int)(Interval*60/(float)MAX_BLOOD*1000);

        Log.d(TAG,"startHpService:"+time);



        setBlood(MAX_BLOOD);
        pause = false;
        scheduledThreadPool.scheduleAtFixedRate(this, time, time, TimeUnit.MILLISECONDS);
    }

    public void pasueHpService(){
        pause = true;
    }

    public void stopHpService(){
        pause = true;
        scheduledThreadPool.shutdown();
    }


    public void setBlood(int Value){
        this.blood = Value;
        setText(String.valueOf(blood));
    }

    public int getBlood(){
        return blood;
    }

    @Override
    public void run() {
        Log.d(TAG,"run");
        if (!scheduledThreadPool.isShutdown() && !pause && blood!=0){
            Log.d(TAG,"扣血:"+blood);
            blood --;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setBlood(blood);
                }
            });
        }
    }
}
