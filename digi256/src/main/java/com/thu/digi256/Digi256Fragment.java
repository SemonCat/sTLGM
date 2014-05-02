package com.thu.digi256;


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

import com.thu.digi256.Bean.MainGame;
import com.thu.digi256.View.MainView;

import java.io.File;

/**
 * Created by SemonCat on 2014/3/29.
 */
public class Digi256Fragment extends BaseFragment implements MainGame.GameEventListener {

    private static final String TAG = Digi256Fragment.class.getName();

    MainView view;
    private TextView ScoreTextView;
    private TextView PatternCodeTextView;
    private ImageView Redo;
    private int Score;

    private final String GameCode = "m51";

    public final String SDCardPath = Environment.getExternalStorageDirectory().toString();
    public final String TLGM_Student_Path = SDCardPath + File.separator +
            "TLGM_Student";


    private final String GameDataPath = TLGM_Student_Path + "/Game/" + GameCode;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view.game.setListener(this);

        Redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view.game.generatePattern();
                //PatternCodeTextView.setText("類型："+view.game.getPatternCode());
                view.game.newGame();
            }
        });

        Drawable[][] mGameData = getGameData();

        if (mGameData!=null){
            view.setGameResource(mGameData);
            view.game.newGame();
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

        Redo = (ImageView) mView.findViewById(R.id.ButtonRedo);

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
            Log.d(TAG,"FileNotExists");
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
    }

    public void GameFileNotFound() {
        view.setVisibility(View.GONE);
    }
}
