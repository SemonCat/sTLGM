package com.thu.stlgm.game;

import android.app.Fragment;

/**
 * Created by SemonCat on 2014/3/2.
 */
public class BaseGame extends Fragment{
    public interface OnFragmentFinishListener{
        void OnFragmentFinishEvent(BaseGame fragment);
    }

    private OnFragmentFinishListener mListener;

    protected void finishFragment(){
        if (getActivity()!=null){
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            if (mListener!=null)
                mListener.OnFragmentFinishEvent(this);
        }
    }

    public void setFragmentFinishListener(OnFragmentFinishListener listener){
        this.mListener = listener;
    }
}
