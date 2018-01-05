package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.ApplyVIPResultBean;
import com.lede.second_23.bean.MsgBean;
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
 * Created by ld on 17/12/12.
 */

public class PushService {

    private static final int PUSH_MATCH_INFO = 1112505;



    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String> matchInfoPush = null;

    private PersonalAlbumBean.DataBean.UserInfo userInfo;

    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public PushService() {

    }

    public PushService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case PUSH_MATCH_INFO:
                        parseMatchedInfo(response.get());
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
                    case PUSH_MATCH_INFO:

                        myCallBack.onFail(response.get().toString());
                        break;
                    default:
                        break;
                }
            }
        };
    }



    //匹配成功后，向被匹配的用户发送推送
    public void pushMatchedInfo(String targetUserID, MyCallBack myCallBack){

        this.myCallBack = myCallBack;
        matchInfoPush = NoHttp.createStringRequest(GlobalConstants.URL + "/push/pushMessageByUser", RequestMethod.POST);
        matchInfoPush.add("access_token",  (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        matchInfoPush.add("userId", targetUserID);
        RequestServer.getInstance().request(PUSH_MATCH_INFO, matchInfoPush, simpleResponseListener);

    }


    /**
     * {"msg":"请求成功","result":10000}
     * @param s
     */
    private void parseMatchedInfo(String s) {
        ApplyVIPResultBean applyVIPResultBean = mGson.fromJson(s, ApplyVIPResultBean.class);

        if(applyVIPResultBean.getResult()==10000){
            String result = applyVIPResultBean.getMsg();
            if(!result.equals("")){
                myCallBack.onSuccess(result);
            }

        }


    }


    private void parseVerifyVIPInfo(String s){
        MsgBean msgBean=mGson.fromJson(s,MsgBean.class);
        if(msgBean.getResult()==10000){

            myCallBack.onSuccess(null);
        }
    }










}
