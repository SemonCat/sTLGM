package com.thu.persona.WizardPager.model;

import android.app.Fragment;

import com.thu.persona.WizardPager.ui.PageFragmentCallbacks;
import com.thu.persona.WizardPager.ui.PersonaStep2Fragment;
import com.thu.persona.WizardPager.ui.PersonaStep4Fragment;

import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep4 extends Page{

    public PersonaStep4(PageFragmentCallbacks mPageFragmentCallbacks, ModelCallbacks callbacks, String title) {
        super(mPageFragmentCallbacks, callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return new PersonaStep4Fragment(this);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }
}
