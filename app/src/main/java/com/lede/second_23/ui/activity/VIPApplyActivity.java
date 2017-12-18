package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.TIME;
import static com.lede.second_23.global.GlobalConstants.TOTAL;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/11/21.
 */

public class VIPApplyActivity extends BaseActivity {

    @Bind(R.id.portrait)
    ImageView portrait;

    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.apply_1)
    LinearLayout apply1;
    @Bind(R.id.apply_3)
    LinearLayout apply3;
    @Bind(R.id.apply_6)
    LinearLayout apply6;
    @Bind(R.id.apply_12)
    LinearLayout apply12;

    @Bind(R.id.text_1_1)
    TextView text11;
    @Bind(R.id.text_1_2)
    TextView text12;
    @Bind(R.id.text_1_3)
    TextView text13;

    @Bind(R.id.text_3_1)
    TextView text31;
    @Bind(R.id.text_3_2)
    TextView text32;
    @Bind(R.id.text_3_3)
    TextView text33;

    @Bind(R.id.text_6_1)
    TextView text61;
    @Bind(R.id.text_6_2)
    TextView text62;
    @Bind(R.id.text_6_3)
    TextView text63;

    @Bind(R.id.text_12_1)
    TextView text121;
    @Bind(R.id.text_12_2)
    TextView text122;
    @Bind(R.id.text_12_3)
    TextView text123;



    private int mouthCount=3;
    private String totalAmount="";


    public static VIPApplyActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vip_apply);

        ButterKnife.bind(this);
        instance=this;

        mouthCount=3;
        totalAmount="30.00";
        setSelected(3);
        getUserInfo();

    }
    public void getUserInfo(){
        MyCallBack myCallBack =new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                PersonalAlbumBean.DataBean.UserInfo userInfo= (PersonalAlbumBean.DataBean.UserInfo) o;
                Glide.with(VIPApplyActivity.this)
                        .load(userInfo.getImgUrl())
                        .bitmapTransform(new CropCircleTransformation(VIPApplyActivity.this))
                        .into(portrait);
                userName.setText(userInfo.getNickName());
            }

            @Override
            public void onFail(String mistakeInfo) {

                Toast.makeText(VIPApplyActivity.this,"请求发生错误",Toast.LENGTH_SHORT).show();
            }
        };

        UserInfoService userInfoService=new UserInfoService(VIPApplyActivity.this);
        userInfoService.getUserInfo(   (String) SPUtils.get(this, USERID, ""), myCallBack);
    }


    @OnClick({R.id.back,R.id.apply_1,R.id.apply_3,R.id.apply_6,R.id.apply_12,R.id.apply_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.apply_1:
                mouthCount=1;
                totalAmount="12.00";

                setSelected(1);
                break;
            case R.id.apply_3:
                mouthCount=3;
                totalAmount="30.00";
                setSelected(3);
                break;
            case R.id.apply_6:
                mouthCount=6;
                totalAmount="60.00";
                setSelected(6);
                break;
            case R.id.apply_12:
                mouthCount=12;
                totalAmount="90.00";
                setSelected(12);
                break;
            case R.id.apply_confirm:
                Intent intent=new Intent(VIPApplyActivity.this,PayActivity.class);
                intent.putExtra(TIME,mouthCount);
                intent.putExtra(TOTAL,totalAmount);
                startActivity(intent);
                break;
            default:
                break;

        }


    }


    public void setSelected(int mouthCount){
        switch (mouthCount){
            case 1:
                apply1.setSelected(true);
                apply3.setSelected(false);
                apply6.setSelected(false);
                apply12.setSelected(false);

                text11.setTextColor(Color.parseColor("#ffffff"));
                text12.setTextColor(Color.parseColor("#ffffff"));
                text13.setTextColor(Color.parseColor("#ffffff"));

                text31.setTextColor(Color.parseColor("#222222"));
                text32.setTextColor(Color.parseColor("#222222"));
                text33.setTextColor(Color.parseColor("#222222"));

                text61.setTextColor(Color.parseColor("#222222"));
                text62.setTextColor(Color.parseColor("#222222"));
                text63.setTextColor(Color.parseColor("#222222"));

                text121.setTextColor(Color.parseColor("#222222"));
                text122.setTextColor(Color.parseColor("#222222"));
                text123.setTextColor(Color.parseColor("#222222"));


                break;
            case 3:
                apply1.setSelected(false);
                apply3.setSelected(true);
                apply6.setSelected(false);
                apply12.setSelected(false);
                text11.setTextColor(Color.parseColor("#222222"));
                text12.setTextColor(Color.parseColor("#222222"));
                text13.setTextColor(Color.parseColor("#222222"));

                text31.setTextColor(Color.parseColor("#ffffff"));
                text32.setTextColor(Color.parseColor("#ffffff"));
                text33.setTextColor(Color.parseColor("#ffffff"));

                text61.setTextColor(Color.parseColor("#222222"));
                text62.setTextColor(Color.parseColor("#222222"));
                text63.setTextColor(Color.parseColor("#222222"));

                text121.setTextColor(Color.parseColor("#222222"));
                text122.setTextColor(Color.parseColor("#222222"));
                text123.setTextColor(Color.parseColor("#222222"));


                break;
            case 6:
                apply1.setSelected(false);
                apply3.setSelected(false);
                apply6.setSelected(true);
                apply12.setSelected(false);
                text11.setTextColor(Color.parseColor("#222222"));
                text12.setTextColor(Color.parseColor("#222222"));
                text13.setTextColor(Color.parseColor("#222222"));

                text31.setTextColor(Color.parseColor("#222222"));
                text32.setTextColor(Color.parseColor("#222222"));
                text33.setTextColor(Color.parseColor("#222222"));

                text61.setTextColor(Color.parseColor("#ffffff"));
                text62.setTextColor(Color.parseColor("#ffffff"));
                text63.setTextColor(Color.parseColor("#ffffff"));

                text121.setTextColor(Color.parseColor("#222222"));
                text122.setTextColor(Color.parseColor("#222222"));
                text123.setTextColor(Color.parseColor("#222222"));


                break;
            case 12:
                apply1.setSelected(false);
                apply3.setSelected(false);
                apply6.setSelected(false);
                apply12.setSelected(true);
                text11.setTextColor(Color.parseColor("#222222"));
                text12.setTextColor(Color.parseColor("#222222"));
                text13.setTextColor(Color.parseColor("#222222"));

                text31.setTextColor(Color.parseColor("#222222"));
                text32.setTextColor(Color.parseColor("#222222"));
                text33.setTextColor(Color.parseColor("#222222"));

                text61.setTextColor(Color.parseColor("#222222"));
                text62.setTextColor(Color.parseColor("#222222"));
                text63.setTextColor(Color.parseColor("#222222"));

                text121.setTextColor(Color.parseColor("#ffffff"));
                text122.setTextColor(Color.parseColor("#ffffff"));
                text123.setTextColor(Color.parseColor("#ffffff"));


                break;
            default:
                break;
        }
    }


}
