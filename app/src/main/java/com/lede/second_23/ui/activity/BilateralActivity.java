package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.BilateralBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
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
 * 互相打过招呼的人相当于好友列表
 */
public class BilateralActivity extends AppCompatActivity {

    @Bind(R.id.iv_bilateral_activity_reply)
    ImageView ivBilateralActivityReply;
    @Bind(R.id.iv_bilateral_activity_videoreply)
    ImageView ivBilateralActivityVideoreply;
    @Bind(R.id.iv_bilateral_activity_zan)
    ImageView ivBilateralActivityZan;
    private RequestQueue requestQueue;
    private Gson mGson;

    @Bind(R.id.rv_bilateral_activity_show)
    PullToRrefreshRecyclerView rv_show;

    private CommonAdapter mAdapter;
    private Context mContext;
    private ArrayList<BilateralBean.DataBean.ListBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilateral);
        ButterKnife.bind(this);
        mGson = new Gson();
        mContext = this;
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        rv_show.getRefreshableView().setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter = new CommonAdapter<BilateralBean.DataBean.ListBean>(mContext, R.layout.layout_bilateral_item, dataList) {
            @Override
            protected void convert(ViewHolder holder, BilateralBean.DataBean.ListBean listBean, int position) {
                DIYImageView iv = holder.getView(R.id.iv_bilateral_item_test);
                TextView tv_name = holder.getView(R.id.iv_bilateral_item_name);
                Glide.with(mContext)
                        .load(listBean.getImgUrl())
                        .asBitmap()
                        .error(R.mipmap.loading)
                        .placeholder(R.mipmap.loading)
                        .into(iv);
                tv_name.setText(listBean.getNickName());
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, ConcernActivity_2.class);
                intent.putExtra("userId", dataList.get(position).getUserId());
                mContext.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rv_show.getRefreshableView().setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        Request<String> bilateral_Request = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/friends/bilateral", RequestMethod.POST);
        bilateral_Request.add("access_token", (String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""));
        bilateral_Request.add("pageNum", 1);
        bilateral_Request.add("pageSize", 100);
        requestQueue.add(100, bilateral_Request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                L.i("TAB", response.get());
                parserJson(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void parserJson(String json) {
        BilateralBean bilateralBean = mGson.fromJson(json, BilateralBean.class);
        if (bilateralBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
        } else {
            dataList.addAll(bilateralBean.getData().getList());
            mAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.iv_bilateral_activity_back,R.id.iv_bilateral_activity_reply
            ,R.id.iv_bilateral_activity_zan,R.id.iv_bilateral_activity_videoreply})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_bilateral_activity_back:
                finish();
                break;
            case R.id.iv_bilateral_activity_reply:
                intent=new Intent(this, GetReplyActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_bilateral_activity_zan:
                intent=new Intent(this,GetZanActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_bilateral_activity_videoreply:
                intent=new Intent(this,GetVideoReplyActivity.class);
                startActivity(intent);

                break;
        }

    }
}
