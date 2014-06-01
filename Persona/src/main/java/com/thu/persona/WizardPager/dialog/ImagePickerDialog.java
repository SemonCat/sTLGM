package com.thu.persona.WizardPager.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.larswerkman.holocolorpicker.*;
import com.thu.persona.R;
import com.thu.persona.WizardPager.MainActivity;
import com.thu.persona.WizardPager.adapter.GoogleImageAdapter;
import com.thu.persona.WizardPager.bean.ImageResult;
import com.thu.persona.WizardPager.util.GoogleImageSearchHelper;

import java.io.File;
import java.util.List;

/**
 * Created by SemonCat on 2014/5/27.
 */
public class ImagePickerDialog extends DialogFragment implements GoogleImageSearchHelper.OnSearchFinishListener{
    public interface OnPictureSelectListener{
        void OnLocalPictureSelected(File mFile);
        void OnWebPictureSelected(String Url);
    }


    private static final int IMAGE_PICKER_SELECT = 0x12345;

    private OnPictureSelectListener mListener;
    private GoogleImageAdapter mAdapter;
    private GoogleProgressBar SearchProgressBar;
    private GridView mGridView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View mContentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_image_picker, null);

        Button LocalImageButton = (Button)mContentView.findViewById(R.id.LocalImageButton);

        LocalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickFromLocal();
            }
        });

        final SearchView GoogleImageSearchView = (SearchView) mContentView.findViewById(R.id.GoogleImageSearchView);
        GoogleImageSearchView.clearFocus();
        GoogleImageSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GoogleImageSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                GoogleImageSearchHelper.SearchImage(newText,ImagePickerDialog.this);
                SearchProgressBar.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.GONE);
                return false;
            }
        });

        SearchProgressBar = (GoogleProgressBar) mContentView.findViewById(R.id.SearchProgressBar);


        mGridView = (GridView) mContentView.findViewById(R.id.GoogleImage);
        mAdapter = new GoogleImageAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setEmptyView(mContentView.findViewById(R.id.SearchEmptyView));

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (mListener!=null){
                    mListener.OnWebPictureSelected(mAdapter.getItem(position).getFullUrl());
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("大頭貼選擇器")
                .setView(mContentView)
                .setNegativeButton("取消",null)

                .create();
    }

    @Override
    public void OnSearchSuccess(List<ImageResult> mData) {
        for (ImageResult imageResult : mData){
            Log.d("","ImageUrl:"+imageResult.getFullUrl());
        }


        mAdapter.refrush(mData);

        SearchProgressBar.setVisibility(View.GONE);
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnFail(Throwable error) {
        error.printStackTrace();

        SearchProgressBar.setVisibility(View.GONE);
        mGridView.setVisibility(View.VISIBLE);
    }

    public void PickFromLocal(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), IMAGE_PICKER_SELECT);
    }

    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {
            //Bitmap bitmap = getBitmapFromCameraData(data, getActivity());
            dismiss();
            String FilePath = getFilePathFromPickIntent(data,getActivity());
            if (mListener!=null){
                mListener.OnLocalPictureSelected(new File(FilePath));
            }
        }
    }

    public static String getFilePathFromPickIntent(Intent data, Context context){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }


    public DialogFragment setListener(OnPictureSelectListener mListener) {
        this.mListener = mListener;
        return this;
    }
}
