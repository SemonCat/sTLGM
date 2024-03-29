package com.thu.persona.WizardPager.model;

import android.app.Fragment;

import com.semoncat.wizzardpager.model.ModelCallbacks;
import com.semoncat.wizzardpager.model.Page;
import com.semoncat.wizzardpager.model.ReviewItem;
import com.semoncat.wizzardpager.ui.PageFragmentCallbacks;
import com.thu.persona.WizardPager.ui.PersonaStep1Fragment;
import com.thu.persona.WizardPager.ui.PersonaStep2Fragment;

import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep2 extends Page {

    public PersonaStep2(PageFragmentCallbacks mPageFragmentCallbacks, ModelCallbacks callbacks, String title) {
        super(mPageFragmentCallbacks, callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return new PersonaStep2Fragment(this);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }
}
