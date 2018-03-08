package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.adapter.ImageViewPagerAdapter_2;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.ForumService;
import com.lede.second_23.ui.activity.ForumDetailActivity;
import com.lede.second_23.ui.activity.ForumPicActivity;
import com.lede.second_23.ui.activity.ForumVideoPlayActivity;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.ui.view.HackyViewPager;
import com.lede.second_23.utils.TimeUtils;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Fragment
 * 后台推送的动态
 * 可以切换显示方式
 * Created by ld on 18/3/5.
 */

public class ForumFragment_Recommend extends Fragment{



    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;



    private Gson mGson;

    private CommonAdapter mAdapter1;
    private CommonAdapter mAdapter2;


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
    private Context context;
    private ArrayList<ImageView> imgViews;
    private List<AllForumBean.DataBean.SimpleBean.ListBean.AllRecordsBean> list;
    private LayoutInflater layoutInflater;
    private int width;

    private boolean isGridLayout=true; //默认gridLayout
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    private RefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recyclerview, container, false);


        ButterKnife.bind(this, view);

        mRequestManager = Glide.with(getContext());
        mGson = new Gson();
        forumService = new ForumService(getActivity());


        context=getContext();
        layoutInflater = LayoutInflater.from(context);
        initView();
        initEvent(refreshLayout);

        return view;
    }




    public void changeLayout(ImageView findChange ){

        if(isGridLayout){
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(mAdapter2);
            findChange.setSelected(true);
            isGridLayout=false;
        }else{
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(mAdapter1);

            findChange.setSelected(false);
            isGridLayout=true;
        }



    }



    private void initView() {



        //图片加圆角  注：不能在xml直接使用 centerCrop
        transformation = new MultiTransformation(
                new CenterCrop(getContext()),
                new RoundedCornersTransformation(getContext(), 10, 0, RoundedCornersTransformation.CornerType.ALL)


        );


        gridLayoutManager = new GridLayoutManager(getContext(), 3) {

        };

        linearLayoutManager=new LinearLayoutManager(getContext()){

        };





        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (position % 16 == 0 || position % 16 == 9) {
                    return 2;
                } else {
                    return 1;
                }


            }
        });


        mAdapter1 = new CommonAdapter<AllForumBean.DataBean.SimpleBean.ListBean>(getActivity(), R.layout.imageview_item, forumList) {
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
                                    intent.putExtra("isRefreshNearbyFragment",true);

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
                                    intent.putExtra("isRefreshNearbyFragment",true);

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

        mAdapter1.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(getActivity(), ForumDetailActivity.class);
                intent.putExtra("isRefreshNearbyFragment",true);

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


        mAdapter2 = new CommonAdapter<AllForumBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_forum_show, forumOriginalList) {


            @Override
            protected void convert(ViewHolder holder, final AllForumBean.DataBean.SimpleBean.ListBean listBean, final int position) {
                DIYImageView diy_userimg = holder.getView(R.id.diyiv_item_forum_userimg);
                ImageView vipTag=holder.getView(R.id.vip_tag);
                TextView tv_nickname = holder.getView(R.id.tv_item_forum_nickname);

                TextView tv_time = holder.getView(R.id.tv_item_forum_time);
                TextView tv_text = holder.getView(R.id.tv_item_forum_text);
                RelativeLayout rl_pic = holder.getView(R.id.rl_item_forum_pic);

//                iv_video_reply.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        forumVideoReplyList.clear();
//                        forumVideoReplyList.add(null);
//                        showVideoByUserList(listBean.getForumId());
//                        currentForumId = listBean.getForumId();
//                        showPopwindow();
//
//                    }
//                });
                rl_pic.removeAllViews();
                View view = null;
                if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    list = listBean.getAllRecords();
                    width = rl_pic.getWidth();
                    imgViews = null;
                    imgViews = new ArrayList<>();
                    ArrayList<String> banner = null;
                    banner = new ArrayList<>();
                    for (int i = 0; i < listBean.getAllRecords().size(); i++) {
                        banner.add(listBean.getAllRecords().get(i).getUrl());
                    }
                    if (listBean.getAllRecords().get(0).getDspe().equals("0")) {
                        if (list.size() == 1) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_1, rl_pic, true);
                            ImageView iv_1_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_1_1);
                            imgViews.clear();
                            imgViews.add(iv_1_1);
                        } else if (list.size() == 2) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_2, rl_pic, true);
                            ImageView iv_2_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_2_1);
                            ImageView iv_2_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_2_2);
                            imgViews.clear();
                            imgViews.add(iv_2_1);
                            imgViews.add(iv_2_2);
                        } else if (list.size() == 3) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_3, rl_pic, true);
                            ImageView iv_3_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_3_1);
                            ImageView iv_3_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_3_2);
                            ImageView iv_3_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_3_3);
                            imgViews.add(iv_3_1);
                            imgViews.add(iv_3_2);
                            imgViews.add(iv_3_3);
                        } else if (list.size() == 4) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_4, rl_pic, true);
                            ImageView iv_4_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_4_1);
                            ImageView iv_4_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_4_2);
                            ImageView iv_4_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_4_3);
                            ImageView iv_4_4 = (ImageView) view.findViewById(R.id.iv_item_9gongge_4_4);
                            imgViews.add(iv_4_1);
                            imgViews.add(iv_4_2);
                            imgViews.add(iv_4_3);
                            imgViews.add(iv_4_4);
                        } else if (list.size() == 5) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_5, rl_pic, true);
                            ImageView iv_5_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_5_1);
                            ImageView iv_5_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_5_2);
                            ImageView iv_5_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_5_3);
                            ImageView iv_5_4 = (ImageView) view.findViewById(R.id.iv_item_9gongge_5_4);
                            ImageView iv_5_5 = (ImageView) view.findViewById(R.id.iv_item_9gongge_5_5);
                            imgViews.add(iv_5_1);
                            imgViews.add(iv_5_2);
                            imgViews.add(iv_5_3);
                            imgViews.add(iv_5_4);
                            imgViews.add(iv_5_5);
                        } else if (list.size() == 6) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_6, rl_pic, true);
                            ImageView iv_6_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_6_1);
                            ImageView iv_6_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_6_2);
                            ImageView iv_6_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_6_3);
                            ImageView iv_6_4 = (ImageView) view.findViewById(R.id.iv_item_9gongge_6_4);
                            ImageView iv_6_5 = (ImageView) view.findViewById(R.id.iv_item_9gongge_6_5);
                            ImageView iv_6_6 = (ImageView) view.findViewById(R.id.iv_item_9gongge_6_6);
                            imgViews.add(iv_6_1);
                            imgViews.add(iv_6_2);
                            imgViews.add(iv_6_3);
                            imgViews.add(iv_6_4);
                            imgViews.add(iv_6_5);
                            imgViews.add(iv_6_6);
                        } else if (list.size() == 7) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_7, rl_pic, true);
                            ImageView iv_7_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_1);
                            ImageView iv_7_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_2);
                            ImageView iv_7_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_3);
                            ImageView iv_7_4 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_4);
                            ImageView iv_7_5 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_5);
                            ImageView iv_7_6 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_6);
                            ImageView iv_7_7 = (ImageView) view.findViewById(R.id.iv_item_9gongge_7_7);
                            imgViews.add(iv_7_1);
                            imgViews.add(iv_7_2);
                            imgViews.add(iv_7_3);
                            imgViews.add(iv_7_4);
                            imgViews.add(iv_7_5);
                            imgViews.add(iv_7_6);
                            imgViews.add(iv_7_7);
                        } else if (list.size() == 8) {
                            view = layoutInflater.inflate(R.layout.item_9gongge_8, rl_pic, true);
                            ImageView iv_8_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_1);
                            ImageView iv_8_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_2);
                            ImageView iv_8_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_3);
                            ImageView iv_8_4 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_4);
                            ImageView iv_8_5 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_5);
                            ImageView iv_8_6 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_6);
                            ImageView iv_8_7 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_7);
                            ImageView iv_8_8 = (ImageView) view.findViewById(R.id.iv_item_9gongge_8_8);

                            imgViews.add(iv_8_1);
                            imgViews.add(iv_8_2);
                            imgViews.add(iv_8_3);
                            imgViews.add(iv_8_4);
                            imgViews.add(iv_8_5);
                            imgViews.add(iv_8_6);
                            imgViews.add(iv_8_7);
                            imgViews.add(iv_8_8);
                        } else {
                            view = layoutInflater.inflate(R.layout.item_9gongge_9, rl_pic, true);
                            ImageView iv_9_1 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_1);
                            ImageView iv_9_2 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_2);
                            ImageView iv_9_3 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_3);
                            ImageView iv_9_4 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_4);
                            ImageView iv_9_5 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_5);
                            ImageView iv_9_6 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_6);
                            ImageView iv_9_7 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_7);
                            ImageView iv_9_8 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_8);
                            ImageView iv_9_9 = (ImageView) view.findViewById(R.id.iv_item_9gongge_9_9);

                            imgViews.add(iv_9_1);
                            imgViews.add(iv_9_2);
                            imgViews.add(iv_9_3);
                            imgViews.add(iv_9_4);
                            imgViews.add(iv_9_5);
                            imgViews.add(iv_9_6);
                            imgViews.add(iv_9_7);
                            imgViews.add(iv_9_8);
                            imgViews.add(iv_9_9);vipTag.setVisibility(View.VISIBLE);
                        }

                        for (int i = 0; i < imgViews.size(); i++) {
                            final int finalI = i;
                            final ArrayList<String> finalBanner = banner;
                            imgViews.get(i).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ForumPicActivity.class);
                                    intent.putExtra("banner", finalBanner);
                                    intent.putExtra("position", finalI);
                                    startActivity(intent);
                                }
                            });
                            Glide.with(context).load(banner.get(i)).into(imgViews.get(i));
                        }
                    } else {
                        view = layoutInflater.inflate(R.layout.item_tuceng_pic, rl_pic, true);
                        HackyViewPager hvp_imgs = (HackyViewPager) view.findViewById(R.id.hvp_item_tuceng_imgs);
                        hvp_imgs.setAdapter(new ImageViewPagerAdapter_2(getActivity().getSupportFragmentManager(), banner));
                        final LinearLayout ll_inDicator = (LinearLayout) view.findViewById(R.id.ll_item_tuceng_indicator);
                        for (int i = 0; i < banner.size(); i++) {
                            ImageView inDicator = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_indicator, ll_inDicator, false);
                            if (i == 0) {
                                inDicator.setImageResource(R.mipmap.current);
                            }
                            ll_inDicator.addView(inDicator);
                        }
                        hvp_imgs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                for (int i = 0; i < ll_inDicator.getChildCount(); i++) {
                                    if (i == position) {
                                        ((ImageView) ll_inDicator.getChildAt(i)).setImageResource(R.mipmap.current);
                                    } else {
                                        ((ImageView) ll_inDicator.getChildAt(i)).setImageResource(R.mipmap.unckecked);
                                    }
                                }
                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }
                } else {
                    view = layoutInflater.inflate(R.layout.item_video, rl_pic, true);
                    ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_item_video_pic);
                    Glide.with(context).load(listBean.getAllRecords().get(0).getUrlThree()).into(iv_pic);
                    iv_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ForumVideoPlayActivity.class);
                            intent.putExtra("pic_patch", listBean.getAllRecords().get(0).getUrlThree());
                            intent.putExtra("video_patch", listBean.getAllRecords().get(0).getUrlTwo());
                            startActivity(intent);
                        }
                    });
                }
                final TextView tv_likeCount = holder.getView(R.id.tv_item_forum_likecount);
                tv_likeCount.setText(listBean.getLikeCount() + "");
                TextView tv_commentCount = holder.getView(R.id.tv_item_forum_commentcount);
                tv_commentCount.setText(listBean.getClickCount() + "");
                final ImageView iv_like = holder.getView(R.id.iv_item_forum_like);
                if (listBean.isLike()) {
                    iv_like.setImageResource(R.mipmap.praised);
                } else {
                    iv_like.setImageResource(R.mipmap.praise);
                }
                iv_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        forumService.praiseForum(listBean.getForumId(), new MyCallBack() {
                            @Override
                            public void onSuccess(Object o) {
                                int likenum = listBean.getLikeCount();
                                Log.i("likeCount", "onClick: " + listBean.getLikeCount());
                                if (listBean.isLike()) {
                                    listBean.setLike(false);
                                    iv_like.setImageResource(R.mipmap.praise);
//                            tv_likeCount.setText((likenum-1) + "赞");
                                    listBean.setLikeCount(likenum - 1);
                                } else {
                                    listBean.setLike(true);

                                    iv_like.setImageResource(R.mipmap.praised);
//                            tv_likeCount.setText((likenum+1) + "赞");
                                    listBean.setLikeCount(likenum + 1);
                                }
                                tv_likeCount.setText(listBean.getLikeCount() + "");

                            }

                            @Override
                            public void onFail(String mistakeInfo) {

                            }
                        });




                    }
                });
                Glide.with(context).load(listBean.getUser().getImgUrl()).into(diy_userimg);
                diy_userimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserInfoActivty.class);
                        intent.putExtra(USERID, listBean.getUserId());
                        startActivity(intent);
                    }
                });
                tv_nickname.setText(listBean.getUser().getNickName());
                tv_nickname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UserInfoActivty.class);
                        intent.putExtra(USERID, listBean.getUserId());
                        startActivity(intent);
                    }
                });

                if(listBean.getUser().getTrueName()!=null&&listBean.getUser().getTrueName().equals("1")){
                    vipTag.setVisibility(View.VISIBLE);
                }else{
                    vipTag.setVisibility(View.GONE);
                }
                Date createDate = null;
                //"2017-05-19 17:15:40"
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    createDate = formatter.parse(listBean.getCreatTime());
                    tv_time.setText(TimeUtils.getTimeFormatText(createDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_text.setText(listBean.getForumText());
            }
        };
        mAdapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent intent = new Intent(context, ForumDetailActivity.class);
