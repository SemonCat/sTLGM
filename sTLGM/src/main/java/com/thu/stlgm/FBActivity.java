package com.thu.stlgm;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by SemonCat on 2014/3/7.
 */
public class FBActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);

        finish();
    }
}
