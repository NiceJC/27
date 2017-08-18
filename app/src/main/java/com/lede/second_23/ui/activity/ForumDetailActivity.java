package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.lede.second_23.bean.CommentForForumBean;
import com.lede.second_23.bean.ForumDeleteBean;
import com.lede.second_23.bean.ForumDetailCommentBean;
import com.lede.second_23.bean.ForumDetailHeadBean;
import com.lede.second_23.bean.ForumVideoReplyBean;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.bean.ReplyForCommentBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.view.HackyViewPager;
import com.lede.second_23.utils.PushUserUtil;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ForumDetailActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int FOURM_DETAIL = 1000;
    private static final int COMMENT_FOR_FORUM = 2000;
    private static final int COMMENT_FOR_REPLY = 3000;
    private static final int DELETE_COMMENT = 4000;
    private static final int FORUM_LIKE = 5000;
    private static final int SHOW_VIDEO_REPLY = 6000;
    private static final int GET_HEAD_INFO = 7000;
    private static final int DELETE_FORUM=8000;

    @Bind(R.id.prv_forum_detail_show)
    PullToRrefreshRecyclerView prvForumDetailShow;
    @Bind(R.id.et_forum_detail_comment)
    EditText etForumDetailComment;
    @Bind(R.id.tv_forum_detail_send)
    TextView tvForumDetailSend;
    @Bind(R.id.iv_forum_detail_activity_back)
    ImageView ivForumDetailActivityBack;
    @Bind(R.id.iv_forum_detail_activity_menu)
    ImageView ivForumDetailActivityMenu;
    private CommonAdapter detailAdapter;
    private RequestQueue requestQueue;
    private int pageNum = 1;
    private int pageSize = 100;
    private long forumId;
    private int showVideoReplyPageNum = 1;
    private int showVideoReplyPageSize = 20;
    private Gson mGson;
    private Context context;
    private String commentText;
    private ArrayList<ForumDetailCommentBean.DataBean.SimpleBean.ListBean> forumDetailList = new ArrayList<>();
    private ArrayList<ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean> forumVideoReplyList = new ArrayList<>();
    private String forumuserId;
    private String replyForUserText;
    private long commentId;
    private ForumDetailHeadBean.DataBean.AllForumBean listBean;
    final String[] items1 = {"回复", "删除", "举报"};
    final String[] items2 = {"回复", "举报"};
    final String[] items3 = {"删除"};
    final String[] items4 = {"举报"};
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LayoutInflater layoutInflater;
    private boolean isOnRefreshing = false;
    private ForumDetailCommentBean forumDetailCommentBean;
    private boolean isHasNextPage = true;
    private List<ForumDetailHeadBean.DataBean.AllForumBean.AllRecordsBean> list;
    private long currentForumId;
    private CommonAdapter forumVideoReplyAdapter;
    private ForumDetailHeadBean forumDetailHeadBean;
    private DIYImageView diy_userimg;
    private TextView tv_nickname;
    private TextView tv_time;
    private TextView tv_text;
    private RelativeLayout rl_pic;
    private ImageView iv_video_reply;
    private TextView tv_likeCount;
    private TextView tv_commentCount;
    private ImageView iv_like;
    private String mUserId;
    private ArrayList<ImageView> imgViews;
    private ArrayList<String> banner;
    private int deleteType=0; //删除时候的操作类型 0删除评论 1删除微博
    private TextView tv_videocount;
    private String commentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        ButterKnife.bind(this);
        context = this;
        Intent intent = getIntent();
        forumId = intent.getLongExtra("forumId", 0);
