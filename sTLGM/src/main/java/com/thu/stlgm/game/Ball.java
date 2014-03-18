package com.thu.stlgm.game;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thu.stlgm.R;
import com.thu.stlgm.anim.AnimUtils;
import com.thu.stlgm.component.BallView;
import com.thu.stlgm.component.BookLayout;
import com.thu.stlgm.component.BookView;
import com.thu.stlgm.component.MoveImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/21.
 */
@EFragment(R.layout.fragment_ball)
public class Ball extends BaseGame{

    private static final String TAG = Ball.class.getName();

    @ViewById
    RelativeLayout container;


    private BallView mBallView;

    @ViewById
    ImageView Start;

    @ViewById
    ImageView Reciprocal;

    //@ViewById
    //ImageView book1,book2,book3,book4,book5;

    @ViewById
    BookLayout BookLayout;

    @ViewById
    ImageView centerIcon;

    private boolean[] answer = new boolean[]{true,true,true,false,false,true};
    private boolean[] userAnswer = new boolean[5];

    private int bookdrawables[][] = new int[][]
            {{R.drawable.book1,R.drawable.book1_g,R.drawable.book1_r},
            {R.drawable.book2,R.drawable.book2_g,R.drawable.book2_r},
            {R.drawable.book3,R.drawable.book3_g,R.drawable.book3_r},
            {R.drawable.book4,R.drawable.book4_g,R.drawable.book4_r},
            {R.drawable.book5,R.drawable.book5_g,R.drawable.book5_r},};

    private int type1Drawale[][] = new int[][]
            {{R.drawable.hp,R.drawable.ebay,R.drawable.google,R.drawable.intel,R.drawable.youtube},
            {R.drawable.amazon,R.drawable.intel,R.drawable.hp,R.drawable.apple,R.drawable.youtube},
            {R.drawable.google,R.drawable.apple,R.drawable.amazon,R.drawable.microsoft,R.drawable.youtube},
            {R.drawable.ebay,R.drawable.hp,R.drawable.fb,R.drawable.intel,R.drawable.youtube},};

    private int type1Logo[] = new int[]{
            R.drawable.larryandsergey,R.drawable.jobs,R.drawable.bill,R.drawable.mark};

    private boolean type1answer[][] = new boolean[][]
            {{false,false,true,false,false},
            {false,false,false,true,false},
            {false,false,false,true,false},
            {false,false,true,false,false},};

    @ViewById
    FrameLayout StartFrame;

    @AnimationRes
    Animation push_right_to_left;

    private int ballview = R.drawable.ball2;

