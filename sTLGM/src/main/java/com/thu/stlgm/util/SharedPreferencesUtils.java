package com.thu.stlgm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by SemonCat on 2014/3/12.
 */
public class SharedPreferencesUtils {

    private static final String FB_ACCESSTOKEN = "facebook_accesstoken";


    public static void setFB_AccessToken(Context mContext,String FB_ID,String AccessToken){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPreferences.edit().putString(FB_ID+"_"+FB_ACCESSTOKEN,AccessToken).commit();

    }

    public static String getFB_AccessToken(Context mContext,String FB_ID){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        return mSharedPreferences.getString(FB_ID+"_"+FB_ACCESSTOKEN,null);
    }


}
