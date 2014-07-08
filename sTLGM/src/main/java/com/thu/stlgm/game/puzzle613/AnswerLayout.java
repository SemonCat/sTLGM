package com.thu.stlgm.game.puzzle613;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.puzzle.R;

/**
 * Created by JimmyLi on 2014/6/10.
 */
public class AnswerLayout extends ViewGroup{
    private int mChildSize = 60;

    private int mChildPadding = 5;

    private int mLayoutPadding = 10;

    public static final float DEFAULT_FROM_DEGREES = 0f;

    public static final float DEFAULT_TO_DEGREES = 360.0f;

    public static final int DEFAULT_MIN_RADIUS = 300;

    private float mFromDegrees = DEFAULT_FROM_DEGREES;

    private float mToDegrees = DEFAULT_TO_DEGREES;

    private final static int MIN_RADIUS = DEFAULT_MIN_RADIUS;

    /* the distance between the layout's center and any child's center */
    private int mRadius;

    public AnswerLayout(Context context) {
        super(context);
    }

    public AnswerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

//        if (attrs != null) {
//            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BookLayout, 0, 0);
//            mFromDegrees = a.getFloat(R.styleable.BookLayout_fromDegrees, DEFAULT_FROM_DEGREES);
//            mToDegrees = a.getFloat(R.styleable.BookLayout_toDegrees, DEFAULT_TO_DEGREES);
//            mChildSize = Math.max(a.getDimensionPixelSize(R.styleable.BookLayout_childSize, 0), 0);
//            a.recycle();
//        }
    }



    private static int computeRadius(final float arcDegrees, final int childCount, final int childSize,
                                     final int childPadding, final int minRadius) {
        if (childCount < 2) {
            return minRadius;
        }

        final float perDegrees = arcDegrees / (childCount - 1);
        final float perHalfDegrees = perDegrees / 2;
        final int perSize = childSize + childPadding;

        final int radius = (int) ((perSize / 2) / Math.sin(Math.toRadians(perHalfDegrees)));

        return Math.max(radius, minRadius);
    }

    private static Rect computeChildFrame(final int centerX, final int centerY, final int radius, final float degrees,
                                          final int size) {

        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));

        return new Rect((int) (childCenterX - size / 2), (int) (childCenterY - size / 2),
                (int) (childCenterX + size / 2), (int) (childCenterY + size / 2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int radius = mRadius = computeRadius(Math.abs(mToDegrees - mFromDegrees), getChildCount(), mChildSize,
                mChildPadding, MIN_RADIUS);
        final int size = radius * 2 + mChildSize + mChildPadding + mLayoutPadding * 2;

        setMeasuredDimension(size, size);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View mView = getChildAt(i);
            if (mView!=null){
                mView.measure(MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        final int radius =  mRadius;

        final int childCount = getChildCount();
        final float perDegrees = (mToDegrees - mFromDegrees) / (childCount);

        float degrees = mFromDegrees;
        for (int i = 0; i < childCount; i++) {
            Rect frame = computeChildFrame(centerX, centerY, radius, degrees, mChildSize);
            degrees += perDegrees;
            getChildAt(i).layout(frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    private static int getTransformedIndex(final boolean expanded, final int count, final int index) {
        if (expanded) {
            return count - 1 - index;
        }

        return index;
    }



    public void setArc(float fromDegrees, float toDegrees) {
        if (mFromDegrees == fromDegrees && mToDegrees == toDegrees) {
            return;
        }

        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;

        requestLayout();
    }

    public void setChildSize(int size) {
        if (mChildSize == size || size < 0) {
            return;
        }

        mChildSize = size;

        requestLayout();
    }

    public int getChildSize() {
        return mChildSize;
    }
}
