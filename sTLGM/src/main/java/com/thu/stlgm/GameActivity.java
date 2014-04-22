package com.thu.stlgm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thu.stlgm.api.FacebookAlbumUtils;
import com.thu.stlgm.bean.Photo;
import com.thu.stlgm.fragment.AlbumFragment;
import com.thu.stlgm.game.gallifrey.GallifreyFragment;

import com.facebook.Request;
import com.facebook.Response;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.stlgm.adapter.PlayerInfoAdapter;
import com.thu.stlgm.api.PollHandler;
import com.thu.stlgm.api.SQService;
import com.thu.stlgm.bean.Blood;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.component.PinEntryDialogController;
import com.thu.stlgm.component.UPinEntryDelegate;
import com.thu.stlgm.dialog.AdminLoginDialog;
import com.thu.stlgm.facebook.FBMultiAccountMgr;
import com.thu.stlgm.fragment.GameFragmentMgr;
import com.thu.stlgm.fragment.PhotoFragment;
import com.thu.stlgm.fragment.PhotoFragment_;
import com.thu.stlgm.fragment.PhotoUploadFragment_;
import com.thu.stlgm.fragment.WorkFragment;
import com.thu.stlgm.game.Ball;
import com.thu.stlgm.game.Ball_;
import com.thu.stlgm.game.BaseGame;
import com.thu.stlgm.game.FlippyBookFragment;
import com.thu.stlgm.game.GameMgr;
import com.thu.stlgm.game.GlowBallFragment;
import com.thu.stlgm.game.Medicine;
import com.thu.stlgm.game.Medicine_;
import com.thu.stlgm.game.PuzzleFragment;
import com.thu.stlgm.painter.PainterFragment;
import com.thu.stlgm.util.ConstantUtil;
import com.thu.stlgm.util.MedicineValueUtil;
import com.thu.stlgm.util.PlayStateMgr;
import com.thu.stlgm.util.SharedPreferencesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
@EActivity(R.layout.activity_game)
public class GameActivity extends BaseActivity implements GameMgr.OnGameFinishListener,PollHandler.OnMessageReceive, Medicine.OnMedicineGetListener, PlayerInfoAdapter.OnLeaderChangeListener {

    private static final String TAG = GameActivity.class.getName();

    public static final String FIRSTACCOUNT = "FirstAccount";

    private PollHandler mPollHandler;

    @ViewById
    ListView ListViewPlayerInfo;

    @ViewById
    ImageView facebook;

    @ViewById
    ImageView camera;

    @ViewById
    TextView Money;

    @ViewById
    ViewGroup MoneyInfo;

    @ViewById
    ProgressBar UploadCameraProgressBar;

    private PlayerInfoAdapter mPlayerInfoAdapter;

    private GameFragmentMgr mGameFragmentMgr;

    private GameMgr mGameMgr;

    private int playTime = 0;

    private PlayStateMgr mPlayStateMgr;

    private PlayerInfoAdapter.OnLeaderChangeListener mLeaderChangeListener;

    private Handler mHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookAlbumUtils.LoadAlbumCache();