//        forumuserId = intent.getStringExtra("userId");
//        listBean = (AllForumBean.DataBean.SimpleBean.ListBean) intent.getSerializableExtra("forum");
        mUserId = (String) SPUtils.get(context, GlobalConstants.USERID, "");
        mGson = new Gson();

        requestQueue = GlobalConstants.getRequestQueue();
        getHeadInfo();
        initView();
        initData();

    }

    private void getHeadInfo() {
        Request<String> getHeadInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showForumByUser", RequestMethod.POST);
        getHeadInfoRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        getHeadInfoRequest.add("forumId", forumId);
        requestQueue.add(GET_HEAD_INFO, getHeadInfoRequest, this);
    }

    private void initView() {
        detailAdapter = new CommonAdapter<ForumDetailCommentBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_forum_detail_1, forumDetailList) {
            @Override
            protected void convert(ViewHolder holder, final ForumDetailCommentBean.DataBean.SimpleBean.ListBean listBean, int position) {
                DIYImageView diyiv_userimg = holder.getView(R.id.diyiv_item_forum_detail1_userimg);
                TextView tv_nickname = holder.getView(R.id.tv_item_forum_detail1_nickname);
                TextView tv_time = holder.getView(R.id.tv_item_forum_detail1_time);
                TextView tv_body = holder.getView(R.id.tv_item_forum_detail1_body);
                TextView tv_showReply = holder.getView(R.id.tv_item_forum_detail1_replay);
                tv_showReply.setText("查看共" + listBean.getReplyCount() + "条回复");
                tv_showReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, AllReplyActivity.class);
                        intent.putExtra("forumuserId", forumuserId);
                        intent.putExtra("commentuserId", listBean.getUserId());
                        intent.putExtra("commentId", listBean.getCommentId());
                        intent.putExtra("forumId", forumId);
                        intent.putExtra("commentInfo", listBean);
                        startActivity(intent);
                    }
                });
                Glide.with(context).load(listBean.getUserInfo().getImgUrl()).into(diyiv_userimg);
                diyiv_userimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OtherPersonActivity.class);
                        intent.putExtra("userId", listBean.getUserId());
                        startActivity(intent);
                    }
                });
                tv_nickname.setText(listBean.getUserInfo().getNickName());
                tv_nickname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OtherPersonActivity.class);
                        intent.putExtra("userId", listBean.getUserId());
                        startActivity(intent);
                    }
                });
                tv_time.setText(listBean.getCreatTime());
                tv_body.setText(listBean.getCommentText());

            }
        };
        detailAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                commentId = forumDetailList.get(position - 1).getCommentId();
                commentUserId = forumDetailList.get(position - 1).getUserId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.add);
                builder.setTitle("选择操作");
                //    指定下拉列表的显示数据
                if (forumDetailList.get(position - 1).getUserId().equals(mUserId) || forumuserId.equals(mUserId)) {
                    if (forumDetailList.get(position - 1).getUserId().equals(mUserId)) {
                        //    设置一个下拉的列表选择项
                        builder.setItems(items3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        deleteType=0;
                                        showDeleteDialog();
                                        break;

                                }

                            }
                        });
                    } else {
                        //    设置一个下拉的列表选择项
                        builder.setItems(items1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        showReplyDialog();
                                        break;
                                    case 1:
                                        showDeleteDialog();
                                        break;
                                    case 3:
                                        Intent intent = new Intent(context, ReportActivity.class);
                                        intent.putExtra("forumId", forumId);
                                        intent.putExtra("commentId", commentId);
                                        startActivity(intent);
                                        break;
                                }

                            }
                        });
                    }

                } else {
                    builder.setItems(items2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    showReplyDialog();
                                    break;
                                case 1:
                                    Intent intent = new Intent(context, ReportActivity.class);
                                    intent.putExtra("forumId", forumId);
                                    intent.putExtra("commentId", commentId);
                                    startActivity(intent);
                                    break;
                            }

                        }
                    });

                }
                builder.show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //添加头布局
        addHeadView(detailAdapter);


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

    private void addHeadView(CommonAdapter myCommonAdapter) {
        //装饰者设计模式，把原来的adapter传入
        headerAndFooterWrapper = new HeaderAndFooterWrapper(myCommonAdapter);
        //添加头布局View对象
        headerAndFooterWrapper.addHeaderView(setHeadView());
        prvForumDetailShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvForumDetailShow.getRefreshableView().setAdapter(headerAndFooterWrapper);
        prvForumDetailShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvForumDetailShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {


            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isHasNextPage = true;
                isOnRefreshing = true;
                forumDetailList.clear();
                pageNum = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (isHasNextPage) {
                    isOnRefreshing = true;
                    pageNum++;
                    initData();
                } else {
                    Toast.makeText(context, "没有更多的评论", Toast.LENGTH_SHORT).show();
                    prvForumDetailShow.onRefreshComplete();
                }
            }
        });

    }

    private View setHeadView() {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_forum_show, null);


        diy_userimg = (DIYImageView) view.findViewById(R.id.diyiv_item_forum_userimg);
        tv_nickname = (TextView) view.findViewById(R.id.tv_item_forum_nickname);
        tv_time = (TextView) view.findViewById(R.id.tv_item_forum_time);
        tv_text = (TextView) view.findViewById(R.id.tv_item_forum_text);
        rl_pic = (RelativeLayout) view.findViewById(R.id.rl_item_forum_pic);
        tv_likeCount = (TextView) view.findViewById(R.id.tv_item_forum_likecount);
        tv_videocount = (TextView) view.findViewById(R.id.tv_item_forum_videocount);
        tv_commentCount = (TextView) view.findViewById(R.id.tv_item_forum_commentcount);

        iv_like = (ImageView) view.findViewById(R.id.iv_item_forum_like);
        iv_video_reply = (ImageView) view.findViewById(R.id.iv_item_forum_video_reply);
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

        return view;
    }

    /**
     * 显示底部视频互动弹窗
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_pop_forum_video_reply, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                500);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.7f;

        this.getWindow().setAttributes(params);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(ForumDetailActivity.this.findViewById(R.id.ll_prv_forum_detail_bottom),
                Gravity.BOTTOM, 0, 0);
        PullToRrefreshRecyclerView prv_pop_forum_video_reply = (PullToRrefreshRecyclerView) view.findViewById(R.id.prv_pop_forum_video_reply_show);
        prv_pop_forum_video_reply.getRefreshableView().setLayoutManager(new GridLayoutManager(context, 5));
        prv_pop_forum_video_reply.getRefreshableView().setAdapter(forumVideoReplyAdapter);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
                WindowManager.LayoutParams params = ForumDetailActivity.this.getWindow().getAttributes();
                params.alpha = 1.0f;

                ForumDetailActivity.this.getWindow().setAttributes(params);
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

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否删除?"); //设置内容
        builder.setIcon(R.mipmap.add);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (deleteType==0) {
                    deleteCommentOrReply();
                }else {
                    deleteForum();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();

    }

    /**
     * 删除微博
     */
    private void deleteForum() {
        Request<String> deleteForumRequest=NoHttp.createStringRequest(GlobalConstants.URL+"/allForum/updateForumByType",RequestMethod.POST);
        deleteForumRequest.add("access_token",(String) SPUtils.get(context,GlobalConstants.TOKEN,""));
        deleteForumRequest.add("forumId",forumId);
        requestQueue.add(DELETE_FORUM,deleteForumRequest,this);
    }

    /**
     * 删除评论
     */
    private void deleteCommentOrReply() {
        Request<String> deleteRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/deleteCommentOrReplyByForum", RequestMethod.POST);
        deleteRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        deleteRequest.add("forumId", forumId);
        deleteRequest.add("commentId", commentId);
        requestQueue.add(DELETE_COMMENT, deleteRequest, this);
    }

    /**
     * 显示回复对话框
     */
    private void showReplyDialog() {
        LayoutInflater factory = LayoutInflater.from(context);//提示框
        final View view = factory.inflate(R.layout.layout_reply_dialog, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.et_reply_dialog_reply);//获得输入框对象
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("回复评论")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                replyForUserText = edit.getText().toString();
                                if (replyForUserText.isEmpty()) {
                                    Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                                } else {
                                    commentForReply();
                                    dialog.dismiss();
                                }
                                //事件
                            }
                        }).setNegativeButton("取消", null).create();
        dialog.show();
    }

    private void commentForReply() {
        Request<String> commentForReplyRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/commentForReply", RequestMethod.POST);
        commentForReplyRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        commentForReplyRequest.add("forunId", forumId);
        commentForReplyRequest.add("commentId", commentId);
        commentForReplyRequest.add("replyText", replyForUserText);
        commentForReplyRequest.add("userName", (String) SPUtils.get(context, GlobalConstants.NAME, ""));
        NoHttp.getRequestQueueInstance().add(COMMENT_FOR_REPLY, commentForReplyRequest, this);
    }

    @OnClick({R.id.tv_forum_detail_send,R.id.iv_forum_detail_activity_back,R.id.iv_forum_detail_activity_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forum_detail_send:
                if (etForumDetailComment.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请输入评论", Toast.LENGTH_SHORT).show();
                } else {
                    commentText = etForumDetailComment.getText().toString();
                    commentForForum();
                }
                break;
            case R.id.iv_forum_detail_activity_back:
                finish();
                break;
            case R.id.iv_forum_detail_activity_menu:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.add);
                builder.setTitle("选择操作");
                if (forumuserId.equals((String) SPUtils.get(context, GlobalConstants.USERID,""))) {
                    //    设置一个下拉的列表选择项
                    builder.setItems(items3, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    deleteType=1;
                                    showDeleteDialog();
                                    break;

                            }

                        }
                    });
                }else {
                    builder.setItems(items4, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(context, ReportActivity.class);
                                    intent.putExtra("forumId", forumId);
                                    startActivity(intent);
                                    break;

                            }

                        }
                    });
                }
                builder.show();
                break;
        }
    }

    private void commentForForum() {
        Request<String> commentForForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/commentForForum", RequestMethod.POST);

        commentForForumRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        commentForForumRequest.add("forumId", forumId);
        commentForForumRequest.add("commentText", commentText);
        commentForForumRequest.add("forumUserId", forumuserId);
        Random random = new Random();
        long commentId = System.currentTimeMillis() * 100 + random.nextInt(100);
        commentForForumRequest.add("commentId", commentId);
        requestQueue.add(COMMENT_FOR_FORUM, commentForForumRequest, this);
    }

    /**
     * 获取第一层评论
     */
    private void initData() {
        Request<String> forumDetailRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/showCommentByForumId", RequestMethod.POST);
        forumDetailRequest.add("forumId", forumId);
        forumDetailRequest.add("pageNum", pageNum);
        forumDetailRequest.add("pageSize", pageSize);
        requestQueue.add(FOURM_DETAIL, forumDetailRequest, this);

    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("ForumDetailActivity", "onSucceed: " + response.get());
        switch (what) {
            case FOURM_DETAIL:
                parseForumDetail(response.get());
                break;
            case COMMENT_FOR_FORUM:
                parseCommentForForumJson(response.get());
                break;
            case COMMENT_FOR_REPLY:
                parseReplyForCommentJson(response.get());
                break;
            case DELETE_COMMENT:
                parseDeleteComment(response.get());
                break;
            case SHOW_VIDEO_REPLY:
                parseShowVideo(response.get());
                break;
            case GET_HEAD_INFO:
                parseDetailJson(response.get());
                break;
            case DELETE_FORUM:
                parseForumDeleteJson(response.get());
                break;
        }
    }

    /**
     * 解析删除评论
     * @param json
     */
    private void parseDeleteComment(String json) {
        MsgBean msgBean=mGson.fromJson(json,MsgBean.class);
        if (msgBean.getResult()==10000) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            pageNum=1;
            forumDetailList.clear();
            initData();
        }else {
            Toast.makeText(context, msgBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析回复评论微博成功
     * @param json
     */
    private void parseReplyForCommentJson(String json) {
        ReplyForCommentBean replyForCommentBean=mGson.fromJson(json,ReplyForCommentBean.class);
        if (replyForCommentBean.getResult()==10000) {
            Toast.makeText(context, "回复成功", Toast.LENGTH_SHORT).show();
            pageNum=1;
            forumDetailList.clear();
            initData();
//            PushUserUtil.pushUser(forumuserId,(String)SPUtils.get(context,GlobalConstants.TOKEN,""));
            PushUserUtil.pushUser(commentUserId,(String)SPUtils.get(context,GlobalConstants.TOKEN,""));
        }else {
            Toast.makeText(context, "回复失败，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析评论微博
     * @param json
     */
    private void parseCommentForForumJson(String json) {
        CommentForForumBean commentForForumBean=mGson.fromJson(json,CommentForForumBean.class);
        if (commentForForumBean.getResult()==10000) {
            Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
            pageNum=1;
            forumDetailList.clear();
            initData();
            if (!forumuserId.equals((String) SPUtils.get(context,GlobalConstants.USERID,""))) {
                PushUserUtil.pushUser(forumuserId,(String)SPUtils.get(context,GlobalConstants.TOKEN,""));

            }
            InputMethodManager imm1 = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            etForumDetailComment.setText("");

            imm1.hideSoftInputFromWindow(etForumDetailComment.getWindowToken(), 0);//从控件所在的窗口中隐藏
        }else {
            Toast.makeText(context, "评论失败，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 解析删除微博json 3335962040859
     * @param json
     */
    private void parseForumDeleteJson(String json) {
        ForumDeleteBean forumDelteBean=mGson.fromJson(json,ForumDeleteBean.class);
        if (forumDelteBean.getResult()==10000) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onFailed(int what, Response<String> response) {
        switch (what) {
            case FOURM_DETAIL:
                if (isOnRefreshing) {
                    isOnRefreshing = false;
                    prvForumDetailShow.onRefreshComplete();
                }
                break;
        }
    }

    @Override
    public void onFinish(int what) {

    }

    private void parseForumDetail(String json) {
        forumDetailCommentBean = mGson.fromJson(json, ForumDetailCommentBean.class);
        if (forumDetailCommentBean.getData().getSimple().getList().size() == 0 || forumDetailCommentBean.getData().getSimple().getList() == null) {
            Toast.makeText(context, "没有更多的评论了", Toast.LENGTH_SHORT).show();
            isHasNextPage = false;
        } else {
            forumDetailList.addAll(forumDetailCommentBean.getData().getSimple().getList());
        }

        if (isOnRefreshing) {
            isOnRefreshing = false;
            prvForumDetailShow.onRefreshComplete();
        }
        headerAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 解析视频回复
     *
     * @param json
     */
    private void parseShowVideo(String json) {
        ForumVideoReplyBean forumVideoReplyBean = mGson.fromJson(json, ForumVideoReplyBean.class);
        if (forumVideoReplyBean.getResult()==10000) {
            if (forumVideoReplyBean.getData().getSimplePageInfo().getList().size()==0) {
                Toast.makeText(context, "无视频互动", Toast.LENGTH_SHORT).show();
            }else {
                forumVideoReplyList.addAll(getList(forumVideoReplyBean.getData().getSimplePageInfo().getList()));
                forumVideoReplyAdapter.notifyDataSetChanged();
            }
        }else {
            Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析微博详情
     *
     * @param json
     */
    private void parseDetailJson(String json) {
        forumDetailHeadBean = mGson.fromJson(json, ForumDetailHeadBean.class);
        forumuserId = forumDetailHeadBean.getData().getAllForum().getUserId();
        setHeadInfo();
    }

    private void setHeadInfo() {
        rl_pic.removeAllViews();
        View childView = null;
        listBean = forumDetailHeadBean.getData().getAllForum();
        if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
            list = listBean.getAllRecords();
            imgViews = null;
            imgViews = new ArrayList<>();
            banner = null;
            banner = new ArrayList<>();
            ArrayList<ForumDetailHeadBean.DataBean.AllForumBean.AllRecordsBean> records=new ArrayList<>();
            records.addAll(listBean.getAllRecords());
            Collections.sort(records, new Comparator<ForumDetailHeadBean.DataBean.AllForumBean.AllRecordsBean>() {
                @Override
                public int compare(ForumDetailHeadBean.DataBean.AllForumBean.AllRecordsBean allRecordsBean, ForumDetailHeadBean.DataBean.AllForumBean.AllRecordsBean t1) {
                    return allRecordsBean.getRecordOrder()>t1.getRecordOrder()?1:-1;
                }
            });
            Log.i("compare", "convert: "+mGson.toJson(records));
            for (int i = 0; i < records.size(); i++) {
                banner.add(records.get(i).getUrl());
            }
            if (listBean.getAllRecords().get(0).getDspe().equals("0")) {
                if (list.size() == 1) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_1, rl_pic, true);
                    ImageView iv_1_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_1_1);
                    imgViews.clear();
                    imgViews.add(iv_1_1);
//                Glide.with(context).load(list.get(0).getUrl()).into(iv_1_1);
                } else if (list.size() == 2) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_2, rl_pic, true);
                    ImageView iv_2_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_2_1);
                    ImageView iv_2_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_2_2);
                    imgViews.clear();
                    imgViews.add(iv_2_1);
                    imgViews.add(iv_2_2);
//                Glide.with(context).load(list.get(0).getUrl()).into(iv_2_1);
//                Glide.with(context).load(list.get(1).getUrl()).into(iv_2_2);
                } else if (list.size() == 3) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_3, rl_pic, true);
                    ImageView iv_3_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_3_1);
                    ImageView iv_3_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_3_2);
                    ImageView iv_3_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_3_3);
                    imgViews.add(iv_3_1);
                    imgViews.add(iv_3_2);
                    imgViews.add(iv_3_3);
//                Glide.with(context).load(list.get(0).getUrl()).into(iv_3_1);
//                Glide.with(context).load(list.get(1).getUrl()).into(iv_3_2);
//                Glide.with(context).load(list.get(2).getUrl()).into(iv_3_3);
                } else if (list.size() == 4) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_4, rl_pic, true);
                    ImageView iv_4_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_4_1);
                    ImageView iv_4_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_4_2);
                    ImageView iv_4_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_4_3);
                    ImageView iv_4_4 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_4_4);
                    imgViews.add(iv_4_1);
                    imgViews.add(iv_4_2);
                    imgViews.add(iv_4_3);
                    imgViews.add(iv_4_4);
