package com.thu.stlgm.game;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bugsense.trace.G;
import com.thu.stlgm.R;
import com.thu.stlgm.game.BaseGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*GAME'S MAIN FRAGMENT*/
public class PuzzleFragment extends BaseGame implements View.OnDragListener,View.OnTouchListener {


    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;
    private static View onDragedView;
    private List<FrameLayout> question;
    FrameLayout piecesFrame[];
    private boolean gameOver = false;

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
        return inflater.inflate(R.layout.puzzle_game,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onGameCreate(getActivity(),(ViewGroup)getActivity().findViewById(R.id.game_container));

    }


    @Override
    protected void StartGame() {
        onGameStart();
    }

    private void onGameCreate(Context context,ViewGroup container){

//initialize instance field
        Activity activity = getActivity();
        question = new ArrayList<FrameLayout>();
//set question
        FrameLayout A = (FrameLayout)activity.findViewById(R.id.A);
        A.setTag(R.string.has_answer,"");
// A.setTag(R.string.has_answer,Boolean.valueOf(false));
        A.setTag(R.string.answer,"Art");
        A.setTag(R.string.is_question,Boolean.valueOf(true));
        A.setOnTouchListener(this);
        question.add(A);

        FrameLayout B = (FrameLayout)activity.findViewById(R.id.B);
        B.setTag(R.string.has_answer,"");
// B.setTag(R.string.has_answer,Boolean.valueOf(false));
        B.setTag(R.string.answer,"Life");
        B.setTag(R.string.is_question,Boolean.valueOf(true));
        B.setOnTouchListener(this);
        question.add(B);

        FrameLayout C = (FrameLayout)activity.findViewById(R.id.C);
        C.setTag(R.string.has_answer,"");
// C.setTag(R.string.has_answer,Boolean.valueOf(false));
        C.setTag(R.string.answer,"Content");
        C.setTag(R.string.is_question,Boolean.valueOf(true));
        C.setOnTouchListener(this);
        question.add(C);

        FrameLayout D = (FrameLayout)activity.findViewById(R.id.D);
        D.setTag(R.string.has_answer,"");
// D.setTag(R.string.has_answer,Boolean.valueOf(false));
        D.setTag(R.string.answer,"Form");
        D.setTag(R.string.is_question,Boolean.valueOf(true));
        D.setOnTouchListener(this);
        question.add(D);

        FrameLayout E = (FrameLayout)activity.findViewById(R.id.E);
        E.setTag(R.string.has_answer,"");
// E.setTag(R.string.has_answer,Boolean.valueOf(false));
        E.setTag(R.string.answer,"Combination");
        E.setTag(R.string.is_question,Boolean.valueOf(true));
        E.setOnTouchListener(this);
        question.add(E);

        FrameLayout F = (FrameLayout)activity.findViewById(R.id.F);
        F.setTag(R.string.has_answer,"");
// F.setTag(R.string.has_answer,Boolean.valueOf(false));
        F.setTag(R.string.answer,"Imagination");
        F.setTag(R.string.is_question,Boolean.valueOf(true));
        F.setOnTouchListener(this);
        question.add(F);

        FrameLayout G = (FrameLayout)activity.findViewById(R.id.G);
        G.setTag(R.string.has_answer,"");
// G.setTag(R.string.has_answer,Boolean.valueOf(false));
        G.setTag(R.string.answer,"Subconscious");
        G.setTag(R.string.is_question,Boolean.valueOf(true));
        G.setOnTouchListener(this);
        question.add(G);

        for(int i=0;i<question.size();++i)
            question.get(i).setOnDragListener(this);

        Random random = new Random();
        piecesFrame = new FrameLayout[]{ new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context),
                new FrameLayout(context)};
        for(int i=0;i<piecesFrame.length;++i){
            piecesFrame[i].addView(new ImageView(context));//background
            piecesFrame[i].addView(new ImageView(context));//content
        }




