package com.thu.stlgm.game.gallifrey.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.thu.stlgm.R;
import com.thu.stlgm.game.gallifrey.bean.BaseChess;
import com.thu.stlgm.game.gallifrey.bean.Bomb;
import com.thu.stlgm.game.gallifrey.model.Explosion;


/**
 * Created by SemonCat on 2014/3/19.
 */
public class ChessView extends ImageView{

    private static final String TAG = ChessView.class.getName();

    private BaseChess mBaseChess;

    private Drawable mBackupDrawable;

    private int mTouchCounter;

    private Drawable mBombEffect;
    private Drawable mTargetBombEffect;

    private Handler mHandler;

    private boolean IsBombArea;

    private static final int EXPLOSION_SIZE = 100;

    private Explosion explosion;

    private Drawable mBackupImageDrawable;

    private Drawable mQuestionDrawable;

    public ChessView(Context context,BaseChess baseChess) {
        super(context);
        this.mBaseChess = baseChess;
        init();
    }

    private void init(){

       mBombEffect = getResources().getDrawable(R.drawable.obj_m_framelock);

       mHandler = new Handler();

       setBackgroundResource(R.drawable.obj_m_frame);
       setBombEffect(mBombEffect);
       setTargetBombEffect(getResources().getDrawable(R.drawable.obj_m_framelockold));
       setImageDrawable(null);

       switch (mBaseChess.getType()){
           case EMPTY:
               TypeEmpty();
               break;
           case BOMB:
               TypeBomb();
               break;
           case BLANK:
               TypeBlank();
               break;
           case STAR:
               TypeStar();
               break;
       }
    }

    public void setBombArea(){
        if (getBackground()==null) return;

        restoreBomb();

        Drawable mBackgroundDrawable = getBackground();
        LayerDrawable mBombEffectLayerDrawable = new LayerDrawable(new Drawable[]{
                mBackgroundDrawable,mBombEffect
        });

        //備份原本的圖案
        mBackupDrawable = mBackgroundDrawable;
        setBackgroundDrawable(mBombEffectLayerDrawable);
        IsBombArea = true;
    }

    public void setTargetBombArea(){
        if (getBackground()==null) return;

        restoreBomb();

        Drawable mBackgroundDrawable = getBackground();
        LayerDrawable mBombEffectLayerDrawable = new LayerDrawable(new Drawable[]{
                mBackgroundDrawable,mTargetBombEffect
        });

        //備份原本的圖案
        mBackupDrawable = mBackgroundDrawable;
        setBackgroundDrawable(mBombEffectLayerDrawable);
        IsBombArea = true;
    }

    public void setBombEffect(Drawable mDrawable){
        this.mBombEffect = mDrawable;
    }

    public void setTargetBombEffect(Drawable targetBombEffect){
        this.mTargetBombEffect = targetBombEffect;
    }

    public void addQuestion(Drawable mQuestion){
        this.mQuestionDrawable = mQuestion;
        /*
        if (mBackupImageDrawable!=null){
            setImageDrawable(mBackupImageDrawable);
        }
        */

        mBackupDrawable = getDrawable();

        LayerDrawable mQuestionLayerDrawable = new LayerDrawable(new Drawable[]{
                mBackupDrawable,mQuestion
        });

        setImageDrawable(mQuestionLayerDrawable);
    }

    public void resetQusetion(){
        if (mBackupImageDrawable!=null){
            setImageDrawable(mBackupImageDrawable);
        }
    }

    public void restoreBomb(){
        IsBombArea = false;
    }

    private void TypeBomb(){
        Bomb mBomb = (Bomb)getBaseChess();

        switch (mBomb.getBombType()){
            case Left:
                setImageResource(R.drawable.obj_m_planetbomb01);
                break;
            case Right:
                setImageResource(R.drawable.obj_m_planetbomb03);
                break;
            case Straight:
                setImageResource(R.drawable.obj_m_planetbomb02);
                break;
        }

    }

    private void TypeStar(){

        setImageResource(R.drawable.obj_m_star);

        if (mQuestionDrawable!=null){

            addQuestion(mQuestionDrawable);
        }
    }


    private void TypeBlank(){

        setVisibility(View.GONE);
    }

    private void TypeEmpty(){

        setImageBitmap(null);
        //setBackgroundColor(Color.GREEN);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (explosion != null) {
            explosion.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    public void Explosion(){

        Explosion(getWidth()/2,getHeight()/2);
    }

    public void Explosion(int x,int y){

        if (explosion == null || explosion.getState() == Explosion.STATE_DEAD) {

            explosion = new Explosion(EXPLOSION_SIZE, x, y);
            ValueAnimator Explosion = ObjectAnimator.ofInt(0,100);
            Explosion.setDuration(2000);
            Explosion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    invalidate();
                    if (explosion != null && explosion.isAlive()) {
                        explosion.update();
                    }
                }
            });
            Explosion.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    explosion.setState(com.thu.stlgm.game.gallifrey.model.Explosion.STATE_DEAD);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            Explosion.start();
        }

    }

    public BaseChess getBaseChess() {
        return mBaseChess;
    }

    public void setBaseChess(BaseChess baseChess){
        this.mBaseChess = baseChess;
        init();
    }

    public void cleanQusetion(){
        mQuestionDrawable = null;
    }

    public int getTouchCounter() {
        return mTouchCounter;
    }

    public void addTouchCounter(){
        this.mTouchCounter += 1;
    }

    public void resetTouchCounter(){
        this.mTouchCounter = 0;
    }

    public void setTouchCounter(int mTouchCounter) {
        this.mTouchCounter = mTouchCounter;
    }

    public boolean IsBombArea() {
        return IsBombArea;
    }
}
