package com.lede.second_23.ui.fragment;

import android.animation.ObjectAnimator;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.adapter.ImageViewPagerAdapter_2;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.bean.ForumVideoReplyBean;
import com.lede.second_23.bean.PushUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.AllIssueActivity;
import com.lede.second_23.ui.activity.ForumDetailActivity;
import com.lede.second_23.ui.activity.ForumPicActivity;
import com.lede.second_23.ui.activity.ForumReplyVideoPlayActivity;
import com.lede.second_23.ui.activity.ForumVideoPlayActivity;
import com.lede.second_23.ui.activity.ForumVideoReplyActivity;
import com.lede.second_23.ui.activity.MainActivity;
import com.lede.second_23.ui.activity.SearchingActivity;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.ui.view.HackyViewPager;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.TimeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.R.id.prv_forum_activity_show;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/8/17.
 */

public class ForumFragment extends Fragment implements OnResponseListener<String> {
    private static final String TAG = "ForumFragment";
    private static final int FORUM_CODE = 1000;
    private static final int FORUM_LIKE = 2000;
    private static final int PUSH_USER = 3000;
    private static final int SHOW_VIDEO_REPLY = 4000;

    @Bind(R.id.iv_forum_activity_back)
    ImageView ivForumActivityBack;
    @Bind(R.id.tv_forum_activity_nonview)
    TextView tvForumActivityNonview;
    @Bind(prv_forum_activity_show)
    PullToRrefreshRecyclerView prvForumActivityShow;
    @Bind(R.id.iv_forum_activity_send)
    ImageView ivForumActivitySend;
    @Bind(R.id.rl_forum_activity_bottom)
    RelativeLayout rlForumActivityBottom;
    @Bind(R.id.iv_forum_title)
    ImageView ivForumTitle;
    @Bind(R.id.iv_forum_activity_search)
    ImageView search;

    private RequestQueue requestQueue;
    private Context context;
    private int pageNum = 1;
    private int pageSize = 20;
    private int showVideoReplyPageNum = 1;
    private int showVideoReplyPageSize = 20;
    private CommonAdapter headAdapter;
    private CommonAdapter forumAdapter;
    private CommonAdapter forumVideoReplyAdapter;
    private ArrayList<AllForumBean.DataBean.SimpleBean.ListBean> forumList = new ArrayList<>();
    private ArrayList<PushUserBean.DataBean.UserInfoListBean> pushUserList = new ArrayList<>();
    private ArrayList<ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean> forumVideoReplyList = new ArrayList<>();
    private Gson mGson;
    private boolean isshowBottom = true;
    private ObjectAnimator animator;
    private boolean isOnRefreshing = false;
    private AllForumBean allforumBean;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LayoutInflater layoutInflater;
    private List<AllForumBean.DataBean.SimpleBean.ListBean.AllRecordsBean> list;
    private int width;
    private long currentForumId;
    private ArrayList<ImageView> imgViews;
    private ArrayList<String> banner;
    private boolean isShowPopwindow = false;
    private boolean isShow27=false;
    private LinearLayoutManager linearLayoutManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_forum, container, false);
        ButterKnife.bind(this, view);
        linearLayoutManager = new LinearLayoutManager(context);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGson = new Gson();
        forumVideoReplyList.add(null);
        layoutInflater = LayoutInflater.from(context);
        requestQueue = GlobalConstants.getRequestQueue();
        initData();
//        initHead();
        initView();
    }

