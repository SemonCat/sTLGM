package com.thu.stlgm.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


public class BookView extends ImageView {

    private int id;

    private boolean QuestionAnswer = false;

    private boolean Answered = false;

    private Drawable NormalBackground;
    private Drawable AnsweredBackground;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public BookView(Context context) {
        super(context);
    }

    public void setBookCover(int resourceId){
        //setImageBitmap(BitmapFactory.decodeResource(getResources(), resourceId));
        String imageUri = "drawable://" + resourceId;
        ImageLoader.getInstance().displayImage(imageUri, this,options);
    }

    public void setBookCover(Bitmap BookCover){
        setImageBitmap(BookCover);
    }

    public void setNormalBackground(int BookBackground){
        this.NormalBackground = getResources().getDrawable(BookBackground);
    }

    public void setNormalBackground(Drawable normalBackground){
        this.NormalBackground = normalBackground;
    }



    public void setAnsweredBackground(int BookBackground){
        this.AnsweredBackground = getResources().getDrawable(BookBackground);
    }

    public void setAnsweredBackground(Drawable BookBackground){
        this.AnsweredBackground = BookBackground;


    }

    public void setAnswered(boolean Answered){
        if (Answered){
            setBookBackground(AnsweredBackground);
        }else{
            setBookBackground(NormalBackground);
        }
    }

    private void setBookBackground(Drawable background){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
        }else{
            setBackgroundDrawable(background);
        }
    }


    public int getBookId() {
        return id;
    }

    public void setBookId(int id) {
        this.id = id;
    }

    public boolean getQuestionAnswer(){
        return QuestionAnswer;
    }

    public void setQuestionAnswer(boolean questionAnswer){
        this.QuestionAnswer = questionAnswer;
    }
}
