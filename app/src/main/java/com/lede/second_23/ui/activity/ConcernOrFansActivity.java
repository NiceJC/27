package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.ConcernOrFansBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * 关注的人OR 粉丝
 */
public class ConcernOrFansActivity extends BaseActivity implements OnResponseListener<String>{

    @Bind(R.id.tv_concern_or_fans_activity_title)
    TextView tv_title;
    @Bind(R.id.recyclerView)
    RecyclerView rv_show;

    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;


    private CommonAdapter mAdapter;
    private Gson mGson;
    private RequestQueue requestQueue;
    private ArrayList<ConcernOrFansBean.DataBean.ListBean> mList=new ArrayList<>();
    private Context context=this;
    private ImageView tv_right;
    private String id;
    private int currentPage=1;
    private static final int PAGE_SIZE=20;

    private int type;
    private static final int TYPE_CONCERN=0;
    private static final int TYPE_FANS=1;


    private int clickPosition;
    private boolean clickIsFriend=true;

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

        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        id = intent.getStringExtra("id");
        if (type==TYPE_CONCERN) {
            tv_title.setText("已关注");

        }else {
            tv_title.setText("粉丝");

        }
        initRecyvlerView();
        initEvent();
        toRefresh();

    }

    @OnClick({R.id.iv_concern_or_fans_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_concern_or_fans_activity_back:
                finish();
                break;
        }
    }




    private void toRefresh(){
        isRefresh=true;
        doRequest(1);
    }
    private void toLoadMore(){
        isRefresh=false;
        if(isHasNextPage){
            doRequest(currentPage+1);
        }else{
            refreshLayout.finishLoadmore();
        }

    }

    private void doRequest(int pageNum){
        if(type==TYPE_CONCERN){
            concernedService(pageNum);
        }else{
            fansService(pageNum);
        }
    }



    private void fansService(int pageNum) {
        Request<String> fansRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/"+id+"/toCollect", RequestMethod.POST);
        fansRequest.add("toUserId", (String) SPUtils.get(this, USERID,""));
        fansRequest.add("pageNum",pageNum);
        fansRequest.add("pageSize",PAGE_SIZE);
        requestQueue.add(200,fansRequest,this);
    }

    private void concernedService(int pageNum) {
        Request<String> concernedRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/"+id+"/userCollect", RequestMethod.POST);
//        concernedRequest.add("id", (String) SPUtils.get(this,GlobalConstants.USERID,""));
//        concernedRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        concernedRequest.add("toUserId", (String) SPUtils.get(this, USERID,""));
        concernedRequest.add("pageNum",pageNum);
        concernedRequest.add("pageSize",PAGE_SIZE);
        requestQueue.add(100,concernedRequest,this);
    }


    private void initEvent(){


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                toRefresh();


            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                toLoadMore();
            }
        });



    }

    private void initRecyvlerView() {

        mAdapter= new CommonAdapter<ConcernOrFansBean.DataBean.ListBean>(this, R.layout.concern_or_fans_item, mList) {
            @Override
            protected void convert(ViewHolder holder, final ConcernOrFansBean.DataBean.ListBean listBean, final int position) {
                CircleTextImageView cliv_touxiang=holder.getView(R.id.cliv_concern_or_fans_item_touxiang);
                ImageView vipTag=holder.getView(R.id.vip_tag);
                TextView tv_nickName=holder.getView(R.id.tv_concern_or_fans_item_nickname);
                if (listBean.getTrueName()!=null&&listBean.getTrueName().equals("1")) {
                   vipTag.setVisibility(View.VISIBLE);
                }else{
                    vipTag.setVisibility(View.GONE);
                }
                tv_right=holder.getView(R.id.iv_concern_or_fans_item_right);

                    if (listBean.isFriend()) {
                        tv_right.setSelected(true);
                    }else {
                        tv_right.setSelected(false);


                }
                if (listBean.getUserId().equals(SPUtils.get(ConcernOrFansActivity.this, USERID,""))) {
                    tv_right.setVisibility(View.GONE);
                }else{
                    tv_right.setVisibility(View.VISIBLE);
                }

                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        clickPosition=position;
                        clickIsFriend=listBean.isFriend();
                        if (listBean.isFriend()) {
                            cancelCollect(listBean.getUserId());

                        }else {
                            createCollect(listBean.getUserId());
                        }
                        tv_right.setClickable(false);
                    }
                });

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
                Intent intent=new Intent(context,UserInfoActivty.class);
                intent.putExtra(USERID,mList.get(position).getUserId());
                context.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rv_show.setAdapter(mAdapter);

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
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefresh();
                break;
            case 200:
                parseConcernOrFansJson(response.get());
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefresh();
                break;
            case 300:


                mList.get(clickPosition).setFriend(true);
                mAdapter.notifyDataSetChanged();


                break;
            case 400:
                mList.get(clickPosition).setFriend(false);
                mAdapter.notifyDataSetChanged();

                break;
        }
    }



    @Override
    public void onFailed(int what, Response<String> response) {
        Toast.makeText(getApplicationContext(),"请求数据出错",Toast.LENGTH_SHORT).show();
        refreshLayout.finishLoadmore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onFinish(int what) {

    }

    private void parseConcernOrFansJson(String json) {
        ConcernOrFansBean concernOrFansBean=mGson.fromJson(json,ConcernOrFansBean.class);
        if(isRefresh){
            mList.clear();
            currentPage=1;
            isHasNextPage=true;
        }else{
            if(concernOrFansBean.getData().getList().size()==0){
               isHasNextPage=false;
            }else{
                currentPage++;
            }
        }
        mList.addAll(concernOrFansBean.getData().getList());
        mAdapter.notifyDataSetChanged();

    }


}
