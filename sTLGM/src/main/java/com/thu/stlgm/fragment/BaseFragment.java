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
public class BaseFragment extends Fragment {

    public interface OnFragmentFinishEvent{
        void OnFragmentFinish();
    }

    Toast mToast;

    private OnFragmentFinishEvent mListener;

    protected void showToast(String Message){
        if (mToast==null){
            mToast = Toast.makeText(getActivity(),Message,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(Message);
        }
        mToast.show();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
        setupAdapter();
        setupEvent();

    }

    protected void setupView(){

    }

    protected void setupAdapter(){

    }

    protected void setupEvent(){

    }

    public void finishFragment(){
        if (getFragmentManager()!=null){
            getFragmentManager().beginTransaction().remove(this).commit();

            if (mListener!=null)
                mListener.OnFragmentFinish();
        }
    }

    public void setOnFragmentFinishListener(OnFragmentFinishEvent mListener) {
        this.mListener = mListener;
    }
}
