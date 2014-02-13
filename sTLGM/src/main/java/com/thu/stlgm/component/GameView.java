package com.thu.stlgm.component;



import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 *
 * Copyright (c) 2012 All rights reserved
 * 名稱：GameView.java
 * 描述：繼承自該類的view只需要考慮怎麼繪制畫面
 * 需要實現draw(Canvas canvas)方法
 * 注意：實現的draw方法中不要锁定、解锁畫布
 * 不要進行異常處理
 * @author zhaoqp
 * @date：2012-10-30 下午4:42:32
 * @version v1.0
 */
public abstract class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = "GameView";
    public ViewThread thread;   //刷幀的線程
    //定義SurfaceHolder對象
    private SurfaceHolder mSurfaceHolder = null;
    public String fps="FPS:N/A";          //用於顯示幀速率的字符串，調試使用
    private boolean loop = true;
    private boolean pause = true;
    //睡眠的毫秒數
    private int sleepSpan = 1000/30;
    public GameView(Context context){
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        //Log.d(TAG, "--GameView Created--");
        // 實例化SurfaceHolder
        mSurfaceHolder = this.getHolder();
        // 添加回調
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        thread = new ViewThread(mSurfaceHolder,this);
        thread.start();
    }

    /**
     * 設置刷新的sleep間隔時間
     */
    public void setSleep(int time){
        this.sleepSpan = time;
    }

    /**
     * 設置循環標記位
     * @param loop
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * 設置循環暫停標記位
     * @param pause
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * 繪圖
     */
    public abstract void onDraw(Canvas canvas);

    /**
     * 在surface創建時激發的擴展方法
     */
    public void expandSurfaceCreated(){
    }
    /**
     * 在surface創建時激發的擴展方法
     */
    public void expandSurfaceDestroyed(){
    }

    /**
     * 在surface的大小發生改變時激發
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }
    /**
     * 在surface銷毀時激發
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        //如果後台重繪線程沒起來,就启動它
        if(! this.thread.isAlive()){
            try{
                //启動刷幀線程
                this.setLoop(true);
                this.setPause(true);
                this.thread.start();
                expandSurfaceCreated();
            }catch(Exception e){
                e.printStackTrace();
                this.setLoop(false);
                this.setPause(false);
            }
        }
        Log.d(TAG, "--surfaceCreated--");
    }
    /**
     * 在surface銷毀時激發
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        releaseViewThread();
        Log.d(TAG, "--surfaceDestroyed--");
    }

    /**
     * 釋放view類線程
     */
    public void releaseViewThread(){
        if(thread != null && thread.isAlive()){
            this.setPause(false);
            this.setLoop(false);
            try {
                thread.interrupt();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    /**
     * 觸摸屏事件
     * @param event
     */
    public void onMyTouchEvent(MotionEvent event){
    }

    /**
     * 按鍵事件按下
     * @param keyCode
     * @param event
     */
    public void onMyKeyDown(int keyCode, KeyEvent event) {
    }

    /**
     * 按鍵事件抬起
     * @param keyCode
     * @param event
     */
    public void onMyKeyUp(int keyCode, KeyEvent event) {
    }

    /**
     * 滾動事件
     * @param event
     */
    public void onMyTrackballEvent(MotionEvent event) {
    }

    /**
     * 刷幀線程
     */
    class ViewThread extends Thread{

        private SurfaceHolder surfaceHolder;
        private GameView gameView;
        private int count = 0;              //記錄幀數，該變量用於計算幀速率
        private long start = System.nanoTime(); //記錄起始時間，該變量用於計算幀速率
        /**
         * 構造方法
         * @param surfaceHolder
         * @param gameView
         */
        public ViewThread(SurfaceHolder surfaceHolder, GameView gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (loop) {
                while (pause) {
                    canvas = null;
                    try {
                        if(!Thread.currentThread().isInterrupted()){
                            //锁定整個畫布，在內存要求比較高的情況下，建議参數不要为null
                            canvas = this.surfaceHolder.lockCanvas();
                            synchronized (this.surfaceHolder) {
                                gameView.onDraw(canvas);//繪制
                            }
                        }
                    }catch (Exception e){
                        //e.printStackTrace();
                        Thread.currentThread().interrupt();
                        loop = false;
                    }finally {
                        if (canvas != null) {
                            //更新屏幕顯示內容
                            this.surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                    this.count++;
                    if(count == 20){ //如果計滿20幀
                        count = 0;  //清空計數器
                        long tempStamp = System.nanoTime(); //獲取當前時間
                        long span = tempStamp - start;  //獲取時間間隔
                        start = tempStamp;     //为start重新賦值
                        double fps = Math.round(100000000000.0/span*20)/100.0;//計算幀速率
                        gameView.fps = "FPS:"+fps;//將計算出的幀速率設置到gameView的相應字符串對象中
                    }
                    try{
                        Thread.sleep(sleepSpan);  //睡眠指定毫秒數
                    }catch(Exception e){
                        //e.printStackTrace();      //打印堆棧信息
                    }
                }
            }
        }
    }
}

