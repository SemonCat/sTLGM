package com.thu.stlgm.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thu.stlgm.GameActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.adapter.PlayerInfoAdapter;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.game.BaseGame;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

/**
 * Created by SemonCat on 2014/3/6.
 */
@EFragment(R.layout.fragment_beforegame)
public class BeforeGameFragment extends BaseGame{

    @ViewById
    ImageView MissionContent,NormalText,StartNow;

    @ViewById
    TextView ChangeTip;

    private boolean ShowNormal = false;


    private int resourceId;

    private String[] tips = new String[]{"小周表示：\n麻煩換人好嗎？","統神：\n在不換人小心被告！"};

    @Click
    void StartNow(){
        finishFragment();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();

        String imageUri = "drawable://" + resourceId;
        ImageLoader.getInstance().displayImage(imageUri, MissionContent,options);
        */



        if (ShowNormal){
            NormalText.setVisibility(View.VISIBLE);
        }else{

            /*
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            String imageUri = "drawable://" + resourceId;
            ImageLoader.getInstance().displayImage(imageUri, MissionContent,options);
            */
            MissionContent.setImageResource(resourceId);
        }



        setupStartNow();
        setupLeaderChangeListener();

        GameActivity gameActivity = (GameActivity) getActivity();
        checkPlayState(gameActivity);
    }

    private void setupStartNow(){
        StartNow.setBackgroundResource(R.anim.btn_s_beginmission);
        ((AnimationDrawable)StartNow.getBackground()).start();
    }

    public void setupMissionContent(int resourceId){
        this.resourceId = resourceId;


    }

    private void setupLeaderChangeListener(){
        final GameActivity gameActivity = (GameActivity) getActivity();
        if (gameActivity!=null){
            gameActivity.setLeaderChangeListener(new PlayerInfoAdapter.OnLeaderChangeListener() {
                @Override
                public void OnLeaderChangeEvent(StudentBean leader) {
                    checkPlayState(gameActivity);
                }
            });
        }
    }

    private void checkPlayState(GameActivity gameActivity){
        if (gameActivity.getPlayState()){
            setStartNowVisible(false);
            setChangeTipVisible(true);
        }else{
            setStartNowVisible(true);
            setChangeTipVisible(false);
        }
    }

    private void setStartNowVisible(boolean visible){
        if (visible){
            StartNow.setVisibility(View.VISIBLE);
        }else {
            StartNow.setVisibility(View.GONE);
        }
    }

    private void setChangeTipVisible(boolean visible){
        if (visible){
            ChangeTip.setText(tips[new Random().nextInt(tips.length)]);
            ChangeTip.setVisibility(View.GONE);
        }else {
            ChangeTip.setVisibility(View.GONE);
        }
    }

    public void ShowNormal(){
        ShowNormal = true;
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
