package com.thu.stlgm.game;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.thu.stlgm.R;

public class PillarsManager{
	private ArrayList<ImageView> restoredViewList;
    private ArrayList<ImageView> restoredStongList;//stong
	private Context context;
	private ViewGroup container;
    private ViewGroup StongContainer;
	private Random random;
    private int picArrayTrue[];
    private int picArrayFalse[];
	private int screenWidth;
	private int heightRandomHeighten;
	private int heightRandomMin;
	public PillarsManager(Context context,ViewGroup container,int[] imageFalse,int[] imageTrue){
		restoredViewList = new ArrayList<ImageView>();
        restoredStongList= new ArrayList<ImageView>();//stong
		this.context = context;
		this.container = container;
        this.picArrayFalse=imageFalse;
        this.picArrayTrue=imageTrue;
		random = new Random();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;
		heightRandomHeighten = (int) (displaymetrics.heightPixels*0.1);
		heightRandomMin = (int) (displaymetrics.heightPixels*0.3);
		
	}
    public ImageView getImageView(ImageView book){
        if(restoredViewList.isEmpty()){
            ImageView iv = new ImageView(context);
            container.addView(iv);
            book.bringToFront();
            return setUpPillar(iv);
        }
        else{
            return setUpPillar(restoredViewList.remove(0));
        }

    }
	public void RestoreView(ImageView restoredView){
		restoredViewList.add(restoredView);
	}

	//set parameter of Pillar(just like width、height)
	private ImageView setUpPillar(ImageView pillar){
		LayoutParams rlp = (LayoutParams) pillar.getLayoutParams();

		if(random.nextBoolean()){
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);// 0 means false
            pillar.setBackgroundResource(R.anim.obj_m_fflappypost);

            pillar.setScaleType(ImageView.ScaleType.FIT_START);

		}
		else{
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);// 0 means false
            pillar.setBackgroundResource(R.anim.obj_m_rflappypost);

            pillar.setScaleType(ImageView.ScaleType.FIT_END);
		}

        ((AnimationDrawable)pillar.getBackground()).start();
		
		rlp.width=120;
		rlp.height=random.nextInt(heightRandomHeighten)+heightRandomMin;
		
		rlp.setMargins(screenWidth, rlp.topMargin, rlp.rightMargin, rlp.bottomMargin);
		pillar.setLayoutParams(rlp);

        pillar.setTag(random.nextBoolean());
        if(!Boolean.valueOf(pillar.getTag().toString())){
            int randomImage = new Random().nextInt(picArrayFalse.length);
            //Log.d("random1",String.valueOf(randomImage));
            //pillar.setBackgroundColor(Color.BLACK);
            pillar.setImageResource(picArrayFalse[randomImage]);
            //pillar.setScaleType();
            //pillar.setBackgroundResource(picArrayFalse[randomImage]);

        }
        else {
            int randomImage2 = new Random().nextInt(picArrayTrue.length);
            //Log.d("random2",String.valueOf(randomImage2));
            //pillar.setBackgroundColor(Color.BLACK);
            pillar.setImageResource(picArrayTrue[randomImage2]);
            //pillar.setBackgroundResource(picArrayTrue[randomImage2]);

        }

		
		return pillar;
	}

	
}
