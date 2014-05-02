package com.thu.stlgm.game;

import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.thu.stlgm.R;
import com.thu.stlgm.component.MoveImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.Random;

/**
 * Created by SemonCat on 2014/2/20.
 */
@EFragment(R.layout.fragment_medicine)
public class Medicine extends BaseGame{
    public interface OnMedicineGetListener{
        void OnMedicineGetEvent(int reward);
    }

    @ViewById
    RelativeLayout container;

    @AnimationRes
    Animation fade_out_scale_big;

    private int mReward;

    private MoveImageView mMoveImageView;

    private OnMedicineGetListener mListener;

    private Handler mHandler;

    private long timeout = 20*1000;

    private String mTargetSid;

    private int[] Drawable = new int[]{R.drawable.grape,R.drawable.lemon,R.drawable.orange};

    @AfterViews
    void Init(){
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    finishFragment();
                }catch (Exception mException){

                }
            }
        },timeout);

        setupMoveImageView();
    }

    private void setupMoveImageView(){
        mMoveImageView = new MoveImageView(getActivity());


        mMoveImageView.setImageResource(Drawable[new Random().nextInt(Drawable.length)]);


        container.addView(mMoveImageView,new RelativeLayout.LayoutParams(100,100));


        final ViewTreeObserver observer= container.getViewTreeObserver();

        if (observer!=null){
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mMoveImageView.start();

                    ViewTreeObserver obs = container.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }

        mMoveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoveImageView.stop();
                mMoveImageView.setVisibility(View.GONE);
                mMoveImageView.startAnimation(fade_out_scale_big);

                if (mListener!=null)
                    mListener.OnMedicineGetEvent(mReward);

                finishFragment();
            }
        });
    }

    public void setListener(int reward,OnMedicineGetListener mListener) {
        this.mReward = reward;
        this.mListener = mListener;
    }

    public void setTargetSid(String sid){
        this.mTargetSid = sid;
    }

    public String getTargetSid(){
        return mTargetSid;
    }
}
