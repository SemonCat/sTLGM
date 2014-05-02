package com.thu.persona.WizardPager.model;

import android.app.Fragment;

import com.thu.persona.WizardPager.ui.NumberFragment;
import com.thu.persona.WizardPager.ui.PageFragmentCallbacks;

public class NumberPage extends TextPage {

	public NumberPage(PageFragmentCallbacks mPageFragmentCallbacks,ModelCallbacks callbacks, String title) {
		super(mPageFragmentCallbacks,callbacks, title);
	}

	@Override
	public Fragment createFragment() {
		return NumberFragment.create(getKey(),getPageFragmentCallbacks());
	}

}
