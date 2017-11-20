package com.lede.second_23.ui.fragment;

import android.content.Intent;
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
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.activity.UserInfoActivty;
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

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/10/19.
 */

public class MainFragmentYouLike extends Fragment {

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;

    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;
    Request<String> pushUserRequest = null;
    private static final int REQUEST_PUSHED_USER = 666;


    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;


    private ArrayList<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> pushUserList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_like, container, false);


        ButterKnife.bind(this, view);

        mGson = new Gson();
        initView();
        initEvent();
        toRefresh();

        return view;
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));


        mAdapter = new CommonAdapter<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>(getActivity(), R.layout.fragment_youlike_item, pushUserList) {
            @Override
            protected void convert(ViewHolder holder, final SameCityUserBean.DataBean.UserInfoList.UserInfoListBean userInfoListBean, int position) {

                if (position == 1) {
                    LinearLayout linearLayout = (LinearLayout) holder.getConvertView();
                    linearLayout.setPadding(0, UiUtils.dip2px(80), 0, 0);
                } else { //复用第二个的padding会导致滑动时错乱，将其他的padding定为0即可
                    LinearLayout linearLayout = (LinearLayout) holder.getConvertView();
                    linearLayout.setPadding(0, 0, 0, 0);
                }
                ImageView userIcon = holder.getView(R.id.user_icon);
                TextView user_nickName = holder.getView(R.id.user_nickName);
                user_nickName.setText(userInfoListBean.getNickName());
                Glide.with(getContext())
                        .load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(userIcon);

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), UserInfoActivty.class);
                        intent.putExtra(USERID, userInfoListBean.getUserId());
                        startActivity(intent);

                    }
                });


            }


        };

        mRecyclerView.setAdapter(mAdapter);


    }


    public void initEvent() {


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
        if (pushUserRequest != null) {
            pushUserRequest.cancel();
        }
        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {

        if (pushUserRequest != null) {
            pushUserRequest.cancel();
        }
        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        } else {
            mRefreshLayout.finishLoadmore();
        }
    }

    private void doRequest(int page) {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_PUSHED_USER:


                        parsePushUser(response.get());
                        mRefreshLayout.finishLoadmore();
                        mRefreshLayout.finishRefresh();

                        break;
                    default:
                        break;

                }

            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_PUSHED_USER:

                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();

                        break;
                    default:
                        break;

                }
            }
        };

        pushUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/push/newPushByUser", RequestMethod.POST);
        pushUserRequest.add("access_token", (String) SPUtils.get(getActivity(), GlobalConstants.TOKEN, ""));


        pushUserRequest.add("pageNum", page);
        pushUserRequest.add("pageSize", PAGE_SIZE);

        RequestServer.getInstance().request(REQUEST_PUSHED_USER, pushUserRequest, simpleResponseListener);


    }


    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parsePushUser(String json) {
        SameCityUserBean sameCityUserBean = mGson.fromJson(json, SameCityUserBean.class);
        if (isRefresh) {
            pushUserList.clear();
            currentPage = 1;
            isHasNextPage = true;
        } else {
            if (sameCityUserBean.getData().getUserInfoList().getList().size() == 0) {
                isHasNextPage = false;
            } else {
                currentPage++;
            }
        }
        pushUserList.addAll(sameCityUserBean.getData().getUserInfoList().getList());
        mAdapter.notifyDataSetChanged();
    }


}
