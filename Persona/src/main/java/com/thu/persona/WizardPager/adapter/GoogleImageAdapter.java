package com.thu.persona.WizardPager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.thu.persona.R;
import com.thu.persona.WizardPager.bean.ImageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/5/27.
 */
public class GoogleImageAdapter extends BaseAdapter {

    private List<ImageResult> mData;

    public GoogleImageAdapter() {
        mData = new ArrayList<ImageResult>();
    }

    public GoogleImageAdapter(List<ImageResult> mData) {
        this.mData = mData;
    }

    public void refrush(List<ImageResult> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ImageResult getItem(int position) {
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
            convertView = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_google_image,
                    parent, false);

            holder = new ViewHolder();
            holder.Picture = (ImageView) convertView.findViewById(R.id.Picture);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Picasso.with(parent.getContext()).load(getItem(position).getFullUrl()).resize(50,50).into(holder.Picture);

        return convertView;
    }

    class ViewHolder {
        ImageView Picture;
    }
}
