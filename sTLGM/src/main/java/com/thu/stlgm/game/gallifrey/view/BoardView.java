package com.thu.stlgm.game.gallifrey.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


import com.thu.stlgm.R;
import com.thu.stlgm.game.gallifrey.bean.BaseChess;
import com.thu.stlgm.game.gallifrey.bean.Bomb;
import com.thu.stlgm.game.gallifrey.bean.ChessBoard;
import com.thu.stlgm.game.gallifrey.bean.Empty;
import com.thu.stlgm.game.gallifrey.bean.LeftistBomb;
import com.thu.stlgm.game.gallifrey.bean.RightistBomb;
import com.thu.stlgm.game.gallifrey.bean.Star;
import com.thu.stlgm.game.gallifrey.bean.StraightLineBomb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class BoardView extends ViewGroup implements View.OnClickListener,
        View.OnTouchListener{

    public interface OnBombChangeListener{
        /**
         *
         * @param currentBombCounter 已經用了幾顆炸彈
         */
        void OnBombAddEvent(int currentBombCounter);
        void OnBombTypeChangeEvent();
        void OnBombCancelEvent(int currentBombCounter);
    }


    public interface OnGameOverListener{
        void OnGameOverEvent(int score);
    }

    private static final String TAG = BoardView.class.getName();

    private ChessBoard mChessBoard;

    private int mChildSize = dip2px(135);

    private Map<Point,ChessView> mChessViewMgr;

    private boolean IsStart;

    private int score;
    private boolean IsWrong;

    private Handler mHandler;

    private OnBombChangeListener mListener;

    private OnGameOverListener mGameOverListener;

    private boolean IsTargetBombArea;

    private Integer[] mTrueArray = new Integer[]{R.drawable.opt_m23_t_01,
            R.drawable.opt_m23_t_02,
            R.drawable.opt_m23_t_03};

    private Integer[] mFalseArray = new Integer[]{R.drawable.opt_m23_f_01,
            R.drawable.opt_m23_f_02,
            R.drawable.opt_m23_f_03,
            R.drawable.opt_m23_f_04,};

    private List<Integer> mTrueList = new ArrayList<Integer>(Arrays.asList(mTrueArray));
    private List<Integer> mFalseList = new ArrayList<Integer>(Arrays.asList(mFalseArray));


    //最多炸彈數量
    private int MaxBomb = 2;

    private int BombCounter = 0;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        IsStart = false;
        mHandler = new Handler();
        mChessBoard = new ChessBoard(0,0);
        IsTargetBombArea = false;
        score = 0;
        IsWrong = false;

        mChessViewMgr = new HashMap<Point, ChessView>();
        MapBoard();
    }



    public void startGame(){
        IsStart = true;
    }


    public void setupChessBoard(ChessBoard chessBoard){
        this.mChessBoard = chessBoard;
        MapBoard();
    }

    private void MapBoard(){
        removeAllViews();
        for (int i=0;i<mChessBoard.getWidth();i++){
            for (int j=0;j<mChessBoard.getHeight();j++){

                BaseChess mBaseChess = mChessBoard.getChess(i,j);

                ChessView mChessView = new ChessView(getContext(),
                        mBaseChess);
                mChessView.setOnClickListener(this);
                //mChessView.setOnTouchListener(this);

                if (mBaseChess instanceof Star){
                    Star mStar = (Star)mBaseChess;

                    if (mStar.IsAnswer()){
                        mChessView.addQuestion(getResources().getDrawable(getTrueImageId()));
                    }else{
                        mChessView.addQuestion(getResources().getDrawable(getFalseImageId()));
                    }
                }

                addView(mChessView);
                addChessAnim(mChessView);

                mChessViewMgr.put(new Point(i,j),mChessView);
            }
        }

        ShowAllBombArea();
    }

    private int getTrueImageId(){

        Collections.shuffle(mTrueList);
        int ResourceId = mTrueList.get(0);
        mTrueList.remove(0);

        return ResourceId;
    }

    private int getFalseImageId(){
        Collections.shuffle(mFalseList);
        int ResourceId = mFalseList.get(0);
        mFalseList.remove(0);


        return ResourceId;
    }

    private void RefreshBoard(){
        for (int i=0;i<mChessBoard.getWidth();i++){
            for (int j=0;j<mChessBoard.getHeight();j++){

                mChessViewMgr.get(new Point(i,j)).setBaseChess(mChessBoard.getChess(i,j));
            }
        }

        ShowAllBombArea();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View ChildView = getChildAt(i);
            if (ChildView==null) {throw new NullPointerException();}

            if (!(ChildView instanceof ChessView)){
                throw new RuntimeException("Error View Type!");
            }

            BaseChess mBaseChess = ((ChessView) ChildView).getBaseChess();

            Rect frame = computeChildFrame(
                    mBaseChess,
                    mBaseChess.getX(),
                    mBaseChess.getY(),
                    mChildSize);

            ChildView.layout(frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(
                mChessBoard.getWidth()*mChildSize,
                mChessBoard.getHeight()*mChildSize+mChildSize);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View mView = getChildAt(i);
            if (mView!=null){
                mView.measure(MeasureSpec.makeMeasureSpec(mChildSize,
                                MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY));
            }
        }
    }

    private Rect computeChildFrame(BaseChess mBaseChess,int X, int Y, int size) {

        int left = (X*size);

        int top;
        if (mBaseChess.getX()%2==1){
            top = (Y*size)+size/2+size/4;
        }else{
            top = (Y*size)+size/2-size/4;
        }

        return new Rect(left,top,
                left+size, top+size);
    }

    @Override
    public void onClick(View mView) {

        if (!IsStart) return;

        ChessView mChessView = ((ChessView) mView);
        BaseChess mBaseChess = mChessView.getBaseChess();

        if (mBaseChess.isFixed()) return;

        switch (mBaseChess.getType()){
            case EMPTY:
                if (BombCounter < MaxBomb) {
                    BombCounter ++;
                    mChessView.resetTouchCounter();
                    mChessBoard.setChess(new LeftistBomb(
                            mBaseChess.getX(), mBaseChess.getY()));

                    if (mListener!=null)
                        mListener.OnBombAddEvent(MaxBomb-BombCounter);

                }

                break;
            case BLANK:
                break;
            case STAR:
                break;
            case BOMB:
                if (mChessView.getTouchCounter()>=3){
                    mChessBoard.setChess(new Empty(mBaseChess.getX(), mBaseChess.getY()));

                    BombCounter --;

                    if (mListener!=null)
                        mListener.OnBombCancelEvent(MaxBomb-BombCounter);
                }else{
                    ChangeBomb((Bomb)mBaseChess);
                }

                break;
        }

        mChessView.addTouchCounter();
        RefreshBoard();
    }


    private void ChangeBomb(Bomb mBomb){

        int MaxBombType = Bomb.BombType.values().length;
        int EnumIndex = Bomb.BombType.valueOf(mBomb.getBombType().name()).ordinal();

        int TargetType;

        TargetType = EnumIndex+1;

        if (TargetType > MaxBombType-1){
            TargetType = 0;
        }else if (TargetType < 0){
            TargetType = MaxBombType-1;
        }



        if (TargetType==-1){
            mChessBoard.setChess(new Empty(mBomb.getX(), mBomb.getY()));
        }else{
            Bomb.BombType mBombType = Bomb.BombType.values()[TargetType];

            switch (mBombType){
                case Straight:
                    mChessBoard.setChess(new StraightLineBomb(
                            mBomb.getX(), mBomb.getY()));
                    break;
                case Right:
                    mChessBoard.setChess(new RightistBomb(
                            mBomb.getX(), mBomb.getY()));
                    break;
                case Left:
                    mChessBoard.setChess(new LeftistBomb(
                            mBomb.getX(), mBomb.getY()));
                    break;
            }
        }

    }



    private void ShowAllBombArea(){
        for (int i=0;i<mChessBoard.getWidth();i++){
            for (int j=0;j<mChessBoard.getHeight();j++){

                ChessView mChessView = mChessViewMgr.get(new Point(i,j));
                BaseChess mBaseChess = mChessView.getBaseChess();

                if (mBaseChess instanceof Bomb){

                    Bomb mBomb = ((Bomb)mBaseChess);

                    ShowBombArea(mBomb);
                }

            }
        }
    }

    private void ShowBombArea(Bomb mBomb){



        for (int i=0;i<mChessBoard.getWidth();i++){
            for (int j=0;j<mChessBoard.getHeight();j++){
                if (mBomb.IsBombArea(i,j)){

                    if (mChessBoard.getTargetBomb().equalsXY(mBomb)){
                        mChessViewMgr.get(new Point(i,j)).setTargetBombArea();
                    }else{
                        mChessViewMgr.get(new Point(i,j)).setBombArea();
                    }
                }
            }
        }
    }



    private void ExplosionAllBombArea(){
        for (int i=0;i<mChessBoard.getWidth();i++){
            for (int j=0;j<mChessBoard.getHeight();j++){

                ChessView mChessView = mChessViewMgr.get(new Point(i,j));
                BaseChess mBaseChess = mChessView.getBaseChess();

                if (mBaseChess instanceof Bomb){

                    Bomb mBomb = ((Bomb)mBaseChess);


                    if (mChessBoard.getTargetBomb().IsBombArea(mBomb.getX(),mBomb.getY())){
                        IsTargetBombArea = true;
                    }

                    if (IsTargetBombArea){
                        ExplosionBombArea(mBomb);
                    }
                }

            }
        }

        if (IsWrong){
            score = 0;
        }

        Log.d(TAG,"Score:"+score);

        if (mGameOverListener!=null)
            mGameOverListener.OnGameOverEvent(score);
    }

    private void ExplosionBombArea(Bomb mBomb){


        for (int i=0;i<mChessBoard.getWidth();i++){
            for (int j=0;j<mChessBoard.getHeight();j++){
                if (mBomb.IsBombArea(i,j)){

                    ChessView mChessView = mChessViewMgr.get(new Point(i, j));
                    mChessView.Explosion();

                    if (mChessView.getBaseChess() instanceof Star){
                        if (((Star)mChessView.getBaseChess()).IsAnswer()){
                            score++;
                        }else{
                            IsWrong = true;
                        }
                    }

                    mChessBoard.setChess(new Empty(i,j));
                }
            }
        }


    }

    @Override
    public boolean onTouch(View mView, MotionEvent event) {
        ChessView mChessView = ((ChessView) mView);
        BaseChess mBaseChess = mChessView.getBaseChess();

        switch (mBaseChess.getType()){
            case EMPTY:
                break;
            case BLANK:
                break;
            case STAR:
                break;
            case BOMB:
                BombTouchEvent((Bomb)mBaseChess,event);

                mChessView.Explosion();
                mChessBoard.setChess(new Empty(mBaseChess));
                RefreshBoard();

                return true;
        }

        return false;
    }

    private void BombTouchEvent(Bomb mBomb,MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

    }

    private void addChessAnim(final ChessView mChessView){
        mChessView.setVisibility(View.GONE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float fromX = new Random().nextInt(6)+5;
                float fromY = new Random().nextInt(6)+5;

                if (new Random().nextBoolean()){
                    fromX = -fromX;
                }

                if (new Random().nextBoolean()){
                    fromY = -fromY;
                }

                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,
                        fromX,
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        fromY,
                        Animation.RELATIVE_TO_SELF,
                        0.0f
                );
                translateAnimation.setDuration(new Random().nextInt(501)+500);
                translateAnimation.setFillAfter(true);

                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mChessView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                mChessView.startAnimation(translateAnimation);
            }
        },new Random().nextInt(1000));

    }

    public void Shot(){
        if (MaxBomb-BombCounter!=0){

            return;
        }

        Bomb TargetBomb = mChessBoard.getTargetBomb();

        if (TargetBomb!=null){
            ChessView mTargetChess = mChessViewMgr.get(new Point(TargetBomb.getX(),TargetBomb.getY()));

            mChessBoard.setChess(new Empty(TargetBomb));
            mTargetChess.Explosion();
            ExplosionAllBombArea();
            RefreshBoard();
        }


    }

    public void setListener(OnBombChangeListener mListener) {
        this.mListener = mListener;
    }

    public void setGameOverListener(OnGameOverListener mGameOverListener) {
        this.mGameOverListener = mGameOverListener;
    }



    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
