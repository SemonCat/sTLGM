package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class Empty extends BaseChess{

    public Empty(BaseChess src){
        super(src.getX(),src.getY());
    }

    public Empty(int x, int y) {
        super(x, y);
    }

    @Override
    public ChessType getType() {
        return ChessType.EMPTY;
    }
}
