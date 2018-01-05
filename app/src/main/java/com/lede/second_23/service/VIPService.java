package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.ApplyVIPResultBean;
import com.lede.second_23.bean.CheckVIPBean;
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

import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;

/**
 * Created by ld on 17/11/22.
 */

public class VIPService {

    private static final int REQUEST_APPLY_VIP = 1505;
    private static final int REQUEST_CHECK_VIP = 1515;
    private static final int REQUEST_VIRIFY_VIP = 15105;


    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String> applyVIPRequest = null;
    private Request<String> checkVIPRequest = null;

    private Request<String> verifyVIPInfor=null;
    private PersonalAlbumBean.DataBean.UserInfo userInfo;

    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public VIPService() {

    }

    public VIPService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case REQUEST_APPLY_VIP:
                        parseApplyResult(response.get());
                        break;

                    case REQUEST_CHECK_VIP:
                        parseCheckResult(response.get());
                        break;
                    case REQUEST_VIRIFY_VIP:
                        parseVerifyVIPInfo(response.get());

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
                    case REQUEST_APPLY_VIP:
                    case REQUEST_CHECK_VIP:
                    case REQUEST_VIRIFY_VIP:
                        myCallBack.onFail(response.get().toString());
                        break;
                    default:
                        break;
                }
            }
        };
    }



    //申请VIP 需要类型和天数 和一个带参的回调接口
    public void applyVIP(String type, int days,final MyCallBack myCallBack){

        this.myCallBack = myCallBack;
        applyVIPRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/vip/vipOpen", RequestMethod.POST);
        applyVIPRequest.add("token",  (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        applyVIPRequest.add("type", type);
        applyVIPRequest.add("day", days);
        RequestServer.getInstance().request(REQUEST_APPLY_VIP, applyVIPRequest, simpleResponseListener);

    }


    /**
     * {"msg":"vip开通成功","result":10000}
     * @param s
     */
    private void parseApplyResult(String s) {
        ApplyVIPResultBean applyVIPResultBean = mGson.fromJson(s, ApplyVIPResultBean.class);

        if(applyVIPResultBean.getResult()==10000){
            String result = applyVIPResultBean.getMsg();
            if(!result.equals("")){
                myCallBack.onSuccess(result);
            }

        }


    }

    //验证VIP的身份
    public void checkVIP(MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        checkVIPRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/vip/vipVerifyTime", RequestMethod.POST);
        checkVIPRequest.add("token",  (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));



        RequestServer.getInstance().request(REQUEST_CHECK_VIP, checkVIPRequest, simpleResponseListener);

    }

    private void parseCheckResult(String s){
        CheckVIPBean checkVIPBean=mGson.fromJson(s,CheckVIPBean.class);
        if(checkVIPBean!=null){
            SPUtils.put(mActivity,VIPSTATUS,checkVIPBean.getMsg());
            myCallBack.onSuccess(checkVIPBean.getMsg());
        }
    }



    //支付成功后  向后台提交支付结果
    public void verifyVIPInfo(String userId, String trade_no, String total_fee, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        verifyVIPInfor = NoHttp.createStringRequest(GlobalConstants.URL + "/vip/vipVerifyInfo", RequestMethod.POST);
        verifyVIPInfor.add("userId", userId);
        verifyVIPInfor.add("trade_no", trade_no);
        verifyVIPInfor.add("total_fee", total_fee);

        RequestServer.getInstance().request(REQUEST_VIRIFY_VIP, verifyVIPInfor, simpleResponseListener);

    }
    private void parseVerifyVIPInfo(String s){
        MsgBean msgBean=mGson.fromJson(s,MsgBean.class);
        if(msgBean.getResult()==10000){

            myCallBack.onSuccess(null);
        }
    }

}
