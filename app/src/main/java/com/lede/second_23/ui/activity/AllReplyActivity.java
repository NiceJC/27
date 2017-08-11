package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.ForumDetailCommentBean;
import com.lede.second_23.bean.ReplyBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.view.TextViewFixTouchConsume;
import com.lede.second_23.utils.NoLineCllikcSpan;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllReplyActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int REPLY_REQUEST = 1000;
    private static final int COMMENT_FOR_REPLY = 2000;
    private static final int REPLY_FOR_USER = 3000;
    private static final int DELETE_REPLY = 4000;

    @Bind(R.id.prv_all_reply_show)
    PullToRrefreshRecyclerView prvAllReplyShow;
    @Bind(R.id.et_all_reply_reply)
    EditText etAllReplyReply;
    @Bind(R.id.tv_all_reply_send)
    TextView tvAllReplySend;
    private RequestQueue requestQueue;
    private ArrayList<ReplyBean.DataBean.SimpleBean.ListBean> replyList = new ArrayList<>();
    private Gson mGson;
    private long commentId;
    private int pageNum = 1;
    private int pageSize = 20;
    private CommonAdapter replyAdapter;
    private Context context;
    private String replyStr = " 回复 ";
    private String replyText;
    private String replyForUserText;
    private long forumId;
    private String toUserId;
    private String touserName;
    private Intent intent;
    private String forumuserId;
    private String commentuserId;
    private String currentUserId;
    private int replyId;
    //    指定下拉列表的显示数据
    final String[] items1 = {"回复","举报", "删除"};
    final String[] items2 = {"回复","举报"};
    final String[] items3={"删除"};
    private ForumDetailCommentBean.DataBean.SimpleBean.ListBean listBean;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LayoutInflater layoutInflater;
    private boolean isOnRereshing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reply);
        ButterKnife.bind(this);
        context = this;
        requestQueue = GlobalConstants.getRequestQueue();
//        intent.putExtra("forumuserId",forumuserId);
//        intent.putExtra("commentuserId",listBean.getUserId());

        intent = getIntent();
        forumuserId = intent.getStringExtra("forumuserId");
        commentuserId = intent.getStringExtra("commentuserId");
        commentId = intent.getLongExtra("commentId", 0);
        forumId = intent.getLongExtra("forumId", 0);
        listBean = (ForumDetailCommentBean.DataBean.SimpleBean.ListBean) intent.getSerializableExtra("commentInfo");
        currentUserId = (String) SPUtils.get(context, GlobalConstants.USERID, "");
        mGson = new Gson();
        initView();
        initData();


    }

    @OnClick({R.id.tv_all_reply_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_reply_send:
                if (etAllReplyReply.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请输入评论", Toast.LENGTH_SHORT).show();
                } else {
                    replyText = etAllReplyReply.getText().toString();
                    commentForReply();
                }
                break;

        }
    }

    private void commentForReply() {
        Request<String> commentForReplyRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/commentForReply", RequestMethod.POST);
        commentForReplyRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        commentForReplyRequest.add("forunId", forumId);
        commentForReplyRequest.add("commentId", commentId);
        commentForReplyRequest.add("replyText", replyText);
        commentForReplyRequest.add("userName", (String) SPUtils.get(context, GlobalConstants.NAME, ""));
        NoHttp.getRequestQueueInstance().add(COMMENT_FOR_REPLY, commentForReplyRequest, this);
    }

    private void initView() {
        replyAdapter = new CommonAdapter<ReplyBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_reply, replyList) {
            @Override
            protected void convert(ViewHolder holder, final ReplyBean.DataBean.SimpleBean.ListBean listBean, int position) {
                TextViewFixTouchConsume tv_reply = holder.getView(R.id.tv_item_reply_reply);
                if (listBean.getToUserName() == null) {
                    SpannableString sbs = new SpannableString(listBean.getUserInfo().getNickName() + ":" + listBean.getReplyText());
                    sbs.setSpan(new NoLineCllikcSpan() {
                        @Override
                        public void onClick(View widget) {
                            Intent intent=new Intent(context, ConcernActivity_2.class);
                            intent.putExtra("userId",listBean.getUserId());
                            startActivity(intent);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#2b4ed2")); //设置颜色
                        }
                    }, 0, listBean.getUserInfo().getNickName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_reply.setText(sbs);
                    tv_reply.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
//                    tv_reply.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起

                } else {

                    SpannableString sbs = new SpannableString(listBean.getUserInfo().getNickName() + replyStr + listBean.getToUserName() + ":" + listBean.getReplyText());
                    sbs.setSpan(new NoLineCllikcSpan() {
                        @Override
                        public void onClick(View widget) {
                            Intent intent=new Intent(context, ConcernActivity_2.class);
                            intent.putExtra("userId",listBean.getUserId());
                            startActivity(intent);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#2b4ed2")); //设置颜色
                        }
                    }, 0, listBean.getUserInfo().getNickName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sbs.setSpan(new NoLineCllikcSpan() {
                        @Override
                        public void onClick(View widget) {
                            Intent intent=new Intent(context, ConcernActivity_2.class);
                            intent.putExtra("userId",listBean.getToUserId());
                            startActivity(intent);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#2b4ed2")); //设置颜色
                        }
                    }, listBean.getUserInfo().getNickName().length() + replyStr.length(), listBean.getUserInfo().getNickName().length() + replyStr.length() + listBean.getToUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tv_reply.setText(sbs);
                    tv_reply.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());

                }

            }
        };

        replyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                toUserId = replyList.get(position - 1).getUserId();
                touserName = replyList.get(position - 1).getUserInfo().getNickName();
                replyId = replyList.get(position - 1).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.add);
                builder.setTitle("选择操作");
                Log.i("shanchu", "onClick: commentuserId"+commentuserId+"    forumuserId"+forumuserId+"     listBean.getUserId()"+replyList.get(position-1).getUserId()+"   "+currentUserId+"这里应该只显示删除");

                if (commentuserId.equals(currentUserId) || forumuserId.equals(currentUserId) || replyList.get(position-1).getUserId().equals(currentUserId)) {
                    if (replyList.get(position-1).getUserId().equals(currentUserId)) {
                        Log.i("shanchu", "onClick: "+listBean.getUserId()+"   "+currentUserId+"这里应该只显示删除");
                        builder.setItems(items3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case 0:
                                        showDeleteDialog();
                                        break;
                                }

                            }
                        });
                    }else {
                        builder.setItems(items1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case 0:
                                        showReplyDialog();
                                        break;
                                    case 1:
                                        Intent intent=new Intent(context,ReportActivity.class);
                                        intent.putExtra("forumId",forumId);
                                        intent.putExtra("commentId",commentId);
                                        intent.putExtra("replyId",listBean.getId());
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        showDeleteDialog();
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
                                    Intent intent=new Intent(context,ReportActivity.class);
                                    intent.putExtra("forumId",forumId);
                                    intent.putExtra("commentId",commentId);
                                    intent.putExtra("replyId",listBean.getId());
                                    startActivity(intent);
                                    break;
                            }

                        }
                    });
                }


                builder.show();
