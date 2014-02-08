package com.thu.stlgm;

import android.app.Activity;
import android.app.ActionBar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.facebook.Session;
import com.thu.stlgm.fragment.MainFragment;
import com.thu.stlgm.fragment.MainFragment_;
import com.thu.stlgm.fragment.TestFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransFragment(new MainFragment_());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    protected void TransFragment(Fragment mFragment){

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mFragment)
                .commit();
    }
}
