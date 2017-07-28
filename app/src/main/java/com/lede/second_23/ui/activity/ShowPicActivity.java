package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lede.second_23.AppConstant;
import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowPicActivity extends Activity {

    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.tv_showpic_activity_cancel)
    TextView tvShowpicActivityCancel;
    @Bind(R.id.tv_showpic_activity_next)
    TextView tvShowpicActivityNext;
    private int picWidth;
    private int picHeight;
    private String img_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        ButterKnife.bind(this);

        picWidth = getIntent().getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
        picHeight = getIntent().getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
        img_path=getIntent().getStringExtra(AppConstant.KEY.IMG_PATH);
        img.setImageURI(Uri.parse(img_path));
        img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
    }

    @OnClick({R.id.tv_showpic_activity_cancel,R.id.tv_showpic_activity_next})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_showpic_activity_cancel:
                finish();
                break;
            case R.id.tv_showpic_activity_next:
                Intent intent=new Intent(this,AllIssueTextActivity.class);
                intent.putExtra("img_path",img_path);
                startActivity(intent);
                finish();
                break;
        }
    }
}
