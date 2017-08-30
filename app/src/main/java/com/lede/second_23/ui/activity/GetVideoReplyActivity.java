package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.GetVideoReplyBean;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetVideoReplyActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int GET_VIDEO_REPLY=1000;

    @Bind(R.id.iv_get_video_reply_activity_back)
    ImageView ivGetVideoReplyActivityBack;
    @Bind(R.id.prv_get_video_reply_activity_show)
    PullToRrefreshRecyclerView prvGetVideoReplyActivityShow;
    private RequestQueue requestQueue;
    private Gson mGson;
    private Context context;
    private ArrayList<GetVideoReplyBean.DataBean.SimplePageInfoBean.ListBean> getVideoReplyList=new ArrayList<>();
    private int pageNum=1;
    private int pageSize=20;
    private CommonAdapter getZanAdapter;
    private boolean isOnRefreshing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_video_reply);
        ButterKnife.bind(this);
        requestQueue = GlobalConstants.getRequestQueue();
        context=this;
        mGson = new Gson();
        initView();
        initData();
    }

    private void initData() {
        Request<String> getZanRequest= NoHttp.createStringRequest(GlobalConstants.URL+"/VideoReply/showVideoByUserHome", RequestMethod.POST);
        getZanRequest.add("access_token", (String) SPUtils.get(context,GlobalConstants.TOKEN,""));
        getZanRequest.add("pageNum",pageNum);
        getZanRequest.add("pageSize",pageSize);
        requestQueue.add(GET_VIDEO_REPLY,getZanRequest,this);
    }

    private void initView() {
        getZanAdapter= new CommonAdapter<GetVideoReplyBean.DataBean.SimplePageInfoBean.ListBean>(context, R.layout.item_get_zan, getVideoReplyList) {
            @Override
            protected void convert(ViewHolder holder, GetVideoReplyBean.DataBean.SimplePageInfoBean.ListBean listBean, int position) {
                DIYImageView diyiv_userimg=holder.getView(R.id.diyiv_item_get_zan_userimg);
                TextView tv_nickname=holder.getView(R.id.tv_item_get_zan_nickname);
                ImageView iv_forum_pic=holder.getView(R.id.iv_item_get_zan_forum_pic);
                Glide.with(context).load(listBean.getUserInfo().getImgUrl()).into(diyiv_userimg);
                if (listBean.getAllRecord().getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    Glide.with(context).load(listBean.getAllRecord().getUrlThree()).into(iv_forum_pic);
                }else {
                    Glide.with(context).load(listBean.getAllRecord().getUrl()).into(iv_forum_pic);
                }
                tv_nickname.setText(listBean.getUserInfo().getNickName()+"等"+listBean.getReplyCount()+"人给你留影");
            }
        };
        getZanAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(context,ForumDetailActivity.class);
                intent.putExtra("forumId",getVideoReplyList.get(position).getForumId());
                intent.putExtra("isOpenReplyVideo",true);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        prvGetVideoReplyActivityShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvGetVideoReplyActivityShow.getRefreshableView().setAdapter(getZanAdapter);
        prvGetVideoReplyActivityShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvGetVideoReplyActivityShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isOnRefreshing =true;
                pageNum=1;
                getVideoReplyList.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isOnRefreshing=true;
                pageNum++;
                initData();
            }
        });
    }

    @OnClick({R.id.iv_get_video_reply_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_get_video_reply_activity_back:
                finish();
                break;
        }
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("GetZanActivity", "parseGetZanJson: "+response.get());
        switch (what) {
            case GET_VIDEO_REPLY:
                parseGetZanJson(response.get());
                break;
        }

    }



    @Override
    public void onFailed(int what, Response<String> response) {
        prvGetVideoReplyActivityShow.onRefreshComplete();
        Toast.makeText(context, "连接出错请检查网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish(int what) {

    }
    private void parseGetZanJson(String json) {

        GetVideoReplyBean getVideoReplyBean=mGson.fromJson(json,GetVideoReplyBean.class);
        if (isOnRefreshing) {
            prvGetVideoReplyActivityShow.onRefreshComplete();
            isOnRefreshing=false;
        }
        if (getVideoReplyBean.getResult()==10000) {
            if (getVideoReplyBean.getData().getSimplePageInfo().getList().size()==0) {
                Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
            }else {
                getVideoReplyList.addAll(getVideoReplyBean.getData().getSimplePageInfo().getList());
                getZanAdapter.notifyDataSetChanged();
            }

        }else {
            Toast.makeText(context, "服务器出错", Toast.LENGTH_SHORT).show();
        }
    }
}
