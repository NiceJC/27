package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.StatusBarUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 个人资料卡页面  如果显示的是别人的资料  需要隐藏右上角的编辑键
 * Created by ld on 17/11/1.
 */

public class UserInfoCardActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.edit)
    ImageView edit;
    @Bind(R.id.user_img)
    ImageView userImg;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_sex)
    ImageView userSex;
    @Bind(R.id.user_intro)
    TextView userIntro;
    @Bind(R.id.user_location)
    TextView userLocation;
    @Bind(R.id.user_age)
    TextView userAge;
    @Bind(R.id.user_hobby)
    TextView userHobby;
    @Bind(R.id.user_school)
    TextView userSchool;

    @OnClick({R.id.back,R.id.edit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                Intent intent=new Intent(UserInfoCardActivity.this,EditInformationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private Request<String> userInfoRequest = null;
    private final static int REQUEST_USER_INFO=23451;
    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private String userId;
    private RequestManager requestManager;
    private UserInfoBean.DataBean.InfoBean userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);
        StatusBarUtil.transparencyBar(this);
        ButterKnife.bind(this);
        requestManager=Glide.with(this);
        mGson = new Gson();
        userId=getIntent().getStringExtra(USERID);
        if(!userId.equals((String) SPUtils.get(this, GlobalConstants.USERID, ""))){
            edit.setVisibility(View.GONE);
        }else{
            edit.setVisibility(View.VISIBLE);
        }

//        doRequest();
    }




    private void doRequest() {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        parseUserInfo(response.get());
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        break;
                    default:
                        break;
                }
            }
        };


        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/" + userId + "/heDetail", RequestMethod.POST);
        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);


    }


    private void parseUserInfo(String s) {
        UserInfoBean userInfoBean = mGson.fromJson(s, UserInfoBean.class);
        userInfo = userInfoBean.getData().getInfo();
        setUserInfo();
    }

    public void setUserInfo() {
        userName.setText(userInfo.getNickName());
        if (userInfo.getSex().equals("男")) {

            userSex.setSelected(false);
        } else {
            userSex.setSelected(true);
        }
        userIntro.setText(userInfo.getNote());

        requestManager.load(userInfo.getImgUrl())
                .into(userImg);
        userLocation.setText(userInfo.getAddress());
        userAge.setText(userInfo.getHobby());
        userHobby.setText(userInfo.getWechat());
        userSchool.setText(userInfo.getHometown());
    }

    @Override
    protected void onResume() {
        super.onResume();
        doRequest();
    }
}
