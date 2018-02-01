package com.lede.second_23.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.TopicsBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.TOPICID;

/**
 *
 * 主题的列表展开页
 * 展示所有主题
 * Created by ld on 18/1/16.
 */

public class TopicsActivity extends BaseActivity {

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.tittle)
    TextView tittle;
    private Gson mGson;

    private Activity context;
    private CommonAdapter mAdapter;
    private ArrayList<TopicsBean.DataBean.SimpleBean.ListBean> topicsData = new ArrayList<>();

    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;

    private RequestManager requestManager;
    public MultiTransformation transformation;
    private boolean isEditing=false;
    private FindMoreService findMoreService;
    ItemTouchHelper.Callback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_s);

        context = this;
        ButterKnife.bind(context);
        mGson = new Gson();

        initView();
        initEvent();
        toRefresh();

    }


    @OnClick({R.id.back})
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
        requestManager = Glide.with(context);
        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(3) , 0, RoundedCornersTransformation.CornerType.TOP)
        );

        mRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
        findMoreService=new FindMoreService(context);


        mAdapter = new CommonAdapter<TopicsBean.DataBean.SimpleBean.ListBean>(this, R.layout.item_topic, topicsData) {
            @Override
            protected void convert(ViewHolder holder, final TopicsBean.DataBean.SimpleBean.ListBean listBean, int position) {

                ImageView imageView=holder.getView(R.id.image);
                TextView textView=holder.getView(R.id.text);
                requestManager.load(listBean.getUrlOne()).bitmapTransform(transformation).into(imageView);
                textView.setText(listBean.getPhotoName());


            }

        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                Intent intent=new Intent(context, TopicItemsActivity.class);
                intent.putExtra(TOPICID,topicsData.get(position).getUuid());
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        mRecyclerView.setAdapter(mAdapter);



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


        findMoreService.requestAllTopics(page, 10, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();

                List<TopicsBean.DataBean.SimpleBean.ListBean> topics= (List<TopicsBean.DataBean.SimpleBean.ListBean>) o;


                if (isRefresh) {
                    topicsData.clear();
                    currentPage = 1;
                    isHasNextPage = true;
                } else {
                    if (topics.size() == 0) {
                        isHasNextPage = false;
                    } else {
                        currentPage++;
                    }
                }

                topicsData.addAll(topics);
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
