package com.thu.stlgm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.stlgm.R;
import com.thu.stlgm.bean.Photo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by SemonCat on 2014/3/21.
 */
public class PhotoAdapter extends BaseAdapter{

    private Context mContext;
    private List<Photo> mAlbumList;

    public PhotoAdapter(Context mContext) {
        this.mContext = mContext;
        mAlbumList = new ArrayList<Photo>();
    }

    public PhotoAdapter(Context context, List<Photo> albumList) {
        this.mAlbumList  = albumList;
        this.mContext = context;
    }

    public void Refresh(List<Photo> albumList){
        this.mAlbumList = albumList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAlbumList.size();
    }

    @Override
    public Photo getItem(int position) {
        return mAlbumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mAlbumList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_photo,
                    parent, false);

            holder = new PhotoAdapter.ViewHolder();
            holder.Photo = (ImageView) convertView.findViewById(R.id.Photo);
            holder.Caption = (TextView) convertView.findViewById(R.id.Caption);

            convertView.setTag(holder);
        }else{
            holder = (PhotoAdapter.ViewHolder) convertView.getTag();
        }

        Photo mPhoto = getItem(position);

        ImageLoader.getInstance().displayImage(mPhoto.getSrc_big(),holder.Photo);


        holder.Caption.setText(mPhoto.getCaption());


        return convertView;


    }


    class ViewHolder{
        ImageView Photo;
        TextView Caption;
    }
}
