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

package com.semoncat.bmc;

import android.content.Context;
import com.semoncat.bmc.model.BMCStep1;
import com.semoncat.wizzardpager.model.AbstractWizardModel;
import com.semoncat.wizzardpager.model.PageList;
import com.semoncat.wizzardpager.ui.PageFragmentCallbacks;

public class BMCWizardModel extends AbstractWizardModel {
	public BMCWizardModel(PageFragmentCallbacks mPageFragmentCallbacks, Context context) {
		super(mPageFragmentCallbacks,context);

	}

	@Override
	protected PageList onNewRootPageList() {

		return new PageList(
                new BMCStep1(getPageFragmentCallbacks(),this,"Step1")
                );
	}
}
