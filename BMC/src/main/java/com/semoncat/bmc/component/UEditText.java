package com.semoncat.bmc.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by SemonCat on 2014/6/3.
 */
public class UEditText extends EditText {

    private static final String TAG = UEditText.class.getName();

    private static final String li = " ● ";
    private static final String SPACE = " ";

    private boolean IsChanged = false;

    public UEditText(Context context) {
        super(context);
        init();
    }

    public UEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setText(li);


        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (IsChanged) return;


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (IsChanged) return;

                if (getSelectionStart() > 0 && s.charAt(getSelectionStart() - 1) == '\n') {
                    IsChanged = true;
                    s.insert(getSelectionStart(), li);
                    IsChanged = false;
                }
            }
        });


        this.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                return false;
            }
        });
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new UInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class UInputConnection extends InputConnectionWrapper {

        public UInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                if (getText().length() > li.length()) {
                    int endIndex = getSelectionStart();
                    int startIndex = endIndex - li.length() - 1;

                    String lastWord = getText().subSequence(startIndex, endIndex).toString();
                    Log.d(TAG, "LastWord:" + lastWord);
                    if (lastWord.equals("\n" + li)) {
                        Log.d(TAG, "Equals");
                        getText().delete(startIndex, endIndex);
                        return true;
                    }
                }else{
                    return true;
                }


            }
            return super.sendKeyEvent(event);
        }

    }
}
