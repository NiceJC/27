package com.lede.second_23.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.UpUserInfoBean;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.ProgressDialogUtils;
import com.lede.second_23.utils.SPUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditInformationActivity extends AppCompatActivity implements OnResponseListener<String> {


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
    DIYImageView circle_iv_editinformation_touxiang;
    @Bind(R.id.tv_edit_information_activity_school)
    TextView tvEditInformationActivitySchool;

    private String selectedImg;
    private RequestQueue requestQueue;
    private boolean isBoy = true;
    private Context mContext;
    private int jumpType;
    private boolean isEditNickName = false;

    private static final int CREATE_USER = 1000;
    private static final int LOAD_USER = 2000;
    private static final int UPDATE_USER = 3000;
    private static final int UPIMG_USER = 4000;
    private String currentNickName = "";
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);
        mContext = this;
        requestQueue = GlobalConstants.getRequestQueue();
        Intent intent = getIntent();
        jumpType = intent.getIntExtra("jumpType", 2);
        if (jumpType != 1) {
            loadUserInfo();
        }

    }


    /**
     * requestCode 0:昵称 1:性别 2:个性签名 3:职业
     *
     * @param view
     */
    @OnClick({R.id.tv_edit_information_activity_nickname, R.id.tv_edit_information_activity_sign
            , R.id.rl_edit_information_activity_sex, R.id.rl_edit_information_activity_age
            , R.id.tv_edit_information_activity_updata, R.id.rl_edit_information_activity_city
            , R.id.rl_edit_information_activity_job, R.id.circle_iv_editinformation_touxiang
            , R.id.iv_edit_information_activity_back,R.id.rl_edit_information_activity_school})
    public void onclick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_edit_information_activity_nickname:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("body", tv_edit_information_activity_nickname.getText().toString().trim());
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_edit_information_activity_sign:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("body", tv_edit_information_activity_sign.getText().toString().trim());
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_edit_information_activity_sex:
                intent = new Intent(this, SexDialogActivity.class);
                intent.putExtra("sex", isBoy);
                startActivityForResult(intent, 3);
                break;
