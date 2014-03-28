package com.thu.stlgm.painter;

import android.graphics.Paint;

/**
 * Created by JimmyLi on 2014/3/24.
 */
public class PathObject {
    private int PaintColor;
    private float BrushSize;
    private float X;
    private float Y;
    private int IsMoveTo;
    private Boolean IsErase;
    private int Number;
    public PathObject(int paintColor,float brushSize,float x, float y,int isMoveTo,int number){
        PaintColor=paintColor;
        BrushSize=brushSize;
        X=x;
        Y=y;
        IsMoveTo=isMoveTo;
        //IsErase=isErase;
        Number=number;
    }
    public int getNumber() {
        return Number;
    }

    public void setPaintColor(int paintColor) {
        PaintColor = paintColor;
    }

    public void setBrushSize(float brushSize) {
        BrushSize = brushSize;
    }

    public void setX(float x) {
        X = x;
    }

    public void setY(float y) {
        Y = y;
    }

    public void setIsMoveTo(int isMoveTo) {
        IsMoveTo = isMoveTo;
    }

    public void setIsErase(Boolean isErase) {
        IsErase = isErase;
    }

    public Boolean getIsErase() {
        return IsErase;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public int getIsMoveTo() {
        return IsMoveTo;
    }

    public float getBrushSize() {
        return BrushSize;
    }

    public int getPaintColor() {
        return PaintColor;
    }
}
