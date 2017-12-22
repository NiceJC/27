package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.NearbyPhotoBean;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.NearByUserService;
import com.lede.second_23.ui.base.BaseActivity;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.LATITUDE;
import static com.lede.second_23.global.GlobalConstants.LONGITUDE;
import static com.lede.second_23.global.GlobalConstants.SEXTYPE;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.USER_SEX;

/**
 * Created by ld on 17/12/20.
 */

public class NearByActivity extends BaseActivity implements AMapLocationListener {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Bind(R.id.recyclerView_girl_1)
    RecyclerView girlRecyclerView1;
    @Bind(R.id.recyclerView_girl_2)
    RecyclerView girlRecyclerView2;



    private Gson mGson;

    private CommonAdapter mAdapter;
    private CommonAdapter girlAdapter1;
    private CommonAdapter girlAdapter2;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private double myLatitude;//纬度
    private double myLongitude;//经度
    public AMapLocationClientOption mLocationOption = null;


    public MultiTransformation transformation;
    private RequestManager mRequestManager;
    private ArrayList<NearbyPhotoBean.DataBean.UserPhotoBean> nearbyUserPhotoList = new ArrayList<>();

    private List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> girlList1 =new ArrayList<>();
    private List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> girlList2 =new ArrayList<>();

    String address;
    String matchedSex="女";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivty_nearby);
        ButterKnife.bind(this);

        mRequestManager = Glide.with(this);
        mGson = new Gson();
        address= (String) SPUtils.get(this, ADDRESS, "");
        String sex= (String) SPUtils.get(this,USER_SEX,"男");

        myLatitude=Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LATITUDE,""));
        myLongitude=Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LONGITUDE,""));


//



        if(sex.equals("女")){
            matchedSex="男";
        }
        initView();
        initEvent();
        doRequest();
        mRefreshLayout.isRefreshing();
    }



    @OnClick({  R.id.back,R.id.girl_more })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.girl_more:
                //改为获取异性同城用户
                Intent intent=new Intent(this, SameCityActivity.class);
                intent.putExtra(ADDRESS,address);
                intent.putExtra(SEXTYPE,matchedSex);
                startActivity(intent);
                break;

            default:
                break;



        }

    }



    private void initView() {
        //图片加圆角  注：不能在xml直接使用 centerCrop
        transformation = new MultiTransformation(
                new CenterCrop(this),
                new RoundedCornersTransformation(this, 10, 0, RoundedCornersTransformation.CornerType.ALL)


        );


        GridLayoutManager manager1=new   GridLayoutManager(this, 3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mRecyclerView.setLayoutManager(manager1);
        mAdapter = new CommonAdapter<NearbyPhotoBean.DataBean.UserPhotoBean>(this, R.layout.fragment_nearby_item, nearbyUserPhotoList) {
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
                        Intent intent = new Intent(NearByActivity.this, ConcernActivity_2.class);
                        intent.putExtra(USERID, userPhotoBean.getUserId());
                        startActivity(intent);
                    }
                });


            }
        };
        mRecyclerView.setAdapter(mAdapter);


        girlRecyclerView1.setLayoutManager(new GridLayoutManager(this,4));
        girlAdapter1 = new CommonAdapter<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>(this, R.layout.near_user_item, girlList1) {
            @Override
            protected void convert(ViewHolder holder, final SameCityUserBean.DataBean.UserInfoList.UserInfoListBean userInfoListBean, int position) {
                ImageView imageView=holder.getView(R.id.user_icon);
                TextView textView=holder.getView(R.id.user_nickName);
                ImageView vipTag=holder.getView(R.id.vip_tag);
                mRequestManager.load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(NearByActivity.this))
                        .into(imageView);
                textView.setText(userInfoListBean.getNickName());
                if(userInfoListBean.getTrueName()!=null&&userInfoListBean.getTrueName().equals("1")){
                    vipTag.setVisibility(View.VISIBLE);
                }else{
                    vipTag.setVisibility(View.GONE);
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NearByActivity.this, UserInfoActivty.class);
                        intent.putExtra(USERID, userInfoListBean.getUserId());
                        startActivity(intent);
                    }
                });

            }
        };
        girlRecyclerView1.setAdapter(girlAdapter1);


        girlRecyclerView2.setLayoutManager(new GridLayoutManager(this,3));
        girlAdapter2 = new CommonAdapter<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>(this, R.layout.near_user_item, girlList2) {
            @Override
            protected void convert(ViewHolder holder, final SameCityUserBean.DataBean.UserInfoList.UserInfoListBean userInfoListBean, int position) {
                ImageView imageView=holder.getView(R.id.user_icon);
                TextView textView=holder.getView(R.id.user_nickName);
                ImageView vipTag=holder.getView(R.id.vip_tag);
                mRequestManager.load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(NearByActivity.this))
                        .into(imageView);
                textView.setText(userInfoListBean.getNickName());
                if(userInfoListBean.getTrueName()!=null&&userInfoListBean.getTrueName().equals("1")){
                    vipTag.setVisibility(View.VISIBLE);
                }else{
                    vipTag.setVisibility(View.GONE);
                }

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NearByActivity.this, UserInfoActivty.class);
                        intent.putExtra(USERID, userInfoListBean.getUserId());
                        startActivity(intent);
                    }
                });

            }
        };
        girlRecyclerView2.setAdapter(girlAdapter2);


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

        NearByUserService nearByUserService= new NearByUserService(this);
        nearByUserService.requestNearbyPhoto(address, myLatitude, myLongitude, new MyCallBack() {
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


        nearByUserService.requestCitySingleSex(address,matchedSex, 1, 7, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list= (List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>) o;


                girlList1.clear();
                girlList1.add(list.get(0));
                girlList1.add(list.get(1));
                girlList1.add(list.get(2));
                girlList1.add(list.get(3));


                girlAdapter1.notifyDataSetChanged();

                girlList2.clear();
                girlList2.add(list.get(4));
                girlList2.add(list.get(5));
                girlList2.add(list.get(6));
                girlAdapter2.notifyDataSetChanged();

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });



    }





    //定位
    public void getLocation() {


        mLocationClient = new AMapLocationClient(this.getApplicationContext());
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
                SPUtils.put(this, LATITUDE, myLatitude);
                myLongitude = aMapLocation.getLongitude();//获取经度
                SPUtils.put(this, LONGITUDE, myLongitude);

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
