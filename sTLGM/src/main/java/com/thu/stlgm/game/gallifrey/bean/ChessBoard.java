package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class ChessBoard {

    private int mWidth;
    private int mHeight;
    private BaseChess mChessBoard[][];

    private Bomb TargetBomb;

    public ChessBoard(int mWidth, int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;

        init();
    }

    private void init(){
        resetChessBoard();
    }

    public void resetChessBoard(){
        mChessBoard = new BaseChess[mWidth][mHeight];

        for (int i=0;i<mWidth;i++){
            for (int j=0;j<mHeight;j++){
                setChess(new Empty(i, j));
            }
        }
    }

    public void setChess(BaseChess mChess){

        mChessBoard[mChess.getX()][mChess.getY()] = mChess;

    }

    public void takeChess(int x,int y){
        mChessBoard[x][y] = new Empty(x,y);
    }

    public BaseChess getChess(int x,int y){
        return mChessBoard[x][y];
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public Bomb getTargetBomb() {
        return TargetBomb;
    }

    public void setTargetBomb(Bomb targetBomb) {
        TargetBomb = targetBomb;
    }
}
