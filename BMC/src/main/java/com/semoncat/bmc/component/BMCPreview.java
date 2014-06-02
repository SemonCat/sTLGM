package com.semoncat.bmc.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by SemonCat on 2014/6/2.
 */
public class BMCPreview extends View{

    private static final String TAG = BMCPreview.class.getName();

    /**
     * This is BMC Number.
     *  -----------------------
     * |   |    |    |    |    |
     * |   | 1  |    | 4  |    |
     * | 0 |----| 3  |----| 6  |
     * |   |    |    |    |    |
     * |   | 2  |    | 5  |    |
     *  -----------------------
     * |          |            |
     * |    7     |     8      |
     *  -----------------------
     */
    private static final int AreaNum = 9;

    private static final int HighLightColor = 0x33999999;

    private final int LineWidth = dip2px(2);

    private Paint mPaint;
    private boolean[] IsHighLightArray;

    public BMCPreview(Context context) {
        super(context);
        init();
    }

    public BMCPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BMCPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        IsHighLightArray = new boolean[AreaNum];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas){
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(LineWidth);

        float centerLineWidth = LineWidth/2f;


        //畫邊框
        canvas.drawLine(centerLineWidth,0,centerLineWidth,canvas.getHeight(),mPaint);
        canvas.drawLine(canvas.getWidth()-centerLineWidth,0,canvas.getWidth()-centerLineWidth,canvas.getHeight(),mPaint);

        canvas.drawLine(0,centerLineWidth,canvas.getWidth(),centerLineWidth,mPaint);
        canvas.drawLine(0,canvas.getHeight()-centerLineWidth,canvas.getWidth(),canvas.getHeight()-centerLineWidth,mPaint);

        //畫區隔
        //上
        canvas.drawLine(0,canvas.getHeight()/2f,canvas.getWidth(),canvas.getHeight()/2f,mPaint);
        canvas.drawLine(canvas.getWidth()/4f,0,canvas.getWidth()/4,canvas.getHeight()/2f,mPaint);
        canvas.drawLine(canvas.getWidth()*3/4f,0,canvas.getWidth()*3/4f,canvas.getHeight()/2f,mPaint);
        //下
        canvas.drawLine(canvas.getWidth()*3/8f,canvas.getHeight()/2f,canvas.getWidth()*3/8f,canvas.getHeight(),mPaint);
        canvas.drawLine(canvas.getWidth()*6/8f,canvas.getHeight()/2f,canvas.getWidth()*6/8f,canvas.getHeight(),mPaint);
    }

    private int dip2px(float dipValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
