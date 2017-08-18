package com.lede.second_23.utils;

import com.google.gson.Gson;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.global.GlobalConstants;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by ld on 17/8/17.
 */

public class PushUserUtil {
    private static final int PUSH_USER=1000;
    private static Gson gson;

    public static void pushUser( String userId, String  access_token){
        gson = new Gson();
        Request<String> pushUserRequest= NoHttp.createStringRequest(GlobalConstants.URL+"/push/pushMessageByUser", RequestMethod.POST);
        pushUserRequest.add("access_token",access_token);
        pushUserRequest.add("userId",userId);
        GlobalConstants.getRequestQueue().add(PUSH_USER, pushUserRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case PUSH_USER:
                        MsgBean msgBean=gson.fromJson(response.get(),MsgBean.class);
                        if (msgBean.getResult()==10000) {
                            L.i("PushUser","-------》推送成功"+msgBean.getMsg());
                        }else {
                            L.i("PushUser","-------》推送失败");
                        }
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
