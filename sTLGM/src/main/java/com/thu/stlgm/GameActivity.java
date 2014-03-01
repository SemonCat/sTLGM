package com.thu.stlgm;

import android.accounts.Account;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.adapter.PlayerInfoAdapter;
import com.thu.stlgm.api.BeanTranslate;
import com.thu.stlgm.api.PollHandler;
import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.AccountBean;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.facebook.FBMultiAccountMgr;
import com.thu.stlgm.fragment.LoginFragment;
import com.thu.stlgm.game.Ball;
import com.thu.stlgm.game.Ball_;
import com.thu.stlgm.game.Choice;
import com.thu.stlgm.game.Choice_;
import com.thu.stlgm.game.Medicine_;
import com.thu.stlgm.util.AccountFinder;
import com.thu.stlgm.util.MusicManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EActivity(R.layout.activity_game)
public class GameActivity extends BaseActivity implements PollHandler.OnMessageReceive{

    private static final String TAG = GameActivity.class.getName();

    public static final String FIRSTACCOUNT = "FirstAccount";

    private PollHandler mPollHandler;

    @ViewById
    ListView ListViewPlayerInfo;

    private PlayerInfoAdapter mPlayerInfoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @AfterViews
    void Init(){

        initAdapter();

    }

    private void setupPollHandler(StudentBean mStudentBean){
        mPollHandler = new PollHandler(mStudentBean.getGroupID());
        mPollHandler.setListener(this);

        mPollHandler.addStudent(mStudentBean);

        mPollHandler.start();
    }

    @Override
    public void OnMissionReceive(String quizid, String taskid, String groupid) {
        Log.d(TAG, "ReceiveMission,QuizID:" + quizid + " TaskID:" + taskid + " GID:" + groupid);
    }

    @Override
    public void OnMissionSuccess(int GetCoin, int AllCoin) {
        Log.d(TAG, "Mission Success,GetCoin:" + GetCoin + " AllCoin:" + AllCoin);
    }

    @Override
    public void OnHpStart(long Time, int Interval) {
        Log.d(TAG, "OnHpStart,Time:" + new SimpleDateFormat("HH:mm:ss").format(new Date(Time)) + " Interval:" + Interval);
    }

    @Override
    public void OnHpInfo(int blood) {
        Log.d(TAG, "OnHpInfo,Hp:" + blood);
    }

    @Override
    public void OnHpPause() {
        Log.d(TAG, "OnHpPause");
    }


    @Override
    public void getAdditional() {
        Log.d(TAG, "getAdditional");
    }

    private void initAdapter(){
        if (mPlayerInfoAdapter==null){
            StudentBean mStudentBean = getStudentBean();
            mPlayerInfoAdapter = new PlayerInfoAdapter(this,mStudentBean);

            if (mStudentBean!=null){
                //開始Polling
                setupPollHandler(mStudentBean);

                //查詢組員人數
                SQService.getGroupMemberCounter(mStudentBean.getGroupID(),new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String counterString = new String(responseBody);
                        int counter = Integer.valueOf(counterString);
                        if (counter>0)
                            mPlayerInfoAdapter.setupMemberCounter(counter);
                        else
                            mPlayerInfoAdapter.setupMemberCounter(5);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        error.printStackTrace();
                        mPlayerInfoAdapter.setupMemberCounter(5);
                    }
                });
            }

        }

        ListViewPlayerInfo.setAdapter(mPlayerInfoAdapter);

    }

    @ItemClick
    public void ListViewPlayerInfoItemClicked(final int position) {
        if (!mPlayerInfoAdapter.getItem(position).isLogin()){


            SQService.StudentLogin(this, new SQService.OnSQLoginFinish() {
                @Override
                public void OnSQLoginFinish(StudentBean mData) {
                    mPlayerInfoAdapter.refreshData(position,mData);
                }

                @Override
                public void OnSQLoginFail(String fid) {
                    showToast("登入失敗，該帳號不存在於SQ資料庫中！");
                }

                @Override
                public void OnSQLoginNetworkError(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    showToast("網路錯誤，請尋求助教支援！");
                }
            });
        }else if (mPlayerInfoAdapter.getItemViewType(position)==
                        PlayerInfoAdapter.TYPE_ITEM_CHOISABLE){
            mPlayerInfoAdapter.switchLeader(position);
        }else{
            mPlayerInfoAdapter.setChoisable(position);
        }
    }

    private StudentBean getStudentBean(){
        Bundle mBundle = getIntent().getExtras();
        if (mBundle!=null){
            StudentBean mStudentBean = (StudentBean)mBundle.getSerializable(FIRSTACCOUNT);
            if (mStudentBean!=null)
                return mStudentBean;
            else
                return null;
        }else
            return null;
    }

    private void playChoice(){

        replaceFragment(new Choice_());

    }

    @Click
    void playMedicine(){

        replaceFragment(new Medicine_());

    }

    @Click
    void playBall(){

        replaceFragment(new Ball_());
    }

    private void replaceFragment(Fragment mFragment){

        FragmentTransaction transaction =getFragmentManager().beginTransaction();

        transaction.replace(R.id.GameContent, mFragment);

        transaction.commit();

    }

    public void addBlood(){
        mPlayerInfoAdapter.setLeaderBloodState();
    }

    public void setBlood(int value){
        mPlayerInfoAdapter.setBlood(value);
    }


}
