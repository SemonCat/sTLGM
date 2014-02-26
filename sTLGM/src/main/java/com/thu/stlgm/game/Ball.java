package com.thu.stlgm.game;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Build;
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
public class Ball extends Fragment{

    private static final String TAG = Ball.class.getName();

    @ViewById
    RelativeLayout container;


    private BallView mBallView;

    @ViewById
    ImageView Start;

    @ViewById
    ImageView Reciprocal;

    @ViewById
    ImageView book1,book2,book3,book4,book5;

    private boolean[] answer = new boolean[]{true,true,true,false,false};
    private boolean[] userAnswer = new boolean[5];

    private int bookdrawables[][] = new int[][]
            {{R.drawable.book1,R.drawable.book1_g,R.drawable.book1_r},
            {R.drawable.book2,R.drawable.book2_g,R.drawable.book2_r},
            {R.drawable.book3,R.drawable.book3_g,R.drawable.book3_r},
            {R.drawable.book4,R.drawable.book4_g,R.drawable.book4_r},
            {R.drawable.book5,R.drawable.book5_g,R.drawable.book5_r},};

    @ViewById
    FrameLayout StartFrame;

    @AnimationRes
    Animation push_right_to_left;

    private Handler mHandler;

    @AfterViews
    void Init(){
        mHandler = new Handler();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupStart();


        final ViewTreeObserver observer= container.getViewTreeObserver();

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

    private void setupBallView(){
        mBallView = new BallView(getActivity());
        mBallView.setImageResource(R.drawable.ball2);
        int BallWidth = 100;
        int BallHeight = 100;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BallWidth,BallHeight);
        params.leftMargin = container.getWidth()/2-BallWidth/2;
        params.topMargin = container.getHeight()/2-BallHeight/2;
        container.addView(mBallView,params);

        StartFrame.bringToFront();

        mBallView.setImageResource(R.anim.ball);
        AnimationDrawable frameAnimation =    (AnimationDrawable)mBallView.getDrawable();
        frameAnimation.setCallback(mBallView);
        frameAnimation.setVisible(true, true);

        frameAnimation.start();

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
                /*
                int index = Integer.valueOf((String)mView.getTag());
                ((ImageView)mView).setImageResource(bookdrawables[index - 1][0]);
                */
            }
        });


    }

    private void setupStart(){
        Reciprocal.setImageResource(R.anim.reciprocal);
        AnimationDrawable frameAnimation =    (AnimationDrawable)Reciprocal.getDrawable();

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
        getActivity().findViewById(R.id.Victory).setVisibility(View.GONE);
        Reciprocal.setVisibility(View.VISIBLE);
        setupStart();
        resetAnswer();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mBallView.getLayoutParams();
        params.leftMargin = container.getWidth()/2-mBallView.getWidth()/2;
        params.topMargin = container.getHeight()/2-mBallView.getHeight()/2;
        mBallView.setLayoutParams(params);
    }
}

