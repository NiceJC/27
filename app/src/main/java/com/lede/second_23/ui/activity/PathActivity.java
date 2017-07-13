package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.rewrite_map.MyWalRouteOverlay;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PathActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.tv_path_activity_distance)
    TextView tv_distance;
//    private AMapNavi mAMapNavi;
    private double latitude;
    private double longitude;
    private AMap aMap;
    private RouteSearch routeSearch;
    private final int ROUTE_TYPE_WALK = 3;
    private LatLonPoint mStartPoint ;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint ;//终点，116.481288,39.995576
    private DriveRouteResult mDriveRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private RequestQueue requestQueue;
    private MyWalRouteOverlay walkRouteOverlay;
    private Bitmap startBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        ButterKnife.bind(this);
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
//        loadBitmap((String)SPUtils.get(this,GlobalConstants.HEAD_IMG,""));
        Intent intent=getIntent();
        latitude=Double.parseDouble(intent.getStringExtra("lat"));
        longitude=Double.parseDouble(intent.getStringExtra("lon"));
        tv_distance.setText(intent.getStringExtra("dis"));
        mEndPoint=new LatLonPoint(latitude,longitude);
        mStartPoint=new LatLonPoint(Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LATITUDE,"")),Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LONGITUDE,"")));
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
//        aMap.setCustomMapStylePath(  "file:///android_asset/mymapblack.data");
//        aMap.setMapCustomEnable(true);
        initMap();


    }

    @OnClick({R.id.iv_path_activity_back})
    public  void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_path_activity_back:
                finish();
                break;
        }
    }

    private void initMap() {
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 卫星地图模式

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        /**
         * 路线
         */
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.DrivingDefault);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
//        if (mStartPoint == null) {
//            ToastUtil.show(mContext, "定位中，稍后再试...");
//            return;
//        }
//        if (mEndPoint == null) {
//            ToastUtil.show(mContext, "终点未设置");
//        }
//        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
//        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);


                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);

                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
//                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
//                    mBottomLayout.setVisibility(View.VISIBLE);
//                    int dis = (int) drivePath.getDistance();
//                    int dur = (int) drivePath.getDuration();
//                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
//                    mRotueTimeDes.setText(des);
//                    mRouteDetailDes.setVisibility(View.VISIBLE);
//                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
//                    mRouteDetailDes.setText("打车约"+taxiCost+"元");
//                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext,
//                                    DriveRouteDetailActivity.class);
//                            intent.putExtra("drive_path", drivePath);
//                            intent.putExtra("drive_result",
//                                    mDriveRouteResult);
//                            startActivity(intent);
//                        }
//                    });
                } else if (result != null && result.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    walkRouteOverlay = new MyWalRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
//                    walkRouteOverlay.setStartBitmap(startBitmap);
                    walkRouteOverlay.setNodeIconVisibility(false);
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
//                    mBottomLayout.setVisibility(View.VISIBLE);
//                    int dis = (int) walkPath.getDistance();
//                    int dur = (int) walkPath.getDuration();
//                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
//                    mRotueTimeDes.setText(des);
//                    mRouteDetailDes.setVisibility(View.GONE);
//                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext,
//                                    WalkRouteDetailActivity.class);
//                            intent.putExtra("walk_path", walkPath);
//                            intent.putExtra("walk_result",
//                                    mWalkRouteResult);
//                            startActivity(intent);
//                        }
//                    });
                } else if (result != null && result.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }


    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    public  void loadBitmap(final String url){
        Bitmap bitmap;
        Request<Bitmap> bitmapRequest = NoHttp.createImageRequest(url, RequestMethod.POST);
        requestQueue.add(100, bitmapRequest, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                if (url.equals(SPUtils.get(PathActivity.this, GlobalConstants.HEAD_IMG,""))) {
//                    walkRouteOverlay.setStartBitmap(response.get());
                    startBitmap=response.get();
                }else {
//                    walkRouteOverlay.setStartBitmap(response.get());
                }

            }

            @Override
            public void onFailed(int what, Response<Bitmap> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
