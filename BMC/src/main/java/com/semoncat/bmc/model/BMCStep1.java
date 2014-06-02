package com.semoncat.bmc.model;

import android.app.Fragment;
import com.semoncat.bmc.ui.BMCStep1Fragment;
import com.semoncat.wizzardpager.model.ModelCallbacks;
import com.semoncat.wizzardpager.model.Page;
import com.semoncat.wizzardpager.model.ReviewItem;
import com.semoncat.wizzardpager.ui.PageFragmentCallbacks;

import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class BMCStep1 extends Page {

    public BMCStep1(PageFragmentCallbacks mPageFragmentCallbacks, ModelCallbacks callbacks, String title) {
        super(mPageFragmentCallbacks, callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return new BMCStep1Fragment(this);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }
}
