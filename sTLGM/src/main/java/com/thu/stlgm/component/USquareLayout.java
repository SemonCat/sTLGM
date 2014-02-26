package com.thu.stlgm.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class USquareLayout extends LinearLayout {
	
    public USquareLayout(Context context) {
        this(context,null);
    }

    public USquareLayout(Context context, AttributeSet attrs) {
    	super(context, attrs);
       
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int MeasureSpec = widthMeasureSpec;
        
        if (heightMeasureSpec<MeasureSpec) 
        	MeasureSpec = heightMeasureSpec;
        super.onMeasure(MeasureSpec, MeasureSpec);
    }
}