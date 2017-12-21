package com.lede.second_23.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.ForumService;
import com.lede.second_23.service.NearByUserService;
import com.lede.second_23.ui.activity.NearByActivity;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.utils.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.USER_SEX;

/**
 * Created by ld on 17/10/19.
 */

public class MainFragmentNearBy extends Fragment {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Bind(R.id.recyclerView_girl)
    RecyclerView girlRecyclerView;


    private Gson mGson;

    private CommonAdapter mAdapter;
    private CommonAdapter girlAdapter;


    private ForumService forumService;
    public MultiTransformation transformation;
    private RequestManager mRequestManager;

    private ArrayList<AllForumBean.DataBean.SimpleBean.ListBean> forumList = new ArrayList<>();
    private ArrayList<List<AllForumBean.DataBean.SimpleBean.ListBean>> forumListList = new ArrayList<>();
    private List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> girlList = new ArrayList<>();

    private boolean isRefresh = true;
    private boolean isHasNextPage = true;

    private int currentPage = 1;
    String address;
    String matchedSex = "女";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_nearby, container, false);


        ButterKnife.bind(this, view);

        mRequestManager = Glide.with(getContext());
        mGson = new Gson();
        address = (String) SPUtils.get(getActivity(), ADDRESS, "");
        String sex = (String) SPUtils.get(getActivity(), USER_SEX, "男");
        forumService = new ForumService(getActivity());

        if (sex.equals("女")) {
            matchedSex = "男";
        }
        initView();
        initEvent();

        toRefresh();
        mRefreshLayout.isRefreshing();
        return view;
    }


    @OnClick({R.id.women_img_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.women_img_more:
                //改为获取异性同城用户
                Intent intent = new Intent(getActivity(), NearByActivity.class);

                startActivity(intent);
                break;

            default:
                break;


        }

    }


    private void initView() {
        //图片加圆角  注：不能在xml直接使用 centerCrop
        transformation = new MultiTransformation(
                new CenterCrop(getContext()),
                new RoundedCornersTransformation(getContext(), 10, 0, RoundedCornersTransformation.CornerType.ALL)


        );


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdapter = new CommonAdapter<List<AllForumBean.DataBean.SimpleBean.ListBean>>(getActivity(), R.layout.pushed_forum_item, forumListList) {
            @Override
            protected void convert(ViewHolder holder, final List<AllForumBean.DataBean.SimpleBean.ListBean> list, int position) {

                ImageView imageView0 = holder.getView(R.id.image1);
                ImageView imageView1 = holder.getView(R.id.image2);
                ImageView imageView2 = holder.getView(R.id.image3);
                ImageView imageView3 = holder.getView(R.id.image4);
                ImageView imageView4 = holder.getView(R.id.image5);
                ImageView imageView5 = holder.getView(R.id.image6);
                ImageView imageView6 = holder.getView(R.id.image7);
                ImageView imageView7 = holder.getView(R.id.image8);
                ImageView imageView8 = holder.getView(R.id.image9);






//                List<ImageView> viewList=new ArrayList<>();
//                viewList.add(imageView0);
//                viewList.add(imageView1);
//                viewList.add(imageView2);
//                viewList.add(imageView3);
//                viewList.add(imageView4);
//                viewList.add(imageView5);
//                viewList.add(imageView6);
//                viewList.add(imageView7);

//                for(int i=0;i<list.size();i++){
//
//
//                }




                if (list.size()<1) {
//                    imageView0.setVisibility(View.GONE);
                } else if (!list.get(0).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(0).getAllRecords().get(0).getUrl()).into(imageView0);
                } else {
                    mRequestManager.load(list.get(0).getAllRecords().get(0).getUrlThree()).into(imageView0);
                }

                if (list.size()<2) {
//                    imageView1.setVisibility(View.GONE);
                } else if (!list.get(1).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(1).getAllRecords().get(0).getUrl()).into(imageView1);
                } else {
                    mRequestManager.load(list.get(1).getAllRecords().get(0).getUrlThree()).into(imageView1);
                }

                if (list.size()<3) {
//                    imageView2.setVisibility(View.GONE);
                } else if (!list.get(2).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(2).getAllRecords().get(0).getUrl()).into(imageView2);
                } else {
                    mRequestManager.load(list.get(2).getAllRecords().get(0).getUrlThree()).into(imageView2);
                }


                if (list.size()<4) {
//                    imageView3.setVisibility(View.GONE);
                } else if (!list.get(3).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(3).getAllRecords().get(0).getUrl()).into(imageView3);
                } else {
                    mRequestManager.load(list.get(3).getAllRecords().get(0).getUrlThree()).into(imageView3);
                }

                if (list.size()<5) {
//                    imageView4.setVisibility(View.GONE);
                } else if (!list.get(4).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(4).getAllRecords().get(0).getUrl()).into(imageView4);
                } else {
                    mRequestManager.load(list.get(4).getAllRecords().get(0).getUrlThree()).into(imageView4);
                }
                if (list.size()<6) {
//                    imageView5.setVisibility(View.GONE);
                } else if (!list.get(5).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(5).getAllRecords().get(0).getUrl()).into(imageView5);
                } else {
                    mRequestManager.load(list.get(5).getAllRecords().get(0).getUrlThree()).into(imageView5);
                }
                if (list.size()<7) {
//                    imageView6.setVisibility(View.GONE);
                } else if (!list.get(6).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(6).getAllRecords().get(0).getUrl()).into(imageView6);
                } else {
                    mRequestManager.load(list.get(6).getAllRecords().get(0).getUrlThree()).into(imageView6);
                }
                if (list.size()<8) {
//                    imageView7.setVisibility(View.GONE);
                } else if (!list.get(7).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(7).getAllRecords().get(0).getUrl()).into(imageView7);
                } else {
                    mRequestManager.load(list.get(7).getAllRecords().get(0).getUrlThree()).into(imageView7);
                }
                if (list.size()<9) {
//                    imageView0.setVisibility(View.GONE);
                } else if (!list.get(8).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(list.get(8).getAllRecords().get(0).getUrl()).into(imageView8);
                } else {
                    mRequestManager.load(list.get(8).getAllRecords().get(0).getUrlThree()).into(imageView8);
                }


