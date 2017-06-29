package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.T;
import com.lede.second_23.utils.TimeCountUtil;
import com.lede.second_23.utils.Validator;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int SEND_VALIDATE = 1000;
    private static final int VERIFY_VALIDATE = 2000;
    private static final int SEND_PASSWORD = 3000;

    @Bind(R.id.et_register_activity_phone)
    EditText et_register_activity_phone;
    @Bind(R.id.et_register_activity_pwd)
    EditText et_register_activity_pwd;
    @Bind(R.id.et_register_activity_validate)
    EditText et_register_activity_validate;
    @Bind(R.id.tv_register_activity_register)
    TextView tv_register_activity_register;
    @Bind(R.id.bt_register_activity_send)
    Button bt_register_activity_send;

    private RequestQueue requestQueue;
    //手机号
    private String phone;
    //密码
    private String pwd;
    //验证码
    private String validate;
    //安全码
    private String safeCode;
    //json返回的msg
    private String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();
    }


    @OnClick({R.id.tv_register_activity_register, R.id.bt_register_activity_send
            ,R.id.iv_register_activity_back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.bt_register_activity_send:
                phone = et_register_activity_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    T.showShort(this, "手机号码不能为空哦");
                    return;
                }
                if (!Validator.isMobile(phone)) {
                    T.showShort(this, "请输入正确的手机号码");
                    return;
                }
                bt_register_activity_send.setClickable(false);
                //访问服务器获取验证码
                getValidateFromServce(phone);
                break;
            case R.id.tv_register_activity_register:
                phone = et_register_activity_phone.getText().toString().trim();
                pwd = et_register_activity_pwd.getText().toString().trim();
                validate = et_register_activity_validate.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    T.showShort(this, "手机号码不能为空哦");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    T.showShort(this, "密码不能为空哦");
                    return;
                }
                if (!Validator.isMobile(phone)) {
                    T.showShort(this, "请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(validate)) {
                    T.showShort(this, "验证码不能为空哦");
                    return;
                }
                if (!Validator.isPassword(pwd)) {
                    T.showShort(this, "请输入格式正确的密码");
                    return;
                }
                tv_register_activity_register.setClickable(false);
                submitValidateToServce(phone, validate);
                break;
            case R.id.iv_register_activity_back:
                finish();
                break;
        }
    }

    //获取验证码
    public void getValidateFromServce(String phone) {
        //创建获取验证码的请求
        Request<String> sendRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/sendRegisterSms", RequestMethod.POST);
        sendRequest.add("phone", phone);
        requestQueue.add(SEND_VALIDATE, sendRequest, this);
    }

    //验证手机和验证码
    public void submitValidateToServce(String phone, String validate) {
        Request<String> sendRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/verifSms", RequestMethod.POST);
        sendRequest.add("keyword", phone);
        sendRequest.add("code", validate);
        requestQueue.add(VERIFY_VALIDATE, sendRequest, this);
    }

    //提交密码
    public void submitPassWordToServce(String pwd, String safeCode) {
        Request<String> sendRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/register", RequestMethod.POST);
        sendRequest.add("password", pwd);
        sendRequest.add("safeCode", safeCode);
        requestQueue.add(SEND_PASSWORD, sendRequest, this);
    }

    @Override
    public void onStart(int what) {


    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case SEND_VALIDATE:
                bt_register_activity_send.setClickable(true);
                if (response.responseCode() == 200) {
                    String json = response.get();
                    try {
                        JSONObject jsonobject = new JSONObject(json);
                        msg = jsonobject.getString("msg");
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //修改按钮
                    Log.i("TAG", "onSucceed: 获取验证码返回的数据:" + response.get());
                    TimeCountUtil timeCountUtil = new TimeCountUtil(this, 60000, 1000, bt_register_activity_send);
                    timeCountUtil.start();
                }
                break;
            case VERIFY_VALIDATE:
                tv_register_activity_register.setClickable(true);
                if (response.responseCode() == 200) {
//                    L.e("注册的返回数据" + response.get());
                    String json = response.get();
                    Log.i("TAGG", "onSucceed: 注册返回的数据" + response.get());
                    try {
                        JSONObject jsonobject = new JSONObject(json);
                        msg = jsonobject.getString("msg");
                        if (msg.equals("验证成功")) {
                            JSONObject jo_data = jsonobject.getJSONObject("data");
                            safeCode = jo_data.getString("safeCode");
                            submitPassWordToServce(pwd, safeCode);
                        } else {
                            Toast.makeText(this, "验证码验证失败:" + msg, Toast.LENGTH_SHORT).show();

                        }

//                        Toast.makeText(this, "注册返回的safeCode:"+safeCode, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

//                    parseJson(json);
                    Log.i("TAG", "onSucceed: 验证验证码返回的数据" + response.get());
//                    Toast.makeText(this, "验证验证码返回的数据"+response.get(), Toast.LENGTH_SHORT).show();

//                    finish();

                }
                break;
            case SEND_PASSWORD:
                if (response.responseCode() == 200) {
                    //修改按钮
//                    TimeCountUtil timeCountUtil = new TimeCountUtil(this, 60000, 1000, btnRegisterPassWordNextStep);
//                    timeCountUtil.start_1();
                    String json = response.get();
                    try {
                        JSONObject jsonobject = new JSONObject(json);
                        msg = jsonobject.getString("msg");
                        if (msg.equals("注册成功")) {
                            finish();
                        }else{
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                            tv_register_activity_register.setClickable(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    Log.i("TAG", "注册onSucceed: " + response.get());
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
}
