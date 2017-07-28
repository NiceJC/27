package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.myapplication.views.diyimage.DIYImageView;
import com.lede.second_23.R;
import com.lede.second_23.ui.fragment.CameraFragment;
import com.lede.second_23.ui.fragment.ShortVideoFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 这应该算拍照界面 不是发布 未更名
 */
public class AllIssueActivity extends AppCompatActivity {

    @Bind(R.id.rr_all_issue_activity_show)
    RelativeLayout rrAllIssueActivityShow;
    @Bind(R.id.ib_all_issue_activity_change)
    ImageButton ibAllIssueActivityChange;
    @Bind(R.id.diyiv_all_issue_activity_show)
    public DIYImageView diyivAllIssueActivityShow;
    private FragmentManager supportFragmentManager;
    private CameraFragment cameraFragment;
    private boolean isCamera = true;
    private ShortVideoFragment shortVideoFragment;
    public static AllIssueActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_issue);
        ButterKnife.bind(this);
        instance=this;
        supportFragmentManager = getSupportFragmentManager();
        cameraFragment = new CameraFragment();
        shortVideoFragment = new ShortVideoFragment();
        supportFragmentManager.beginTransaction().replace(R.id.rr_all_issue_activity_show, cameraFragment).commit();
    }

    @OnClick({R.id.ib_all_issue_activity_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_all_issue_activity_change:
                if (isCamera) {
                    supportFragmentManager.beginTransaction().replace(R.id.rr_all_issue_activity_show, shortVideoFragment).commit();
                    isCamera = false;
                    ibAllIssueActivityChange.setImageResource(R.mipmap.change_photo);
                } else {
                    supportFragmentManager.beginTransaction().replace(R.id.rr_all_issue_activity_show, cameraFragment).commit();
                    isCamera = true;
                    ibAllIssueActivityChange.setImageResource(R.mipmap.change_video);
                }
                break;
        }

    }
}
