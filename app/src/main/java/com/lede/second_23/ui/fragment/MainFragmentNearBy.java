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
import android.widget.LinearLayout;
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
import com.lede.second_23.ui.activity.ForumDetailActivity;
import com.lede.second_23.ui.activity.NearByActivity;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
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
 * 这个瓜皮布局的思路要记一下
 * 这是写的最有意思的一个页面
 * <p>
 * <p>
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
    private ArrayList<AllForumBean.DataBean.SimpleBean.ListBean> forumAddList = new ArrayList<>();
    private ArrayList<AllForumBean.DataBean.SimpleBean.ListBean> forumOriginalList = new ArrayList<>();

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


        GridLayoutManager manager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (position % 16 == 0 || position % 16 == 9) {
                    return 2;
                } else {
                    return 1;
                }


            }
        });

        mRecyclerView.setLayoutManager(manager);
        mAdapter = new CommonAdapter<AllForumBean.DataBean.SimpleBean.ListBean>(getActivity(), R.layout.imageview_item, forumList) {
            @Override
            protected void convert(ViewHolder holder, final AllForumBean.DataBean.SimpleBean.ListBean bean, int position) {
//
                ImageView imageview1 = holder.getView(R.id.image1);
                ImageView imageview2 = holder.getView(R.id.image2);
                if (position % 16 == 0 || position % 16 == 9) {
                    imageview1.setLayoutParams(new LinearLayout.LayoutParams(UiUtils.dip2px(254), UiUtils.dip2px(254)));
                    imageview2.setVisibility(View.GONE);
                } else if (position % 16 == 1 || position % 16 == 8) {
                    imageview2.setVisibility(View.VISIBLE);
                    imageview1.setLayoutParams(new LinearLayout.LayoutParams(UiUtils.dip2px(125), UiUtils.dip2px(125)));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UiUtils.dip2px(125), UiUtils.dip2px(125));
                    lp.setMargins(0, UiUtils.dip2px(4), 0, 0);
                    imageview2.setLayoutParams(lp);

                    int count = position / 16;


                    if (position % 16 == 1) {

                       final int fcount  = count * 2 + 0;
                        if (forumAddList.size() >= fcount + 1) {
                            if (!forumAddList.get(fcount).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                                mRequestManager.load(forumAddList.get(fcount).getAllRecords().get(0).getUrl()).into(imageview2);

                            } else {
                                mRequestManager.load(forumAddList.get(fcount).getAllRecords().get(0).getUrlThree()).into(imageview2);
                            }
                            imageview2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), ForumDetailActivity.class);
                                    intent.putExtra("forumId", forumAddList.get(fcount).getForumId());
                                    intent.putExtra("userId", forumAddList.get(fcount).getUserId());
                                    intent.putExtra("forum", forumAddList.get(fcount));
                                    startActivity(intent);
                                }
                            });
                        }


                    } else if (position % 16 == 8) {
                       final int  fcount = count * 2 + 1;
                        if (forumAddList.size() >= fcount + 1) {

                            if (!forumAddList.get(fcount).getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                                mRequestManager.load(forumAddList.get(fcount).getAllRecords().get(0).getUrl()).into(imageview2);

                            } else {
                                mRequestManager.load(forumAddList.get(fcount).getAllRecords().get(0).getUrlThree()).into(imageview2);
                            }
                            imageview2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), ForumDetailActivity.class);
                                    intent.putExtra("forumId", forumAddList.get(fcount).getForumId());
                                    intent.putExtra("userId", forumAddList.get(fcount).getUserId());
                                    intent.putExtra("forum", forumAddList.get(fcount));
                                    startActivity(intent);
                                }
                            });
                        }
                    }




                } else {
                    imageview2.setVisibility(View.GONE);
                    imageview1.setLayoutParams(new LinearLayout.LayoutParams(UiUtils.dip2px(125), UiUtils.dip2px(125)));

                }


                if (!bean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    mRequestManager.load(bean.getAllRecords().get(0).getUrl()).into(imageview1);
                } else {
                    mRequestManager.load(bean.getAllRecords().get(0).getUrlThree()).into(imageview1);
                }

            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(getActivity(), ForumDetailActivity.class);
                intent.putExtra("forumId", forumList.get(position).getForumId());
                intent.putExtra("userId", forumList.get(position).getUserId());
                intent.putExtra("forum", forumList.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
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

        forumService.requestPushForum(pageNum, 80, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                List<AllForumBean.DataBean.SimpleBean.ListBean> list = (List<AllForumBean.DataBean.SimpleBean.ListBean>) o;

                if (list.size() != 0) {

                    if (isRefresh) {
                        forumList.clear();
                        forumOriginalList.clear();
                        currentPage = 1;
                        isHasNextPage = true;
                    } else {
                        currentPage++;
                    }
                    forumList.addAll(list);
                    forumOriginalList.addAll(list);
                    checkData();
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

    /**
     * 对即将进行展示的data进行处理，以符合瓜皮布局
     * <p>
     * 每9个数据抽出一个数据
     * <p>
     * 每18个数据 ，抽出 position=2 ，position=9
     */
    public void checkData() {

        forumAddList.clear();
        int addCount = forumOriginalList.size() / 18;

        for (int i = 0; i <= addCount; i++) {
            if (i < addCount) {
                forumAddList.add(forumOriginalList.get(i * 18 + 2));
                forumAddList.add(forumOriginalList.get(i * 18 + 9));
            } else {

                int position18 = forumOriginalList.size() % 18;
                if (position18 > 2) {
                    forumAddList.add(forumOriginalList.get(i * 18 + 2));
                }

                if (position18 > 9) {
                    forumAddList.add(forumOriginalList.get(i * 18 + 9));

                }
            }


        }

        forumList.removeAll(forumAddList);
        mAdapter.notifyDataSetChanged();
    }


}

