package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.bean.ForumDetailBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.SPUtils;
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

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForumDetailActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int FOURM_DETAIL = 1000;
    private static final int COMMENT_FOR_FORUM = 2000;
    private static final int COMMENT_FOR_REPLY = 3000;
    private static final int DELETE_COMMENT = 4000;

    @Bind(R.id.prv_forum_detail_show)
    PullToRrefreshRecyclerView prvForumDetailShow;
    @Bind(R.id.et_forum_detail_comment)
    EditText etForumDetailComment;
    @Bind(R.id.tv_forum_detail_send)
    TextView tvForumDetailSend;
    private CommonAdapter detailAdapter;
    private RequestQueue requestQueue;
    private int pageNum = 1;
    private int pageSize = 20;
    private long forumId;
    private String forumUserId;
    private Gson mGson;
    private Context context;
    private String commentText;
    private ArrayList<ForumDetailBean.DataBean.SimpleBean.ListBean> forumDetailList = new ArrayList<>();
    private String forumuserId;
    private String toUserId;
    private String touserName;
    private String replyForUserText;
    private long commentId;
    private String userImg;
    private String nickname;
    private String forumText;
    private AllForumBean.DataBean.SimpleBean.ListBean listBean;
    final String[] items1 = {"回复", "删除"};
    final String[] items2 = {"回复"};
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        ButterKnife.bind(this);
        context = this;

//        intent.putExtra("forumId",forumList.get(position).getForumId());
//        intent.putExtra("userId",forumList.get(position).getUserId());
//        intent.putExtra("userImg",forumList.get(position).getUser().getImgUrl());
//        intent.putExtra("nickname",forumList.get(position).getUser().getNickName());
//        intent.putExtra("forumText",forumList.get(position).getForumText());
//        ArrayList<AllForumBean.DataBean.SimpleBean.ListBean.AllRecordsBean> records=new ArrayList<>();
//        records.addAll(forumList.get(position).getAllRecords());
//        intent.putExtra("records",records);
//        intent.putExtra("createTime",forumList.get(position).getCreatTime());
        Intent intent = getIntent();
        forumId = intent.getLongExtra("forumId", 0);
        forumuserId = intent.getStringExtra("userId");

//        userImg = intent.getStringExtra("userImg");
//        nickname = intent.getStringExtra("nickname");
//        forumText = intent.getStringExtra("forumText");
//        intent.getParcelableArrayListExtra("records");
        listBean = (AllForumBean.DataBean.SimpleBean.ListBean) intent.getSerializableExtra("forum");
        mGson = new Gson();
//        if (listBean!=null) {
//            Log.i("json", "onCreate: "+mGson.toJson(listBean));
//        }else {
//            Log.i("json", "onCreate: listBean=null");
//        }

        requestQueue = GlobalConstants.getRequestQueue();

        initView();
        initData();

    }

    private void initView() {
        detailAdapter = new CommonAdapter<ForumDetailBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_forum_detail_1, forumDetailList) {
            @Override
            protected void convert(ViewHolder holder, final ForumDetailBean.DataBean.SimpleBean.ListBean listBean, int position) {
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
                        intent.putExtra("commentInfo",listBean);
                        startActivity(intent);
                    }
                });
                Glide.with(context).load(listBean.getUserInfo().getImgUrl()).into(diyiv_userimg);
                tv_nickname.setText(listBean.getUserInfo().getNickName());
                tv_time.setText(listBean.getCreatTime());
                tv_body.setText(listBean.getCommentText());

            }
        };
        detailAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                toUserId = forumDetailList.get(position).getUserId();
//                touserName = forumDetailList.get(position).getUserInfo().getNickName();
                commentId = forumDetailList.get(position-1).getCommentId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.add);
                builder.setTitle("选择操作");
                //    指定下拉列表的显示数据
                if (forumDetailList.get(position-1).getUserId().equals((String) SPUtils.get(context, GlobalConstants.USERID, "")) || forumuserId.equals((String) SPUtils.get(context, GlobalConstants.USERID, ""))) {
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
                            }

                        }
                    });
                } else {
                    builder.setItems(items2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    showReplyDialog();
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




    }

    private void addHeadView(CommonAdapter myCommonAdapter) {
        //装饰者设计模式，把原来的adapter传入
        headerAndFooterWrapper = new HeaderAndFooterWrapper(myCommonAdapter);
        //添加头布局View对象
        headerAndFooterWrapper.addHeaderView(setHeadView());
        prvForumDetailShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvForumDetailShow.getRefreshableView().setAdapter(headerAndFooterWrapper);

    }

    private View setHeadView() {
        layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_forum_show,null);

        DIYImageView diy_userimg = (DIYImageView) view.findViewById(R.id.diyiv_item_forum_userimg);
        TextView tv_nickname=(TextView) view.findViewById(R.id.tv_item_forum_nickname);
        TextView tv_time=(TextView)view.findViewById(R.id.tv_item_forum_time);
        TextView tv_text=(TextView)view.findViewById(R.id.tv_item_forum_text);
        Glide.with(context).load(listBean.getUser().getImgUrl()).into(diy_userimg);
        tv_nickname.setText(listBean.getUser().getNickName());
        tv_time.setText(listBean.getCreatTime());
        tv_text.setText(listBean.getForumText());

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
                Toast.makeText(context, "确认" + which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });

//        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {//设置忽略按钮
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(context, "忽略" + which, Toast.LENGTH_SHORT).show();
//            }
//        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();

    }

    private void deleteCommentOrReply() {
        Request<String> deleteRequest = NoHttp.createStringRequest("http://192.168.31.81:8080/comment/deleteCommentOrReplyByForum", RequestMethod.POST);
        deleteRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        deleteRequest.add("forumId", forumId);
        deleteRequest.add("commentId", commentId);
        requestQueue.add(DELETE_COMMENT, deleteRequest, this);
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

    @OnClick({R.id.tv_forum_detail_send})
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
        NoHttp.getRequestQueueInstance().add(COMMENT_FOR_FORUM, commentForForumRequest, this);
    }

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

                break;
            case COMMENT_FOR_REPLY:

                break;
            case DELETE_COMMENT:

                break;
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    private void parseForumDetail(String json) {
        ForumDetailBean forumDetailBean = mGson.fromJson(json, ForumDetailBean.class);
        forumDetailList.addAll(forumDetailBean.getData().getSimple().getList());
        headerAndFooterWrapper.notifyDataSetChanged();
    }

}
