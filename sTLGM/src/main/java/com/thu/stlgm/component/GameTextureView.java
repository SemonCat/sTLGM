package com.thu.stlgm.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;

/**
 * Created by SemonCat on 2014/2/13.
 */
public abstract class GameTextureView extends TextureView implements
        TextureView.SurfaceTextureListener,Runnable {
    public static final int GAME_HEART = 1000 / 60; // 每秒刷新30次

    private Thread thread;
    //private SurfaceHolder holder;

    protected Paint paint;

    private boolean isRun; // 線程運行標誌
    private float fps; // 記錄每次刷屏使用的時間


    public GameTextureView(Context context) {
        this(context, null);

    }

    public GameTextureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GameTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSurfaceTextureListener(this);
    }

    /**
     * 執行遊戲邏輯方法
     */
    public void _update() {

    }

    /**
     * 執行遊戲繪製
     */
    public void _draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        onDrawEvent(canvas);
        //Log.d(VIEW_LOG_TAG, "_draw");
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface,
                                          int width, int height) {
        // TODO Auto-generated method stub
        Log.d(VIEW_LOG_TAG, "surfaceCreated");
        thread = new Thread(this);
        isRun = true;
        thread.start();
    }

    public abstract void onDrawEvent(Canvas canvas);

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                                            int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // TODO Auto-generated method stub
        isRun = false;
        thread = null;
        Log.d(VIEW_LOG_TAG, "surfaceDestroyed");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        long start, end, useTime;

        while (isRun) {
            start = System.currentTimeMillis();
            {
                Canvas canvas = this.lockCanvas();
                if (canvas==null) return;
                _update(); // 刷新界面上所有元素
                _draw(canvas); // 繪製界面元素
                unlockCanvasAndPost(canvas);
            }
            end = System.currentTimeMillis();
            useTime = (int) (end - start);
            fps = 1000 / Math.max(System.currentTimeMillis() - start, 1);

            if (useTime < GAME_HEART) { // 保證每次刷屏時間間隔相同
                try {
                    Thread.sleep(GAME_HEART - useTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


}
