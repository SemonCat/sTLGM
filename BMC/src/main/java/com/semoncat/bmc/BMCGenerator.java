package com.semoncat.bmc;

import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import com.semoncat.bmc.ui.BMCStepFragment;

/**
 * Created by SemonCat on 2014/6/3.
 */
public class BMCGenerator {

    private static final String TAG = BMCGenerator.class.getName();

    private static int TEXT_SIZE_TITLE = 40;
    private static final int TEXT_SIZE_CONTENT = 30;
    private static final int LineWidth = 2;

    private static final int defaultHeight = 800;

    private static final float defaultProportion = 8/5f;

    private static final int defaultWidth = (int)(defaultHeight*defaultProportion);

    private static final int TEXT_PADDING = 20;

    private static final int TitleSpace = 100;

    private static final int TITLE_COLOR = 0xff0099cc;

    public static Bitmap getPersonaBitmap(String[] mData) {

        Paint mPaint = new Paint();

        int width = defaultWidth;
        int height = defaultHeight;

        int MaxTextHeight = getMaxHeight(width, mData);

        if (MaxTextHeight > defaultHeight){
            height = MaxTextHeight;
            width = (int)(height*defaultProportion);
            TEXT_SIZE_TITLE = (int) (height/40f);
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        drawRect(mData, canvas, mPaint);


        return bitmap;
    }


    private static void drawText(RectF mArea, String Content, Canvas canvas, TextPaint textPaint) {

        Content = Content==null ? "" : Content;

        textPaint.reset();

        textPaint.setColor(Color.BLACK);

        textPaint.setTextSize(TEXT_SIZE_CONTENT);

        StaticLayout layout = new StaticLayout(Content, textPaint, (int) mArea.width()-TEXT_PADDING, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

        canvas.save();
        canvas.translate(mArea.left+(TEXT_PADDING/2), mArea.top+TitleSpace);

        layout.draw(canvas);
        canvas.restore();


        //canvas.translate(mArea.left, mArea.top);
    }

    private static void drawRect(String[] mData, Canvas canvas, Paint mPaint) {
        RectF[] mAreaRectF = new RectF[mData.length];



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

        TextPaint textPaint = new TextPaint();
        for (int i = 0; i < mAreaRectF.length; i++) {
            mPaint.reset();

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(LineWidth);

            RectF mRectF = mAreaRectF[i];
            //畫框線
            canvas.drawRect(mRectF, mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(TITLE_COLOR);
            mPaint.setTextSize(TEXT_SIZE_TITLE);
            float titleY = mRectF.top+TEXT_SIZE_TITLE+TEXT_PADDING;
            //畫標題
            canvas.drawText(BMCStepFragment.SaveKey[i],mRectF.left+TEXT_PADDING,titleY,mPaint);

            //把內容畫出來
            drawText(mRectF, mData[i], canvas, textPaint);
        }
    }

    private static int getMaxHeight(int width, String[] mData) {
        RectF[] mAreaRectF = new RectF[mData.length];

        Bitmap bitmap = Bitmap.createBitmap(width, defaultHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float centerLineWidth = LineWidth / 2f;

        //水平座標
        float horizontalCoordinate = canvas.getHeight() * 3 / 5f;

        mAreaRectF[0] = new RectF(centerLineWidth, centerLineWidth, canvas.getWidth() / 5f, horizontalCoordinate);
        mAreaRectF[1] = new RectF(canvas.getWidth() / 5f, centerLineWidth, canvas.getWidth() * 2 / 5f, horizontalCoordinate / 2f);
        mAreaRectF[2] = new RectF(canvas.getWidth() / 5f, horizontalCoordinate / 2, canvas.getWidth() * 2 / 5f, horizontalCoordinate);
        mAreaRectF[3] = new RectF(canvas.getWidth() * 2 / 5f, centerLineWidth, canvas.getWidth() * 3 / 5f, horizontalCoordinate);
        mAreaRectF[4] = new RectF(canvas.getWidth() * 3 / 5f, centerLineWidth, canvas.getWidth() * 4 / 5f, horizontalCoordinate / 2f);
        mAreaRectF[5] = new RectF(canvas.getWidth() * 3 / 5f, horizontalCoordinate / 2f, canvas.getWidth() * 4 / 5f, horizontalCoordinate);
        mAreaRectF[6] = new RectF(canvas.getWidth() * 4 / 5f, centerLineWidth, canvas.getWidth(), horizontalCoordinate);
        mAreaRectF[7] = new RectF(centerLineWidth, horizontalCoordinate, canvas.getWidth() / 2f, canvas.getHeight());
        mAreaRectF[8] = new RectF(canvas.getWidth() / 2f, horizontalCoordinate, canvas.getWidth(), canvas.getHeight());


        TextPaint textPaint = new TextPaint();
        for (int i = 0; i < mAreaRectF.length; i++) {
            RectF mRectF = mAreaRectF[i];
            int textHeight = getTextHeight(mData[i], (int) mRectF.width(), textPaint);
            if (textHeight > mRectF.height()) {
                mRectF.set(mRectF.left, mRectF.top, mRectF.right, mRectF.top + textHeight);
            }
            //Log.d(TAG, "Index:" + i + ",TextHeight:" + mRectF.height());
        }


        int maxTopHeight = 0;
        int maxBottomHeight = 0;
        for (int i = 0; i < mAreaRectF.length; i++) {
            int tempTopHeight = 0;
            int tempBottomHeight = 0;
            if (i == 1 || i == 4) {

                float RectF_Height_1 = mAreaRectF[i].height();
                float RectF_Height_2 = mAreaRectF[i + 1].height();

                tempTopHeight = (int) (RectF_Height_1 > RectF_Height_2 ? RectF_Height_1*2 : RectF_Height_2*2);

                //tempTopHeight = (int) (mAreaRectF[i].height() + mAreaRectF[i + 1].height());

            } else if (i == 7 || i == 8) {

                tempBottomHeight = (int) mAreaRectF[i].height();

            } else {
                tempTopHeight = (int) mAreaRectF[i].height();
            }

            if (tempTopHeight > maxTopHeight) {
                maxTopHeight = tempTopHeight;
            }

            if (tempBottomHeight > maxBottomHeight) {
                maxBottomHeight = tempBottomHeight;
            }
        }

        int MaxHeight = maxBottomHeight + maxTopHeight + (TitleSpace*2);
        //Log.d(TAG, "maxBottomHeight:" + maxBottomHeight + ",maxTopHeight:" + maxTopHeight + ",MaxHeight:" + MaxHeight);


        bitmap.recycle();
        return MaxHeight;
    }

    private static int getTextHeight(String Content, int width, TextPaint textPaint) {
        Content = Content==null ? "" : Content;

        textPaint.reset();

        textPaint.setColor(Color.BLACK);

        textPaint.setTextSize(TEXT_SIZE_CONTENT);

        StaticLayout layout = new StaticLayout(Content, textPaint, 240, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

        return layout.getHeight();
    }

    void drawMultilineText(String str, int x, int y, Paint paint, Canvas canvas) {
        Rect mBounds = new Rect();
        int lineHeight = 0;
        int yoffset = 0;
        String[] lines = str.split("\n");

        // set height of each line (height of text + 20%)
        paint.getTextBounds("Ig", 0, 2, mBounds);
        lineHeight = (int) ((float) mBounds.height() * 1.2);
        // draw each line
        for (int i = 0; i < lines.length; ++i) {
            canvas.drawText(lines[i], x, y + yoffset, paint);
            yoffset = yoffset + lineHeight;
        }
    }

}
