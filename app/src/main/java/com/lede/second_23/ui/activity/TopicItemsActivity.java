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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.TopicItemsBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.ISMANAGER;
import static com.lede.second_23.global.GlobalConstants.TOPICID;
import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;

/**
 *
 * 主题item的点击展开页
 * 展示某个主题下的所有版块
 * 可选城市，根据城市再细查
 * Created by ld on 18/1/16.
 */

public class TopicItemsActivity extends BaseActivity {

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.address)
    TextView address;



    private Gson mGson;

    private Activity context;
    private CommonAdapter topicItemAdapter;

    private static final int PAGE_SIZE = 100;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;
    private String currentCity;


    private MultiTransformation transformation;
    private FindMoreService findMoreService;
    private List<TopicItemsBean.DataBean.SimpleBean.ListBean> topicItemsData=new ArrayList<>();
    private RequestManager requestManager;
    private boolean isCitySelected=false; //是否选择了城市
    private static final int REQUEST_CODE_PICK_CITY = 0;
    private long topicID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);

        context = this;
        ButterKnife.bind(context);
        mGson = new Gson();
        requestManager= Glide.with(context);

        topicID=getIntent().getLongExtra(TOPICID,0);


        initView();
        initEvent();
        toRefresh();

    }


    @OnClick({R.id.back,R.id.address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.address:
                //启动
                startActivityForResult(new Intent(context, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);

                break;

            default:
                break;
        }
    }


    private void initView() {

        findMoreService=new FindMoreService(context);
        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(3) , 0, RoundedCornersTransformation.CornerType.ALL)
        );

        topicItemAdapter=new CommonAdapter<TopicItemsBean.DataBean.SimpleBean.ListBean>(context,R.layout.item_topic_item_2,topicItemsData) {
            @Override
            protected void convert(ViewHolder holder, final TopicItemsBean.DataBean.SimpleBean.ListBean listBean, int position) {
                ImageView imageView=holder.getView(R.id.image);
                TextView textView=holder.getView(R.id.text);
                ImageView edit=holder.getView(R.id.edit);
                if(ISMANAGER){
                    edit.setVisibility(View.VISIBLE);
                }else{
                    edit.setVisibility(View.GONE);
                }
                requestManager.load(listBean.getUrlOne()).bitmapTransform(transformation).into(imageView);
                textView.setText(listBean.getDetailsName());


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, TopicItemEditActivity.class);
                        intent.putExtra(TOPICITEMID, listBean.getUuidBySub());
                        startActivity(intent);
                    }
                });


            }
        };
        topicItemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(context, TopicItemDetailActivity.class);
                intent.putExtra(TOPICID,topicItemsData.get(position).getUuid());
                intent.putExtra(TOPICITEMID,topicItemsData.get(position).getUuidBySub());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {


                return false;
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
        mRecyclerView.setAdapter(topicItemAdapter);

    }


    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                currentCity= data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY)+"市";

                address.setText(currentCity);
                isCitySelected=true;
                toRefresh();
            }
        }
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


        MyCallBack myCallBack=new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                List<TopicItemsBean.DataBean.SimpleBean.ListBean> list = (List<TopicItemsBean.DataBean.SimpleBean.ListBean>) o;

                if (list.size() != 0) {

                    if (isRefresh) {
                        topicItemsData.clear();

                        currentPage = 1;
                        isHasNextPage = true;


                    } else {
                        currentPage++;
                    }
                    topicItemsData.addAll(list);

                    topicItemAdapter.notifyDataSetChanged();
                } else {

                    if(isRefresh){
                        topicItemsData.clear();
                        topicItemAdapter.notifyDataSetChanged();

                    }

                    isHasNextPage = false;
                }
            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        };

        if(isCitySelected){
            findMoreService.requestTopicItemsByTopicAndCity(topicID,currentCity, page, PAGE_SIZE,myCallBack);
        }else{
            findMoreService.requestTopicItemsByTopic(topicID, page, PAGE_SIZE, myCallBack);

        }




    }


}
