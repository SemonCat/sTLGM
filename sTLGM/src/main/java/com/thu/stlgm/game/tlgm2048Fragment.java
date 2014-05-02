package com.thu.stlgm.game;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semoncat.tlgm2048.Bean.MainGame;
import com.semoncat.tlgm2048.View.View2048;
import com.thu.stlgm.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class tlgm2048Fragment extends BaseGame implements MainGame.GameEventListener {

    private View2048 GameView;

    private TextView Button1TextView;
    private TextView Button2TextView;

    private ImageView Question;

    private FrameLayout Button1FrameLayout;
    private FrameLayout Button2FrameLayout;

    private String[] mAnswerArray = new String[]{"無", "不", "在", "社", "所", "群"};


    private String Button1 = "無所不在";
    private String Button2 = "社群";

    private boolean Answer = new Random().nextBoolean();

    Set<Integer> mButton1Answer = new HashSet<Integer>();
    Set<Integer> mButton2Answer = new HashSet<Integer>();

    private final static int[] trueDrawable = new int[]{
            com.semoncat.tlgm2048.R.drawable.opt_m53_01,
            com.semoncat.tlgm2048.R.drawable.opt_m53_02,
            com.semoncat.tlgm2048.R.drawable.opt_m53_03,
            com.semoncat.tlgm2048.R.drawable.opt_m53_04,
    };

    private final static int[] falseDrawable = new int[]{
            com.semoncat.tlgm2048.R.drawable.opt_m53_05,
            com.semoncat.tlgm2048.R.drawable.opt_m53_06,
            com.semoncat.tlgm2048.R.drawable.opt_m53_07,
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        GameView.game.setListener(this);


        mButton1Answer.add(0);
        UpdateButton();


        int drawable;
        if (Answer){
            drawable = trueDrawable[new Random().nextInt(trueDrawable.length)];
        }else{
            drawable = falseDrawable[new Random().nextInt(falseDrawable.length)];
        }
        Question.setImageResource(drawable);

        Button1FrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckButton1Finish() && Answer){
                    GameOver(OverType.Win,0);
                }else{
                    GameOver(OverType.Dead,0);
                }
            }
        });

        Button2FrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckButton2Finish() && !Answer){
                    GameOver(OverType.Win, 0);
                }else{
                    GameOver(OverType.Dead,0);
                }
            }
        });
    }

    private Spanned getButtonText(String src, Integer... HighLight) {
        List<Integer> mHighLight = Arrays.asList(HighLight);

        StringBuilder TextBuilder = new StringBuilder();

        for (int i = 0; i < src.length(); i++) {
            if (HighLight.length > 0) {

                char mChar = src.charAt(i);
                String mString;
                if (!mHighLight.contains(i)) {
                    mString = "<font color='gray'>" + mChar + "</font>";
                } else {
                    mString = String.valueOf(mChar);
                }

                TextBuilder.append(mString);

            } else {
                char mChar = src.charAt(i);
                String mString = "<font color='gray'>" + mChar + "</font>";
                TextBuilder.append(mString);
            }
        }

        return Html.fromHtml(TextBuilder.toString());
    }

    private void UpdateButton() {
        Button1TextView.setText(getButtonText(Button1, mButton1Answer.toArray(new Integer[mButton1Answer.size()])));
        Button2TextView.setText(getButtonText(Button2, mButton2Answer.toArray(new Integer[mButton2Answer.size()])));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mContentView = inflater.inflate(com.semoncat.tlgm2048.R.layout.fragment_tlgm2048, container, false);

        GameView = (View2048) mContentView.findViewById(com.semoncat.tlgm2048.R.id.GameArea_2048);

        Button1TextView = (TextView) mContentView.findViewById(com.semoncat.tlgm2048.R.id.Button1TextView);
        Button2TextView = (TextView) mContentView.findViewById(com.semoncat.tlgm2048.R.id.Button2TextView);

        Button1FrameLayout = (FrameLayout) mContentView.findViewById(com.semoncat.tlgm2048.R.id.Button1);
        Button2FrameLayout = (FrameLayout) mContentView.findViewById(com.semoncat.tlgm2048.R.id.Button2);

        Question = (ImageView) mContentView.findViewById(com.semoncat.tlgm2048.R.id.Question);

        return mContentView;
    }


    @Override
    public void OnGameStart() {

    }

    @Override
    public void OnScoreGet(int score) {
        switch (score) {
            case 4:
                mButton1Answer.add(2);
                break;
            case 8:
                mButton1Answer.add(3);
                break;
            case 16:
                mButton2Answer.add(0);
                break;
            case 32:
                mButton1Answer.add(1);
                break;
            case 64:
                mButton2Answer.add(1);
                break;
            case 128:

                break;
            case 256:

                break;
        }

        if (CheckButton1Finish() && CheckButton2Finish()){
            GameView.setVisibility(View.GONE);
        }
        UpdateButton();
    }

    private boolean CheckButton1Finish(){
        return (mButton1Answer.size()==Button1.length());
    }

    private boolean CheckButton2Finish(){
        return (mButton2Answer.size()==Button2.length());
    }

    @Override
    public void OnScoreChange(long score) {

    }

    @Override
    public void OnHighScoreChange(long score) {

    }

    @Override
    public void OnGameOver() {
        GameOver(OverType.Dead,0);
    }

    @Override
    protected void StartGame() {
        GameView.game.newGame();
    }



    @Override
    protected void RestartFragment(int quizid, int counter, int container, FragmentManager manager, OnGameOverListener onGameOverListener) {

        tlgm2048Fragment tlgm2048Fragment = new tlgm2048Fragment();

        addFragment(tlgm2048Fragment, counter, container, manager, onGameOverListener);
    }

}
