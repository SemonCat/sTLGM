package com.thu.stlgm.fragment;

import com.thu.stlgm.R;
import com.thu.stlgm.component.FacebookLoginButton;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment{

    private static final String TAG = LoginFragment.class.getName();

    @ViewById
    FacebookLoginButton FBLoginButton;

    @Click
    void Reset(){
        FBLoginButton.reStartAnim();
    }
}
