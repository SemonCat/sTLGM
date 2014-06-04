package com.semoncat.bmc.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import com.semoncat.bmc.R;
import com.semoncat.bmc.component.BMCPreview;
import com.semoncat.bmc.component.UEditText;
import com.semoncat.wizzardpager.model.Page;

/**
 * Created by SemonCat on 2014/6/2.
 */
public class BMCStepFragment extends StepFragment {

    private static final String TAG = BMCStepFragment.class.getName();

    private BMCPreview mBMCPreview;
    private UEditText mEditText;
    private TextView mPageTitle, mPageSmallTitle, mPageContent;

    public static final String SaveKey[] = new String[]{
            "Key Partners", "Key Activities",
            "Key Resources", "Value Proposition",
            "Customer Relationships", "Channels",
            "Customer Segments", "Cost Structure",
            "Revenue Streams"};

    private int step = 1;

    public BMCStepFragment(Page mPage) {
        super(mPage);
    }

    public BMCStepFragment setupStep(int step) {

        this.step = step;

        return this;
    }

    @Override
    protected void setupView(View rootView) {
        mBMCPreview = (BMCPreview) rootView.findViewById(R.id.BMCPreview);
        mBMCPreview.setHighLight(step - 1, true);

        mPageTitle = (TextView) rootView.findViewById(R.id.PageTitle);
        mPageContent = (TextView) rootView.findViewById(R.id.PageContent);
        mPageSmallTitle = (TextView) rootView.findViewById(R.id.PageSmallTitle);

        mEditText = (UEditText) rootView.findViewById(R.id.PageEditText);
        addEditTextToSave(SaveKey[step-1],mEditText);

        switch (step) {
            case 1:

                mPageTitle.setText(R.string.BMC_KeyPartners_Label);
                mPageSmallTitle.setText(R.string.BMC_KeyPartners_Label);
                mPageContent.setText(R.string.BMC_KeyPartners_Content);
                break;
            case 2:
                mPageTitle.setText(R.string.BMC_KeyActivities_Label);
                mPageSmallTitle.setText(R.string.BMC_KeyActivities_Label);
                mPageContent.setText(R.string.BMC_KeyActivities_Content);
                break;
            case 3:
                mPageTitle.setText(R.string.BMC_KeyResources_Label);
                mPageSmallTitle.setText(R.string.BMC_KeyResources_Label);
                mPageContent.setText(R.string.BMC_KeyResources_Content);
                break;
            case 4:
                mPageTitle.setText(R.string.BMC_ValueProposition_Label);
                mPageSmallTitle.setText(R.string.BMC_ValueProposition_Label);
                mPageContent.setText(R.string.BMC_ValueProposition_Content);
                break;
            case 5:
                mPageTitle.setText(R.string.BMC_CustomerRelationships_Label);
                mPageSmallTitle.setText(R.string.BMC_CustomerRelationships_Label);
                mPageContent.setText(R.string.BMC_CustomerRelationships_Content);
                break;
            case 6:
                mPageTitle.setText(R.string.BMC_Channels_Label);
                mPageSmallTitle.setText(R.string.BMC_Channels_Label);
                mPageContent.setText(R.string.BMC_Channels_Content);
                break;
            case 7:
                mPageTitle.setText(R.string.BMC_CustomerSegments_Label);
                mPageSmallTitle.setText(R.string.BMC_CustomerSegments_Label);
                mPageContent.setText(R.string.BMC_CustomerSegments_Content);
                break;
            case 8:
                mPageTitle.setText(R.string.BMC_CostStructure_Label);
                mPageSmallTitle.setText(R.string.BMC_CostStructure_Label);
                mPageContent.setText(R.string.BMC_CostStructure_Content);
                break;
            case 9:
                mPageTitle.setText(R.string.BMC_RevenueStreams_Label);
                mPageSmallTitle.setText(R.string.BMC_RevenueStreams_Label);
                mPageContent.setText(R.string.BMC_RevenueStreams_Content);
                break;
        }


    }

    @Override
    protected void setupEvent() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString(SaveKey[step-1],input);
                mPage.notifyDataChanged();
            }
        });
    }

    @Override
    protected int setupContentLayout() {
        return R.layout.fragment_bmc_step;
    }


}