//                        Glide.with(context).load(list.get(0).getUrl()).into(iv_4_1);
//                        Glide.with(context).load(list.get(1).getUrl()).into(iv_4_2);
//                        Glide.with(context).load(list.get(2).getUrl()).into(iv_4_3);
//                        Glide.with(context).load(list.get(3).getUrl()).into(iv_4_4);
                } else if (list.size() == 5) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_5, rl_pic, true);
                    ImageView iv_5_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_5_1);
                    ImageView iv_5_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_5_2);
                    ImageView iv_5_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_5_3);
                    ImageView iv_5_4 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_5_4);
                    ImageView iv_5_5 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_5_5);
                    imgViews.add(iv_5_1);
                    imgViews.add(iv_5_2);
                    imgViews.add(iv_5_3);
                    imgViews.add(iv_5_4);
                    imgViews.add(iv_5_5);
//                        Glide.with(context).load(list.get(0).getUrl()).into(iv_5_1);
//                        Glide.with(context).load(list.get(1).getUrl()).into(iv_5_2);
//                        Glide.with(context).load(list.get(2).getUrl()).into(iv_5_3);
//                        Glide.with(context).load(list.get(3).getUrl()).into(iv_5_4);
//                        Glide.with(context).load(list.get(4).getUrl()).into(iv_5_5);
                } else if (list.size() == 6) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_6, rl_pic, true);
                    ImageView iv_6_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_6_1);
                    ImageView iv_6_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_6_2);
                    ImageView iv_6_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_6_3);
                    ImageView iv_6_4 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_6_4);
                    ImageView iv_6_5 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_6_5);
                    ImageView iv_6_6 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_6_6);
                    imgViews.add(iv_6_1);
                    imgViews.add(iv_6_2);
                    imgViews.add(iv_6_3);
                    imgViews.add(iv_6_4);
                    imgViews.add(iv_6_5);
                    imgViews.add(iv_6_6);
