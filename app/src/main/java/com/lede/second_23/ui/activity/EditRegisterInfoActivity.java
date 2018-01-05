package com.lede.second_23.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.UpUserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.interface_utils.OnUploadFinish;
import com.lede.second_23.service.PickService;
import com.lede.second_23.service.UploadService;
import com.lede.second_23.utils.ProgressDialogUtils;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.SnackBarUtil;
import com.lede.second_23.utils.StatusBarUtil;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.qiniu.android.storage.UploadManager;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 注册成功后登录时 创建用户信息
 * 保证用户上传新的头像  新的相册  以及创建基本的用户信息
 * Created by ld on 17/11/10.
 */

public class EditRegisterInfoActivity extends AppCompatActivity implements OnResponseListener<String> {

    @Bind(R.id.rl_edit_information_activity_sex)
    RelativeLayout rl_edit_information_activity_sex;
    @Bind(R.id.tv_edit_information_activity_nickname)
    TextView tv_edit_information_activity_nickname;
    @Bind(R.id.tv_edit_information_activity_age)
    TextView tv_edit_information_activity_age;
    @Bind(R.id.tv_edit_information_activity_constellation)
    TextView tv_edit_information_activity_constellation;
    @Bind(R.id.tv_edit_information_activity_sign)
    TextView tv_edit_information_activity_sign;
    @Bind(R.id.tv_edit_information_activity_sex)
    TextView tv_edit_information_activity_sex;
    @Bind(R.id.tv_edit_information_activity_city)
    TextView tv_edit_information_activity_city;
    @Bind(R.id.tv_edit_information_activity_hobby)
    TextView tv_edit_information_activity_hobby;
    @Bind(R.id.circle_iv_editinformation_touxiang)
    ImageView circle_iv_editinformation_touxiang;
    @Bind(R.id.tv_edit_information_activity_school)
    TextView tvEditInformationActivitySchool;
    @Bind(R.id.upload_pic)
    LinearLayout uploadPic;
    @Bind(R.id.upload_button)
    ImageView uploadButton;
    @Bind(R.id.upload_image1)
    ImageView uploadImage1;
    @Bind(R.id.upload_image2)
    ImageView uploadImage2;


    private String selectedImg;
    private RequestQueue requestQueue;
    private boolean isBoy = true;
    private Context mContext;
    private int jumpType;
    private boolean isEditNickName = false;

    private static final int CREATE_USER = 1000;

    private String currentNickName = "";
    private Dialog dialog;
    private static final int GET_QIUNIUTOKEN = 23123;
    private static final int UPLOADREQUEST = 44144;
    private FunctionOptions optionsPic; //选择图片
    private List<LocalMedia> selectMedia = new ArrayList<>(); //选择操作返回的媒体存放
    private UploadManager uploadManager;


