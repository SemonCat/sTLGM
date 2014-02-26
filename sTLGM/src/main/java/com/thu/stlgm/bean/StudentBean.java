package com.thu.stlgm.bean;

import com.facebook.android.R;

/**
 * Created by SemonCat on 2014/2/18.
 */
public class StudentBean extends AccountBean{
    private String SID;
    private String Department;
    private int Blood;


    private String GroupID;

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


    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    @Override
    public int hashCode() {

        return getSID().hashCode();
    }

    @Override
    public boolean equals(Object o) {


        String token1 = ((StudentBean) o).getSID();
        String token2 = this.getSID();

        if (token1==null || token2==null)
            return false;
        return token1.equals(token2);
    }
}
