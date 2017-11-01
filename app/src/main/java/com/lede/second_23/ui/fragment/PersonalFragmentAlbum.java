package com.lede.second_23.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
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

/**
 * 个人中心下的个人相册页面
 * Created by wjc on 17/10/27.
 */

public class PersonalFragmentAlbum extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Gson mGson;
    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_MY_ALBUM = 26336;
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isHasNextPage = false;

    PersonalFragment1 personalFragment;
    private ArrayList<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> mDataList=new ArrayList<>();

    private boolean isRefresh=true; //刷新种类 刷新还是加载更多

    private Request<String> getMyAlbumRequest=null;


    private RequestManager requestManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_only, container, false);



        ButterKnife.bind(this, view);

        mGson = new Gson();
        requestManager=Glide.with(getContext());
        initView();
        initEvent();
        toRefresh();
        return view;
    }


    private void initView() {
        personalFragment=(PersonalFragment1)getParentFragment();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        mAdapter = new CommonAdapter<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean>(getActivity(), R.layout.item_person_fragment_show, mDataList) {

            @Override
            protected void convert(ViewHolder holder, PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean, int position) {
                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);

                ImageView showView_photos= holder.getView(R.id.iv_person_fragment_item_photos);
                showView_photos.setVisibility(View.GONE);
                if(userPhotoBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")){
                    requestManager.load(userPhotoBean.getUrlImg())
                    .into(showView_show);
                    showView_play.setVisibility(View.GONE);

                }else{
                    requestManager.load(userPhotoBean.getUrlFirst())
                    .into(showView_show);
                    showView_play.setVisibility(View.VISIBLE);
                }


            }
        };

        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent intent=new Intent(getActivity(),ForumDetailActivity.class);
//                intent.putExtra("forumId",mDataList.get(position).getForumId()); //position-1?
//                startActivity(intent);
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
        if(getMyAlbumRequest!=null){
            getMyAlbumRequest.cancel();
        }
        isRefresh=true;
        doRequest(1);
    }

    public void toLoadMore() {

        if(getMyAlbumRequest!=null){
            getMyAlbumRequest.cancel();
        }
        if(isHasNextPage){
            isRefresh=false;
            doRequest(currentPage+1);
        }else{
            Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();
            personalFragment.isOver();
        }
    }


    private void doRequest(int pageNum) {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_MY_ALBUM:
                        parsePersonAlbum(response.get());
                        personalFragment.isOver();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_MY_ALBUM:
                        personalFragment.isOver();
                        break;
                    default:
                        break;

                }
            }
        };

        getMyAlbumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        getMyAlbumRequest.add("userId", (String) SPUtils.get(getContext(), GlobalConstants.USERID, ""));
        getMyAlbumRequest.add("pageNum", pageNum);
        getMyAlbumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_MY_ALBUM, getMyAlbumRequest, simpleResponseListener);

    }


    /**
     * 解析个人全国微博
     *
     * @param json
     */
    private void parsePersonAlbum(String json) {
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(json, PersonalAlbumBean.class);
        if (personalAlbumBean.getResult() == 10000) {
            if (personalAlbumBean.getData().getSimple().getList().size() == 0) {
                Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();

            } else {
                isHasNextPage = personalAlbumBean.getData().getSimple().isHasNextPage();
                if(isRefresh){
                    currentPage=1;
                    mDataList.clear();
                }else{
                    currentPage++;
                }
                mDataList.addAll(personalAlbumBean.getData().getSimple().getList());

                mAdapter.notifyDataSetChanged();
            }
        }
    }


}