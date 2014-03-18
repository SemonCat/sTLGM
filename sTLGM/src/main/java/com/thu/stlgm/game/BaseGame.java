package com.thu.stlgm.game;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.thu.stlgm.R;
import com.thu.stlgm.util.UAnimDrawable;

/**
 * Created by SemonCat on 2014/3/2.
 */
public class BaseGame extends Fragment {

    public interface OnFragmentFinishListener {
        void OnFragmentFinishEvent(BaseGame fragment);
    }

    public interface OnGameOverListener {
        void OnGameOverEvent(OverType mOverType,int score);
    }

    private OnFragmentFinishListener mListener;

    private OnGameOverListener mOnGameOverListener;

    protected Handler mHandler;


    private ImageView mStartAnim;

    private ImageView mAnim;

    private UAnimDrawable mUAnimDrawable;

    private int Round = 0;

    private Activity mActivity;

    private boolean PlayStartAnim = true;

    private boolean IsGameOver = false;

    public enum OverType{
        Win,
        Dead
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mHandler = new Handler();
        mAnim = (ImageView) getActivity().findViewById(R.id.GameOverAnim);

        if (PlayStartAnim){
            mStartAnim = (ImageView) getActivity().findViewById(R.id.GameStartAnim);
            mStartAnim.setVisibility(View.VISIBLE);
            Log.d("mStartAnim","Start");

            mUAnimDrawable = new UAnimDrawable((AnimationDrawable) getResources().getDrawable(
                    R.anim.obj_m_begin)) {
                @Override
                public void onAnimationFinish() {
                    mStartAnim.setVisibility(View.GONE);
                    StartGame();
                }
            };
            mStartAnim.setBackgroundDrawable(mUAnimDrawable);
            mUAnimDrawable.start();
        }
    }


    public void stopStartGameAnim(){
        this.PlayStartAnim = false;
    }


    protected void StartGame(){

    }

    public void setRound(int round) {
        this.Round = round;
    }

    public int getRound() {
        return Round;
    }


    protected void GameOver(final OverType mType,final int score) {

        if (IsGameOver) return;

        IsGameOver = true;

        showGameOverAnim(mType);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ((AnimationDrawable) mAnim.getBackground()).stop();
                mAnim.setVisibility(View.GONE);


                if (mActivity != null) {
                    AnimationDrawable animationDrawable = (AnimationDrawable) mAnim.getBackground();

                    if (animationDrawable!=null){
                        animationDrawable.stop();
                    }

                    mAnim.clearAnimation();



                    finishFragment();
                    if (mOnGameOverListener != null)
                        mOnGameOverListener.OnGameOverEvent(mType,score);
                }
            }
        }, 3000);

    }

    private void showGameOverAnim(final OverType mType) {

        if (getActivity()==null) return;

        FrameLayout main = (FrameLayout) getActivity().findViewById(R.id.GameContent);

        //閃爍
        final View mView = new View(getActivity());
        main.addView(mView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mView.setBackgroundColor(Color.BLACK);

        int fadeinoutDuration = 1000;

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(fadeinoutDuration);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mType == OverType.Dead){
                    mAnim.setBackgroundResource(R.anim.obj_s_gameovertext);
                }else if (mType == OverType.Win){
                    mAnim.setBackgroundResource(R.anim.obj_s_winner);
                }
                mAnim.setVisibility(View.VISIBLE);
                ((AnimationDrawable) mAnim.getBackground()).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mView.startAnimation(fadeIn);





    }

    protected void finishFragment() {


        mActivity.getFragmentManager().beginTransaction().remove(this).commit();
        //mActivity.getFragmentManager().executePendingTransactions();

        if (mListener != null)
            mListener.OnFragmentFinishEvent(this);

    }

    /**
     *
     * @param quizid
     * @param counter
     * @param container
     * @param manager
     * @param onGameOverListener
     */
    protected void RestartFragment(int quizid,int counter,int container,FragmentManager manager,OnGameOverListener onGameOverListener) {


    }

    protected void addFragment(BaseGame baseGame,int counter,int container, FragmentManager manager,OnGameOverListener listener){
        baseGame.setRound(counter);
        baseGame.setOnGameOverListener(listener);

        FragmentTransaction fragmentTransaction= manager.beginTransaction();

        fragmentTransaction.add(container,baseGame).commit();

        //manager.executePendingTransactions();
    }


    public void setOnGameOverListener(OnGameOverListener mOnGameOverListener) {
        this.mOnGameOverListener = mOnGameOverListener;
    }

    public void setFragmentFinishListener(OnFragmentFinishListener listener) {
        this.mListener = listener;
    }
}
