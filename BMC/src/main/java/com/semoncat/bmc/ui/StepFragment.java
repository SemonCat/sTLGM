package com.semoncat.bmc.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.semoncat.bmc.R;
import com.semoncat.wizzardpager.model.Page;

import java.util.IllegalFormatException;

/**
 * Created by SemonCat on 2014/6/2.
 */
public abstract class StepFragment extends TextSaverFragment{

    private static final String TAG = StepFragment.class.getName();

    protected Page mPage;

    protected StepFragment() {

    }

    public StepFragment(Page mPage) {
        this.mPage = mPage;
    }

    protected void setupView(View rootView){

    }

    protected void setupEvent(){

    }

    protected int setupContentLayout(){
        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (setupContentLayout()==-1){
            throw new IllegalArgumentException("You need setup ContentLayout id.");
        }

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);


        View rootView = localInflater.inflate(setupContentLayout(), container, false);

        setupView(rootView);
        setupEvent();

        return rootView;
    }
}
