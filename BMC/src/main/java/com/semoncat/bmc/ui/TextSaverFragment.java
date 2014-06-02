package com.semoncat.bmc.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.HashMap;

/**
 * 將 EditText 的文字儲存起來T。
 * Created by SemonCat on 2014/6/2.
 */
public class TextSaverFragment extends Fragment {

    private static final String TAG = TextSaverFragment.class.getName();

    private HashMap<String,EditText> mSaveTextView;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mSharedPreferences = getActivity().getSharedPreferences("TextSaver", Context.MODE_PRIVATE);
    }

    protected void addEditTextToSave(String Key,EditText mEditText) {
        if (mSaveTextView == null) {
            mSaveTextView = new HashMap<String, EditText>();
        }

        mSaveTextView.put(Key, mEditText);
    }

    protected void ResetAllStoreValue(){
        mSharedPreferences.edit().clear().commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSharedPreferences != null && mSaveTextView!=null) {
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();

            for (String Key : mSaveTextView.keySet()) {
                EditText mEditText = mSaveTextView.get(Key);

                if (mEditText != null &&
                        !TextUtils.isEmpty(mEditText.getText().toString())) {

                    mEditor.putString(Key,mEditText.getText().toString());

                }
            }

            mEditor.commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();



        if (mSharedPreferences != null && mSaveTextView!=null) {


            for (String Key : mSaveTextView.keySet()) {
                EditText mEditText = mSaveTextView.get(Key);

                String Text = mSharedPreferences.getString(Key,"");

                if (mEditText != null &&
                        !TextUtils.isEmpty(Text)) {

                    mEditText.setText(Text);

                }
            }

        }

    }
}