    private int Type;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Init();

    }

    public void setupType(int type){
        this.Type = type;

        switch (type){
            case 0:

                ballview = R.drawable.ball2;

                break;
            case 1:

                ballview = R.drawable.ball3;

                break;
        }
    }

    void Init(){
        mHandler = new Handler();

        setupStart();


        ViewTreeObserver observer= container.getViewTreeObserver();

        if (observer!=null){
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    setupBallView();

                    ViewTreeObserver obs = container.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
    }

    private void setupBallView(){
        mBallView = new BallView(getActivity());
        mBallView.setImageResource(ballview);
        int BallWidth = 100;
        int BallHeight = 100;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BallWidth,BallHeight);
        params.leftMargin = container.getWidth()/2-BallWidth/2;
        params.topMargin = container.getHeight()/2-BallHeight/2;
        container.addView(mBallView,params);

        StartFrame.bringToFront();

        mBallView.setImageResource(R.anim.ball2);
        AnimationDrawable frameAnimation =    (AnimationDrawable)mBallView.getDrawable();
        frameAnimation.setCallback(mBallView);
        frameAnimation.setVisible(true, true);

        frameAnimation.start();



        mBallView.setListener(new BallView.OnObjectTouchEvent() {
            @Override
            public void OnObjectTouchEvent(BookView mView) {
                if (mView.getQuestionAnswer()){
                    mView.setBackgroundResource(R.drawable.green);
                    OnGameOver(1);
                }else{
                    OnGameOver(0);
                }
            }

            @Override
            public void OnObjectOutsideEvent(BookView mView) {

            }
        });

        setupBookLayout();
        /*
        mBallView.addViewList(book1);
        mBallView.addViewList(book2);
        mBallView.addViewList(book3);
        mBallView.addViewList(book4);
        mBallView.addViewList(book5);

        mBallView.setListener(new BallView.OnObjectTouchEvent() {
            @Override
            public void OnObjectTouchEvent(View mView) {
                int index = Integer.valueOf((String)mView.getTag());

                if (answer[index-1]){
                    ((ImageView)mView).setImageResource(bookdrawables[index - 1][1]);
                    userAnswer[index-1] = true;
                    if (checkAnswer()){
                        playVictory();
                    }
                }else{
                    ((ImageView)mView).setImageResource(bookdrawables[index - 1][2]);

                    if (!resetAnswering){
                        resetAnswering = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetAnswering = false;
                                resetAnswer();
                            }
                        },1000);
                    }
                }
            }

            @Override
            public void OnObjectOutsideEvent(View mView) {
            }
        });
        */

    }

    private void OnGameOver(final int score){
        GameOver(score);

        /*
        playVictory();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GameOver(score);
            }
        },2000);
        */
    }

    protected void GameOver(int score){

        mBallView.stop();
        super.GameOver(OverType.Dead,score);

    }

    private void setupBookLayout(){
        List<BookView> mBooks = new ArrayList<BookView>();

        Log.d(TAG,"Round:"+getRound());


        for (int i=0;i<type1Drawale[getRound()].length;i++){
            BookView book = new BookView(getActivity());
            book.setTag(i);
            book.setBookCover(type1Drawale[getRound()][i]);
            book.setQuestionAnswer(type1answer[getRound()][i]);
            mBooks.add(book);
            BookLayout.addView(book);
        }


        centerIcon.setImageResource(type1Logo[getRound()]);

        mBallView.setBookViewList(mBooks);
    }

    private void setupStart(){
        Reciprocal.setImageResource(R.anim.reciprocal);
        AnimationDrawable frameAnimation =    (AnimationDrawable)Reciprocal.getDrawable();

        if (frameAnimation!=null){
            frameAnimation.setCallback(Reciprocal);
            frameAnimation.setVisible(true, true);

            frameAnimation.start();

            int iDuration = 0;

            for (int i = 0; i < frameAnimation.getNumberOfFrames(); i++) {
                iDuration += frameAnimation.getDuration(i);
            }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Reciprocal.setVisibility(View.GONE);
                    Start.startAnimation(push_right_to_left);
                }
            },iDuration);
        }

        push_right_to_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBallView.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private boolean resetAnswering = false;
    private void resetAnswer(){
        userAnswer = new boolean[5];

        /*
        List<ImageView> imageViewList = new ArrayList<ImageView>();
        imageViewList.add(book1);
        imageViewList.add(book2);
        imageViewList.add(book3);
        imageViewList.add(book4);
        imageViewList.add(book5);

        int counter = 0;
        for (ImageView view:imageViewList){
            view.setImageResource(bookdrawables[counter][0]);
            counter++;
        }
        */
    }

    private void playVictory(){
        View Victory = getActivity().findViewById(R.id.Victory);
        if (Victory!=null)
            Victory.setVisibility(View.VISIBLE);
        mBallView.stop();
    }

    private boolean checkAnswer(){

        return Arrays.equals(userAnswer, answer);
    }

    @Click
    void reset(){
        mBallView.stop();
        if (getActivity()!=null){
            getActivity().findViewById(R.id.Victory).setVisibility(View.GONE);
        }
        Reciprocal.setVisibility(View.VISIBLE);
        setupStart();
        resetAnswer();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mBallView.getLayoutParams();
        if (params!=null){
            params.leftMargin = container.getWidth()/2-mBallView.getWidth()/2;
            params.topMargin = container.getHeight()/2-mBallView.getHeight()/2;
            mBallView.setLayoutParams(params);
        }
    }

    @Override
    protected void RestartFragment(int quizid,int counter, int container, FragmentManager manager, OnGameOverListener onGameOverListener) {

        Ball_ ball_ = new Ball_();
        ball_.setupType(quizid);

        addFragment(ball_,counter,container,manager,onGameOverListener);
    }
}

