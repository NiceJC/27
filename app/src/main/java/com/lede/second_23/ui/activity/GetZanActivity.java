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
import com.lede.second_23.bean.GetZanBean;
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
 * 收到的点赞
 */
public class GetZanActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int GET_ZAN=1000;

    @Bind(R.id.iv_get_zan_activity_back)
    ImageView ivGetZanActivityBack;
    @Bind(R.id.prv_get_zan_activity_show)
    PullToRrefreshRecyclerView prvGetZanActivityShow;
    private RequestQueue requestQueue;
    private Gson mGson;
    private Context context;
    private ArrayList<GetZanBean.DataBean.SimpleBean.ListBean> getZanHeplerList=new ArrayList<>();
    private ArrayList<GetZanBean.DataBean.SimpleBean.ListBean> getZanList=new ArrayList<>();
    private int pageNum=1;
    private int pageSize=100;
    private CommonAdapter getZanAdapter;
    private boolean isOnRefreshing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_zan);
        ButterKnife.bind(this);
        requestQueue = GlobalConstants.getRequestQueue();
        context=this;
        mGson = new Gson();
        initView();
        initData();
    }

    private void initData() {
        Request<String> getZanRequest= NoHttp.createStringRequest(GlobalConstants.URL+"/allForum/showLikeByUser", RequestMethod.POST);
        getZanRequest.add("access_token", (String) SPUtils.get(context,GlobalConstants.TOKEN,""));
        getZanRequest.add("pageNum",pageNum);
        getZanRequest.add("pageSize",pageSize);
        requestQueue.add(GET_ZAN,getZanRequest,this);
    }

    private void initView() {
        getZanAdapter= new CommonAdapter<GetZanBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_get_zan, getZanList) {
            @Override
            protected void convert(ViewHolder holder, final GetZanBean.DataBean.SimpleBean.ListBean listBean, int position) {
                DIYImageView diyiv_userimg=holder.getView(R.id.diyiv_item_get_zan_userimg);
                TextView tv_nickname=holder.getView(R.id.tv_item_get_zan_nickname);
                ImageView iv_forum_pic=holder.getView(R.id.iv_item_get_zan_forum_pic);
                iv_forum_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,ForumDetailActivity.class);
                        intent.putExtra("forumId",listBean.getForumId());
                        startActivity(intent);
                    }
                });
                Glide.with(context).load(listBean.getUserInfo().get(0).getImgUrl()).into(diyiv_userimg);
                if (listBean.getAllRecord().getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    Glide.with(context).load(listBean.getAllRecord().getUrlThree()).into(iv_forum_pic);
                }else {
                    Glide.with(context).load(listBean.getAllRecord().getUrl()).into(iv_forum_pic);
                }
                tv_nickname.setText(listBean.getUserInfo().get(0).getNickName()+"等"+listBean.getCountLike()+"人赞了你");
            }
        };
        getZanAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(context,ForumZanActivity.class);
                intent.putExtra("forumId",getZanList.get(position).getForumId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        prvGetZanActivityShow.getRefreshableView().setLayoutManager(new LinearLayoutManager(context));
        prvGetZanActivityShow.getRefreshableView().setAdapter(getZanAdapter);
        prvGetZanActivityShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvGetZanActivityShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isOnRefreshing =true;
                pageNum=1;
                getZanList.clear();
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

    @OnClick({R.id.iv_get_zan_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_get_zan_activity_back:
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
            case GET_ZAN:
                parseGetZanJson(response.get());
                break;
        }

    }



    @Override
    public void onFailed(int what, Response<String> response) {
        prvGetZanActivityShow.onRefreshComplete();
        Toast.makeText(context, "连接出错请检查网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish(int what) {

    }
    private void parseGetZanJson(String json) {

        GetZanBean getZanBean=mGson.fromJson(json,GetZanBean.class);
        if (isOnRefreshing) {
            prvGetZanActivityShow.onRefreshComplete();
            isOnRefreshing=false;
        }
        if (getZanBean.getResult()==10000) {
            if (getZanBean.getData().getSimple().getList().size()==0) {
                Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
            }else {
                getZanList.addAll(getZanBean.getData().getSimple().getList());
                getZanAdapter.notifyDataSetChanged();
            }

        }else {
            Toast.makeText(context, "服务器出错", Toast.LENGTH_SHORT).show();
        }
    }
}
