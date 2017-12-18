package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.lede.second_23.bean.GetReplyBean;
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

/**
 * 收到的评论页
 */
public class GetReplyActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int GET_REPLY=1000;

    @Bind(R.id.iv_get_reply_activity_back)
    ImageView ivGetReplyActivityBack;
    @Bind(R.id.prv_get_reply_activity_show)
    PullToRrefreshRecyclerView prvGetReplyActivityShow;
    private CommonAdapter getReplyAdapter;
    private RequestQueue requestQueue;
    private Context context;
    private int pageNum=1;
    private int pageSize=20;
    private boolean isOnRefrshing=false;
    private Gson mGson;
    private ArrayList<GetReplyBean.DataBean.SimpleBean.ListBean> getReplyList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reply);
        ButterKnife.bind(this);
        context=this;
        SPUtils.put(context,GlobalConstants.GETREPLY,0);
        requestQueue = GlobalConstants.getRequestQueue();
        mGson = new Gson();
        initView();
        initData();
    }

    private void initData() {
        Request<String> getReply= NoHttp.createStringRequest(GlobalConstants.URL+"/allForum/showNoticeByUser", RequestMethod.POST);
        getReply.add("access_token",(String) SPUtils.get(context,GlobalConstants.TOKEN,""));
        getReply.add("pageNum",pageNum);
        getReply.add("pageSize",pageSize);
        requestQueue.add(GET_REPLY,getReply,this);
    }

    private void initView() {
        getReplyAdapter= new CommonAdapter<GetReplyBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_get_reply, getReplyList) {
            @Override
            protected void convert(ViewHolder holder, GetReplyBean.DataBean.SimpleBean.ListBean listBean, int position) {
                DIYImageView diy_userimg=holder.getView(R.id.diyiv_item_get_reply_userimg);
                TextView tv_nickname=holder.getView(R.id.tv_item_get_reply_nickname);
                if (listBean.getUserInfo().getTrueName().equals("1")) {
                    Drawable drawableRight = getResources().getDrawable(
                            R.mipmap.v4);

                    tv_nickname.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    tv_nickname.setCompoundDrawablePadding(2);
                }
                TextView tv_time=holder.getView(R.id.tv_item_get_reply_time);
                TextView tv_text=holder.getView(R.id.tv_item_get_reply_text);
                ImageView iv_forum_pic=holder.getView(R.id.iv_item_get_reply_forum_pic);
                tv_nickname.setText(listBean.getUserInfo().getNickName());
                tv_time.setText(listBean.getCreatTime());
                if (listBean.getNoticeType()==0) {
                    tv_text.setText(listBean.getNoticeText());
                }else {
                    tv_text.setText("回复: "+listBean.getNoticeText());
                }

                Glide.with(context)
                        .load(listBean.getUserInfo().getImgUrl())
                        .into(diy_userimg);
                if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    Glide.with(context)
                            .load(listBean.getAllRecords().get(0).getUrl())
                            .into(iv_forum_pic);
                }else {
                    Glide.with(context)
                            .load(listBean.getAllRecords().get(0).getUrlThree())
                            .into(iv_forum_pic);
                }


            }
        };
        getReplyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // TODO  直接回复
                Intent intent=new Intent(context,ForumDetailActivity.class);
                intent.putExtra("forumId",getReplyList.get(position).getForumId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        prvGetReplyActivityShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvGetReplyActivityShow.getRefreshableView().setAdapter(getReplyAdapter);
        prvGetReplyActivityShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvGetReplyActivityShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageNum=1;
                isOnRefrshing=true;
                getReplyList.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isOnRefrshing=true;
                pageNum++;
                initData();
            }
        });
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("GetReplyActivity", "onSucceed: "+response.get());
            parseGetReplyJson(response.get());
    }



    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    private void parseGetReplyJson(String json) {
        GetReplyBean getReplyBean=mGson.fromJson(json,GetReplyBean.class);
        if (getReplyBean.getResult()==10000) {
            if (isOnRefrshing) {
                prvGetReplyActivityShow.onRefreshComplete();
                isOnRefrshing=false;
            }
            if (getReplyBean.getData().getSimple().getList().size()==0) {
                Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
            }else {
                getReplyList.addAll(getReplyBean.getData().getSimple().getList());

            }
        }
        getReplyAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_get_reply_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_get_reply_activity_back:
                finish();
                break;
        }
    }
}
