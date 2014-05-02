package com.thu.persona.WizardPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;

import com.thu.persona.WizardPager.bean.PersonaData;

import java.io.ByteArrayOutputStream;

/**
 * Created by SemonCat on 2014/5/1.
 */
public class PersonaGenerator {

    private static final int LineWidth = 1;
    private static final int IconPaddingX = 20;
    private static final int IconPaddingY = 20;

    private static final int AreaTwoPaddingX = 20;
    private static final int AreaTwoPaddingY = 20;

    private static final int AreaThreePaddingX = 20;
    private static final int AreaThreePaddingY = 20;

    private static final int AreaThreeTextPadding = 20;

    private static final int AreaFourPaddingX = 20;
    private static final int AreaFourPaddingY = 20;

    private static final int AreaFourTextPadding = 20;

    private static final int AreaFivePaddingX = 20;
    private static final int AreaFivePaddingY = 20;

    private static final int AreaFiveTextPadding = 20;

    private static final int MAX_TITLE_TEXT_SIZE = 35;
    private static final int MIN_CONTENT_TEXT_SIZE = 20;

    private static final int TitleTextColor = 0xff0099cc;

    enum ScalingLogic{
        CROP,
        FIT
    }

    public static byte[] saveBitmap2ByteArray(Bitmap bitmap){
        bitmap = bitmap.copy(bitmap.getConfig(),true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));return scaledBitmap;
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }
    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public static Bitmap getPersonaBitmap(PersonaData mData){
        Paint mPaint = new Paint();

        Bitmap bitmap = Bitmap.createBitmap(1280, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        drawLine(canvas,mPaint);
        drawIcon(mData,canvas,mPaint);
        drawBasicInfo(mData,canvas,mPaint);
        drawMain(mData,canvas,mPaint);
        drawNeed(mData,canvas,mPaint);
        drawGoodThings(mData,canvas,mPaint);
        drawBadThings(mData,canvas,mPaint);
        drawAttr(mData,canvas,mPaint);

        return bitmap;
    }

    private static void drawLine(Canvas canvas,Paint mPaint){

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(LineWidth);
        //畫邊框
        canvas.drawLine(LineWidth,0,LineWidth,canvas.getHeight(),mPaint);
        canvas.drawLine(canvas.getWidth()-LineWidth,0,canvas.getWidth()-LineWidth,canvas.getHeight(),mPaint);

        canvas.drawLine(0,LineWidth,canvas.getWidth(),LineWidth,mPaint);
        canvas.drawLine(0,canvas.getHeight()-LineWidth,canvas.getWidth(),canvas.getHeight()-LineWidth,mPaint);

        //畫區隔
        //上
        canvas.drawLine(0,canvas.getHeight()/2,canvas.getWidth(),canvas.getHeight()/2,mPaint);
        canvas.drawLine(canvas.getWidth()/4,0,canvas.getWidth()/4,canvas.getHeight()/2,mPaint);
        canvas.drawLine(canvas.getWidth()*3/4,0,canvas.getWidth()*3/4,canvas.getHeight()/2,mPaint);
        //下
        canvas.drawLine(canvas.getWidth()*3/8,canvas.getHeight()/2,canvas.getWidth()*3/8,canvas.getHeight(),mPaint);
        canvas.drawLine(canvas.getWidth()*6/8,canvas.getHeight()/2,canvas.getWidth()*6/8,canvas.getHeight(),mPaint);

    }

    private static void drawIcon(PersonaData mData,Canvas canvas,Paint mPaint){
        Bitmap icon = mData.getIcon();
        if (icon!=null){
            int width = canvas.getWidth()/4;
            int height = canvas.getHeight()/2;
            icon = Bitmap.createScaledBitmap(icon,width-IconPaddingX*2,width-IconPaddingY*2,false);
            canvas.drawBitmap(icon,LineWidth+IconPaddingX,LineWidth+IconPaddingY,mPaint);
        }
    }

    private static void drawBasicInfo(PersonaData mData,Canvas canvas,Paint mPaint){
        mPaint.reset();
        int IconHeight = canvas.getWidth()/4;
        int AreaOneHeight = canvas.getHeight()/2;
        float textSize = (AreaOneHeight-IconHeight)/2f;
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);

        String nameInfoFormat = "%s，%s，%s";
        String nameInfo = String.format(nameInfoFormat,mData.getName(),mData.getGender(),mData.getAge());

        canvas.drawText(nameInfo,IconPaddingX,IconHeight+IconPaddingY,mPaint);
        canvas.drawText(mData.getJob(),IconPaddingX,IconHeight+IconPaddingY+textSize,mPaint);
    }

    private static void drawMain(PersonaData mData,Canvas canvas,Paint mPaint){
        mPaint.reset();
        mPaint.setAntiAlias(true);
        int AreaTwoX = canvas.getWidth()/4;
        int AreaTwoWidth = canvas.getWidth()/2;
        int AreaTwoHeight = canvas.getHeight()/2;
        float TitleTextSize;
        float ContentTextSize;



        String TitleFormat = "\"%s\"";
        String Title = String.format(TitleFormat,mData.getTitle());


        //計算出字體大小
        TitleTextSize = (AreaTwoWidth - AreaTwoPaddingX)/Title.length();
        TitleTextSize = Math.min(TitleTextSize,MAX_TITLE_TEXT_SIZE);
        mPaint.setTextSize(TitleTextSize);
        mPaint.setColor(Color.RED);

        //畫大標題
        canvas.drawText(Title,AreaTwoPaddingX+AreaTwoX,AreaTwoPaddingY+TitleTextSize,mPaint);

        //內容字體大小為標題大小減去參數
        ContentTextSize = TitleTextSize - 10;
        ContentTextSize = Math.max(ContentTextSize,MIN_CONTENT_TEXT_SIZE);
        mPaint.setTextSize(ContentTextSize);
        mPaint.setColor(Color.BLACK);
        //分割內容字串
        String Content = mData.getContent();

        int string_Counter = (int)((AreaTwoWidth - AreaTwoPaddingX)/ContentTextSize);

        String[] mSpiltContent = spiltString(Content,string_Counter);

        for (int i = 0;i<mSpiltContent.length;i++){
            String mContentData = mSpiltContent[i];
            canvas.drawText(mContentData,AreaTwoPaddingX+AreaTwoX,(AreaTwoPaddingY+TitleTextSize*2)+(i*ContentTextSize),mPaint);
        }


    }

    private static void drawNeed(PersonaData mData,Canvas canvas,Paint mPaint){
        mPaint.reset();
        mPaint.setAntiAlias(true);

        int AreaThreeX = canvas.getWidth()*3/4;
        int AreaThreeWidth = canvas.getWidth()/4;

        int NeedTitleTextSize;

        int NeedTextSize;

        NeedTitleTextSize = (AreaThreeWidth-AreaThreePaddingX)/5;
        NeedTextSize = (AreaThreeWidth-AreaThreePaddingX)/11;

        //畫標題
        mPaint.setTextSize(NeedTitleTextSize);
        mPaint.setColor(TitleTextColor);
        canvas.drawText("Needs"
                ,AreaThreePaddingX+AreaThreeX
                ,NeedTitleTextSize+AreaThreePaddingY
                ,mPaint);

        //畫內容
        mPaint.setTextSize(NeedTextSize);
        mPaint.setColor(Color.BLACK);


        String NeedStringFormat = "• %s";
        String[] need = mData.getNeed();

        for (int i = 0;i<need.length;i++){

            String mContentData = need[i];
            //如果資料為空就跳過不畫
            if (TextUtils.isEmpty(mContentData)) continue;

            mContentData = String.format(NeedStringFormat,mContentData);

            canvas.drawText(mContentData
                    ,AreaThreePaddingX+AreaThreeX
                    ,NeedTitleTextSize+AreaThreePaddingY*2+NeedTextSize+(i*(NeedTextSize+AreaThreeTextPadding))
                    ,mPaint);
        }

    }

    private static void drawGoodThings(PersonaData mData,Canvas canvas,Paint mPaint){
        int AreaFourX = 0;
        int AreaFourY = canvas.getHeight()/2;
        int AreaFourWidth = canvas.getWidth()*3/8;
        int AreaFourHeight = canvas.getHeight()/2;

        int GoodThingsTitleTextSize;

        int GoodThingsTextSize;

        GoodThingsTitleTextSize = (AreaFourWidth-AreaFourPaddingX)/8;
        GoodThingsTextSize = (AreaFourWidth-AreaFourPaddingX)/20;

        //畫標題
        mPaint.setTextSize(GoodThingsTitleTextSize);
        mPaint.setColor(TitleTextColor);
        canvas.drawText("ideal features"
                ,AreaFourPaddingX+AreaFourX
                ,AreaFourY+GoodThingsTitleTextSize+AreaFourPaddingY
                ,mPaint);

        //畫內容
        mPaint.setTextSize(GoodThingsTextSize);
        mPaint.setColor(Color.BLACK);


        String GoodThingsStringFormat = "• %s";
        String[] GoodThings = mData.getGoodThings();

        for (int i = 0;i<GoodThings.length;i++){

            String mContentData = GoodThings[i];
            //如果資料為空就跳過不畫
            if (TextUtils.isEmpty(mContentData)) continue;

            mContentData = String.format(GoodThingsStringFormat,mContentData);

            canvas.drawText(mContentData
                    ,AreaFourPaddingX+AreaFourX
                    ,AreaFourY+GoodThingsTitleTextSize+AreaFourPaddingY*2+GoodThingsTextSize+(i*(GoodThingsTextSize+AreaFourTextPadding))
                    ,mPaint);
        }
    }

    private static void drawBadThings(PersonaData mData,Canvas canvas,Paint mPaint){
        int AreaFiveX = canvas.getWidth()*3/8;
        int AreaFiveY = canvas.getHeight()/2;
        int AreaFiveWidth = canvas.getWidth()*3/8;
        int AreaFiveHeight = canvas.getHeight()/2;

        int BadThingsTitleTextSize;

        int BadThingsTextSize;

        BadThingsTitleTextSize = (AreaFiveWidth-AreaFivePaddingX)/8;
        BadThingsTextSize = (AreaFiveWidth-AreaFivePaddingX)/20;

        //畫標題
        mPaint.setTextSize(BadThingsTitleTextSize);
        mPaint.setColor(TitleTextColor);
        canvas.drawText("Frustration"
                ,AreaFivePaddingX+AreaFiveX
                ,AreaFiveY+BadThingsTitleTextSize+AreaFivePaddingY
                ,mPaint);

        //畫內容
        mPaint.setTextSize(BadThingsTextSize);
        mPaint.setColor(Color.BLACK);


        String BadThingsStringFormat = "• %s";
        String[] BadThings = mData.getBadThings();

        for (int i = 0;i<BadThings.length;i++){

            String mContentData = BadThings[i];
            //如果資料為空就跳過不畫
            if (TextUtils.isEmpty(mContentData)) continue;

            mContentData = String.format(BadThingsStringFormat,mContentData);

            canvas.drawText(mContentData
                    ,AreaFivePaddingX+AreaFiveX
                    ,AreaFiveY+BadThingsTitleTextSize+AreaFivePaddingY*2+BadThingsTextSize+(i*(BadThingsTextSize+AreaFiveTextPadding))
                    ,mPaint);
        }
    }

    private static void drawAttr(PersonaData mData,Canvas canvas,Paint mPaint){
        Bitmap attr = mData.getAttr();
        if (attr!=null){
            int width = canvas.getWidth()/4;
            int height = canvas.getHeight()/2;
            //attr = Bitmap.createScaledBitmap(attr,width,height,true);
            attr = createScaledBitmap(attr,width,height,ScalingLogic.FIT);
            canvas.drawBitmap(attr,canvas.getWidth()*6/8,canvas.getHeight()/2,mPaint);
            //canvas.drawBitmap(attr,0,0,mPaint);
        }else{
            Log.d("","Attr==null");
        }
    }


    /**
     * 自動斷行
     * @param mString
     * @param number 幾個字一行
     * @return
     */
    private static String[] spiltString(String mString,int number){
        int stringLength = mString.length();

        int LineCounter = stringLength%number==0 ? stringLength/number : stringLength/number+1;

        String[] result = new String[LineCounter];

        for (int i = 0;i < LineCounter;i++){

            if (stringLength%number!=0 && i == LineCounter-1){

                result[i] = mString.substring(i*number);
                continue;
            }


            result[i] = mString.substring(i*number,i*number+number-1);



        }

        return result;

    }
}