    //该页面要上传的三部分内容 要确保都ok了 再跳转下一页面
    private boolean isPrortraitOK = false;
    private boolean isPhotoOK = false;
    private boolean isUserInfoOK = false;
    public MultiTransformation transformation;

    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_infor);
        StatusBarUtil.transparencyBar(this);
        ButterKnife.bind(this);
        mGson = new Gson();
        mContext = this;
        requestQueue = GlobalConstants.getRequestQueue();
        uploadManager = MyApplication.getUploadManager();
        transformation = new MultiTransformation(
                new CenterCrop(this),
                new RoundedCornersTransformation(this, 10, 0, RoundedCornersTransformation.CornerType.ALL)
        );
        showCompleteDialog();
        Glide.with(mContext)
                .load(R.mipmap.head_defaut)
                .bitmapTransform(new CropCircleTransformation(EditRegisterInfoActivity.this))
                .into(circle_iv_editinformation_touxiang);
    }


    /**
     * requestCode 0:昵称 1:性别 2:个性签名 3:职业
     *
     * @param view
     */
    @OnClick({R.id.upload_button
            , R.id.tv_edit_information_activity_nickname, R.id.tv_edit_information_activity_sign
            , R.id.rl_edit_information_activity_sex, R.id.rl_edit_information_activity_age
            , R.id.tv_edit_information_activity_updata, R.id.rl_edit_information_activity_city
            , R.id.rl_edit_information_activity_job, R.id.circle_iv_editinformation_touxiang
            , R.id.iv_edit_information_activity_back, R.id.rl_edit_information_activity_school})
    public void onclick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.upload_button:
                upLoadAlbum();
                break;
            case R.id.tv_edit_information_activity_nickname:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("body", "");
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_edit_information_activity_sign:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("body", "");
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_edit_information_activity_sex:
                intent = new Intent(this, SexDialogActivity.class);
                intent.putExtra("sex", isBoy);
                startActivityForResult(intent, 3);
                break;

            case R.id.rl_edit_information_activity_age:
                showAgeDialog();
                break;
            case R.id.tv_edit_information_activity_updata:
                if (tv_edit_information_activity_nickname.getText().toString().trim().equals("") || tv_edit_information_activity_nickname.getText().toString().trim().equals("昵称")) {
                    Toast.makeText(mContext, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tv_edit_information_activity_sign.getText().toString().trim().equals("") || tv_edit_information_activity_sign.getText().toString().trim().equals("签名")) {
                    Toast.makeText(mContext, "请输入签名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tv_edit_information_activity_city.getText().toString().trim().equals("") || tv_edit_information_activity_city.getText().toString().trim().equals("城市")) {
                    Toast.makeText(mContext, "请输入城市", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tv_edit_information_activity_hobby.getText().toString().trim().equals("") || tv_edit_information_activity_hobby.getText().toString().trim().equals("爱好")) {
                    Toast.makeText(mContext, "请输入爱好", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tv_edit_information_activity_age.getText().toString().trim().equals("") || tv_edit_information_activity_age.getText().toString().trim().equals("年龄")) {
                    Toast.makeText(mContext, "请选择年龄", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tv_edit_information_activity_sex.getText().toString().trim().equals("") || tv_edit_information_activity_sex.getText().toString().trim().equals("性别")) {
                    Toast.makeText(mContext, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isPrortraitOK) {
                    Toast.makeText(mContext, "请上传头像", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isPhotoOK) {
                    Toast.makeText(mContext, "请至少上传一张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                CreateUserInfo();//创建用户请求
                break;

            case R.id.rl_edit_information_activity_city:
                showCityDialog();
                break;
            case R.id.rl_edit_information_activity_job:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("body", "");
                startActivityForResult(intent, 2);
                break;
            case R.id.circle_iv_editinformation_touxiang:
                PickService pickService = new PickService(this);
                pickService.pickPortrait(portraitPickCallback);
                break;
            case R.id.iv_edit_information_activity_back:
                finish();
                break;
            case R.id.rl_edit_information_activity_school:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("body", "");
                startActivityForResult(intent, 4);
                break;
        }
    }


    //给用户一个完善信息的提示
    private void showCompleteDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.complete_info_dialog))
                .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.toComplete:

                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setExpanded(false).create();
        dialogPlus.show();

    }

    /**
     * 城市选择Dialog
     */
    private void showCityDialog() {
        CityPicker cityPicker = new CityPicker.Builder(EditRegisterInfoActivity.this)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("浙江省")
                .city("杭州市")
                .onlyShowProvinceAndCity(true)
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
//                String district = citySelected[2];
                //邮编
//                String code = citySelected[3];
                tv_edit_information_activity_city.setText(province + " " + city);
            }

            @Override
            public void onCancel() {
//                Toast.makeText(EditInformationActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 设置年龄对话框
     */
    private void showAgeDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker,
                                          int year, int month, int dayOfMonth) {
                        Calendar mycalendar = Calendar.getInstance();//获取现在时间
                        String nowyear = String.valueOf(mycalendar.get(Calendar.YEAR));//获取年份
                        int age = Integer.parseInt(nowyear);
                        int birth = age - year;
                        tv_edit_information_activity_age.setText("" + birth);
                        tv_edit_information_activity_constellation.setText(getAstro(month + 1, dayOfMonth));
                    }
                };
        Dialog dialog = new DatePickerDialog(this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 自动换算星座
     *
     * @param month
     * @param day
     * @return
     */
    private String getAstro(int month, int day) {
        String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
                "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
        int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return astro[index];
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0 && resultCode == 0) {
                if (data.getStringExtra("body") != null) {
                    /**
                     * 要判断是否修改了用户名
                     */
                    if (currentNickName.equals(data.getStringExtra("body"))) {
                        isEditNickName = false;
                    } else {
                        isEditNickName = true;
                    }
                    tv_edit_information_activity_nickname.setText(data.getStringExtra("body"));
                }
            } else if (requestCode == 1 && resultCode == 1) {
                if (data.getStringExtra("body") != null) {
                    tv_edit_information_activity_sign.setText(data.getStringExtra("body"));
                }
            } else if (requestCode == 3 && resultCode == 3) {
                if (data.getBooleanExtra("sex", true)) {
                    tv_edit_information_activity_sex.setText("男");
                    isBoy = true;
                } else {
                    tv_edit_information_activity_sex.setText("女");
                    isBoy = false;
                }
            } else if (requestCode == 2 && resultCode == 2) {
                tv_edit_information_activity_hobby.setText(data.getStringExtra("body"));
            } else if (requestCode == 4 && resultCode == 4) {
                tvEditInformationActivitySchool.setText(data.getStringExtra("body"));
            }
        }


    }


    /**
     * 创建用户信息请求
     */
    public void CreateUserInfo() {
        Request<String> createUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/create", RequestMethod.POST);

        createUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        if (selectedImg == null) {
            Toast.makeText(mContext, "请选择头像", Toast.LENGTH_SHORT).show();
            return;
        }
        createUserRequest.add("pic", new File(selectedImg));
        createUserRequest.add("wechat", tv_edit_information_activity_hobby.getText().toString().trim());
        createUserRequest.add("address", tv_edit_information_activity_city.getText().toString().trim());
        createUserRequest.add("qq", tv_edit_information_activity_constellation.getText().toString().trim());
        createUserRequest.add("note", tv_edit_information_activity_sign.getText().toString().trim() + "");
//        createUserRequest.add("hometown",tv_edit_information_activity_marry.getText().toString().trim());

        String age;
        if (tv_edit_information_activity_age.getText() == null || tv_edit_information_activity_age.getText().equals("")) {
            age = "18";
        } else {
            age = tv_edit_information_activity_age.getText().toString().trim();
        }
        createUserRequest.add("hobby", age);
        createUserRequest.add("nickName", tv_edit_information_activity_nickname.getText().toString().trim());
        createUserRequest.add("sex", isBoy);
        createUserRequest.add("hometown", tvEditInformationActivitySchool.getText().toString().trim());
        requestQueue.add(CREATE_USER, createUserRequest, this);

    }

    @Override
    public void onStart(int what) {
        switch (what) {
            case CREATE_USER:
                dialog = ProgressDialogUtils.createLoadingDialog(EditRegisterInfoActivity.this, "正在创建用户信息请稍后");
                dialog.setCancelable(false);
                dialog.show();

                break;
        }
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case CREATE_USER:
                if (dialog != null) {
                    dialog.dismiss();
                }
                ;
                parseUpdate_UserInfo(response.get());
                break;


        }
    }


    @Override
    public void onFailed(int what, Response<String> response) {
        switch (what) {
            case CREATE_USER:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;

        }
    }

    @Override
    public void onFinish(int what) {

    }

    private void parseUpdate_UserInfo(String json) {
        Gson mGson = new Gson();
        UpUserInfoBean upUserInfoBean = mGson.fromJson(json, UpUserInfoBean.class);
        if (upUserInfoBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            if (upUserInfoBean.getResult() == 10000) {

                Toast.makeText(mContext, "保存信息成功", Toast.LENGTH_SHORT).show();
                SPUtils.put(mContext, GlobalConstants.IS_EDITINFO, true);

                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                MyApplication.instance.getRongIMTokenService();

            } else if (upUserInfoBean.getResult() == 10019) {
                Toast.makeText(mContext, "用户名已存在", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void upLoadAlbum() {
        PickService pickService = new PickService(this);
        pickService.pickPhoto(5,new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {

                OnUploadFinish onUploadFinish = new OnUploadFinish() {
                    @Override
                    public void success() {
                        isPhotoOK = true;
                        SnackBarUtil.getInstance(uploadPic, EditRegisterInfoActivity.this, R.string.upload_success).show();
                    }

                    @Override
                    public void failed() {
                        SnackBarUtil.getInstance(uploadPic, EditRegisterInfoActivity.this, R.string.upload_failed).show();

                    }
                };
                if (list != null && list.size() != 0) {
                    if (list.size() == 1) {
                        Glide.with(mContext).load(list.get(0).getCompressPath()).bitmapTransform(transformation).into(uploadImage1);
                    } else {
                        Glide.with(mContext).load(list.get(0).getCompressPath()).bitmapTransform(transformation).into(uploadImage1);
                        Glide.with(mContext).load(list.get(1).getCompressPath()).bitmapTransform(transformation).into(uploadImage2);

                    }
                    UploadService uploadService = new UploadService(EditRegisterInfoActivity.this);
                    uploadService.upload(list, onUploadFinish);
                }

            }

            @Override
            public void onSelectSuccess(LocalMedia localMedia) {
            }
        });
    }

    /**
     * 头像选择器回调
     */
    private PictureConfig.OnSelectResultCallback portraitPickCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            // 多选回调

        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            // 单选回调
            selectedImg = media.getCutPath();
            Glide.with(mContext)
                    .load(selectedImg)
                    .bitmapTransform(new CropCircleTransformation(EditRegisterInfoActivity.this))
                    .into(circle_iv_editinformation_touxiang);
            isPrortraitOK = true;
        }

    };


}
