package com.thu.stlgm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.thu.stlgm.R;

/**
 * Created by SemonCat on 2014/1/29.
 */
public class TestFragment extends BaseFragment{

    private static final String TAG = TestFragment.class.getName();

    private Button mLogin1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLogin1 = (Button) getActivity().findViewById(R.id.doLogin1);

        mLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session s = new Session(getActivity().getApplicationContext());
                Session.OpenRequest request = new Session.OpenRequest(getActivity());
                request.setPermissions("publish_stream"); // Note that you cannot set email AND publish_actions in the same request
                request.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);

                request.setCallback(new Session.StatusCallback() {
                    @Override
                    public void call(final Session session, SessionState sessionState, Exception e) {
                        if (e!=null)
                            e.printStackTrace();
                        Log.d(TAG, "call");
                        Log.d(TAG, "AccessToken:" + session.getAccessToken().toString());
                        Log.d(TAG, "getState:" + session.getState());

                    }

                });
                s.openForPublish(request);
            }
        });
    }
}
