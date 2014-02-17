package com.thu.stlgm;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.facebook.Session;
import com.thu.stlgm.util.MusicManager;

/**
 * Created by SemonCat on 2014/1/28.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (!MusicManager.isInit())
            MusicManager.init(getApplicationContext());
        MusicManager.startMusic();
    }
}
