package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class LeftistBomb extends Bomb {
    public LeftistBomb(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean IsBombArea(int x, int y) {


        int srcX = getX();
        int srcY = getY();
        int tempLeftX;
        int tempLeftSrcX;

        tempLeftSrcX = srcX / 2;
        tempLeftX = x / 2;

        if (srcY + tempLeftX - tempLeftSrcX == y) {
            return true;
        }

        return false;
    }

    @Override
    public BombType getBombType() {
        return BombType.Left;
    }
}
