package com.lede.second_23.ui.fragment;

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
import com.lede.second_23.bean.NearbyUsersBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.T;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by ld on 17/10/19.
 */

public class MainFragmentNearBy extends Fragment implements AMapLocationListener {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;

    private MultiItemTypeAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_NEARBY_USER = 777;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private double myLatitude;//纬度
    private double myLongitude;//经度
    public AMapLocationClientOption mLocationOption = null;


    public MultiTransformation transformation;
    private RequestManager mRequestManager;
    private ArrayList<Object> nearbyUserList = new ArrayList<>();

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


    private void initView() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        mAdapter = new MultiItemTypeAdapter<>(getActivity(), nearbyUserList);
        mAdapter.addItemViewDelegate(new UserInfoDelegate());
        mAdapter.addItemViewDelegate(new UserPhotoDelegate());
        mRecyclerView.setAdapter(mAdapter);


        //图片加圆角  注：不能在xml直接使用 centerCrop
        transformation = new MultiTransformation(
                new CenterCrop(getContext()),
                new RoundedCornersTransformation(getContext(), 20, 0, RoundedCornersTransformation.CornerType.ALL)



        );
//.bitmapTransform(new CenterCrop(mContext),new RoundedCornersTransformation(mContext, radius, 0))
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
                refreshlayout.finishLoadmore(500);
            }
        });
    }


    private void doRequest() {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_NEARBY_USER:
                        mRefreshLayout.finishRefresh();
                        parsePushUser(response.get());
                        break;
                    default:
                        break;

                }

            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_NEARBY_USER:


                        mRefreshLayout.finishRefresh();
                        break;
                    default:
                        break;

                }
            }
        };

        Request<String> nearbyUsersRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/photoNearHome", RequestMethod.POST);
//
//        String token=(String) SPUtils.get(getActivity(), GlobalConstants.TOKEN, "");
//        String userId=(String) SPUtils.get(getActivity(), GlobalConstants.USERID, "");
//        String sex=(String) SPUtils.get(getActivity(), GlobalConstants.SET_SEX, "All");
//        String r=((int) SPUtils.get(getActivity(), GlobalConstants.SET_DISTANCE, 10)) * 1000 + "";
//        String agemin=(int) (float) SPUtils.get(getActivity(), GlobalConstants.SET_MINAGE, 0.0f) + "";
//        String intageMax=(int) (float) SPUtils.get(getActivity(), GlobalConstants.SET_MAXAGE, 99.0f) + "";
//        double lon=myLongitude;
//        double lat=myLatitude;




        nearbyUsersRequest.add("access_token", (String) SPUtils.get(getActivity(), GlobalConstants.TOKEN, ""));
//        final String userId, String sex, String radius, String ageMin, String ageMax, String lon, String lat
        nearbyUsersRequest.add("userId", (String) SPUtils.get(getActivity(), GlobalConstants.USERID, ""));
//        userRequest.add("userId", "ee59fb2659654db69352fd34f85d642c");
        nearbyUsersRequest.add("sex", (String) SPUtils.get(getActivity(), GlobalConstants.SET_SEX, "All"));
        nearbyUsersRequest.add("radius", ((int) SPUtils.get(getActivity(), GlobalConstants.SET_DISTANCE, 10)) * 1000 + "");
//        Log.i("TAB", "userService: "+(float)SPUtils.get(mContext,GlobalConstants.SET_MINAGE,0.0f));

        nearbyUsersRequest.set("ageMin", (int) (float) SPUtils.get(getActivity(), GlobalConstants.SET_MINAGE, 0.0f) + "");

        nearbyUsersRequest.add("ageMax", (int) (float) SPUtils.get(getActivity(), GlobalConstants.SET_MAXAGE, 99.0f) + "");
        nearbyUsersRequest.add("lon", myLongitude+"");
        nearbyUsersRequest.add("lat", myLatitude+"");

        RequestServer.getInstance().request(REQUEST_NEARBY_USER, nearbyUsersRequest, simpleResponseListener);

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


    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parsePushUser(String json) {

        NearbyUsersBean nearbyUsersBean = mGson.fromJson(json, NearbyUsersBean.class);
        List<NearbyUsersBean.DataBean.UserInfoListBean> userInfoList = nearbyUsersBean.getData().getUserInfoList();
        List<NearbyUsersBean.DataBean.UserPhotoListBean> userPhotoList = nearbyUsersBean.getData().getUserPhotoList();

        nearbyUserList.clear();
        nearbyUserList.addAll(userPhotoList);
        nearbyUserList.addAll(userInfoList);
        mAdapter.notifyDataSetChanged();

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


    public class UserPhotoDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_nearby_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof NearbyUsersBean.DataBean.UserPhotoListBean;
        }

        @Override
        public void convert(ViewHolder holder, Object o, int position) {

            NearbyUsersBean.DataBean.UserPhotoListBean userPhotoListBean = (NearbyUsersBean.DataBean.UserPhotoListBean) o;
            ImageView imageView = holder.getView(R.id.iv_item_test);
            ImageView videoTag = holder.getView(R.id.iv_main_fragment_item_play);

            if (userPhotoListBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
                videoTag.setVisibility(View.GONE);
                mRequestManager
                        .load(userPhotoListBean.getUrlImg())

                        .bitmapTransform(transformation)
                        .into(imageView);
            } else {
                videoTag.setVisibility(View.VISIBLE);
                mRequestManager.load(userPhotoListBean.getUrlFirst())
                        .bitmapTransform(transformation)
                        .into(imageView);
            }
        }

    }

    public class UserInfoDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.fragment_nearby_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof NearbyUsersBean.DataBean.UserInfoListBean;
        }

        @Override
        public void convert(ViewHolder holder, Object o, int position) {

            NearbyUsersBean.DataBean.UserInfoListBean userInfoListBean = (NearbyUsersBean.DataBean.UserInfoListBean) o;
            ImageView imageView = holder.getView(R.id.iv_item_test);
            ImageView videoTag = holder.getView(R.id.iv_main_fragment_item_play);

            videoTag.setVisibility(View.GONE);
            mRequestManager
                    .load(userInfoListBean.getImgUrl())
                    .bitmapTransform(transformation)
                    .into(imageView);
        }


    }


}