        mHandler = new Handler();
    }

    @AfterViews
    void Init() {
        mGameFragmentMgr = new GameFragmentMgr(this, R.id.GameContent);
        mGameMgr = new GameMgr(this,R.id.GameContent);
        mGameMgr.setListener(this);
        initAdapter();



    }

    private void setupPollHandler(StudentBean mStudentBean) {
        mPollHandler = new PollHandler(mStudentBean.getGroupID());
        mPollHandler.setListener(this);

        mPollHandler.addStudent(mStudentBean);

        mPollHandler.start();
    }

    @Override
    @UiThread
    public void OnMissionReceive(String quizid, String taskid, String groupid) {
        if (mGameMgr.IsRun()) return;
        Log.d(TAG, "ReceiveMission,QuizID:" + quizid + " TaskID:" + taskid + " GID:" + groupid);

        mPlayStateMgr = new PlayStateMgr(mPollHandler.getLoginStudent());

        /*
        if (quizid.equals("0")) {
            playBall(0);
        }else if (quizid.equals("1")){
            playBall(1);
        }else if (quizid.equals("2")){
            playFlappy(2);
        }
        */




        if (quizid.equals("21")) {
            playPuzzle(21);
        }else if (quizid.equals("22")) {
            playFlappy(22);
        }else if (quizid.equals("23")){
            playGallifrey(23);
        }else if (quizid.equals("41")){
            playPuzzle(41);
        }else if (quizid.equals("43")){
            playBall(43);
        }else if (taskid.equals("WhiteBoard")){
            ShowWhiteBoard(quizid);
        }

    }

    @Override
    @UiThread
    public void OnMissionSuccess(int GetCoin, int AllCoin) {
        Log.d(TAG, "Mission Success,GetCoin:" + GetCoin + " AllCoin:" + AllCoin);
        Money.setText(String.valueOf(AllCoin));
    }

    @Override
    @UiThread
    public void OnHpStart(StudentBean mStudent, long Time, int Interval) {
        Log.d(TAG, "OnHpStart,SID:" + mStudent.getSID() + " Time:" + new SimpleDateFormat("HH:mm:ss").format(new Date(Time)) + " Interval:" + Interval);


        List<StudentBean> mStudents = mPlayerInfoAdapter.getStudents();
        StudentBean studentBean = mStudents.get(mStudents.indexOf(mStudent));
        if (studentBean != null) {
            studentBean.startHpService(Time, Interval);
        } else {
            Log.d(TAG, "studentBean==null");
        }
    }

    @Override
    @UiThread
    public void OnHpInfo(StudentBean mStudent, int blood) {
        Log.d(TAG, "OnHpInfo,Hp:" + blood);
    }

    @Override
    @UiThread
    public void OnHpPause() {
        for (StudentBean mStudent:mPlayerInfoAdapter.getStudents()){
            mStudent.pasueHpService();
        }

        Log.d(TAG, "OnHpPause");
    }


    @Override
    @UiThread
    public void getAdditional(StudentBean mTarget, int blood) {
        Log.d(TAG, "getAdditional:" + blood);
        playMedicine(mTarget.getSID(), blood, this);
    }

    @Override
    public void OnLeaderChangeEvent(StudentBean leader) {

        checkMedicine();

        if (mLeaderChangeListener!=null)
            mLeaderChangeListener.OnLeaderChangeEvent(leader);
    }

    private void initAdapter() {
        if (mPlayerInfoAdapter == null) {
            StudentBean mStudentBean = getStudentBean();
            mPlayerInfoAdapter = new PlayerInfoAdapter(ListViewPlayerInfo, mStudentBean);
            mPlayerInfoAdapter.setOnLeaderChangeListener(this);


            initGroupData(mStudentBean);

        }

        ListViewPlayerInfo.setAdapter(mPlayerInfoAdapter);

    }

    private void initGroupData(StudentBean mStudentBean){
        if (mStudentBean != null) {
            //開始Polling
            setupPollHandler(mStudentBean);

            //查詢組員人數
            SQService.getGroupMemberCounter(mStudentBean.getGroupID(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String counterString = new String(responseBody);
                    int counter = Integer.valueOf(counterString);
                    if (counter > 0) {
                        mPlayerInfoAdapter.setupMemberCounter(counter);
                    }else{
                        mPlayerInfoAdapter.setupMemberCounter(5);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();
                    mPlayerInfoAdapter.setupMemberCounter(5);
                }
            });

            //查詢金錢
            SQService.getGroupCoin(mStudentBean.getGroupID(),new SQService.OnGroupCoinGet() {
                @Override
                public void OnGroupCoinGetEvent(int coin) {
                    Money.setText(String.valueOf(coin));
                }
            });
        }
    }

    /**
     * 管理者強制登入
     * @param position
     */
    @ItemLongClick
    public void ListViewPlayerInfoItemLongClicked(final int position) {

        UPinEntryDelegate mUPinEntryDelegate = new UPinEntryDelegate();


        final AlertDialog mPinEntryDialog =
                PinEntryDialogController.getEntryDialog(this,mUPinEntryDelegate);

        mUPinEntryDelegate.setListener(new UPinEntryDelegate.OnPinEntryListener() {
            @Override
            public void OnEnterCorrectPin() {
                showToast("OnEnterCorrectPin");

                SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
                    @Override
                    public void OnAllStudentGetEvent(List<StudentBean> studentList) {
                        AdminLoginDialog adminLoginDialog =
                                new AdminLoginDialog(GameActivity.this,
                                        studentList,
                                        new AdminLoginDialog.OnLoginClickListener() {
                                    @Override
                                    public void OnLoginClickEvent(final StudentBean mStudent) {

                                        SQService.StudentLogin(mStudent.getId(),new SQService.OnSQLoginFinish() {
                                            @Override
                                            public void OnSQLoginFinish(StudentBean mData) {

                                                String accessToken = SharedPreferencesUtils.
                                                        getFB_AccessToken(GameActivity.this,mData.getId());

                                                if (accessToken==null) return;
                                                mData.setName(mStudent.getName());
                                                mData.setAccessToken(accessToken);

                                                if (mPollHandler != null) {
                                                    mPollHandler.addStudent(mData);
                                                }else{
                                                    initGroupData(mData);
                                                }

                                                mPlayerInfoAdapter.refreshData(position, mData);
                                            }

                                            @Override
                                            public void OnSQLoginFail(String fid) {

                                            }

                                            @Override
                                            public void OnSQLoginNetworkError(
                                                    int statusCode, Header[] headers,
                                                    byte[] responseBody, Throwable error) {

                                            }
                                        });


                                    }
                                });

                        adminLoginDialog.show(getFragmentManager(),null);
                    }
                });

                mPinEntryDialog.dismiss();
            }

            @Override
            public void OnEnterFailPin() {
                mPinEntryDialog.dismiss();
            }
        });

        mPinEntryDialog.show();
    }

    @ItemClick
    public void ListViewPlayerInfoItemClicked(final int position) {
        if (!mPlayerInfoAdapter.getItem(position).isLogin()) {


            SQService.StudentLogin(this, new SQService.OnSQLoginFinish() {
                @Override
                public void OnSQLoginFinish(StudentBean mData) {
                    /*
                    if (mData.getGroupID()!=mPlayerInfoAdapter.getLeaderStudent().getGroupID()) {
                        showToast("不同組請勿交互登入！");
                        return;
                    }
                    */

                    if (mPollHandler != null) {
                        mPollHandler.addStudent(mData);
                    }else{
                        initGroupData(mData);
                    }

                    mPlayerInfoAdapter.refreshData(position, mData);
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
        } else if (mPlayerInfoAdapter.getItemViewType(position) ==
                PlayerInfoAdapter.TYPE_ITEM_CHOISABLE) {
            mPlayerInfoAdapter.switchLeader(position);
        } else if (mPlayerInfoAdapter.getItemViewType(position) ==
                PlayerInfoAdapter.TYPE_LEADER_LOGIN){

                StudentBean mLeader = mPlayerInfoAdapter.getLeaderStudent();
                SQService.SingUp(mLeader.getSID(),mLeader.getId(),new AsyncHttpResponseHandler());
        } else{
            mPlayerInfoAdapter.setChoisable(position);
        }
    }

    private StudentBean getStudentBean() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            StudentBean mStudentBean = (StudentBean) mBundle.getSerializable(FIRSTACCOUNT);
            if (mStudentBean != null)
                return mStudentBean;
            else
                return null;
        } else
            return null;
    }

    void playMedicine(String sid, int reward, Medicine.OnMedicineGetListener mListener) {

        if (!mGameMgr.IsRun()){
            Medicine_ medicine_ = new Medicine_();
            medicine_.setListener(reward, mListener);
            medicine_.setTargetSid(sid);
            medicine_.stopStartGameAnim();
            //replaceFragment(medicine_, Medicine.class.getName());
            mGameFragmentMgr.addMedicineQueue(medicine_);
            checkMedicine();
        }
    }

    private void checkMedicine() {
        Fragment fragment = mGameFragmentMgr.getMedicineFromQueue(mPlayerInfoAdapter.getLeaderStudent().getSID());
        if (fragment != null) {
            mGameFragmentMgr.replaceFragment(fragment);
        }
    }

    @Click
    void playBall() {



        //playGlowBall(0);
        //playFlappy(0);
    }

    void playBall(int quizid){
        Ball_ ball_ = new Ball_();
        ball_.setupType(quizid);

        mGameMgr.PlayGame(quizid,ball_, Ball.class.getName(), mPollHandler.getLoginStudentCounter()+1);
    }

    void playPuzzle(int quizid){
        mGameMgr.PlayGame(quizid,new PuzzleFragment(), Ball.class.getName(), mPollHandler.getLoginStudentCounter()+1);
    }

    void playFlappy(int quizid){
        mGameMgr.PlayGame(quizid,new FlippyBookFragment(), Ball.class.getName(), mPollHandler.getLoginStudentCounter()+1);
    }

    void playGlowBall(int quizid){
        mGameMgr.PlayGame(quizid,new GlowBallFragment(), Ball.class.getName(), mPollHandler.getLoginStudentCounter()+1);
    }

    void playGallifrey(int quizid){
        mGameMgr.PlayGame(quizid,new GallifreyFragment(), Ball.class.getName(), mPollHandler.getLoginStudentCounter()+1);
    }

    void ShowWhiteBoard(String Week){

        HideCamera();

        FragmentTransaction transaction =getFragmentManager().beginTransaction();

        transaction.replace(R.id.FBContent, new PainterFragment(Week), Fragment.class.getName());

        transaction.commitAllowingStateLoss();

        //replaceFragment(new PainterFragment(Week));
    }
    @Override
    public void OnGameStartEvent() {
        HideInfo();
    }

    @Override
    public void OnGameOverEvent(boolean IsWin) {
        ShowInfo();
        ShowCamera();

        mGameMgr.setIsRun(false);

        /*
        if (IsWin) {
            SQService.addMoney(mPlayerInfoAdapter.getLeaderStudent().getGroupID());
        }
        */
    }

    @Override
    public void OnGameNextEvent(boolean IsWin,int round) {


        if (round!=1){
            mPlayStateMgr.setPlayed(mPlayerInfoAdapter.getLeaderStudent().getSID());


        }
        ShowInfo();
    }

    @Override
    public void OnGameFinishEvent(boolean IsWin, int round) {
        Log.d(TAG,"Round:"+round+",IsWin:"+IsWin);

        if (round!=0 && IsWin){
                Log.d(TAG,"Win!!!");
                SQService.AddStudentCoin(
                        mPlayerInfoAdapter.getLeaderStudent().getSID(),
                        ConstantUtil.WinCoin);

        }
    }

    private void replaceFragment(BaseGame mFragment, String mFragmentTag) {

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

    private void replaceFragment(Fragment mFragment){
        FragmentTransaction transaction =getFragmentManager().beginTransaction();
        Fragment findFragment = getFragmentManager().findFragmentByTag(Fragment.class.getName());
        if (findFragment!=null){
            return;
        }
        transaction.replace(R.id.FBContent, mFragment, Fragment.class.getName());

        transaction.commit();
    }

    public void addBlood(int blood) {
        mPlayerInfoAdapter.addLeaderBlood(blood);

    }

    public void setBlood(int value) {
        mPlayerInfoAdapter.setLeaderBlood(value);
    }


    @Override
    public void OnMedicineGetEvent(int reward) {

        StudentBean leader = mPlayerInfoAdapter.getLeaderStudent();

        long rewardMills;
        Blood bloodBean;
        if (leader.getBlood() > 25) {
            bloodBean = MedicineValueUtil.getSmallMedicine(leader.getBlood(), reward, leader.getInterval(), StudentBean.MAX_BLOOD);
            rewardMills = bloodBean.getBloodMills();
        } else {
            bloodBean = MedicineValueUtil.getBigMedicine(leader.getBlood(), reward, leader.getInterval(), StudentBean.MAX_BLOOD);
            rewardMills = bloodBean.getBloodMills();
        }

        int rewardSecond = (int) (rewardMills / 1000);

        addBlood(bloodBean.getBlood());
        SQService.getMedicine(leader.getSID(), rewardSecond);

    }

    public void postPhoto(String albumId,byte[] photo, Request.OnProgressCallback mCallback) {
        FBMultiAccountMgr multiAccountMgr = new FBMultiAccountMgr(this);
        multiAccountMgr.
                postPhoto(mPlayerInfoAdapter.getLeaderStudent(),
                        albumId, photo, mCallback);
    }

    public void postPhoto(byte[] photo, Request.OnProgressCallback mCallback) {
        postPhoto(ConstantUtil.WeekAlbum,photo,mCallback);
    }

    public void LikePhoto(final Photo mPhoto){
        FBMultiAccountMgr multiAccountMgr = new FBMultiAccountMgr(this);
        multiAccountMgr.likePhoto(mPlayerInfoAdapter.getLeaderStudent(),mPhoto.getId(),new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (response.getError()==null){
                    showToast("按讚成功！");
                    mPhoto.setLike_count(mPhoto.getLike_count()+1);
                }
            }
        });
    }

    @Click
    void camera() {


        //startCamera();

        FragmentTransaction transaction =getFragmentManager().beginTransaction();

        transaction.replace(R.id.GameContent, new PhotoUploadFragment_());

        transaction.commit();

        facebook.setVisibility(View.GONE);

    }

    boolean IsFbShow = false;
    AlbumFragment workFragment;

    @Click
    void facebook() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (IsFbShow){
            workFragment.Finish();
            IsFbShow = false;

        }else{
            workFragment = new AlbumFragment();

            transaction.replace(R.id.FBContent, workFragment);

            transaction.commit();

            IsFbShow = true;
        }
    }


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    private File tmpPhotoPath = new File(Environment.getExternalStorageDirectory(), "photo.jpg");

    void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tmpPhotoPath));
        startActivityForResult(intent,
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //Upload();
            }
        }
    }

    void Upload() {


        File file = tmpPhotoPath;
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (Exception e) {

        }

        postPhoto(bytes, new Request.OnProgressCallback() {
            @Override
            public void onProgress(long current, long max) {
            }

            @Override
            public void onCompleted(Response response) {
                Log.d(TAG,"Result:"+response.toString());
                if (response.getError()==null){
                    facebook.setVisibility(View.GONE);
                }
            }
        });

    }

    public void HideInfo(){
        ListViewPlayerInfo.setVisibility(View.GONE);
        MoneyInfo.setVisibility(View.GONE);
    }

    public void ShowInfo(){
        ListViewPlayerInfo.setVisibility(View.VISIBLE);
        MoneyInfo.setVisibility(View.VISIBLE);
    }

    public void HideCamera(){
        camera.setVisibility(View.GONE);
        facebook.setVisibility(View.GONE);
    }

    public void ShowCamera(){
        camera.setVisibility(View.VISIBLE);
        facebook.setVisibility(View.GONE);
    }

    public void ShowCameraUploadProgress(){
        camera.setVisibility(View.GONE);
        UploadCameraProgressBar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HideCameraUploadProgress();
            }
        },10*1000);

    }

    public void HideCameraUploadProgress(){
        camera.setVisibility(View.VISIBLE);
        UploadCameraProgressBar.setVisibility(View.GONE);
    }

    public boolean getPlayState(){
        return mPlayStateMgr.getPlayed(mPlayerInfoAdapter.getLeaderStudent().getSID());
    }

    public StudentBean getLeader(){
        return mPlayerInfoAdapter.getLeaderStudent();
    }

    public void setLeaderChangeListener(PlayerInfoAdapter.OnLeaderChangeListener mLeaderChangeListener) {
        this.mLeaderChangeListener = mLeaderChangeListener;
    }
}