        ((ImageView)piecesFrame[0].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag01);
        ((ImageView)piecesFrame[1].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag02);
        ((ImageView)piecesFrame[2].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag03);
        ((ImageView)piecesFrame[3].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag04);
        ((ImageView)piecesFrame[4].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag05);
        ((ImageView)piecesFrame[5].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag06);
        ((ImageView)piecesFrame[6].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag07);
        ((ImageView)piecesFrame[7].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag08);
        ((ImageView)piecesFrame[8].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag09);
        ((ImageView)piecesFrame[9].getChildAt(0)).setImageResource(R.drawable.btn_m_puzzlefrag10);

        ((ImageView)piecesFrame[0].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_05);
        ((ImageView)piecesFrame[1].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_07);
        ((ImageView)piecesFrame[2].getChildAt(1)).setImageResource(R.drawable.opt_m21_f_03);
        ((ImageView)piecesFrame[3].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_01);
        ((ImageView)piecesFrame[4].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_02);
        ((ImageView)piecesFrame[5].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_03);
        ((ImageView)piecesFrame[6].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_04);
        ((ImageView)piecesFrame[7].getChildAt(1)).setImageResource(R.drawable.opt_m21_f_01);
        ((ImageView)piecesFrame[8].getChildAt(1)).setImageResource(R.drawable.opt_m21_t_06);
        ((ImageView)piecesFrame[9].getChildAt(1)).setImageResource(R.drawable.opt_m21_f_02);

