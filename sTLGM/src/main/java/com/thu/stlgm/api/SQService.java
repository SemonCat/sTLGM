package com.thu.stlgm.api;

import android.nfc.Tag;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.bean.StudentBean;

import org.apache.http.Header;

/**
 * Created by SemonCat on 2014/2/18.
 */
public class SQService {
    private static final String TAG = SQService.class.getName();

    private static final String ServerIP = "http://54.214.24.26:8080/StudentSquare_server";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public interface OnSQLoginFinish{
        public void OnSQLoginFinish(StudentBean mData);
    }
    public static void StudentLogin(String fid){
        client.get(ServerIP+"/StudentLogin?fid=" + fid,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String message = new String(responseBody);
                Log.d(TAG,"Result:"+message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d(TAG,"Error Result:"+error.toString());
            }
        });
    }
}
