package com.lede.second_23.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.FriendBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.activity.ConcernActivity_2;
import com.lede.second_23.ui.activity.WelcomeActivity;
import com.lede.second_23.utils.SPUtils;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 向我打招呼的人 列表页面
 * Created by ld on 17/10/19.
 */

public class MainFragmentSayHi extends Fragment {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;

    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_SAYHI_USER = 888;


    private List<FriendBean.DataBean> mSayHiUserList=new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_sayhi, container, false);


        ButterKnife.bind(this, view);

        mGson = new Gson();
        initView();
        initEvent();
        doRequest();
        mRefreshLayout.isRefreshing();
        return view;
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mAdapter = new CommonAdapter<FriendBean.DataBean>(getActivity(), R.layout.fragment_youlike_item, mSayHiUserList) {
            @Override
            protected void convert(ViewHolder holder, final FriendBean.DataBean dataBean, int position) {

                ImageView userIcon=holder.getView(R.id.user_icon);
                TextView user_nickName=holder.getView(R.id.user_nickName);
                user_nickName.setText(dataBean.getNickName());
                Glide.with(getContext())
                        .load(dataBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(userIcon);

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ConcernActivity_2.class);
                        intent.putExtra(USERID,dataBean.getUserId());
                        Toast.makeText(mContext, "已有人向你打招呼了\n" +
                                "点击笑脸可以互动了", Toast.LENGTH_LONG).show();
                        getActivity().startActivity(intent);
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
                doRequest();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(500);
            }
        });
    }




    private void doRequest() {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_SAYHI_USER:
                        mRefreshLayout.finishRefresh();
                        parseSayHiUser(response.get());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_SAYHI_USER:

                        mRefreshLayout.finishRefresh();
                        break;
                    default:
                        break;
                }
            }
        };
        Request<String> sayHiUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/" + (String) SPUtils.get(getContext(), USERID, "") + "/followers", RequestMethod.POST);
        sayHiUserRequest.add("access_token", (String) SPUtils.get(getContext(), GlobalConstants.TOKEN, ""));
        sayHiUserRequest.add("pageNum", 1);
        sayHiUserRequest.add("pageSize", 100);
        RequestServer.getInstance().request(REQUEST_SAYHI_USER, sayHiUserRequest, simpleResponseListener);


    }


    /**
     * 解析喜欢我的用户
     *
     * @param json
     */
    private void parseSayHiUser(String json) {
        FriendBean friendBean = mGson.fromJson(json, FriendBean.class);

        List<FriendBean.DataBean> allSayHi=new ArrayList<>();
        allSayHi.addAll(friendBean.getData());

        mSayHiUserList.clear();


        if (friendBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(getContext(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), WelcomeActivity.class));
        } else {
            //将打过招呼 并且还不是好友的用户 显示出来
            for (FriendBean.DataBean user:allSayHi
                 ) {
                if (!user.isFriend()) {
                    mSayHiUserList.add(user);
                }
            }
            mAdapter.notifyDataSetChanged();


        }

    }

}
