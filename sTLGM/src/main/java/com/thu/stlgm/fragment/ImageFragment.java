package com.thu.stlgm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.stlgm.R;
import com.thu.stlgm.fragment.BaseFragment;

import uk.co.senab.photoview.PhotoView;

/**
 * 老師推播圖片用
 * Created by SemonCat on 2014/5/5.
 */
public class ImageFragment extends BaseFragment{

    private PhotoView mImageContent;

    private String mType = "";

    private static final String[] ImageURL = new String[]{
        "https://dl.dropboxusercontent.com/s/spnvjaddtde2p41/i01.png",
        "https://dl.dropboxusercontent.com/s/vjpoziexw8e7e6g/i02.png",
        "https://dl.dropboxusercontent.com/s/8383ahjnczoo532/i03.png",
        "https://dl.dropboxusercontent.com/s/9kinji6r4zp4smh/i04.png"
    };

    public ImageFragment(String Type){
        this.mType = Type;

    }


    private String getImageUrl(){
        if (mType.equals("i01")){
            return ImageURL[0];
        }else if (mType.equals("i02")){
            return ImageURL[1];
        }else if (mType.equals("i03")){
            return ImageURL[2];
        }else if (mType.equals("i04")){
            return ImageURL[3];
        }else{
            return null;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void setupView() {
        String imageUrl = getImageUrl();

        if (imageUrl!=null){
            ImageLoader.getInstance().displayImage(imageUrl,mImageContent);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_image,container,false);

        mImageContent = (PhotoView) mRootView.findViewById(R.id.ImageContent);


        return mRootView;
    }
}
