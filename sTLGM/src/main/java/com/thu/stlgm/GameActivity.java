package com.thu.stlgm;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.adapter.PlayerInfoAdapter;
import com.thu.stlgm.api.PollHandler;
import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.Blood;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.fragment.GameFragmentMgr;
import com.thu.stlgm.game.Ball;
import com.thu.stlgm.game.Ball_;
import com.thu.stlgm.game.BaseGame;
import com.thu.stlgm.game.Medicine;
import com.thu.stlgm.game.Medicine_;
import com.thu.stlgm.util.MedicineValueUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EActivity(R.layout.activity_game)
public class GameActivity extends BaseActivity implements PollHandler.OnMessageReceive,Medicine.OnMedicineGetListener,PlayerInfoAdapter.OnLeaderChangeListener{

    private static final String TAG = GameActivity.class.getName();

    public static final String FIRSTACCOUNT = "FirstAccount";

    private PollHandler mPollHandler;

    @ViewById
    ListView ListViewPlayerInfo;

    private PlayerInfoAdapter mPlayerInfoAdapter;

    private GameFragmentMgr mGameFragmentMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @AfterViews
    void Init(){
        mGameFragmentMgr = new GameFragmentMgr(this,R.id.GameContent);
        initAdapter();

    }

    private void setupPollHandler(StudentBean mStudentBean){
        mPollHandler = new PollHandler(mStudentBean.getGroupID());
        mPollHandler.setListener(this);

        mPollHandler.addStudent(mStudentBean);

        mPollHandler.start();
    }

    @Override
    @UiThread
    public void OnMissionReceive(String quizid, String taskid, String groupid) {
        Log.d(TAG, "ReceiveMission,QuizID:" + quizid + " TaskID:" + taskid + " GID:" + groupid);
        if (quizid.equals("1")){
            playBall();
        }
    }

    @Override
    @UiThread
    public void OnMissionSuccess(int GetCoin, int AllCoin) {
        Log.d(TAG, "Mission Success,GetCoin:" + GetCoin + " AllCoin:" + AllCoin);
    }

    @Override
    @UiThread
    public void OnHpStart(StudentBean mStudent,long Time, int Interval) {
        Log.d(TAG, "OnHpStart,SID:"+mStudent.getSID()+" Time:" + new SimpleDateFormat("HH:mm:ss").format(new Date(Time)) + " Interval:" + Interval);


        List<StudentBean> mStudents = mPlayerInfoAdapter.getStudents();
        StudentBean studentBean = mStudents.get(mStudents.indexOf(mStudent));
        if (studentBean!=null){
            studentBean.startHpService(Time,Interval);
        }else{
            Log.d(TAG,"studentBean==null");
        }
    }

    @Override
    @UiThread
    public void OnHpInfo(StudentBean mStudent,int blood) {
        Log.d(TAG, "OnHpInfo,Hp:" + blood);
    }

    @Override
    @UiThread
    public void OnHpPause() {
        Log.d(TAG, "OnHpPause");
    }


    @Override
    @UiThread
    public void getAdditional(StudentBean mTarget,int blood) {
        Log.d(TAG, "getAdditional:"+blood);
        playMedicine(mTarget.getSID(),blood, this);
    }

    @Override
    public void OnLeaderChangeEvent(StudentBean leader) {
        checkMedicine();
    }

    private void initAdapter(){
        if (mPlayerInfoAdapter==null){
            StudentBean mStudentBean = getStudentBean();
            mPlayerInfoAdapter = new PlayerInfoAdapter(ListViewPlayerInfo,mStudentBean);
            mPlayerInfoAdapter.setOnLeaderChangeListener(this);

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
                    mPollHandler.addStudent(mData);
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

    void playMedicine(String sid,int reward,Medicine.OnMedicineGetListener mListener){
        Medicine_ medicine_ = new Medicine_();
        medicine_.setListener(reward,mListener);
        medicine_.setTargetSid(sid);
        //replaceFragment(medicine_, Medicine.class.getName());
        mGameFragmentMgr.addMedicineQueue(medicine_);
        checkMedicine();
    }

    private void checkMedicine(){
        Fragment fragment = mGameFragmentMgr.getMedicineFromQueue(mPlayerInfoAdapter.getLeaderStudent().getSID());
        if (fragment!=null){
            mGameFragmentMgr.replaceFragment(fragment);
        }
    }

    @Click
    void playBall(){

        replaceFragment(new Ball_(),Ball.class.getName());
    }

    private void replaceFragment(BaseGame mFragment,String mFragmentTag){

        /*
        FragmentTransaction transaction =getFragmentManager().beginTransaction();
        Fragment findFragment = getFragmentManager().findFragmentByTag(mFragmentTag);
        if (findFragment!=null){
            return;
        }
        transaction.replace(R.id.GameContent, mFragment, mFragmentTag);

        transaction.commit();
        */
        mGameFragmentMgr.addFragmentQueue(mFragment);
    }



    public void addBlood(int blood){
        mPlayerInfoAdapter.addLeaderBlood(blood);

    }

    public void setBlood(int value){
        mPlayerInfoAdapter.setLeaderBlood(value);
    }


    @Override
    public void OnMedicineGetEvent(int reward) {

        StudentBean leader = mPlayerInfoAdapter.getLeaderStudent();

        long rewardMills;
        Blood bloodBean;
        if (leader.getBlood()>25){
            bloodBean = MedicineValueUtil.getSmallMedicine(leader.getBlood(),reward,leader.getInterval(),StudentBean.MAX_BLOOD);
            rewardMills = bloodBean.getBloodMills();
        }else{
            bloodBean = MedicineValueUtil.getBigMedicine(leader.getBlood(),reward, leader.getInterval(), StudentBean.MAX_BLOOD);
            rewardMills = bloodBean.getBloodMills();
        }

        int rewardSecond = (int)(rewardMills/1000);

        addBlood(bloodBean.getBlood());
        SQService.getMedicine(leader.getSID(),rewardSecond);

    }
}
