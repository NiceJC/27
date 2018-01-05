package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

/**
 * Created by ld on 17/12/28.
 */

public class AlbumService {


    private static final int REQUEST_MY_ALBUM = 1623205;
    private static final int DELETE_MY_ALBUM=1210121;
    private static final int POST_MY_ALBUM=121394;



    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;


    private Request<String> getMyAlbumRequest = null;
    private Request<String> deleteMyAlbumRequest=null;
    private Request<String> postMyAlbumRequest=null;



    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public AlbumService() {

    }

    public AlbumService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case REQUEST_MY_ALBUM:
                        parseGetMyAlbum(response.get());
                        break;
                    case DELETE_MY_ALBUM:
                        parseDeleteAlbum(response.get());

                        break;
                    case POST_MY_ALBUM:
                        parsePostAlbum(response.get());
                        break;


                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case REQUEST_MY_ALBUM:
                    case DELETE_MY_ALBUM:
                    case POST_MY_ALBUM:
                        myCallBack.onFail("数据请求出错");
                        break;
                    default:
                        break;
                }
            }
        };
    }



    public void getMyAlbum(String userId, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        getMyAlbumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        getMyAlbumRequest.add("userId", userId);
        getMyAlbumRequest.add("pageNum",1);
        getMyAlbumRequest.add("pageSize", 5);
        RequestServer.getInstance().request(REQUEST_MY_ALBUM, getMyAlbumRequest, simpleResponseListener);

    }
    public void deleteAlbum(Integer id,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        deleteMyAlbumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/deleteUserPhoto", RequestMethod.POST);

        deleteMyAlbumRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        deleteMyAlbumRequest.add("id",id);
        RequestServer.getInstance().request(DELETE_MY_ALBUM, deleteMyAlbumRequest, simpleResponseListener);


    }

    public void postAlbum(){

        postMyAlbumRequest= NoHttp.createStringRequest(GlobalConstants.URL + "/photo/deleteUserPhoto", RequestMethod.POST);

    }







    public void parseGetMyAlbum(String json){
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(json, PersonalAlbumBean.class);
        if (personalAlbumBean.getResult() == 10000) {
            myCallBack.onSuccess(personalAlbumBean);

        }else{
            myCallBack.onFail("数据请求出错");
        }

    }

    public void parseDeleteAlbum(String json){
        myCallBack.onSuccess("");

    }

    public void parsePostAlbum(String json){
        myCallBack.onSuccess("");

    }




}
