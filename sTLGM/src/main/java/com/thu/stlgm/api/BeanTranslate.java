package com.thu.stlgm.api;

import android.util.Log;

import com.thu.stlgm.bean.StudentBean;

/**
 * Created by SemonCat on 2014/2/19.
 */
public class BeanTranslate {
    private final static String TAG = BeanTranslate.class.getName();

    public static StudentBean toStudent(String fid,String result){

        String[] analResult = result.split(",");
        if (analResult.length>2){

            String SID = analResult[1];
            String GID = analResult[0];
            String IMG = analResult[2];
            if (!SID.equals("not found")){
                Log.d(TAG, "SID:" + SID);
                Log.d(TAG,"GID:"+GID);
                Log.d(TAG,"IMG:"+IMG);
                StudentBean mStudentBean = new StudentBean();
                mStudentBean.setSID(SID);
                mStudentBean.setGroupID(GID);
                mStudentBean.setId(fid);
                mStudentBean.setImgUrl(IMG);

                return mStudentBean;
            }else{
                Log.d(TAG,"找不到此FID");
            }
        }else{
            Log.d(TAG,"Result Error");
        }

        return null;
    }
}
