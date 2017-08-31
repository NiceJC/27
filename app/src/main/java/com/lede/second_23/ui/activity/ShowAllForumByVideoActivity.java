package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.R;
import com.lede.second_23.bean.ShowAllForumByVideoBean;
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

public class ShowAllForumByVideoActivity extends AppCompatActivity implements OnResponseListener<String> {

    @Bind(R.id.prv_show_all_forum_video_show)
    PullToRrefreshRecyclerView prvShowAllForumVideoShow;

    private static final int ALLVIDEO = 1000;
    @Bind(R.id.iv_show_all_forum_video_back)
    ImageView ivShowAllForumVideoBack;

    private Context context;
    private CommonAdapter<ShowAllForumByVideoBean.DataBean.SimpleBean.ListBean> myVideoAdapter;
    private ArrayList<ShowAllForumByVideoBean.DataBean.SimpleBean.ListBean> videoList = new ArrayList<>();
    private RequestQueue requestQueue;
    private int pageNum = 1;
    private int pageSize = 20;
    private Gson mGson;
    private ShowAllForumByVideoBean showAllForumByVideoBean;
    private boolean isOnRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_forum_by_video);
        ButterKnife.bind(this);
        mGson = new Gson();
        context = this;
        requestQueue = GlobalConstants.getRequestQueue();
        initView();
        initData();

    }

    private void initData() {
        Request<String> showAllForumVideoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showAllForumByVideo", RequestMethod.POST);
        showAllForumVideoRequest.add("pageNum", pageNum);
        showAllForumVideoRequest.add("pageSize", pageSize);
        requestQueue.add(ALLVIDEO, showAllForumVideoRequest, this);
    }

    private void initView() {
        myVideoAdapter = new CommonAdapter<ShowAllForumByVideoBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_person_fragment_show, videoList) {
            @Override
            protected void convert(ViewHolder holder, ShowAllForumByVideoBean.DataBean.SimpleBean.ListBean listBean, int position) {
                ImageView diyiv_show = holder.getView(R.id.iv_person_fragment_item_show);
                ImageView iv_photos = holder.getView(R.id.iv_person_fragment_item_photos);
                ImageView iv_play = holder.getView(R.id.iv_person_fragment_item_play);
                iv_photos.setVisibility(View.GONE);
                iv_play.setVisibility(View.GONE);
                Glide.with(context)
                        .load(listBean.getAllRecords().get(0).getUrlThree()).into(diyiv_show);
            }
        };
        myVideoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(context, ForumDetailActivity.class);
                intent.putExtra("forumId",videoList.get(position).getForumId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        prvShowAllForumVideoShow.getRefreshableView().setLayoutManager(new GridLayoutManager(context, 3));
        prvShowAllForumVideoShow.getRefreshableView().setAdapter(myVideoAdapter);
        prvShowAllForumVideoShow.setMode(PullToRefreshBase.Mode.BOTH);
        prvShowAllForumVideoShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                videoList.clear();
                pageNum=1;
                initData();
                isOnRefresh=true;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageNum++;
                initData();
                isOnRefresh=true;
            }
        });
    }

    @OnClick({R.id.iv_show_all_forum_video_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_show_all_forum_video_back:
                finish();
                break;
        }
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case ALLVIDEO:
                parseAllVideo(response.get());
                break;
        }

    }


    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    private void parseAllVideo(String json) {
        showAllForumByVideoBean = mGson.fromJson(json, ShowAllForumByVideoBean.class);
        if (showAllForumByVideoBean.getData().getSimple().getList().size()==0) {
            Toast.makeText(context, "没有更多的内容啦", Toast.LENGTH_SHORT).show();
        }else {
            videoList.addAll(showAllForumByVideoBean.getData().getSimple().getList());
        }

        if (isOnRefresh) {
            prvShowAllForumVideoShow.onRefreshComplete();
            isOnRefresh=false;
        }
        myVideoAdapter.notifyDataSetChanged();
    }
}
