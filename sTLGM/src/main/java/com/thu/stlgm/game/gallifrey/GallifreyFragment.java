package com.thu.stlgm.game.gallifrey;

import android.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thu.stlgm.R;
import com.thu.stlgm.game.gallifrey.view.BoardView;
import com.thu.stlgm.game.BaseGame;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class GallifreyFragment extends BaseGame{

    private BoardView mBoardView;

    private TextView BombCounter;

    private ImageView UFO,UFO_Open,ET_Walk,ET;
    private Animation UFO_Anim,UFO_Open_Anim;

    private FrameLayout AnimLayout;

    private Animation ET_WALK_Path,ET_WEAPON;

    private String LeftBombTip = "剩餘炸彈數量：";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
        setupEvent();
        setupAnim();



    }



    private void setupView(){
        BombCounter = (TextView) getActivity().findViewById(R.id.BombCounter);
        BombCounter.setText(LeftBombTip+2);

        UFO = (ImageView) getActivity().findViewById(R.id.UFO);
        UFO_Open = (ImageView) getActivity().findViewById(R.id.UFO_Open);

        ET_Walk = (ImageView)getActivity().findViewById(R.id.ET_Walk);
        ET_Walk.setBackgroundResource(R.anim.obj_m_etwalk);

        ET = (ImageView)getActivity().findViewById(R.id.ET_WEAPON);
        ET.setBackgroundResource(R.anim.obj_m_et);

        AnimLayout = (FrameLayout) getActivity().findViewById(R.id.AnimLayout);
    }


    private void setupEvent(){
        ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBoardView!=null){
                    mBoardView.Shot();
                }
            }
        });
    }

    private void setupAnim(){
        UFO_Anim = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,
                0f,
                TranslateAnimation.RELATIVE_TO_SELF,
                0f,
                TranslateAnimation.RELATIVE_TO_SELF,
                0f,
                TranslateAnimation.RELATIVE_TO_PARENT,
                0.65f
        );

        UFO_Anim.setDuration(1000);

        UFO_Anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                UFO.setVisibility(View.GONE);
                AnimLayout.setVisibility(View.VISIBLE);


                ((AnimationDrawable)ET_Walk.getBackground()).start();
                ET_Walk.startAnimation(ET_WALK_Path);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ET_WALK_Path = AnimationUtils.loadAnimation(getActivity(),R.anim.et_walk_path);
        ET_WALK_Path.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationDrawable mAnim =  ((AnimationDrawable)ET_Walk.getBackground());
                mAnim.stop();
                ET_Walk.setBackgroundDrawable(null);

                ET.setVisibility(View.VISIBLE);
                ((AnimationDrawable)ET.getBackground()).start();

                setupBoard();
                mBoardView.startGame();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setupBoard(){
        mBoardView = (BoardView) getActivity().findViewById(R.id.BoardView);
        mBoardView.setupChessBoard(MapMgr.MapGenerator(getRound()));
        mBoardView.setListener(new BoardView.OnBombChangeListener() {
            @Override
            public void OnBombAddEvent(int currentBombCounter) {
                BombCounter.setText(LeftBombTip+currentBombCounter);
            }

            @Override
            public void OnBombTypeChangeEvent() {

            }

            @Override
            public void OnBombCancelEvent(int currentBombCounter) {
                BombCounter.setText(LeftBombTip+currentBombCounter);
            }
        });

        mBoardView.setGameOverListener(new BoardView.OnGameOverListener() {
            @Override
            public void OnGameOverEvent(int score) {
                OnGameOver(score);
            }
        });
    }

    @Override
    public void StartGame(){

        UFO.startAnimation(UFO_Anim);

    }


    public void OnGameOver(int score){
        if (score<3){
            GameOver(OverType.Dead,score);
        }else{
            GameOver(OverType.Win,score);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallifrey, container, false);
    }

    @Override
    protected void RestartFragment(int quizid,int counter, int container, FragmentManager manager, OnGameOverListener onGameOverListener) {

        GallifreyFragment gallifreyFragment = new GallifreyFragment();
        gallifreyFragment.setOnGameOverListener(onGameOverListener);

        addFragment(gallifreyFragment,counter,container,manager,onGameOverListener);

    }
}
