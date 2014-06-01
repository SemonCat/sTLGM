package com.thu.persona.WizardPager.model;

import android.app.Fragment;

import com.thu.persona.WizardPager.ui.PageFragmentCallbacks;
import com.thu.persona.WizardPager.ui.PersonaStep2Fragment;
import com.thu.persona.WizardPager.ui.PersonaStep5Fragment;

import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep5 extends Page{

    public PersonaStep5(PageFragmentCallbacks mPageFragmentCallbacks, ModelCallbacks callbacks, String title) {
        super(mPageFragmentCallbacks, callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return new PersonaStep5Fragment(this);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }
}
