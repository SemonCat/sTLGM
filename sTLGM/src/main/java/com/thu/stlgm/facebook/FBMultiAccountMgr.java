package com.thu.stlgm.facebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.fragment.MainFragment;
import com.thu.stlgm.util.ConstantUtil;

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

    private FBEventListener mListener;

    public FBMultiAccountMgr(Context Context) {
        this.mContext = Context;

    }


    /**
     * 登入完成以後，索取使用者資料
     * @param session
     * @param sessionState
     * @param e
     */
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

                            mAccount.setName(mUser.getName());
                            mAccount.setId(mUser.getId());

                            Log.d(TAG,"Facebook UUID:"+mUser.getId());

                            //joinGroup(session,mUser.getId());

                            //登入程序正式完成。

                            if (mListener!=null)
                                mListener.OnLoginFinish(mAccount);



                        }
                    }).executeAsync();

        }
    }

    private void joinGroup(final Session session,String UID){
        if (session.isOpened()) {
            Log.d(TAG, "AccessToken:" + session.getAccessToken());

            Bundle mBundle = new Bundle();
            mBundle.putString("access_token",ConstantUtil.App_Access_Token);
            mBundle.putString("from", ConstantUtil.TeacherId);

            String Action = "/"+ ConstantUtil.GroupId+"/members/"+UID;

            Log.d(TAG,"Action:"+Action);

            new Request(session,
                    Action,
                    mBundle,
                    HttpMethod.POST,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {

                            Log.d(TAG, response.toString());



                        }
                    }).executeAsync();

        }
    }

    /**
     * 登入程序
     */
    public void Login(){
        //先清空所有快取與資料
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();
        if (Session.openActiveSessionFromCache(mContext)!=null)
            Session.openActiveSessionFromCache(mContext).closeAndClearTokenInformation();

        //準備登入Dialog
        WebDialog localWebDialog = new WebDialog
                .Builder(mContext, mContext.getString(R.string.app_id), "oauth", null)
                .build();


        //設定登入完成CallBack
        localWebDialog.setOnCompleteListener(new WebDialog.OnCompleteListener()
        {
            public void onComplete(Bundle bundle, FacebookException facebookException)
            {
                if (bundle!=null){
                    //登入成功
                    Session.getActiveSession();
                    AccessToken localAccessToken = AccessToken.
                            createFromExistingAccessToken(bundle.getString("access_token"), null, null, AccessTokenSource.WEB_VIEW, null);
                    //打開Session
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

    public Session SwitchAccount(AccountBean mAccount){
        Session mSession = openSessionByToken(mAccount.getAccessToken());
        return mSession;
    }

    private Session openSessionByToken(String token) {
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();

        AccessToken mAccessToken = AccessToken.createFromExistingAccessToken(token,
                null, null, AccessTokenSource.WEB_VIEW, null);
        Session mSession = Session.openActiveSessionWithAccessToken(mContext, mAccessToken, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                Log.d(TAG,"openSessionByToken");
                Log.d(TAG, "getState:" + session.getState());
                Log.d(TAG, "getPermissions:" + session.getPermissions());
            }
        });
        return mSession;
    }

    public void postPhoto(StudentBean mAccount,String GroupId,byte[] photo,final Request.OnProgressCallback mCallback) {
        Session mSession = SwitchAccount(mAccount);
        if (mSession != null && mSession.isOpened()) {

            Bundle mBundle = new Bundle();
            mBundle.putString("message", "第 "+mAccount.getGroupID()+" 組。");
            mBundle.putByteArray("source", photo);


            new Request(mSession,
                    "/"+GroupId+"/photos",
                    mBundle,
                    HttpMethod.POST,
                    new Request.OnProgressCallback() {
                        @Override
                        public void onProgress(long current, long max) {
                            if (mCallback!=null)
                                mCallback.onProgress(current,max);
                        }

                        @Override
                        public void onCompleted(Response response) {
                            if (mCallback!=null)
                                mCallback.onCompleted(response);
                            Log.d(TAG, response.toString());
                        }
                    }
            ).executeAsync();
        } else {
            Log.d(TAG, "mSessionIsNull:" + String.valueOf(mSession == null));
        }

    }

    public void likePhoto(StudentBean mAccount,long id,final Request.Callback mListener) {
        Session mSession = SwitchAccount(mAccount);
        if (mSession != null && mSession.isOpened()) {

            new Request(mSession,
                    "/"+id+"/likes",
                    null,
                    HttpMethod.POST,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {

                            if (response.getError()==null){


                            }else{
                                Log.d(TAG,"Get Error:"+response.getError().toString());
                            }

                            mListener.onCompleted(response);
                        }
                    }
            ).executeAsync();
        } else {
            Log.d(TAG, "mSessionIsNull:" + String.valueOf(mSession == null));
        }

    }



    public void setListener(FBEventListener mListener) {
        this.mListener = mListener;
    }

}
