package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public abstract class BaseChess {
    public enum ChessType{
        BLANK,
        EMPTY,
        BOMB,
        STAR
    }

    private int x;
    private int y;
    private boolean IsFixed = false;

    public BaseChess(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract ChessType getType();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFixed() {
        return IsFixed;
    }

    public void setFixed(boolean isFixed) {
        IsFixed = isFixed;
    }

    public boolean equalsXY(BaseChess mBaseChess){
        return getX()==mBaseChess.getX() && getY()==mBaseChess.getY();
    }
}
