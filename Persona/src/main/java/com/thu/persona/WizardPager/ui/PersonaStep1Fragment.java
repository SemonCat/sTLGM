package com.thu.persona.WizardPager.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.semoncat.wizzardpager.model.Page;
import com.squareup.picasso.Picasso;
import com.thu.persona.R;
import com.thu.persona.WizardPager.PersonaGenerator;
import com.thu.persona.WizardPager.component.DrawingView;
import com.thu.persona.WizardPager.component.PathObject;
import com.thu.persona.WizardPager.component.PersonaPreview;
import com.thu.persona.WizardPager.dialog.ColorPickerDialog;
import com.thu.persona.WizardPager.dialog.ImagePickerDialog;
import com.thu.persona.WizardPager.model.PersonaStep1;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep1Fragment extends TextSaverFragment implements ColorPicker.OnColorChangedListener,ImagePickerDialog.OnPictureSelectListener {

    private Page mPage;

    private Button ColorPickerButton,EraseButton,CleanAllButton,WebImageButton;
    private EditText Name,Gender,Age,Job;
    private DrawingView mDrawingView;

    private ViewGroup Icon;

    private SeekBar mPaintSize;

    private PersonaPreview PersonaPreviewStep1;

    public PersonaStep1Fragment(Page mPage){
        this.mPage = mPage;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (!menuVisible){
            if (Icon!=null && mPage!=null){
                Icon.setDrawingCacheEnabled(true);
                Icon.buildDrawingCache();
                //mPage.getData().putParcelable("Icon",Icon.getDrawingCache());
                mPage.getData().putByteArray("Icon", PersonaGenerator.saveBitmap2ByteArray(Icon.getDrawingCache()));
                mPage.notifyDataChanged();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        setupEvent();

        addEditTextToSave("Name",Name);
        addEditTextToSave("Gender",Gender);
        addEditTextToSave("Age",Age);
        addEditTextToSave("Job",Job);


        if (savedInstanceState!=null){
            final ArrayList<PathObject> mData = savedInstanceState.getParcelableArrayList("mDrawingView");
            if (mData !=null){

                ViewTreeObserver observer = mDrawingView.getViewTreeObserver();

                if (observer != null) {
                    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mDrawingView.startNew();
                            mDrawingView.reDrawPath(mData);
                            ViewTreeObserver obs = mDrawingView.getViewTreeObserver();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                obs.removeOnGlobalLayoutListener(this);
                            } else {
                                obs.removeGlobalOnLayoutListener(this);
                            }
                        }
                    });
                }

            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("mDrawingView", mDrawingView.getPaintPath());
        super.onSaveInstanceState(outState);

    }

    private void setupView(){
        PersonaPreviewStep1.setHighLight(0,true);

        Name.setText(mPage.getData().getString("Name"));
        Gender.setText(mPage.getData().getString("Gender"));
        Age.setText(mPage.getData().getString("Age"));
        Job.setText(mPage.getData().getString("Job"));
    }

    private void setupEvent() {
        WebImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePickerDialog().setListener(PersonaStep1Fragment.this).show(getFragmentManager(), null);
            }
        });

        ColorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(false);
                if (getFragmentManager() != null) {
                    ColorPickerDialog mColorPickerDialog = new ColorPickerDialog(mDrawingView.getColor());
                    mColorPickerDialog.setOnColorChangedListener(PersonaStep1Fragment.this);
                    mColorPickerDialog.show(getFragmentManager(), null);
                }
            }
        });

        mPaintSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawingView.setBrushSize(5+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        EraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
            }
        });

        CleanAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.startNew();
            }
        });

        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Name",input);
                mPage.notifyDataChanged();
            }
        });

        Gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Gender",input);
                mPage.notifyDataChanged();
            }
        });

        Age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Age",input);
                mPage.notifyDataChanged();
            }
        });

        Job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Job", input);
                mPage.notifyDataChanged();
            }
        });
    }


    @Override
    public void OnLocalPictureSelected(File mFile) {
        Picasso.with(getActivity()).load(mFile).resize(200,200).into(mDrawingView);
    }

    @Override
    public void OnWebPictureSelected(String Url) {
        Picasso.with(getActivity()).load(Url).resize(200,200).into(mDrawingView);
    }

    @Override
    public void onColorChanged(int i) {
        mDrawingView.setErase(false);
        mDrawingView.setColor(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);


        View rootView = localInflater.inflate(R.layout.fragment_persona_step_1, container, false);

        Icon = (ViewGroup) rootView.findViewById(R.id.Icon);

        WebImageButton = (Button) rootView.findViewById(R.id.WebImageButton);

        ColorPickerButton = (Button) rootView.findViewById(R.id.ColorPickerButton);
        EraseButton = (Button) rootView.findViewById(R.id.EraseButton);
        mDrawingView = (DrawingView) rootView.findViewById(R.id.PersonaDrawingView);
        mDrawingView.setBrushSize(5);
        mPaintSize = (SeekBar) rootView.findViewById(R.id.PaintSize);
        CleanAllButton = (Button) rootView.findViewById(R.id.CleanAllPaint);

        Name = (EditText) rootView.findViewById(R.id.Name);
        Gender = (EditText) rootView.findViewById(R.id.Gender);
        Age = (EditText) rootView.findViewById(R.id.Age);
        Job = (EditText) rootView.findViewById(R.id.Job);

        PersonaPreviewStep1 = (PersonaPreview) rootView.findViewById(R.id.PersonaPreviewStep1);

        return rootView;
    }
}
