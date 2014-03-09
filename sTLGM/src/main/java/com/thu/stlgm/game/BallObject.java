package com.thu.stlgm.game;

/**
 * Created by JimmyLi on 2014/3/7.
 */
public class BallObject {

    private boolean IsTouch;
    private boolean Answer;

    public BallObject(boolean isTouch, boolean answer) {
        IsTouch = isTouch;
        Answer = answer;
    }

    public boolean isTouch() {
        return IsTouch;
    }

    public void setTouch(boolean isTouch) {
        IsTouch = isTouch;
    }

    public boolean isAnswer() {
        return Answer;
    }

    public void setAnswer(boolean answer) {
        Answer = answer;
    }
}