//                if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
//                    mRequestManager.load(listBean.getAllRecords().get(0).getUrl()).into(showView_show);
//                    showView_play.setVisibility(View.GONE);
//                    if(listBean.getAllRecords().size()==1||listBean.getAllRecords().size()==0){
//                        showView_photos.setVisibility(View.GONE);
//                    }else{
//                        showView_photos.setVisibility(View.VISIBLE);
//                    }


//                }else {
//                    Log.i("videopic", "convert: "+listBean.getAllRecords().get(0).getUrlThree());
//                    mRequestManager.load(listBean.getAllRecords().get(0).getUrlThree()).into(showView_show);
//                    showView_play.setVisibility(View.VISIBLE);
//                    showView_photos.setVisibility(View.GONE);
//                }


            }
        };
        mRecyclerView.setAdapter(mAdapter);


        girlRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        girlAdapter = new CommonAdapter<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>(getActivity(), R.layout.near_user_item, girlList) {
            @Override
            protected void convert(ViewHolder holder, final SameCityUserBean.DataBean.UserInfoList.UserInfoListBean userInfoListBean, int position) {
                ImageView imageView = holder.getView(R.id.user_icon);
                TextView textView = holder.getView(R.id.user_nickName);
                ImageView vipTag = holder.getView(R.id.vip_tag);
                mRequestManager.load(userInfoListBean.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(imageView);
                textView.setText(userInfoListBean.getNickName());
                if (userInfoListBean.getTrueName() != null && userInfoListBean.getTrueName().equals("1")) {
                    vipTag.setVisibility(View.VISIBLE);
                } else {
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
        girlRecyclerView.setAdapter(girlAdapter);


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

        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {


        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        } else {
            mRefreshLayout.finishLoadmore();
        }
    }

    private void doRequest(int pageNum) {

        NearByUserService nearByUserService = new NearByUserService(getActivity());

        nearByUserService.requestCitySingleSex(address, matchedSex, 1, 3, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list = (List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean>) o;
                girlList.clear();
                girlList.addAll(list);
                girlAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });

        forumService.requestPushForum(pageNum,100, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                List<AllForumBean.DataBean.SimpleBean.ListBean> list = (List<AllForumBean.DataBean.SimpleBean.ListBean>) o;
                List<AllForumBean.DataBean.SimpleBean.ListBean> list1 = new ArrayList<AllForumBean.DataBean.SimpleBean.ListBean>();
                List<AllForumBean.DataBean.SimpleBean.ListBean> list2 = new ArrayList<AllForumBean.DataBean.SimpleBean.ListBean>();

                if (list.size() != 0) {

                    if (isRefresh) {
                        forumListList.clear();
                        currentPage = 1;
                        isHasNextPage = true;
                    } else {
                        currentPage++;
                    }

                    if (list.size() > 9) {
                        for (int i = 0; i < 9; i++) {
                            list1.add(list.get(i));

                        }
                        for (int i = 9; i < list.size(); i++) {
                            list2.add(list.get(i));
                        }

                        forumListList.add(list1);
                        forumListList.add(list2);


                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list1.add(list.get(i));
                        }
                        forumListList.add(list1);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    isHasNextPage = false;
                }





            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });


    }


}

