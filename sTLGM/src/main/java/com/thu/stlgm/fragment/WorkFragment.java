package com.thu.stlgm.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.android.Facebook;
import com.thu.stlgm.R;
import com.thu.stlgm.util.ConstantUtil;


/**
 * Created by SemonCat on 2014/3/6.
 */
public class WorkFragment extends BaseFragment implements OnClickListener{

    public static final String EXTRA_URL = "url";

    private String mUrl = null;

    private FrameLayout mView = null;
    private ImageView mImageView = null;
    private WebView mWebview = null;
    private ProgressBar mPbar = null;

    private ImageButton mBackBtn = null;
    private ImageButton mFowardBtn = null;
    private ImageButton mRefreshBtn = null;
    private ProgressBar mRefreshPbar = null;
    private ImageButton mShareBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = ConstantUtil.Facebook +ConstantUtil.GroupId;

        if (!mUrl.startsWith("http"))
            mUrl = "http://" + mUrl;
    }

    @SuppressLint({
            "SetJavaScriptEnabled", "NewApi"
    })
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        mPbar = (ProgressBar) view.findViewById(R.id.web_view_progress);

        mView = (FrameLayout) view.findViewById(R.id.web_view);
        mView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        mImageView = new ImageView(getActivity());
        mImageView.setBackgroundColor(getResources().getColor(android.R.color.white));
        mImageView.setImageResource(R.drawable.big_image_loading);
        mImageView.setScaleType(ScaleType.CENTER_INSIDE);
        mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) mImageView.getDrawable()).start();
            }
        });
        mView.addView(mImageView);

        mWebview = new WebView(getActivity());
        mWebview.setVisibility(View.GONE);
        mWebview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        if (mUrl != null) {
            mWebview.setWebViewClient(new MyWebViewClient());
            mWebview.setWebChromeClient(new MyWebChromeClient());
            mWebview.getSettings().setPluginState(PluginState.ON);
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setDefaultZoom(ZoomDensity.FAR);
            mWebview.getSettings().setBuiltInZoomControls(true);
            mWebview.getSettings().setSupportZoom(true);
            mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebview.getSettings().setAllowFileAccess(true);
            mWebview.getSettings().setDomStorageEnabled(true);
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.getSettings().setAppCacheEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mWebview.getSettings().setDisplayZoomControls(false);

            mWebview.loadUrl(mUrl);
        }
        mView.addView(mWebview);

        mBackBtn = (ImageButton) view.findViewById(R.id.web_view_btn_back);
        mFowardBtn = (ImageButton) view.findViewById(R.id.web_view_btn_forward);
        mRefreshBtn = (ImageButton) view.findViewById(R.id.web_view_btn_refresh);
        mRefreshPbar = (ProgressBar) view.findViewById(R.id.loading);
        mShareBtn = (ImageButton) view.findViewById(R.id.web_view_btn_share);

        mBackBtn.setOnClickListener(this);
        mFowardBtn.setOnClickListener(this);
        mRefreshBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);

        updateActionView();

        return view;
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress)
        {
            if (progress < 100 && mPbar.getVisibility() == ProgressBar.GONE)
                mPbar.setVisibility(ProgressBar.VISIBLE);
            mPbar.setProgress(progress);
            if (progress == 100)
                mPbar.setVisibility(ProgressBar.GONE);
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mRefreshPbar.setVisibility(View.VISIBLE);
            mRefreshBtn.setVisibility(View.INVISIBLE);
            updateActionView();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            final Animation fade = new AlphaAnimation(0.0f, 1.0f);
            fade.setDuration(200);
            view.startAnimation(fade);
            view.setVisibility(View.VISIBLE);
            mRefreshPbar.setVisibility(View.INVISIBLE);
            mRefreshBtn.setVisibility(View.VISIBLE);
            updateActionView();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.endsWith(".mp4"))
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                view.getContext().startActivity(intent);
                return true;
            }
            else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.web_view_btn_back:
                mWebview.goBack();
                break;
            case R.id.web_view_btn_forward:
                mWebview.goForward();
                break;
            case R.id.web_view_btn_refresh:
                mWebview.reload();
                break;
            case R.id.web_view_btn_share:
                Uri uri = Uri.parse(mUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
        updateActionView();
    }

    private void updateActionView() {
        if (mWebview.canGoBack())
            mBackBtn.setEnabled(true);
        else
            mBackBtn.setEnabled(false);

        if (mWebview.canGoForward())
            mFowardBtn.setEnabled(true);
        else
            mFowardBtn.setEnabled(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (mWebview!=null){
            mView.removeView(mWebview);
            mWebview.removeAllViews();
            mWebview.destroy();
        }
    }


    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebview.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebview.onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebview != null) {
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }

    public void Finish(){
        if (getActivity()!=null){
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}
