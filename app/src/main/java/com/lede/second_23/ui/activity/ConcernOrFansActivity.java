package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.ConcernOrFansBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关注的人OR 粉丝
 */
public class ConcernOrFansActivity extends AppCompatActivity implements OnResponseListener<String>, LoadMoreWrapper.OnLoadMoreListener {

    private int type;
    @Bind(R.id.tv_concern_or_fans_activity_title)
    TextView tv_title;
    @Bind(R.id.rv_concern_or_fans_activity_show)
    RecyclerView rv_show;
    @Bind(R.id.tv_conver_or_fans_activity_ifnone)
    TextView tv_ifNone;
    @Bind(R.id.srl_concern_or_fans_activity_refresh)
    SwipeRefreshLayout srl_refresh;
    private CommonAdapter mAdapter;
    private Gson mGson;
    private RequestQueue requestQueue;
    private ArrayList<ConcernOrFansBean.DataBean.ListBean> mList=new ArrayList<>();
    private Context context=this;
    private ImageView tv_right;
    private String id;
    private int pageNum=1;
    private LayoutInflater layoutInflater;
    private LoadMoreWrapper<Object> loadMoreWrapper;
    private View inflate;
    private boolean isHasNextPage=true;
    private boolean isRefresh=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern_or_fans);
        ButterKnife.bind(this);
        mGson = new Gson();
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        layoutInflater = LayoutInflater.from(context);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        id = intent.getStringExtra("id");
        if (type==0) {
            tv_title.setText("已关注");
            concernedService();
        }else {
            tv_title.setText("粉丝");
            fansService();
        }

        initRecyvlerView();
    }

    @OnClick({R.id.iv_concern_or_fans_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_concern_or_fans_activity_back:
                finish();
                break;
        }
    }

    private void fansService() {
        Request<String> fansRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/"+id+"/toCollect", RequestMethod.POST);
//        concernedRequest.add("id", (String) SPUtils.get(this,GlobalConstants.USERID,""));
//        fansRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        fansRequest.add("toUserId", (String) SPUtils.get(this,GlobalConstants.USERID,""));
        fansRequest.add("pageNum",pageNum);
        fansRequest.add("pageSize",20);
        requestQueue.add(200,fansRequest,this);
    }

    private void concernedService() {
        Request<String> concernedRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/"+id+"/userCollect", RequestMethod.POST);
//        concernedRequest.add("id", (String) SPUtils.get(this,GlobalConstants.USERID,""));
//        concernedRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        concernedRequest.add("toUserId", (String) SPUtils.get(this,GlobalConstants.USERID,""));
        concernedRequest.add("pageNum",pageNum);
        concernedRequest.add("pageSize",20);
        requestQueue.add(100,concernedRequest,this);
    }

    private void initRecyvlerView() {

        mAdapter= new CommonAdapter<ConcernOrFansBean.DataBean.ListBean>(this, R.layout.concern_or_fans_item, mList) {
            @Override
            protected void convert(ViewHolder holder, final ConcernOrFansBean.DataBean.ListBean listBean, int position) {
                CircleTextImageView cliv_touxiang=holder.getView(R.id.cliv_concern_or_fans_item_touxiang);
                TextView tv_nickName=holder.getView(R.id.tv_concern_or_fans_item_nickname);
                if (listBean.getTrueName().equals("1")) {
                    Drawable drawableRight = getResources().getDrawable(
                            R.mipmap.v1_fans);

                    tv_nickName.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    tv_nickName.setCompoundDrawablePadding(2);
                }
                tv_right=holder.getView(R.id.iv_concern_or_fans_item_right);
//                tv_right.setClickable(true);
//                if (type==0) {
//                    tv_right.setImageResource(R.mipmap.concern_right);
//                }else {
                    if (listBean.isFriend()) {
                        tv_right.setSelected(true);
                    }else {
                        tv_right.setSelected(false);

//                    }

                }
                if (listBean.getUserId().equals(SPUtils.get(ConcernOrFansActivity.this, GlobalConstants.USERID,""))) {
                    tv_right.setVisibility(View.GONE);
                }

                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (tv_right.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.mipmap.concern_right).getConstantState()) {
//                            cancelCollect(listBean.getUserId());
//                        }else {
//                            createCollect(listBean.getUserId());
//                        }
//                        tv_right.setClickable(false);
                        if (listBean.isFriend()) {
                            cancelCollect(listBean.getUserId());
                        }else {
                            createCollect(listBean.getUserId());
                        }
                    }
                });
