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
import android.widget.EditText;

import com.semoncat.wizzardpager.model.Page;
import com.thu.persona.R;
import com.thu.persona.WizardPager.component.PersonaPreview;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep4Fragment extends TextSaverFragment{

    private Page mPage;

    private EditText Need01,Need02,Need03,Need04,Need05;

    private PersonaPreview PersonaPreviewStep4;

    public PersonaStep4Fragment(Page mPage){
        this.mPage = mPage;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        setupEvent();

        addEditTextToSave("Need01",Need01);
        addEditTextToSave("Need02",Need02);
        addEditTextToSave("Need03",Need03);
        addEditTextToSave("Need04",Need04);
        addEditTextToSave("Need05",Need05);

    }

    private void setupView(){
        Need01.setText(mPage.getData().getString("Need01"));
        Need02.setText(mPage.getData().getString("Need02"));
        Need03.setText(mPage.getData().getString("Need03"));
        Need04.setText(mPage.getData().getString("Need04"));
        Need05.setText(mPage.getData().getString("Need05"));

        PersonaPreviewStep4.setHighLight(2,true);
    }

    private void setupEvent() {

        Need01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Need01", input);
                mPage.notifyDataChanged();
            }
        });

        Need02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Need02", input);
                mPage.notifyDataChanged();
            }
        });

        Need03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Need03", input);
                mPage.notifyDataChanged();
            }
        });

        Need04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Need04", input);
                mPage.notifyDataChanged();
            }
        });

        Need05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Need05", input);
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


        View rootView = localInflater.inflate(R.layout.fragment_persona_step_4, container, false);

        Need01 = (EditText) rootView.findViewById(R.id.Need01);
        Need02 = (EditText) rootView.findViewById(R.id.Need02);
        Need03 = (EditText) rootView.findViewById(R.id.Need03);
        Need04 = (EditText) rootView.findViewById(R.id.Need04);
        Need05 = (EditText) rootView.findViewById(R.id.Need05);

        PersonaPreviewStep4 = (PersonaPreview) rootView.findViewById(R.id.PersonaPreviewStep4);

        return rootView;
    }
}
