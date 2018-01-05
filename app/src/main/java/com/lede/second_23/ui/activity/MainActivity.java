package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.adapter.MyFragmentPagerAdapter;
import com.lede.second_23.bean.CheckPhotoBean;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.interface_utils.OnUploadFinish;
import com.lede.second_23.service.PickService;
import com.lede.second_23.service.UploadService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.service.VIPService;
import com.lede.second_23.ui.fragment.ForumFragment;
import com.lede.second_23.ui.fragment.MainFragment1;
import com.lede.second_23.ui.fragment.PersonalFragment2;
import com.lede.second_23.utils.MyViewPager;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.SnackBarUtil;
import com.lede.second_23.utils.StatusBarUtil;
import com.lede.second_23.utils.T;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;
import com.umeng.analytics.MobclickAgent;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.LATITUDE;
import static com.lede.second_23.global.GlobalConstants.LONGITUDE;
import static com.lede.second_23.global.GlobalConstants.MESSAGE_TYPE;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.USER_HEAD_IMG;

/**
 * 管理
 */
public class MainActivity extends FragmentActivity implements AMapLocationListener {

    public MyViewPager vp_main_fg;
    private RelativeLayout relativeLayout;
    private ArrayList<Fragment> fragmentList;
    public static MainActivity instance = null;
    private int widthPixels;
    private boolean isScrolling = false;
    private boolean isRight = false;
    //    private ChildFragment childFragment;
//    private OldCameraFragment cameraFragment;
//    private IssueFragment issueFragment;
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    private ForumFragment forumFragment;
    private MainFragment1 mainFragment1;
    private PersonalFragment2 personFragment2;

    private AMapLocationClient mlocationClient;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    public AMapLocationClientOption mLocationOption = null;

    private double myLatitude;//纬度
    private double myLongitude;//经度

    private ImageView declaration;
    private NearbySearch mNearbySearch;

    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_IF_HAVEPHOTO = 66423;
    private CheckPhotoBean checkPhotoBean = null;
    private Gson mGson;

    private String pushUserId;
    private int messageType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.StatusBarLightMode(this);


        pushUserId=getIntent().getStringExtra(USERID);
        messageType=getIntent().getIntExtra(MESSAGE_TYPE,1);

        mGson = new Gson();
        instance = this;
        getLocation();
        Log.i("TAC", "onCreate: ");
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#3b5998"));
        UpdateHelper.getInstance().autoUpdate("com.lede.second_23", false, 5000);
        UpdateHelper.getInstance().setDebugMode(true);
        initView();
        requestPhoto();
        checkVIP();