//                        Glide.with(context).load(list.get(0).getUrl()).into(iv_6_1);
//                        Glide.with(context).load(list.get(1).getUrl()).into(iv_6_2);
//                        Glide.with(context).load(list.get(2).getUrl()).into(iv_6_3);
//                        Glide.with(context).load(list.get(3).getUrl()).into(iv_6_4);
//                        Glide.with(context).load(list.get(4).getUrl()).into(iv_6_5);
//
//                        Glide.with(context).load(list.get(5).getUrl()).into(iv_6_6);
                } else if (list.size() == 7) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_7, rl_pic, true);
                    ImageView iv_7_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_1);
                    ImageView iv_7_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_2);
                    ImageView iv_7_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_3);
                    ImageView iv_7_4 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_4);
                    ImageView iv_7_5 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_5);
                    ImageView iv_7_6 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_6);
                    ImageView iv_7_7 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_7_7);
                    imgViews.add(iv_7_1);
                    imgViews.add(iv_7_2);
                    imgViews.add(iv_7_3);
                    imgViews.add(iv_7_4);
                    imgViews.add(iv_7_5);
                    imgViews.add(iv_7_6);
                    imgViews.add(iv_7_7);

//                        Glide.with(context).load(list.get(0).getUrl()).into(iv_7_1);
//                        Glide.with(context).load(list.get(1).getUrl()).into(iv_7_2);
//                        Glide.with(context).load(list.get(2).getUrl()).into(iv_7_3);
//                        Glide.with(context).load(list.get(3).getUrl()).into(iv_7_4);
//                        Glide.with(context).load(list.get(4).getUrl()).into(iv_7_5);
//                        Glide.with(context).load(list.get(5).getUrl()).into(iv_7_6);
//                        Glide.with(context).load(list.get(6).getUrl()).into(iv_7_7);
                } else if (list.size() == 8) {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_8, rl_pic, true);
                    ImageView iv_8_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_1);
                    ImageView iv_8_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_2);
                    ImageView iv_8_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_3);
                    ImageView iv_8_4 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_4);
                    ImageView iv_8_5 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_5);
                    ImageView iv_8_6 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_6);
                    ImageView iv_8_7 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_7);
                    ImageView iv_8_8 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_8_8);

                    imgViews.add(iv_8_1);
                    imgViews.add(iv_8_2);
                    imgViews.add(iv_8_3);
                    imgViews.add(iv_8_4);
                    imgViews.add(iv_8_5);
                    imgViews.add(iv_8_6);
                    imgViews.add(iv_8_7);
                    imgViews.add(iv_8_8);

