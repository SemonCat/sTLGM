package com.thu.stlgm.bean;

import com.facebook.android.R;

/**
 * Created by SemonCat on 2014/2/18.
 */
public class StudentBean extends AccountBean{
    private String SID;
    private String Department;
    private int Blood;

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public int getBlood() {
        return Blood;
    }

    public void setBlood(int blood) {
        Blood = blood;
    }
}
