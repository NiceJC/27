package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.T;
import com.lede.second_23.utils.Validator;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册、忘记密码 设置密码页面 根据type区分 0 注册时设置密码 1 忘记密码时设置密码
 */
public class ForgetPassword_PassWordActivity extends BaseActivity implements OnResponseListener<String> {

    private RequestQueue requestQueue;

    private static final int SEND_PASSWORD=1000;
    @Bind(R.id.et_forget_password_pwd_activity_pwd)
    EditText et_forget_password_pwd_activity_pwd;
    @Bind(R.id.et_forget_password_pwd_activity_repwd)
    EditText et_forget_password_pwd_activity_repwd;
    @Bind(R.id.tv_forget_password_pwd_activity_update)
    ImageView tv_forget_password_pwd_activity_update;

    private String pwd;
    private String repwd;
    private String safeCode;
    private int type;
    private String url="/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password__pass_word);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        safeCode=intent.getStringExtra("safeCode");
//        if (type==0) {
//            url="/register";
//            Toast.makeText(this, "注册时设置密码", Toast.LENGTH_SHORT).show();
//        }else{
            url="/forgetPassword";
//            Toast.makeText(this, "忘记密码时设置密码", Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(this, "safeCode"+safeCode, Toast.LENGTH_SHORT).show();

        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();
    }

    @OnClick(R.id.tv_forget_password_pwd_activity_update)
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.tv_forget_password_pwd_activity_update:

                pwd = et_forget_password_pwd_activity_pwd.getText().toString().trim();
                repwd = et_forget_password_pwd_activity_repwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd)) {
                    T.showShort(this, "密码不能为空哦");
                    return;
                }
                if (!Validator.isPassword(pwd) || !Validator.isPassword(repwd)) {
                    T.showShort(this, "请输入格式正确的密码");
                    return;
                }
                if(!pwd.equals(repwd)){
                    T.showShort(this, "请确认两次密码一致");
                    return;
                }
                tv_forget_password_pwd_activity_update.setClickable(false);
                submitPassWordToServce(pwd,safeCode);
                break;
        }
    }
    //提交密码
    public void submitPassWordToServce(String pwd,String safeCode){
        Request<String> sendRequest = NoHttp.createStringRequest(GlobalConstants.URL + url, RequestMethod.POST);
        sendRequest.add("password",pwd);
        sendRequest.add("safeCode",safeCode);
        requestQueue.add(SEND_PASSWORD, sendRequest, this);
    }
    @Override
    public void onSucceed(int i, Response<String> response) {
        super.onSucceed(i,response);
        switch (i) {
            case SEND_PASSWORD:  //提交密码
                tv_forget_password_pwd_activity_update.setClickable(true);
                if (response.responseCode() == 200) {
                    //修改按钮
//                    TimeCountUtil timeCountUtil = new TimeCountUtil(this, 60000, 1000, tv_forget_password_activity_next);
//                    timeCountUtil.start_1();
//                    Toast.makeText(this, "提交密码返回的数据"+response.get(), Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "注册onSucceed: "+response.get());
                    finish();
                }
                break;
        }
    }

    @Override
    public void onFailed(int i, Response response) {

        Log.i("TAG", "onFailed: ------ResponseCode----"+response.responseCode());
        //设置注册按钮可用
        tv_forget_password_pwd_activity_update.setClickable(true);
        T.showShort(this, "网络请求失败");
    }
}
