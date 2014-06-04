package com.semoncat.bmc.model;

import android.app.Fragment;
import com.semoncat.bmc.ui.BMCStepFragment;
import com.semoncat.wizzardpager.model.ModelCallbacks;
import com.semoncat.wizzardpager.model.Page;
import com.semoncat.wizzardpager.model.ReviewItem;
import com.semoncat.wizzardpager.ui.PageFragmentCallbacks;

import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class BMCStep7 extends Page {

    public BMCStep7(PageFragmentCallbacks mPageFragmentCallbacks, ModelCallbacks callbacks, String title) {
        super(mPageFragmentCallbacks, callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return new BMCStepFragment(this).setupStep(7);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }
}
