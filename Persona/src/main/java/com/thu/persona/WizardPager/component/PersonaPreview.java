package com.thu.persona.WizardPager.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaPreview extends View{

    private static final int AreaSize = 6;

    private static final int AreaOneColor = 0x33999999;

    private static final int LineWidth = 1;

    private Paint mPaint;
    private boolean[] IsHighLightArray;

    public PersonaPreview(Context context) {
        super(context);
        init();
    }

    public PersonaPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PersonaPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        IsHighLightArray = new boolean[AreaSize];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        drawHighLight(canvas);
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
        canvas.drawLine(canvas.getWidth()/4f,0,canvas.getWidth()/4f,canvas.getHeight()/2f,mPaint);
        canvas.drawLine(canvas.getWidth()*3/4f,0,canvas.getWidth()*3/4f,canvas.getHeight()/2f,mPaint);
        //下
        canvas.drawLine(canvas.getWidth()*3/8f,canvas.getHeight()/2f,canvas.getWidth()*3/8f,canvas.getHeight(),mPaint);
        canvas.drawLine(canvas.getWidth()*6/8f,canvas.getHeight()/2f,canvas.getWidth()*6/8f,canvas.getHeight(),mPaint);
    }

    private void drawHighLight(Canvas canvas){

        mPaint.reset();
        //draw Area 1
        mPaint.setColor(AreaOneColor);
        if (IsHighLightArray[0]){
            canvas.drawRect(LineWidth,LineWidth,canvas.getWidth()/4,canvas.getHeight()/2,mPaint);
        }

        if (IsHighLightArray[1]){
            canvas.drawRect(canvas.getWidth()/4,LineWidth,
                    canvas.getWidth()*3/4,canvas.getHeight()/2,mPaint);
        }

        if (IsHighLightArray[2]){
            canvas.drawRect(canvas.getWidth()*3/4,LineWidth,
                    canvas.getWidth(),canvas.getHeight()/2,mPaint);
        }

        if (IsHighLightArray[3]){
            canvas.drawRect(LineWidth,canvas.getHeight()/2,
                    canvas.getWidth()*3/8,canvas.getHeight(),mPaint);
        }

        if (IsHighLightArray[4]){
            canvas.drawRect(canvas.getWidth()*3/8,canvas.getHeight()/2,
                    canvas.getWidth()*6/8,canvas.getHeight(),mPaint);
        }

        if (IsHighLightArray[5]){
            canvas.drawRect(canvas.getWidth()*6/8,canvas.getHeight()/2,
                    canvas.getWidth(),canvas.getHeight(),mPaint);
        }
    }

    public void setHighLight(int position,boolean IsHighLight){
        if (position >= 0 && position <= IsHighLightArray.length){
            IsHighLightArray[position] = IsHighLight;
        }
        invalidate();
    }
}
