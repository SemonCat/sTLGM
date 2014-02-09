package com.thu.stlgm.adapter;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.AccountBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class PlayerInfoAdapter extends BaseAdapter{

    private Context mContext;
    private List<AccountBean> mData;

    public PlayerInfoAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<AccountBean>();
    }

    public void refreshData(int position,AccountBean mAccount){
        if (!mData.contains(mAccount)){
            mData.remove(position);
            mData.add(position,mAccount);
            notifyDataSetChanged();
        }
    }

    public PlayerInfoAdapter(Context context,List<AccountBean> Data) {
        this.mContext = context;
        this.mData = Data;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_playerinfo,
                    parent, false);

            holder = new ViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
            holder.Photo = (RoundedImageView) convertView.findViewById(R.id.Photo);
            holder.LoginState = (TextView) convertView.findViewById(R.id.LoginState);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        AccountBean mAccount = getItem(position);
        holder.Name.setText(mAccount.getName());

        if (mAccount.isLogin())
            holder.LoginState.setText("已登入");
        else
            holder.LoginState.setText("未登入");

        return convertView;
    }

    class ViewHolder{
        RoundedImageView Photo;
        TextView Name;
        TextView LoginState;
    }
}
