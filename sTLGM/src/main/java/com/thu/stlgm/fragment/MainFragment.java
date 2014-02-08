package com.thu.stlgm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.AccountBean;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by SemonCat on 2014/1/28.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements Session.StatusCallback {

    private static final String TAG = MainFragment.class.getName();

    AsyncHttpClient client = new AsyncHttpClient();

    private AccountBean[] mAccounts = new AccountBean[4];

    private Handler mHandler = new Handler();

    @ViewById
    TextView UserName1;

    @ViewById
    TextView UserName2;

    @ViewById
    TextView UserName3;

    @ViewById
    TextView UserName4;

    @ViewById
    Button doPost1;

    @ViewById
    Button doPost2;

    @ViewById
    Button doPost3;

    @ViewById
    Button doPost4;

    private TextView[] mUserNames;
    private Button[] mPosts;

    private int currectItem = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserNames = new TextView[]{UserName1, UserName2, UserName3, UserName4};
        mPosts = new Button[]{doPost1, doPost2, doPost3, doPost4};
    }

    @Click
    void doLogin1() {
        Log.d(TAG, "doLogin1");

        currectItem = 0;

        getSession();
    }

    @Click
    void doLogin2() {
        Log.d(TAG, "doLogin2");

        currectItem = 1;

        getSession();
    }

    @Click
    void doLogin3() {
        Log.d(TAG, "doLogin3");

        currectItem = 2;

        getSession();

    }

    @Click
    void doLogin4() {
        Log.d(TAG, "doLogin4");

        currectItem = 3;

        getSession();
    }

    @Click
    void doPost1() {
        currectItem = 0;
        openSessionByToken(mAccounts[currectItem].getAccessToken());

    }

    @Click
    void doPost2() {
        currectItem = 1;
        openSessionByToken(mAccounts[currectItem].getAccessToken());

    }

    @Click
    void doPost3() {
        currectItem = 2;
        openSessionByToken(mAccounts[currectItem].getAccessToken());

    }

    @Click
    void doPost4() {
        currectItem = 3;
        openSessionByToken(mAccounts[currectItem].getAccessToken());

    }

    @Override
    public void call(final Session session, SessionState sessionState, Exception e) {
        if (e != null)
            e.printStackTrace();

        Log.d(TAG, "getState:" + session.getState());
        if (session.isOpened()) {
            Log.d(TAG, "AccessToken:" + session.getAccessToken());

            AccountBean mAccount = new AccountBean();
            mAccount.setId(String.valueOf(currectItem));
            mAccount.setAccessToken(session.getAccessToken());
            mAccounts[currectItem] =  mAccount;

            getName(session);

        }
    }


    private Session getSession() {
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();

        /*
        Session s = new Session(getActivity().getApplicationContext());
        Session.OpenRequest request = new Session.OpenRequest(getActivity());
        request.setPermissions("publish_stream","user_groups");

        request.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
        request.setCallback(this);
        s.openForPublish(request);
        Session.setActiveSession(s);
        */
        Bundle bundle = new Bundle();
        bundle.putString("message", "message");
        WebDialog localWebDialog = new WebDialog.Builder(getActivity(), getString(R.string.app_id), "oauth", bundle).build();
        localWebDialog.setOnCompleteListener(new WebDialog.OnCompleteListener()
        {
            public void onComplete(Bundle bundle, FacebookException facebookException)
            {

                Session.getActiveSession();
                AccessToken localAccessToken = AccessToken.createFromExistingAccessToken(bundle.getString("access_token"), null, null, AccessTokenSource.WEB_VIEW, null);
                Session.openActiveSessionWithAccessToken(getActivity().getApplicationContext(), localAccessToken, new Session.StatusCallback() {
                    @Override
                    public void call(final Session session, SessionState sessionState, Exception e) {
                        if (e!=null)
                            e.printStackTrace();
                        Log.d(TAG, "call");
                        Log.d(TAG, "AccessToken:" + session.getAccessToken().toString());
                        Log.d(TAG, "getState:" + session.getState());
                        Session.setActiveSession(session);
                    }

                });
            }
        });
        localWebDialog.show();

        return null;
    }

    private void openSessionByToken(String token) {
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();
        AccessToken mAccessToken = AccessToken.createFromExistingAccessToken(token,
                null, null, AccessTokenSource.FACEBOOK_APPLICATION_WEB, Arrays.asList("publish_stream"));
        Session.openActiveSessionWithAccessToken(getActivity(), mAccessToken, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                Log.d(TAG,"openSessionByToken");
                Log.d(TAG, "getState:" + session.getState());
                Log.d(TAG, "getPermissions:" + session.getPermissions());
                post(session);
            }
        });
    }

    private void post(Session mSession) {
        if (mSession != null && mSession.isOpened()) {

            Bundle mBundle = new Bundle();
            mBundle.putString("message", "測試");
            new Request(mSession,
                    "/535812409773717/feed",
                    mBundle,
                    HttpMethod.POST,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            Log.d(TAG, response.toString());
                        }
                    }
            ).executeAsync();
        } else {
            Log.d(TAG, "mSessionIsNull:" + String.valueOf(mSession == null));
        }

    }

    private void getName(Session mSession) {
        new Request(mSession,
                "/me",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        Log.d(TAG, "Error:" + response.getError());
                        GraphUser mUser = response.getGraphObjectAs(GraphUser.class);
                        //Log.d(TAG,"Respones:"+response.getGraphObject().getInnerJSONObject().toString());
                        mUserNames[currectItem].setText(mUser.getName());
                        mPosts[currectItem].setVisibility(View.VISIBLE);
                    }
                }).executeAsync();


    }

    @Override
    public void onDestroy() {
        if (Session.getActiveSession()!=null)
            Session.getActiveSession().closeAndClearTokenInformation();

        super.onDestroy();
    }
}