//                        Glide.with(context).load(list.get(0).getUrl()).into(iv_8_1);
//                        Glide.with(context).load(list.get(1).getUrl()).into(iv_8_2);
//                        Glide.with(context).load(list.get(2).getUrl()).into(iv_8_3);
//                        Glide.with(context).load(list.get(3).getUrl()).into(iv_8_4);
//                        Glide.with(context).load(list.get(4).getUrl()).into(iv_8_5);
//                        Glide.with(context).load(list.get(5).getUrl()).into(iv_8_6);
//                        Glide.with(context).load(list.get(6).getUrl()).into(iv_8_7);
//                        Glide.with(context).load(list.get(7).getUrl()).into(iv_8_8);
                } else {
                    childView = layoutInflater.inflate(R.layout.item_9gongge_9, rl_pic, true);
                    ImageView iv_9_1 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_1);
                    ImageView iv_9_2 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_2);
                    ImageView iv_9_3 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_3);
                    ImageView iv_9_4 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_4);
                    ImageView iv_9_5 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_5);
                    ImageView iv_9_6 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_6);
                    ImageView iv_9_7 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_7);
                    ImageView iv_9_8 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_8);
                    ImageView iv_9_9 = (ImageView) childView.findViewById(R.id.iv_item_9gongge_9_9);
                    imgViews.add(iv_9_1);
                    imgViews.add(iv_9_2);
                    imgViews.add(iv_9_3);
                    imgViews.add(iv_9_4);
                    imgViews.add(iv_9_5);
                    imgViews.add(iv_9_6);
                    imgViews.add(iv_9_7);
                    imgViews.add(iv_9_8);
                    imgViews.add(iv_9_9);

