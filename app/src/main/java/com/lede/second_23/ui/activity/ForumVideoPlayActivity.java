package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class ForumVideoPlayActivity extends AppCompatActivity {

    @Bind(R.id.jcplay_forum_video_play_video)
    JCVideoPlayer jcplayForumVideoPlayVideo;
    @Bind(R.id.iv_forum_video_play_close)
    ImageView ivForumVideoPlayClose;
    private Intent intent;
    private String pic_patch;
    private String video_patch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_video_play);
        ButterKnife.bind(this);
        intent = getIntent();
        pic_patch = intent.getStringExtra("pic_patch");
        video_patch = intent.getStringExtra("video_patch");
        jcplayForumVideoPlayVideo.setUp(video_patch,pic_patch,null);

    }

    @OnClick({R.id.iv_forum_video_play_close})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_forum_video_play_close:
                finish();
                break;
        }
    }
}
