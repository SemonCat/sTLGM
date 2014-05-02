package com.example.linematch.app;

import android.app.Activity;
import android.os.Bundle;

import lineMatch.lineMatchFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.container, new lineMatchFragment()).commit();
    }
}
