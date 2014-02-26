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

    int left = 0, top = 0;
    int dx = 1, dy = 1;
    Handler handler = new Handler();
    Boolean isRuning = true;
    RelativeLayout.LayoutParams layoutParams;

    Bitmap bitmap;

    Thread mThread;

    private int parentWidth,parentHeight;



    public MoveImageView(Context context) {
        super(context);
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public void start(){
        this.handler = new Handler();
        parentWidth = ((View)getParent()).getWidth();
        parentHeight = ((View)getParent()).getHeight();
        bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        isRuning = true;
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

            left += dx;
            top += dy;

            handler.post(new Runnable() {

                @Override
                public void run() {

                    layoutParams.leftMargin = left;
                    layoutParams.topMargin = top;
                    setLayoutParams(layoutParams);
                }
            });
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
