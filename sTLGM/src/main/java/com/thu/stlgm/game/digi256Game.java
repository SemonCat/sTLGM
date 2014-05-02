package com.thu.stlgm.game;

import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thu.digi256.View.MainView;
import com.thu.stlgm.R;

import java.io.File;

import static com.thu.digi256.Bean.MainGame.*;

/**
 * Created by SemonCat on 2014/4/24.
 */
public class digi256Game extends BaseGame implements GameEventListener {

    private static final String TAG = digi256Game.class.getName();

    MainView view;
    private TextView ScoreTextView;
    private TextView PatternCodeTextView;
    private ImageView Redo;
    private int Score;

    private static final String GameCode = "m51";

    public static final String SDCardPath = Environment.getExternalStorageDirectory().toString();
    public static final String TLGM_Student_Path = SDCardPath + File.separator +
            "TLGM_Student";


    private static final String GameDataPath = TLGM_Student_Path + "/Game/" + GameCode;

    private int Redo_Counter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view.game.setListener(this);

        Redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view.game.generatePattern();
                //PatternCodeTextView.setText("類型："+view.game.getPatternCode());
                if (Redo_Counter>0){
                    view.game.newGame();
                    Score = 0;
                    ScoreTextView.setText("分數："+Score);
                    Redo_Counter--;
                }
            }
        });

        Drawable[][] mGameData = getGameData();

        if (mGameData!=null){
            view.setGameResource(mGameData);

            PatternCodeTextView.setText("類型："+view.game.getPatternCode());
        }else{
            GameFileNotFound();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_digi256, container, false);

        ScoreTextView = (TextView) mView.findViewById(R.id.Score);

        PatternCodeTextView = (TextView) mView.findViewById(R.id.PatternCode);

        Redo = (ImageView) mView.findViewById(com.thu.digi256.R.id.ButtonRedo);

        view = (MainView) mView.findViewById(R.id.GameView);


        return mView;
    }

    private Drawable[][] getGameData() {

        File mFile = new File(GameDataPath);

        if (mFile.exists() && mFile.isDirectory()) {

            File[] mDataList = mFile.listFiles();
            String[] mDataNameList = mFile.list();

            int q_counter = 0, a_counter = 0;
            int min_counter;

            for (String mDataName : mDataNameList) {
                if (mDataName.startsWith("q")) {
                    q_counter++;
                } else if (mDataName.startsWith("a")) {
                    a_counter++;
                }
            }

            min_counter = q_counter;
            if (a_counter < min_counter) {
                min_counter = a_counter;
            }

            Drawable[][] mGameDataDrawable = new Drawable[min_counter][2];

            /*
            for (int i = 0;i<min_counter;i++){
                String QuestionPath = "/q_"+i+".png";
                String AnswerPath = "/a_"+i+".png";

                Log.d(TAG,"GameDataPath+QuestionPath"+GameDataPath+QuestionPath);

                mGameDataDrawable[i][0] = Drawable.createFromPath(GameDataPath+QuestionPath);
                mGameDataDrawable[i][1] = Drawable.createFromPath(GameDataPath+AnswerPath);
            }
            */

            int Counter = 0;
            for (File mDataFile : mDataList) {
                String FileName = mDataFile.getName();
                if (FileName.startsWith("q")) {

                    String GameCounterString[] = FileName.replace(".png","").split("_");
                    if (GameCounterString.length>=2){
                        int GameCounter;
                        try{
                            GameCounter = Integer.valueOf(GameCounterString[1]);
                        }catch (NumberFormatException mError){
                            return null;
                        }

                        String fileType = ".png";

                        mGameDataDrawable[Counter][0] = Drawable.createFromPath(
                                mDataFile.getPath());
                        mGameDataDrawable[Counter][1] = Drawable.createFromPath(
                                GameDataPath+File.separator+"a_"+GameCounter+fileType);
                    }

                    Counter++;
                }
            }

            return mGameDataDrawable;
        } else {
            Log.d(TAG, "FileNotExists");
            return null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        view.pauseUpdate();

    }

    @Override
    public void onResume() {
        super.onResume();

        view.resumeUpdate();
    }


    @Override
    public void OnGameStart() {

    }

    @Override
    public void OnScoreGet(int score) {

        Score++;
        ScoreTextView.setText("分數："+Score);
        if (Score>=3){
            GameOver(OverType.Win,Score);
        }

    }

    @Override
    public void OnScoreChange(long score) {

    }

    @Override
    public void OnHighScoreChange(long score) {


    }


    @Override
    public void OnGameOver() {
        view.pauseUpdate();
        GameOver(OverType.Dead,Score);
    }

    public void GameFileNotFound() {
        view.setVisibility(View.GONE);
    }


    @Override
    protected void StartGame() {
        Redo_Counter = 1;
        view.game.newGame();
    }



    @Override
    protected void RestartFragment(int quizid, int counter, int container, FragmentManager manager, OnGameOverListener onGameOverListener) {

        digi256Game digi256Game = new digi256Game();

        addFragment(digi256Game, counter, container, manager, onGameOverListener);
    }

}