        piecesFrame[0].setTag("Form");
        piecesFrame[1].setTag("Subconscious");
        piecesFrame[2].setTag("Story");
        piecesFrame[3].setTag("Life");
        piecesFrame[4].setTag("Content");
        piecesFrame[5].setTag("Imagination");
        piecesFrame[6].setTag("Combination");
        piecesFrame[7].setTag("Subconscious");
        piecesFrame[8].setTag("Art");
        piecesFrame[9].setTag("Value");

/*
*
* setting property
*
*/
        final int PIECES_INTERVAL = (SCREEN_WIDTH-getPiecesTotalWidth(piecesFrame))/(piecesFrame.length+1);
        int marginLeft=0;
        for(int i=0;i<piecesFrame.length;++i){
            piecesFrame[i].setAlpha(0f);
            container.addView(piecesFrame[i]);
            settingPiece(piecesFrame[i],PIECES_INTERVAL*(i+1)+marginLeft,random.nextBoolean()?random.nextFloat()*25:random.nextFloat()*-25);
            marginLeft += piecesFrame[i].getWidth();
        }
    }

    private void onGameStart(){
        playAnimation();
    }

    private int onGameOver(){
        gameOver = true;
        int score=0;
        for(int i=0;i<question.size();++i){
            String opt = (String) question.get(i).getTag(R.string.has_answer);
            String answer = (String) question.get(i).getTag(R.string.answer);
            Log.e("", "opt=" + opt);
            Log.e("","answer="+answer);
            if(opt==answer)
                ++score;
        }
        Log.e("","GameOver Score="+score);

        if (score==7){
            GameOver(OverType.Win,score);
        }else{
            GameOver(OverType.Dead,score);
        }


        return score;
    }

    private void settingPiece(final FrameLayout piece,int leftMargin,float rotation){

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) piece.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlp.setMargins(leftMargin, rlp.topMargin, rlp.rightMargin, 30);
        piece.setRotation(rotation);
        piece.setOnTouchListener(this);
        piece.setOnDragListener(this);
    }

    private int getPiecesTotalWidth(FrameLayout pieces[]){
        int sum=0;
        for(int i=0;i<pieces.length;++i){
            sum+=pieces[i].getWidth();
        }
        return sum;
    }

    private void playAnimation(){
        Activity activity = getActivity();
        ImageView left = (ImageView) activity.findViewById(R.id.left);
        ImageView center = (ImageView) activity.findViewById(R.id.center);
        ImageView right = (ImageView) activity.findViewById(R.id.right);
        ImageView under = (ImageView) activity.findViewById(R.id.under);
        ImageView word = (ImageView) activity.findViewById(R.id.words);

        ValueAnimator leftAnim = ObjectAnimator.ofFloat(left,"translationX",-1000f,0f);
        ValueAnimator rightAnim = ObjectAnimator.ofFloat(right,"translationX",1000f,0f);
        ValueAnimator centerAnim = ObjectAnimator.ofFloat(center,"translationY",-1000f,0f);
        ValueAnimator underAnim = ObjectAnimator.ofFloat(under,"translationY",1000f,0f);

        ValueAnimator leftAlpha = ObjectAnimator.ofFloat(left,"alpha",0f,1f);
        ValueAnimator rightAlpha = ObjectAnimator.ofFloat(right,"alpha",0f,1f);
        ValueAnimator centerAlpha = ObjectAnimator.ofFloat(center,"alpha",0f,1f);
        ValueAnimator underAlpha = ObjectAnimator.ofFloat(under,"alpha",0f,1f);

        AnimatorSet leftSet = new AnimatorSet();
        AnimatorSet rightSet = new AnimatorSet();
        AnimatorSet centerSet = new AnimatorSet();
        AnimatorSet underSet = new AnimatorSet();

        leftSet.playTogether(leftAnim,leftAlpha);
        rightSet.playTogether(rightAnim,rightAlpha);
        centerSet.playTogether(centerAnim,centerAlpha);
        underSet.playTogether(underAnim,underAlpha);

        ValueAnimator wordAlpha = ObjectAnimator.ofFloat(word, "alpha", 0f,1f);

        ValueAnimator aAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.A), "alpha", 0f,0.8f);
        ValueAnimator bAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.B), "alpha", 0f,0.8f);
        ValueAnimator cAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.C), "alpha", 0f,0.8f);
        ValueAnimator dAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.D), "alpha", 0f,0.8f);
        ValueAnimator eAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.E), "alpha", 0f,0.8f);
        ValueAnimator fAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.F), "alpha", 0f,0.8f);
        ValueAnimator gAlpha = ObjectAnimator.ofFloat(activity.findViewById(R.id.G), "alpha", 0f,0.8f);

        AnimatorSet questionAnimSet = new AnimatorSet();
        questionAnimSet.playTogether(aAlpha,bAlpha,cAlpha,dAlpha,eAlpha,fAlpha,gAlpha);

        AnimatorSet bgSet = new AnimatorSet();

        bgSet.playSequentially(leftSet,rightSet,centerSet,underSet,wordAlpha);
        bgSet.setDuration(600);

        AnimatorSet[] animList = new AnimatorSet[piecesFrame.length+2];

        animList[0] = bgSet;

        for(int i=0;i<piecesFrame.length;++i){
            ValueAnimator scaleX = ObjectAnimator.ofFloat(piecesFrame[i], "scaleX", 3.0f,1.0f);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(piecesFrame[i], "scaleY", 3.0f,1.0f);
            ValueAnimator alpha = ObjectAnimator.ofFloat(piecesFrame[i], "alpha", 0.5f,1.0f);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(80);
            set.playTogether(scaleX,scaleY,alpha);
            ValueAnimator rotate = ObjectAnimator.ofFloat(piecesFrame[i], "translationX",0f,10f,0f,-10f,0f);
            rotate.setRepeatCount(5);
            rotate.setDuration(10);
            AnimatorSet set2 = new AnimatorSet();
            set2.playSequentially(set,rotate);
            animList[i+1] = set2;
        }
        animList[piecesFrame.length+1] = questionAnimSet;
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    private boolean isGameOver(){
        for(int i=0;i<question.size();++i){
            if(question.get(i).getTag(R.string.has_answer)==""){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                if(v==onDragedView)
                    v.setVisibility(View.GONE);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//scale start
                ValueAnimator vax = ObjectAnimator.ofFloat(v, "scaleX", 1.0f,1.4f);
                ValueAnimator vay = ObjectAnimator.ofFloat(v, "scaleY", 1.0f,1.4f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.setDuration(100);
                animSet.playTogether(vax,vay);
                animSet.start();
//scale end
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//scale start
                ValueAnimator vax_out = ObjectAnimator.ofFloat(v, "scaleX", 1.4f,1.0f);
                ValueAnimator vay_out = ObjectAnimator.ofFloat(v, "scaleY", 1.4f,1.0f);
                AnimatorSet animSet_out = new AnimatorSet();
                animSet_out.playTogether(vax_out,vay_out);
                animSet_out.setDuration(100);
                animSet_out.start();
//scale end
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if(v==onDragedView)
                    if(!event.getResult())
                        v.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DROP:
//scale start
                ValueAnimator vax_drop = ObjectAnimator.ofFloat(v, "scaleX", 1.4f,1.0f);
                ValueAnimator vay_drop = ObjectAnimator.ofFloat(v, "scaleY", 1.4f,1.0f);
                AnimatorSet animSet_drop = new AnimatorSet();
                animSet_drop.playTogether(vax_drop,vay_drop);
                animSet_drop.setDuration(100);
                animSet_drop.start();
//scale end
                if(v.getTag(R.string.has_answer)!=null && v.getTag(R.string.has_answer)==""){//is question block and has no answer
                    v.setTag(R.string.has_answer,onDragedView.getTag());
                    v.setTag(R.string.has_view, onDragedView);
                    ImageView bg = (ImageView) ((ViewGroup)onDragedView).getChildAt(0);
                    bg.buildDrawingCache();
                    v.setBackgroundDrawable(new BitmapDrawable(bg.getDrawingCache()));//setBackground

                    ImageView word = (ImageView) ((ViewGroup)onDragedView).getChildAt(1);
                    word.buildDrawingCache();
                    ((ImageView)((ViewGroup)v).getChildAt(1)).setImageBitmap(word.getDrawingCache());//set words
                }
                else
                    onDragedView.setVisibility(View.VISIBLE);
                if(isGameOver()) onGameOver();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(gameOver) return true;
                if(v.getTag(R.string.is_question)==null){//Is not a question(i.e. is a option)
                    v.startDrag(null,new View.DragShadowBuilder(v), null, 0);
                    onDragedView = v;
                }
                else{//Is a question
                    ValueAnimator vax = ObjectAnimator.ofFloat(v, "scaleX", 1.0f,1.2f);
                    ValueAnimator vay = ObjectAnimator.ofFloat(v, "scaleY", 1.0f,1.2f);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(vax,vay);
                    set.setDuration(100);
                    set.start();

                    if(v.getTag(R.string.has_view)!=null){
                        View hasView = (View) v.getTag(R.string.has_view);
                        hasView.setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.btn_m_puzzlefrag11);
                        ((ImageView)((ViewGroup)v).getChildAt(1)).setImageBitmap(null);
                        v.setTag(R.string.has_view, null);
                        v.setTag(R.string.has_answer, "");
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if(gameOver) return true;
                if(v.getTag(R.string.is_question)==null){//Is not a question(i.e. is a option)

                }
                else{//Is a question
                    ValueAnimator vax = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1.0f);
                    ValueAnimator vay = ObjectAnimator.ofFloat(v, "scaleY", 1.2f,1.0f);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(vax,vay);
                    set.setDuration(100);
                    set.start();
                }
                break;
        }
        return true;
    }


    @Override
    public void RestartFragment(int quizid,int counter,int container,FragmentManager manager,OnGameOverListener onGameOverListener) {

        PuzzleFragment puzzleFragment = new PuzzleFragment();
        puzzleFragment.setOnGameOverListener(onGameOverListener);


        addFragment(puzzleFragment,counter,container,manager,onGameOverListener);

    }
}
