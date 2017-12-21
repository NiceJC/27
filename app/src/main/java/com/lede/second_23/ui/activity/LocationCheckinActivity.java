package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lede.second_23.R;
import com.lede.second_23.ui.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 显示 发布微博事的定位信息
 * Created by ld on 17/12/18.
 */

public class LocationCheckinActivity extends BaseActivity implements  AMapLocationListener, PoiSearch.OnPoiSearchListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.back)
    ImageView back;





    private AMapLocationClient mlocationClient;
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData;
    private WifiManager mWifiManager;
    private PoiSearch poisearch;

    private LatLng checkinpoint,mlocation;
    private Button locbtn, checkinbtn;
    private boolean isItemClickAction, isLocationAction;

    private int selectedPosition=-1;
    private CommonAdapter<PoiItem> mAdapter;
    private String locateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_checkin);

        ButterKnife.bind(this);

        resultData = new ArrayList<>();
        init();
        //初始化定位
        initLocation();
        //开始定位
        startLocation();

    }

    @OnClick({R.id.back,R.id.locate_bt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.locate_bt:
                Toast.makeText(LocationCheckinActivity.this, "重新定位...", Toast.LENGTH_SHORT).show();

                //设置定位参数
                mlocationClient.setLocationOption(getOption());
                // 启动定位
                mlocationClient.startLocation();
                break;
            default:
                break;
        }


    }


    private void init() {

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new CommonAdapter<PoiItem>(LocationCheckinActivity.this,R.layout.view_holder_result,resultData) {
            @Override
            protected void convert(ViewHolder holder, PoiItem poiItem, int position) {

               TextView textTitle = holder.getView(R.id.text_title);
                TextView textSubTitle = holder.getView(R.id.text_title_sub);
                ImageView imageCheck =holder.getView(R.id.image_check);

                textTitle.setText(poiItem.getTitle());
                textSubTitle.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());

                imageCheck.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);


            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                selectedPosition=position;
                mAdapter.notifyDataSetChanged();
                locateText=resultData.get(position).getTitle();
                Intent intent=new Intent(LocationCheckinActivity.this,AllIssueTextActivity.class);
                intent.putExtra("location",locateText);
                startActivity(intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        recyclerView.setAdapter(mAdapter);



    }



    /**
     * 初始化定位，设置回调监听
     */
    private void initLocation() {
        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位监听
        mlocationClient.setLocationListener(this);
    }

    /**
     * 设置定位参数
     * @return 定位参数类
     */
    private AMapLocationClientOption getOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setLocationCacheEnable(false);//设置是否返回缓存中位置，默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        return mOption;
    }

    /**
     * 开始定位
     */
    private void startLocation(){
        checkWifiSetting();
        //设置定位参数
        mlocationClient.setLocationOption(getOption());
        // 启动定位
        mlocationClient.startLocation();
    }

    /**
     * 销毁定位
     *
     */
    private void destroyLocation(){
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
            mlocationClient = null;}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }







    /**
     * 检查wifi，并提示用户开启wifi
     */
    private void checkWifiSetting() {
        if (mWifiManager.isWifiEnabled()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("开启WIFI会提升定位准确性"); //设置内容
        builder.setIcon(R.mipmap.logo);//设置图标，图片id即可
        builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent); // 打开系统设置界面
            }
        });
        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }



    /**
     * 返回定位结果的回调
     * @param aMapLocation 定位结果
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
            mlocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            checkinpoint = mlocation;
            isLocationAction = true;

                doSearchQuery(searchLatlonPoint);


        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr",errText);
        }
    }

    /**
     * 搜索周边poi
     * @param centerpoint
     */
    private void doSearchQuery(LatLonPoint centerpoint) {
        PoiSearch.Query query = new PoiSearch.Query("","","");
        query.setPageSize(30);
        query.setPageNum(0);
        poisearch = new PoiSearch(this,query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(centerpoint, 500, true));
        poisearch.searchPOIAsyn();
    }

    /**
     * 搜索Poi回调
     * @param poiResult 搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {

        if (resultCode == AMapException.CODE_AMAP_SUCCESS){
            if (poiResult != null && poiResult.getPois().size() > 0){
                List<PoiItem> poiItems = poiResult.getPois();
                resultData.addAll(poiItems);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(LocationCheckinActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LocationCheckinActivity.this, "搜索失败，错误 "+resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ID搜索poi的回调
     * @param poiItem 搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int resultCode) {

    }



}
