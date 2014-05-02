package com.thu.digi256.View;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by SemonCat on 2014/4/8.
 */
public class GameSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {
    public static final int GAME_HEART = 1000 / 30; // 每秒刷新30次

    private Thread thread;
    private SurfaceHolder holder;

    protected Paint paint;

    private boolean isRun; // 線程運行標誌
    private float fps; // 記錄每次刷屏使用的時間

    public GameSurfaceView(Context context) {
        this(context, null);

    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

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
        canvas.drawColor(Color.BLACK);
        //Log.d(VIEW_LOG_TAG, "_draw");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.d(VIEW_LOG_TAG, "surfaceCreated");
        thread = new Thread(this);
        isRun = true;
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        isRun = false;
        thread = null;
        Log.d(VIEW_LOG_TAG, "surfaceDestroyed");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        long start, end, useTime;

        while (isRun) {
            start = System.currentTimeMillis();
            {
                Canvas canvas = holder.lockCanvas();
                if (canvas!=null){
                    _update(); // 刷新界面上所有元素
                    _draw(canvas); // 繪製界面元素
                }
                holder.unlockCanvasAndPost(canvas);
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