package com.thu.stlgm.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thu.stlgm.bean.StudentBean;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static StudentBean Json2Student(String result){

        StudentBean mStudent = new StudentBean();

        try{
            JSONObject mJsonObj = new JSONObject(result);
            mStudent.setSID(mJsonObj.getString("sid"));
            mStudent.setGroupID(mJsonObj.getString("group"));
            mStudent.setId(mJsonObj.getString("fb"));
            mStudent.setImgUrl(mJsonObj.getString("imgUrl"));
            mStudent.setName(mJsonObj.getString("name"));
            mStudent.setDepartment(mJsonObj.getString("dep"));

        }catch (JSONException e){
            return null;
        }

        return mStudent;
    }
}
