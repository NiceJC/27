package com.lede.second_23.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.Md5Util;
import com.lede.second_23.utils.PermissionsChecker;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.UiUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.tools.NetUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements OnResponseListener<String> {

    private static final int REQUEST_CODE = 0; // 请求码
    private static final int REPORTFIRST = 4000;
    private static final int LOADUSERIFO = 1000;
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
//    @Bind(R.id.iv_welcome_pic01)
//    ImageView ivWelcomePic01;
//    @Bind(R.id.iv_welcome_pic02)
//    ImageView ivWelcomePic02;
//    @Bind(R.id.iv_welcome_pic03)
//    ImageView ivWelcomePic03;
//    @Bind(R.id.tv_welcome_login)
//    TextView tvWelcomeLogin;
//    @Bind(R.id.tv_welcome_register)
//    TextView tvWelcomeRegister;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private RequestQueue requestQueue;
    private boolean isNetWork = true;
//    private float curTranslationX;
//    private float curTranslationY;
//    private ObjectAnimator pic01MoveX;
//    private ObjectAnimator pic01MoveY;
//    private ObjectAnimator pic02MoveX;
//    private ObjectAnimator pic02MoveY;
//    private ObjectAnimator pic03MoveX;
//    private ObjectAnimator pic03MoveY;
//    private AnimatorSet animatorSet01;
//    private AnimatorSet animatorSet02;
//    private AnimatorSet animatorSet03;
    private boolean isHasInfo = false;
//    public Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    animatorSet01.start();
//                    break;
//                case 2:
//                    animatorSet02.start();
//                    break;
//                case 3:
//                    animatorSet03.start();
//                    break;
//                default:
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        requestQueue = GlobalConstants.getRequestQueue();
        Log.i("WelcomeActivity", "onCreate: "+NetUtil.getLocalIPAddress());
        Glide.with(WelcomeActivity.this)
                .load(R.mipmap.newstart)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(ivLogo, 1));
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }else {
            if (SPUtils.contains(this, GlobalConstants.TOKEN)) {
                loadUserInfo();
            } else {
                UiUtils.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        delayedRun();
                    }
                }, 3000);
            }
            if (!SPUtils.contains(this, GlobalConstants.ISFIRST)) {
                SPUtils.put(this, GlobalConstants.ISFIRST, "ISFRIST");
                reportFirst();
            }
        }




    }

