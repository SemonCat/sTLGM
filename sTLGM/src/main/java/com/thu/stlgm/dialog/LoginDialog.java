package com.thu.stlgm.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.thu.stlgm.R;
import com.thu.stlgm.util.ConstantUtil;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SemonCat on 2014/1/28.
 */
public class LoginDialog extends DialogFragment {

    private static final String TAG = LoginDialog.class.getName();


    private WebView LoginWebView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_login);
        dialog.setTitle("Login");
        dialog.setCancelable(true);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        LoginWebView = (WebView) dialog.findViewById(R.id.LoginWebView);
        LoginWebView.loadUrl(ConstantUtil.LoginURL);
        LoginWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Map<String,String> header = new HashMap<String, String>();
                //header.put("Host","m.facebook.com");



                view.loadUrl(url,header);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, url);
                super.onPageFinished(view, url);
            }
        });


        LoginWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                view.requestFocus();
            }
        });



        return dialog;
    }

}