//            case R.id.rl_edit_information_activity_sign:
//                intent = new Intent(this, EditActivity.class);
//                intent.putExtra("type", 2);
//                intent.putExtra("body",tv_edit_information_activity_sign.getText().toString().trim());
//                startActivityForResult(intent, 2);
//                break;
            case R.id.rl_edit_information_activity_age:
                showAgeDialog();
                break;
            case R.id.tv_edit_information_activity_updata:
                if (tv_edit_information_activity_nickname.equals("昵称")) {
                    Toast.makeText(mContext, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }else if (tv_edit_information_activity_age.equals("年龄")){
                    Toast.makeText(mContext, "请选择年龄", Toast.LENGTH_SHORT).show();
                    return;
                }else if (tv_edit_information_activity_sex.equals("性别")){
                    Toast.makeText(mContext, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (jumpType == 1) {
                    if (!TextUtils.isEmpty(tv_edit_information_activity_nickname.getText().toString().trim())) {
                        CreateUserInfo();//创建用户请求
                    } else {
                        Toast.makeText(mContext, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (selectedImg != null) {
                        updateUserImg();
                        updateUserInfo();
                    } else {
                        updateUserInfo();
                    }
                }
//
                break;
//            case R.id.rl_edit_information_activity_userpic:
////                selectorPhoto();
//                break;
//            case R.id.rl_edit_information_activity_marry:
//
//                break;
            case R.id.rl_edit_information_activity_city:
                showCityDialog();
                break;
            case R.id.rl_edit_information_activity_job:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("body", tv_edit_information_activity_hobby.getText().toString().trim());
                startActivityForResult(intent, 2);
                break;
            case R.id.circle_iv_editinformation_touxiang:
                FunctionOptions options = new FunctionOptions.Builder()
                        .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                        .setCropMode(FunctionConfig.CROP_MODEL_1_1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
                        .setCompress(true) //是否压缩
                        .setEnablePixelCompress(false) //是否启用像素压缩
                        .setEnableQualityCompress(false) //是否启质量压缩
                        .setMaxSelectNum(1) // 可选择图片的数量
                        .setMinSelectNum(1)// 图片或视频最低选择数量，默认代表无限制
                        .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                        .setVideoS(0)// 查询多少秒内的视频 单位:秒
                        .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                        .setEnablePreview(true) // 是否打开预览选项
                        .setEnableCrop(true) // 是否打开剪切选项
                        .setCircularCut(true)// 是否采用圆形裁剪
//                        .setCheckedBoxDrawable() // 选择图片样式
//                        .setRecordVideoDefinition() // 视频清晰度
//                        .setRecordVideoSecond() // 视频秒数
//                        .setCustomQQ_theme()// 可自定义QQ数字风格，不传就默认是蓝色风格
                        .setGif(false)// 是否显示gif图片，默认不显示
//                        .setCropW() // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
//                        .setCropH() // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
//                        .setMaxB() // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb左右
//                        .setPreviewColor(Color.parseColor("")) //预览字体颜色
//                        .setCompleteColor() //已完成字体颜色
//                        .setPreviewTopBgColor()//预览图片标题背景色
//                        .setPreviewBottomBgColor() //预览底部背景色
//                        .setBottomBgColor() //图片列表底部背景色
//                        .setGrade() // 压缩档次 默认三档
                        .setCheckNumMode(true) //QQ选择风格
//                        .setCompressQuality() // 图片裁剪质量,默认无损
                        .setImageSpanCount(3) // 每行个数
//                        .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
//                        .setCompressW() // 压缩宽 如果值大于图片原始宽高无效
//                        .setCompressH() // 压缩高 如果值大于图片原始宽高无效
//                        .setThemeStyle() // 设置主题样式
//                        .setPicture_title_color() // 设置标题字体颜色
//                        .setPicture_right_color() // 设置标题右边字体颜色
//                        .setLeftBackDrawable() // 设置返回键图标
//                        .setStatusBar() // 设置状态栏颜色，默认是和标题栏一致
                        .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                        .setNumComplete(false) // 0/9 完成  样式
                        .setClickVideo(true)// 点击声音
                        .create();
                PictureConfig.getInstance().init(options).openPhoto(this, resultCallback);
                break;
            case R.id.iv_edit_information_activity_back:
                finish();
                break;
            case R.id.rl_edit_information_activity_school:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("body", tvEditInformationActivitySchool.getText().toString().trim());
                startActivityForResult(intent, 4);
                break;
        }
    }


    /**
     * 城市选择Dialog
     */
    private void showCityDialog() {
        CityPicker cityPicker = new CityPicker.Builder(EditInformationActivity.this)
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
            }else if (requestCode==4&&resultCode==4){
                tvEditInformationActivitySchool.setText(data.getStringExtra("body"));
            }
        }


    }

    /**
     * 请求服务器获取用户信息
     */
    public void loadUserInfo() {
        Request<String> loadUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/" + SPUtils.get(this, GlobalConstants.USERID, "") + "/detail", RequestMethod.GET);
        loadUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(LOAD_USER, loadUserRequest, this);

    }

    /**
     * 修改用户头像请求
     */
    private void updateUserImg() {
        Request<String> updateUserImgRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/changeImg", RequestMethod.POST);
        updateUserImgRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        updateUserImgRequest.add("pic", new File(selectedImg));
        requestQueue.add(UPIMG_USER, updateUserImgRequest, this);
    }

    /**
     * 修改用户请求
     */
    public void updateUserInfo() {
        Request<String> updateUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/update", RequestMethod.POST);
        updateUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        updateUserRequest.add("wechat", tv_edit_information_activity_hobby.getText().toString().trim());
        updateUserRequest.add("address", tv_edit_information_activity_city.getText().toString().trim());
        updateUserRequest.add("qq", tv_edit_information_activity_constellation.getText().toString().trim());
        updateUserRequest.add("note", tv_edit_information_activity_sign.getText().toString().trim() + "");
//        updateUserRequest.add("hometown",tv_edit_information_activity_marry.getText().toString().trim());
        updateUserRequest.add("hobby", tv_edit_information_activity_age.getText().toString().trim());
        if (isEditNickName) {
            updateUserRequest.add("nickName", tv_edit_information_activity_nickname.getText().toString().trim());
        }
        updateUserRequest.add("sex", isBoy);
        updateUserRequest.add("hometown",tvEditInformationActivitySchool.getText().toString().trim());
        requestQueue.add(UPDATE_USER, updateUserRequest, this);

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
        createUserRequest.add("hobby", tv_edit_information_activity_age.getText().toString().trim());
        createUserRequest.add("nickName", tv_edit_information_activity_nickname.getText().toString().trim());
        createUserRequest.add("sex", isBoy);
        createUserRequest.add("hometown",tvEditInformationActivitySchool.getText().toString().trim());
        requestQueue.add(CREATE_USER, createUserRequest, this);

    }

    @Override
    public void onStart(int what) {
        switch (what) {
            case CREATE_USER:
                dialog = ProgressDialogUtils.createLoadingDialog(EditInformationActivity.this, "正在创建用户信息请稍后");
                dialog.show();
                break;
        }
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case CREATE_USER:
                Log.i("TAG", "onSucceed: " + response.get());
                L.i("CREATE_USER_onSucceed:" + response.get());
                parseUpdate_UserInfo(response.get());
                break;
            case LOAD_USER:
                Log.i("TAG", "onSucceed: " + response.get());
                L.i("LOAD_USER_onSucceed:" + response.get());
                parmeUserInfoJson(response.get());
                break;
            case UPDATE_USER:
                Log.i("TAG", "UPDATE_USERonSucceed: " + response.get());
                L.i("UPDATE_USER_onSucceed:" + response.get());
                parseUpdate_UserInfo(response.get());
                break;
            case UPIMG_USER:
                Log.i("TAG", "UPIMG_USERonSucceed: " + response.get());
                L.i("UPIMG_USER_onSucceed:" + response.get());
                break;
        }
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
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(mContext, "保存信息成功", Toast.LENGTH_SHORT).show();
                SPUtils.put(mContext, GlobalConstants.IS_EDITINFO, true);
                if (jumpType == 1) {

                    Intent intent = new Intent(this, WelcomeActivity.class);
                    startActivity(intent);
                    MyApplication.instance.getRongIMTokenService();
                }

                finish();
            } else if (upUserInfoBean.getResult() == 10019) {
                Toast.makeText(mContext, "用户名已存在", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onFailed(int what, Response<String> response) {
        switch (what) {
            case CREATE_USER:
                Log.i("TAG", "onFailed: " + response.get());
                L.i("CREATE_USER_onFailed:" + response.get());
                break;
            case LOAD_USER:
                Log.i("TAG", "onFailed: " + response.get());
                L.i("LOAD_USER_onFailed:" + response.get());
//                parmeUserInfoJson(response.get());
                break;
            case UPDATE_USER:
                Log.i("TAG", "UPDATE_USERon_onFailed: " + response.get());
                L.i("UPDATE_USER_onFailed:" + response.get());
                break;
            case UPIMG_USER:
                Log.i("TAG", "UPIMG_USER_onFailed: " + response.get());
                L.i("UPIMG_USER_onFailed:" + response.get());
                break;
        }
    }

    @Override
    public void onFinish(int what) {

    }

    /**
     * 解析用户信息
     */
    private void parmeUserInfoJson(String json) {
        Gson gson = new Gson();
        UserInfoBean userInfoBean = gson.fromJson(json, UserInfoBean.class);
        if (userInfoBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            tv_edit_information_activity_nickname.setText(userInfoBean.getData().getInfo().getNickName());
            currentNickName = userInfoBean.getData().getInfo().getNickName();
            if (userInfoBean.getData().getInfo().getSex().toString().trim().equals("男")) {
                isBoy = true;
                tv_edit_information_activity_sex.setText("男");
//            iv_edit_information_activity_sex.setImageResource(R.mipmap.sex_boy);
            } else {
                isBoy = false;
                tv_edit_information_activity_sex.setText("女");
//            iv_edit_information_activity_sex.setImageResource(R.mipmap.sex_girl);
            }
            if (userInfoBean.getData().getInfo().getNote()!=null) {
                tv_edit_information_activity_sign.setText(userInfoBean.getData().getInfo().getNote());

            }
            if (userInfoBean.getData().getInfo().getHobby()!=null) {
                tv_edit_information_activity_age.setText(userInfoBean.getData().getInfo().getHobby());

            }
            if (userInfoBean.getData().getInfo().getQq()!=null) {
                tv_edit_information_activity_constellation.setText(userInfoBean.getData().getInfo().getQq());

            }
//        tv_edit_information_activity_marry.setText(userInfoBean.getData().getInfo().getHometown());
            if (userInfoBean.getData().getInfo().getWechat()!=null) {
                tv_edit_information_activity_hobby.setText(userInfoBean.getData().getInfo().getWechat());

            }
            if (userInfoBean.getData().getInfo().getAddress()!=null) {
                tv_edit_information_activity_city.setText(userInfoBean.getData().getInfo().getAddress());

            }
            if (userInfoBean.getData().getInfo().getHometown()!=null) {
                tvEditInformationActivitySchool.setText(userInfoBean.getData().getInfo().getHometown());

            }
            Glide.with(this)
                    .load(userInfoBean.getData().getInfo().getImgUrl())
//                .placeholder(R.mipmap.loading)
                    .into(circle_iv_editinformation_touxiang);
        }
    }

    /**
     * 头像选择器回调
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
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
                    .into(circle_iv_editinformation_touxiang);
        }
    };
}
