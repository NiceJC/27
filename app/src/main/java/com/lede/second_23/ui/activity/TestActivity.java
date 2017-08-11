package com.lede.second_23.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.PermissionsChecker;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.UiUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

public class TestActivity extends AppCompatActivity {

    private Button bt_text_activity_jump;
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private ImageView iv_logo;
    private RequestQueue requestQueue;
    private boolean isNetWork=true;

    private boolean isHasInfo=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        requestQueue = GlobalConstants.getRequestQueue();
        mPermissionsChecker = new PermissionsChecker(this);
        if (SPUtils.contains(this, GlobalConstants.TOKEN)) {
            loadUserInfo();
        }

        bt_text_activity_jump = (Button) findViewById(R.id.bt_text_activity_jump);
        bt_text_activity_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtils.contains(TestActivity.this, GlobalConstants.TOKEN)) {
                    startActivity(new Intent(TestActivity.this,MainActivity.class));
                }else {
                    startActivity(new Intent(TestActivity.this,LoginActivity.class));
                }

            }
        });
        UiUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(TestActivity.this)
                        .load(R.mipmap.start)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new GlideDrawableImageViewTarget(iv_logo, 1));
                delayedRun();
            }
        },600);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    /**
     * 请求服务器获取用户信息
     */
    public void loadUserInfo() {
        Request<String> loadUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/" + SPUtils.get(this, GlobalConstants.USERID, "") + "/detail", RequestMethod.GET);
        loadUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(100, loadUserRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                parmeUserInfoJson(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                isNetWork=false;
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
        if (userInfoBean.getMsg().equals("用户没有登录")) {
            isHasInfo=false;
        }else {
            if (userInfoBean.getData().getInfo()==null) {
                isHasInfo=false;
            }else {
                isHasInfo=true;
            }
        }

    }

    /**
     * 延时任务
     */
    private void delayedRun() {
        UiUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if ((Boolean) SPUtils.get(TestActivity.this, GlobalConstants.TOKENUNUSEFULL,true)) {
//                    startActivity(new Intent(TestActivity.this,LoginActivity.class));
//                }else {
//
//                }
                Intent intent=null;
                if (isNetWork) {
                    if (SPUtils.contains(TestActivity.this, GlobalConstants.TOKEN)&&!(Boolean) SPUtils.get(TestActivity.this, GlobalConstants.TOKENUNUSEFULL,true)) {
                        if (isHasInfo) {
                            intent=new Intent(TestActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                                intent=new Intent(TestActivity.this,EditInformationActivity.class);
                                intent.putExtra("jumpType",1);
                                Toast.makeText(TestActivity.this, "您还没有创建过用户信息", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                        }

                    }else {
                        intent=new Intent(TestActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(TestActivity.this, "无法连接到服务器,请检查网络", Toast.LENGTH_SHORT).show();
                    intent=new Intent(TestActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 3000);
    }
}
