/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thu.persona.WizardPager;

import android.content.Context;
import android.util.Log;

import com.semoncat.wizzardpager.model.AbstractWizardModel;
import com.semoncat.wizzardpager.model.PageList;
import com.semoncat.wizzardpager.ui.PageFragmentCallbacks;
import com.thu.persona.WizardPager.model.PersonaStep1;
import com.thu.persona.WizardPager.model.PersonaStep2;
import com.thu.persona.WizardPager.model.PersonaStep3;
import com.thu.persona.WizardPager.model.PersonaStep4;
import com.thu.persona.WizardPager.model.PersonaStep5;

public class PersonaWizardModel extends AbstractWizardModel {
	public PersonaWizardModel(PageFragmentCallbacks mPageFragmentCallbacks, Context context) {
		super(mPageFragmentCallbacks,context);

	}

	@Override
	protected PageList onNewRootPageList() {

		return new PageList(
                new PersonaStep1(getPageFragmentCallbacks(),this,"Step1"),
                new PersonaStep2(getPageFragmentCallbacks(),this,"Step2"),
                new PersonaStep3(getPageFragmentCallbacks(),this,"Step3"),
                new PersonaStep4(getPageFragmentCallbacks(),this,"Step4"),
                new PersonaStep5(getPageFragmentCallbacks(),this,"Step5"));
	}
}
