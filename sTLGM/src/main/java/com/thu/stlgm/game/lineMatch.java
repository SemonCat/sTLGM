package com.thu.stlgm.game;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.thu.stlgm.R;

import java.util.List;
import java.util.Random;
import lineMatch.GridObject;
import lineMatch.lineMatchView;


public class lineMatch extends BaseGame {

    private lineMatchView drawView;
    private Button Submit;
//    String questionArray[][]=new String[5][5];
    GridObject questionArray[][]=new GridObject[5][5];
    List<String> adf;
    int viewHeight=666;
    int viewWidth=666;
    String answerTextTrue[]={"在與創新對象互動過程中的個人體驗"};
    String answerTextFalse[]={"使用者對過去產品的使用經驗","在創新過程中對使用者的喜好經驗","使用者對創新的接受程度"};
    int answerTrueSide[]={0,1,4,0};
    int answerFalseSide[]={0,3,3,0};

    public lineMatch() {
        // Required empty public constructor
    }



    public void startGame(){
        drawView.setStart(true);
        Submit.setEnabled(true);
    }

    public void setQuestionArray() {


        for (int i=0;i<questionArray.length;i++){
            for (int j=0 ;j<questionArray[0].length;j++){
                questionArray[i][j] = new GridObject();
                questionArray[i][j].setAnswerString("");
                questionArray[i][j].setAnswer(false);
                questionArray[i][j].setQuestion(false);
                questionArray[i][j].setGridColor(-1);
            }
        }
    }
    private void setQuestion(int x,int y,int GridColor,String answerString,int direction){
        questionArray[y][x].setQuestion(true);
        questionArray[y][x].setGridColor(GridColor);
        questionArray[y][x].setAnswerString(answerString);
        questionArray[y][x].setDirection(direction);
    }
    private void setAnswer(int x,int y,String answerString,int direction){
        questionArray[y][x].setAnswer(true);
        questionArray[y][x].setAnswerString(answerString);
        questionArray[y][x].setDirection(direction);
    }
    private void setBlock(int x,int y){
        questionArray[y][x].setQuestion(true);
        questionArray[y][x].setGridColor(R.drawable.obj_o_itemdelete);
        questionArray[y][x].setAnswerString("X");
        questionArray[y][x].setDirection(0);
    }
    private void setGame(int number){
        if (number==0){
            setQuestion(3,1,R.drawable.top_m_uxup,"A",0);
            setAnswer(0,0,"A",3);
            setBlock(1,3);
            setBlock(3,2);
        }else if (number==1){
            setQuestion(1,3,R.drawable.top_m_uxdown,"A",2);
            setAnswer(0,1,"A",3);
            setBlock(0,0);
            setBlock(0,3);
            setBlock(0,4);
        }else if (number==2){
            setQuestion(2,2,R.drawable.top_m_uxdown,"A",2);
            setAnswer(0,4,"A",3);
            setBlock(2,1);
            setBlock(3,1);
            setBlock(1,3);
            setBlock(1,4);
        }else if (number==3){
            setQuestion(1,3,R.drawable.top_m_uxleft,"A",3);
            setAnswer(0,0,"A",3);
            setBlock(2,1);
            setBlock(0,4);
            setBlock(1,4);
            setBlock(2,4);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int randomInt=new Random().nextInt(4);

        drawView=(lineMatchView) getActivity().findViewById(R.id.drawingView);

        setQuestionArray();
        setGame(randomInt);
        //initQuestionArray();
        //questionArray[3][2]="A,Red";
       // questionArray[1][2]="B,Red";
        drawView.setupDrawView(questionArray);
        drawView.invalidate();
        Submit=(Button)getActivity().findViewById(R.id.submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO
                drawView.OnSubmitClick();
                onGameOver(drawView.checkTube());
            }
        });
//        int viewHeight=666;

        RelativeLayout mRelativeLayout=(RelativeLayout)getActivity().findViewById(R.id.lineMatch);
        ViewTreeObserver mViewTreeObserver = drawView.getViewTreeObserver();
        if (mViewTreeObserver!=null){
            mViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    viewHeight=drawView.getHeight();
                    viewWidth=drawView.getWidth();
//                    Log.d("drawView", "width=" + drawView.getWidth() + ",height=" + drawView.getHeight());
                }
            });
        }
        int eachWidth=viewWidth/questionArray[0].length;
        int eachHeight=viewHeight/questionArray.length;



        RelativeLayout.LayoutParams answerParamsTrue =new RelativeLayout.LayoutParams(eachWidth+1,eachHeight+1);
//        layoutParams.leftMargin=178;
//        layoutParams.topMargin=567;
        answerParamsTrue.addRule(RelativeLayout.ALIGN_TOP,R.id.drawingView);
        answerParamsTrue.addRule(RelativeLayout.ALIGN_LEFT,R.id.drawingView);
        answerParamsTrue.topMargin=answerTrueSide[randomInt]*eachHeight-1;
        answerParamsTrue.leftMargin=-eachHeight-1;
        TextView answerTrue=new TextView(getActivity());
        answerTrue.setLayoutParams(answerParamsTrue);
        answerTrue.setBackgroundResource(R.drawable.grid_touch);
        answerTrue.setPadding(10,10,10,10);
        answerTrue.setText(answerTextTrue[new Random().nextInt(answerTextTrue.length)]);
        answerTrue.setTextColor(Color.BLACK);
        answerTrue.setTextSize(18);
        answerTrue.setGravity(Gravity.CENTER);
        mRelativeLayout.addView(answerTrue);

        RelativeLayout.LayoutParams answerParamsFalse =new RelativeLayout.LayoutParams(eachWidth+1,eachHeight+1);
        answerParamsFalse.addRule(RelativeLayout.ALIGN_TOP,R.id.drawingView);
        answerParamsFalse.addRule(RelativeLayout.ALIGN_RIGHT,R.id.drawingView);
        answerParamsFalse.rightMargin=-eachWidth-1;
        answerParamsFalse.topMargin=answerFalseSide[randomInt]*eachHeight-1;
        TextView answerFalse=new TextView(getActivity());
        answerFalse.setLayoutParams(answerParamsFalse);
        answerFalse.setBackgroundResource(R.drawable.grid_touch);
        answerFalse.setPadding(10,10,10,10);
        answerFalse.setText(answerTextFalse[new Random().nextInt(answerTextFalse.length)]);
        answerFalse.setTextColor(Color.BLACK);
        answerFalse.setTextSize(18);
        answerFalse.setGravity(Gravity.CENTER);
        mRelativeLayout.addView(answerFalse);


    }


    public void initQuestionArray(){
        for(int i=0;i<questionArray.length;i++){
            for(int j=0;j<questionArray[0].length;j++){
//                questionArray[i][j]=String.valueOf(i)+String.valueOf(j)+","+"Red";
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_line_match_game, container, false);
    }


    public void onGameOver(boolean result){
        if (result){
            GameOver(OverType.Win,3);
        }else{
            GameOver(OverType.Dead,0);
        }
    }

    @Override
    protected void StartGame() {
        startGame();
    }

    @Override
    protected void RestartFragment(int quizid, int counter, int container, FragmentManager manager, OnGameOverListener onGameOverListener) {

        lineMatch mlineMatch = new lineMatch();
        mlineMatch.setOnGameOverListener(onGameOverListener);

        addFragment(mlineMatch, counter, container, manager, onGameOverListener);
    }

}
