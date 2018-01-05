package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import static com.lede.second_23.ui.activity.VIPSettingActivity.ALL;
import static com.lede.second_23.ui.activity.VIPSettingActivity.BOY;
import static com.lede.second_23.ui.activity.VIPSettingActivity.GIRL;

/**
 * Created by ld on 17/11/30.
 */

public class PushedUserService {


    private static final int REQUEST_PUSH_USER = 1515;


    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String> pushBoyRequest = null;
    private Request<String> pushAllRequest = null;
    private Request<String> pushGirlRequest =null;



    private String userId;
    private MyCallBack myCallBack;



    private Activity mActivity;



    public PushedUserService() {

    }

    public PushedUserService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case REQUEST_PUSH_USER:
                        parsePushUser(response.get());
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
                    case REQUEST_PUSH_USER:
                        myCallBack.onFail(response.get().toString());
                        break;

                    default:
                        break;
                }
            }
        };
    }



    public void requestPushUser(int choosenSex,int pageNum,int pageSize,MyCallBack myCallBack){
        this.myCallBack=myCallBack;



        switch (choosenSex) {
            case BOY:

                pushBoyRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/push/pushByBoy", RequestMethod.POST);
                pushBoyRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
                pushBoyRequest.add("pageNum", pageNum);
                pushBoyRequest.add("pageSize", pageSize);

                RequestServer.getInstance().request(REQUEST_PUSH_USER, pushBoyRequest, simpleResponseListener);

                break;
            case GIRL:
                pushGirlRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/push/pushByGirl", RequestMethod.POST);
                pushGirlRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
                pushGirlRequest.add("pageNum", pageNum);
                pushGirlRequest.add("pageSize", pageSize);

                RequestServer.getInstance().request(REQUEST_PUSH_USER, pushGirlRequest, simpleResponseListener);

                break;
            case ALL:

                pushAllRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/push/newPushByUser", RequestMethod.POST);
                pushAllRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
                pushAllRequest.add("pageNum", pageNum);
                pushAllRequest.add("pageSize", pageSize);

                RequestServer.getInstance().request(REQUEST_PUSH_USER, pushAllRequest, simpleResponseListener);

                break;
            default:
                break;
        }


    }



    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parsePushUser(String json) {
        SameCityUserBean sameCityUserBean = mGson.fromJson(json, SameCityUserBean.class);

        if(sameCityUserBean.getResult()==10000){
            myCallBack.onSuccess(sameCityUserBean.getData().getUserInfoList().getList());
        }else{
            myCallBack.onFail(sameCityUserBean.getMsg());

        }


    }





}