//                        Glide.with(context).load(list.get(0).getUrl()).into(iv_9_1);
//                        Glide.with(context).load(list.get(1).getUrl()).into(iv_9_2);
//                        Glide.with(context).load(list.get(2).getUrl()).into(iv_9_3);
//                        Glide.with(context).load(list.get(3).getUrl()).into(iv_9_4);
//                        Glide.with(context).load(list.get(4).getUrl()).into(iv_9_5);
//                        Glide.with(context).load(list.get(5).getUrl()).into(iv_9_6);
//                        Glide.with(context).load(list.get(6).getUrl()).into(iv_9_7);
//                        Glide.with(context).load(list.get(7).getUrl()).into(iv_9_8);
//                        Glide.with(context).load(list.get(8).getUrl()).into(iv_9_9);

                }
                for (int i = 0; i < imgViews.size(); i++) {
                    Glide.with(context).load(banner.get(i)).into(imgViews.get(i));
                    final int finalI = i;
                    final ArrayList<String> finalBanner = banner;
                    imgViews.get(i).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
//                                banner=new ArrayList<>();
//                                for (int i = 0; i < listBean.getAllRecords().size(); i++) {
//                                    banner.add(listBean.getAllRecords().get(i).getUrl());
//                                }
                            Intent intent = new Intent(context, ForumPicActivity.class);
                            intent.putExtra("banner", finalBanner);
                            intent.putExtra("position", finalI);
                            startActivity(intent);
                        }
                    });

                }
            } else {
                childView = layoutInflater.inflate(R.layout.item_tuceng_pic, rl_pic, true);
                HackyViewPager hvp_imgs = (HackyViewPager) childView.findViewById(R.id.hvp_item_tuceng_imgs);
                hvp_imgs.setAdapter(new ImageViewPagerAdapter_2(getSupportFragmentManager(), banner));
                final LinearLayout ll_inDicator = (LinearLayout) childView.findViewById(R.id.ll_item_tuceng_indicator);
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
            childView = layoutInflater.inflate(R.layout.item_video, rl_pic, true);
            ImageView iv_pic = (ImageView) childView.findViewById(R.id.iv_item_video_pic);
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

        tv_videocount.setText(listBean.getVideoCount()+"");
        tv_commentCount.setText(listBean.getClickCount() + "");
        tv_likeCount.setText(listBean.getLikeCount() + "");
        if (listBean.isLike()) {
            iv_like.setImageResource(R.mipmap.praised);
        } else {
            iv_like.setImageResource(R.mipmap.praise);
        }
        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forumLikeRequest(listBean.getForumId());
                int likenum = listBean.getLikeCount();
                if (listBean.isLike()) {
                    listBean.setLike(false);
                    iv_like.setImageResource(R.mipmap.praise);
                    listBean.setLikeCount(likenum - 1);
                } else {
                    listBean.setLike(true);
                    iv_like.setImageResource(R.mipmap.praised);
                    listBean.setLikeCount(likenum + 1);
                }
                tv_likeCount.setText(listBean.getLikeCount() + "");
            }
        });
        Glide.with(context).load(listBean.getUser().getImgUrl()).into(diy_userimg);
        diy_userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherPersonActivity.class);
                intent.putExtra("userId", listBean.getUserId());
                startActivity(intent);
            }
        });
        tv_nickname.setText(listBean.getUser().getNickName());
        tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherPersonActivity.class);
                intent.putExtra("userId", listBean.getUserId());
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


    /**
     * 去重
     * @param arr
     * @return
     */
    private ArrayList getList(List arr) {
        List list = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()) {
            Object obj = (Object) it.next();
            if(!list.contains(obj)){                //不包含就添加
                list.add(obj);
            }
        }
        return (ArrayList) list;

    }

}
