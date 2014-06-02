package com.semoncat.bmc.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.semoncat.bmc.R;
import com.semoncat.wizzardpager.model.AbstractWizardModel;
import com.semoncat.wizzardpager.model.ModelCallbacks;
import com.semoncat.wizzardpager.model.Page;

/**
 * Created by SemonCat on 2014/5/1.
 */
public class BMCPreviewFragment extends StepFragment implements ModelCallbacks {

    private AbstractWizardModel mWizardModel;

    public BMCPreviewFragment(AbstractWizardModel mWizardModel){
        this.mWizardModel = mWizardModel;
        mWizardModel.registerListener(this);
    }

    @Override
    protected void setupView(View rootView) {

    }

    @Override
    protected void setupEvent() {
    }

    @Override
    protected int setupContentLayout() {
        return R.layout.fragment_bmc_preview;
    }

    @Override
    public void onPageDataChanged(Page page) {

    }

    @Override
    public void onPageTreeChanged() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);

    }

}
