package com.thu.stlgm.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.etsy.android.grid.StaggeredGridView;
import com.thu.stlgm.R;
import com.thu.stlgm.adapter.AlbumAdapter;
import com.thu.stlgm.api.FacebookAlbumUtils;
import com.thu.stlgm.bean.Album;
import com.thu.stlgm.bean.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class AlbumFragment extends BaseFragment{

    private static final String TAG = AlbumFragment.class.getName();

    private GridView Album;

    private ProgressBar mLoadingBar;

    private AlbumAdapter mAlbumAdapter;

    private Handler mHandler;

    private List<com.thu.stlgm.bean.Album> mAlbumList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mHandler = new Handler();

        super.onActivityCreated(savedInstanceState);

        getAlbum();


    }




    @Override
    protected void setupView() {
        Album = (GridView) getActivity().findViewById(R.id.Album);
        mLoadingBar = (ProgressBar) getActivity().findViewById(R.id.LoadingBar);
    }

    @Override
    protected void setupEvent() {
        Album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                if (mAlbumList!=null){
                    mBundle.putParcelableArrayList(Album_PhotoFragment.PHOTO,new ArrayList<Photo>(
                            mAlbumAdapter.getItem(position).getPhotos()));
                }

                Album_PhotoFragment mAlbum_PhotoFragment = Album_PhotoFragment.getInstance(mBundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.add(R.id.FBContent,mAlbum_PhotoFragment);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    protected void setupAdapter() {
        mAlbumAdapter = new AlbumAdapter(getActivity());
        Album.setAdapter(mAlbumAdapter);

    }

    private void getAlbum(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                mAlbumList = FacebookAlbumUtils.getAllGroupAlbum();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAlbumAdapter.Refresh(mAlbumList);
                        mLoadingBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }


    public void Finish(){
        if (getActivity()!=null){
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }
}