//    private void initHead() {
//        Request<String> headRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/push/newPushUser", RequestMethod.POST);
//        headRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
//        requestQueue.add(PUSH_USER, headRequest, this);
//    }

    private void initView() {
        forumAdapter = new CommonAdapter<AllForumBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_forum_show, forumList) {


            @Override
            protected void convert(ViewHolder holder, final AllForumBean.DataBean.SimpleBean.ListBean listBean, final int position) {
                DIYImageView diy_userimg = holder.getView(R.id.diyiv_item_forum_userimg);
                TextView tv_nickname = holder.getView(R.id.tv_item_forum_nickname);
//
//                if (listBean.getUser().getTrueName().equals("1")) {
//                    Drawable drawableRight = getResources().getDrawable(
//                            R.mipmap.v5);
//
//                    tv_nickname.setCompoundDrawablesWithIntrinsicBounds(null,
//                            null, drawableRight, null);
//                    tv_nickname.setCompoundDrawablePadding(2);
//                }

                TextView tv_time = holder.getView(R.id.tv_item_forum_time);
                TextView tv_text = holder.getView(R.id.tv_item_forum_text);
                RelativeLayout rl_pic = holder.getView(R.id.rl_item_forum_pic);
                ImageView iv_video_reply = holder.getView(R.id.iv_item_forum_video_reply);
                TextView iv_video_count = holder.getView(R.id.tv_item_forum_videocount);
                iv_video_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forumVideoReplyList.clear();
                        forumVideoReplyList.add(null);
                        showVideoByUserList(listBean.getForumId());
                        currentForumId = listBean.getForumId();
                        showPopwindow();

                    }
                });
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
                            imgViews.add(iv_9_9);
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
                iv_video_count.setText(listBean.getVideoCount() + "");
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
                        forumLikeRequest(listBean.getForumId());
//                        forumLikePosition=position;
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
                        Log.i("likeCount", "onClicked: " + listBean.getLikeCount());

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
        forumAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(context, ForumDetailActivity.class);
                intent.putExtra("forumId", forumList.get(position - 1).getForumId());
                intent.putExtra("userId", forumList.get(position - 1).getUserId());
                intent.putExtra("forum", forumList.get(position - 1));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        prvForumActivityShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvForumActivityShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isOnRefreshing = true;
                pushUserList.clear();
                forumList.clear();
                pageNum = 1;
                initData();
//                initHead();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (allforumBean.getData().getSimple().isHasNextPage()) {
                    isOnRefreshing = true;
                    pageNum++;
                    initData();

                } else {
                    Toast.makeText(context, "没有更多的动态了", Toast.LENGTH_SHORT).show();
                    prvForumActivityShow.onRefreshComplete();
                }
            }
        });
        prvForumActivityShow.getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition()==0) {
                    ivForumTitle.setImageResource(R.mipmap.forum_discover);
                }else {
                    ivForumTitle.setImageResource(R.mipmap.forum_27);
                }
                if (dy >= 30) {
                    if (isshowBottom) {
                        return;
                    } else {
                        isshowBottom = true;
                        Log.i("onScrolled", "onScrolled: 向下滑动");
                        bottom_show(0);
                    }

                } else if (dy <= -30) {
                    if (isshowBottom) {
                        isshowBottom = false;
                        Log.i("onScrolled", "onScrolled: 向上滑动");
                        bottom_show(1);
                    } else {
                        return;
                    }
                }
            }
        });

        //添加头布局
        addHeadView(forumAdapter);

        //底部弹出视频回复的adapter
        forumVideoReplyAdapter = new CommonAdapter<ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean>(context, R.layout.item_pop_video_reply, forumVideoReplyList) {
            @Override
            protected void convert(ViewHolder holder, ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean listBean, int position) {
                DIYImageView div_usrimg = holder.getView(R.id.diyiv_item_pop_video_reply_item);
                if (position == 0) {
                    Glide.with(context).load(R.mipmap.pai).into(div_usrimg);
                } else {
                    Glide.with(context).load(listBean.getUserInfo().getImgUrl()).into(div_usrimg);
                }
            }
        };
        forumVideoReplyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(context, ForumVideoReplyActivity.class);
                    intent.putExtra("forumId", currentForumId);
                    startActivity(intent);
                } else {
                    intent = new Intent(context, ForumReplyVideoPlayActivity.class);
                    intent.putExtra("list", forumVideoReplyList);
                    intent.putExtra("position", position - 1);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 视频根据forumId查询用户列表
     *
     * @param forumId
     */
    private void showVideoByUserList(long forumId) {
        Request<String> videoReplyRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/VideoReply/showVideoByUserList", RequestMethod.POST);
        videoReplyRequest.add("forumId", forumId);
        videoReplyRequest.add("pageNum", showVideoReplyPageNum);
        videoReplyRequest.add("pageSize", showVideoReplyPageSize);
        requestQueue.add(SHOW_VIDEO_REPLY, videoReplyRequest, this);
    }

    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_pop_forum_video_reply, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        isShowPopwindow = true;
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                500);
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;

        getActivity().getWindow().setAttributes(params);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(prvForumActivityShow,
                Gravity.BOTTOM, 0, 0);
        PullToRrefreshRecyclerView prv_pop_forum_video_reply = (PullToRrefreshRecyclerView) view.findViewById(R.id.prv_pop_forum_video_reply_show);
        prv_pop_forum_video_reply.getRefreshableView().setLayoutManager(new GridLayoutManager(context, 5));
        prv_pop_forum_video_reply.getRefreshableView().setAdapter(forumVideoReplyAdapter);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                isShowPopwindow = false;
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1.0f;

                getActivity().getWindow().setAttributes(params);
            }
        });

    }


    private void addHeadView(CommonAdapter myCommonAdapter) {
        //装饰者设计模式，把原来的adapter传入
        headerAndFooterWrapper = new HeaderAndFooterWrapper(myCommonAdapter);
        //添加头布局View对象
//        headerAndFooterWrapper.addHeaderView(setHeadView());

        prvForumActivityShow.getRefreshableView().setLayoutManager(linearLayoutManager);
        prvForumActivityShow.getRefreshableView().setAdapter(headerAndFooterWrapper);
    }

