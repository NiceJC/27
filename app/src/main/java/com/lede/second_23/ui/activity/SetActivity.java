package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.LocationBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.glide.GlideCatchUtil;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.ielse.widget.RangeSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends AppCompatActivity {

    @Bind(R.id.tv_set_activity_age)
    TextView tv_age;
    @Bind(R.id.rsb_set_activity_age)
    RangeSeekBar rsb_age;
    @Bind(R.id.tv_set_activity_save)
    TextView tv_save;
    @Bind(R.id.sb_set_activity_distance)
    SeekBar sb_distance;
    @Bind(R.id.rl_set_activity_sex)
    RelativeLayout rl_sex;
    @Bind(R.id.tv_set_activity_sex)
    TextView tv_sex;
    @Bind(R.id.tv_set_activity_distance)
    TextView tv_distance;
    @Bind(R.id.tv_set_activity_catchsize)
    TextView tv_catchSize;
    private Context mContext;
    private float minAge = 0;
    private float maxAge = 41;
    private RequestQueue requestQueue;
    private Gson mGson;
    private int yourChoice=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        mContext = this;
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        mGson = new Gson();
        qureyLatType();
        initController();
        initData();
    }

    private void initController() {
        rsb_age.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max) {
                minAge = min;
                maxAge = max;
                if ((int) max == 41) {
                    tv_age.setText((int) min + "-40+");
                } else {
                    tv_age.setText((int) min + "-" + (int) max);
                }
            }
        });
        sb_distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_distance.setText(i + 1 + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {
        tv_catchSize.setText(GlideCatchUtil.getInstance().getCacheSize());
        if (SPUtils.contains(mContext, GlobalConstants.SET_SEX)) {
            tv_sex.setText((String) SPUtils.get(mContext, GlobalConstants.SET_SEX, ""));
        } else {
            tv_sex.setText("全部人");
        }
        if (SPUtils.contains(mContext, GlobalConstants.SET_MINAGE)) {
            rsb_age.setValue((float) SPUtils.get(mContext, GlobalConstants.SET_MINAGE, 0.0f), (float) SPUtils.get(mContext, GlobalConstants.SET_MAXAGE, 41.0f));
            minAge = (float) SPUtils.get(mContext, GlobalConstants.SET_MINAGE, 0.0f);
            maxAge = (float) SPUtils.get(mContext, GlobalConstants.SET_MAXAGE, 41.0f);
            if ((int) maxAge == 41) {
                tv_age.setText((int) minAge + "-40+");
            } else {
                tv_age.setText((int) minAge + "-" + (int) maxAge);
            }
        } else {
            rsb_age.setValue(0.0f, 41.0f);
        }
        if (SPUtils.contains(mContext, GlobalConstants.SET_DISTANCE)) {
            sb_distance.setProgress((int) SPUtils.get(mContext, GlobalConstants.SET_DISTANCE, 9) - 1);
        } else {
            sb_distance.setProgress(9);
        }
    }

    @OnClick({R.id.rl_set_activity_sex, R.id.tv_set_activity_save, R.id.ll_set_activity_exit
            , R.id.ll_set_activity_clear, R.id.ll_set_activity_about, R.id.iv_set_activity_back
            , R.id.ll_set_activity_clear_location,R.id.ll_set_activity_update})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.rl_set_activity_sex:
                showDialog();
                break;
            case R.id.tv_set_activity_save:
                SPUtils.put(mContext, GlobalConstants.SET_SEX, tv_sex.getText().toString());
                SPUtils.put(mContext, GlobalConstants.SET_DISTANCE, Integer.parseInt(tv_distance.getText().toString()));
                SPUtils.put(mContext, GlobalConstants.SET_MINAGE, minAge);
                SPUtils.put(mContext, GlobalConstants.SET_MAXAGE, maxAge);
                finish();
                break;
            case R.id.ll_set_activity_exit:
                /**
                 * //设置中筛选条件性别
                 public static final String SET_SEX="SET_SEX";
                 //设置中筛选条件最小年龄
                 public static final String SET_MINAGE="SET_MINAGE";
                 //设置中筛选条件最大年龄
                 public static final String SET_MAXAGE="SET_MAXAGE";
                 //设置中筛选的距离
                 public static final String SET_DISTANCE="SET_DISTANCE";
                 */
//                SPUtils.remove(this,GlobalConstants.TOKEN);
//                SPUtils.remove(this,GlobalConstants.USERID);
//                SPUtils.remove(this,GlobalConstants.HEAD_IMG);
//                SPUtils.remove(this,GlobalConstants.SET_SEX);
//                SPUtils.remove(this,GlobalConstants.SET_MINAGE);
//                SPUtils.remove(this,GlobalConstants.SET_MAXAGE);
//                SPUtils.remove(this,GlobalConstants.SET_DISTANCE);
                SPUtils.clear(this);
                startActivity(new Intent(this, LoginActivity.class));
//                MainActivity.instance.onDestroy();
                MainActivity.instance.finish();
                finish();
                break;
            case R.id.ll_set_activity_clear:
                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                tv_catchSize.setText(GlideCatchUtil.getInstance().getCacheSize());
                break;
            case R.id.ll_set_activity_about:
                startActivity(new Intent(this, AboutWeActivity.class));
                break;
            case R.id.iv_set_activity_back:
                finish();
                break;
            case R.id.ll_set_activity_clear_location:
//                clear_distance();
                show_loactionChoiceDialog();
                break;
            case R.id.ll_set_activity_update:
                UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#3b5998"));
//                UpdateHelper.getInstance().autoUpdate(getPackageName(), false, 5000);
//                UpdateHelper.getInstance().setDebugMode(true);
                UpdateHelper.getInstance().manualUpdate("com.lede.second_23");
                break;
        }
    }

    private void qureyLatType(){
        Request<String> qureyLatType_Request = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/userDistance", RequestMethod.POST);
        qureyLatType_Request.add("userId", (String) SPUtils.get(this, GlobalConstants.USERID, ""));
        requestQueue.add(200, qureyLatType_Request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                L.i(response.get());
                parseLoactionType(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void parseLoactionType(String json) {
        LocationBean locationBean = mGson.fromJson(json, LocationBean.class);
        yourChoice=Integer.parseInt(locationBean.getData().getUserAmap().getType());
    }

    /**
     * 设置地理地址开关
     */
    private void clear_distance(int type) {
        Request<String> clear_distance_Request = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/updateLatType", RequestMethod.POST);
        clear_distance_Request.add("userId", (String) SPUtils.get(this, GlobalConstants.USERID, ""));
        clear_distance_Request.add("type", type);
        requestQueue.add(100, clear_distance_Request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response)
            {
                L.i(response.get());
//                if (yourChoice==0) {
//                    SPUtils.put(SetActivity.this,GlobalConstants.IS_SHOW_LOCATION,true);
//                }else {
//                    SPUtils.put(SetActivity.this,GlobalConstants.IS_SHOW_LOCATION,false);
//                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                L.i(response.get());
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void showDialog() {
        final String items[] = {"全部人", "男", "女"};
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("选择性别"); //设置标题
        builder.setIcon(R.mipmap.logo);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(mContext, items[which], Toast.LENGTH_SHORT).show();
                tv_sex.setText(items[which]);
            }
        });
//        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(mContext, "确定", Toast.LENGTH_SHORT).show();
//            }
//        });
        builder.create().show();
    }



    private void show_loactionChoiceDialog() {
        final String[] items = {"打开向好友分享位置", "关闭向好友分享位置"};

        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(SetActivity.this);
        singleChoiceDialog.setTitle("位置开关");
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                yourChoice = which;
            }
        };
        singleChoiceDialog.setSingleChoiceItems(items, yourChoice, clickListener);
//        if (((Boolean) SPUtils.get(SetActivity.this, GlobalConstants.IS_SHOW_LOCATION, true))) {
//            yourChoice=0;
//            singleChoiceDialog.setSingleChoiceItems(items, 0, clickListener);
//        } else {
//            yourChoice=1;
//            singleChoiceDialog.setSingleChoiceItems(items, 1, clickListener);
//        }
//        // 第二个参数是默认选项，此处设置为0
//        singleChoiceDialog.setSingleChoiceItems(items, 0,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        yourChoice = which;
//                    }
//                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            clear_distance(yourChoice);

                    }
                });
        singleChoiceDialog.show();
    }
}

