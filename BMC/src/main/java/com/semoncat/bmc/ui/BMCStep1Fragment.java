package com.semoncat.bmc.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.semoncat.bmc.R;
import com.semoncat.wizzardpager.model.Page;

/**
 * Created by SemonCat on 2014/6/2.
 */
public class BMCStep1Fragment extends StepFragment{

    private static final String TAG = BMCStep1Fragment.class.getName();

    public BMCStep1Fragment(Page mPage) {
        super(mPage);
    }

    @Override
    protected void setupView(View rootView) {

    }

    @Override
    protected void setupEvent() {

    }

    @Override
    protected int setupContentLayout() {
        return R.layout.fragment_bmc_step_1;
    }


}