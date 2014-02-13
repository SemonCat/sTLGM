package com.thu.stlgm.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by SemonCat on 2014/2/13.
 */
public abstract class GameTextureView extends TextureView implements TextureView.SurfaceTextureListener{

    private static final String TAG = GameTextureView.class.getName();
    public ViewThread thread;   //刷幀的線程
    public String fps="FPS:N/A";          //用於顯示幀速率的字符串，調試使用
    private boolean loop = true;
    private boolean pause = true;
    //睡眠的毫秒數
    private int sleepSpan = 1000/30;

    public GameTextureView(Context context) {
        super(context);
        init();
    }

    public GameTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        //Log.d(TAG, "--GameView Created--");
        this.setSurfaceTextureListener(this);
        thread = new ViewThread(this);
        thread.start();
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
    public abstract void onDrawEvent(Canvas canvas);

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

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
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

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        releaseViewThread();
        expandSurfaceDestroyed();
        Log.d(TAG, "--surfaceDestroyed--");
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

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
     * 刷幀線程
     */
    class ViewThread extends Thread{

        private GameTextureView gameView;
        private int count = 0;              //記錄幀數，該變量用於計算幀速率
        private long start = System.nanoTime(); //記錄起始時間，該變量用於計算幀速率
        /**
         * 構造方法
         * @param gameView
         */
        public ViewThread(GameTextureView gameView) {
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
                            canvas = gameView.lockCanvas();
                            synchronized (GameTextureView.class) {
                                gameView.onDrawEvent(canvas);//繪制
                            }
                        }
                    }catch (Exception e){
                        Thread.currentThread().interrupt();
                        loop = false;
                    }finally {
                        if (canvas != null) {
                            //更新屏幕顯示內容
                            gameView.unlockCanvasAndPost(canvas);
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

                    }
                }
            }
        }
    }
}
