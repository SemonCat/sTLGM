package com.thu.stlgm.game;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.util.Log;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.fragment.BaseFragment;
import com.thu.stlgm.fragment.BeforeGameFragment;
import com.thu.stlgm.fragment.BeforeGameFragment_;

/**
 * Created by SemonCat on 2014/3/6.
 */
public class GameMgr {

    private static final String TAG = GameMgr.class.getName();

    public interface OnGameFinishListener {
        void OnGameStartEvent();

        void OnGameNextEvent();

        void OnGameOverEvent();
    }

    private GameActivity mActivity;
    private int mContainer;
    private Handler mHandler;

    private int timeout = 5 * 1000;

    private int counter = 1;

    private OnGameFinishListener mListener;

    public GameMgr(GameActivity activity, int containerId) {
        this.mActivity = activity;
        this.mContainer = containerId;
        mHandler = new Handler();
    }

    public void PlayGame(final int quizId, final BaseGame game, final String tag, final int max_counter) {


        final BeforeGameFragment_ baseFragment = new BeforeGameFragment_();
        checkContentResource(quizId,baseFragment);

        baseFragment.setFragmentFinishListener(new BaseGame.OnFragmentFinishListener() {
            @Override
            public void OnFragmentFinishEvent(BaseGame fragment) {
                Log.d(TAG, "OnFragmentFinishEvent");

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null)
                            mListener.OnGameStartEvent();
                        replaceFragment(game);
                    }
                }, 100);

            }
        });

        game.setOnGameOverListener(new BaseGame.OnGameOverListener() {
            @Override
            public void OnGameOverEvent(int score) {
                game.finishFragment();


                if (counter < max_counter) {
                    game.setRound(counter);
                    counter++;

                    PlayGame(quizId, game, tag, max_counter);
                    if (mListener != null)
                        mListener.OnGameNextEvent();
                } else {
                    if (mListener != null)
                        mListener.OnGameOverEvent();
                }


            }
        });

        replaceFragment(baseFragment);


    }

    private void checkContentResource(int quizId, BeforeGameFragment_ baseFragment) {
        Log.d(TAG,"counter:"+counter);
        int resource;
        if (quizId == 0 && counter == 1) {
            resource = MissionContentMgr.getMissionContent(quizId);
            baseFragment.setupMissionContent(resource);


        } else if (counter == 1) {
            resource = MissionContentMgr.getMissionContent(quizId);
            baseFragment.setupMissionContent(resource);
        } else {
            Log.d(TAG, "getNormal");
            resource = MissionContentMgr.getNormal();
            baseFragment.setupMissionContent(resource);
        }
    }

    public void resetCounter() {
        this.counter = 1;
    }


    private void replaceFragment(Fragment fragment) {
        mActivity.getFragmentManager().beginTransaction()
                .replace(mContainer, fragment)
                .commit();
    }

    public void replaceFragment(final Fragment mFragment, final String TAG) {

        FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
        Fragment findFragment = mActivity.getFragmentManager().findFragmentByTag(TAG);
        if (findFragment != null) {
            return;
        }
        transaction.replace(mContainer, mFragment, TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();


    }

    public void setListener(OnGameFinishListener mListener) {
        this.mListener = mListener;
    }
}
