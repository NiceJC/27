package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.bean.PersonAllForumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

/**
 * Created by ld on 17/12/20.
 */

public class ForumService {


    private static final int REQUEST_PUSH_FORUM = 162505;
    private static final int REQUEST_MY_FORUM=1213121;



    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;


    private Request<String> pushForumRequest = null;
    private Request<String> myForumRequest=null;



    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public ForumService() {

    }

    public ForumService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_PUSH_FORUM:
                        parseForumJson(response.get());
                        break;
                    case REQUEST_MY_FORUM:
                        parseMyAllForum(response.get());
                        break;


                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_PUSH_FORUM:

                        myCallBack.onFail("数据请求出错");
                        break;
                    case REQUEST_MY_FORUM:
                        myCallBack.onFail("数据请求出错");
                        break;
                    default:
                        break;
                }
            }
        };
    }



    //匹配成功后，向被匹配的用户发送推送
    public void requestPushForum(int pageNum,int pageSize, MyCallBack myCallBack){

        this.myCallBack = myCallBack;
        pushForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/pushAllForum", RequestMethod.POST);
        pushForumRequest.add("pageNum", pageNum);
        pushForumRequest.add("pageSize", pageSize);
//        pushForumRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        RequestServer.getInstance().request(REQUEST_PUSH_FORUM, pushForumRequest, simpleResponseListener);

    }

    public void requestMyForum(String userId,int pageNum,int pageSize,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        myForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showForumByHome", RequestMethod.POST);
        myForumRequest.add("userId", userId);
        myForumRequest.add("pageNum", pageNum);
        myForumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_MY_FORUM, myForumRequest, simpleResponseListener);

    }




    /**
     * 解析全国forum
     *
     * @param json
     */
    private void parseForumJson(String json) {
        AllForumBean allforumBean = mGson.fromJson(json, AllForumBean.class);
        if(allforumBean.getResult()==10000){
            myCallBack.onSuccess(allforumBean.getData().getSimple().getList());
        }else{
            myCallBack.onFail("数据请求出错");
        }

    }
    /**
     * 解析个人全国微博
     *
     * @param json
     */
    private void parseMyAllForum(String json) {
        PersonAllForumBean personAllForumBean = mGson.fromJson(json, PersonAllForumBean.class);
        if(personAllForumBean.getResult()==10000){
            myCallBack.onSuccess(personAllForumBean.getData().getSimple().getList());
        }else{
            myCallBack.onFail("数据请求出错");
        }
    }





}