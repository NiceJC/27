package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/11/16.
 */

public class SameCityActivity extends BaseActivity {
    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;

    private Activity context;
    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;
    Request<String> sameCityUserRequest =null;
    private ArrayList<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> sameCityUserList = new ArrayList<>();
    private static final int REQUEST_PUSHED_USER=6166;

    private static final int PAGE_SIZE=20;
    private int currentPage=1;
    private boolean isRefresh=true;
    private String address;
    private boolean isHasNextPage=true;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.same_city_activity);

        context=this;
        ButterKnife.bind(context);
        mGson=new Gson();
        address=getIntent().getStringExtra(ADDRESS);

        initView();
        initEvent();
        toRefresh();

    }


    @OnClick(R.id.back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        mAdapter=new CommonAdapter<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>(this,R.layout.fragment_youlike_item, sameCityUserList) {
            @Override
            protected void convert(ViewHolder holder, final SameCityUserBean.DataBean.UserInfoList.UserInfoListBean userInfoListBean, int position) {


                if(position==2){
                    LinearLayout linearLayout= (LinearLayout) holder.getConvertView();
                    linearLayout.setPadding(0, UiUtils.dip2px(80),0,0);
                }else{ //复用第二个的padding会导致滑动时错乱，将其他的padding定为0即可
                    LinearLayout linearLayout= (LinearLayout) holder.getConvertView();
                    linearLayout.setPadding(0,0,0,0);
                }
                ImageView userIcon=holder.getView(R.id.user_icon);
                TextView user_nickName=holder.getView(R.id.user_nickName);
                user_nickName.setText(userInfoListBean.getNickName());
                Glide.with(SameCityActivity.this)
                        .load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(SameCityActivity.this))
                        .into(userIcon);

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SameCityActivity.this, UserInfoActivty.class);
                        intent.putExtra(USERID,userInfoListBean.getUserId());
                        startActivity(intent);
                    }
                });
            }


        };


        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        View view= LayoutInflater.from(this).inflate(R.layout.same_city_acivity_head,null);
        mHeaderAndFooterWrapper.addHeaderView(view);
        TextView textView= (TextView) view.findViewById(R.id.location);
        textView.setText(address);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();




    }


    public void initEvent(){


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                toRefresh();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                toLoadMore();
            }
        });
    }

    public void toRefresh() {

        //重复请求之前，取消之前的请求
        if (sameCityUserRequest != null) {
            sameCityUserRequest.cancel();
        }
        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {

        if (sameCityUserRequest != null) {
            sameCityUserRequest.cancel();
        }
        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        }else{
            mRefreshLayout.finishLoadmore();
        }
    }

    private void doRequest(int page) {

        simpleResponseListener=new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what){
                    case REQUEST_PUSHED_USER:

                        parseNearUser(response.get());
                        mRefreshLayout.finishLoadmore();
                        mRefreshLayout.finishRefresh();

                        break;
                    default:
                        break;

                }

            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what){
                    case REQUEST_PUSHED_USER:
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();
                        break;
                    default:
                        break;

                }            }
        };

        sameCityUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/findUserByAddress", RequestMethod.POST);
        sameCityUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        sameCityUserRequest.add("address",address);
        sameCityUserRequest.add("pageNum", page);
        sameCityUserRequest.add("pageSize", PAGE_SIZE);

        RequestServer.getInstance().request(REQUEST_PUSHED_USER, sameCityUserRequest,simpleResponseListener);





    }



    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parseNearUser(String json) {

        SameCityUserBean sameCityUserBean = mGson.fromJson(json, SameCityUserBean.class);
        if(isRefresh){
            sameCityUserList.clear();
            currentPage=1;
            isHasNextPage=true;
        }else{
            if(sameCityUserBean.getData().getUserInfoList().getList().size()==0){
                isHasNextPage=false;
            }else{
                currentPage++;
            }
        }


        //显示列表排除掉自己
        List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list=new ArrayList<>();
        list.addAll(sameCityUserBean.getData().getUserInfoList().getList());
        if(list.size()!=0){
            Iterator<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> it=list.iterator();
            while (it.hasNext()){
                SameCityUserBean.DataBean.UserInfoList.UserInfoListBean user=it.next();
                if(user.getUserId().equals((String) SPUtils.get(this, USERID, ""))){
                    it.remove();
                }
            }


            sameCityUserList.addAll(list);
        }

        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

}
