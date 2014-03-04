package com.thu.stlgm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Response;
import com.thu.stlgm.GameActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.component.CameraView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by SemonCat on 2014/3/4.
 */
@EFragment(R.layout.fragment_photo)
public class PhotoFragment extends BaseFragment{
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    @ViewById
    ImageView Photo;

    @ViewById
    ProgressBar PhotoProgressbar;

    private File tmpPhotoPath = new File(Environment.getExternalStorageDirectory(),"photo.jpg");

    @Click
    void Upload(){
        if (getActivity()!=null){

            File file = tmpPhotoPath;
            int size = (int) file.length();
            byte[] bytes = new byte[size];

            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (Exception e) {

            }

            ((GameActivity)getActivity()).postPhoto(bytes, new Request.OnProgressCallback() {
                @Override
                public void onProgress(long current, long max) {
                    PhotoProgressbar.setMax((int) max);
                    PhotoProgressbar.setProgress((int) current);
                }

                @Override
                public void onCompleted(Response response) {

                }
            });
        }
    }

    @Click
    void startCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tmpPhotoPath));
        startActivityForResult(intent,
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bitmapSrc = BitmapFactory.decodeFile(tmpPhotoPath.getAbsolutePath());

                // Convert ByteArray to Bitmap::

                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmapSrc, Photo.getWidth(), Photo.getHeight(), false);
                bitmapSrc.recycle();
                Photo.setImageBitmap(newBitmap);

            }
        }
    }

}
