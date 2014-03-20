package com.thu.stlgm.game.gallifrey.bean;

/**
 * 炸彈的abstract類別，
 * 實作IsBombArea判斷炸彈區域
 * Created by SemonCat on 2014/3/19.
 */
public abstract class Bomb extends BaseChess{
    public enum BombType{
        Left,

        Straight,

        Right,
    }



    public Bomb(BaseChess src){
        super(src.getX(),src.getY());
    }

    public Bomb(int x, int y) {
        super(x, y);
    }

    public abstract boolean IsBombArea(int x,int y);

    @Override
    public ChessType getType() {
        return ChessType.BOMB;
    }



    public abstract BombType getBombType();


}
