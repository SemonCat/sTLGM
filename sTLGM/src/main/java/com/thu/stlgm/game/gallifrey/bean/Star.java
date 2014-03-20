package com.thu.stlgm.game.gallifrey.bean;

/**
 * Created by SemonCat on 2014/3/19.
 */
public class Star extends BaseChess{

    private boolean IsAnswer;

    public Star(int x, int y) {
        super(x, y);
        init();
    }

    private void init(){
        IsAnswer = false;
        setFixed(true);
    }

    @Override
    public ChessType getType() {
        return ChessType.STAR;
    }

    public boolean IsAnswer() {
        return IsAnswer;
    }

    public void setAnswer(boolean isAnswer) {
        IsAnswer = isAnswer;
    }
}
