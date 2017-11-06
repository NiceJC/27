package com.lede.second_23.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.activity.ForumPicActivity;
import com.lede.second_23.utils.SPUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;

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
    public MultiTransformation transformation;
    private static final int REQUEST_MY_ALBUM = 26336;
    private static final int DELETE_MY_ALBUM = 12211;
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isHasNextPage = false;


    private ArrayList<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> mDataList = new ArrayList<>();

    private boolean isRefresh = true; //刷新种类 刷新还是加载更多

    private Request<String> getMyAlbumRequest = null;
    private Request<String> deleteAlbum = null;


    private RequestManager requestManager;


    private ArrayList<String> userphotoURLS = new ArrayList<>();
    private String userId;
    private boolean isMyPhoto;  //区分 当前展示的是他人的图片还是自己的


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_only, container, false);


        String userID = getActivity().getIntent().getStringExtra(USERID);
        if (userID != null && !userID.equals("")) {
            userId = userID;
            isMyPhoto = false;

        } else {
            userId = (String) SPUtils.get(getContext(), USERID, "");
            isMyPhoto = true;
        }

        ButterKnife.bind(this, view);

        mGson = new Gson();
        requestManager = Glide.with(getContext());
        initView();
        initEvent();
        toRefresh();
        return view;
    }


    private void initView() {

        transformation = new MultiTransformation(
                new CenterCrop(getContext()),
                new RoundedCornersTransformation(getContext(), 20, 0, RoundedCornersTransformation.CornerType.ALL)



        );
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));


        mAdapter = new CommonAdapter<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean>(getActivity(), R.layout.item_person_fragment_show, mDataList) {

            @Override
            protected void convert(ViewHolder holder, PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean, int position) {
                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);

                ImageView showView_photos = holder.getView(R.id.iv_person_fragment_item_photos);
                showView_photos.setVisibility(View.GONE);
                if (userPhotoBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
                    requestManager.load(userPhotoBean.getUrlImg())
                            .into(showView_show);
                    showView_play.setVisibility(View.GONE);

                } else {
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
                Intent intent = new Intent(getActivity(), ForumPicActivity.class);


                intent.putStringArrayListExtra("banner", userphotoURLS);
                intent.putExtra("position", position);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {
                if (isMyPhoto) {
                    DialogPlus dialogPlus=DialogPlus.newDialog(getActivity())
                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_delete_photo))
                            .setCancelable(true)
                            .setGravity(Gravity.CENTER)
                            .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {

                            switch (view.getId()){
                                case R.id.confirm:

                                    deletePhoto( Integer.parseInt(mDataList.get(position).getId()));
                                    dialog.dismiss();
                                    break;
                                case R.id.cancel:
                                    dialog.dismiss();
                                    break;
                                default:
                                    break;
                            }

                        }
                    })
                            .setExpanded(true).create();
                    ImageView imageView= (ImageView) dialogPlus.getHolderView().findViewById(R.id.image);
                    Glide.with(getContext())
                            .load(userphotoURLS.get(position))
                            .bitmapTransform(transformation)
                            .into(imageView);
                    dialogPlus.show();
                    return true;

                }


                return false;
            }
        });


    }


    public void initEvent() {

    }



    public void toRefresh() {

        //重复请求之前，取消之前的请求
        if (getMyAlbumRequest != null) {
            getMyAlbumRequest.cancel();
        }
        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {

        if (getMyAlbumRequest != null) {
            getMyAlbumRequest.cancel();
        }
        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        } else {
            Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();
        }
    }


    private void deletePhoto(Integer id){

        deleteAlbum = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/deleteUserPhoto", RequestMethod.POST);

        deleteAlbum.add("access_token", (String) SPUtils.get(getContext(), GlobalConstants.TOKEN, ""));
        deleteAlbum.add("id",id);
        RequestServer.getInstance().request(DELETE_MY_ALBUM, deleteAlbum, simpleResponseListener);


    }

    private void doRequest(int pageNum) {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_MY_ALBUM:
                        parsePersonAlbum(response.get());
                        break;
                    case DELETE_MY_ALBUM:
                        Toast.makeText(getContext(),"图片已删除",Toast.LENGTH_SHORT).show();
                        doRequest(1);
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_MY_ALBUM:

                        break;
                    default:
                        break;

                }
            }
        };

        getMyAlbumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        getMyAlbumRequest.add("userId", userId);
        getMyAlbumRequest.add("pageNum", pageNum);
        getMyAlbumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_MY_ALBUM, getMyAlbumRequest, simpleResponseListener);

    }


    /**
     * 解析个人相册
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
                if (isRefresh) {
                    currentPage = 1;
                    mDataList.clear();
                } else {
                    currentPage++;
                }
                mDataList.addAll(personalAlbumBean.getData().getSimple().getList());

                userphotoURLS.clear();
                for (PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean : mDataList
                        ) {

                    String url;
                    if (userPhotoBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
                        url = userPhotoBean.getUrlImg();
                    } else {
                        url = userPhotoBean.getUrlFirst();

                    }
                    userphotoURLS.add(url);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    }


}