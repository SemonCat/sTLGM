package com.thu.stlgm.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thu.stlgm.game.Ball;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/21.
 */
public class BallView extends ImageView implements SensorEventListener{
    public interface OnObjectTouchEvent{
        public void OnObjectTouchEvent(BookView mView);
        public void OnObjectOutsideEvent(BookView mView);
    }
    /**SensorManager管理器**/
    private SensorManager mSensorMgr = null;
    Sensor mSensor = null;
    RelativeLayout.LayoutParams layoutParams;
    private int parentWidth,parentHeight;

    int left = 0, top = 0;


    private OnObjectTouchEvent mListener;

    private Rect ViewHitRect = new Rect();

    private int[] ViewLocation = new int[2];

    private boolean isRuning = true;

    public BallView(Context context) {
        super(context);
    }

    public void start(){
        layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        left = layoutParams.leftMargin;
        top = layoutParams.topMargin;
        parentWidth = ((View)getParent()).getWidth();
        parentHeight = ((View)getParent()).getHeight();



        mSensorMgr = (SensorManager) getContext().getSystemService(Activity.SENSOR_SERVICE);

        if (mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // 注册listener，第三个参数是检测的精确度
        //SENSOR_DELAY_FASTEST 最灵敏 因为太快了没必要使用
        //SENSOR_DELAY_GAME    游戏开发中使用
        //SENSOR_DELAY_NORMAL  正常速度
        //SENSOR_DELAY_UI          最慢的速度
        mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        isRuning = true;
    }

    public void stop(){
        isRuning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isRuning) return;

        float mGX = event.values[0];
        float mGY= event.values[1];

        mGX = 2*mGX;
        mGY = 2*mGY;

        //mGX = -mGX;

        left -= mGX;
        top += mGY;

        if (left < 0) {
            left = 0;
        } else if (left > parentWidth-getWidth()) {
            left = parentWidth-getWidth();
        }
        if (top < 0) {
            top = 0;
        } else if (top > parentHeight-getHeight()) {
            top = parentHeight-getHeight();
        }

        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        setLayoutParams(layoutParams);

        checkList();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    Rect outRect = new Rect();
    int[] location = new int[2];

    public boolean inViewBounds(View view){
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);


        getDrawingRect(ViewHitRect);
        getLocationOnScreen(ViewLocation);
        ViewHitRect.offset(ViewLocation[0],ViewLocation[1]);


        return outRect.intersect(ViewHitRect);
    }

    private List<BookView> mViews;
    private void checkList(){
        if (mViews!=null){
            for (BookView mView:mViews){
                boolean result = inViewBounds(mView);
                if (result){
                    if (mListener!=null)
                        mListener.OnObjectTouchEvent(mView);
                }else{
                    if (mListener!=null)
                        mListener.OnObjectOutsideEvent(mView);
                }
            }
        }
    }


    public void setListener(OnObjectTouchEvent mListener) {
        this.mListener = mListener;
    }


    public void setBookViewList(List<BookView> mViews){
        this.mViews = mViews;
    }

    public void addBookView(BookView mBook){
        if (mViews==null){
            mViews = new ArrayList<BookView>();
        }
        this.mViews.add(mBook);
    }

}

