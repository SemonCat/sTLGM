package com.thu.digi256;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    // request codes we use when invoking an external activity
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.container,new Digi256Fragment()).commit();

    }

}
