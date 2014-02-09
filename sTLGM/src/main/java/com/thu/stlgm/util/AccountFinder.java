package com.thu.stlgm.util;

import android.accounts.Account;

import com.facebook.model.GraphUser;
import com.thu.stlgm.bean.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class AccountFinder {
    public List<AccountBean> findAllByGroup(AccountBean mAccount){
        List<AccountBean> mData = new ArrayList<AccountBean>();

        /**Fake Data**/
        for (int i=0;i<4;i++){
            AccountBean mFake = new AccountBean();
            mFake.setName("愛瘋"+i);
            mData.add(mFake);
        }

        if (mAccount!=null)
            mData.add(mAccount);


        return mData;
    }
}
