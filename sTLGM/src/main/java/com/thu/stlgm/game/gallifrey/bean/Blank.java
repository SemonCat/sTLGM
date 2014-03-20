package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class Blank extends BaseChess{

    public Blank(int x, int y) {
        super(x, y);
    }

    @Override
    public ChessType getType() {
        return ChessType.BLANK;
    }
}
