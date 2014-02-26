package com.thu.stlgm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.GameActivity_;
import com.thu.stlgm.R;
import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.component.FacebookLoginButton;
import com.thu.stlgm.facebook.FBMultiAccountMgr;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.apache.http.Header;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment implements SQService.OnSQLoginFinish{

    private static final String TAG = LoginFragment.class.getName();

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

    private void init(){

        scanning_light.startAnimation(push_down_to_top);

        gifImageView.setImageResource(R.anim.ironman);
        AnimationDrawable frameAnimation =    (AnimationDrawable)gifImageView.getDrawable();
        frameAnimation.setCallback(gifImageView);
        frameAnimation.setVisible(true, true);

        frameAnimation.start();
    }

    @Click(R.id.FBLoginButton)
    void Login(){

        SQService.StudentLogin(getActivity(), this);

    }

    @Override
    public void OnSQLoginFinish(StudentBean mData) {
        Intent mIntent = new Intent(getActivity(), GameActivity_.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(GameActivity.FIRSTACCOUNT,mData);

        mIntent.putExtras(mBundle);

        startActivity(mIntent);
        getActivity().finish();
    }

    @Override
    public void OnSQLoginFail(String fid) {
        showToast("登入失敗，該帳號不存在於SQ資料庫中！");
    }

    @Override
    public void OnSQLoginNetworkError(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        showToast("網路錯誤，請尋求助教支援！");
    }

    @Click
    void Reset(){
        FBLoginButton.reStartAnim();
    }
}