//                if (!id.equals(SPUtils.get(context, GlobalConstants.USERID,""))) {
//                    tv_right.setClickable(false);
//                }
                tv_nickName.setText(listBean.getNickName());
                Glide.with(context)
                        .load(listBean.getImgUrl())
                        .into(cliv_touxiang);
            }
        };
        rv_show.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(context,OtherPersonActivity.class);
                intent.putExtra("userId",mList.get(position).getUserId());
                context.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum=1;
                mList.clear();
                if (type==0) {
                    concernedService();
                }else {
                    fansService();
                }
                isRefresh=true;
                srl_refresh.setRefreshing(isRefresh);
            }
        });

//        rv_show.setAdapter(mAdapter);
        setLoadMore();
    }

    //设置recyclerView上拉加载更多
    private void setLoadMore() {
        //定义请求页数
        pageNum = 1;
        loadMoreWrapper = new LoadMoreWrapper<>(mAdapter);
        inflate=layoutInflater.inflate(R.layout.item_loading,null);
        loadMoreWrapper.setLoadMoreView(inflate);
        loadMoreWrapper.setOnLoadMoreListener(this);
        rv_show.setAdapter(loadMoreWrapper);
    }

    /**
     * 关注用户
     */
    private void createCollect(String id) {
        Request<String> createCollectRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/createCollect", RequestMethod.POST);
        createCollectRequest.add("id",id);
        createCollectRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        requestQueue.add(300,createCollectRequest,this);
    }

    /**
     * 取消关注
     */
    private void cancelCollect(String id) {
        Request<String> cancelCollectRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/cancelCollect", RequestMethod.POST);
        cancelCollectRequest.add("id",id);
        cancelCollectRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        requestQueue.add(400,cancelCollectRequest,this);
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        L.i(response.get());

        switch (what) {
            case 100:
                parseConcernOrFansJson(response.get());
                break;
            case 200:
                parseConcernOrFansJson(response.get());
                break;
            case 300:
                mList.clear();
                pageNum=1;
                if (type==0) {
//                    tv_title.setText("已关注");
                    concernedService();
                }else {
//                    tv_title.setText("粉丝");
                    fansService();
                }
                break;
            case 400:
                mList.clear();
                pageNum=1;
                if (type==0) {
//                    tv_title.setText("已关注");

                    concernedService();
                }else {
//                    tv_title.setText("粉丝");
                    fansService();
                }
                break;
        }
    }



    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    private void parseConcernOrFansJson(String json) {
        ConcernOrFansBean concernOrFansBean=mGson.fromJson(json,ConcernOrFansBean.class);
        isHasNextPage=concernOrFansBean.getData().isHasNextPage();
        mList.addAll(concernOrFansBean.getData().getList());
        if (mList.size()==0) {
//            Toast.makeText(context, "无内容", Toast.LENGTH_SHORT).show();
            inflate.setVisibility(View.GONE);
//            tv_ifNone.setVisibility(View.VISIBLE);
        }
        isRefresh=false;
        srl_refresh.setRefreshing(isRefresh);
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
        if (!isHasNextPage) {
            inflate.setVisibility(View.GONE);
//            Toast.makeText(context, "无更多内容", Toast.LENGTH_SHORT).show();
        }else {
            pageNum++;
            if (type==0) {
                concernedService();
            }else {
                fansService();
            }
        }
    }
}
