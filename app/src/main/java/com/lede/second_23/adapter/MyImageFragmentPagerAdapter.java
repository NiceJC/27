package com.lede.second_23.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lede.second_23.ui.fragment.ImageFragment_2;

import java.util.List;

/**
 * Created by ld on 17/8/30.
 */

public class MyImageFragmentPagerAdapter extends FragmentPagerAdapter {
    List<String> mDatas;
    public MyImageFragmentPagerAdapter(FragmentManager fm, List data) {
        super(fm);
        mDatas = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position);
        Fragment fragment = ImageFragment_2.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
