package com.thu.stlgm.bean;

/**
 * Created by SemonCat on 2014/3/2.
 */
public class Blood {


    private int blood;
    private long bloodMills;

    public Blood(int blood, long bloodMills) {
        this.blood = blood;
        this.bloodMills = bloodMills;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public long getBloodMills() {
        return bloodMills;
    }

    public void setBloodMills(long bloodMills) {
        this.bloodMills = bloodMills;
    }
}
