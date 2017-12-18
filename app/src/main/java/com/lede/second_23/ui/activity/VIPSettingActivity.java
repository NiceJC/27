package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.lede.second_23.R;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.VIPService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.VIPPUSHSEX;
import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;

/**
 * Created by ld on 17/11/21.
 */

public class VIPSettingActivity extends BaseActivity {


    public static final int BOY = 1;
    public static final int GIRL = 2;
    public static final int ALL = 3;

    public static final String NOTOPEN = "该用户未开通vip";
    public static final String NOTOVERDUE = "该用户vip未过期";
    public static final String OVERDUE = "该用户vip已过期";

    private int choosenSex ;
    private String VIPStatus;

    @Bind(R.id.sex)
    ImageView sexImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vip_setting);

        ButterKnife.bind(this);

        VIPStatus = (String) SPUtils.get(this, VIPSTATUS, NOTOPEN);
        choosenSex = (int) SPUtils.get(this, VIPPUSHSEX,ALL);

        switch (choosenSex){
            case BOY:
                sexImage.setImageResource(R.mipmap.boy11);
                break;
            case GIRL:
                sexImage.setImageResource(R.mipmap.girl11);
                break;
            case ALL:
                sexImage.setImageResource(R.mipmap.all11);
                break;
            default:
                break;

        }

    }

    @OnClick({R.id.back, R.id.vip_apply, R.id.vip_intro, R.id.vip_push_sex})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.vip_apply:
                Intent intent = new Intent(VIPSettingActivity.this, VIPApplyActivity.class);
                startActivity(intent);
                break;
            case R.id.vip_intro:
                Intent intent1 = new Intent(VIPSettingActivity.this, VIPIntroduceActivity.class);
                startActivity(intent1);
                break;
            case R.id.vip_push_sex:

                //如果用户是vip 直接打开，如果不是，再验证一遍，防止刚刚开通的用户点不开特权
                if (VIPStatus.equals(NOTOVERDUE)) {
                    showChooseSexDialog();
                }else{
                    VIPService vipService = new VIPService(this);
                    vipService.checkVIP(new MyCallBack() {
                        @Override
                        public void onSuccess(Object o) {
                            VIPStatus = (String) SPUtils.get(VIPSettingActivity.this, VIPSTATUS, NOTOPEN);
                            if (VIPStatus.equals(NOTOPEN)) {
                                showNotVIPDialog();
                            } else if (VIPStatus.equals(OVERDUE)) {
                                showOverdueDialog();
                            } else if (VIPStatus.equals(NOTOVERDUE)) {
                                showChooseSexDialog();
                            }
                        }

                        @Override
                        public void onFail(String mistakeInfo) {
                        }
                    });


                }


                break;
            default:
                break;
        }
    }

    //还不是VIP的提示框
    private void showNotVIPDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.no_vip_dialog))
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
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setExpanded(false).create();
        dialogPlus.show();

    }

    //VIP的功能实现框
    private void showChooseSexDialog() {

        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.choose_sex_dialog))
                .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setInAnimation(R.anim.fade_in)
                .setOutAnimation(R.anim.fade_out)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                        ImageView boy = (ImageView) dialog.findViewById(R.id.boy);
                        ImageView girl = (ImageView) dialog.findViewById(R.id.girl);
                        ImageView all = (ImageView) dialog.findViewById(R.id.all);



                        switch (view.getId()) {
                            case R.id.boy:
                                boy.setSelected(true);
                                girl.setSelected(false);
                                all.setSelected(false);
                                choosenSex = BOY;

                                break;

                            case R.id.girl:
                                boy.setSelected(false);
                                girl.setSelected(true);
                                all.setSelected(false);
                                choosenSex = GIRL;
                                break;
                            case R.id.all:
                                boy.setSelected(false);
                                girl.setSelected(false);
                                all.setSelected(true);
                                choosenSex = ALL;
                                break;
                            case R.id.confirm:

                                SPUtils.put(VIPSettingActivity.this,VIPPUSHSEX,choosenSex);
                                switch (choosenSex){
                                    case BOY:
                                        sexImage.setImageResource(R.mipmap.boy11);
                                        break;
                                    case GIRL:
                                        sexImage.setImageResource(R.mipmap.girl11);
                                        break;
                                    case ALL:
                                        sexImage.setImageResource(R.mipmap.all11);
                                        break;
                                    default:
                                        break;

                                }
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setExpanded(false).create();

        ImageView boy = (ImageView) dialogPlus.findViewById(R.id.boy);
        ImageView girl = (ImageView) dialogPlus.findViewById(R.id.girl);
        ImageView all = (ImageView) dialogPlus.findViewById(R.id.all);
        switch (choosenSex){
            case BOY:
                boy.setSelected(true);
                break;
            case GIRL:
                girl.setSelected(true);
                break;
            case ALL:
                all.setSelected(true);
                break;

        }
        dialogPlus.show();

    }

    //过期VIP的续费提示框
    private void showOverdueDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.vip_overdue_dialog))
                .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setInAnimation(R.anim.fade_in)
                .setOutAnimation(R.anim.fade_out)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.cancel:

                                dialog.dismiss();
                                break;

                            case R.id.confirm:
                                Intent intent = new Intent(VIPSettingActivity.this, VIPApplyActivity.class);
                                startActivity(intent);
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
}
