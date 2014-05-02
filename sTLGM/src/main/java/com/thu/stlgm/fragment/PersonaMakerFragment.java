package com.thu.stlgm.fragment;

import android.app.Activity;
import android.app.ProgressDialog;

import com.facebook.Request;
import com.facebook.Response;
import com.thu.persona.WizardPager.PersonaFragment;
import com.thu.stlgm.GameActivity;
import com.thu.stlgm.GameActivity_;

/**
 * Created by SemonCat on 2014/5/2.
 */
public class PersonaMakerFragment extends PersonaFragment {

    private final static String PersonaAlbumId = "443890565748503";

    @Override
    protected void OnDataSubmit(byte[] mResult) {

        Activity mActivity = getActivity();
        final ProgressDialog mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("上傳中");
        mProgressDialog.setMessage("有預感會上傳失敗，失敗的話麻煩重傳一下><");
        if (mActivity instanceof GameActivity_){
            final GameActivity_ gameActivity_ = (GameActivity_)mActivity;
            if (mResult!=null){
                mProgressDialog.show();
                gameActivity_.postPhoto(PersonaAlbumId,mResult,new Request.OnProgressCallback() {
                    @Override
                    public void onProgress(long l, long l2) {

                    }

                    @Override
                    public void onCompleted(Response response) {
                        mProgressDialog.dismiss();
                        if (response.getError()==null){
                            finish();
                            gameActivity_.showToast("好險，上傳成功了！");
                        }else{
                            gameActivity_.showToast("哎呀！有東西出錯了！請在上傳一次！");
                        }

                    }
                });
            }else{
                gameActivity_.showToast("哎呀！有東西出錯了！");
            }
        }
    }

    private void finish(){
        Activity mActivity = getActivity();
        if (mActivity instanceof GameActivity_){
            GameActivity_ gameActivity_ = (GameActivity_)mActivity;
            gameActivity_.ShowCamera();
            gameActivity_.ShowInfo();
        }

        if (getFragmentManager()!=null){
            getFragmentManager().beginTransaction().remove(this).commit();
        }

        System.gc();

    }
}
