package com.thu.stlgm.game;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.thu.stlgm.R;

public class BallsManager {
	public static final int boxWidth=130;
	public static final int boxHeight=130;
	private ArrayList<ImageView> restoredViewList;
	private Context context;
	private ViewGroup container;
	private Random random;
	private int screenWidth;
    private int Type;

    private int picArrayTrue[]=new int[]{};
    private int picArrayFalse[]=new int[]{};

    private boolean answer[] = new boolean[]{true,true,false,true,true,true,false};


	public ColorGenerator colorGenerator;

	public BallsManager(Context context, ViewGroup container,int[] imageFalse,int[] imageTrue,int type) {
		restoredViewList = new ArrayList<ImageView>();
		this.context = context;
		this.container = container;
        this.picArrayFalse=imageFalse;
        this.picArrayTrue=imageTrue;
        Type=type;
		random = new Random();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;
		colorGenerator = new ColorGenerator();
	}

	public ImageView getImageView() {
		if (restoredViewList.isEmpty()) {
			ImageView iv = new ImageView(context);
			//iv.setTag(Boolean.valueOf(false));
            BallObject mBallOBJ = new BallObject(false,answer[new Random().nextInt(answer.length)]);
            iv.setTag(mBallOBJ);
			iv.setOnTouchListener(onTouchListener);
			container.addView(iv);
			return setUpBall(iv);
		} else {
			ImageView iv = restoredViewList.remove(0);
            BallObject mBallOBJ = new BallObject(false,answer[new Random().nextInt(answer.length)]);
            iv.setTag(mBallOBJ);
			return setUpBall(iv);
		}

	}

	public void RestoreView(ImageView restoredView) {
		restoredViewList.add(restoredView);
	}

	// set parameter of ball(just like width、height)
	private ImageView setUpBall(ImageView ball) {
		LayoutParams rlp = (LayoutParams) ball.getLayoutParams();
		rlp.width = boxWidth;
		rlp.height = boxHeight;
		rlp.setMargins(random.nextInt(screenWidth - boxWidth), 0, 0, 0);
		ball.setLayoutParams(rlp);

        BallObject mBallOBJ = (BallObject)ball.getTag();
        if (mBallOBJ.isAnswer()){
            if(Type==0){
                ball.setBackgroundResource(picArrayTrue[new Random().nextInt(picArrayTrue.length)]);
            }else if(Type==1){
                ball.setImageResource(picArrayTrue[new Random().nextInt(picArrayTrue.length)]);
                ball.setBackgroundResource(R.drawable.aerolite3);
            }

            //ball.setBackgroundColor(Color.GREEN);
        }else{
            if(Type==0){
                ball.setBackgroundResource(picArrayFalse[new Random().nextInt(picArrayFalse.length)]);
            }else if(Type==1){
                ball.setImageResource(picArrayFalse[new Random().nextInt(picArrayFalse.length)]);
                ball.setBackgroundResource(R.drawable.aerolite3);
            }
            //ball.setBackgroundColor(Color.RED);
        }



		ball.setAlpha(1.0f);
		ball.setScaleX(1.0f);
		ball.setScaleY(1.0f);
		return ball;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(final View v) {
            BallObject mBallOBJ = (BallObject)v.getTag();
            mBallOBJ.setTouch(true);
			v.setTag(mBallOBJ);
		}
	};

	private OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
            BallObject mBallOBJ = (BallObject)v.getTag();
            mBallOBJ.setTouch(true);
            v.setTag(mBallOBJ);

			return false;
		}
	};
	
	public class ColorGenerator {
		private int colorArr[];
		private Random rand;

		public ColorGenerator() {
			rand = new Random();
			colorArr = new int[] { Color.GREEN,Color.RED };
		}

		public int getColor() {
			return colorArr[rand.nextInt(colorArr.length)];
		}
	}

	public void clearCachedImages(){
		restoredViewList.removeAll(restoredViewList);
	}
}
