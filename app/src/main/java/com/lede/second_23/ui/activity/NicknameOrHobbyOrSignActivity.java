package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
        if (body!=null) {
            et_text.setText(body);
        }
        switch (type) {
            case 0:

                et_text.setHint("请输入昵称");
                break;
            case 1:
                et_text.setMaxLines(2);
                et_text.setHint("请输入个性签名");
                break;
            case 2:

                et_text.setHint("请输入爱好");
                break;
            case 4:

                et_text.setHint("请输入学校");
                break;

            case 5:
                et_text.setHint("请输入商家用户名");
                break;

            case 6:
                et_text.setHint("请输入版块名称");
                break;
            case 7:
                et_text.setHint("请输入版块简介");
                break;





        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }


    @OnClick({R.id.iv_nhs_dialog_finish,R.id.tv_nhs_dialog_ok})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.iv_nhs_dialog_finish:
                et_text.setText("");
                break;
            case R.id.tv_nhs_dialog_ok:

                body=et_text.getText().toString().toString();
                Intent intent=new Intent();


                switch (type){

                    case 0:
                        String regex = "\\s+";
                        String str1 = body.replaceAll(regex, "");
                        if (TextUtils.isEmpty(str1)) {
                            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(str1.length()>8){
                            Toast.makeText(this, "昵称不能多于8个字符", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent.putExtra("body",str1);
                        break;
                    case 1:
                        if(body.length()>30){
                            Toast.makeText(this, "签名不能多于30个字符", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent.putExtra("body",body);
                        break;
                    case 2:
                        intent.putExtra("body",body);
                        break;
                    case 4:
                        intent.putExtra("body",body);
                        break;
                    case 5:
                    case 6:
                    case 7:
                        intent.putExtra("body",body);
                        break;


                    default:
                        break;
                }
                setResult(type,intent);
                finish();
                break;
        }
    }
}
