package com.thu.stlgm.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.RoundedImageView;
import com.thu.stlgm.R;
import com.thu.stlgm.anim.AnimUtils;
import com.thu.stlgm.bean.StudentBean;
import com.thu.stlgm.component.BloodView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class PlayerInfoAdapter extends BaseAdapter{

    private static final String TAG = PlayerInfoAdapter.class.getName();


    public interface OnLeaderChangeListener {
        void OnLeaderChangeEvent(StudentBean leader);
    }

    private static final int TYPE_ITEM = -1;
    private static final int TYPE_LEADER = -2;
    private static final int TYPE_ITEM_LOGIN = 0;
    private static final int TYPE_LEADER_LOGIN = 1;
    public static final int TYPE_ITEM_CHOISABLE = 3;
    private static final int TYPE_MAX_COUNT = 5;

    private Context mContext;
    private List<StudentBean> mData;

    private int mChoisablePosition = -1;

    private Handler mHandler = new Handler();
    //切換狀態TIMEOUT
    private static final int mSwitchTimeout = 5000;

    private int AnimPosition = -1;
    private boolean mPlayAnim = false;


    private Animation fade_out_push_top;


    private ListView mListView;

    private OnLeaderChangeListener mListener;

    public PlayerInfoAdapter(ListView listView) {
        this.mListView = listView;
        this.mContext = mListView.getContext();
        mData = new ArrayList<StudentBean>();
        initAnim();
    }

    public PlayerInfoAdapter(ListView listView,StudentBean Data) {
        this.mListView = listView;
        this.mContext = mListView.getContext();
        this.mData = new ArrayList<StudentBean>();
        if (Data!=null){
            mData.add(Data);
        }else{
            for (int i = 0;i<5;i++)
                mData.add(i,new StudentBean());
        }

        initAnim();
    }

    public PlayerInfoAdapter(ListView listView,List<StudentBean> Data) {
        this.mListView = listView;
        this.mContext = mListView.getContext();
        this.mData = Data;

        initAnim();
    }

    private void initAnim(){
        fade_out_push_top = AnimationUtils.loadAnimation(mContext,R.anim.fade_out_push_top);
    }

    public void setupMemberCounter(int counter){
        int max = counter - getCount();
        for (int i = 0;i<max;i++){
            mData.add(i,new StudentBean());
        }

        notifyDataSetChanged();
    }

    public void refreshData(int position,StudentBean mStudentBean){
        if (!mData.contains(mStudentBean)){
            mData.remove(position);
            mData.add(position,mStudentBean);
            notifyDataSetChanged();
        }else{
            Toast.makeText(mContext,"請勿重複登入！",Toast.LENGTH_SHORT).show();
        }
    }


    private Runnable disableChoisableCallback = new Runnable() {
        @Override
        public void run() {
            disableChoisable();
        }
    };
    public void setChoisable(int position){
        this.mChoisablePosition = position;
        notifyDataSetChanged();
        mHandler.postDelayed(disableChoisableCallback,mSwitchTimeout);
    }

    public void disableChoisable(){
        this.mChoisablePosition = -1;
        notifyDataSetChanged();
    }

    public void switchLeader(final int position){
        mPlayAnim = true;
        AnimPosition = position;

        disableChoisable();
        mHandler.removeCallbacks(disableChoisableCallback);

        notifyDataSetChanged();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.swap(mData, position, mData.size() - 1);
                mPlayAnim = false;
                AnimPosition = -1;
                notifyDataSetChanged();

                if (mListener!=null)
                    mListener.OnLeaderChangeEvent(getItem(getCount()-1));
            }
        },300);
    }

    @Override
    public int getItemViewType(int position) {
        //Leader
        if (position==(getCount()-1)){
            if (getItem(position).isLogin())
                return TYPE_LEADER_LOGIN;
            else 
                return TYPE_LEADER;
        }else{
            if (getItem(position).isLogin())
                if (position==mChoisablePosition)
                    return TYPE_ITEM_CHOISABLE;
                else
                    return TYPE_ITEM_LOGIN;
            else
                return TYPE_ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public StudentBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        switch (getItemViewType(position)){
            case TYPE_ITEM:
                return TypeItem(position, convertView, parent);
            case TYPE_ITEM_LOGIN:
                return TypeItemLogin(position,convertView,parent);
            case TYPE_ITEM_CHOISABLE:
                return TypeItemChoisable(position, convertView, parent);
            case TYPE_LEADER:
                return TypeLeader(position,convertView,parent);
            case TYPE_LEADER_LOGIN:
                return TypeLeaderLogin(position,convertView,parent);

            default:
                return TypeItem(position, convertView, parent);
        }
    }

    private View TypeItemLogin(final int position,View convertView, ViewGroup parent){
        ItemLoginViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_playerinfo_login,
                            parent, false);

            holder = new ItemLoginViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.Blood = (BloodView) convertView.findViewById(R.id.Blood);

            convertView.setTag(holder);
        }else{
            holder = (ItemLoginViewHolder) convertView.getTag();
        }

        StudentBean mStudentBean = getItem(position);
        holder.Name.setText(mStudentBean.getName());

        mStudentBean.setBloodChangeListener(new StudentBean.OnBloodChangeListener() {
            @Override
            public void OnBloodChangeEvent(final int blood) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        BloodView view = getBloodView(position);
                        if (view!=null){
                            view.setBlood(blood);
                        }
                    }
                });
            }
        });


        holder.Blood.setBlood(mStudentBean.getBlood());


        if (position==AnimPosition){
            Animation animation = AnimUtils.getPushRightOutLeftIn(mContext,convertView,null);
            convertView.startAnimation(animation);
        }

        return convertView;
    }

    private View TypeItemChoisable(final int position,View convertView, ViewGroup parent){
        ItemChoisableViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_playerinfo_choisable,
                            parent, false);

            holder = new ItemChoisableViewHolder();
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.SwitchLeader = (Button) convertView.findViewById(R.id.SwitchLeader);

            convertView.setTag(holder);
        }else{
            holder = (ItemChoisableViewHolder) convertView.getTag();
        }

        StudentBean mStudentBean = getItem(position);

        holder.SwitchLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLeader(position);
            }
        });
        
        return convertView;
    }


    private View TypeItem(int position,View convertView, ViewGroup parent){
        ItemViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_playerinfo,
                            parent, false);

            holder = new ItemViewHolder();
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.LoginState = (TextView) convertView.findViewById(R.id.LoginState);

            convertView.setTag(holder);
        }else{
            holder = (ItemViewHolder) convertView.getTag();
        }

        holder.LoginState.setText("未登入");

        return convertView;
    }

    private View TypeLeader(int position,View convertView, ViewGroup parent){
        LeaderViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_leaderinfo,
                            parent, false);

            holder = new LeaderViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.LoginState = (TextView) convertView.findViewById(R.id.LoginState);
            holder.BloodState = (TextView) convertView.findViewById(R.id.BloodState);

            convertView.setTag(holder);
        }else{
            holder = (LeaderViewHolder) convertView.getTag();
        }

        StudentBean mStudentBean = getItem(position);
        holder.Name.setText(mStudentBean.getName());

        holder.LoginState.setText("登入失敗");





        return convertView;
    }
    
    private View TypeLeaderLogin(final int position,View convertView, ViewGroup parent){
        LeaderLoginViewHolder holder;
        StudentBean mStudentBean = getItem(position);

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_leaderinfo_login,
                            parent, false);

            holder = new LeaderLoginViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.Blood = (BloodView) convertView.findViewById(R.id.Blood);
            holder.BloodState = (TextView) convertView.findViewById(R.id.BloodState);

            convertView.setTag(holder);
        }else{
            holder = (LeaderLoginViewHolder) convertView.getTag();
        }

        mStudentBean.setBloodChangeListener(new StudentBean.OnBloodChangeListener() {
            @Override
            public void OnBloodChangeEvent(final int blood) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getBloodView(position).setBlood(blood);
                    }
                });
            }
        });


        holder.Name.setText(mStudentBean.getName());

        holder.Blood.setBlood(mStudentBean.getBlood());



        if (mPlayAnim){
            Animation animation = AnimUtils.getPushLeftOutRightIn(mContext,convertView,null);
            convertView.startAnimation(animation);
        }


        return convertView;
    }




    private View getViewByPosition(int position){
        int firstPosition = mListView.getFirstVisiblePosition() - mListView.getHeaderViewsCount(); // This is the same as child #0
        int wantedChild = position - firstPosition;
        // Say, first visible position is 8, you want position 10, wantedChild will now be 2
        // So that means your view is child #2 in the ViewGroup:
        if (wantedChild < 0 || wantedChild >= mListView.getChildCount()) {
            Log.w(TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
            return null;
        }
        // Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
        View wantedView = mListView.getChildAt(wantedChild);
        return wantedView;
    }

    public void getLeaderBlood(){
        View convertView = getViewByPosition(getCount()-1);
        TextView BloodView = (TextView) convertView.findViewById(R.id.Blood);
        BloodView.setText("test");

    }

    public void setLeaderBlood(int blood){
        View convertView = getViewByPosition(getCount()-1);
        BloodView BloodView = (BloodView) convertView.findViewById(R.id.Blood);

        BloodView.setBlood(blood);
    }

    public void addLeaderBlood(final int blood){
        final int animDuration = 50;

        final StudentBean leader = getItem(getCount()-1);
        View convertView = getViewByPosition(getCount()-1);
        final BloodView BloodView = (BloodView) convertView.findViewById(R.id.Blood);

        final int currentBlood = leader.getBlood();

        mHandler.postDelayed(new Runnable() {
            int counter = 0;
            @Override
            public void run() {

                if (counter<blood){
                    counter++;
                    BloodView.setBlood(currentBlood+counter);
                    mHandler.postDelayed(this,animDuration);

                }else{
                    BloodView.setBlood(leader.getBlood());
                }

            }
        }, animDuration);




        showLeaderBloodAnim(blood);
    }

    private BloodView getBloodView(int position){
        View convertView = getViewByPosition(position);
        return (BloodView) convertView.findViewById(R.id.Blood);
    }

    /**
     * 顯示HP增加動作
     */
    private void showLeaderBloodAnim(int value){
        View convertView = getViewByPosition(getCount()-1);
        TextView BloodState = (TextView) convertView.findViewById(R.id.BloodState);

        BloodState.setText("+"+value);
        BloodState.setVisibility(View.VISIBLE);
        //延遲一秒後才上移
        fade_out_push_top.setStartTime(1000);
        BloodState.startAnimation(fade_out_push_top);
    }


    public List<StudentBean> getStudents(){
        return mData;
    }

    public StudentBean getLeaderStudent(){
        return mData.get(getCount()-1);
    }

    public void setOnLeaderChangeListener(OnLeaderChangeListener listener){
        this.mListener = listener;
    }

    /**ViewHolder**/

    class ItemViewHolder{
        RoundedImageView Photo;
        TextView LoginState;
    }
    
    class ItemLoginViewHolder{
        RoundedImageView Photo;
        TextView Name;
        BloodView Blood;
    }

    class ItemChoisableViewHolder{
        RoundedImageView Photo;
        Button SwitchLeader;
    }

    class LeaderViewHolder{
        RoundedImageView Photo;
        TextView Name;
        TextView LoginState;
        TextView BloodState;
    }

    class LeaderLoginViewHolder{
        RoundedImageView Photo;
        TextView Name;
        BloodView Blood;
        TextView BloodState;
    }
}
