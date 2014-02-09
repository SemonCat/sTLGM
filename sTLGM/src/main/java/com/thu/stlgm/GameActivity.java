package com.thu.stlgm;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.thu.stlgm.adapter.PlayerInfoAdapter;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.facebook.FBMultiAccountMgr;
import com.thu.stlgm.fragment.LoginFragment;
import com.thu.stlgm.util.AccountFinder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EActivity(R.layout.activity_game)
public class GameActivity extends BaseActivity{

    private static final String TAG = GameActivity.class.getName();

    public static final String FIRSTACCOUNT = "FirstAccount";

    private FBMultiAccountMgr mFBMultiAccountMgr;

    @ViewById
    ListView ListViewPlayerInfo;

    private PlayerInfoAdapter mPlayerInfoAdapter;

    @AfterViews
    void Init(){
        mFBMultiAccountMgr = new FBMultiAccountMgr(this);

        initAdapter();
    }


    private void initAdapter(){
        mPlayerInfoAdapter = new PlayerInfoAdapter(this,new AccountFinder().findAllByGroup(getAccount()));

        ListViewPlayerInfo.setAdapter(mPlayerInfoAdapter);
    }

    @ItemClick
    public void ListViewPlayerInfoItemClicked(final int position) {
        if (!mPlayerInfoAdapter.getItem(position).isLogin()){
            mFBMultiAccountMgr.setListener(new FBMultiAccountMgr.FBEventListener() {
                @Override
                public void OnLoginFinish(AccountBean mAccount) {
                    mPlayerInfoAdapter.refreshData(position,mAccount);
                }
            });
            mFBMultiAccountMgr.Login();
        }
    }

    private AccountBean getAccount(){
        Bundle mBundle = getIntent().getExtras();
        if (mBundle!=null){
            AccountBean mAccount = (AccountBean)mBundle.getSerializable(FIRSTACCOUNT);
            if (mAccount!=null)
                return mAccount;
            else
                return null;
        }else
            return null;
    }
}
