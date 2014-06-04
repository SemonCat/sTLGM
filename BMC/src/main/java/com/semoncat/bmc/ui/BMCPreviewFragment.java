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
import com.semoncat.bmc.BMCGenerator;
import com.semoncat.bmc.R;
import com.semoncat.bmc.component.BMCPreview;
import com.semoncat.wizzardpager.model.AbstractWizardModel;
import com.semoncat.wizzardpager.model.ModelCallbacks;
import com.semoncat.wizzardpager.model.Page;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/5/1.
 */
public class BMCPreviewFragment extends StepFragment implements ModelCallbacks {

    private static final String TAG = BMCPreviewFragment.class.getName();

    private AbstractWizardModel mWizardModel;

    private PhotoView BMC;

    public BMCPreviewFragment(AbstractWizardModel mWizardModel) {
        this.mWizardModel = mWizardModel;
        mWizardModel.registerListener(this);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            if (mWizardModel != null && BMC != null) {
                getData();
            }
        }
    }

    private void getData() {
        Bundle mData = mWizardModel.save();
        Bundle mStep1 = mData.getBundle("Step1");
        Bundle mStep2 = mData.getBundle("Step2");
        Bundle mStep3 = mData.getBundle("Step3");
        Bundle mStep4 = mData.getBundle("Step4");
        Bundle mStep5 = mData.getBundle("Step5");
        Bundle mStep6 = mData.getBundle("Step6");
        Bundle mStep7 = mData.getBundle("Step7");
        Bundle mStep8 = mData.getBundle("Step8");
        Bundle mStep9 = mData.getBundle("Step9");

        Bundle mDataArray[] = new Bundle[]{
                mStep1, mStep2,
                mStep3, mStep4,
                mStep5, mStep6,
                mStep7, mStep8,
                mStep9};

        String[] mDataStringArray = new String[mDataArray.length];
        for (int i = 0 ; i < mDataArray.length ; i++){
            mDataStringArray[i] = mDataArray[i].getString(BMCStepFragment.SaveKey[i]);

        }

        Bitmap Result = BMCGenerator.getPersonaBitmap(mDataStringArray);
        BMC.setImageBitmap(Result);
    }

    @Override
    protected void setupView(View rootView) {
        BMC = (PhotoView) rootView.findViewById(R.id.BMC);
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
