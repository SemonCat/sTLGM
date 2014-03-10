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


    private int counter = 0;

    private OnGameFinishListener mListener;


    public GameMgr(GameActivity activity, int containerId) {
        this.mActivity = activity;
        this.mContainer = containerId;
        mHandler = new Handler();
    }

    public void PlayGame(final int quizId, final BaseGame game, final String tag, final int max_counter) {


        final BeforeGameFragment_ beforeGameFragment = new BeforeGameFragment_();
        checkContentResource(quizId,beforeGameFragment);

        final BaseGame.OnGameOverListener onGameOverListener = new BaseGame.OnGameOverListener() {
            @Override
            public void OnGameOverEvent(int score) {

                if (counter < max_counter-1) {

                    counter++;

                    PlayGame(quizId, game, tag, max_counter);
                    if (mListener != null)
                        mListener.OnGameNextEvent();
                } else {
                    resetCounter();
                    if (mListener != null)
                        mListener.OnGameOverEvent();
                }


            }
        };

        game.setOnGameOverListener(onGameOverListener);

        beforeGameFragment.setFragmentFinishListener(new BaseGame.OnFragmentFinishListener() {
            @Override
            public void OnFragmentFinishEvent(BaseGame fragment) {
                Log.d(TAG, "OnFragmentFinishEvent");


                if (mListener != null){
                    mListener.OnGameStartEvent();
                }


                /*
                try{
                    replaceFragment(game.getClass().newInstance());
                }catch(InstantiationException mInstantiationException){

                }catch (IllegalAccessException mIllegalAccessException){

                }
                */

                game.RestartFragment(quizId,counter,mContainer,mActivity.getFragmentManager(),onGameOverListener);


            }
        });



        replaceFragment(beforeGameFragment);


    }

    private void checkContentResource(int quizId, BeforeGameFragment_ baseFragment) {
        Log.d(TAG,"counter:"+counter);
        int resource;
        if (quizId == 0 && counter == 0) {
            resource = MissionContentMgr.getMissionContent(quizId);
            baseFragment.setupMissionContent(resource);


        } else if (counter == 0) {
            resource = MissionContentMgr.getMissionContent(quizId);
            baseFragment.setupMissionContent(resource);
        } else {
            Log.d(TAG, "getNormal");
            resource = MissionContentMgr.getNormal();
            baseFragment.setupMissionContent(resource);
        }
    }

    public void resetCounter() {
        this.counter = 0;
    }


    private void replaceFragment(Fragment fragment) {


        mActivity.getFragmentManager().beginTransaction()
                .replace(mContainer, fragment)
                .commit();
        mActivity.getFragmentManager().executePendingTransactions();

        //mActivity.getFragmentManager().beginTransaction().add(mContainer,fragment).commit();
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
