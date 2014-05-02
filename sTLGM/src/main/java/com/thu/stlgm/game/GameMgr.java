package com.thu.stlgm.game;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.util.Log;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.fragment.BeforeGameFragment_;

/**
 * Created by SemonCat on 2014/3/6.
 */
public class GameMgr {

    private static final String TAG = GameMgr.class.getName();

    public interface OnGameFinishListener {
        void OnGameStartEvent();

        void OnGameNextEvent(boolean IsWin,int round);

        void OnGameFinishEvent(boolean IsWin,int round);

        void OnGameOverEvent(boolean IsWin);
    }

    private GameActivity mActivity;
    private int mContainer;
    private Handler mHandler;

    private int timeout = 5 * 1000;


    private int counter = 0;

    private boolean IsRun = false;

    private OnGameFinishListener mListener;


    private boolean IsWin = false;

    public GameMgr(GameActivity activity, int containerId) {
        this.mActivity = activity;
        this.mContainer = containerId;
        mHandler = new Handler();
    }

    public void PlayGame(final int quizId, final BaseGame game, final String tag, final int max_counter) {
        IsRun = true;

        final BeforeGameFragment_ beforeGameFragment = new BeforeGameFragment_();
        beforeGameFragment.stopStartGameAnim();
        checkContentResource(quizId,beforeGameFragment);

        final BaseGame.OnGameOverListener onGameOverListener = new BaseGame.OnGameOverListener() {
            @Override
            public void OnGameOverEvent(BaseGame.OverType mType,int score) {

                if (mType== BaseGame.OverType.Win){ IsWin = true;}
                if (mListener != null)
                    mListener.OnGameFinishEvent(mType== BaseGame.OverType.Win,counter);

                if (counter < max_counter-1) {

                    counter++;

                    PlayGame(quizId, game, tag, max_counter);
                    if (mListener != null)
                        mListener.OnGameNextEvent(mType== BaseGame.OverType.Win,counter);
                } else {
                    resetCounter();
                    System.gc();
                    if (mListener != null)
                        mListener.OnGameOverEvent(IsWin);
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
        mActivity.HideCamera();

    }

    private void checkContentResource(int quizId, BeforeGameFragment_ baseFragment) {
        Log.d(TAG,"counter:"+counter);
        int resource;
        if (counter == 0) {
            resource = MissionContentMgr.getMissionContent(quizId);
            baseFragment.setupMissionContent(resource);
        } else {
            Log.d(TAG, "getNormal");
            //resource = MissionContentMgr.getNormal();
            baseFragment.ShowNormal();
        }
    }

    public void resetCounter() {
        this.counter = 0;
        IsRun = false;
    }


    private void replaceFragment(Fragment fragment) {
        mActivity.getFragmentManager().beginTransaction()
                .replace(mContainer, fragment)
                .commitAllowingStateLoss();


        //mActivity.getFragmentManager().

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

    public void setIsRun(boolean IsRun){
        this.IsRun = IsRun;
    }

    public boolean IsRun(){
        return IsRun;
    }

    public void setListener(OnGameFinishListener mListener) {
        this.mListener = mListener;
    }
}
