package com.thu.persona.WizardPager.bean;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by SemonCat on 2014/5/1.
 */
public class PersonaData{
    Bitmap Icon;
    Bitmap Attr;
    String Name;
    String Gender;
    String Age;
    String Job;
    String Title;
    String Content;
    String[] GoodThings;
    String[] BadThings;
    String[] Need;

    public PersonaData() {
        Name = "";
        Gender = "";
        Age = "";
        Job = "";
        Title = "";
        Content = "";
        GoodThings = new String[5];
        BadThings = new String[5];
        Need = new String[5];

        Arrays.fill(GoodThings,"");
        Arrays.fill(BadThings,"");
        Arrays.fill(Need,"");
    }

    public Bitmap getIcon() {
        return Icon;
    }

    public void setIcon(Bitmap icon) {
        Icon = icon;
    }

    public Bitmap getAttr() {
        return Attr;
    }

    public void setAttr(Bitmap attr) {
        Attr = attr;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String[] getGoodThings() {
        return GoodThings;
    }

    public void setGoodThings(String[] goodThings) {
        GoodThings = goodThings;
    }

    public String[] getBadThings() {
        return BadThings;
    }

    public void setBadThings(String[] badThings) {
        BadThings = badThings;
    }

    public String[] getNeed() {
        return Need;
    }

    public void setNeed(String[] need) {
        Need = need;
    }
}