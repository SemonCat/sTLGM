package com.thu.stlgm.component;

/**
 * Created by SemonCat on 2014/3/18.
 */
public class UPinEntryDelegate implements PinEntryDialogController.PinEntryDelegate  {

    public interface OnPinEntryListener{
        void OnEnterCorrectPin();
        void OnEnterFailPin();
    }

    private String Code = "0829";

    private OnPinEntryListener mListener;

    public UPinEntryDelegate() {

    }

    public void setListener(OnPinEntryListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void didCreatePin(String createdPin) {

    }

    @Override
    public void didFailToCreatePin() {

    }

    @Override
    public void didEnterCorrectPin() {
        if (mListener!=null)
            mListener.OnEnterCorrectPin();
    }

    @Override
    public void didFailEnteringCorrectPin() {
        if (mListener!=null)
            mListener.OnEnterFailPin();
    }

    @Override
    public int getMode() {
        return PinEntryDialogController.MODE_VERIFY;
    }

    @Override
    public String getVerificationPin() {
        return Code;
    }

    @Override
    public String getTitleForMode(int mode) {
        return null;
    }

    @Override
    public void setPinEntryDialogController(PinEntryDialogController dialogController) {

    }
}
