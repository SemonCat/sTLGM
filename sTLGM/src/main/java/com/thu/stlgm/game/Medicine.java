package com.thu.stlgm.game;

import android.app.Fragment;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.component.MoveImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

/**
 * Created by SemonCat on 2014/2/20.
 */
@EFragment(R.layout.fragment_medicine)
public class Medicine extends Fragment{

    @ViewById
    RelativeLayout container;

    @AnimationRes
    Animation fade_out_scale_big;

    private MoveImageView mMoveImageView;

    @AfterViews
    void Init(){
        setupMoveImageView();
    }

    private void setupMoveImageView(){
        mMoveImageView = new MoveImageView(getActivity());
        mMoveImageView.setImageResource(R.drawable.medicion_1_y);
        container.addView(mMoveImageView,new RelativeLayout.LayoutParams(100,100));


        final ViewTreeObserver observer= container.getViewTreeObserver();

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

        mMoveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoveImageView.stop();
                mMoveImageView.setVisibility(View.GONE);
                mMoveImageView.startAnimation(fade_out_scale_big);
                ((GameActivity)getActivity()).addBlood();
            }
        });
    }

    @Click
    void Reset(){
        mMoveImageView.setVisibility(View.VISIBLE);
        mMoveImageView.start();
        ((GameActivity)getActivity()).setBlood(60);
    }
}
