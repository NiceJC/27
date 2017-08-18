package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.lede.second_23.R;
import com.lede.second_23.adapter.MyFragmentPagerAdapter;
import com.lede.second_23.bean.ForumVideoReplyBean;
import com.lede.second_23.ui.fragment.ForumReplyVideoPlayFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForumReplyVideoPlayActivity extends ActionBarActivity {

    @Bind(R.id.vp_forum_reply_video_play_activity_show)
    ViewPager vpForumReplyVideoPlayActivityShow;
    private Intent intent;
    private ArrayList<ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean> forumVideoReplyList=new ArrayList<>();
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    private Bundle bundle;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_reply_video_play);
        ButterKnife.bind(this);
        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        forumVideoReplyList= (ArrayList<ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean>) intent.getSerializableExtra("list");
        forumVideoReplyList.remove(0);
        initData();
    }

    private void initData() {
        for (int i = 0; i < forumVideoReplyList.size(); i++) {
            ForumReplyVideoPlayFragment playFragment=new ForumReplyVideoPlayFragment();
            bundle=null;
            bundle = new Bundle();
            bundle.putSerializable("info",forumVideoReplyList.get(i));
            bundle.putInt("position",i);
            playFragment.setArguments(bundle);
            fragmentList.add(playFragment);
        }
        vpForumReplyVideoPlayActivityShow.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList));
        vpForumReplyVideoPlayActivityShow.setCurrentItem(position);
    }
}
