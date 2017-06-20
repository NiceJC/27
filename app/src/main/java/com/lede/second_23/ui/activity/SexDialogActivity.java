package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ld on 17/5/22.
 */

public class SexDialogActivity extends Activity {

    @Bind(R.id.iv_sex_dialog_finish)
    ImageView iv_finish;
    @Bind(R.id.iv_sex_dialog_boy)
    ImageView iv_boy;
    @Bind(R.id.iv_sex_dialog_girl)
    ImageView iv_girl;
    @Bind(R.id.tv_sex_dialog_ok)
    TextView tv_ok;
    private boolean isBoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先去除应用程序标题栏  注意：一定要在setContentView之前
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_sex_dialog);
        ButterKnife.bind(this);
        Intent intent =getIntent();
        isBoy=intent.getBooleanExtra("sex",true);
        if (isBoy) {
            iv_boy.setImageResource(R.mipmap.click_boy);
        }else {
            iv_girl.setImageResource(R.mipmap.click_girl);
        }
    }
    @OnClick({R.id.iv_sex_dialog_finish,R.id.tv_sex_dialog_ok,R.id.iv_sex_dialog_boy,R.id.iv_sex_dialog_girl})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_sex_dialog_finish:
                finish();
                break;
            case R.id.tv_sex_dialog_ok:
                Intent intent=new Intent();
                intent.putExtra("sex",isBoy);
                setResult(3,intent);
                finish();
                break;
            case R.id.iv_sex_dialog_boy:
                iv_boy.setImageResource(R.mipmap.click_boy);
                iv_girl.setImageResource(R.mipmap.no_click_girl);
                isBoy=true;
                break;
            case R.id.iv_sex_dialog_girl:
                iv_boy.setImageResource(R.mipmap.no_click_boy);
                iv_girl.setImageResource(R.mipmap.click_girl);
                isBoy=false;
                break;
        }
    }
}
