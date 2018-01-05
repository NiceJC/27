package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.MatchedUserBean;
import com.lede.second_23.bean.MatingVerifyBean;
import com.lede.second_23.bean.MessageBean;
import com.lede.second_23.bean.NewMatingUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import static com.lede.second_23.global.GlobalConstants.TOKEN;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/12/1.
 */

public class MatingService {



    private static final int VERIFYMATING = 15175;  //验证匹配信息
    private static final int CREATEMATING = 15815;  //创建匹配
    private static final int MATING = 115815;  //获取相匹配用户
    private static final int NEWMATING = 115215;  //获取相匹配用户的补充接口
    private static final int REFRESHCOUNT = 415815;  //非VIP用户刷新后 剩余次数-1





    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String> verifyRequest = null;
    private Request<String> createRequest = null;
    private Request<String> matingRequest =null;
    private Request<String> newMatingRequest=null;
    private Request<String> refreshCountRequest=null;



    private String userId;
    private MyCallBack myCallBack;





    private Activity mActivity;



    public MatingService() {

    }

    public MatingService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case VERIFYMATING:
                        parseVerify(response.get());
                        break;
                    case CREATEMATING:
                        parseCreate(response.get());
                        break;
                    case MATING:
                        parseMatchedUser(response.get());
                        break;
                    case NEWMATING:
                        parseNewMatchedUser(response.get());
                        break;
                    case REFRESHCOUNT:
                        parseCount(response.get());

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
                    case VERIFYMATING:


                    case CREATEMATING:

                    case MATING:

                    case REFRESHCOUNT:

                    case NEWMATING:

                        myCallBack.onFail(response.get().toString());

                    default:
                        break;
                }
            }
        };
    }



    public void requestVerify(MyCallBack myCallBack){
        this.myCallBack=myCallBack;

        verifyRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/marry/verifyMarry", RequestMethod.POST);

        verifyRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));

        RequestServer.getInstance().request(VERIFYMATING, verifyRequest, simpleResponseListener);


    }
    public void requestCreate(MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        createRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/marry/creatMarry", RequestMethod.POST);

        createRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));

        RequestServer.getInstance().request(CREATEMATING, createRequest, simpleResponseListener);

    }

    public void requestMating(String sex,Integer pageNum,Integer pageSize,MyCallBack myCallBack){

        this.myCallBack=myCallBack;
        matingRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/marry/marryInfo", RequestMethod.POST);

        matingRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));
        matingRequest.add("pageNum",pageNum);
        matingRequest.add("pageSize",pageSize);
        matingRequest.add("sex",sex);

        RequestServer.getInstance().request(MATING, matingRequest, simpleResponseListener);

    }
    public void requestNewMating( String sex ,MyCallBack myCallBack  ){
        this.myCallBack=myCallBack;
        newMatingRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/users/findMarryInfo", RequestMethod.POST);
        newMatingRequest.add("access_token", (String) SPUtils.get(mActivity,TOKEN,""));
        newMatingRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));
        newMatingRequest.add("sex",sex);

        RequestServer.getInstance().request(NEWMATING, newMatingRequest, simpleResponseListener);

    }
    public void requestRefreshCount(MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        refreshCountRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/marry/marryByCount", RequestMethod.POST);

        refreshCountRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));
        RequestServer.getInstance().request(REFRESHCOUNT, refreshCountRequest, simpleResponseListener);


    }







    //{"msg":"用户无匹配信息"}{"msg":"匹配信息成功"}
    private void parseVerify(String s){

        MessageBean messageBean=mGson.fromJson(s,MessageBean.class);
        if(messageBean.getMsg().equals("匹配信息错误")){
            myCallBack.onFail(messageBean.getMsg());

        }else{

            if(messageBean.getMsg().equals("用户无匹配信息")){
                myCallBack.onSuccess(3);

            }else{
                MatingVerifyBean matingVerifyBean=mGson.fromJson(s,MatingVerifyBean.class);
                matingVerifyBean.getData().getUserMarrie().get(0).getMarryDesp();
                int times=Integer.parseInt(matingVerifyBean.getData().getUserMarrie().get(0).getMarryDesp());
                myCallBack.onSuccess(times);

            }

        }



    }

    //{"msg":"创建用户第一次匹配成功"} {"msg":"匹配信息成功"}
    private void parseCreate(String s){

        MessageBean messageBean=mGson.fromJson(s,MessageBean.class);
        myCallBack.onSuccess(messageBean.getMsg());

    }

    //{"msg":"次数减一成功"}
    private void parseCount(String s){

        MessageBean messageBean=mGson.fromJson(s,MessageBean.class);
        myCallBack.onSuccess(messageBean.getMsg());

    }




    private void parseMatchedUser(String json) {
        MatchedUserBean matchedUserBean = mGson.fromJson(json, MatchedUserBean.class);

        if(matchedUserBean.getResult()==10000){
            //显示列表排除掉自己
//            List<MatchedUserBean.DataBean.UserInfoList.UserInfoBean> list=new ArrayList<>();
//            list.addAll(matchedUserBean.getData().getUserInfoList().getList());
//            if(list.size()!=0){
//                Iterator<MatchedUserBean.DataBean.UserInfoList.UserInfoBean> it=list.iterator();
//                while (it.hasNext()){
//                    MatchedUserBean.DataBean.UserInfoList.UserInfoBean user=it.next();
//                    if(user.getUserId().equals((String) SPUtils.get(mActivity, USERID, ""))){
//                        it.remove();
//                    }
//                }
//            }
            myCallBack.onSuccess(matchedUserBean);





        }else{
            myCallBack.onFail(matchedUserBean.getMsg());

        }


    }

    private void parseNewMatchedUser(String json){

        NewMatingUserBean newMatingUserBean = mGson.fromJson(json, NewMatingUserBean.class);

        if(newMatingUserBean.getResult()==10000){

            myCallBack.onSuccess(newMatingUserBean);

        }else{
            myCallBack.onFail(newMatingUserBean.getMsg());

        }

    }


}
