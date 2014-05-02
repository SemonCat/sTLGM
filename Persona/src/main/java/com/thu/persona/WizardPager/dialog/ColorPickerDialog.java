package com.thu.persona.WizardPager.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.thu.persona.R;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class ColorPickerDialog extends DialogFragment{

    private int DefaultColor;
    private ColorPicker.OnColorChangedListener mOnColorChangedListener;

    public ColorPickerDialog(int defaultColor){
        this.DefaultColor = defaultColor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View mContentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color_picker, null);

        ColorPicker picker = (ColorPicker) mContentView.findViewById(R.id.picker);
        SVBar svBar = (SVBar) mContentView.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) mContentView.findViewById(R.id.opacitybar);
        SaturationBar saturationBar = (SaturationBar) mContentView.findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) mContentView.findViewById(R.id.valuebar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        picker.setColor(DefaultColor);
        picker.setOldCenterColor(DefaultColor);

        if (mOnColorChangedListener!=null){
            picker.setOnColorChangedListener(mOnColorChangedListener);
        }




        return new AlertDialog.Builder(getActivity())
                .setTitle("顏色選擇器")
                .setView(mContentView)
                .setNegativeButton("完成",null)

                .create();
    }

    public void setOnColorChangedListener(ColorPicker.OnColorChangedListener listener){
        this.mOnColorChangedListener = listener;
    }
}
