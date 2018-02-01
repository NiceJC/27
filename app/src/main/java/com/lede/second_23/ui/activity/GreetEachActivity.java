package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;
import com.lede.second_23.bean.BilateralBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.SocialService;
import com.lede.second_23.ui.base.BaseActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * 相互打过招呼的人物列表
 * 相当于好友列表
 * Created by ld on 18/1/17.
 */

public class GreetEachActivity extends BaseActivity {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Activity context;
    private CommonAdapter mAdapter;

    private SocialService socialService;
    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean isRefresh = true;
    private boolean isHasNextPage = true;
    private ArrayList<BilateralBean.DataBean.ListBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greet_each);

        context = this;
        ButterKnife.bind(context);

        initView();
        initEvent();
        toRefresh();

    }


    @OnClick(R.id.back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(context,3));

        mAdapter = new CommonAdapter<BilateralBean.DataBean.ListBean>(this, R.layout.fragment_youlike_item, dataList) {
            @Override
            protected void convert(ViewHolder holder, final BilateralBean.DataBean.ListBean userInfoListBean, int position) {


                ImageView userIcon = holder.getView(R.id.user_icon);
                TextView user_nickName = holder.getView(R.id.user_nickName);
                ImageView vipTag = holder.getView(R.id.vip_tag);

                user_nickName.setText(userInfoListBean.getNickName());


                Glide.with(context)
                        .load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(userIcon);

                    vipTag.setVisibility(View.GONE);



                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserInfoActivty.class);
                        intent.putExtra(USERID, userInfoListBean.getUserId());
                        startActivity(intent);
                    }
                });
            }


        };



        mRecyclerView.setAdapter(mAdapter);
        socialService=new SocialService(context);



    }


    public void initEvent() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                toRefresh();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                toLoadMore();
            }
        });
    }

    public void toRefresh() {


        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {

        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        } else {
            mRefreshLayout.finishLoadmore();
        }
    }

    private void doRequest(int page) {

        socialService.getGreetRelation(page, PAGE_SIZE, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();

                ArrayList<BilateralBean.DataBean.ListBean> list = (ArrayList<BilateralBean.DataBean.ListBean>) o;
                if (isRefresh) {
                    dataList.clear();
                    currentPage = 1;
                    isHasNextPage = true;
                } else {
                    if (list.size() == 0) {
                        isHasNextPage = false;
                    } else {
                        currentPage++;
                    }
                }

                dataList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }


}
