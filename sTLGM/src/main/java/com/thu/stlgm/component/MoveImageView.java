package com.thu.stlgm.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by SemonCat on 2014/2/20.
 */
public class MoveImageView extends ImageView implements Runnable{

    int left, top;
    int dx = 1, dy = 1;
    Handler handler = new Handler();
    Boolean isRuning = true;
    RelativeLayout.LayoutParams layoutParams;

    Bitmap bitmap;

    Thread mThread;

    TYPE mTYPE;

    boolean ViewVisible;

    Handler mHandler = new Handler();

    enum TYPE{
        TYPE1,
        TYPE2
    }

    private int parentWidth,parentHeight;



    public MoveImageView(Context context) {
        super(context);
        setVisibility(View.GONE);
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public void setRandomImage(int... ImageNumber){
        int randomImage = new Random().nextInt(ImageNumber.length);
        setImageResource(ImageNumber[randomImage]);
    }


    public void start(){
        this.handler = new Handler();
        parentWidth = ((View)getParent()).getWidth();
        parentHeight = ((View)getParent()).getHeight();
        bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        isRuning = true;
        ViewVisible = true;

        boolean randomType = new Random().nextBoolean();
        if(randomType){
            mTYPE = TYPE.TYPE1;
        }
        else {
            mTYPE = TYPE.TYPE2;
        }

        if (parentWidth==0) parentWidth = 500;
        if (parentHeight==0) parentHeight = 500;

        int randomLeft =new Random().nextInt(parentWidth)-getWidth();
        int randomTop =new Random().nextInt(parentHeight)-getHeight();
        if(randomLeft<0) randomLeft=0;
        if(randomTop<0) randomTop=0;
        left=randomLeft;
        top=randomTop;
        mThread = new Thread(this);
        mThread.start();
    }

    public void stop(){
        isRuning = false;
        mThread.interrupt();
    }

    @Override
    public void run() {

        while (isRuning) {



            dx = left < 0 || (left + getWidth()) > parentWidth ? -dx : dx;
            dy = top < 0 || (top + getHeight()) > parentHeight ? -dy : dy;

            int randomX = new Random().nextInt(7);
            int randomY = new Random().nextInt(4)+2;
            int randomSleep = new Random().nextInt(2)+2;

            if (ViewVisible){
                ViewVisible =false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(View.VISIBLE);
                    }
                });


            }

            boolean array[]=new boolean[]{true,true,true,true,false,false,false};

            switch (mTYPE){
                case TYPE1:
                    if(array[randomX]){
                        left += dx;
                    };
                    if(array[randomY]){
                        top += dy;
                    };

                    break;
                case TYPE2:
                    left += dx;
                    top += dy;

                    break;
            }

            handler.post(new Runnable() {

                @Override
                public void run() {

                    layoutParams.leftMargin = left;
                    layoutParams.topMargin = top;
                    setLayoutParams(layoutParams);
                }
            });
            try {
                Thread.sleep(randomSleep);
            } catch (InterruptedException e) {

            }


        }


    }


}