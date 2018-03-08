package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.AllForumBean;
import com.lede.second_23.bean.MessageBean;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.bean.PersonAllForumBean;
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
 * Created by ld on 17/12/20.
 */

public class ForumService {


    private static final int REQUEST_PUSH_FORUM = 162505;
    private static final int REQUEST_MY_FORUM = 1213121;
    private static final int REQUEST_PRAISE_FORUM = 1217878;
    private static final int REQUEST_ALL_FORUM = 125676;
    private static final int REQUEST_CONCERNED_FORUM = 1005676;

    private static final int PUSH_FORUM = 161276;
    private static final int BLOCK_FORUM = 56205;


    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;


    private Request<String> pushForumRequest = null;
    private Request<String> concernedForumRequest = null;

    private Request<String> myForumRequest = null;
    private Request<String> praiseForumRequest = null;
    private Request<String> allForumRequest = null;
    private Request<String> pushForum = null;
    private Request<String> blockForum = null;


    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;


    public ForumService() {

    }

    public ForumService(Activity activity) {
        this.mActivity = activity;
        mGson = new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (mActivity.isDestroyed()) {
                    return;
                }
                switch (what) {
                    case REQUEST_PUSH_FORUM:
                        parseForumJson(response.get());
                        break;
                    case REQUEST_MY_FORUM:
                        parseMyAllForum(response.get());
                        break;
                    case REQUEST_PRAISE_FORUM:
                        parsePraiseForum(response.get());

                        break;
                    case REQUEST_ALL_FORUM:
                        parseForumJson(response.get());

                        break;
                    case REQUEST_CONCERNED_FORUM:
                        parseForumJson(response.get());

                        break;
                    case PUSH_FORUM:
                        parseUpdateForumData(response.get());
                        break;
                    case BLOCK_FORUM:
                        parseBlockForum(response.get());


                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                if (mActivity.isDestroyed()) {
                    return;
                }
                switch (what) {
                    case REQUEST_PUSH_FORUM:
                    case REQUEST_MY_FORUM:
                    case REQUEST_PRAISE_FORUM:
                    default:
                        myCallBack.onFail("数据请求出错");

                        break;

                }
            }
        };
    }


    //获取所有动态，暂时用于管理版本
    public void requestAllForum(int pageNum, int pageSize, MyCallBack myCallBack) {
        this.myCallBack = myCallBack;

        allForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showAllForum", RequestMethod.POST);
        allForumRequest.add("pageNum", pageNum);
        allForumRequest.add("pageSize", pageSize);
        allForumRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        RequestServer.getInstance().request(REQUEST_ALL_FORUM, allForumRequest, simpleResponseListener);
    }

    // {"msg":"推送操作成功"}
    // {"msg":"取消操作成功"}
    // 管理版本 推送/取消推送 动态
    public void pushForum(long forumId, MyCallBack myCallBack) {
        this.myCallBack = myCallBack;

        pushForum = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/updateAllForumStuats", RequestMethod.POST);
        pushForum.add("forumId", forumId);
        RequestServer.getInstance().request(PUSH_FORUM, pushForum, simpleResponseListener);
    }

    //管理版本 屏蔽动态
    public void blockForum(long forumId, MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
//        Long forumId, Integer type = 2

        blockForum = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/updateAllForumType", RequestMethod.POST);
        blockForum.add("forumId", forumId);
        blockForum.add("type", 2);
        RequestServer.getInstance().request(BLOCK_FORUM, blockForum, simpleResponseListener);


    }

    //获取推送的高品质动态
    public void requestPushForum(int pageNum, int pageSize, MyCallBack myCallBack) {

        this.myCallBack = myCallBack;
        pushForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/pushAllForum", RequestMethod.POST);
        pushForumRequest.add("pageNum", pageNum);
        pushForumRequest.add("userId",  (String) SPUtils.get(mActivity,USERID,""));
        pushForumRequest.add("pageSize", pageSize);
//        pushForumRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        RequestServer.getInstance().request(REQUEST_PUSH_FORUM, pushForumRequest, simpleResponseListener);

    }

    //获取自己发布过的的动态
    public void requestMyForum(String userId, int pageNum, int pageSize, MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
        myForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showForumByHome", RequestMethod.POST);
        myForumRequest.add("userId", userId);
        myForumRequest.add("pageNum", pageNum);
        myForumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_MY_FORUM, myForumRequest, simpleResponseListener);

    }

    //获取关注的人的动态（包括自己的）
    public void requestConcernedForum( int pageNum, int pageSize, MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
        concernedForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/showCollectForum", RequestMethod.POST);
        concernedForumRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));
        concernedForumRequest.add("pageNum", pageNum);
        concernedForumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_CONCERNED_FORUM, concernedForumRequest, simpleResponseListener);

    }


    //给喜欢的动态点赞
    public void praiseForum(long forumId, MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
        praiseForumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/clickLike", RequestMethod.POST);
        praiseForumRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        praiseForumRequest.add("forumId", forumId);
        RequestServer.getInstance().request(REQUEST_PRAISE_FORUM, praiseForumRequest, simpleResponseListener);
    }

    // {"msg":"推送操作成功"}
    // {"msg":"取消操作成功"}
    private void parseUpdateForumData(String s) {
        MessageBean messageBean = mGson.fromJson(s, MessageBean.class);
        if (messageBean.getMsg().equals("推送操作成功")) {
            myCallBack.onSuccess("推送操作成功");
        } else if (messageBean.getMsg().equals("取消操作成功")) {
            myCallBack.onSuccess("取消操作成功");

        } else {
            myCallBack.onFail("");

        }

    }

    //{"msg":"屏蔽成功","result":10000}
    private void parseBlockForum(String s) {
        MsgBean messageBean = mGson.fromJson(s, MsgBean.class);
        if (messageBean.getResult()==10000) {
            myCallBack.onSuccess("屏蔽成功");
        } else {
            myCallBack.onFail("");

        }

    }



    /**
     * 解析全国forum
     *
     * @param json
     */
    private void parseForumJson(String json) {
        AllForumBean allforumBean = mGson.fromJson(json, AllForumBean.class);
        if (allforumBean.getResult() == 10000) {
            myCallBack.onSuccess(allforumBean.getData().getSimple().getList());
        } else {
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
        if (personAllForumBean.getResult() == 10000) {
            myCallBack.onSuccess(personAllForumBean.getData().getSimple().getList());
        } else {
            myCallBack.onFail("数据请求出错");
        }
    }

    private void parsePraiseForum(String json) {
        myCallBack.onSuccess("");


    }


}
