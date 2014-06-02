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
public class PersonaStep3Fragment extends TextSaverFragment{

    private Page mPage;

    private EditText GoodThings01,GoodThings02,GoodThings03,GoodThings04,GoodThings05;
    private EditText BadThings01,BadThings02,BadThings03,BadThings04,BadThings05;

    private PersonaPreview PersonaPreviewStep3;

    public PersonaStep3Fragment(Page mPage){
        this.mPage = mPage;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        setupEvent();

        addEditTextToSave("GoodThings01",GoodThings01);
        addEditTextToSave("GoodThings02",GoodThings02);
        addEditTextToSave("GoodThings03",GoodThings03);
        addEditTextToSave("GoodThings04",GoodThings04);
        addEditTextToSave("GoodThings05",GoodThings05);

        addEditTextToSave("BadThings01",BadThings01);
        addEditTextToSave("BadThings02",BadThings02);
        addEditTextToSave("BadThings03",BadThings03);
        addEditTextToSave("BadThings04",BadThings04);
        addEditTextToSave("BadThings05",BadThings05);
    }

    private void setupView(){
        GoodThings01.setText(mPage.getData().getString("GoodThings01"));
        GoodThings02.setText(mPage.getData().getString("GoodThings02"));
        GoodThings03.setText(mPage.getData().getString("GoodThings03"));
        GoodThings04.setText(mPage.getData().getString("GoodThings04"));
        GoodThings05.setText(mPage.getData().getString("GoodThings05"));

        BadThings01.setText(mPage.getData().getString("BadThings01"));
        BadThings02.setText(mPage.getData().getString("BadThings02"));
        BadThings03.setText(mPage.getData().getString("BadThings03"));
        BadThings04.setText(mPage.getData().getString("BadThings04"));
        BadThings05.setText(mPage.getData().getString("BadThings05"));

        PersonaPreviewStep3.setHighLight(3,true);
        PersonaPreviewStep3.setHighLight(4,true);
    }

    private void setupEvent() {

        GoodThings01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("GoodThings01", input);
                mPage.notifyDataChanged();
            }
        });

        GoodThings02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("GoodThings02", input);
                mPage.notifyDataChanged();
            }
        });

        GoodThings03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("GoodThings03", input);
                mPage.notifyDataChanged();
            }
        });

        GoodThings04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("GoodThings04", input);
                mPage.notifyDataChanged();
            }
        });

        GoodThings05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("GoodThings05", input);
                mPage.notifyDataChanged();
            }
        });

        BadThings01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("BadThings01", input);
                mPage.notifyDataChanged();
            }
        });

        BadThings02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("BadThings02", input);
                mPage.notifyDataChanged();
            }
        });

        BadThings03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("BadThings03", input);
                mPage.notifyDataChanged();
            }
        });

        BadThings04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("BadThings04", input);
                mPage.notifyDataChanged();
            }
        });

        BadThings05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("BadThings05", input);
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


        View rootView = localInflater.inflate(R.layout.fragment_persona_step_3, container, false);

        GoodThings01 = (EditText) rootView.findViewById(R.id.GoodThings01);
        GoodThings02 = (EditText) rootView.findViewById(R.id.GoodThings02);
        GoodThings03 = (EditText) rootView.findViewById(R.id.GoodThings03);
        GoodThings04 = (EditText) rootView.findViewById(R.id.GoodThings04);
        GoodThings05 = (EditText) rootView.findViewById(R.id.GoodThings05);

        BadThings01 = (EditText) rootView.findViewById(R.id.BadThings01);
        BadThings02 = (EditText) rootView.findViewById(R.id.BadThings02);
        BadThings03 = (EditText) rootView.findViewById(R.id.BadThings03);
        BadThings04 = (EditText) rootView.findViewById(R.id.BadThings04);
        BadThings05 = (EditText) rootView.findViewById(R.id.BadThings05);

        PersonaPreviewStep3 = (PersonaPreview) rootView.findViewById(R.id.PersonaPreviewStep3);

        return rootView;
    }
}
