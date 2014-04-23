package com.thu.stlgm.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.stlgm.BaseActivity;
import com.thu.stlgm.NewAccountActivity;
import com.thu.stlgm.R;
import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.util.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SemonCat on 2014/1/28.
 */
public class AdminLoginDialog extends DialogFragment {

    public interface OnLoginClickListener{
        void OnLoginClickEvent(StudentBean mStudent);
    }

    private static final String TAG = AdminLoginDialog.class.getName();

    private List<StudentBean> mStudentList;
    private Context mContext;

    private AutoCompleteTextView sid;
    private TextView Dep,Name;
    private RoundedImageView Icon;

    private Button Login;

    private StudentBean currentStudent;

    private OnLoginClickListener mLoginListener;

    private DialogInterface.OnDismissListener onDismissListener;

    public AdminLoginDialog(Context context,List<StudentBean> list,OnLoginClickListener mListener) {
        this.mContext = context;
        this.mStudentList = list;
        this.mLoginListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        View mContentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_adminlogin, null);
        dialog.setContentView(mContentView);
        dialog.setTitle("Login");
        dialog.setCancelable(true);

        setupView(mContentView);
        setupAutoComplete();
        setupEvent();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();




        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDismissListener.onDismiss(dialog);
        super.onDismiss(dialog);

    }

    private void setupAutoComplete(){
        List<String> sidList = new ArrayList<String>();

        for (StudentBean mStudent : mStudentList){
            sidList.add(mStudent.getSID());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext
                ,android.R.layout.simple_spinner_dropdown_item,sidList);
        sid.setAdapter(adapter);
    }

    private void setupView(View mContentView){
        sid = (AutoCompleteTextView) mContentView.findViewById(R.id.sid);
        Dep = (TextView) mContentView.findViewById(R.id.Dep);
        Name = (TextView) mContentView.findViewById(R.id.Name);
        Icon = (RoundedImageView) mContentView.findViewById(R.id.Icon);
        Login = (Button) mContentView.findViewById(R.id.Login);
    }

    private void setupEvent(){
        sid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StudentBean mStudentBean = SQService.
                        findStudentBySID(mStudentList, sid.getText().toString());
                if (mStudentBean!=null){
                    currentStudent = mStudentBean;
                    ImageLoader.getInstance().
                            displayImage(mStudentBean.getImgUrl(), Icon);

                    Dep.setText(mStudentBean.getDepartment());
                    Name.setText(mStudentBean.getName());

                    ((BaseActivity)getActivity()).HideSoftKeyBoard();
                }else{
                    cleanData();
                }
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mLoginListener!=null)
                    mLoginListener.OnLoginClickEvent(currentStudent);
            }
        });
    }

    private void cleanData(){
        Icon.setImageResource(R.drawable.default_icon);
        Dep.setText("");
        Name.setText("");
        currentStudent = null;
    }


    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
