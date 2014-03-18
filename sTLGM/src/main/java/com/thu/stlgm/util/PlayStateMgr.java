package com.thu.stlgm.util;

import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.StudentBean;

import java.util.List;

/**
 * Created by SemonCat on 2014/3/13.
 */
public class PlayStateMgr {

    private List<StudentBean> mStudentList;

    private boolean IsPlayed[];

    public PlayStateMgr(List<StudentBean> studentBeanList){
        mStudentList = studentBeanList;
        IsPlayed = new boolean[5];
    }

    public void setPlayed(String sid){
        StudentBean mStudent = SQService.findStudentBySID(mStudentList,sid);

        int index = mStudentList.indexOf(mStudent);

        if (index!=-1){
            IsPlayed[index] = true;
        }
    }

    public boolean getPlayed(String sid){
        StudentBean mStudent = SQService.findStudentBySID(mStudentList,sid);

        int index = mStudentList.indexOf(mStudent);

        if (index!=-1){
            return IsPlayed[index];
        }else{
            return false;
        }
    }

    public void reset(){
        mStudentList.clear();

        IsPlayed = new boolean[5];
    }
}
