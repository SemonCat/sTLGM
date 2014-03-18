package com.thu.stlgm.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;

import com.thu.stlgm.R;
import com.thu.stlgm.game.BaseGame;
import com.thu.stlgm.game.Medicine;
import com.thu.stlgm.game.Medicine_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/2.
 */
public class GameFragmentMgr implements BaseGame.OnFragmentFinishListener{

    private Activity mActivity;

    private List<BaseGame> mFragmentList;
    private List<Medicine_> mMedicineList;

    private int mFragmentContent;

    private Handler mHandler;

    //間隔時間
    private long timeout = 3000;

    //藥物失效時間
    private long medicineTimeout = 15*1000;

    public GameFragmentMgr(Activity mActivity,int fragmentContent) {
        this.mActivity = mActivity;
        mHandler = new Handler();
        this.mFragmentContent = fragmentContent;
        mFragmentList= new ArrayList<BaseGame>();
        mMedicineList = new ArrayList<Medicine_>();
    }

    public void addFragmentQueue(BaseGame fragment){
        fragment.setFragmentFinishListener(this);
        mFragmentList.add(fragment);
        replaceFragment();
    }

    public void addMedicineQueue(final Medicine_ medicine){
        medicine.setFragmentFinishListener(this);
        mMedicineList.add(medicine);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (mMedicineList){
                    mMedicineList.remove(medicine);
                }
            }
        },medicineTimeout);

    }

    public Medicine_ getMedicineFromQueue(String sid){
        synchronized (mMedicineList){
            for (Medicine_ medicine_ : mMedicineList){
                if (medicine_.getTargetSid().equals(sid)){
                    return medicine_;
                }
            }
        }

        return null;
    }

    public void replaceFragment(){
        if (!mFragmentList.isEmpty() && mActivity!=null){
            FragmentTransaction transaction =mActivity.getFragmentManager().beginTransaction();
            transaction.replace(mFragmentContent, mFragmentList.get(0));

            transaction.commit();
        }
    }

    public void replaceFragment(Fragment fragment){

        if (mActivity==null) return;

        FragmentTransaction transaction =mActivity.getFragmentManager().beginTransaction();
        Fragment findFragment = mActivity.getFragmentManager().findFragmentByTag(fragment.getClass().getName());
        if (findFragment!=null){
            return;
        }
        transaction.replace(R.id.GameContent, fragment, fragment.getClass().getName());

        transaction.commitAllowingStateLoss();
    }

    @Override
    public void OnFragmentFinishEvent(BaseGame fragment) {

        mFragmentList.remove(fragment);
        mMedicineList.remove(fragment);

        /*
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                replace();
            }
        },timeout);
        */
    }
}
