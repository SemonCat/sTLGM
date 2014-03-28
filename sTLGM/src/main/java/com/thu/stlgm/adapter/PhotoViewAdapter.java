package com.thu.stlgm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.stlgm.bean.Photo;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class PhotoViewAdapter extends PagerAdapter {

    private Context mContext;
    private List<Photo> mPhotoList;


    private PhotoViewAttacher.OnPhotoTapListener mListener;

    public PhotoViewAdapter(Context mContext) {
        this.mContext = mContext;
        this.mPhotoList = new ArrayList<Photo>();

    }

    public PhotoViewAdapter(Context mContext, List<Photo> photoList) {
        this.mContext = mContext;
        this.mPhotoList = photoList;

    }


    public void Refresh(List<Photo> photoList){
        this.mPhotoList = photoList;
        notifyDataSetChanged();
    }

    public Photo getItem(int position){
        return mPhotoList.get(position);
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());

        if (mListener!=null){
            photoView.setOnPhotoTapListener(mListener);
        }

        ImageLoader.getInstance().displayImage(mPhotoList.get(position).getSrc_big(),photoView);


        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;

    }


    public void setListener(PhotoViewAttacher.OnPhotoTapListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
