package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.adapter.MyFragmentPagerAdapter;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.fragment.ForumFragment;
import com.lede.second_23.ui.fragment.MainFragment1;
import com.lede.second_23.ui.fragment.PersonalFragment1;
import com.lede.second_23.utils.MyViewPager;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.StatusBarUtil;
import com.lede.second_23.utils.T;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 管理
 */
public class MainActivity extends FragmentActivity implements AMapLocationListener{

    public MyViewPager vp_main_fg;
    private ArrayList<Fragment> fragmentList;
    public  static MainActivity instance=null;
    private int widthPixels;
    private boolean isScrolling=false;
    private boolean isRight=false;
//    private ChildFragment childFragment;
//    private OldCameraFragment cameraFragment;
//    private IssueFragment issueFragment;
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    private ForumFragment forumFragment;
    private MainFragment1 mainFragment1;
    private PersonalFragment1 personFragment1;

    private AMapLocationClient mlocationClient;
    private double myLatitude;//纬度
    private double myLongitude;//经度

    private ImageView declaration;
    private NearbySearch mNearbySearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.StatusBarLightMode(this);

        instance=this;
        getLocation();
        Log.i("TAC", "onCreate: ");
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#3b5998"));
        UpdateHelper.getInstance().autoUpdate("com.lede.second_23", false, 5000);
        UpdateHelper.getInstance().setDebugMode(true);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAC", "onResume: ");
//        if ((int) SPUtils.get(instance, GlobalConstants.GETREPLY,0)>0) {
//            Toast.makeText(instance, "收到评论", Toast.LENGTH_SHORT).show();
//        }

    }

    private void initView() {
        vp_main_fg = (MyViewPager) findViewById(R.id.vp_main_fg);
        declaration= (ImageView) findViewById(R.id.declaration_main);
        if (((Boolean) SPUtils.get(this, GlobalConstants.DECLARATIONMAIN,true))) {
            declaration.setVisibility(View.VISIBLE);
        }else {
            declaration.setVisibility(View.GONE);
            declaration.setClickable(false);
        }

        declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declaration.setVisibility(View.GONE);
                declaration.setClickable(false);
                SPUtils.put(MainActivity.this, GlobalConstants.DECLARATIONMAIN,false);
            }
        });


        initFragmentViewPager();
    }

    private void initFragmentViewPager() {
        fragmentList = new ArrayList<Fragment>();

//        cameraFragment = new OldCameraFragment();
//        fragmentList.add(cameraFragment);

//        mainFragment=new MainFragment();
//        mainFragment=getFragmentManager().findFragmentById(R.layout.main_fragment_layout)
//        fragmentList.add(mainFragment);
//        line=getFragmentManager().findFragmentById(R.layout.main_fragment_layout).getView().findViewById(R.id.v_mainFragment_line);

//        PersonFragment personFragment=new PersonFragment();
//        fragmentList.add(personFragment);

//        issueFragment=new IssueFragment();
//        fragmentList.add(issueFragment);
        forumFragment = new ForumFragment();
        fragmentList.add(forumFragment);
//        childFragment=new ChildFragment();
//        fragmentList.add(childFragment);
        mainFragment1 = new MainFragment1();
        personFragment1 = new PersonalFragment1();
        fragmentList.add(mainFragment1);
        fragmentList.add(personFragment1);

        /**
         * 获取屏幕像素密度
         */
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        widthPixels= dm.widthPixels;
        vp_main_fg.setOffscreenPageLimit(2);
        vp_main_fg.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        vp_main_fg.setCurrentItem(1,false);
//        vp_main_fg.setOffscreenPageLimit(0);
        vp_main_fg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //mainFragment.v_mainFragment_line
//                animator.ofFloat(line,"translationX",100,100);
//                animator.setDuration(400);
//                animator.start_1();
//                if (vp_main_fg.getMoveRight()) {
//                    positionOffset=positionOffset-1;
//                }else if(vp_main_fg.getMoveLeft()){
//
//                }else{
//                    positionOffset=0;
//                }
//                if ((vp_main_fg.getMoveRight()&&isScrolling)||isRight) {
//                    if (positionOffset==0) {
//
//                    }else{
//                        positionOffset=positionOffset-1;
//                    }
//                    isRight=true;
//                }
//                if (positionOffsetPixels==0) {
//                    positionOffset=0;
//                    isRight=false;
//                    isScrolling=false;
//                }
                if (positionOffset==0){

                }else{
                    positionOffset=positionOffset-1;
                }
                Log.i("TAG", "onPageScrolled: positionOffsetPixels----"+positionOffsetPixels+"--positionOffset--"+positionOffset);
//                mainFragment=(MainFragment) childFragment.getChildFragmentManager().getFragments().get(0);
//
//                mainFragment.setLineCallBack(positionOffset);

//                MainFragment.instance.setLineCallBack(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
//                if (position==0) {
//                    vp_main_fg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//
//                }else{
//                    vp_main_fg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state==0) {
//                    isScrolling=true;
//                }else if(state==2){
//                    isScrolling=false;
//                    isRight=false;
//                 }

            }
        });
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RongIM.getInstance().disconnect();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    /**
     * 定位方法
     */

    public void getLocation() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mNearbySearch=NearbySearch.getInstance(this);
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000 * 60 * 10);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
//        mlocationClient.startAssistantLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                myLatitude = aMapLocation.getLatitude();//获取纬度
                SPUtils.put(this, "" + GlobalConstants.LATITUDE, myLatitude);
                myLongitude = aMapLocation.getLongitude();//获取经度
                SPUtils.put(this, "" + GlobalConstants.LONGITUDE, myLongitude);
//                L.i("123"+SPUtils.get(mContext, GlobalConstants.LATITUDE,0.0d));

                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                Log.i("TAB", "onLocationChanged: 纬度:" + myLatitude + ",经度:" + myLongitude + "," + aMapLocation.getAccuracy() + "地址" + aMapLocation.getAddress());
                mlocationClient.stopLocation();
                upLoadLocation();

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                T.showShort(MyApplication.getInstance(), "定位失败,请检查是否开启应用定位权限");

            }
        }
    }

    private void upLoadLocation() {
        //构造上传位置信息
        UploadInfo loadInfo = new UploadInfo();
        //设置上传位置的坐标系支持AMap坐标数据与GPS数据
        loadInfo.setCoordType(NearbySearch.AMAP);
        //设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取
        loadInfo.setPoint(new LatLonPoint(myLatitude, myLongitude));
        //设置上传用户id
        loadInfo.setUserID((String) SPUtils.get(this, GlobalConstants.USERID, ""));
        //调用异步上传接口
        mNearbySearch.uploadNearbyInfoAsyn(loadInfo);
        mNearbySearch.addNearbyListener(new NearbySearch.NearbyListener() {
            @Override
            public void onUserInfoCleared(int i) {

            }

            @Override
            public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int i) {

            }

            @Override
            public void onNearbyInfoUploaded(int i) {
                Log.i("TAB", "onNearbyInfoUploaded: 上报位置" + i);
            }
        });
    }


}
