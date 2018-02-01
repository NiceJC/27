package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lljjcoder.citypickerview.widget.CityPicker;
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

import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;

/**
 * 管理版本排序专用
 * 罗列每个城的所有版块 并进行排序
 * 长按删除
 * Created by ld on 18/1/25.
 */

public class TopicItemsRankActivity extends BaseActivity {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.save)
    TextView save;


    private Gson mGson;

    private Activity context;
    private CommonAdapter topicItemAdapter;

    private static final int PAGE_SIZE = 300;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;
    private String currentCity;

    private MultiTransformation transformation;
    private FindMoreService findMoreService;
    private List<TopicItemsBean.DataBean.SimpleBean.ListBean> topicItemsData = new ArrayList<>();
    private List<TopicItemsBean.DataBean.SimpleBean.ListBean> topicItemRankData = new ArrayList<>();
    private List<TopicItemsBean.DataBean.SimpleBean.ListBean> topicItemRestData = new ArrayList<>();
    private RequestManager requestManager;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_items_rank);


        context = this;
        ButterKnife.bind(context);
        mGson = new Gson();
        requestManager = Glide.with(context);
        currentCity = getIntent().getStringExtra("city");

        mRefreshLayout.setEnableLoadmore(false);

        initView();
        initEvent();
        toRefresh();

    }


    @OnClick({R.id.back, R.id.address, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.address:
                showCityDialog();
                break;

            case R.id.save:
                if (isEditing) {
                    //完成排序提交修改
                    isEditing = false;
                    save.setText("排序");
                    topicItemRankData.addAll(topicItemRestData);
                    saveRank();
                } else {
                    //开始排序
                    isEditing = true;
                    mRefreshLayout.setEnableRefresh(false);
                    topicItemRankData.clear();
                    topicItemRestData.clear();
                    topicItemRestData.addAll(topicItemsData);
                    save.setText("完成");
                }

                break;

            default:
                break;
        }
    }


    private void initView() {

        findMoreService = new FindMoreService(context);
        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(5), 0, RoundedCornersTransformation.CornerType.TOP)
        );


        topicItemAdapter = new CommonAdapter<TopicItemsBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_topic_item_rank, topicItemsData) {
            @Override
            protected void convert(ViewHolder holder, final TopicItemsBean.DataBean.SimpleBean.ListBean listBean, final int position) {
                ImageView imageView = holder.getView(R.id.image);
                TextView textView = holder.getView(R.id.text);
                final TextView rank = holder.getView(R.id.rank);
                ImageView edit = holder.getView(R.id.edit);
                requestManager.load(listBean.getUrlOne()).bitmapTransform(transformation).into(imageView);
                textView.setText(listBean.getDetailsName());

                if(listBean.getStatus()!=0){
                    rank.setText(listBean.getStatus()+"");

                }else{
                    rank.setText("");
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isEditing) {

                            if (topicItemRankData.contains(listBean)&&topicItemRankData.indexOf(listBean)==topicItemRankData.size()-1) {
                                topicItemRankData.remove(listBean);
                                topicItemRestData.add(listBean);

                                listBean.setStatus(0);

                                rank.setText("");

                            } else {
                                topicItemRankData.add(listBean);
                                topicItemRestData.remove(listBean);
                                int id=topicItemRankData.indexOf(listBean) + 1;
                                listBean.setStatus(id);
                                rank.setText(id+"");

                            }


                        }

                    }
                });

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
//                Intent intent=new Intent(context, TopicItemDetailActivity.class);
//                intent.putExtra(TOPICID,topicItemsData.get(position).getUuid());
//                intent.putExtra(TOPICITEMID,topicItemsData.get(position).getUuidBySub());
//                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

                showDeleteDialog(topicItemsData.get(position).getDetailsName(), topicItemsData.get(position).getId());


                return false;
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mRecyclerView.setAdapter(topicItemAdapter);


    }

    public void saveRank() {

        FindMoreService findMoreService2 = new FindMoreService(context);

        for (TopicItemsBean.DataBean.SimpleBean.ListBean topic : topicItemRankData
                ) {

            int order = topicItemRankData.indexOf(topic) + 1;
            findMoreService2.saveTopicItemRank(topic.getUuidBySub(), order, new MyCallBack() {
                @Override
                public void onSuccess(Object o) {
                    mRefreshLayout.setEnableRefresh(true);
                    toRefresh();
                }

                @Override
                public void onFail(String mistakeInfo) {
                    mRefreshLayout.setEnableRefresh(true);

                }
            });

        }


    }

    public void showDeleteDialog(String topicItemName, final int topicId) {

        final AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("删除板块");
        localBuilder.setIcon(R.mipmap.logo);
        localBuilder.setMessage("确认删除 " + topicItemName + " ?");
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                /**
                 * 确定操作
                 * */
                deleteTopic(topicId);
                paramAnonymousDialogInterface.dismiss();

            }
        });
        localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                paramAnonymousDialogInterface.dismiss();

            }
        });

        /***
         * 设置点击返回键不会消失
         * */
        localBuilder.setCancelable(false).create();

        localBuilder.show();
    }

    public void deleteTopic(int topicId) {

        findMoreService.deleteTopicItem(topicId, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                toRefresh();
            }

            @Override
            public void onFail(String mistakeInfo) {
                Toast.makeText(context, "请求出错", Toast.LENGTH_SHORT).show();


            }
        });


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

    /**
     * 城市选择Dialog
     */
    private void showCityDialog() {
        CityPicker cityPicker = new CityPicker.Builder(context)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("浙江省")
                .city("杭州市")
                .onlyShowProvinceAndCity(true)
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
//                String district = citySelected[2];
                //邮编
//                String code = citySelected[3];
                currentCity = city;
                address.setText(currentCity);

                toRefresh();
            }

            @Override
            public void onCancel() {
//                Toast.makeText(EditInformationActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doRequest(int page) {


        findMoreService.requestTopicItemsByCity(currentCity, page, PAGE_SIZE, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                List<TopicItemsBean.DataBean.SimpleBean.ListBean> list = (List<TopicItemsBean.DataBean.SimpleBean.ListBean>) o;

                if(list.size()!=0){

                    for (TopicItemsBean.DataBean.SimpleBean.ListBean listBean:list
                         ) {
                        listBean.setStatus(0);
                    }

                }


                if (isRefresh) {
                    topicItemsData.clear();
                    topicItemRankData.clear();

                    currentPage = 1;
                    isHasNextPage = true;
                } else {
                    if (list.size() == 0) {
                        isHasNextPage = false;
                    } else {
                        isHasNextPage = true;
                        currentPage++;
                    }

                }

                topicItemsData.addAll(list);
                topicItemAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });


    }


}
