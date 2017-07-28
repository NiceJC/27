package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.global.GlobalConstants;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ForumActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int FORUM_CODE = 1000;

    @Bind(R.id.prv_forum_activity_show)
    PullToRrefreshRecyclerView prvForumActivityShow;
    private RequestQueue requestQueue;
    private Context context;
    private int pageNum = 1;
    private int pageSize = 10;
    private CommonAdapter forumAdapter;
    private ArrayList<AllForumBean.DataBean.SimpleBean.ListBean> forumList = new ArrayList<>();
    private Gson mGson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);
        context=this;
        mGson = new Gson();
        requestQueue = GlobalConstants.getRequestQueue();
        initData();
        initView();
    }

    private void initView() {
        forumAdapter = new CommonAdapter<AllForumBean.DataBean.SimpleBean.ListBean>(this, R.layout.item_forum_show, forumList) {

            @Override
            protected void convert(ViewHolder holder, AllForumBean.DataBean.SimpleBean.ListBean listBean, int position) {
                DIYImageView diy_userimg = holder.getView(R.id.diyiv_item_forum_userimg);
                TextView tv_nickname=holder.getView(R.id.tv_item_forum_nickname);
                TextView tv_time=holder.getView(R.id.tv_item_forum_time);
                TextView tv_text=holder.getView(R.id.tv_item_forum_text);
                Glide.with(context).load(listBean.getUser().getImgUrl()).into(diy_userimg);
                tv_nickname.setText(listBean.getUser().getNickName());
                tv_time.setText(listBean.getCreatTime());
                tv_text.setText(listBean.getForumText());
            }
        };
        forumAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(context,ForumDetailActivity.class);
                intent.putExtra("forumId",forumList.get(position).getForumId());
                intent.putExtra("userId",forumList.get(position).getUserId());
//                intent.putExtra("userImg",forumList.get(position).getUser().getImgUrl());
//                intent.putExtra("nickname",forumList.get(position).getUser().getNickName());
//                intent.putExtra("forumText",forumList.get(position).getForumText());
//                ArrayList<AllForumBean.DataBean.SimpleBean.ListBean.AllRecordsBean> records=new ArrayList<>();
//                records.addAll(forumList.get(position).getAllRecords());
                intent.putExtra("forum", forumList.get(position));
//                intent.put
//                intent.putExtra("createTime",forumList.get(position).getCreatTime());
//                intent.
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        prvForumActivityShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvForumActivityShow.getRefreshableView().setAdapter(forumAdapter);
    }

    private void initData() {
        Request<String> forumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showAllForum", RequestMethod.POST);
        forumRequest.add("pageNum", pageNum);
        forumRequest.add("pageSize", pageSize);
        requestQueue.add(FORUM_CODE, forumRequest, this);
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
        }
    }



    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    /**
     * 解析全国forum
     * @param json
     */
    private void parseForumJson(String json) {
        AllForumBean allforumBean=mGson.fromJson(json,AllForumBean.class);
        forumList.addAll(allforumBean.getData().getSimple().getList());
        forumAdapter.notifyDataSetChanged();
    }
}
