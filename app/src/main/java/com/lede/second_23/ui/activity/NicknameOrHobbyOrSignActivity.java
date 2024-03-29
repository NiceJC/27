package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ld on 17/5/22.
 */

public class NicknameOrHobbyOrSignActivity extends Activity {

    @Bind(R.id.iv_nhs_dialog_finish)
    ImageView iv_finish;
    @Bind(R.id.tv_nhs_dialog_ok)
    TextView tv_ok;
    @Bind(R.id.et_nhs_dialog_text)
    EditText et_text;
    private int type;
    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先去除应用程序标题栏  注意：一定要在setContentView之前
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_nickname_or_hobby_or_sign_dialog);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        body=intent.getStringExtra("body");
        et_text.setText(body);
        switch (type) {
            case 0:
                et_text.setMaxLines(8);
                et_text.setHint("请输入昵称");
                break;
            case 1:
                et_text.setMaxLines(30);
                et_text.setHint("请输入个性签名");
                break;
            case 2:
                et_text.setMaxLines(10);
                et_text.setHint("请输入爱好");
                break;
        }
    }


    @OnClick({R.id.iv_nhs_dialog_finish,R.id.tv_nhs_dialog_ok})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.iv_nhs_dialog_finish:
                finish();
                break;
            case R.id.tv_nhs_dialog_ok:

                body=et_text.getText().toString().toString();
                Intent intent=new Intent();
                if (type==0) {
                    String regex = "\\s+";
                    String str1 = body.replaceAll(regex, "");
                    if (TextUtils.isEmpty(str1)) {
                        Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //TODO
//                    else if (!Validator.isNickName(str1)){
//                        Toast.makeText(this, "昵称不允许出现特殊字符哦", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    intent.putExtra("body",str1);
                }else {
                    intent.putExtra("body",body);
                }
                setResult(type,intent);
                finish();
                break;
        }
    }
}
