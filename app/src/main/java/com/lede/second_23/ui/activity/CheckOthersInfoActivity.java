package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.thinkcool.circletextimageview.CircleTextImageView;
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
 * 查看别人个人中心
 */
public class CheckOthersInfoActivity extends AppCompatActivity implements OnResponseListener<String> {

    @Bind(R.id.tv_edit_information_activity_nickname)
    TextView tv_nickname;
    @Bind(R.id.tv_edit_information_activity_age)
    TextView tv_age;
    @Bind(R.id.tv_edit_information_activity_constellation)
    TextView tv_constellation;
    @Bind(R.id.tv_edit_information_activity_sign)
    TextView tv_sign;
//    @Bind(R.id.iv_edit_information_activity_sex)
//    ImageView iv_sex;
//    @Bind(R.id.tv_edit_information_activity_marry)
//    TextView tv_marry;
    @Bind(R.id.tv_edit_information_activity_city)
    TextView tv_city;
    @Bind(R.id.tv_edit_information_activity_hobby)
    TextView tv_job;
    @Bind(R.id.circle_iv_editinformation_touxiang)
    CircleTextImageView circle_iv_touxiang;
//    @Bind(R.id.tv_edit_information_activity_title)
//    TextView tv_tilte;
    @Bind(R.id.tv_edit_information_activity_updata)
    TextView tv_update;


    private String userid;
    private Gson mGson;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);
        userid=getIntent().getStringExtra("userid");
        mGson = new Gson();
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        userInfoService(userid);
        tv_update.setVisibility(View.GONE);
//        tv_tilte.setText("查看他人资料");

    }

    private void userInfoService(String userid) {
        Request<String> userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/"+userid+"/heDetail", RequestMethod.POST);
        requestQueue.add(100,userInfoRequest,this);
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        L.i(response.get());
        parserJson(response.get());
    }




    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }

    @OnClick({R.id.iv_edit_information_activity_back})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.iv_edit_information_activity_back:
                finish();
                break;
        }
    }

    private void parserJson(String s) {
        UserInfoBean userInfoBean=mGson.fromJson(s, UserInfoBean.class);
        UserInfoBean.DataBean.InfoBean infoBean=userInfoBean.getData().getInfo();
        tv_nickname.setText(infoBean.getNickName());
//        tv_tilte.setText(infoBean.getNickName());
        tv_age.setText(infoBean.getHobby());
        tv_constellation.setText(infoBean.getQq());
        tv_sign.setText(infoBean.getNote());
//        if (infoBean.getSex().equals("男")) {
//            iv_sex.setImageResource(R.mipmap.sex_boy);
//        }else {
//            iv_sex.setImageResource(R.mipmap.sex_girl);
//        }
//        tv_marry.setText(infoBean.getHometown());
        tv_city.setText(infoBean.getAddress());
        tv_job.setText(infoBean.getWechat());
        Glide.with(this)
                .load(infoBean.getImgUrl())
                .error(R.mipmap.loading)
                .placeholder(R.mipmap.loading)
                .into(circle_iv_touxiang);
    }
}
