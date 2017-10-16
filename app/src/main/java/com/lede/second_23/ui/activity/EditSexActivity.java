package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 废弃
 */
public class EditSexActivity extends AppCompatActivity {

    private boolean isBoy=true;

    @Bind(R.id.iv_edit_sex_activity_boy)
    ImageView iv_edit_sex_activity_boy;
    @Bind(R.id.iv_edit_sex_activity_girl)
    ImageView iv_edit_sex_activity_girl;
    @Bind(R.id.tv_edit_sex_activity_boy)
    TextView tv_edit_sex_activity_boy;
    @Bind(R.id.tv_edit_sex_activity_girl)
    TextView tv_edit_sex_activity_girl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        ButterKnife.bind(this);
        isBoy=getIntent().getBooleanExtra("sex",true);
        initView();
        showNormalDialog();
    }

    private void initView() {
        if (isBoy) {
            iv_edit_sex_activity_boy.setImageResource(R.mipmap.click_boy);
            tv_edit_sex_activity_boy.setTextColor(Color.parseColor("#5795f1"));
            iv_edit_sex_activity_girl.setImageResource(R.mipmap.no_click_girl);
            tv_edit_sex_activity_girl.setTextColor(Color.parseColor("#b3b3b3"));
        }else {
            iv_edit_sex_activity_boy.setImageResource(R.mipmap.no_click_boy);
            tv_edit_sex_activity_boy.setTextColor(Color.parseColor("#b3b3b3"));
            iv_edit_sex_activity_girl.setImageResource(R.mipmap.click_girl);
            tv_edit_sex_activity_girl.setTextColor(Color.parseColor("#f573c6"));
        }
    }

    @OnClick({R.id.iv_edit_sex_activity_boy,R.id.iv_edit_sex_activity_girl,R.id.tv_edit_sex_activity_updata,R.id.tv_edit_sex_activity_cancel})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.iv_edit_sex_activity_boy:
                iv_edit_sex_activity_boy.setImageResource(R.mipmap.click_boy);
                tv_edit_sex_activity_boy.setTextColor(Color.parseColor("#5795f1"));
                iv_edit_sex_activity_girl.setImageResource(R.mipmap.no_click_girl);
                tv_edit_sex_activity_girl.setTextColor(Color.parseColor("#b3b3b3"));
                isBoy=true;
                break;
            case R.id.iv_edit_sex_activity_girl:
                iv_edit_sex_activity_boy.setImageResource(R.mipmap.no_click_boy);
                tv_edit_sex_activity_boy.setTextColor(Color.parseColor("#b3b3b3"));
                iv_edit_sex_activity_girl.setImageResource(R.mipmap.click_girl);
                tv_edit_sex_activity_girl.setTextColor(Color.parseColor("#f573c6"));
                isBoy=false;
                break;
            case R.id.tv_edit_sex_activity_updata:
                Intent intent=new Intent();
                intent.putExtra("sex",isBoy);
                setResult(1,intent);
                finish();
                break;
            case R.id.tv_edit_sex_activity_cancel:
                finish();
                break;
        }
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("提醒");
        normalDialog.setMessage("性别只能更改一次,终身大事考虑清楚哦!");
        normalDialog.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
    }
}
