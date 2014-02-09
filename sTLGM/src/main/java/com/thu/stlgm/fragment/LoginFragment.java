package com.thu.stlgm.fragment;

import android.os.Bundle;

import com.thu.stlgm.R;
import com.thu.stlgm.component.FacebookLoginButton;
import com.thu.stlgm.facebook.FBMultiAccountMgr;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment{

    private static final String TAG = LoginFragment.class.getName();

    private FBMultiAccountMgr mFBMultiAccountMgr;

    @ViewById
    FacebookLoginButton FBLoginButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init(){
        mFBMultiAccountMgr = new FBMultiAccountMgr(getActivity());
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
