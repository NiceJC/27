package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

/**
 * Created by ld on 17/11/21.
 */

public class UserInfoService {

    private static final int REQUEST_USER_INFO = 555;
    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String> userInfoRequest = null;

    private PersonalAlbumBean.DataBean.UserInfo userInfo;

    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public UserInfoService() {

    }

    public UserInfoService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();

    }



    //获取用户信息  需要userId 和一个带参的回调接口
    public void getUserInfo(String userId, final MyCallBack myCallBack){

        this.userId=userId;
        this.myCallBack = myCallBack;

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        parseUserInfo(response.get());
                        break;

                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        myCallBack.onFail(response.get().toString());
                        break;
                    default:
                        break;
                }
            }
        };

        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        userInfoRequest.add("userId", userId);
        userInfoRequest.add("pageNum", 1);
        userInfoRequest.add("pageSize", 1);

        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);

    }


    private void parseUserInfo(String s) {
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(s, PersonalAlbumBean.class);
        userInfo = personalAlbumBean.getData().getUserInfo().get(0);
        if(userInfo!=null){
            myCallBack.onSuccess(userInfo);
        }

    }





}
