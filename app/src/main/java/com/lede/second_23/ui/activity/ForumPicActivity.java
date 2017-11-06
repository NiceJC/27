package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lede.second_23.R;
import com.lede.second_23.adapter.ImageViewPagerAdapter;
import com.lede.second_23.ui.view.HackyViewPager;
import com.lede.second_23.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForumPicActivity extends AppCompatActivity {

    @Bind(R.id.hvp_forum_pic_photo)
    HackyViewPager hvpForumPicPhoto;
    @Bind(R.id.image_current)
    TextView imageCurrent;
    @Bind(R.id.image_count)
    TextView imageCount;


    private ArrayList<String> banner;
    private Intent intent;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_pic);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        intent = getIntent();
        banner=intent.getStringArrayListExtra("banner");
        position = intent.getIntExtra("position",0);
        imageCount.setText(banner.size()+"");
        initData();
    }

    private void initData() {
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), banner);

//        tv_text.setText(text);
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        hvpForumPicPhoto.setAdapter(adapter);
        hvpForumPicPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imageCurrent.setText(position+1+"");
//                pager.getChildAt(current).setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        Toast.makeText(MyPhotoActivity.this, "当前长按的图片是第"+current+"个", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//                if (pager.getChildAt(current)==null) {
//
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        hvpForumPicPhoto.setCurrentItem(position);
        imageCurrent.setText(position+1+"");
    }
}
