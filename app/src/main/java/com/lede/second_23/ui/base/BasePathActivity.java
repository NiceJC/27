//package com.lede.second_23.ui.base;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.amap.api.navi.AMapNavi;
//import com.amap.api.navi.AMapNaviListener;
//import com.amap.api.navi.model.AMapLaneInfo;
//import com.amap.api.navi.model.AMapNaviCameraInfo;
//import com.amap.api.navi.model.AMapNaviCross;
//import com.amap.api.navi.model.AMapNaviInfo;
//import com.amap.api.navi.model.AMapNaviLocation;
//import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
//import com.amap.api.navi.model.AMapServiceAreaInfo;
//import com.amap.api.navi.model.AimLessModeCongestionInfo;
//import com.amap.api.navi.model.AimLessModeStat;
//import com.amap.api.navi.model.NaviInfo;
//import com.amap.api.navi.model.NaviLatLng;
//import com.autonavi.tbt.TrafficFacilityInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by ld on 17/6/2.
// */
//
//public class BasePathActivity extends Activity implements AMapNaviListener {
//    protected NaviLatLng mEndLatlng = new NaviLatLng(39.925846, 116.432765);
//    protected NaviLatLng mStartLatlng = new NaviLatLng(39.925041, 116.437901);
//    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
//    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
//    public AMapNavi mAMapNavi;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
//        mAMapNavi.addAMapNaviListener(this);
//        sList.add(mStartLatlng);
//        eList.add(mEndLatlng);
//    }
//
//    @Override
//    public void onInitNaviFailure() {
//
//    }
//
//    @Override
//    public void onInitNaviSuccess() {
//        int strategy=0;
//        try {
//            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        ArrayList<NaviLatLng> mynaviLatLngArrayList=new ArrayList<>();
////        NaviLatLng naviLatLng=new NaviLatLng((double) SPUtils.get(this, GlobalConstants.LATITUDE,0.0d),(double)SPUtils.get(this, GlobalConstants.LONGITUDE,0.0d));
////        mynaviLatLngArrayList.add(naviLatLng);
//        mAMapNavi.calculateDriveRoute(sList, eList, null, strategy);
//
//    }
//
//    @Override
//    public void onStartNavi(int i) {
//
//    }
//
//
//    @Override
//    public void onTrafficStatusUpdate() {
//
//    }
//
//    @Override
//    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
//
//    }
//
//    @Override
//    public void onGetNavigationText(int i, String s) {
//
//    }
//
//    @Override
//    public void onEndEmulatorNavi() {
//
//    }
//
//    @Override
//    public void onArriveDestination() {
//
//    }
//
//    @Override
//    public void onCalculateRouteSuccess() {
//
//    }
//
//    @Override
//    public void onCalculateRouteFailure(int i) {
//
//    }
//
//    @Override
//    public void onReCalculateRouteForYaw() {
//
//    }
//
//    @Override
//    public void onReCalculateRouteForTrafficJam() {
//
//    }
//
//    @Override
//    public void onArrivedWayPoint(int i) {
//
//    }
//
//    @Override
//    public void onGpsOpenStatus(boolean b) {
//
//    }
//
//    @Override
//    public void onNaviInfoUpdate(NaviInfo naviInfo) {
//
//    }
//
//    @Override
//    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
//
//    }
//
//    @Override
//    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
//
//    }
//
//    @Override
//    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
//
//    }
//
//    @Override
//    public void showCross(AMapNaviCross aMapNaviCross) {
//
//    }
//
//    @Override
//    public void hideCross() {
//
//    }
//
//    @Override
//    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
//
//    }
//
//    @Override
//    public void hideLaneInfo() {
//
//    }
//
//    @Override
//    public void onCalculateMultipleRoutesSuccess(int[] ints) {
//
//    }
//
//    @Override
//    public void notifyParallelRoad(int i) {
//
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
//
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
//
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
//
//    }
//
//    @Override
//    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
//
//    }
//
//    @Override
//    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
//
//    }
//
//    @Override
//    public void onPlayRing(int i) {
//
//    }
//}
