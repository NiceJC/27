package com.lede.second_23.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.NearbyPhotoBean;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.NearByUserService;
import com.lede.second_23.ui.activity.ConcernActivity_2;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.T;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/10/19.
 */

public class MainFragmentNearBy extends Fragment implements AMapLocationListener {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.women_img_1)
    ImageView womenImg1;
    @Bind(R.id.women_img_2)
    ImageView womenImg2;
    @Bind(R.id.women_img_3)
    ImageView womenImg3;

    @Bind(R.id.women_name_1)
    ImageView womenName1;
    @Bind(R.id.women_name_2)
    ImageView womenName2;
    @Bind(R.id.women_name_3)
    ImageView womenName3;

    @Bind(R.id.all_img_1)
    ImageView allImg1;
    @Bind(R.id.all_img_2)
    ImageView allImg2;
    @Bind(R.id.all_img_3)
    ImageView allImg3;

    @Bind(R.id.all_name_1)
    ImageView allName1;
    @Bind(R.id.all_name_2)
    ImageView allName2;
    @Bind(R.id.all_name_3)
    ImageView allName3;




    private Gson mGson;

    private CommonAdapter mAdapter;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private double myLatitude;//纬度
    private double myLongitude;//经度
    public AMapLocationClientOption mLocationOption = null;


    public MultiTransformation transformation;
    private RequestManager mRequestManager;
    private ArrayList<NearbyPhotoBean.DataBean.UserPhotoBean> nearbyUserPhotoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_nearby, container, false);


        ButterKnife.bind(this, view);

        mRequestManager = Glide.with(getContext());
        mGson = new Gson();
        initView();
        initEvent();
        getLocation();
        mRefreshLayout.isRefreshing();
        return view;
    }


    @OnClick({  R.id.women_img_more,R.id.all_img_more })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.women_img_more:
                break;
            case R.id.all_img_more:
                break;
            default:
                break;



        }

    }



    private void initView() {
        //图片加圆角  注：不能在xml直接使用 centerCrop
        transformation = new MultiTransformation(
                new CenterCrop(getContext()),
                new RoundedCornersTransformation(getContext(), 10, 0, RoundedCornersTransformation.CornerType.ALL)


        );
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new CommonAdapter<NearbyPhotoBean.DataBean.UserPhotoBean>(getActivity(), R.layout.fragment_nearby_item, nearbyUserPhotoList) {

            @Override
            protected void convert(ViewHolder holder, final NearbyPhotoBean.DataBean.UserPhotoBean userPhotoBean, int position) {

                ImageView imageView = holder.getView(R.id.iv_item_test);

                mRequestManager
                        .load(userPhotoBean.getUrlImg())
                        .bitmapTransform(transformation)
                        .into(imageView);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ConcernActivity_2.class);
                        intent.putExtra(USERID, userPhotoBean.getUserId());

                        getActivity().startActivity(intent);
                    }
                });


            }
        };


        mRecyclerView.setAdapter(mAdapter);



    }


    public void initEvent() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getLocation();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(200);
            }
        });
    }


    private void doRequest() {
        NearByUserService nearByUserService= new NearByUserService(getActivity());
        nearByUserService.requestNearbyPhoto((String) (SPUtils.get(getActivity(), ADDRESS, "")), myLatitude, myLongitude, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<NearbyPhotoBean.DataBean.UserPhotoBean> list= (List<NearbyPhotoBean.DataBean.UserPhotoBean>) o;
                nearbyUserPhotoList.clear();
                nearbyUserPhotoList.addAll(list);
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh();

            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();

            }
        });




    }

    public void setGirl(List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list){
        if(list.size()>0){

        }
        if(list.size()>1){

        }
        if(list.size()>2){

        }

    }

    public void setAll(List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list){



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
                SPUtils.put(getActivity(), "" + GlobalConstants.LATITUDE, myLatitude);
                myLongitude = aMapLocation.getLongitude();//获取经度
                SPUtils.put(getActivity(), "" + GlobalConstants.LONGITUDE, myLongitude);

                doRequest();

                mLocationClient.stopLocation();

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                T.showShort(MyApplication.getInstance(), "定位失败,请检查是否开启应用定位权限");
                mRefreshLayout.finishRefresh();
            }
        }
    }




}

