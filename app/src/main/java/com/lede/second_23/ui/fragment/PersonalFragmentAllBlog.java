package com.lede.second_23.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonAllForumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.activity.ForumDetailActivity;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 个人中心下的 动态页面
 * Created wjc ld on 17/10/27.
 */

public class PersonalFragmentAllBlog extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;
    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_ALL_FORUM = 111;
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isHasNextPage = false;


    private ArrayList<PersonAllForumBean.DataBean.SimpleBean.ListBean> mDataList=new ArrayList<>();

    private boolean isRefresh=true; //刷新种类 刷新还是加载更多

    private Request<String> getAllForumRequest=null;


    private String userId;
    private RequestManager requestManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_only, container, false);

        String userID=getActivity().getIntent().getStringExtra(USERID);
        if(userID!=null&&!userID.equals("")){
            userId=userID;
        }else {
            userId=(String) SPUtils.get(getContext(), USERID, "");
        }


        ButterKnife.bind(this, view);

        mGson = new Gson();
        requestManager=Glide.with(getContext());
        initView();
        initEvent();
        toRefresh();
        return view;
    }


    private void initView() {


        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        mAdapter = new CommonAdapter<PersonAllForumBean.DataBean.SimpleBean.ListBean>(getActivity(), R.layout.item_person_fragment_show, mDataList) {

            @Override
            protected void convert(ViewHolder holder, PersonAllForumBean.DataBean.SimpleBean.ListBean listBean, int position) {
                ImageView showView_photos = (ImageView) holder.getView(R.id.iv_person_fragment_item_photos);
                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);
                if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    requestManager.load(listBean.getAllRecords().get(0).getUrl()).into(showView_show);
                    showView_play.setVisibility(View.GONE);
                    if(listBean.getAllRecords().size()==1||listBean.getAllRecords().size()==0){
                        showView_photos.setVisibility(View.GONE);
                    }else{
                        showView_photos.setVisibility(View.VISIBLE);
                    }


                }else {
                    Log.i("videopic", "convert: "+listBean.getAllRecords().get(0).getUrlThree());
                    requestManager.load(listBean.getAllRecords().get(0).getUrlThree()).into(showView_show);
                    showView_play.setVisibility(View.VISIBLE);
                    showView_photos.setVisibility(View.GONE);
                }
            }
        };

        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(getActivity(),ForumDetailActivity.class);
                intent.putExtra("forumId",mDataList.get(position).getForumId()); //position-1?
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }


    public void initEvent() {

    }

    public void toRefresh() {

        //重复请求之前，取消之前的请求
        if(getAllForumRequest!=null){
            getAllForumRequest.cancel();
        }
        isRefresh=true;
        doRequest(1);
    }

    public void toLoadMore() {

        if(getAllForumRequest!=null){
            getAllForumRequest.cancel();
        }
        if(isHasNextPage){
            isRefresh=false;
            doRequest(currentPage+1);
        }else{
            Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();

        }
    }


    private void doRequest(int pageNum) {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_ALL_FORUM:
                        parsePersonAllForum(response.get());

                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_ALL_FORUM:

                        break;
                    default:
                        break;

                }
            }
        };

        getAllForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showForumByHome", RequestMethod.POST);
        getAllForumRequest.add("userId", userId);
        getAllForumRequest.add("pageNum", pageNum);
        getAllForumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_ALL_FORUM, getAllForumRequest, simpleResponseListener);

    }


    /**
     * 解析个人全国微博
     *
     * @param json
     */
    private void parsePersonAllForum(String json) {
        PersonAllForumBean personAllForumBean = mGson.fromJson(json, PersonAllForumBean.class);
        if (personAllForumBean.getResult() == 10000) {
            if (personAllForumBean.getData().getSimple().getList().size() == 0) {
                Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();

            } else {
                isHasNextPage = personAllForumBean.getData().getSimple().isHasNextPage();
                if(isRefresh){
                    currentPage=1;
                    mDataList.clear();
                }else{
                    currentPage++;
                }
                mDataList.addAll(personAllForumBean.getData().getSimple().getList());

                mAdapter.notifyDataSetChanged();
            }
        }
    }


}
