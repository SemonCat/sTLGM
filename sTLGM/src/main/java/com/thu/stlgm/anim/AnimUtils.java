package com.thu.stlgm.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.thu.stlgm.R;

/**
 * Created by SemonCat on 2014/2/11.
 */
public class AnimUtils {
    public interface OnAnimEndListener {
        void OnAnimEnd();
    }

    public static Animation getPushRightOutLeftIn(Context mContext, final View mView, final OnAnimEndListener mListener) {

        final Animation push_left_in = AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
        final Animation push_right_out = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);
        if (push_left_in != null && push_right_out != null) {
            push_right_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mView.startAnimation(push_left_in);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            push_left_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mListener != null)
                        mListener.OnAnimEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return push_right_out;

    }

    public static Animation getPushLeftOutRightIn(Context mContext, final View mView, final OnAnimEndListener mListener) {

        final Animation push_right_in = AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
        final Animation push_left_out = AnimationUtils.loadAnimation(mContext, R.anim.push_left_out);

        if (push_left_out != null && push_right_in != null) {
            push_left_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mView.startAnimation(push_right_in);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            push_right_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mListener != null)
                        mListener.OnAnimEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return push_left_out;
    }


}



