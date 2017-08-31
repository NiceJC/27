package com.lede.second_23.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by ld on 17/8/30.
 */

public class MyHackPagerAdapter extends PagerAdapter {
    List<String> mDatas;
    private AppCompatActivity activity;

    public MyHackPagerAdapter(AppCompatActivity activity,List<String> mDatas) {
        this.activity=activity;
        this.mDatas = mDatas;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = mDatas.get(position);
        PhotoView photoView = new PhotoView(activity);
        Glide.with(activity)
                .load(url)
                .into(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }
}
