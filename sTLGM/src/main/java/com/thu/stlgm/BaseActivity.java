package com.thu.stlgm;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.Session;
import com.thu.stlgm.util.MusicManager;

/**
 * Created by SemonCat on 2014/1/28.
 */
public class BaseActivity extends FragmentActivity {

    Toast mToast;

    protected void showToast(String Message){
        if (mToast==null){
            mToast = Toast.makeText(this,Message,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(Message);
        }
        mToast.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //MusicManager.pauseMusic();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();


        /*
        if (!MusicManager.isInit())
            MusicManager.init(getApplicationContext());
        MusicManager.startMusic();
        */
    }
}
