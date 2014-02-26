package com.thu.stlgm.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by SemonCat on 2014/1/28.
 */
public class BaseFragment extends android.support.v4.app.Fragment{

    Toast mToast;

    protected void showToast(String Message){
        if (mToast==null){
            mToast = Toast.makeText(getActivity(),Message,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(Message);
        }
        mToast.show();

    }

}
