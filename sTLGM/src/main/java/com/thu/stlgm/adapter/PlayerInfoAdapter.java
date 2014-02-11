package com.thu.stlgm.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.thu.stlgm.R;
import com.thu.stlgm.anim.AnimUtils;
import com.thu.stlgm.bean.AccountBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class PlayerInfoAdapter extends BaseAdapter{

    private static final String TAG = PlayerInfoAdapter.class.getName();

    private static final int TYPE_ITEM = -1;
    private static final int TYPE_LEADER = -2;
    private static final int TYPE_ITEM_LOGIN = 0;
    private static final int TYPE_LEADER_LOGIN = 1;
    public static final int TYPE_ITEM_CHOISABLE = 3;
    private static final int TYPE_MAX_COUNT = 5;

    private Context mContext;
    private List<AccountBean> mData;

    private int mChoisablePosition = -1;

    private Handler mHandler;
    //切換狀態TIMEOUT
    private static final int mSwitchTimeout = 5000;

    private int AnimPosition = -1;
    private boolean mPlayAnim = false;

    public PlayerInfoAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<AccountBean>();
        mHandler = new Handler();
    }



    public PlayerInfoAdapter(Context context,List<AccountBean> Data) {
        this.mContext = context;
        this.mData = Data;
        mHandler = new Handler();
    }

    public void refreshData(int position,AccountBean mAccount){
        if (!mData.contains(mAccount)){
            mData.remove(position);
            mData.add(position,mAccount);
            notifyDataSetChanged();
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
    public AccountBean getItem(int position) {
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

    private View TypeItemLogin(int position,View convertView, ViewGroup parent){
        ItemLoginViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_playerinfo_login,
                            parent, false);

            holder = new ItemLoginViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.Blood = (TextView) convertView.findViewById(R.id.Blood);

            convertView.setTag(holder);
        }else{
            holder = (ItemLoginViewHolder) convertView.getTag();
        }

        AccountBean mAccount = getItem(position);
        holder.Name.setText(mAccount.getName());


        holder.Blood.setText("54/100");


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

        AccountBean mAccount = getItem(position);

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

            convertView.setTag(holder);
        }else{
            holder = (LeaderViewHolder) convertView.getTag();
        }

        AccountBean mAccount = getItem(position);
        holder.Name.setText(mAccount.getName());

        holder.LoginState.setText("登入失敗");

        return convertView;
    }
    
    private View TypeLeaderLogin(int position,View convertView, ViewGroup parent){
        LeaderLoginViewHolder holder;

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.adapter_leaderinfo_login,
                            parent, false);

            holder = new LeaderLoginViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.Blood = (TextView) convertView.findViewById(R.id.Blood);



            convertView.setTag(holder);
        }else{
            holder = (LeaderLoginViewHolder) convertView.getTag();
        }

        AccountBean mAccount = getItem(position);
        holder.Name.setText(mAccount.getName());

        holder.Blood.setText("60/100");



        if (mPlayAnim){
            Animation animation = AnimUtils.getPushLeftOutRightIn(mContext,convertView,null);
            convertView.startAnimation(animation);
        }


        return convertView;
    }

    class ItemViewHolder{
        RoundedImageView Photo;
        TextView LoginState;
    }
    
    class ItemLoginViewHolder{
        RoundedImageView Photo;
        TextView Name;
        TextView Blood;
    }

    class ItemChoisableViewHolder{
        RoundedImageView Photo;
        Button SwitchLeader;
    }

    class LeaderViewHolder{
        RoundedImageView Photo;
        TextView Name;
        TextView LoginState;
    }

    class LeaderLoginViewHolder{
        RoundedImageView Photo;
        TextView Name;
        TextView Blood;
    }
}
