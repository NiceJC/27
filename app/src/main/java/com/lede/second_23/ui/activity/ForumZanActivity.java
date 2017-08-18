package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.ForumZanBean;
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
import butterknife.OnClick;

public class ForumZanActivity extends AppCompatActivity implements OnResponseListener<String> {

    @Bind(R.id.iv_forum_zan_activity_back)
    ImageView ivForumZanActivityBack;
    @Bind(R.id.rv_forum_zan_activity_show)
    PullToRrefreshRecyclerView rvForumZanActivityShow;

    private static final int FORUM_ZAN=1000;

    private CommonAdapter mAdapter;
    private Context mContext;
    private RequestQueue requestQueue;
    private Gson mGson;
    private ArrayList<ForumZanBean.DataBean.SimpleBean.ListBean.UserInfoBean> userInfoBeanArrayList=new ArrayList<>();
    private long forumId;
    private int pageNum=1;
    private int pageSize=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_zan);
        ButterKnife.bind(this);
        mGson = new Gson();
        forumId = getIntent().getLongExtra("forumId", 0);
        mContext = this;
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        rvForumZanActivityShow.getRefreshableView().setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter= new CommonAdapter<ForumZanBean.DataBean.SimpleBean.ListBean.UserInfoBean>(mContext, R.layout.layout_bilateral_item, userInfoBeanArrayList) {
            @Override
            protected void convert(ViewHolder holder, ForumZanBean.DataBean.SimpleBean.ListBean.UserInfoBean userInfoBean, int position) {
                DIYImageView iv = holder.getView(R.id.iv_bilateral_item_test);
                TextView tv_name = holder.getView(R.id.iv_bilateral_item_name);
                Glide.with(mContext).load(userInfoBean.getImgUrl()).into(iv);
                tv_name.setText(userInfoBean.getNickName());
            }
        };
        rvForumZanActivityShow.getRefreshableView().setAdapter(mAdapter);
        initData();
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(ForumZanActivity.this,OtherPersonActivity.class);
                intent.putExtra("userId",userInfoBeanArrayList.get(position).getUserId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initData() {
        Request<String> getForumZan= NoHttp.createStringRequest(GlobalConstants.URL+"/allForum/showClickLikeList", RequestMethod.POST);
        getForumZan.add("forumId",forumId);
        getForumZan.add("pageNum",pageNum);
        getForumZan.add("pageSize",pageSize);
        requestQueue.add(FORUM_ZAN,getForumZan,this);
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("ForumZanActivity", "onSucceed: "+response.get());
        if (response.responseCode()==200) {
            switch (what) {
                case FORUM_ZAN:

                    parseForumZan(response.get());

                    break;
            }
        }

    }



    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }
    private void parseForumZan(String json) {
        ForumZanBean forumZanBean=mGson.fromJson(json,ForumZanBean.class);
        if (forumZanBean.getResult()==10000) {
            if (forumZanBean.getData().getSimple().getList().get(0).getUserInfo().size()==0) {
                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
            }else {
                userInfoBeanArrayList.addAll(forumZanBean.getData().getSimple().getList().get(0).getUserInfo());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick({R.id.iv_forum_zan_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_forum_zan_activity_back:
                finish();
                break;
        }
    }
}
