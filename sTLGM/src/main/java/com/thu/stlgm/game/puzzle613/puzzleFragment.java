package com.thu.stlgm.game.puzzle613;



import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.puzzle.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class puzzleFragment extends Fragment implements View.OnDragListener,View.OnTouchListener {
    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;
    private static View onDragedView;
    private ArrayList<FrameLayout> question;
    private ArrayList<FrameLayout> piecesFrameList;
    private int GAMETYPE=0;
    private boolean SHOWCIRCLE=false;
//    FrameLayout piecesFrame[];
    private boolean gameOver = false;

    AnswerLayout mAnswerLayout;

    private static int QUESTIONVIEW_SIZE=450;
    private int IMAGEVIEW_MODE=0;
    private float IMAGEVIEW_ALPHA=0.5f;

//        TODO  answerString對應answerInt=答案

    String centerQuestionString="A";
    String questionString[]={};
    String answerString[] = {"A", "B", "C"};
    int answerInt[] = {R.drawable.opt_o613_01,R.drawable.opt_o613_02,R.drawable.opt_o613_03};
    ArrayList<Integer> getIndex = new ArrayList<Integer>();


    public puzzleFragment setType(int mtype,boolean showCircle){
        this.GAMETYPE = mtype;
        this.SHOWCIRCLE=showCircle;
        return this;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        SCREEN_HEIGHT = p.y;
        SCREEN_WIDTH = p.x;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.puzzle_game, container, false);



        switch (GAMETYPE){
            case 0:
                rootView.setBackgroundResource(R.drawable.bkg_m_09);
                break;
            case 1:
                rootView.setBackgroundResource(R.drawable.bkg_m_10);
        }

        for (int i=0;i<answerInt.length;i++){
            getIndex.add(i);
        }

        mAnswerLayout=new AnswerLayout(getActivity());
        RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(QUESTIONVIEW_SIZE,QUESTIONVIEW_SIZE);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mAnswerLayout.setLayoutParams(mLayoutParams);
        mAnswerLayout.setAlpha(0f);
        mAnswerLayout.setChildSize(180);

        question = new ArrayList<FrameLayout>();

        if (!centerQuestionString.equals("")||centerQuestionString!=null){
            FrameLayout mFrameLayout=new FrameLayout(getActivity());
            mFrameLayout.setClipChildren(false);
            RelativeLayout.LayoutParams mqLayoutParams=new RelativeLayout.LayoutParams(QUESTIONVIEW_SIZE,QUESTIONVIEW_SIZE);
            mqLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

//            mFrameLayout.setBackgroundResource(android.R.color.background_dark);

            TextView mTextView =new TextView(getActivity());
            mTextView.setTextColor(0xFF000000);
            mTextView.setGravity(Gravity.CENTER);

            ImageView mImageView =new ImageView(getActivity());
            mImageView.setScaleType(ImageView.ScaleType.CENTER);
            mImageView.setBackgroundResource(R.drawable.obj_m_smallcircle);
            mImageView.setAlpha(IMAGEVIEW_ALPHA);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            mFrameLayout.addView(mTextView,layoutParams);
            mFrameLayout.addView(mImageView,layoutParams);

            mFrameLayout.setTag(R.string.has_answer, "");
//			mFrameLayout.setTag(R.string.has_answer,Boolean.valueOf(false));
            mFrameLayout.setTag(R.string.answer, centerQuestionString);
            mFrameLayout.setTag(R.string.is_question, Boolean.valueOf(true));
            mFrameLayout.setOnTouchListener(this);
            question.add(mFrameLayout);
            rootView.addView(mFrameLayout,mqLayoutParams);
//            mAnswerLayout.addView(mFrameLayout);
        }

        for (int i=0;i<questionString.length;i++){
            FrameLayout mFrameLayout=new FrameLayout(getActivity());
            mFrameLayout.setClipChildren(false);
//            mFrameLayout.setBackgroundResource(android.R.color.background_dark);

            TextView mTextView =new TextView(getActivity());
            mTextView.setTextColor(0xFF000000);
            mTextView.setGravity(Gravity.CENTER);

            ImageView mImageView =new ImageView(getActivity());
            mImageView.setScaleType(ImageView.ScaleType.CENTER);
            mImageView.setBackgroundResource(R.drawable.obj_m_smallcircle);
            mImageView.setAlpha(IMAGEVIEW_ALPHA);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            mFrameLayout.addView(mTextView,layoutParams);
            mFrameLayout.addView(mImageView,layoutParams);

            mFrameLayout.setTag(R.string.has_answer, "");
//			mFrameLayout.setTag(R.string.has_answer,Boolean.valueOf(false));
            mFrameLayout.setTag(R.string.answer, questionString[i]);
            mFrameLayout.setTag(R.string.is_question, Boolean.valueOf(true));
            mFrameLayout.setOnTouchListener(this);
            question.add(mFrameLayout);
            mAnswerLayout.addView(mFrameLayout);
        }
        for (int i = 0; i < question.size(); ++i)
            question.get(i).setOnDragListener(this);


        rootView.addView(mAnswerLayout);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AnswerLayout mAnswerLayout=new AnswerLayout(getActivity());
        RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(450,450);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        mAnswerLayout.setLayoutParams(mLayoutParams);




        onGameCreate(getActivity(), (ViewGroup) getActivity().findViewById(R.id.game_container),mAnswerLayout);
//        onGameCreate(getActivity(), mAnswerLayout);
        onGameStart();
    }

    private void onGameCreate(Context context, ViewGroup container,AnswerLayout mAnswerLayout) {
//              TODO

        Random random = new Random();
        piecesFrameList = new ArrayList<FrameLayout>();
        for (int i=0;i<answerString.length;i++){
            piecesFrameList.add(new FrameLayout(context));
            piecesFrameList.get(i).addView(new ImageView(context), SCREEN_HEIGHT / 5, SCREEN_HEIGHT / 5);//background
            piecesFrameList.get(i).addView(new ImageView(context), SCREEN_HEIGHT / 5, SCREEN_HEIGHT / 5);//content
        }

        Collections.shuffle(getIndex);
        for (int i = 0; i < piecesFrameList.size(); i++) {
            ((ImageView) piecesFrameList.get(i).getChildAt(0)).setImageResource(R.drawable.obj_m_smallcircle);
            ((ImageView) piecesFrameList.get(i).getChildAt(1)).setImageResource(answerInt[getIndex.get(i)]);
            piecesFrameList.get(i).setTag(answerString[getIndex.get(i)]);
            Log.d("index", "i=" + String.valueOf(i) + ",rI=" + String.valueOf(getIndex.get(i)) + ",String=" + answerString[getIndex.get(i)]);
        }


        int PIECES_INTERVAL = (SCREEN_HEIGHT - getPiecesTotalWidth(piecesFrameList)) / (piecesFrameList.size() + 1);
        int marginTop = 60;
        PIECES_INTERVAL = PIECES_INTERVAL * 2;
        for (int i = 0; i < piecesFrameList.size(); ++i) {
            piecesFrameList.get(i).setAlpha(0f);
            container.addView(piecesFrameList.get(i));

                settingPiece(i, piecesFrameList.get(i), PIECES_INTERVAL * (i) + marginTop, random.nextBoolean() ? random.nextFloat() * 25 : random.nextFloat() * -25);

//                settingPiece(i, piecesFrameList.get(i), PIECES_INTERVAL * (i - 4) + marginTop, random.nextBoolean() ? random.nextFloat() * 25 : random.nextFloat() * -25);


            marginTop += piecesFrameList.get(i).getWidth();
        }
    }

    private void onGameStart() {
        playAnimation();
    }

    private int onGameOver() {
        gameOver = true;
        ImageView left = (ImageView) getActivity().findViewById(R.id.center_bkg_img);
        left.clearAnimation();
        int score = 0;
        for (int i = 0; i < question.size(); ++i) {
            String opt = (String) question.get(i).getTag(R.string.has_answer);
            String answer = (String) question.get(i).getTag(R.string.answer);
            Log.e("", "opt=" + opt);
            Log.e("", "answer=" + answer);
//            if (opt == answer)
//                ++score;
            if (opt.equals("A")) score=1;
            else if(opt.equals("B")) score=2;
            else if(opt.equals("C")) score=3;
        }
        Log.e("", "GameOver Score=" + score);
        return score;
    }

    private void settingPiece(int viewNumber, final FrameLayout piece, int topMargin, float rotation) {

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) piece.getLayoutParams();
        Log.d("piece","topMargin:"+topMargin+" piece:"+piece.getHeight()+" screen H:"+SCREEN_HEIGHT);
        if (topMargin+piece.getHeight()<SCREEN_HEIGHT){
            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rlp.setMargins(50, topMargin, rlp.rightMargin, rlp.bottomMargin);
            Log.d("side","left");
        }else {
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rlp.setMargins(rlp.leftMargin, topMargin-SCREEN_HEIGHT, 50, rlp.bottomMargin);
            Log.d("side","right:"+(topMargin-SCREEN_HEIGHT));
        }

