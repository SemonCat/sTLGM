package com.thu.stlgm.game;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.thu.stlgm.R;

/**
 * Created by SemonCat on 2014/3/2.
 */
public class BaseGame extends Fragment {

    public interface OnFragmentFinishListener {
        void OnFragmentFinishEvent(BaseGame fragment);
    }

    public interface OnGameOverListener {
        void OnGameOverEvent(int score);
    }

    private OnFragmentFinishListener mListener;

    private OnGameOverListener mOnGameOverListener;

    protected Handler mHandler;


    private ImageView mAnim;

    private int Round = 0;

    private Activity mActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mHandler = new Handler();
        mAnim = (ImageView) getActivity().findViewById(R.id.GameOverAnim);
        mAnim.setBackgroundResource(R.anim.gameover);
    }

    protected void startGame() {

    }

    public void setRound(int round) {
        this.Round = round;
    }

    public int getRound() {
        return Round;
    }


    protected void GameOver(final int score) {

        showGameOverAnim();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ((AnimationDrawable) mAnim.getBackground()).stop();
                mAnim.setVisibility(View.GONE);


                if (mActivity != null) {
                    finishFragment();
                    if (mOnGameOverListener != null)
                        mOnGameOverListener.OnGameOverEvent(score);
                }
            }
        }, 3000);

    }

    private void showGameOverAnim() {
        mAnim.setVisibility(View.VISIBLE);
        ((AnimationDrawable) mAnim.getBackground()).start();


    }

    protected void finishFragment() {


        mActivity.getFragmentManager().beginTransaction().remove(this).commit();
        mActivity.getFragmentManager().executePendingTransactions();

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

        manager.executePendingTransactions();
    }


    public void setOnGameOverListener(OnGameOverListener mOnGameOverListener) {
        this.mOnGameOverListener = mOnGameOverListener;
    }

    public void setFragmentFinishListener(OnFragmentFinishListener listener) {
        this.mListener = listener;
    }
}
