package com.thu.stlgm.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.facebook.FBMultiAccountMgr;
import com.thu.stlgm.util.ConstantUtil;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;

/**
 * Created by SemonCat on 2014/2/18.
 */
public class SQService {
    private static final String TAG = SQService.class.getName();

    private static final String ServerIP = ConstantUtil.ServerIP;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public interface OnSQLoginFinish{
        public void OnSQLoginFinish(StudentBean mData);
        public void OnSQLoginFail(String fid);
        public void OnSQLoginNetworkError(int statusCode, Header[] headers, byte[] responseBody, Throwable error);
    }

    public interface OnMedicineGetSuccess{
        void OnMedicineGetSuccess(int reward);
    }

    /**
     * 直接登入SQ Server
     * @param mContext
     * @param mListener
     */
    public static void StudentLogin(Context mContext,final OnSQLoginFinish mListener){
        //設定登入提示視窗
        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);

        mProgressDialog.setTitle("請稍候");
        mProgressDialog.setMessage("正在登入SQ...");
        mProgressDialog.setIndeterminate(true);

        //準備登入FB
        FBMultiAccountMgr mFBMultiAccountMgr = new FBMultiAccountMgr(mContext);
        mFBMultiAccountMgr.setListener(new FBMultiAccountMgr.FBEventListener() {
            @Override
            public void OnLoginFinish(final AccountBean mAccount) {
                //FB登入完成，準備登入SQ
                mProgressDialog.show();

                StudentLogin(mAccount.getId(),new OnSQLoginFinish() {
                    //SQ登入完成
                    @Override
                    public void OnSQLoginFinish(StudentBean mData) {
                        mData.setId(mAccount.getId());
                        mData.setName(mAccount.getName());
                        mData.setAccessToken(mAccount.getAccessToken());

                        mProgressDialog.dismiss();
                        mListener.OnSQLoginFinish(mData);
                    }

                    @Override
                    public void OnSQLoginFail(String fid) {
                        mProgressDialog.dismiss();
                        mListener.OnSQLoginFail(fid);
                    }

                    @Override
                    public void OnSQLoginNetworkError(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        mProgressDialog.dismiss();
                        mListener.OnSQLoginNetworkError(statusCode,headers,responseBody,error);
                    }
                });
            }
        });

        mFBMultiAccountMgr.Login();
    }

    /**
     * 用FB ID登入SQ。
     * @param fid
     * @param mListener
     */
    @Deprecated
    public static void StudentLogin(final String fid,final OnSQLoginFinish mListener){

        String URL = ServerIP+"/StudentLogin?fid=" + fid;
        client.get(URL,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String message = new String(responseBody);
                Log.d(TAG,"Result:"+message);

                StudentBean mStudentBean = BeanTranslate.toStudent(fid,message);
                if (mStudentBean!=null)
                    mListener.OnSQLoginFinish(mStudentBean);
                else
                    mListener.OnSQLoginFail(fid);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d(TAG,"Error Result:"+error.toString());
                if (mListener!=null)
                    mListener.OnSQLoginNetworkError(statusCode, headers, responseBody,error);
            }
        });
    }

    public static void getMedicine(String sid,int reward){

        String URL = ServerIP+"/HpEvent?service=8&sid="+sid+"&reward="+reward;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String message = new String(responseBody);
                if (message.equals("success")){
                    Log.d(TAG,"金創藥取得成功！");
                }else{
                    Log.d(TAG,"金創藥取得失敗！");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d(TAG,"Error Result:"+error.toString());

            }
        });
    }

    public static void getGroupMemberCounter(String gid,AsyncHttpResponseHandler responseHandler){
        String URL = ServerIP+"/ExtendService?service=memberNumber&groupid=" + gid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL,responseHandler);

    }
}