//        if (viewNumber < 4) {
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            rlp.setMargins(50, topMargin, rlp.rightMargin, rlp.bottomMargin);
//        } else {
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            rlp.setMargins(rlp.leftMargin, topMargin, 50, rlp.bottomMargin);
//        }
        //rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //rlp.setMargins(leftMargin, rlp.topMargin, rlp.rightMargin, 30);
        //rlp.setMargins(50,topMargin,rlp.rightMargin,rlp.bottomMargin);
        piece.setRotation(rotation);
        piece.setOnTouchListener(this);
        piece.setOnDragListener(this);
    }

    private int getPiecesTotalWidth(ArrayList<FrameLayout> pieces) {
        int sum = 0;
        for (int i = 0; i < pieces.size(); ++i) {
            sum += pieces.get(i).getWidth();
        }
        return sum;
    }

    private void playAnimation() {
        Activity activity = getActivity();

        ImageView center = (ImageView) activity.findViewById(R.id.center_bkg_img);
//			ImageView right = (ImageView) activity.findViewById(R.id.right);
        //ImageView under = (ImageView) activity.findViewById(R.id.under);
        //ImageView word = (ImageView) activity.findViewById(R.id.words);




        //ValueAnimator leftAnim = ObjectAnimator.ofFloat(left,"translationX",-1000f,0f);
        //ValueAnimator rightAnim = ObjectAnimator.ofFloat(right,"translationX",1000f,0f);
        //ValueAnimator centerAnim = ObjectAnimator.ofFloat(center,"translationY",-1000f,0f);
        //ValueAnimator underAnim = ObjectAnimator.ofFloat(under,"translationY",1000f,0f);

        ValueAnimator centerAlpha = ObjectAnimator.ofFloat(center, "alpha", 0f, 1f);
//			ValueAnimator rightAlpha = ObjectAnimator.ofFloat(right,"alpha",0f,1f);
        //ValueAnimator centerAlpha = ObjectAnimator.ofFloat(center,"alpha",0f,1f);
        //ValueAnimator underAlpha = ObjectAnimator.ofFloat(under,"alpha",0f,1f);

        AnimatorSet centerSet = new AnimatorSet();
//        AnimatorSet rightSet = new AnimatorSet();
        //AnimatorSet centerSet = new AnimatorSet();
        //AnimatorSet underSet = new AnimatorSet();

        centerSet.playTogether(centerAlpha);
//			rightSet.playTogether(rightAlpha);

        //centerSet.playTogether(centerAnim,centerAlpha);
        //underSet.playTogether(underAnim,underAlpha);

        //ValueAnimator wordAlpha = ObjectAnimator.ofFloat(word, "alpha", 0f,1f);

//        ValueAnimator aAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.A), "alpha", 0f, 0.0f);
////			ValueAnimator bAlpha =  ObjectAnimator.ofFloat(activity.findViewById(R.id.B), "alpha", 0f,0.9f);
//        ValueAnimator cAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.C), "alpha", 0f, 0.0f);
//        ValueAnimator dAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.D), "alpha", 0f, 0.0f);


        AnimatorSet questionAnimSet = new AnimatorSet();
//        questionAnimSet.playTogether(aAlpha, cAlpha, dAlpha);

        AnimatorSet bgSet = new AnimatorSet();

        //bgSet.playSequentially(leftSet,rightSet,centerSet,underSet,wordAlpha);
        bgSet.playSequentially(centerSet);
        bgSet.setDuration(600);

        if (SHOWCIRCLE){
            Animation leftAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            leftAnim.setDuration( 10000 );
            leftAnim.setRepeatCount(Animation.INFINITE);
            //leftAnim.setInterpolator(new LinearInterpolator());
            center.setAnimation(leftAnim);
        }

        final ValueAnimator answerlayoutAlpha = ObjectAnimator.ofFloat(mAnswerLayout, "alpha", 0f, 1f);
//        AnimatorSet answerlayoutSet=new AnimatorSet();
//        answerlayoutSet.setDuration(80);
//        answerlayoutSet.playTogether(answerlayoutAlpha);
        AnimatorSet[] animList = new AnimatorSet[piecesFrameList.size() + 2];

        animList[0] = bgSet;

        for (int i = 0; i < piecesFrameList.size(); ++i) {
            ValueAnimator scaleX = ObjectAnimator.ofFloat(piecesFrameList.get(i), "scaleX", 3.0f, 1.0f);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(piecesFrameList.get(i), "scaleY", 3.0f, 1.0f);
            ValueAnimator alpha = ObjectAnimator.ofFloat(piecesFrameList.get(i), "alpha", 0.5f, 1.0f);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(80);
            set.playTogether(scaleX, scaleY, alpha);
            ValueAnimator rotate = ObjectAnimator.ofFloat(piecesFrameList.get(i), "translationX", 0f, 10f, 0f, -10f, 0f);
            rotate.setRepeatCount(5);
            rotate.setDuration(10);
            AnimatorSet set2 = new AnimatorSet();
            set2.playSequentially(set, rotate);
            animList[i + 1] = set2;
        }
        animList[piecesFrameList.size() + 1] = questionAnimSet;
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                answerlayoutAlpha.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
//        answerlayoutAlpha.start();
    }

    private boolean isGameOver() {
        for (int i = 0; i < question.size(); ++i) {
            if (question.get(i).getTag(R.string.has_answer) == "") {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (v == onDragedView)
                    v.setVisibility(View.GONE);

                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //scale start
                ValueAnimator vax = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.4f);
                ValueAnimator vay = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.4f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.setDuration(100);
                animSet.playTogether(vax, vay);
                animSet.start();
//                Log.d("touch","v="+v.getTag());
                //scale end
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                //scale start
                ValueAnimator vax_out = ObjectAnimator.ofFloat(v, "scaleX", 1.4f, 1.0f);
                ValueAnimator vay_out = ObjectAnimator.ofFloat(v, "scaleY", 1.4f, 1.0f);
                AnimatorSet animSet_out = new AnimatorSet();
                animSet_out.playTogether(vax_out, vay_out);
                animSet_out.setDuration(100);
                animSet_out.start();
                //scale end
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (v == onDragedView)
                    if (!event.getResult())
                        v.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DROP:
                //scale start
                ValueAnimator vax_drop = ObjectAnimator.ofFloat(v, "scaleX", 1.4f, 1.0f);
                ValueAnimator vay_drop = ObjectAnimator.ofFloat(v, "scaleY", 1.4f, 1.0f);
                AnimatorSet animSet_drop = new AnimatorSet();
                animSet_drop.playTogether(vax_drop, vay_drop);
                animSet_drop.setDuration(100);
                animSet_drop.start();

                v.setAlpha(200);
                //scale end
                if (v.getTag(R.string.has_answer) != null && v.getTag(R.string.has_answer) == "") {//is question block and has no answer
                    v.setTag(R.string.has_answer, onDragedView.getTag());
                    v.setTag(R.string.has_view, onDragedView);
                    ImageView bg = (ImageView) ((ViewGroup) onDragedView).getChildAt(0);
                    bg.buildDrawingCache();


                    //v.setBackgroundDrawable(new BitmapDrawable(bg.getDrawingCache()));//setBackground
                    //((ImageView)((ViewGroup)v).getChildAt(0)).setImageBitmap(bg.getDrawingCache());//setBackground
                    v.setBackgroundDrawable(new BitmapDrawable(getResources(), bg.getDrawingCache()));

                    ImageView word = (ImageView) ((ViewGroup) onDragedView).getChildAt(1);
                    word.buildDrawingCache();
                    ((ImageView) ((ViewGroup) v).getChildAt(1)).setImageBitmap(word.getDrawingCache());//set words

                } else
                    onDragedView.setVisibility(View.VISIBLE);
                if (isGameOver()) onGameOver();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.d("touchQ","v="+v.getTag());
                if (gameOver) return true;
                if (v.getTag(R.string.is_question) == null) {//Is not a question(i.e. is a option)
                    v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
                    onDragedView = v;
                } else {//Is a question
                    ValueAnimator vax = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.2f);
                    ValueAnimator vay = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.2f);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(vax, vay);
                    set.setDuration(100);
                    set.start();
//					TODO
                    if (v.getTag(R.string.has_view) != null) {
                        View hasView = (View) v.getTag(R.string.has_view);
                        hasView.setVisibility(View.VISIBLE);
                        for (int i=0;i<questionString.length;i++){
                            if (v.getTag(R.string.answer).equals(questionString[i]) ) {
                                v.setBackgroundResource(R.drawable.obj_m_smallcircle);
                                v.setAlpha(IMAGEVIEW_ALPHA);
                            }
                        }
//                        if (v.getTag(R.string.answer) == "A") {
//                            v.setBackgroundResource(R.drawable.obj_m_smallcircle);
//                            v.setAlpha(IMAGEVIEW_ALPHA);
//                        } else if (v.getTag(R.string.answer) == "B") {
//                            v.setBackgroundResource(R.drawable.obj_m_smallcircle);
//                            v.setAlpha(IMAGEVIEW_ALPHA);
//                        } else if (v.getTag(R.string.answer) == "C") {
//                            v.setBackgroundResource(R.drawable.obj_m_smallcircle);
//                            v.setAlpha(IMAGEVIEW_ALPHA);
//                        } else if (v.getTag(R.string.answer) == "D") {
//                            v.setBackgroundResource(R.drawable.obj_m_smallcircle);
//                            v.setAlpha(IMAGEVIEW_ALPHA);
//                        }
                        //v.setBackgroundResource(R.drawable.obj_m_smallcircle);
                        ((ImageView) ((ViewGroup) v).getChildAt(1)).setImageBitmap(null);
                        v.setTag(R.string.has_view, null);
                        v.setTag(R.string.has_answer, "");
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (gameOver) return true;
                if (v.getTag(R.string.is_question) == null) {//Is not a question(i.e. is a option)

                } else {//Is a question
                    ValueAnimator vax = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1.0f);
                    ValueAnimator vay = ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1.0f);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(vax, vay);
                    set.setDuration(100);
                    set.start();

                }
                break;
        }
        return true;
    }

}
