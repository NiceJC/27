package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PicOrVideoChooseActivity extends Activity {

    @Bind(R.id.iv_pic_or_video_activity_9pic)
    ImageView ivPicOrVideoActivity9pic;
    @Bind(R.id.iv_pic_or_video_activity_tuceng)
    ImageView ivPicOrVideoActivityTuceng;
    @Bind(R.id.iv_pic_or_video_activity_video)
    ImageView ivPicOrVideoActivityVideo;
    private Intent intent;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_or_video_choose);
        intent = getIntent();
        type = intent.getIntExtra("type", 0);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.iv_pic_or_video_activity_9pic,R.id.iv_pic_or_video_activity_tuceng,R.id.iv_pic_or_video_activity_video})
    public void onClick(View view){
        Intent intent=new Intent(this,AllIssueTextActivity.class);
        switch (view.getId()) {
            case R.id.iv_pic_or_video_activity_9pic:
                intent.putExtra("imgOrVideoType",0);
                intent.putExtra("isCrop",false);
                break;
            case R.id.iv_pic_or_video_activity_tuceng:
                intent.putExtra("imgOrVideoType",0);
                intent.putExtra("isCrop",true);
                break;
            case R.id.iv_pic_or_video_activity_video:
                intent.putExtra("imgOrVideoType",1);
                break;
        }
        startActivity(intent);
        finish();
    }
}
