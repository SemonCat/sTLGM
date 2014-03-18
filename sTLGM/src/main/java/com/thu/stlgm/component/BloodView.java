package com.thu.stlgm.component;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.thu.stlgm.bean.Blood;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by SemonCat on 2014/3/2.
 */
public class BloodView extends TextView{

    private static final String TAG = BloodView.class.getName();

    private int blood;

    private Handler mHandler;

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
        mHandler = new Handler();
    }

    public void setBlood(int Value){
        setBlood(Value,false);
    }

    public void setBlood(int Value,boolean anim){

        final int addValue = Value- this.blood;

        //Log.d(TAG,"AddValue:"+addValue);

        if (addValue >1 && anim){
            final int animDuration = 50;
            mHandler.postDelayed(new Runnable() {
                int counter = 0;
                @Override
                public void run() {

                    if (counter<=addValue){
                        counter++;
                        setBlood(blood+1);
                        mHandler.postDelayed(this,animDuration);

                    }

                }
            }, animDuration);
        }else{
            this.blood = Value;
        }


        setText(String.valueOf(blood));
    }

    public int getBlood(){
        return blood;
    }

}
