package com.thu.stlgm.game;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class GlowBallFragment extends BaseGame {

	private Context context;
	private BallsManager ballsManager;
	private Handler handler;
	private int screenHeight;
	private LinearInterpolator linearInterpolator;
	private boolean closeThread;
	private ImageView underLine;
    private ImageView underLine2;
	private TextView scoreTxt;
	private int currentColor;
	private ViewGroup gameMainLay;
    private int moveBallSpeed=3500;
    private TextView gotScoreText;

    private int picArrayTrue[]=new int[]{};
    private int picArrayFalse[]=new int[]{};

	private Button testBtn;

    private ViewGroup container;

    private int underline2 = R.drawable.checklinebar;
    private int containerBackground = R.drawable.bg8;
    private int animResource = R.anim.ckeckline;

    private int Type;

    private int maxScore = 5;

    public GlowBallFragment(){
        init(0);
    }

    public GlowBallFragment(int Type) {
        this.Type = Type;
        init(Type);
    }

    private void init(int Type){
        switch (Type){
            case 0:
                picArrayFalse=new int[]{R.drawable.opt_m23_f_01,R.drawable.opt_m23_f_02,
                        R.drawable.opt_m23_f_03,R.drawable.opt_m23_f_04};
                picArrayTrue=new int[]{R.drawable.opt_m23_t_01,R.drawable.opt_m23_t_02,
                        R.drawable.opt_m23_t_03};

                containerBackground = R.drawable.bkg_m_03;
                animResource = R.anim.obj_m_checkline01;
                underline2 = -1;

                break;
            case 1:

                picArrayFalse=new int[]{R.drawable.m2_4_f1,R.drawable.m2_4_f2,R.drawable.m2_4_f3,R.drawable.m2_4_f4};
                picArrayTrue=new int[]{R.drawable.m2_4_t1,R.drawable.m2_4_t2,R.drawable.m2_4_t3,R.drawable.m2_4_t4};

                containerBackground = R.drawable.bg9;
                animResource = R.anim.confirm;
                underline2 = R.drawable.checklinebar;





                break;
        }
    }

    @Override
    public void StartGame(){
        onGameStart();
    }

    @Override
	public void onPause() {
		super.onPause();
		closeThread = true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		linearInterpolator = new LinearInterpolator();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.game_fragment, container, false);
        container.setBackgroundResource(containerBackground);
		return container;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gameMainLay = (ViewGroup) getActivity().findViewById(R.id.game_main);

        //picArrayFalse=new int[]{R.drawable.item5,R.drawable.item6,R.drawable.item7,R.drawable.item8,
               // R.drawable.item9,R.drawable.item10,R.drawable.item11,R.drawable.item12,R.drawable.item13,
               // R.drawable.item14,R.drawable.item15,R.drawable.item16,R.drawable.item17};
        //picArrayTrue=new int[]{R.drawable.item1,R.drawable.item2,R.drawable.item3,R.drawable.item4};
        //picArrayTrue=new int[]{R.drawable.aerolite3};
        Log.d("TYPE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",String.valueOf(Type));
		ballsManager = new BallsManager(context, gameMainLay,picArrayFalse,picArrayTrue,Type);
		scoreTxt = (TextView) getActivity().findViewById(R.id.score);
		testBtn = (Button) getActivity().findViewById(R.id.button1);
		underLine = (ImageView) getActivity().findViewById(R.id.underLine);
        underLine2 = (ImageView) getActivity().findViewById(R.id.underLine2);
		underLine.setBackgroundResource(animResource);
        gotScoreText = (TextView)getActivity().findViewById(R.id.textView2);

        if (underline2!=-1){
            underLine2.setBackgroundResource(underline2);
        }
        ((AnimationDrawable) underLine.getBackground()).stop();


        testBtn.setVisibility(View.GONE);
        /*
		testBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction().replace(R.id.main_lay, new GlowBallFragment(0))
																		.remove(GlowBallFragment.this)
																		.commit();
				
				
			}
		});
        */


	}

    private void showEatEffort(){
        ((AnimationDrawable) underLine.getBackground()).stop();
        //underLine.setBackgroundResource(R.anim.confirm);

        ((AnimationDrawable) underLine.getBackground()).start();
    }

	@Override
	public void onResume() {
		//onGameStart();
		super.onResume();
	}

	private Runnable ballGenerator = new Runnable() {

		@Override
		public void run() {
			moveBall(ballsManager.getImageView());
			if (!closeThread)
				handler.postDelayed(ballGenerator, 1000);
		}
	};

	private void moveBall(final ImageView ball) {
		ValueAnimator va = ValueAnimator.ofInt(0, screenHeight
				- (130 + underLine.getHeight()));
		va.setDuration(moveBallSpeed);
        //if(moveBallSpeed>=2500){
           // moveBallSpeed-=50;
        //}
		va.setInterpolator(linearInterpolator);
		va.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

                BallObject mBallOBJ = (BallObject) ball.getTag();

				if (mBallOBJ.isTouch()) {
					animation.cancel();
				}
				RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) ball
						.getLayoutParams();
				rlp.setMargins(rlp.leftMargin,
						(Integer) animation.getAnimatedValue(),
						rlp.rightMargin, rlp.bottomMargin);
				ball.setLayoutParams(rlp);

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
				String s = (String) scoreTxt.getText();
				int score = Integer.parseInt(s);

                BallObject mBallOBJ = (BallObject) ball.getTag();

				if (mBallOBJ.isTouch()) {// Has been touched
					if (mBallOBJ.isAnswer()) {// the same color

                        if (!closeThread){
                            onGameOver(score,OverType.Dead);
                        }
                        //對的答案觸控
					} else {// not the same color
						//score += 5;
                        //錯的答案觸控
                        if(score<maxScore-1) {
                            score += 1;
                            showScore();
                        }
                        else {
                            if (!closeThread){
                                onGameOver(score,OverType.Win);
                            }
                        }
					}
				} else {// Has not been touched
					if (mBallOBJ.isAnswer()) {// the same color
						//score += 10;
                        //對的答案掉落
                        showEatEffort();
					} else {// not the same color
						//score += 1;
                        //錯的答案掉落
                        if (!closeThread){
                            onGameOver(score,OverType.Dead);
                        }
					}
				}



				scoreTxt.setText("" + score);


				ValueAnimator alpha = ObjectAnimator.ofFloat(ball, "alpha",
						1.0f, 0f);
				ValueAnimator scaleX = ObjectAnimator.ofFloat(ball, "scaleX",
						0f);
				ValueAnimator scaleY = ObjectAnimator.ofFloat(ball, "scaleY",
						0f);
				ValueAnimator rotate = ObjectAnimator.ofFloat(ball, "rotation",
						0f, 360f);
				AnimatorSet set = new AnimatorSet();
				set.setDuration(300);
				set.playTogether(alpha, scaleX, scaleY, rotate);
				set.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {

					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						ballsManager.RestoreView(ball);
					}

					@Override
					public void onAnimationCancel(Animator animation) {

					}
				});
				set.start();

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		va.start();
	}

	private void onGameStart() {
		closeThread = false;
		handler.post(ballGenerator);// must be at the bottom of the code
	}
    private void onGameOver(int score,OverType mType){
        //遊戲結束
        //Log.d("GameOver","GameOver!!!!!!!!!!!!!!!!!!!");
        closeThread = true;
        GameOver(mType,1);

    }
	private void onGameRestart() {
		closeThread = true;
		gameMainLay.removeAllViews();
		ballsManager.clearCachedImages();
		gameMainLay.addView(scoreTxt);
		gameMainLay.addView(underLine);
		gameMainLay.addView(testBtn);
		underLine.setBackgroundColor(currentColor = ballsManager.colorGenerator
				.getColor());
		scoreTxt.setText("0");
		//splash start
		
		onGameStart();
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

    @Override
    protected void RestartFragment(int quizid,int counter, int container, FragmentManager manager, OnGameOverListener onGameOverListener) {

        GlowBallFragment glowBallFragment = new GlowBallFragment();
        glowBallFragment.setOnGameOverListener(onGameOverListener);

        addFragment(glowBallFragment,counter,container,manager,onGameOverListener);

    }
}