//    /**
//     * 初始化头视图
//     *
//     * @return
//     */
//    private View setHeadView() {
//        layoutInflater = LayoutInflater.from(context);
//        View view = layoutInflater.inflate(R.layout.layout_all_forum_head, null);
//        RecyclerView rv_head_show = (RecyclerView) view.findViewById(R.id.rv_all_forum_head_show);
//        headAdapter = new CommonAdapter<PushUserBean.DataBean.UserInfoListBean>(context, R.layout.item_all_forum_head, pushUserList) {
//
//            @Override
//            protected void convert(ViewHolder holder, PushUserBean.DataBean.UserInfoListBean userInfosBean, int position) {
//                DIYImageView diyiv_userimg = holder.getView(R.id.diyiv_all_forum_head_userimg);
//                TextView tv_nickname = holder.getView(R.id.tv_all_forum_head_nickname);
//                LinearLayout ll_bg = holder.getView(R.id.ll_all_forum_head_bg);
//                Glide.with(context)
//                        .load(userInfosBean.getImgUrl())
//                        .into(diyiv_userimg);
//                tv_nickname.setText(userInfosBean.getNickName());
////                if (position == 0 || position == 3 || position == 6 || position == 9) {
////                    ll_bg.setBackgroundResource(R.drawable.shape_linearlayout_forum_head_item1);
////                } else if (position == 1 || position == 4 || position == 7) {
////                    ll_bg.setBackgroundResource(R.drawable.shape_linearlayout_forum_head_item2);
////                } else {
////                    ll_bg.setBackgroundResource(R.drawable.shape_linearlayout_forum_head_item3);
////                }
//            }
//        };
//        headAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent intent = new Intent(context, UserInfoActivty.class);
//                intent.putExtra(USERID, pushUserList.get(position).getUserId());
//                startActivity(intent);
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });
//        rv_head_show.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        rv_head_show.setAdapter(headAdapter);
//        return view;
//    }

    /**
     * 全国微博点赞
     *
     * @param forumId
     */
    private void forumLikeRequest(long forumId) {
        Request<String> forumLikeRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/clickLike", RequestMethod.POST);
        forumLikeRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        forumLikeRequest.add("forumId", forumId);
        requestQueue.add(FORUM_LIKE, forumLikeRequest, this);
    }

    /**
     * 显示底部发布
     *
     * @param i
     */
    private void bottom_show(int i) {
        animator = null;
        if (i == 0) {
            animator = ObjectAnimator.ofFloat(rlForumActivityBottom, "translationY", 0, rlForumActivityBottom.getHeight());
            animator.setDuration(500);
            animator.start();
        } else {
            animator = ObjectAnimator.ofFloat(rlForumActivityBottom, "translationY", rlForumActivityBottom.getHeight(), 0);
            animator.setDuration(500);
            animator.start();
        }
    }

    private void initData() {
        Request<String> forumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showAllForum", RequestMethod.POST);
        forumRequest.add("pageNum", pageNum);
        forumRequest.add("pageSize", pageSize);
        forumRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        requestQueue.add(FORUM_CODE, forumRequest, this);
    }

    @OnClick({R.id.iv_forum_activity_send, R.id.iv_forum_activity_back,R.id.iv_forum_activity_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_forum_activity_send:
                Intent intent = new Intent(context, AllIssueActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.iv_forum_activity_back:
                MainActivity.instance.vp_main_fg.setCurrentItem(1);
//                finish();
                break;
            case R.id.iv_forum_activity_search:
                Intent intent1=new Intent(context, SearchingActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("FORUM_CODE", "onSucceed: " + response.get());
        switch (what) {
            case FORUM_CODE:
                parseForumJson(response.get());
                break;
            case FORUM_LIKE:
                parseForumLike(response.get());

                break;
            case PUSH_USER:

                parsePushUser(response.get());
                break;
            case SHOW_VIDEO_REPLY:
                parseShowVideo(response.get());
                break;
        }
    }


    @Override
    public void onFailed(int what, Response<String> response) {
        switch (what) {
            case FORUM_CODE:
                if (isOnRefreshing) {
                    prvForumActivityShow.onRefreshComplete();
                    isOnRefreshing = false;
                }

                break;
        }
    }

    @Override
    public void onFinish(int what) {

    }

    /**
     * 解析视频回复
     *
     * @param json
     */
    private void parseShowVideo(String json) {
        ForumVideoReplyBean forumVideoReplyBean = mGson.fromJson(json, ForumVideoReplyBean.class);
        if (forumVideoReplyBean.getResult() == 10000) {
            if (forumVideoReplyBean.getData().getSimplePageInfo().getList().size() == 0) {
                Toast.makeText(context, "无视频互动", Toast.LENGTH_SHORT).show();
            } else {
                forumVideoReplyList.addAll(getList(forumVideoReplyBean.getData().getSimplePageInfo().getList()));
                forumVideoReplyAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parsePushUser(String json) {
        PushUserBean pushUserBean = mGson.fromJson(json, PushUserBean.class);
        pushUserList.addAll(pushUserBean.getData().getUserInfoList());
        headAdapter.notifyDataSetChanged();
    }

    /**
     * 解析全国forum
     *
     * @param json
     */
    private void parseForumJson(String json) {
        allforumBean = mGson.fromJson(json, AllForumBean.class);
        if (isOnRefreshing) {
            prvForumActivityShow.onRefreshComplete();
            isOnRefreshing = false;
        }
        tvForumActivityNonview.setVisibility(View.GONE);
        if (allforumBean.getData().getSimple().getList().size() == 0) {
            if (pageNum == 1) {
                Toast.makeText(context, "还没有人发布过圈子，快来做第一人哟", Toast.LENGTH_SHORT).show();
                tvForumActivityNonview.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(context, "没有更多的圈子了", Toast.LENGTH_SHORT).show();

            }
        } else {
            forumList.addAll(allforumBean.getData().getSimple().getList());
            headerAndFooterWrapper.notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: " + TAG);
        super.onResume();
        if (isShowPopwindow) {
            forumVideoReplyList.clear();
            forumVideoReplyList.add(null);
            showVideoByUserList(currentForumId);

        }

    }

    /**
     * 解析点赞数据
     *
     * @param json
     */
    private void parseForumLike(String json) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 去重
     *
     * @param arr
     * @return
     */
    private ArrayList getList(List arr) {
        List list = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()) {
            Object obj = (Object) it.next();
            if (!list.contains(obj)) {                //不包含就添加
                list.add(obj);
            }
        }
        return (ArrayList) list;

    }
}
