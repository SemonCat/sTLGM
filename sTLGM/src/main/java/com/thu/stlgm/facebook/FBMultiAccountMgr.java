package com.thu.stlgm.facebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.fragment.MainFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class FBMultiAccountMgr{
    public interface FBEventListener{
        void OnLoginFinish(AccountBean mAccount);
    }

    private static final String TAG = FBMultiAccountMgr.class.getName();

    private Context mContext;
    private Set<AccountBean> mAccountList;
    private FBEventListener mListener;

    public FBMultiAccountMgr(Context Context) {
        this.mContext = Context;
        mAccountList = new HashSet<AccountBean>();
    }


    private void SessionStatusCallback(final Session session, SessionState sessionState, Exception e){
        if (e != null)
            e.printStackTrace();

        Log.d(TAG, "getState:" + session.getState());
        if (session.isOpened()) {
            Log.d(TAG, "AccessToken:" + session.getAccessToken());

            new Request(session,
                    "/me",
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            Log.d(TAG, "Error:" + response.getError());
                            GraphUser mUser = response.getGraphObjectAs(GraphUser.class);

                            AccountBean mAccount = new AccountBean(session.getAccessToken());
                            mAccount.setGraphUser(mUser);

                            mAccountList.add(mAccount);

                            if (mListener!=null)
                                mListener.OnLoginFinish(mAccount);

                            Log.d(TAG,"Current Login User Counter:"+mAccountList.size());
                        }
                    }).executeAsync();

        }
    }

    public void Login(){
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();

        WebDialog localWebDialog = new WebDialog.Builder(mContext, mContext.getString(R.string.app_id), "oauth", null).build();
        localWebDialog.setOnCompleteListener(new WebDialog.OnCompleteListener()
        {
            public void onComplete(Bundle bundle, FacebookException facebookException)
            {
                if (bundle!=null){
                    Session.getActiveSession();
                    AccessToken localAccessToken = AccessToken.
                            createFromExistingAccessToken(bundle.getString("access_token"), null, null, AccessTokenSource.WEB_VIEW, null);
                    Session.openActiveSessionWithAccessToken(
                            mContext.getApplicationContext(),
                            localAccessToken,
                            new Session.StatusCallback() {
                                @Override
                                public void call(Session session, SessionState sessionState, Exception e) {
                                   SessionStatusCallback(session,sessionState,e);
                                }
                            });
                }else{
                    Log.d(TAG,"User Cancel Dialog");
                }

            }
        });
        localWebDialog.show();
    }

    public void SwitchAccount(AccountBean mAccount){
        openSessionByToken(mAccount.getAccessToken());
    }

    private void openSessionByToken(String token) {
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();

        AccessToken mAccessToken = AccessToken.createFromExistingAccessToken(token,
                null, null, AccessTokenSource.WEB_VIEW, null);
        Session.openActiveSessionWithAccessToken(mContext, mAccessToken, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                Log.d(TAG,"openSessionByToken");
                Log.d(TAG, "getState:" + session.getState());
                Log.d(TAG, "getPermissions:" + session.getPermissions());
            }
        });
    }

    public void setListener(FBEventListener mListener) {
        this.mListener = mListener;
    }

}
