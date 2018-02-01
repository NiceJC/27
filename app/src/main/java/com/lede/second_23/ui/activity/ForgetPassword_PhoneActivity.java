package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.base.BaseActivity;
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

/**
 * 忘记密码页面
 */
public class ForgetPassword_PhoneActivity extends BaseActivity implements OnResponseListener<String> {

    private static final int SEND_REQUEST = 1000;
    private static final int SEND_SUBMIT = 2000;
    private RequestQueue requestQueue;
    @Bind(R.id.et_forget_password_activity_phone)
    EditText et_forget_password_activity_phone;
    @Bind(R.id.et_forget_password_activity_validate)
    EditText et_forget_password_activity_validate;
    @Bind(R.id.bt_forget_password_activity_send)
    Button bt_forget_password_activity_send;
    @Bind(R.id.tv_forget_password_activity_next)
    ImageView tv_forget_password_activity_next;

    //手机号
    private String phone;
    //验证码
    private String validate;
    //安全码
    private String safeCode;
    //json返回的msg
    private String msg;
    //


    private String url = "/sendForgetSms";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password_phone);
        ButterKnife.bind(this);

        url = "/sendForgetSms";

        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();
    }

    @OnClick({R.id.tv_forget_password_activity_next, R.id.bt_forget_password_activity_send
            , R.id.iv_forget_password_activity_back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.bt_forget_password_activity_send:
                //判断信息是否完整
                phone = et_forget_password_activity_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    T.showShort(this, "手机号码不能为空哦");
                    return;
                }
                if (!Validator.isMobile(phone)) {
                    T.showShort(this, "请输入正确的手机号码");
                    return;
                }
                bt_forget_password_activity_send.setClickable(false);
                //访问服务器获取验证码
                getValidateFromServce(phone);
                break;
            case R.id.tv_forget_password_activity_next:
                //判断信息是否完整
                phone = et_forget_password_activity_phone.getText().toString().trim();
                validate = et_forget_password_activity_validate.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    T.showShort(this, "手机号码不能为空哦");
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
                tv_forget_password_activity_next.setClickable(false);
                submitValidateToServce(phone, validate);
                break;
            case R.id.iv_forget_password_activity_back:
                finish();
                break;
        }
    }

    //获取验证码
    public void getValidateFromServce(String phone) {
        //创建获取验证码的请求
        Request<String> sendRequest = NoHttp.createStringRequest(GlobalConstants.URL + url, RequestMethod.POST);

        sendRequest.add("keyword", phone);


        requestQueue.add(SEND_REQUEST, sendRequest, this);
    }

    //验证手机和验证码
    public void submitValidateToServce(String phone, String validate) {
        Request<String> sendRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/verifSms", RequestMethod.POST);
        sendRequest.add("keyword", phone);
        sendRequest.add("code", validate);
        requestQueue.add(SEND_SUBMIT, sendRequest, this);
    }

    @Override
    public void onSucceed(int i, Response<String> response) {
        super.onSucceed(i, response);
        switch (i) {
            case SEND_REQUEST:  //获取验证码
                bt_forget_password_activity_send.setClickable(true);
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
                    TimeCountUtil timeCountUtil = new TimeCountUtil(this, 60000, 1000, bt_forget_password_activity_send);
                    timeCountUtil.start();
                }
                break;
            case SEND_SUBMIT:      //验证验证码
                tv_forget_password_activity_next.setClickable(true);
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
                            Intent intent = new Intent(this, ForgetPassword_PassWordActivity.class);
//                            intent.putExtra("type", type);
                            intent.putExtra("safeCode", safeCode);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "验证失败", Toast.LENGTH_SHORT).show();
                        }

//                        Toast.makeText(this, "注册返回的safeCode:"+safeCode, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

//                    parseJson(json);
//                    Log.i("TAG", "onSucceed: 验证验证码返回的数据" + response.get());
//                    Toast.makeText(this, "验证验证码返回的数据"+response.get(), Toast.LENGTH_SHORT).show();

//                    finish();

                }
                break;
        }
    }

    @Override
    public void onFailed(int i, Response response) {

        Log.i("TAG", "onFailed: ------ResponseCode----" + response.responseCode());
        //设置注册按钮可用
        bt_forget_password_activity_send.setClickable(true);
        tv_forget_password_activity_next.setClickable(true);
        T.showShort(this, "网络请求失败");
    }
}
