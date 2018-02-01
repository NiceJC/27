package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.BilateralBean;
import com.lede.second_23.bean.FriendBean;
import com.lede.second_23.bean.IfConcernBean;
import com.lede.second_23.bean.SearchingResultBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * 关注，取消关注，获取关注/粉丝列表
 * 点笑脸 获取互点笑脸的列表，获取收到的笑脸列表
 *
 *
 * Created by ld on 18/1/4.
 */

public class SocialService  {


    private static final int REQUEST_CONCERN = 163205;
    private static final int REQUEST_CANCEL_CONCERN=210121;
    private static final int REQUEST_USER_RELATION = 1163205;
    private static final int REQUEST_USER_GREET_RELATION=23340;
    private static final int REQUEST_USER_GREET_RECEIVE=2320940;
    private static final int REQUEST_USER_BY_NAME=232100;






    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;


    private Request<String> createConcernRequest = null;
    private Request<String> cancelConcernRequest=null;
    private Request<String> userRelationRequest=null;
    private Request<String> userGreetRelationRequest=null;
    private Request<String> getUserGreetReceiveRequest=null;
    private Request<String> getUserByNameRequest=null;





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
                    case REQUEST_USER_GREET_RELATION:
                        parseGreetRelation(response.get());
                        break;
                    case REQUEST_USER_GREET_RECEIVE:
                        parseGreetReceive(response.get());
                        break;
                    case REQUEST_USER_BY_NAME:
                        parseMatchedUser(response.get());
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
                    case REQUEST_USER_RELATION :
                    case REQUEST_USER_GREET_RELATION:
                    case REQUEST_USER_GREET_RECEIVE:
                    default:
                        myCallBack.onFail("数据请求出错");
                        break;

                }
            }
        };
    }

    /**
     * 查询是否关注某个用户
     */

    public void checkIfConcern(String userId,MyCallBack myCallBack){

        this.myCallBack=myCallBack;
        userRelationRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/collection/collectReship", RequestMethod.POST);
        userRelationRequest.add("userId",(String) SPUtils.get(mActivity, USERID, ""));
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

    /**
     * 查询与用户相互打招呼的用户列表
     *
     */

    public void getGreetRelation(int pageNum,int pageSize,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        userGreetRelationRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/friends/bilateral", RequestMethod.POST);
        userGreetRelationRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        userGreetRelationRequest.add("pageNum", pageNum);
        userGreetRelationRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_USER_GREET_RELATION,userGreetRelationRequest,simpleResponseListener);


    }

    /**
     * 查询 向我打招呼的用户列表
     *
     */
    public void getGreetReceive(int pageNum,int pageSize,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        getUserGreetReceiveRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/" + (String) SPUtils.get(mActivity, USERID, "") + "/followers", RequestMethod.POST);
        getUserGreetReceiveRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        getUserGreetReceiveRequest.add("pageNum", pageNum);
        getUserGreetReceiveRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_USER_GREET_RECEIVE, getUserGreetReceiveRequest, simpleResponseListener);


    }

    //根据用户名查询用户
    public void getUserByName(String mkName,MyCallBack myCallBack){
        this.myCallBack=myCallBack;

        getUserByNameRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/findUserNameLike", RequestMethod.POST);
        getUserByNameRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        getUserByNameRequest.add("mkName",mkName);
        RequestServer.getInstance().request(REQUEST_USER_BY_NAME, getUserByNameRequest,simpleResponseListener);


    }
    private void parseMatchedUser(String json) {
        SearchingResultBean searchingResultBean = mGson.fromJson(json, SearchingResultBean.class);
        if(searchingResultBean.getResult()==100205){

            myCallBack.onFail("");
        }else{
            myCallBack.onSuccess(searchingResultBean.getData().getUserInfos());

        }
    }






    private void parseConcernResult(String s){

        myCallBack.onSuccess("");


    }

    private void parseIfConcerned(String s){
        IfConcernBean ifConcernBean= mGson.fromJson(s, IfConcernBean.class);
        myCallBack.onSuccess(ifConcernBean.getData().isCollect());


    }

    private void parseGreetRelation(String s){
        BilateralBean bilateralBean = mGson.fromJson(s, BilateralBean.class);
        if (bilateralBean.getMsg().equals("用户没有登录")) {
            myCallBack.onFail("用户没有登录");

        } else {
            myCallBack.onSuccess(bilateralBean.getData().getList());

        }

    }
    private void parseGreetReceive(String s){
        FriendBean friendBean = mGson.fromJson(s, FriendBean.class);

        if (friendBean.getMsg().equals("用户没有登录")) {
            myCallBack.onFail("用户没有登录");

        } else {
            myCallBack.onSuccess(friendBean.getData());

        }

    }






}
