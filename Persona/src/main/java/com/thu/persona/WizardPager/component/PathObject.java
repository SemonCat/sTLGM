package com.thu.persona.WizardPager.component;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JimmyLi on 2014/3/24.
 */
public class PathObject implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.PaintColor);
        dest.writeFloat(this.BrushSize);
        dest.writeFloat(this.X);
        dest.writeFloat(this.Y);
        dest.writeInt(this.IsMoveTo);
        dest.writeValue(this.IsErase);
        dest.writeInt(this.Number);
    }

    private PathObject(Parcel in) {
        this.PaintColor = in.readInt();
        this.BrushSize = in.readFloat();
        this.X = in.readFloat();
        this.Y = in.readFloat();
        this.IsMoveTo = in.readInt();
        this.IsErase = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.Number = in.readInt();
    }

    public static Parcelable.Creator<PathObject> CREATOR = new Parcelable.Creator<PathObject>() {
        public PathObject createFromParcel(Parcel source) {
            return new PathObject(source);
        }

        public PathObject[] newArray(int size) {
            return new PathObject[size];
        }
    };
}
