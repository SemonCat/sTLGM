package com.semoncat.tlgm2048.Input;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.semoncat.tlgm2048.View.View2048;

/**
 * Created by SemonCat on 2014/3/30.
 */
public class GestureListener implements View.OnTouchListener {




    private final GestureDetector gestureDetector;
    private View2048 mView2048;

    private float x;
    private float y;

    public GestureListener (View2048 view){
        this.mView2048 = view;
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
                if (inRange(View2048.sXNewGame, x, View2048.sXNewGame + View2048.iconSize)
                        && inRange(View2048.sYIcons, y, View2048.sYIcons + View2048.iconSize)) {
                    mView2048.game.newGame();
                    return true;
                }else if (inRange(View2048.sXRedo, x, View2048.sXRedo + View2048.iconSize + View2048.bodyWidthRedo)
                        && inRange(View2048.sYIcons, y, View2048.sYIcons + View2048.iconSize)){

                    mView2048.game.restoreTiles();
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
        mView2048.game.move(1);

    }

    public void onSwipeLeft() {
        mView2048.game.move(3);
    }

    public void onSwipeTop() {
        mView2048.game.move(0);
    }

    public void onSwipeBottom() {
        mView2048.game.move(2);
    }

}


