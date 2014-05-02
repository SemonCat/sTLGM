package com.thu.persona.WizardPager.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.thu.persona.R;
import com.thu.persona.WizardPager.component.DrawingView;
import com.thu.persona.WizardPager.component.PersonaPreview;
import com.thu.persona.WizardPager.dialog.ColorPickerDialog;
import com.thu.persona.WizardPager.model.Page;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep2Fragment extends Fragment{

    private Page mPage;

    private EditText PersonaTitle,PersonaContent;

    private PersonaPreview PersonaPreviewStep2;

    public PersonaStep2Fragment(Page mPage){
        this.mPage = mPage;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        setupEvent();

    }

    private void setupView(){
        PersonaTitle.setText(mPage.getData().getString("PersonaTitle"));
        PersonaContent.setText(mPage.getData().getString("PersonaContent"));

        PersonaPreviewStep2.setHighLight(1,true);
    }

    private void setupEvent() {

        PersonaTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("PersonaTitle",input);
                mPage.notifyDataChanged();
            }
        });

        PersonaContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("PersonaContent",input);
                mPage.notifyDataChanged();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);


        View rootView = localInflater.inflate(R.layout.fragment_persona_step_2, container, false);

        PersonaTitle = (EditText) rootView.findViewById(R.id.PersonaTitle);
        PersonaContent = (EditText) rootView.findViewById(R.id.PersonaContent);

        PersonaPreviewStep2 = (PersonaPreview) rootView.findViewById(R.id.PersonaPreviewStep2);

        return rootView;
    }
}
