package com.thu.stlgm.game;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thu.stlgm.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FlippyBookFragment extends BaseGame {
    private Handler handler;
    private PillarsManager pillarsManager;
    private LinearInterpolator linearInterpolator;
    private ImageView book;
    private boolean fall = true;
    private ValueAnimator preValueAnimator;
    private int screenHeight;
    private int boxLeftMargin = 0;
    private int boxWidth = 0;
    private boolean inTheEnd = false;
    private boolean gameStart = false;
    private int pillarMoveSpeed = 3000;
    private TextView scoreText;
    private TextView gotScoreText;

    //圖片
    private int picArrayFalse[] = new int[]{};
    private int picArrayTrue[] = new int[]{};
    //分數
    private int score = 0;
    private int jumpRange = 200;

    private List<ValueAnimator> moveAnimList;

    private RelativeLayout mainLayout;

    private long lastTime = System.currentTimeMillis();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flippybook, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        handler = new Handler();
        linearInterpolator = new LinearInterpolator();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;

        picArrayFalse=new int[]{R.drawable.m1_4_f1,R.drawable.m1_4_f2,R.drawable.m1_4_f3,R.drawable.m1_4_f4};
        picArrayTrue=new int[]{R.drawable.m1_4_t1,R.drawable.m1_4_t2,R.drawable.m1_4_t3,R.drawable.m1_4_t4};
        book = (ImageView) getActivity().findViewById(R.id.flippyBook);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) book.getLayoutParams();
        boxLeftMargin = rlp.leftMargin;
        boxWidth = rlp.width;

        mainLayout = (RelativeLayout) getActivity().findViewById(R.id.flippybook_main_lay);

        mainLayout.setOnTouchListener(onTouchListener);

        pillarsManager = new PillarsManager(getActivity(), mainLayout,picArrayFalse,picArrayTrue);

        moveAnimList = new ArrayList<ValueAnimator>();


        scoreText=(TextView)getActivity().findViewById(R.id.textView);
        scoreText.setText(String.valueOf(score));
        gotScoreText=(TextView)getActivity().findViewById(R.id.textView2);
        gotScoreText.setVisibility(View.GONE);
        //gotScoreText.setTextColor(Color.GREEN);
        gotScoreText.setTextColor(Color.rgb(228,201,7));



    }


    private void movePillar(final ImageView v) {//this view must have been added to the RelativeLayout(Container)
        final RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        ValueAnimator va = ValueAnimator.ofInt(rlp.leftMargin, 0 - rlp.width);
        va.setDuration(pillarMoveSpeed);
        if(score>=40){
            pillarMoveSpeed=1200;
        }
        else if(score>=30){
            pillarMoveSpeed=1600;
        }
        else if(score>=20){
            pillarMoveSpeed=2000;
        }
        else if (score >= 10)
            pillarMoveSpeed = 2400;

        va.setInterpolator(linearInterpolator);
        va.addUpdateListener(new AnimatorUpdateListener() {
            int leftMargin = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftMargin = (Integer) animation.getAnimatedValue();
                if (leftMargin < boxLeftMargin + boxWidth && leftMargin > boxLeftMargin) {//must be checked conflict
                    RelativeLayout.LayoutParams bookrlp = (RelativeLayout.LayoutParams) book.getLayoutParams();
                    if (rlp.getRules()[RelativeLayout.ALIGN_PARENT_TOP] == -1) {//at top
                        if (bookrlp.topMargin <= rlp.height)
                            onCollision(Boolean.valueOf(v.getTag().toString()));
                    } else {//at bottom
                        if (bookrlp.topMargin + bookrlp.height >= screenHeight - (rlp.height))
                            onCollision(Boolean.valueOf(v.getTag().toString()));
                    }
                }
                else if (leftMargin<boxLeftMargin&&leftMargin+rlp.width>boxLeftMargin+book.getWidth()){
                    RelativeLayout.LayoutParams bookrlp = (RelativeLayout.LayoutParams) book.getLayoutParams();
                    if (rlp.getRules()[RelativeLayout.ALIGN_PARENT_TOP] == -1) {//at top
                        if (bookrlp.topMargin <= rlp.topMargin+rlp.height&&bookrlp.topMargin+bookrlp.height >= rlp.topMargin+rlp.height)
                            onCollision(Boolean.valueOf(v.getTag().toString()));
                    } else {//at bottom
                        if (bookrlp.topMargin+bookrlp.height>= screenHeight - (rlp.height)&&bookrlp.topMargin<= screenHeight - (rlp.height))
                            onCollision(Boolean.valueOf(v.getTag().toString()));
                    }
                }
                else if (leftMargin+rlp.width>boxLeftMargin&&leftMargin+rlp.width<boxLeftMargin+book.getWidth()){
                    RelativeLayout.LayoutParams bookrlp = (RelativeLayout.LayoutParams) book.getLayoutParams();
                    if (rlp.getRules()[RelativeLayout.ALIGN_PARENT_TOP] == -1) {//at top
                        if (bookrlp.topMargin <= rlp.topMargin+rlp.height&&bookrlp.topMargin+bookrlp.height >= rlp.topMargin+rlp.height)
                            onCollision(Boolean.valueOf(v.getTag().toString()));
                    } else {//at bottom
                        if (bookrlp.topMargin+bookrlp.height>= screenHeight - (rlp.height)&&bookrlp.topMargin<= screenHeight - (rlp.height))
                            onCollision(Boolean.valueOf(v.getTag().toString()));
                    }
                }
                rlp.setMargins(leftMargin, rlp.topMargin, rlp.rightMargin, rlp.bottomMargin);
                v.setLayoutParams(rlp);
            }
        });
        va.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pillarsManager.RestoreView(v);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationStart(Animator animation) {
            }
        });
        va.start();
        moveAnimList.add(va);
    }

    private void fallBook() {
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) book.getLayoutParams();
        if (rlp.topMargin < screenHeight - rlp.height){
            rlp.setMargins(rlp.leftMargin, rlp.topMargin += 10, rlp.rightMargin, rlp.bottomMargin);
        }else{
            if (!inTheEnd)
                onCollision(false);
        }
        book.setLayoutParams(rlp);
    }

    private Runnable pillarGeneratorRunnable = new Runnable() {

        @Override
        public void run() {
            if (!inTheEnd){
                movePillar(pillarsManager.getImageView(book));
            }
            handler.postDelayed(pillarGeneratorRunnable, 2200);

        }
    };

    private Runnable bookFallRunnable = new Runnable() {

        @Override
        public void run() {
            if (fall){
                fallBook();
            }
            handler.postDelayed(bookFallRunnable, 10);
        }

    };

    private void setBookColorChange() {
        book = (ImageView) getActivity().findViewById(R.id.flippyBook);
        ValueAnimator va = ValueAnimator.ofInt(50, 100, 50);
        va.setInterpolator(new DecelerateInterpolator());
        va.setDuration(2000);
        va.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                if (inTheEnd) {
                    animation.cancel();
                    return;
                }
                book.setBackgroundColor(val << 8 | 0xFF0000CC);

            }
        });
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.start();
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (inTheEnd) {
                    return false;
                }
                ;

                if (!gameStart) {
                    onGameStart();
                    gameStart = true;
                    return false;
                }

                fall = false;
                bookJump();
            }
            return false;
        }
    };

    private void bookJump() {

        final RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) book.getLayoutParams();


        if ((rlp.topMargin+book.getHeight())-jumpRange<0){
            fall = true;
            return;
        }

        ValueAnimator va = ValueAnimator.ofInt(rlp.topMargin, rlp.topMargin - jumpRange);
        va.setDuration(300);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rlp.setMargins(rlp.leftMargin, (Integer) animation.getAnimatedValue(), rlp.rightMargin, rlp.bottomMargin);
                book.setLayoutParams(rlp);

            }
        });
        va.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fall = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        if (preValueAnimator != null) {
            preValueAnimator.cancel();
            preValueAnimator = null;
        }
        preValueAnimator = va;
        va.start();
    }

    private void onGameStart() {
        ((AnimationDrawable)book.getBackground()).start();
        handler.post(pillarGeneratorRunnable);
        handler.post(bookFallRunnable);
        //setBookColorChange();
    }

    private void OnGameOver() {
        handler.removeCallbacks(pillarGeneratorRunnable);
        pauseAllMoveAnim();

        showGameOverEffect();

    }


    private void onCollision(boolean v) {
        if(v==false){
            inTheEnd = true;
            book.setImageResource(R.drawable.clumsydie);

            AnimationDrawable bookDrawable =  ((AnimationDrawable)book.getBackground());
            if (bookDrawable!=null){
                bookDrawable.stop();
            }


            book.setBackgroundDrawable(null);

            OnGameOver();
        }else {
            if(System.currentTimeMillis()-lastTime>1000){
                score++;
                scoreText.setText(String.valueOf(score));
                lastTime=System.currentTimeMillis();
                showScore();
            }
        }
    }

    private void pauseAllMoveAnim() {
        for (ValueAnimator Anim : moveAnimList) {
            if (Anim.isRunning()){
                Anim.cancel();
            }
        }

        moveAnimList.clear();
    }

    private void showGameOverEffect(){
        if (getActivity()==null) return;

        //閃爍
        final View mView = new View(getActivity());
        mainLayout.addView(mView,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mView.setBackgroundColor(Color.WHITE);

        int fadeinoutDuration = 500;
        float minAlpha = 0f;
        float maxAlpha = 1f;

        Animation fadeIn = new AlphaAnimation(minAlpha, maxAlpha);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(fadeinoutDuration/5);

        Animation fadeOut = new AlphaAnimation(maxAlpha, minAlpha);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(fadeinoutDuration/2);
        fadeOut.setDuration(fadeinoutDuration/2);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainLayout.removeView(mView);
                GameOver(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mView.startAnimation(animation);

    }

    private void showScore(){
        gotScoreText.setVisibility(View.VISIBLE);
        int fadeinoutDuration = 2000;
        float minAlpha = 0f;
        float maxAlpha = 1f;

        Animation fadeIn = new AlphaAnimation(minAlpha, maxAlpha);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(fadeinoutDuration/5);

        Animation fadeOut = new AlphaAnimation(maxAlpha, minAlpha);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(fadeinoutDuration/2);
        fadeOut.setDuration(fadeinoutDuration/2);


        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gotScoreText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        gotScoreText.startAnimation(animation);


    }

}
