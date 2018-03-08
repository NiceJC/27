package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lede.second_23.R;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.ui.activity.AllIssueActivity;
import com.lede.second_23.ui.activity.PushForumActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.ISMANAGER;

/**
 * 点击按钮  切换显示排列方式，也就是简单切换下layoutManager
 * <p>
 * <p>
 * <p>
 * Created by ld on 17/10/19.
 */

public class MainFragmentNearBy extends Fragment {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;


    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.concern_tab)
    TextView concernTab;
    @Bind(R.id.recommend_tab)
    TextView recommendTab;

    @Bind(R.id.find_change)
    ImageView findChange;

    @Bind(R.id.manager)
    TextView manager;


    private boolean isRefresh = true;
    private boolean isHasNextPage = true;

    private int currentPage = 1;
    private Context context;
    private ArrayList<ImageView> imgViews;
    private List<AllForumBean.DataBean.SimpleBean.ListBean.AllRecordsBean> list;
    private LayoutInflater layoutInflater;
    private int width;

    private boolean isGridLayout = true; //默认gridLayout
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;
    private ForumFragment_Concern forumFragment_concern;
    private ForumFragment_Recommend forumFragment_recommend;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_nearby, container, false);


        ButterKnife.bind(this, view);

        context = getContext();

        initView();
        initEvent();

//        toRefresh();
        return view;
    }


    @OnClick({R.id.find_send, R.id.find_change, R.id.manager, R.id.concern_tab, R.id.recommend_tab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_send:
                Intent intent = new Intent(context, AllIssueActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            case R.id.find_change:

                forumFragment_recommend.changeLayout(findChange);

                break;
            case R.id.manager:
                Intent intent2 = new Intent(context, PushForumActivity.class);
                startActivity(intent2);
                break;
            case R.id.concern_tab:
                currentPage = 1;
                concernTab.setSelected(true);
                recommendTab.setSelected(false);
                findChange.setVisibility(View.GONE);
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.recommend_tab:
                currentPage = 0;
                concernTab.setSelected(false);
                recommendTab.setSelected(true);
                findChange.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(0, true);
                break;


            default:
                break;


        }
    }

//    public void changeLayout(){
//
//        if(isGridLayout){
//            mRecyclerView.setLayoutManager(linearLayoutManager);
//            mRecyclerView.setAdapter(mAdapter2);
//            findChange.setSelected(true);
//            isGridLayout=false;
//        }else{
//            mRecyclerView.setLayoutManager(gridLayoutManager);
//            mRecyclerView.setAdapter(mAdapter1);
//
//            findChange.setSelected(false);
//            isGridLayout=true;
//        }
//
//
//
//    }


    private void initView() {

        if (ISMANAGER) {
            manager.setVisibility(View.VISIBLE);
        } else {
            manager.setVisibility(View.GONE);

        }


        fragmentList = new ArrayList<>();


        if (forumFragment_recommend == null) {
            forumFragment_recommend = new ForumFragment_Recommend();
        }
        fragmentList.add(forumFragment_recommend);

        if (forumFragment_concern == null) {
            forumFragment_concern = new ForumFragment_Concern();
        }
        fragmentList.add(forumFragment_concern);


        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentPage = 0;
                    concernTab.setSelected(false);
                    recommendTab.setSelected(true);
                    findChange.setVisibility(View.VISIBLE);
                }

                if (position == 1) {
                    currentPage = 1;
                    concernTab.setSelected(true);
                    recommendTab.setSelected(false);
                    findChange.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        currentPage = 0;
        viewPager.setCurrentItem(currentPage, false);
        recommendTab.setSelected(true);
        findChange.setVisibility(View.VISIBLE);


    }


    public void initEvent() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                if (forumFragment_concern.isResumed()) {
                    forumFragment_concern.toRefresh(mRefreshLayout);
                }
                if (forumFragment_recommend.isResumed()) {
                    forumFragment_recommend.toRefresh(mRefreshLayout);
                }


            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (forumFragment_concern.isResumed()) {
                    forumFragment_concern.toLoadMore(mRefreshLayout);
                }
                if (forumFragment_recommend.isResumed()) {
                    forumFragment_recommend.toLoadMore(mRefreshLayout);
                }
            }
        });


        forumFragment_concern.setRefreshLayout(mRefreshLayout);
        forumFragment_recommend.setRefreshLayout(mRefreshLayout);


    }

    public void toRefresh() {


        forumFragment_concern.toRefresh(mRefreshLayout);

        forumFragment_recommend.toRefresh(mRefreshLayout);

    }

    public void changeToConcern(){
        currentPage = 1;
        concernTab.setSelected(true);
        recommendTab.setSelected(false);
        findChange.setVisibility(View.GONE);
        viewPager.setCurrentItem(1, true);
    }


}

