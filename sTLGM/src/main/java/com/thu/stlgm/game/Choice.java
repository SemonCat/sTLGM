package com.thu.stlgm.game;

import android.content.Context;
import android.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thu.stlgm.R;
import com.thu.stlgm.adapter.BookChoiceAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/19.
 */
@EFragment(R.layout.fragment_choice)
public class Choice extends BaseGame {

    @ViewById
    GridView Question;

    @ViewById
    TextView StatusTip;

    private BookChoiceAdapter mAdapter;

    @AfterViews
    void Init(){
        setupAdapter();
    }

    private void setupAdapter(){
        mAdapter = new BookChoiceAdapter(getActivity());
        Question.setAdapter(mAdapter);
    }

    @Click
    void Finish(){
        List<BookChoiceAdapter.Book> mDatas = mAdapter.checkAnswer();

        String mCurrent = "答對：";
        String mWrong = "答錯：";

        for (BookChoiceAdapter.Book mBook:mDatas)
            if (mBook.AnswerState== BookChoiceAdapter.AnswerState.CURRENT)
                mCurrent += (mBook.id+1)+",";
            else
                mWrong += (mBook.id+1)+",";

        StatusTip.setText(mCurrent+"\n"+mWrong);
    }
}