        handlePushMesesage(pushUserId,messageType);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        pushUserId=intent.getStringExtra(USERID);
        messageType=intent.getIntExtra(MESSAGE_TYPE,1);
        handlePushMesesage(pushUserId,messageType);


    }

    public void handlePushMesesage(String pushUserId,int messageType){
        switch (messageType){
            case 1://系统消息，匹配成功推送
                if(pushUserId!=null&&!pushUserId.equals("")){
                    showPushMessageDialog();
                }

                break;
            case 2://聊天消息
                startActivity(new Intent(this, ConversationListDynamicActivtiy.class));


                break;
            default:
                break;



        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }

    private void initView() {
        relativeLayout= (RelativeLayout) findViewById(R.id.activity_main);
        vp_main_fg = (MyViewPager) findViewById(R.id.vp_main_fg);
        declaration = (ImageView) findViewById(R.id.declaration_main);
        if (((Boolean) SPUtils.get(this, GlobalConstants.DECLARATIONMAIN, true))) {
            declaration.setVisibility(View.VISIBLE);
            declaration.setClickable(true);
        } else {
            declaration.setVisibility(View.GONE);
            declaration.setClickable(false);
        }

        declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declaration.setVisibility(View.GONE);
                declaration.setClickable(false);
                SPUtils.put(MainActivity.this, GlobalConstants.DECLARATIONMAIN, false);
            }
        });

        initFragmentViewPager();




    }



    //展示收到推送的dialog
    private void  showPushMessageDialog(){

        UserInfoService userInfoService=new UserInfoService(this);
        userInfoService.getUserInfo(pushUserId, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                PersonalAlbumBean.DataBean.UserInfo userInfo= (PersonalAlbumBean.DataBean.UserInfo) o;
                String pushUserImgUrl=userInfo.getImgUrl();
                final String pushUserName=userInfo.getNickName();

                String myImgUrl= (String) SPUtils.get(MainActivity.this,USER_HEAD_IMG,"");
                DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.push_message_dialog))
                        .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                        .setCancelable(true)
                        .setGravity(Gravity.CENTER)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {

                                switch (view.getId()) {
                                    case R.id.close:

                                        dialog.dismiss();

                                        break;
                                    case R.id.toChat:
                                        RongIM.getInstance().startConversation(MainActivity.this, Conversation.ConversationType.PRIVATE, pushUserId, pushUserName);
                                        dialog.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .setExpanded(false).create();

                ImageView pushImageView= (ImageView) dialogPlus.findViewById(R.id.image_push_user);
                ImageView myImageView= (ImageView) dialogPlus.findViewById(R.id.image_me);
                Glide.with(MainActivity.this).load(pushUserImgUrl).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(pushImageView);
                Glide.with(MainActivity.this).load(myImgUrl).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(myImageView);


                dialogPlus.show();

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }


    private void initFragmentViewPager() {
        fragmentList = new ArrayList<Fragment>();

        forumFragment = new ForumFragment();
        fragmentList.add(forumFragment);
//        childFragment=new ChildFragment();
//        fragmentList.add(childFragment);
        mainFragment1 = new MainFragment1();
        personFragment2 = new PersonalFragment2();
        fragmentList.add(mainFragment1);
        fragmentList.add(personFragment2);

        /**
         * 获取屏幕像素密度
         */
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        widthPixels= dm.widthPixels;
        vp_main_fg.setOffscreenPageLimit(2);
        vp_main_fg.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        vp_main_fg.setCurrentItem(1, false);
//        vp_main_fg.setOffscreenPageLimit(0);
        vp_main_fg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {

                } else {
                    positionOffset = positionOffset - 1;
                }
                Log.i("TAG", "onPageScrolled: positionOffsetPixels----" + positionOffsetPixels + "--positionOffset--" + positionOffset);

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    private void checkVIP(){

        VIPService vipService=new VIPService(MainActivity.this);
        vipService.checkVIP(new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });
    }

//    private void verifyMarry(){
//        MatingService matingService=new MatingService(this);
//        matingService.requestVerify(new MyCallBack() {
//            @Override
//            public void onSuccess(Object o) {
//
//            }
//
//            @Override
//            public void onFail(String mistakeInfo) {
//
//            }
//        });



//    }

    /**
     * 检测图片墙是否有照片
     * http://localhost:8080/photo/flashPhotoByUser
     * String access_token
     * 正常返回：isExist = true时，代表有；isExist = false;代表无
     */
    //请求当前用户是否在照片墙上上传照片
    private void requestPhoto() {


        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_IF_HAVEPHOTO:
                        parseIfPhotoExist(response.get());
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_IF_HAVEPHOTO:
                        break;
                    default:
                        break;

                }
            }
        };

        Request<String> photoExistRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/flashPhotoByUser", RequestMethod.POST);
        photoExistRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        RequestServer.getInstance().request(REQUEST_IF_HAVEPHOTO, photoExistRequest, simpleResponseListener);
    }


    private void parseIfPhotoExist(String s) {
        checkPhotoBean = mGson.fromJson(s, CheckPhotoBean.class);
        boolean hasPhoto = checkPhotoBean.getData().isExist();
        if (!hasPhoto) {
            showPhotoDialog();
        }
    }

    private void showPhotoDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.check_photo_dialog))
                .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.toUpload:
                                upLoadAlbum();
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setExpanded(false).create();
        dialogPlus.show();

    }


    public void upLoadAlbum() {
        PickService pickService = new PickService(this);
        pickService.pickPhoto(5,new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {
                OnUploadFinish onUploadFinish = new OnUploadFinish() {
                    @Override
                    public void success() {
                        SnackBarUtil.getInstance(vp_main_fg, MainActivity.this, R.string.upload_success).show();
                    }
                    @Override
                    public void failed() {
                        SnackBarUtil.getInstance(vp_main_fg, MainActivity.this, R.string.upload_failed).show();

                    }
                };
                UploadService uploadService = new UploadService(MainActivity.this);
                uploadService.upload(list, onUploadFinish);
            }

            @Override
            public void onSelectSuccess(LocalMedia localMedia) {
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
//
//    public void getLocation() {
//        //声明mLocationOption对象
//        AMapLocationClientOption mLocationOption = null;
//        mNearbySearch = NearbySearch.getInstance(this);
//        mlocationClient = new AMapLocationClient(this);
////初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
////设置定位监听
//        mlocationClient.setLocationListener(this);
////设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
////设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(1000 * 60 * 10);
////设置定位参数
//        mlocationClient.setLocationOption(mLocationOption);
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用onDestroy()方法
//        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//        //启动定位
//        mlocationClient.startLocation();
////        mlocationClient.startAssistantLocation();
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                myLatitude = aMapLocation.getLatitude();//获取纬度
//                SPUtils.put(this, "" + GlobalConstants.LATITUDE, myLatitude);
//                myLongitude = aMapLocation.getLongitude();//获取经度
//                SPUtils.put(this, "" + GlobalConstants.LONGITUDE, myLongitude);
////                L.i("123"+SPUtils.get(mContext, GlobalConstants.LATITUDE,0.0d));
//
//                aMapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
//                Log.i("TAB", "onLocationChanged: 纬度:" + myLatitude + ",经度:" + myLongitude + "," + aMapLocation.getAccuracy() + "地址" + aMapLocation.getAddress());
//                mlocationClient.stopLocation();
//                upLoadLocation();
//
//            } else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError", "location Error, ErrCode:"
//                        + aMapLocation.getErrorCode() + ", errInfo:"
//                        + aMapLocation.getErrorInfo());
//                T.showShort(MyApplication.getInstance(), "定位失败,请检查是否开启应用定位权限");
//
//            }
//        }
//    }

    private void upLoadLocation() {
        mNearbySearch=NearbySearch.getInstance(this);
        //构造上传位置信息
        UploadInfo loadInfo = new UploadInfo();
        //设置上传位置的坐标系支持AMap坐标数据与GPS数据
        loadInfo.setCoordType(NearbySearch.AMAP);
        //设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取
        loadInfo.setPoint(new LatLonPoint(myLatitude, myLongitude));
        //设置上传用户id
        String userID=(String) SPUtils.get(this, USERID, "");
        loadInfo.setUserID(userID);
        //调用异步上传接口
        mNearbySearch.uploadNearbyInfoAsyn(loadInfo);
        mNearbySearch.addNearbyListener(new NearbySearch.NearbyListener() {
            @Override
            public void onUserInfoCleared(int i) {

                int a=i;
            }

            @Override
            public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int i) {

                NearbySearchResult result=nearbySearchResult;
                int a=i;
            }

            @Override
            public void onNearbyInfoUploaded(int i) {
                Log.i("TAB", "onNearbyInfoUploaded: 上报位置" + i);
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

                upLoadLocation();

                mLocationClient.stopLocation();

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                T.showShort(MyApplication.getInstance(), "定位失败,请检查是否开启应用定位权限");

            }
        }
    }


    public void showSnackBar(String text){

        SnackBarUtil.getInstance(relativeLayout,text).show();

    }

}
