package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {

    @Bind(R.id.tv_edit_activity_title)
    TextView tv_edit_activity_title;
    @Bind(R.id.tv_edit_activity_limit)
    TextView tv_edit_activity_limit;
    @Bind(R.id.tv_edit_activity_body)
    TextView tv_edit_activity_body;

    private int type;
    private String str;//昵称或者签名或者职业


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);

        if (type==2) {
            tv_edit_activity_title.setText("个性签名");
            tv_edit_activity_limit.setText("30");
            tv_edit_activity_body.setMaxLines(30);
            tv_edit_activity_body.setHint("请输入个性签名");
        }else if (type==3){
            tv_edit_activity_title.setText("职业");
            tv_edit_activity_limit.setVisibility(View.GONE);
            tv_edit_activity_body.setMaxLines(10);
            tv_edit_activity_body.setHint("请输入职业");
        }
        tv_edit_activity_body.setText(intent.getStringExtra("body"));
    }

    @OnClick({R.id.tv_edit_activity_updata,R.id.tv_edit_activity_cancel})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.tv_edit_activity_updata:
                str=tv_edit_activity_body.getText().toString().toString();
                Intent intent=new Intent();
                intent.putExtra("body",str);
                setResult(type,intent);
                finish();
                break;
            case R.id.tv_edit_activity_cancel:
                finish();
                break;
        }
    }


}
