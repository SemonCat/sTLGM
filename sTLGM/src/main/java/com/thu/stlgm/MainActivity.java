package com.thu.stlgm;

import android.app.Activity;
import android.app.ActionBar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.facebook.Session;
import com.github.snowdream.android.app.AbstractUpdateListener;
import com.github.snowdream.android.app.DownloadTask;
import com.github.snowdream.android.app.UpdateFormat;
import com.github.snowdream.android.app.UpdateInfo;
import com.github.snowdream.android.app.UpdateManager;
import com.github.snowdream.android.app.UpdateOptions;
import com.github.snowdream.android.app.UpdatePeriod;
import com.thu.stlgm.fragment.LoginFragment_;
import com.thu.stlgm.util.ConstantUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransFragment(new LoginFragment_());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    protected void TransFragment(Fragment mFragment) {

        getFragmentManager().beginTransaction()
                .add(R.id.container, mFragment)
                .commit();
    }

    public void newAccount(View mView) {
        startActivity(new Intent(this, NewAccountActivity_.class));
    }


    public void goToGame(View mView){
        Intent mIntent = new Intent(this, GameActivity_.class);

        startActivity(mIntent);
        finish();
    }
}
