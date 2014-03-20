package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class StraightLineBomb extends Bomb{

    public StraightLineBomb(int x, int y) {
        super(x, y);
    }

    public boolean IsBombArea(int x,int y){

        return (x == getX());
    }

    @Override
    public BombType getBombType() {
        return BombType.Straight;
    }
}
