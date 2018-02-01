package com.lede.second_23.ui.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.TopicItemsBean;
import com.lede.second_23.bean.TopicsBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.ui.activity.SearchingActivity;
import com.lede.second_23.ui.activity.TopicCreateActivity;
import com.lede.second_23.ui.activity.TopicItemCreateActivity;
import com.lede.second_23.ui.activity.TopicItemDetailActivity;
import com.lede.second_23.ui.activity.TopicItemsActivity;
import com.lede.second_23.ui.activity.TopicItemsRankActivity;
import com.lede.second_23.ui.activity.TopicsActivity;
import com.lede.second_23.ui.activity.TopicsRankActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.T;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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

import static com.lede.second_23.global.GlobalConstants.CURRENT_CITY;
import static com.lede.second_23.global.GlobalConstants.CURRENT_PROVINCE;
import static com.lede.second_23.global.GlobalConstants.ISMANAGER;
import static com.lede.second_23.global.GlobalConstants.LATITUDE;
import static com.lede.second_23.global.GlobalConstants.LONGITUDE;
import static com.lede.second_23.global.GlobalConstants.TOPICID;
import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;

/**
 *
 * Created by ld on 18/1/10.
 */

public class SearchMoreFragment extends Fragment implements AMapLocationListener {

    @Bind(R.id.recyclerView_1)
    RecyclerView recyclerView1;
    @Bind(R.id.recyclerView_2)
    RecyclerView recyclerView2;
    @Bind(R.id.locate_city_text)
    TextView locationText;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.create_more)
    ImageView createMore;
    @Bind(R.id.create_more_item)
    ImageView createMoreItem;
    @Bind(R.id.edit)
    ImageView edit;
    @Bind(R.id.edit_topic)
    ImageView editTopic;
    @Bind(R.id.position_toast)
    ImageView positionToast;


    private FindMoreService findMoreService;
    private Activity context;
    private CommonAdapter topicAdapter;
    private CommonAdapter topicItemAdapter;
    private int currentPage = 1;
    private int pageSize = 6;
    private boolean isHasNextPage = false;
    private RequestManager requestManager;
    private List<TopicsBean.DataBean.SimpleBean.ListBean> topicData=new ArrayList<>();
    private List<TopicItemsBean.DataBean.SimpleBean.ListBean> topicItemsData=new ArrayList<>();
    public MultiTransformation transformation;
    public MultiTransformation transformationAll;
    private boolean isRefresh;
    private String currentCity;
    private String currentProvence;
    private String location;
    private boolean isProvince=false;
    private ObjectAnimator animator;

    private AMapLocationClient mlocationClient;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    public AMapLocationClientOption mLocationOption = null;

    private double myLatitude;//纬度
    private double myLongitude;//经度
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_more, container, false);

        ButterKnife.bind(this,view);

        currentCity= (String) SPUtils.get(getActivity(),CURRENT_CITY,"杭州市");
        currentProvence= (String) SPUtils.get(getActivity(),CURRENT_PROVINCE,"浙江省");
        location=currentCity;



        initView();
        initEvent();
        toRefresh();
        return view;
    }


    @OnClick({R.id.to_search,R.id.add_more,R.id.locate_city_click,R.id.create_more,R.id.create_more_item,R.id.edit,R.id.edit_topic})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.to_search:
                intent=new Intent(getActivity(), SearchingActivity.class);
                startActivity(intent);
                break;
            case R.id.add_more:
                intent=new Intent(getActivity(), TopicsActivity.class);
                startActivity(intent);
                break;
            case R.id.locate_city_click:

                getLocation();

                break;
            case R.id.create_more:
                 intent=new Intent(getActivity(), TopicCreateActivity.class);
                startActivity(intent);
                break;
            case R.id.create_more_item:
                intent=new Intent(getActivity(), TopicItemCreateActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_topic:
                intent=new Intent(getActivity(), TopicsRankActivity.class);
                startActivity(intent);
                break;

            case R.id.edit:
                intent=new Intent(getActivity(), TopicItemsRankActivity.class);
                intent.putExtra("city",currentCity);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initView() {



        if(ISMANAGER){
            edit.setVisibility(View.VISIBLE);
            editTopic.setVisibility(View.VISIBLE);
            createMore.setVisibility(View.VISIBLE);
            createMoreItem.setVisibility(View.VISIBLE);
        }else{
            edit.setVisibility(View.GONE);
            editTopic.setVisibility(View.GONE);
            createMore.setVisibility(View.GONE);
            createMoreItem.setVisibility(View.GONE);
        }

        locationText.setText(currentCity);
        context=getActivity();
        findMoreService=new FindMoreService(context);
        requestManager = Glide.with(getContext());
        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(3), 0, RoundedCornersTransformation.CornerType.TOP)
        );
        transformationAll = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context,  UiUtils.dip2px(3), 0, RoundedCornersTransformation.CornerType.ALL)
        );


        topicAdapter=new CommonAdapter<TopicsBean.DataBean.SimpleBean.ListBean>(context,R.layout.item_topic_main,topicData) {
            @Override
            protected void convert(ViewHolder holder, TopicsBean.DataBean.SimpleBean.ListBean listBean, int position) {

                ImageView imageView=holder.getView(R.id.image);
                TextView textView=holder.getView(R.id.text);
                ImageView edit=holder.getView(R.id.edit);
                requestManager.load(listBean.getUrlOne()).bitmapTransform(transformation).into(imageView);
                textView.setText(listBean.getPhotoName());

            }
        };
        topicAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(context, TopicItemsActivity.class);
                intent.putExtra(TOPICID,topicData.get(position).getUuid());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setAdapter(topicAdapter);

        //同城版块
        topicItemAdapter=new CommonAdapter<TopicItemsBean.DataBean.SimpleBean.ListBean>(context,R.layout.item_topic_content,topicItemsData) {
            @Override
            protected void convert(ViewHolder holder, TopicItemsBean.DataBean.SimpleBean.ListBean listBean, int position) {
                ImageView imageView=holder.getView(R.id.image);
                TextView textView=holder.getView(R.id.text);
                requestManager.load(listBean.getUrlOne()).bitmapTransform(transformationAll).into(imageView);
                textView.setText(listBean.getDetailsName());

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

        recyclerView2.setLayoutManager(new GridLayoutManager(context,2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView2.setAdapter(topicItemAdapter);





    }

    private void initEvent() {


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                toRefresh();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                toLoadMore();
            }
        });

    }

    //定位
    public void getLocation() {


        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);//一次定位
        mLocationOption.setOnceLocationLatest(true);//返回最近3s内精度最高的一次定位结果

        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                myLatitude = aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getProvince();
                SPUtils.put(getActivity(), LATITUDE, myLatitude);
                myLongitude = aMapLocation.getLongitude();//获取经度
                SPUtils.put(getActivity(), LONGITUDE, myLongitude);

                currentCity=aMapLocation.getCity();
                currentProvence=aMapLocation.getProvince();
                location=currentCity;
                isProvince=false;

                toRefresh();

                SPUtils.put(getActivity(),CURRENT_PROVINCE,aMapLocation.getProvince());
                SPUtils.put(getActivity(),CURRENT_CITY,aMapLocation.getCity());


                mLocationClient.stopLocation();

                positionToast.setVisibility(View.VISIBLE);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        positionToast.setVisibility(View.GONE);

                    }
                },2000);


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                T.showShort(MyApplication.getInstance(), "定位失败,请检查是否开启应用定位权限");

            }
        }
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
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
        }
    }

    private void doRequest(int pageNum) {

        if(isRefresh){
            FindMoreService findMoreService2=new FindMoreService(context);
            findMoreService2.requestAllTopics(1, 6, new MyCallBack() {
                @Override
                public void onSuccess(Object o) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                    List<TopicsBean.DataBean.SimpleBean.ListBean> topics= (List<TopicsBean.DataBean.SimpleBean.ListBean>) o;

                    topicData.clear();
                    topicData.addAll(topics);
                    topicAdapter.notifyDataSetChanged();


                }

                @Override
                public void onFail(String mistakeInfo) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadmore();
                }
            });

        }


        findMoreService.requestTopicItemsByCity(location, pageNum, pageSize, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
                List<TopicItemsBean.DataBean.SimpleBean.ListBean> list = (List<TopicItemsBean.DataBean.SimpleBean.ListBean>) o;



                if (isRefresh) {
                    topicItemsData.clear();


                    currentPage = 1;
                    isHasNextPage = true;

                    if(list.size()==0&&!isProvince){
                        location=currentProvence;
                        isProvince=true;
                        toRefresh();

                    }

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
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
            }
        });



    }




}
