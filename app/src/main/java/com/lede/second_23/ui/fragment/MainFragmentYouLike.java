package com.lede.second_23.ui.fragment;

import android.animation.ObjectAnimator;
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
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.PushedUserService;
import com.lede.second_23.ui.activity.MateActivity;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.SimpleResponseListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.USER_SEX;
import static com.lede.second_23.global.GlobalConstants.VIPPUSHSEX;
import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;
import static com.lede.second_23.ui.activity.VIPSettingActivity.ALL;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOPEN;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOVERDUE;

/**
 * Created by ld on 17/10/19.
 */

public class MainFragmentYouLike extends Fragment {

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.mate)
    ImageView mate;


    private Gson mGson;

    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;
    Request<String> pushUserRequest = null;
    private static final int REQUEST_PUSHED_USER = 666;


    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;
    private int choosenSex ;
    private String VIPStatus;
    private boolean isshowBottom = true;
    private ObjectAnimator animator;
    String address;
    String sex;
    private ArrayList<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> pushUserList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_like, container, false);


        ButterKnife.bind(this, view);

        mGson = new Gson();

        address= (String) SPUtils.get(getActivity(), ADDRESS, "杭州市");
        sex= (String) SPUtils.get(getActivity(),USER_SEX,"男");


        initView();
        initEvent();
        toRefresh();

        return view;
    }

    private void bottom_show(int i) {
        animator = null;
        if (i == 0) {
            animator = ObjectAnimator.ofFloat(mate, "translationY", 0, mate.getHeight()+UiUtils.dip2px(25));
            animator.setDuration(500);
            animator.start();
        } else {
            animator = ObjectAnimator.ofFloat(mate, "translationY", mate.getHeight()+UiUtils.dip2px(25), 0);
            animator.setDuration(500);
            animator.start();
        }
    }
    private void initView() {


        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecyclerView.setLayoutManager(manager);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >= 30) {
                    if (isshowBottom) {
                        return;
                    } else {
                        isshowBottom = true;
//                        if(MainFragment1.instance!=null){
//                            MainFragment1.instance.scrollTopView(1);
//                        }
                        bottom_show(0);
                    }

                } else if (dy <= -30) {
                    if (isshowBottom) {
                        isshowBottom = false;
//                        if(MainFragment1.instance!=null){
//                            MainFragment1.instance.scrollTopView(0);
//                        }
                        bottom_show(1);
                    } else {
                        return;
                    }
                }
            }
        });

        mAdapter = new CommonAdapter<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>(getActivity(), R.layout.fragment_youlike_item, pushUserList) {
            @Override
            protected void convert(ViewHolder holder, final SameCityUserBean.DataBean.UserInfoList.UserInfoListBean userInfoListBean, int position) {

                if (position == 2) {
                    LinearLayout linearLayout = (LinearLayout) holder.getConvertView();
                    linearLayout.setPadding(0, UiUtils.dip2px(80), 0, 0);
                } else { //复用第二个的padding会导致滑动时错乱，将其他的padding定为0即可
                    LinearLayout linearLayout = (LinearLayout) holder.getConvertView();
                    linearLayout.setPadding(0, 0, 0, 0);
                }
                ImageView userIcon = holder.getView(R.id.user_icon);
                TextView user_nickName = holder.getView(R.id.user_nickName);
                ImageView vipTag=holder.getView(R.id.vip_tag);
                user_nickName.setText(userInfoListBean.getNickName());
                Glide.with(getContext())
                        .load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(userIcon);
                if(userInfoListBean.getTrueName()!=null&&userInfoListBean.getTrueName().equals("1")){
                    vipTag.setVisibility(View.VISIBLE);
                }else{
                    vipTag.setVisibility(View.GONE);
                }

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

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_you_like_head, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.same_city);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(), SameCityActivity.class);
//
//                intent.putExtra(ADDRESS,address);
//                intent.putExtra(SEXTYPE,"all");
//                startActivity(intent);
//            }
//        });
        mHeaderAndFooterWrapper.addHeaderView(view);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
//        mHeaderAndFooterWrapper.notifyDataSetChanged();


    }


    public void initEvent() {


        mate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MateActivity.class);


                startActivity(intent);



            }
        });

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
        VIPStatus = (String) SPUtils.get(getActivity(), VIPSTATUS, NOTOPEN);
        choosenSex = (int) SPUtils.get(getActivity(), VIPPUSHSEX,ALL);
        if(!VIPStatus.equals(NOTOVERDUE)){
            //只要用户不是会员  推荐的就是所有性别用户
            choosenSex=ALL;
        }

        PushedUserService pushedUserService =new PushedUserService(getActivity());
        pushedUserService.requestPushUser(choosenSex, page, PAGE_SIZE, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list= (List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>) o;
                if (isRefresh) {
                    pushUserList.clear();
                    currentPage = 1;
                    isHasNextPage = true;
                } else {
                    if (list.size() == 0) {
                        isHasNextPage = false;
                    } else {
                        currentPage++;
                    }
                }

                pushUserList.addAll(list);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });

    }


}
