package com.lede.second_23.ui.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.WelcomeActivity;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.ProgressDialogUtils;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.StatusBarUtil;
import com.lede.second_23.utils.T;
import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;



/**
 * Created by ShiZheQing on 2016/11/14.
 * Activity 基类
 */
public class BaseActivity extends AppCompatActivity implements OnResponseListener<String> {

    private Dialog loadingDialog;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StatusBarUtil.StatusBarLightMode(this);

        mGson = new Gson();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //创建进度条对话框
        loadingDialog = ProgressDialogUtils.createLoadingDialog(this, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStart(int i) {
        loadingDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);

    }

    @Override
    public void onSucceed(int i, Response<String> response) {
        if (response.responseCode() == 401) {
            L.e("basePager:" + response.responseCode() + response.get());

            MsgBean msgBean = mGson.fromJson(response.get(), MsgBean.class);
            if ("token超时重新登录".equals(msgBean.getMsg()) || "用户不存在".equals(msgBean.getMsg())) {
                //登陆过期

                T.showShort(this, "登陆过期请重新登陆");
                SPUtils.remove(this, GlobalConstants.TOKEN);
                SPUtils.remove(this, GlobalConstants.USERNAME);
                SPUtils.remove(this, GlobalConstants.USER_HEAD_IMG);
                SPUtils.remove(this, GlobalConstants.CERTIFICATION);
                finish();
                startActivity(new Intent(this, WelcomeActivity.class));
            }
        }else if(response.responseCode() >= 400 && response.responseCode() < 500){
            if(!TextUtils.isEmpty(response.get())){
//                MsgBean msgBean = mGson.fromJson(response.get(), MsgBean.class);
//                T.showShort(this,msgBean.msg);
                Log.i("TAG", "onSucceed: --------"+response.get());
            }
        }
    }

    @Override
    public void onFailed(int i, Response<String> response) {
        T.showShort(this,"网络访问失败");
    }

    @Override
    public void onFinish(int i) {
        if (loadingDialog != null &&loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 开启activity的动画效果
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
    }

    /**
     * 退出activity的动画效果
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }




}
