package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.IfConcernBean;
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
 * Created by ld on 18/1/4.
 */

public class SocialService  {


    private static final int REQUEST_CONCERN = 163205;
    private static final int REQUEST_CANCEL_CONCERN=210121;
    private static final int REQUEST_USER_RELATION = 1163205;




    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;


    private Request<String> createConcernRequest = null;
    private Request<String> cancelConcernRequest=null;
    private Request<String> userRelationRequest=null;




    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public SocialService() {





    }

    public SocialService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case REQUEST_CONCERN:
                        parseConcernResult(response.get());
                        break;
                    case REQUEST_CANCEL_CONCERN:
                        parseConcernResult(response.get());

                        break;
                    case REQUEST_USER_RELATION:
                        parseIfConcerned(response.get());
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
                    case REQUEST_CONCERN:
                    case REQUEST_CANCEL_CONCERN:
                        myCallBack.onFail("数据请求出错");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 查询关注
     */

    public void checkIfConcern(String userId,MyCallBack myCallBack){

        this.myCallBack=myCallBack;
        userRelationRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/collection/collectReship", RequestMethod.POST);
        userRelationRequest.add("userId",(String) SPUtils.get(mActivity, GlobalConstants.USERID, ""));
        userRelationRequest.add("toUserId",userId);
        RequestServer.getInstance().request(REQUEST_USER_RELATION, userRelationRequest, simpleResponseListener);



    }



    /**
     * 关注用户
     */
    public void createCollect(String id, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        createConcernRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/createCollect", RequestMethod.POST);
        createConcernRequest.add("id",id);
        createConcernRequest.add("access_token",(String) SPUtils.get(mActivity,GlobalConstants.TOKEN,""));
        RequestServer.getInstance().request(REQUEST_CONCERN,createConcernRequest,simpleResponseListener);
    }

    /**
     * 取消关注
     */
    public void cancelCollect(String id, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        cancelConcernRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/cancelCollect", RequestMethod.POST);
        cancelConcernRequest.add("id",id);
        cancelConcernRequest.add("access_token",(String) SPUtils.get(mActivity,GlobalConstants.TOKEN,""));
        RequestServer.getInstance().request(REQUEST_CANCEL_CONCERN,cancelConcernRequest,simpleResponseListener);
    }



    private void parseConcernResult(String s){

        myCallBack.onSuccess("");


    }

    private void parseIfConcerned(String s){
        IfConcernBean ifConcernBean= mGson.fromJson(s, IfConcernBean.class);
        myCallBack.onSuccess(ifConcernBean.getData().isCollect());


    }







}
