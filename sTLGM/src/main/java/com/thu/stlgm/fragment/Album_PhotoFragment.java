package com.thu.stlgm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.thu.stlgm.R;
import com.thu.stlgm.adapter.PhotoAdapter;
import com.thu.stlgm.bean.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class Album_PhotoFragment extends BaseFragment{

    private static final String TAG = PhotoFragment.class.getName();

    public static final String PHOTO = "photo";

    private StaggeredGridView Photo;

    private PhotoAdapter mPhotoAdapter;

    private Handler mHandler;

    private List<com.thu.stlgm.bean.Photo> mPhotoList;

    public static Album_PhotoFragment getInstance(Bundle bundle) {
        Album_PhotoFragment mAlbum_PhotoFragment = new Album_PhotoFragment();
        mAlbum_PhotoFragment.setArguments(bundle);
        return mAlbum_PhotoFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mHandler = new Handler();

        super.onActivityCreated(savedInstanceState);

        getPhoto();


    }




    @Override
    protected void setupView() {
        Photo = (StaggeredGridView) getActivity().findViewById(R.id.PhotoGridView);
    }

    @Override
    protected void setupEvent() {
        getActivity().findViewById(R.id.ClosePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(Album_PhotoFragment.this).commit();
            }
        });

        Photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                if (mPhotoList!=null){
                    mBundle.putParcelableArrayList(PHOTO,new ArrayList<Photo>(
                            mPhotoList));
                }

                mBundle.putInt(PhotoViewFragment.SELECT_PHOTO,position);

                PhotoViewFragment mPhotoViewFragment = PhotoViewFragment.getInstance(mBundle);

                getFragmentManager().beginTransaction().add(R.id.FBContent,mPhotoViewFragment).commit();
            }
        });
    }

    @Override
    protected void setupAdapter() {
        mPhotoAdapter = new PhotoAdapter(getActivity());

        Photo.setAdapter(mPhotoAdapter);

    }

    private void getPhoto(){

        Bundle mBundle = getArguments();
        if (mBundle!=null){
            mPhotoList = mBundle.getParcelableArrayList(PHOTO);
            if (mPhotoList!=null){
                mPhotoAdapter.Refresh(mPhotoList);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_album_photo, container, false);
        return contentView;
    }
}
