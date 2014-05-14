package com.thu.stlgm.bean;

import serializable.StudentHp;
import serializable.StudentInfo;

/**
 * Created by SemonCat on 2014/1/16.
 */
public class Student {


    private String ID;

    private String Department;

    private String Name;

    private String ImageUrl;

    private int Blood;

    private int Money;

    private boolean IsLogin;

    private String GroupID;

    private String FacebookID;

    public static Student toStudent(StudentInfo mInfo) {
        Student mStudent = new Student();
        mStudent.ID = mInfo.getSid();
        mStudent.Department = mInfo.getDep();
        mStudent.Name = mInfo.getName();
        mStudent.ImageUrl = mInfo.getImgUrl();
        mStudent.IsLogin = false;
        mStudent.GroupID = mInfo.getGid();
        mStudent.FacebookID = mInfo.getFb();
        mStudent.Money = mInfo.getCoin();
        return mStudent;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getBlood() {
        return Blood;
    }

    public void setBlood(int blood) {
        Blood = blood;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public boolean IsLogin() {
        return IsLogin;
    }

    public void setLogin(boolean isLogin) {
        IsLogin = isLogin;
    }


    @Override
    public int hashCode() {

        return getID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        String token1 = null;
        if (o instanceof Student){
            token1 = ((Student) o).getID();
        }else if (o instanceof StudentInfo){
            token1 = ((StudentInfo) o).getSid();
        }else if (o instanceof StudentHp){
            token1 = ((StudentHp) o).getSid();
        }

        String token2 = this.getID();

        if (token1==null || token2==null)
            return false;
        return token1.equals(token2);
    }
}