//    private void initAnimation() {
//        curTranslationX = ivWelcomePic01.getTranslationX();
//        curTranslationY = ivWelcomePic01.getTranslationY();
//
//        pic01MoveX = ObjectAnimator.ofFloat(ivWelcomePic01, "translationX", curTranslationX, -2000f, 2000f, curTranslationX);
//        pic01MoveY = ObjectAnimator.ofFloat(ivWelcomePic01, "translationY", curTranslationY, 2000f, curTranslationY);
//        pic01rotation = ObjectAnimator.ofFloat(ivWelcomePic01, "rotation", 15f, -345f);
//
//        pic02MoveX = ObjectAnimator.ofFloat(ivWelcomePic02, "translationX", curTranslationX, -2000f, 2000f, curTranslationX);
//        pic02MoveY = ObjectAnimator.ofFloat(ivWelcomePic02, "translationY", curTranslationY, 2000f, curTranslationY);
//        pic02rotation = ObjectAnimator.ofFloat(ivWelcomePic02, "rotation", 7.5f, -352.5f);
//
//        pic03MoveX = ObjectAnimator.ofFloat(ivWelcomePic03, "translationX", curTranslationX, -2000f, 2000f, curTranslationX);
//        pic03MoveY = ObjectAnimator.ofFloat(ivWelcomePic03, "translationY", curTranslationY, 2000f, curTranslationY);
//        pic03rotation = ObjectAnimator.ofFloat(ivWelcomePic03, "rotation", 0f, -360f);
//
//
//        animatorSet01 = new AnimatorSet();
//        animatorSet02 = new AnimatorSet();
//        animatorSet02.play(pic02MoveX).with(pic02MoveY).with(pic02rotation);
//        animatorSet03 = new AnimatorSet();
//        animatorSet03.play(pic01MoveX).with(pic01MoveY).with(pic01rotation);
//        animatorSet01.play(pic03MoveX).with(pic03MoveY).with(pic03rotation);
//        animatorSet02.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                Message msg = new Message();
//                msg.what = 3;
//                mHandler.sendMessageDelayed(msg, 1000);
//            }
//        });
//        animatorSet03.addListener(new AnimatorListenerAdapter() {
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                animatorSet01.start();
//                Message msg = new Message();
//                msg.what = 1;
//                mHandler.sendMessage(msg);
//
//            }
//        });
//        animatorSet01.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                Message msg = new Message();
//                msg.what = 2;
//                mHandler.sendMessageDelayed(msg, 1000);
//            }
//
//
//        });
//        animatorSet02.setDuration(3000);
//        animatorSet03.setDuration(3000);
//        animatorSet01.setDuration(3000);
//        animatorSet01.start();
//    }


    /**
     * 上报首次打开app
     */
    private void reportFirst() {
        Request<String> reportFirst = NoHttp.createStringRequest(GlobalConstants.URL + "/verifyGdtData.cgi", RequestMethod.POST);
        reportFirst.add("muid", Md5Util.getMD5(GlobalConstants.getDeviceToken()));
        reportFirst.add("conv_type", "MOBILEAPP_ACTIVITE");
        reportFirst.add("appid", GlobalConstants.APPID);
        reportFirst.add("client_ip", NetUtil.getLocalIPAddress());
        reportFirst.add("app_type", "unionandroid");
        reportFirst.add("conv_time", System.currentTimeMillis() / 1000);
        requestQueue.add(REPORTFIRST, reportFirst, this);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }else {
            if (SPUtils.contains(this, GlobalConstants.TOKEN)) {
                loadUserInfo();
            } else {
                UiUtils.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        delayedRun();
                    }
                }, 3000);
            }
            if (!SPUtils.contains(this, GlobalConstants.ISFIRST)) {
                SPUtils.put(this, GlobalConstants.ISFIRST, "ISFRIST");
                reportFirst();
            }
        }
    }

    /**
     * 请求服务器获取用户信息
     */
    public void loadUserInfo() {
        Request<String> loadUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/" + SPUtils.get(this, GlobalConstants.USERID, "") + "/detail", RequestMethod.GET);
        loadUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(LOADUSERIFO, loadUserRequest, this);
    }

    /**
     * 解析用户信息
     */
    private void parmeUserInfoJson(String json) {
        Gson gson = new Gson();
        UserInfoBean userInfoBean = gson.fromJson(json, UserInfoBean.class);
        if (userInfoBean.getMsg().equals("用户没有登录")) {
            SPUtils.remove(WelcomeActivity.this,GlobalConstants.TOKEN);
            isHasInfo = false;
        } else if (userInfoBean.getMsg().equals("token超时请重新登录")) {
            SPUtils.put(WelcomeActivity.this, GlobalConstants.TOKENUNUSEFULL, true);
        } else {
            if (userInfoBean.getData().getInfo() == null) {
                isHasInfo = false;
            } else {
                isHasInfo = true;
            }
        }
        UiUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Glide.with(WelcomeActivity.this)
//                        .load(R.mipmap.start)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .into(new GlideDrawableImageViewTarget(ivLogo, 1));
                delayedRun();
            }
        }, 2000);

    }

    /**
     * 延时任务
     */
    private void delayedRun() {
        UiUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if ((Boolean) SPUtils.get(WelcomeActivity.this, GlobalConstants.TOKENUNUSEFULL,true)) {
//                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
//                }else {
//
//                }
                Intent intent = null;
                if (isNetWork) {
                    if (SPUtils.contains(WelcomeActivity.this, GlobalConstants.TOKEN) && !(Boolean) SPUtils.get(WelcomeActivity.this, GlobalConstants.TOKENUNUSEFULL, true)) {
                        if (isHasInfo) {
                            intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            intent = new Intent(WelcomeActivity.this, EditRegisterInfoActivity.class);
                            Toast.makeText(WelcomeActivity.this, "您还没有创建过用户信息", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();

                        }

                    } else {
//                        tvWelcomeLogin.setVisibility(View.VISIBLE);
//                        tvWelcomeRegister.setVisibility(View.VISIBLE);
                        intent = new Intent(WelcomeActivity.this, LoginOrRegisterActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(WelcomeActivity.this, "无法连接到服务器,请检查网络", Toast.LENGTH_SHORT).show();
//                    intent = new Intent(WelcomeActivity.this, LoginOrRegisterActivity.class);
//                    startActivity(intent);
//                    tvWelcomeLogin.setVisibility(View.VISIBLE);
//                    tvWelcomeRegister.setVisibility(View.VISIBLE);
                }

            }
        }, 3000);
    }

//    @OnClick({R.id.tv_welcome_login, R.id.tv_welcome_register})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_welcome_login:
//                startActivity(new Intent(this, LoginActivity.class));
//                finish();
//                break;
//            case R.id.tv_welcome_register:
//                startActivity(new Intent(this, RegisterActivity.class));
//                finish();
//                break;
//        }
//    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("WelcomeActivity", "onSucceed: " + response.get());
        switch (what) {
            case LOADUSERIFO:
                parmeUserInfoJson(response.get());
                break;
            case REPORTFIRST:

                break;
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (animatorSet01 != null) {
//            animatorSet01.cancel();
//        }
//        if (animatorSet02 != null) {
//            animatorSet02.cancel();
//        }
//        if (animatorSet03 != null) {
//            animatorSet03.cancel();
//        }
//        if (mHandler != null) {
//            mHandler.removeCallbacksAndMessages(null);
//        }
    }


}
