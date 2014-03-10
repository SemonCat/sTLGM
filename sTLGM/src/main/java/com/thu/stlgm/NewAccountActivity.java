package com.thu.stlgm;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.facebook.FBMultiAccountMgr;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/9.
 */
@EActivity(R.layout.activity_newaccount)
public class NewAccountActivity extends BaseActivity{

    private static final String TAG = NewAccountActivity.class.getName();

    @ViewById
    TextView Dep,Name,facebook_login,State;

    @ViewById
    AutoCompleteTextView sid;

    @ViewById
    RoundedImageView Icon;

    StudentBean currentStudent;

    List<StudentBean> mStudentList = new ArrayList<StudentBean>();

    @AfterInject
    void findAllStudents(){
        SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
            @Override
            public void OnAllStudentGetEvent(List<StudentBean> studentList) {
                mStudentList = studentList;

                List<String> sidList = new ArrayList<String>();

                for (StudentBean mStudent : mStudentList){
                    sidList.add(mStudent.getSID());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        NewAccountActivity.this,android.R.layout.simple_spinner_dropdown_item,sidList);
                sid.setAdapter(adapter);
            }
        });
    }

    @Click
    void Icon(){
        findAllStudents();
    }


    @AfterTextChange
    void sidAfterTextChanged(TextView sid){
        StudentBean mStudentBean = SQService.findStudentBySID(mStudentList,sid.getText().toString());
        if (mStudentBean!=null){
            ImageLoader.getInstance().displayImage(mStudentBean.getImgUrl(),Icon);

            Dep.setText(mStudentBean.getDepartment());
            Name.setText(mStudentBean.getName());

            this.currentStudent = mStudentBean;
            HideSoftKeyBoard();
        }else{
            cleanData();
        }
    }

    @Click
    void facebook_login(){
        FBMultiAccountMgr multiAccountMgr = new FBMultiAccountMgr(this);
        multiAccountMgr.setListener(new FBMultiAccountMgr.FBEventListener() {
            @Override
            public void OnLoginFinish(AccountBean mAccount) {
                facebook_login.setText(mAccount.getName()+"\n"+mAccount.getId());
                if (currentStudent!=null){
                    SignUp(currentStudent.getSID(),mAccount.getId());
                }
            }
        });

        multiAccountMgr.Login();
    }

    @Click
    void back2login(){
        finish();
    }

    private void SignUp(String sid,String fb_id){
        SQService.SingUp(sid,fb_id,new AsyncHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                State.setText("網路錯誤");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String message = new String(responseBody);

                Log.d(TAG,message);

                if (message.equals("success")){
                    State.setText("登記完成！");
                }else{
                    State.setText("有東西出錯了：（");
                }
            }
        });
    }

    private void cleanData(){
        Icon.setImageResource(R.drawable.default_icon);
        Dep.setText("");
        Name.setText("");
        State.setText("");
        facebook_login.setText(R.string.fb_login);
        currentStudent = null;
    }
}
