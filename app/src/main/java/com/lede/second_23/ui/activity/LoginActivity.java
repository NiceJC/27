package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.LoginBean;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.SPUtils;
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
 * 登陆页面
 */
public class LoginActivity extends AppCompatActivity {

    private static final int LOGIN_REQUEST = 3000;
    @Bind(R.id.et_login_activity_phone)
    EditText etLoginPhone;
    @Bind(R.id.et_login_activity_pwd)
    EditText etLoginPwd;
    @Bind(R.id.tv_login_activity_login)
    TextView tv_login_activity_login;
    @Bind(R.id.btn_login_register)
    TextView btnLoginRegister;
    @Bind(R.id.btn_login_forget_pwd)
    TextView btnLoginForgetPwd;
    @Bind(R.id.activity_login)
    LinearLayout activityLogin;
//    @Bind(R.id.iv_login_back)
//    ImageView ivLoginBack;
    private RequestQueue requestQueue;
    private Gson mGson;

    private static final int LOAD_USER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置5.0以上隐藏actionBar
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.hide();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mGson = new Gson();
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();

    }

    @OnClick({R.id.tv_login_activity_login, R.id.btn_login_register, R.id.btn_login_forget_pwd})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {

            case R.id.tv_login_activity_login:
                //判断登陆信息是否完整并请求服务器
//                Toast.makeText(this, "点击登录", Toast.LENGTH_SHORT).show();
                loginInfoIsFull();
                break;
            case R.id.btn_login_register:
                //跳转到注册页面
                intent=new Intent(this, RegisterActivity.class);
//                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.btn_login_forget_pwd:
                //跳转到忘记密码页面
                intent=new Intent(this, ForgetPassword_PhoneActivity.class);
                startActivity(intent);
//                startActivity(new Intent(this,RePwdActivity.class));
                break;
//            case R.id.iv_login_back:
//                finish();
//                break;
        }
    }

    //判断登陆信息是否完整
    public void loginInfoIsFull() {
        //获取输入框内容
        String phone = etLoginPhone.getText().toString().trim();
        String pwd = etLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            T.showShort(this, "手机号码不能为空哦");
            return;
        }
        if (!Validator.isMobile(phone)) {
            T.showShort(this, "请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            T.showShort(this, "密码不能为空哦");
            return;
        }

        //设置登陆不能点击
        tv_login_activity_login.setClickable(false);
        //请求服务器
        try {
//            Toast.makeText(this, "启动请求", Toast.LENGTH_SHORT).show();
            Login2Servce(phone, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //访问服务器
    public void Login2Servce(String keyword, String pwd) throws Exception {
        Log.i("TAG", "Login2Servce: -------开始请求服务器登录");
        //获取设备id
//        String deviceToken = GlobalConstants.getDeviceToken();
//        //生成token
//        Map<String, Object> map = new HashMap<>();
//        map.put("password", pwd);
//        String token = JwtSignUtil.signer(map);
//        //获取时间戳
//        long time = System.currentTimeMillis();
//        String sign = keyword + deviceToken + token + time;
//        sign = Md5Util.MD5(sign);

        //创建登陆请求
        final Request<String> loginRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/login", RequestMethod.POST);
//        loginRequest.add("deviceToken", deviceToken);
//        loginRequest.add("token", token);
        loginRequest.add("keyword", keyword);
//        loginRequest.add("tm", String.valueOf(time));
//        loginRequest.add("sign", sign);
        loginRequest.add("password",pwd);

        Log.i("TAGG", "Login2Sezrvce: --------->loginRequest"+loginRequest.url());
        //添加请求到请求队列中
        requestQueue.add(LOGIN_REQUEST, loginRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int i) {
                Log.i("TAGG", "onStart: 开始");
            }

            @Override
            public void onSucceed(int i, Response<String> response) {
                tv_login_activity_login.setClickable(true);
                Log.i("TAGG", "onSucceed: ");
                if (response.responseCode() == 200) {
                    String json = response.get();
                    Log.i("TAGG", "onSucceed: 登录的response:"+response.get());
                    parseJson(json);
                } else {
                    if(!TextUtils.isEmpty(response.get())){
                        MsgBean msgBean = mGson.fromJson(response.get(), MsgBean.class);
                        T.showShort(LoginActivity.this, msgBean.msg);
                    }
                }
            }

            @Override
            public void onFailed(int i, Response<String> response) {
                T.showShort(LoginActivity.this, "网络访问失败，请重新登陆");
                Log.i("TAGG", "onFailed: --------->"+response.responseCode());
                tv_login_activity_login.setClickable(true);
            }

            @Override
            public void onFinish(int i) {

            }
        });
    }

    //解析登录成功的用户信息
    public void parseJson(String json) {

        LoginBean loginBean = mGson.fromJson(json, LoginBean.class);
        if (loginBean.getResult()==10001) {
            Toast.makeText(this, "用户名密码错误", Toast.LENGTH_SHORT).show();
        }else {
            //把access_token存储到sp中
            SPUtils.put(this, GlobalConstants.TOKEN, loginBean.getData().getAccess_token());
            SPUtils.put(this, GlobalConstants.EXPIRES_IN, loginBean.getData().getExpires_in());
            SPUtils.put(this, GlobalConstants.USERID,loginBean.getData().getUser().getId());
//  SPUtils.put(this, GlobalConstants.NAME, loginBean.name);
//        SPUtils.put(this, GlobalConstants.CERTIFICATION, loginBean.certification);
            //提示用户登陆成功并退出登陆界面
            T.showShort(this, "登陆成功");
            SPUtils.put(LoginActivity.this, GlobalConstants.TOKENUNUSEFULL,false);
            loadUserInfo();


        }

    }

    /**
     * 请求服务器获取用户信息
     */
    public void loadUserInfo() {
        Request<String> loadUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/" + SPUtils.get(this, GlobalConstants.USERID, "") + "/detail", RequestMethod.GET);
        loadUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(LOAD_USER, loadUserRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                parmeUserInfoJson(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
    /**
     * 解析用户信息
     */
    private void parmeUserInfoJson(String json) {
        Gson gson = new Gson();
        UserInfoBean userInfoBean = gson.fromJson(json, UserInfoBean.class);

        if (userInfoBean.getData().getInfo()==null) {
            SPUtils.put(this,GlobalConstants.HEAD_IMG,"");
            Intent intent=new Intent(this,EditInformationActivity.class);
            intent.putExtra("jumpType",1);
            startActivity(intent);
        }else {
            MyApplication.instance.getRongIMTokenService();
            startActivity(new Intent(this,TestActivity.class));
        }
        finish();
    }

}
