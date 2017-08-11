package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class ForumVideoPlayActivity extends AppCompatActivity {

    @Bind(R.id.jcplay_forum_video_play_video)
    JCVideoPlayerStandard jcplayForumVideoPlayVideo;
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
        jcplayForumVideoPlayVideo.setUp(video_patch,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
        Glide.with(this).load(pic_patch).into(jcplayForumVideoPlayVideo.thumbImageView);
    }

    @OnClick({R.id.iv_forum_video_play_close})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_forum_video_play_close:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
