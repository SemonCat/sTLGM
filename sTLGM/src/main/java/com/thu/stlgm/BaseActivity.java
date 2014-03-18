package com.thu.stlgm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.Session;
import com.thu.stlgm.util.MusicManager;

/**
 * Created by SemonCat on 2014/1/28.
 */
public class BaseActivity extends FragmentActivity {

    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().addFlags(0x80000000);
    }

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

    public void HideSoftKeyBoard(){
        ((InputMethodManager) BaseActivity.this.
                getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken()
                        ,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_POWER == event.getKeyCode()){
            return true;//If event is handled, falseif
        }
        return super.onKeyDown(keyCode, event);
    }
}