//
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //添加头布局
        addHeadView(replyAdapter);
    }

    /**
     * 添加头布局
     *
     * @param myCommonAdapter
     */
    private void addHeadView(CommonAdapter myCommonAdapter) {
        //装饰者设计模式，把原来的adapter传入
        headerAndFooterWrapper = new HeaderAndFooterWrapper(myCommonAdapter);
        //添加头布局View对象
        headerAndFooterWrapper.addHeaderView(setHeadView());
        prvAllReplyShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvAllReplyShow.getRefreshableView().setAdapter(headerAndFooterWrapper);
        prvAllReplyShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvAllReplyShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageNum=1;
                replyList.clear();
                initData();
                isOnRereshing=true;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageNum++;
                initData();
                isOnRereshing=true;
            }
        });
    }

    /**
     * 初始化头布局
     *
     * @return
     */
    private View setHeadView() {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_forum_detail_1, null);

        DIYImageView diyiv_userimg = (DIYImageView) view.findViewById(R.id.diyiv_item_forum_detail1_userimg);
        TextView tv_nickname = (TextView) view.findViewById(R.id.tv_item_forum_detail1_nickname);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_item_forum_detail1_time);
        TextView tv_body = (TextView) view.findViewById(R.id.tv_item_forum_detail1_body);
        TextView tv_showReply = (TextView) view.findViewById(R.id.tv_item_forum_detail1_replay);
        tv_showReply.setVisibility(View.GONE);
        Glide.with(context).load(listBean.getUserInfo().getImgUrl()).into(diyiv_userimg);
        tv_nickname.setText(listBean.getUserInfo().getNickName());
//        tv_time.setText(listBean.getCreatTime());
        Date createDate=null;
        //"2017-05-19 17:15:40"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            createDate=formatter.parse(listBean.getCreatTime());
            tv_time.setText(TimeUtils.getTimeFormatText(createDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_body.setText(listBean.getCommentText());

        return view;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否删除?"); //设置内容
        builder.setIcon(R.mipmap.add);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCommentOrReply();
                Toast.makeText(context, "删除成功,刷新后将无法看到该条评论", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(context, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    private void deleteCommentOrReply() {
        Request<String> deleteRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/deleteCommentOrReplyByForum", RequestMethod.POST);
        deleteRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        deleteRequest.add("forumId", forumId);
        deleteRequest.add("commentId", commentId);
        deleteRequest.add("replyId", replyId);
        requestQueue.add(DELETE_REPLY, deleteRequest, this);
    }

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
                                    replyForUser();
                                    dialog.dismiss();
                                }
                                //事件
                            }
                        }).setNegativeButton("取消", null).create();
        dialog.show();
    }

    private void replyForUser() {
        Request<String> replyForUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/replyForUser", RequestMethod.POST);
        replyForUserRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        replyForUserRequest.add("forunId", forumId);
        replyForUserRequest.add("commentId", commentId);
        replyForUserRequest.add("replyText", replyForUserText);
        replyForUserRequest.add("toUserId", toUserId);
        replyForUserRequest.add("toUserName", touserName);
        NoHttp.getRequestQueueInstance().add(REPLY_FOR_USER, replyForUserRequest, this);

    }

    private void initData() {
        Request<String> replyRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/comment/showReplyByCommentId", RequestMethod.POST);
        replyRequest.add("commentId", commentId);
        replyRequest.add("pageNum", pageNum);
        replyRequest.add("pageSize", pageSize);
        requestQueue.add(REPLY_REQUEST, replyRequest, this);

    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("AllReplyActivity", "onSucceed: " + response.get());
        switch (what) {
            case REPLY_REQUEST:
                parseReply(response.get());
                break;
        }
    }


    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    private void parseReply(String json) {
        ReplyBean replyBean = mGson.fromJson(json, ReplyBean.class);
        if (isOnRereshing) {
            prvAllReplyShow.onRefreshComplete();
            isOnRereshing=false;
        }
        if (replyBean.getData().getSimple().getList().size()==0) {
            Toast.makeText(context, "没有更多的回复了", Toast.LENGTH_SHORT).show();
        }else {
            replyList.addAll(replyBean.getData().getSimple().getList());

        }
        headerAndFooterWrapper.notifyDataSetChanged();
    }
}
