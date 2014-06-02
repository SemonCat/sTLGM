package com.thu.persona.WizardPager.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.semoncat.wizzardpager.model.Page;
import com.thu.persona.R;
import com.thu.persona.WizardPager.PersonaGenerator;
import com.thu.persona.WizardPager.component.PersonaPreview;

import org.w3c.dom.Text;

/**
 * Created by SemonCat on 2014/4/30.
 */
public class PersonaStep5Fragment extends TextSaverFragment{

    private Page mPage;

    private ViewGroup PersonaBackground;

    private EditText Attr_Title01,Attr_Title02,Attr_Title03,Attr_Title04,Attr_Title05;
    private SeekBar Attr_Value01,Attr_Value02,Attr_Value03,Attr_Value04,Attr_Value05;

    private PersonaPreview PersonaPreviewStep5;

    public PersonaStep5Fragment(Page mPage){
        this.mPage = mPage;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (!menuVisible){
            if (PersonaBackground!=null && mPage!=null){
                HideEmptySeekBar();
                PersonaBackground.setDrawingCacheEnabled(true);
                PersonaBackground.buildDrawingCache();
                mPage.getData().putParcelable("PersonaBackground",PersonaBackground.getDrawingCache());
                //mPage.getData().putByteArray("PersonaBackground", PersonaGenerator.saveBitmap2ByteArray(PersonaBackground.getDrawingCache()));
                mPage.notifyDataChanged();
                ShowEmptySeekBar();
                Log.d("","Save PersonaBackground");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
        setupEvent();

        addEditTextToSave("Attr_Title01",Attr_Title01);
        addEditTextToSave("Attr_Title02",Attr_Title02);
        addEditTextToSave("Attr_Title03",Attr_Title03);
        addEditTextToSave("Attr_Title04",Attr_Title04);
        addEditTextToSave("Attr_Title05",Attr_Title05);

    }

    private void setupView(){
        Attr_Title01.setText(mPage.getData().getString("Attr_Title01"));
        Attr_Title02.setText(mPage.getData().getString("Attr_Title02"));
        Attr_Title03.setText(mPage.getData().getString("Attr_Title03"));
        Attr_Title04.setText(mPage.getData().getString("Attr_Title04"));
        Attr_Title05.setText(mPage.getData().getString("Attr_Title05"));

        Attr_Value01.setProgress(mPage.getData().getInt("Attr_Value01",50));
        Attr_Value02.setProgress(mPage.getData().getInt("Attr_Value02",50));
        Attr_Value03.setProgress(mPage.getData().getInt("Attr_Value03",50));
        Attr_Value04.setProgress(mPage.getData().getInt("Attr_Value04",50));
        Attr_Value05.setProgress(mPage.getData().getInt("Attr_Value05",50));

        PersonaPreviewStep5.setHighLight(5,true);
    }

    private void setupEvent() {

        Attr_Title01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Attr_Title01", input);
                mPage.notifyDataChanged();
            }
        });

        Attr_Title02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Attr_Title02", input);
                mPage.notifyDataChanged();
            }
        });

        Attr_Title03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Attr_Title03", input);
                mPage.notifyDataChanged();
            }
        });

        Attr_Title04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Attr_Title04", input);
                mPage.notifyDataChanged();
            }
        });

        Attr_Title05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = (editable != null) ? editable.toString() : null;

                mPage.getData().putString("Attr_Title05", input);
                mPage.notifyDataChanged();
            }
        });

        Attr_Value01.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPage.getData().putInt("Attr_Value01",progress);
                mPage.notifyDataChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Attr_Value02.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPage.getData().putInt("Attr_Value02",progress);
                mPage.notifyDataChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Attr_Value03.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPage.getData().putInt("Attr_Value03",progress);
                mPage.notifyDataChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Attr_Value04.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPage.getData().putInt("Attr_Value04",progress);
                mPage.notifyDataChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Attr_Value05.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPage.getData().putInt("Attr_Value05",progress);
                mPage.notifyDataChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void HideEmptySeekBar(){
        if (TextUtils.isEmpty(Attr_Title01.getText())){
            Attr_Title01.setVisibility(View.GONE);
            Attr_Value01.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(Attr_Title02.getText())){
            Attr_Title02.setVisibility(View.GONE);
            Attr_Value02.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(Attr_Title03.getText())){
            Attr_Title03.setVisibility(View.GONE);
            Attr_Value03.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(Attr_Title04.getText())){
            Attr_Title04.setVisibility(View.GONE);
            Attr_Value04.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(Attr_Title05.getText())){
            Attr_Title05.setVisibility(View.GONE);
            Attr_Value05.setVisibility(View.GONE);
        }

        Attr_Title01.clearFocus();
        Attr_Title02.clearFocus();
        Attr_Title03.clearFocus();
        Attr_Title04.clearFocus();
        Attr_Title05.clearFocus();
    }

    private void ShowEmptySeekBar(){
        if (TextUtils.isEmpty(Attr_Title01.getText())){
            Attr_Title01.setVisibility(View.VISIBLE);
            Attr_Value01.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(Attr_Title02.getText())){
            Attr_Title02.setVisibility(View.VISIBLE);
            Attr_Value02.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(Attr_Title03.getText())){
            Attr_Title03.setVisibility(View.VISIBLE);
            Attr_Value03.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(Attr_Title04.getText())){
            Attr_Title04.setVisibility(View.VISIBLE);
            Attr_Value04.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(Attr_Title05.getText())){
            Attr_Title05.setVisibility(View.VISIBLE);
            Attr_Value05.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);


        View rootView = localInflater.inflate(R.layout.fragment_persona_step_5, container, false);

        PersonaBackground = (ViewGroup) rootView.findViewById(R.id.PersonaBackground);

        Attr_Title01 = (EditText) rootView.findViewById(R.id.Attr_Title01);
        Attr_Title02 = (EditText) rootView.findViewById(R.id.Attr_Title02);
        Attr_Title03 = (EditText) rootView.findViewById(R.id.Attr_Title03);
        Attr_Title04 = (EditText) rootView.findViewById(R.id.Attr_Title04);
        Attr_Title05 = (EditText) rootView.findViewById(R.id.Attr_Title05);

        Attr_Value01 = (SeekBar) rootView.findViewById(R.id.Attr_Value01);
        Attr_Value02 = (SeekBar) rootView.findViewById(R.id.Attr_Value02);
        Attr_Value03 = (SeekBar) rootView.findViewById(R.id.Attr_Value03);
        Attr_Value04 = (SeekBar) rootView.findViewById(R.id.Attr_Value04);
        Attr_Value05 = (SeekBar) rootView.findViewById(R.id.Attr_Value05);

        PersonaPreviewStep5 = (PersonaPreview) rootView.findViewById(R.id.PersonaPreviewStep5);

        return rootView;
    }
}
