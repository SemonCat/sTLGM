package com.semoncat.bmc.component;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by SemonCat on 2014/6/2.
 */
public class BMCPreview extends View {

    private static final String TAG = BMCPreview.class.getName();

    /**
     * This is BMC Number.
     * -----------------------
     * |   |    |    |    |    |
     * |   | 1  |    | 4  |    |
     * | 0 |----| 3  |----| 6  |
     * |   |    |    |    |    |
     * |   | 2  |    | 5  |    |
     * -----------------------
     * |          |            |
     * |    7     |     8      |
     * -----------------------
     */
    private static final int AreaNum = 9;

    private static final int HighLightColor = 0x33999999;

    private final int LineWidth = dip2px(2);

    private Paint mPaint;
    private RectF[] mAreaRectF;
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

    private void init() {
        mPaint = new Paint();
        IsHighLightArray = new boolean[AreaNum];
        mAreaRectF = new RectF[AreaNum];

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        drawHighLight(canvas);
    }

    private void drawLine(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(LineWidth);

        float centerLineWidth = LineWidth / 2f;

        //水平座標
        float horizontalCoordinate = canvas.getHeight() * 3 / 5f;

        mPaint.setStyle(Paint.Style.STROKE);
        mAreaRectF[0] = new RectF(centerLineWidth, centerLineWidth, canvas.getWidth() / 5f, horizontalCoordinate);
        mAreaRectF[1] = new RectF(canvas.getWidth() / 5f, centerLineWidth, canvas.getWidth() * 2 / 5f, horizontalCoordinate / 2f);
        mAreaRectF[2] = new RectF(canvas.getWidth() / 5f, horizontalCoordinate / 2, canvas.getWidth() * 2 / 5f, horizontalCoordinate);
        mAreaRectF[3] = new RectF(canvas.getWidth() * 2 / 5f, centerLineWidth, canvas.getWidth() * 3 / 5f, horizontalCoordinate);
        mAreaRectF[4] = new RectF(canvas.getWidth() * 3 / 5f, centerLineWidth, canvas.getWidth() * 4 / 5f, horizontalCoordinate / 2f);
        mAreaRectF[5] = new RectF(canvas.getWidth() * 3 / 5f, horizontalCoordinate / 2f, canvas.getWidth() * 4 / 5f, horizontalCoordinate);
        mAreaRectF[6] = new RectF(canvas.getWidth() * 4 / 5f, centerLineWidth, canvas.getWidth(), horizontalCoordinate);
        mAreaRectF[7] = new RectF(centerLineWidth, horizontalCoordinate, canvas.getWidth() / 2f, canvas.getHeight());
        mAreaRectF[8] = new RectF(canvas.getWidth() / 2f, horizontalCoordinate, canvas.getWidth(), canvas.getHeight());


        for (RectF mRectF : mAreaRectF) {
            canvas.drawRect(mRectF, mPaint);
        }

    }

    private void drawHighLight(Canvas canvas) {

        mPaint.reset();
        //draw HighLight
        mPaint.setColor(HighLightColor);

        for (int i = 0; i < AreaNum; i++) {
            RectF mRectF = mAreaRectF[i];

            if (IsHighLightArray[i]) {
                canvas.drawRect(mRectF, mPaint);
            }

        }

    }


    public void setHighLight(int position,boolean IsHighLight){
        if (position >= 0 && position <= IsHighLightArray.length){
            IsHighLightArray[position] = IsHighLight;
        }
        invalidate();
    }

    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
