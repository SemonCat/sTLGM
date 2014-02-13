package com.thu.stlgm.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.thu.stlgm.R;
import com.thu.stlgm.fragment.LoginFragment;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class FacebookLoginButton extends GameView{
    private static final String TAG = FacebookLoginButton.class.getName();


    private Paint mPaint;

    /**圓滑過之角度**/
    private int mStartAngle = 210;
    private int mSweepAngle = 300;

    private float mCircleStrokeWidth = dip2px(5);

    /**Bitmap**/
    private Bitmap mLoginBitmap;
    private Bitmap mFBLogoBitmap;

    /**Color**/
    private int CircleBackground;
    private int CircleFront;

    /**Value**/
    private int mAngle;
    private boolean mCircleFinish;
    private int mLoginX;
    private RectF mCircleRectF;
    private boolean mLoginFinish;

    public FacebookLoginButton(Context context) {
        this(context, null);
    }

    public FacebookLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FacebookLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
      
    private void initView(){
        this.setClickable(true);
        mPaint = new Paint();
        ColorInit();

    }

    //顏色初始化
    private void ColorInit(){
        Resources mResources = getContext().getResources();
        CircleBackground = mResources.getColor(R.color.FBLoginButtonCircleBackground);
        CircleFront = mResources.getColor(R.color.FBLoginButtonCircleFront);
    }

    //動畫相關參數初始化
    private void ValueInit(){
        mAngle = 0;
        mCircleFinish = false;
        mLoginX = getWidth()/2-mLoginBitmap.getWidth()/2;

        float x = (float)3/(float)4;
        mCircleRectF = new RectF(mLoginBitmap.getWidth()*x+mCircleStrokeWidth,
                mLoginBitmap.getHeight()*x+mCircleStrokeWidth,
                getWidth()-mCircleStrokeWidth,
                getHeight()-mCircleStrokeWidth-(mLoginBitmap.getHeight()*x));

        mLoginFinish = false;
    }

    //Bitmap初始化
    private void BitmapInit(){
        mLoginBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.login);
        int LoginHeight = getHeight()/5;
        mLoginBitmap = Bitmap.createScaledBitmap(mLoginBitmap, 2*LoginHeight, LoginHeight, false);

        mFBLogoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fb_logo);
        mFBLogoBitmap = Bitmap.createScaledBitmap(mFBLogoBitmap, mLoginBitmap.getHeight(), mLoginBitmap.getHeight(), false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        BitmapInit();
        ValueInit();
    }


    @Override
    public void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //畫圓
        drawCircle(canvas);

        //畫Login
        drawLogin(canvas);

        //畫LOGO
        drawFBLogo(canvas);

        // 重繪, 再一次執行 onDraw 程序
        //postInvalidateDelayed(30);


    }

    private void drawCircle(Canvas canvas){

        //畫底圓
        mPaint.setColor(CircleBackground);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleStrokeWidth);

        canvas.drawArc(mCircleRectF,
                mStartAngle, mSweepAngle, false, mPaint );


        //動態畫圓
        mPaint.setColor(CircleFront);

        canvas.drawArc(mCircleRectF,
                mStartAngle, mAngle, false, mPaint );


        if( mAngle < mSweepAngle )
            mAngle+=10;
        else{
            //圓畫完了
            mCircleFinish = true;
        }
    }

    private void drawLogin(Canvas canvas){
        if (mCircleFinish){
            canvas.drawBitmap(mLoginBitmap,mLoginX,canvas.getHeight()/2-mLoginBitmap.getHeight()/2,null);
            if (mLoginX > 0)
                mLoginX -= 30;
            else
                mLoginFinish = true;
        }
    }

    private void drawFBLogo(Canvas canvas){
        if (mLoginFinish){
            canvas.drawPoint(mCircleRectF.centerX()+((mCircleRectF.right-mCircleRectF.centerX())/2),mCircleRectF.centerY(),mPaint);
            canvas.drawPoint(mCircleRectF.centerX(),mCircleRectF.centerY()+((mCircleRectF.top-mCircleRectF.centerY())/2),mPaint);
            canvas.drawPoint(mCircleRectF.centerX(),mCircleRectF.centerY()-((mCircleRectF.top-mCircleRectF.centerY())/2),mPaint);
            canvas.drawBitmap(mFBLogoBitmap,mCircleRectF.centerX()-mFBLogoBitmap.getWidth()/2,mCircleRectF.centerY()-mFBLogoBitmap.getHeight()/2,null);
        }
    }

    public void reStartAnim(){
        ValueInit();
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
