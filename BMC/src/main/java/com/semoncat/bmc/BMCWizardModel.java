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
import com.semoncat.bmc.model.*;
import com.semoncat.wizzardpager.model.AbstractWizardModel;
import com.semoncat.wizzardpager.model.PageList;
import com.semoncat.wizzardpager.ui.PageFragmentCallbacks;

public class BMCWizardModel extends AbstractWizardModel {
    public BMCWizardModel(PageFragmentCallbacks mPageFragmentCallbacks, Context context) {
        super(mPageFragmentCallbacks, context);

    }

    @Override
    protected PageList onNewRootPageList() {

        return new PageList(
                new BMCStep1(getPageFragmentCallbacks(), this, "Step1"),
                new BMCStep2(getPageFragmentCallbacks(), this, "Step2"),
                new BMCStep3(getPageFragmentCallbacks(), this, "Step3"),
                new BMCStep4(getPageFragmentCallbacks(), this, "Step4"),
                new BMCStep5(getPageFragmentCallbacks(), this, "Step5"),
                new BMCStep6(getPageFragmentCallbacks(), this, "Step6"),
                new BMCStep7(getPageFragmentCallbacks(), this, "Step7"),
                new BMCStep8(getPageFragmentCallbacks(), this, "Step8"),
                new BMCStep9(getPageFragmentCallbacks(), this, "Step9")
        );
    }
}
