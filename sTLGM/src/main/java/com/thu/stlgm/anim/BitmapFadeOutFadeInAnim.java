package com.thu.stlgm.anim;

/**
 * Created by SemonCat on 2014/2/13.
 */
public class BitmapFadeOutFadeInAnim {
    private int FADE_MILLISECONDS;

    private int ALPHA_STEP;

    private boolean fadein;

    // Need to keep track of the current alpha value
    private int currentAlpha;

    public BitmapFadeOutFadeInAnim() {
        init();
    }

    private void init() {
        currentAlpha = 255;
        FADE_MILLISECONDS = 1000;
        ALPHA_STEP = (int) ((float) 255 / ((float) FADE_MILLISECONDS / ((float) 1000 / (float) 30)));
        fadein = false;
    }

    public void setupDuration(int FADE_MILLISECONDS) {
        this.FADE_MILLISECONDS = FADE_MILLISECONDS;
    }

    public int getAlphaValue() {


        if (!fadein && currentAlpha > 0) {


            currentAlpha -= ALPHA_STEP;


        } else {
            fadein = true;
        }

        if (fadein && currentAlpha < 255) {
            currentAlpha += ALPHA_STEP;
        } else {
            fadein = false;
        }


        return currentAlpha;
    }

    public void reset() {
        init();
    }

}

