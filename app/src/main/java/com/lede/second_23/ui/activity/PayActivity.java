package com.lede.second_23.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.AliPayResultBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.VIPService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.OrderInfoUtil2_0;
import com.lede.second_23.utils.PayResult;
import com.lede.second_23.utils.SPUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.TIME;
import static com.lede.second_23.global.GlobalConstants.TOTAL;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 支付页面
 * Created by ld on 17/11/20.
 */

public class PayActivity extends BaseActivity {


    @Bind(R.id.total_amount)
    TextView mTotalAmount;
    @Bind(R.id.total_amount_confirm)
    TextView mTotalAmountConfirm;
    @Bind(R.id.time)
    TextView mMouthCount;


    private Gson mGson;
    private int mouthCount;
    private String totalAmount;
    private String subject;

    private String userId;
    private String backTotal_fee;
    private String backTrade_no;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017062607572871";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */

    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCI77J9xwMtw/PA2EQibw1iV1V1eFGlxMk56DjTtPyF7S3nDaa4N4nUKCoViGtuhogvQjbO9jFSHvYkBelNL35PIxHtNKeo/JZDDqme2lXXGNnmkIQQjZttIPIbdR2Aw6b17w72YA8SePJ1PCpVH2iS7MzyDRekUJ/x9nP5pYS7az6lU18b+rrdOWd2lJv19mY4P6oRWdRozc4ZDchgwyN92xBbpfV3fyv0yvp8XdSmqUTxc6OxhZFaWpwJgPGr/1bebMg+MzMvNOB0KQo5wxvgGJr7E94C+3+liOb+rXrR0V7xrNZRCLcgI2qxwofuu3Z1/+q5aXuTEgoqR5sjOYO3AgMBAAECggEAIZaBtn45FmGpCVVCqRuKMePwD0c656k0HmMKhUswYX1CtBjSGv9sbECJygxTJaIUB1t7bBpREK6Ne1qD1i3fEnQssn0m3rlN9hbDnjY5te/fisFJ03GYfRUGrZt1aB8Vdd90URRQvhwyhHUEgSqaEOfKgViawvAPu17TJZnmy71tQnY0Tfq4diNcDaCUiX9P4gEnmt0assc5Kq075baQ8uf+009rThWWWL2xLmAkma24FPz2+9hUwDQjRa48+6w2u3d6dcBW0II6gyJfv4jMlh0vGvbjLS1qeAZHDXifrGn51ocgaFPwtzEH2WeCTxuvA5mXfrLV4v2bx9peaOIagQKBgQDqzSrty9thj0oKU8A6qxRpy7hxi5n0Lw3DsnnmBOlDF+X2n3Ov1OZ+UBrQRB8xqidKEh0g6jVmo4W04ehrTzrQYCzzBIry6nRokyJMzbuZjVolLj5qCzxpUk/Sj1se6zd660KL+FGiJCURaeaK78KV4K9Yx+DVl2aOUN9gqZRw9wKBgQCVTKDr72xfbAlqJbO27AB+Mc+TbI9sCT4ng4oc4qLHvQGbL/xWx1qUGATXW+mMQ4OHTos9eXcMDKOjZSvGp4i4HyPKHyD43JlvVlWSgS92vzMDqSk//IO9MydCFLYtQTzmCiBZnJlxty8ktEX98RHIVyHsWNopMd8ycSQ5Fe2TQQKBgAhZBv+xUblkOMOpOppbXrxsK8CNsAmIarrca40L+6cRAHIGNTy5GwC+pFVnNpyzafYuMq7q5L8jbSuDJJo8FOE3qiFxtebxAvZ81KlaXyMUbXII9NscTyeHb6MSybCNuEk8d/818uEK2+6Ej1e9MnPVqyZC5PFzEohYw7hSDx79AoGAGpODabw63FmM63o+nWcxR0Vyn9mJRj/28m213iJxu4Bnkspd7syZ0RbddcCEuFUkSqeunYjLqjdtwZ42xqv36F6Srl4QLnyGS41gui2I12h9mj84eo1mnf1HFbo+G5wrTP+sdocrwVRMbgoxE10gPztTbJD8dKzOAFqnignfOoECgYB1xqXJfbUypLY6G7kILAlqdnWf6dMjrkFFtqI9GAojTnjpGBscfjNrJOaDxuuLujInVRUJQK+6S+uF+uZf42DYOv0UH9mdaOFo6njGhsTAVvqZ8kRvO8evS7CAKwiN3q/LdQ7RNDIKTbgwXTsT9KMq8ZZ9BOSPur8VJHPaqoI1Kg==";

    private static final int SDK_PAY_FLAG = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        mouthCount=intent.getIntExtra(TIME,3);
        totalAmount=intent.getStringExtra(TOTAL);
        subject="开通27会员"+mouthCount+"个月";
        initView();
        mGson=new Gson();
//        applyVip();


    }


    @OnClick({R.id.back,R.id.pay_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.pay_confirm:

                toPay();
                break;
            default:
                break;
        }

    }

    public void initView(){
        mTotalAmount.setText(totalAmount);
        mTotalAmountConfirm.setText(totalAmount);
        mMouthCount.setText(mouthCount+"个月");

    }


    //发起支付宝支付
    public void toPay(){


        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID,totalAmount,subject);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey =RSA2_PRIVATE ;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey,true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                        parseAlipayBack(resultInfo);


                        Toast.makeText(PayActivity.this, "支付成功,正在开通VIP", Toast.LENGTH_SHORT).show();

                    } else if(TextUtils.equals(resultStatus, "8000")){
                        Toast.makeText(PayActivity.this, "正在处理", Toast.LENGTH_SHORT).show();

                    }else if(TextUtils.equals(resultStatus, "4000")){
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                    }else if(TextUtils.equals(resultStatus, "6001")){
                        Toast.makeText(PayActivity.this, "支付已取消", Toast.LENGTH_SHORT).show();

                    }else if(TextUtils.equals(resultStatus, "6002")){
                        Toast.makeText(PayActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();

                    }else if(TextUtils.equals(resultStatus, "6004")){
                        Toast.makeText(PayActivity.this, "结果未知，请联系客服人员处理", Toast.LENGTH_SHORT).show();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };


    public void applyVip(){

        VIPService VIPService =new VIPService(PayActivity.this);
        VIPService.applyVIP("1", mouthCount*30, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                showVIPSuccessDialog();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }

    //VIP开通成功的提示框
    private void showVIPSuccessDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.vip_open_success))
                .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                .setCancelable(true)
                .setInAnimation(R.anim.fade_in)
                .setOutAnimation(R.anim.fade_out)
                .setGravity(Gravity.CENTER)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.hole_view:
                                dialog.dismiss();
                                VIPApplyActivity.instance.finish();
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setExpanded(false).create();
        dialogPlus.show();

    }
    //支付成功后解析支付宝的返回结果，并将支付结果上报服务器
    private void parseAlipayBack(String s){
        AliPayResultBean aliPayResultBean = mGson.fromJson(s, AliPayResultBean.class);

        backTotal_fee=aliPayResultBean.getAlipay_trade_app_pay_response().getTotal_amount();
        backTrade_no=aliPayResultBean.getAlipay_trade_app_pay_response().getTrade_no();
        userId= (String) SPUtils.get(this,USERID,"");
        VIPService vipService=new VIPService(this);
        vipService.verifyVIPInfo(userId, backTrade_no, backTotal_fee, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                applyVip();

            }

            @Override
            public void onFail(String mistakeInfo) {
            }
        });


    }




}
