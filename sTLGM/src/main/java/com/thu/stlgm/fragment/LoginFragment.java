package com.thu.stlgm.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.GameActivity_;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.component.FacebookLoginButton;
import com.thu.stlgm.facebook.FBMultiAccountMgr;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment implements FBMultiAccountMgr.FBEventListener {

    private static final String TAG = LoginFragment.class.getName();

    private FBMultiAccountMgr mFBMultiAccountMgr;

    @ViewById
    FacebookLoginButton FBLoginButton;

    @ViewById(R.id.GifImageView)
    ImageView gifImageView;

    @ViewById
    ImageView scanning_light;

    @AnimationRes
    Animation push_down_to_top;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    @Override
    public void OnLoginFinish(AccountBean mAccount) {
        Intent mIntent = new Intent(getActivity(), GameActivity_.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(GameActivity.FIRSTACCOUNT,mAccount);

        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    private void init(){
        mFBMultiAccountMgr = new FBMultiAccountMgr(getActivity());
        mFBMultiAccountMgr.setListener(this);

        scanning_light.startAnimation(push_down_to_top);

        gifImageView.setImageResource(R.anim.ironman);
        AnimationDrawable frameAnimation =    (AnimationDrawable)gifImageView.getDrawable();
        frameAnimation.setCallback(gifImageView);
        frameAnimation.setVisible(true, true);

        frameAnimation.start();
    }

    @Click(R.id.FBLoginButton)
    void Login(){
        mFBMultiAccountMgr.Login();
    }

    @Click
    void Reset(){
        FBLoginButton.reStartAnim();
    }
}
