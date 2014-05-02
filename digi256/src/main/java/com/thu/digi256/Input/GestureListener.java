package com.thu.digi256.Input;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.thu.digi256.View.MainView;

/**
 * Created by SemonCat on 2014/3/30.
 */
public class GestureListener implements View.OnTouchListener {




    private final GestureDetector gestureDetector;
    private MainView mMainView;

    private float x;
    private float y;

    public GestureListener (MainView view){
        this.mMainView = view;
        gestureDetector = new GestureDetector(view.getContext(), new UGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (inRange(MainView.sXNewGame, x, MainView.sXNewGame + MainView.iconSize)
                        && inRange(MainView.sYIcons, y, MainView.sYIcons + MainView.iconSize)) {
                    mMainView.game.newGame();
                    return true;
                }else if (inRange(MainView.sXRedo, x, MainView.sXRedo + MainView.iconSize + MainView.bodyWidthRedo)
                        && inRange(MainView.sYIcons, y, MainView.sYIcons + MainView.iconSize)){

                    mMainView.game.restoreTiles();
                    return true;
                }
                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    public boolean inRange(float left, float check, float right) {
        return (left <= check && check <= right);
    }

    private class UGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 25;
        private static final int SWIPE_VELOCITY_THRESHOLD = 25;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        mMainView.game.move(1);

    }

    public void onSwipeLeft() {
        mMainView.game.move(3);
    }

    public void onSwipeTop() {
        mMainView.game.move(0);
    }

    public void onSwipeBottom() {
        mMainView.game.move(2);
    }

}


