package com.thu.stlgm.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;



public class BookView extends ImageView {

    private boolean QuestionAnswer = false;

    private boolean Answered = false;

    private Drawable NormalBackground;
    private Drawable AnsweredBackground;

    public BookView(Context context) {
        super(context);
    }

    public void setBookCover(int resourceId){
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resourceId));
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



    public boolean getQuestionAnswer(){
        return QuestionAnswer;
    }

    public void setQuestionAnswer(boolean questionAnswer){
        this.QuestionAnswer = questionAnswer;
    }
}
