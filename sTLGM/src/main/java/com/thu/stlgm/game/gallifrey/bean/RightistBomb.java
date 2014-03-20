package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class RightistBomb extends Bomb {
    public RightistBomb(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean IsBombArea(int x, int y) {
        int srcX = getX();
        int srcY = getY();
        int tempRightX;
        int tempRightSrcX;

        tempRightSrcX = (srcX + 1) / 2;
        tempRightX = (x + 1) / 2;
        if ((srcY - tempRightX + tempRightSrcX) == y) {
            return true;
        }
        
        return false;
    }


    @Override
    public BombType getBombType() {
        return BombType.Right;
    }
}
