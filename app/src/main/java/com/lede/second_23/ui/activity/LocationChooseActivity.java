package com.lede.second_23.ui.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
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
 *
 * 管理版本定位功能
 * 根据关键字  检索匹配的poi
 * Created by ld on 18/3/5.
 */

public class LocationChooseActivity extends BaseActivity implements  PoiSearch.OnPoiSearchListener{




    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.searching_text)
    EditText keyWord;





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

        setContentView(R.layout.activity_location_choose);

        ButterKnife.bind(this);

        resultData = new ArrayList<>();
        init();

    }

    @OnClick({R.id.back,R.id.searching_button})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;

            case R.id.searching_button:
                doSearchQuery(keyWord.getText().toString());
            default:
                break;
        }


    }


    private void init() {

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new CommonAdapter<PoiItem>(LocationChooseActivity.this,R.layout.view_holder_result,resultData) {
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
//                selectedPosition=position;
//                mAdapter.notifyDataSetChanged();
//                locateText=resultData.get(position).getTitle();
//                Intent intent=new Intent(LocationChooseActivity.this,AllIssueTextActivity.class);
//                intent.putExtra("location",locateText);
//                startActivity(intent);
//                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        recyclerView.setAdapter(mAdapter);



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
     */
    private void doSearchQuery(String POIName) {
        PoiSearch.Query query = new PoiSearch.Query(POIName,"","");
        query.setPageSize(30);
        query.setPageNum(0);
        poisearch = new PoiSearch(this,query);
        poisearch.setOnPoiSearchListener(this);
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
                Toast.makeText(LocationChooseActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LocationChooseActivity.this, "搜索失败，错误 "+resultCode, Toast.LENGTH_SHORT).show();
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
