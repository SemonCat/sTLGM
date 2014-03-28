package com.thu.stlgm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.Request;
import com.facebook.Response;
import com.thu.stlgm.BaseActivity;
import com.thu.stlgm.GameActivity;
import com.thu.stlgm.MainActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.util.ConstantUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by SemonCat on 2014/3/13.
 */

@EFragment(R.layout.fragment_photoupload)
public class PhotoUploadFragment extends BaseFragment {

    private static final String TAG = PhotoUploadFragment.class.getName();

    @ViewById
    RelativeLayout PhotoUploadMainLayout;

    @ViewById
    ImageView facebook;

    Activity mActivity;


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;


    private File tmpPhotoPath = new File(Environment.getExternalStorageDirectory(), "photo.jpg");

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        startCamera();

        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

    }

    void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tmpPhotoPath));
        startActivityForResult(intent,
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Click
    void album01(){
        Upload(ConstantUtil.WeekAlbum1);
        finish();
    }

    @Click
    void album02(){
        Upload(ConstantUtil.WeekAlbum2);
        finish();
    }

    void Upload(String album) {

        ((GameActivity) mActivity).ShowCameraUploadProgress();

        File file = tmpPhotoPath;
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (Exception e) {

        }

        if (getActivity()==null) return;

        ((GameActivity) getActivity()).postPhoto(album,bytes, new Request.OnProgressCallback() {
            @Override
            public void onProgress(long current, long max) {
                if (mActivity!=null){
                    ((GameActivity) mActivity).showToast("上傳中："+current+"/"+max);
                }
            }

            @Override
            public void onCompleted(Response response) {
                //Log.d(TAG, "Result:" + response.toString());
                if (mActivity!=null) {
                    ((GameActivity) mActivity).HideCameraUploadProgress();
                }

                if (response.getError() == null) {
                    //facebook.setVisibility(View.VISIBLE);
                    if (mActivity!=null){
                        ((GameActivity) mActivity).showToast("上傳成功！");
                    }
                }else{
                    Log.d(TAG,"Error:"+response.getError());
                    if (mActivity!=null){
                        ((GameActivity) mActivity).showToast("上傳失敗！");
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                PhotoUploadMainLayout.setVisibility(View.VISIBLE);
            }else{
                finish();
            }
        }
    }

    private void finish(){
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
