package com.thu.stlgm.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.GameActivity_;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.component.FacebookLoginButton;
import com.thu.stlgm.facebook.FBMultiAccountMgr;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment implements FBMultiAccountMgr.FBEventListener {

    private static final String TAG = LoginFragment.class.getName();

    private FBMultiAccountMgr mFBMultiAccountMgr;

    @ViewById
    FacebookLoginButton FBLoginButton;

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
