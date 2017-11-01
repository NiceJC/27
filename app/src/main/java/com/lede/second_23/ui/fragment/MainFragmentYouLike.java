package com.lede.second_23.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PushUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by ld on 17/10/19.
 */

public class MainFragmentYouLike extends Fragment  {

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;

    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_PUSHED_USER=666;


    private ArrayList<PushUserBean.DataBean.UserInfoListBean> pushUserList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main_like,container,false);


        ButterKnife.bind(this,view);

        mGson=new Gson();
        initView();
        initEvent();
        doRequest();
        mRefreshLayout.isRefreshing();
        return view;
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));


         mAdapter=new CommonAdapter<PushUserBean.DataBean.UserInfoListBean>(getActivity(),R.layout.fragment_youlike_item,pushUserList) {
            @Override
            protected void convert(ViewHolder holder, PushUserBean.DataBean.UserInfoListBean userInfoListBean, int position) {

                if(position==1){
                    LinearLayout linearLayout= (LinearLayout) holder.getConvertView();


                    linearLayout.setPadding(0, UiUtils.dip2px(100),0,0);

                }


                ImageView userIcon=holder.getView(R.id.user_icon);
                TextView user_nickName=holder.getView(R.id.user_nickName);
                user_nickName.setText(userInfoListBean.getNickName());
                Glide.with(getContext())
                        .load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(userIcon);
            }


        };

        mRecyclerView.setAdapter(mAdapter);


    }


    public void initEvent(){


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                doRequest();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });
    }


    private void doRequest() {

        simpleResponseListener=new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what){
                    case REQUEST_PUSHED_USER:
                        mRefreshLayout.finishRefresh();
                        parsePushUser(response.get());
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
                        break;
                    default:
                        break;

                }            }
        };

        Request<String> pushUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/push/newPushUser", RequestMethod.POST);
        pushUserRequest.add("access_token", (String) SPUtils.get(getActivity(), GlobalConstants.TOKEN, ""));
        RequestServer.getInstance().request(REQUEST_PUSHED_USER, pushUserRequest,simpleResponseListener);





    }



    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parsePushUser(String json) {
        PushUserBean pushUserBean = mGson.fromJson(json, PushUserBean.class);
        pushUserList.clear();
        pushUserList.addAll(pushUserBean.getData().getUserInfoList());
        mAdapter.notifyDataSetChanged();
    }


}
