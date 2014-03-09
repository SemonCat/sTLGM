package com.thu.stlgm.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.stlgm.R;
import com.thu.stlgm.game.BaseGame;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by SemonCat on 2014/3/6.
 */
@EFragment(R.layout.fragment_beforegame)
public class BeforeGameFragment extends BaseGame{

    @ViewById
    ImageView MissionContent;

    private int resourceId;

    @Click
    void StartNow(){
        finishFragment();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();

        String imageUri = "drawable://" + resourceId;
        ImageLoader.getInstance().displayImage(imageUri, MissionContent,options);

    }

    public void setupMissionContent(int resourceId){
        this.resourceId = resourceId;


    }

    @Override
    public void onDestroy() {

        /*
        BitmapDrawable bitmapDrawable = (BitmapDrawable)MissionContent.getBackground();

        if (bitmapDrawable!=null){
            bitmapDrawable.getBitmap().recycle();
        }
        */
        super.onDestroy();


    }
}
