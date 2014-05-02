package com.thu.persona.WizardPager.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thu.persona.R;
import com.thu.persona.WizardPager.PersonaGenerator;
import com.thu.persona.WizardPager.bean.PersonaData;
import com.thu.persona.WizardPager.model.AbstractWizardModel;
import com.thu.persona.WizardPager.model.ModelCallbacks;
import com.thu.persona.WizardPager.model.Page;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/5/1.
 */
public class PersonaPreviewFragment extends Fragment implements ModelCallbacks {

    private PhotoView Persona;

    private AbstractWizardModel mWizardModel;

    public PersonaPreviewFragment(AbstractWizardModel mWizardModel){
        this.mWizardModel = mWizardModel;
        mWizardModel.registerListener(this);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible){
            if (mWizardModel!=null && Persona!=null){
                getData();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void getData(){
        Bundle mData = mWizardModel.save();
        Bundle mStep1 = mData.getBundle("Step1");
        Bundle mStep2 = mData.getBundle("Step2");
        Bundle mStep3 = mData.getBundle("Step3");
        Bundle mStep4 = mData.getBundle("Step4");
        Bundle mStep5 = mData.getBundle("Step5");

        PersonaData mPersonaData = new PersonaData();

        if (mStep1!=null){
            //Get Icon
            byte[] byteArray = mStep1.getByteArray("Icon");
            if (byteArray!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                if (bitmap!=null){
                    mPersonaData.setIcon(bitmap);
                }
            }

            //Get Basic Info
            String Name = mStep1.getString("Name","");
            String Gender = mStep1.getString("Gender","");
            String Age = mStep1.getString("Age","");
            String Job = mStep1.getString("Job","");
            mPersonaData.setName(Name);
            mPersonaData.setGender(Gender);
            mPersonaData.setAge(Age);
            mPersonaData.setJob(Job);
        }

        if (mStep2!=null){

            //Get Title and Content
            String Title = mStep2.getString("PersonaTitle","");
            String Content = mStep2.getString("PersonaContent","");
            mPersonaData.setTitle(Title);
            mPersonaData.setContent(Content);
        }

        if (mStep3!=null){

            //Get Title and Content
            String GoodThings[] = new String[]{
                    mStep3.getString("GoodThings01",""),
                    mStep3.getString("GoodThings02",""),
                    mStep3.getString("GoodThings03",""),
                    mStep3.getString("GoodThings04",""),
                    mStep3.getString("GoodThings05","")
            };

            String BadThings[] = new String[]{
                    mStep3.getString("BadThings01",""),
                    mStep3.getString("BadThings02",""),
                    mStep3.getString("BadThings03",""),
                    mStep3.getString("BadThings04",""),
                    mStep3.getString("BadThings05","")
            };

            mPersonaData.setGoodThings(GoodThings);
            mPersonaData.setBadThings(BadThings);
        }

        if (mStep4!=null){

            //Get Title and Content
            String Need[] = new String[]{
                    mStep4.getString("Need01",""),
                    mStep4.getString("Need02",""),
                    mStep4.getString("Need03",""),
                    mStep4.getString("Need04",""),
                    mStep4.getString("Need05","")
            };

            mPersonaData.setNeed(Need);
        }

        if (mStep5!=null){

            Parcelable mPersonaBackgroundParcelable =
                    mStep5.getParcelable("PersonaBackground");
            if (mPersonaBackgroundParcelable instanceof Bitmap){
                mPersonaData.setAttr(((Bitmap) mPersonaBackgroundParcelable));
            }

        }

        Bitmap mBitmap = PersonaGenerator.getPersonaBitmap(mPersonaData);
        if (mStep5!=null){
            mStep5.putByteArray("Result",PersonaGenerator.saveBitmap2ByteArray(mBitmap));
        }
        Persona.setImageBitmap(mBitmap);
    }

    @Override
    public void onPageDataChanged(Page page) {
        Log.d("","DataChange");
    }

    @Override
    public void onPageTreeChanged() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
        Drawable mPersonaDrawable = Persona.getDrawable();
        if (mPersonaDrawable instanceof BitmapDrawable){
            ((BitmapDrawable) mPersonaDrawable).getBitmap().recycle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);


        View rootView = localInflater.inflate(R.layout.fragment_persona_preview, container, false);


        Persona = (PhotoView) rootView.findViewById(R.id.Persona);


        return rootView;
    }
}