//                intent.putExtra("forumId", forumList.get(position ).getForumId());
//                intent.putExtra("userId", forumList.get(position ).getUserId());
//                intent.putExtra("forum", forumList.get(position ));
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
                intent.putExtra("isRefreshNearbyFragment",true);

                intent.putExtra("forumId", forumOriginalList.get(position).getForumId()); //position-1?
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });



        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter1);





    }
    public void setRefreshLayout(RefreshLayout refreshLayout){
        this.refreshLayout=refreshLayout;
    }


    public void initEvent(RefreshLayout refreshLayout) {

        if(refreshLayout!=null){
            toRefresh(refreshLayout);

        }

    }

    public void toRefresh(RefreshLayout mRefreshLayout ) {

        isRefresh = true;
        doRequest(1,mRefreshLayout);
    }

    public void toLoadMore(RefreshLayout mRefreshLayout) {


        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1, mRefreshLayout);
        } else {

            mRefreshLayout.finishLoadmore();
        }
    }

    private void doRequest(int pageNum, final RefreshLayout mRefreshLayout) {



        forumService.requestPushForum(pageNum, 40, new MyCallBack() {
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

        if(isGridLayout){
            mAdapter1.notifyDataSetChanged();
        }else{
            mAdapter2.notifyDataSetChanged();
        }


    }


}
