package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;
import com.lede.second_23.interface_utils.OnclickFinish;
import com.lede.second_23.ui.view.ZoomImageView;
import com.lede.second_23.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import static com.lede.second_23.global.GlobalConstants.IMAGE_URLS;
import static com.lede.second_23.global.GlobalConstants.POSITION;


/**
 * 点击图片之后 切换到本Activity进行图片的缩放处理
 * Created by wjc
 */

public class ZoomImageActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewPager viewPager;

    private List<String> mImageURLS=new ArrayList<String>();

    private ImageView[] mImageViews;


    private TextView mCurrent;

    private TextView mCount;

    private int firstPosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zoom_image);
        StatusBarUtil.transparencyBar(this);


        initData();


        mImageViews = new ImageView[mImageURLS.size()];
        viewPager = (ViewPager) findViewById(R.id.zoom_viewpager);

        mCount = (TextView) findViewById(R.id.zoom_count);
        mCount.setText(mImageURLS.size() + "");
        mCurrent = (TextView) findViewById(R.id.zoom_current);

        viewPager.setOnClickListener(this);


        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ZoomImageView imageView = new ZoomImageView(getApplicationContext());
                imageView.setmOnClickFinish(new OnclickFinish() {
                    @Override
                    public void onClickFinish() {
                        finish();
//                        overridePendingTransition(R.anim.none, R.anim.out_zoom);
                    }
                });

                Glide.with(ZoomImageActivity.this)
                        .load(mImageURLS.get(position))
                        .into(imageView);

                container.addView(imageView);


                mImageViews[position] = imageView;


                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public int getCount() {
                return mImageURLS.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }


        });


        viewPager.setCurrentItem(firstPosition);
        mCurrent.setText(firstPosition + 1 + "");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Glide.with(ZoomImageActivity.this)
                        .load(mImageURLS.get(position))
                        .into(mImageViews[position]);
                mCurrent.setText(position + 1 + "");


            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


    }

    private void initData() {

        mImageURLS = getIntent().getStringArrayListExtra(IMAGE_URLS);
        firstPosition = getIntent().getIntExtra(POSITION, 0);

    }

    @Override
    public void onBackPressed() {
        finish();
//        overridePendingTransition(R.anim.none, R.anim.out_zoom);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.zoom_viewpager:
                Toast.makeText(this, "ha", Toast.LENGTH_SHORT).show();
                finish();
//                overridePendingTransition(R.anim.none, R.anim.out_zoom);


                break;
            default:
                break;

        }


    }
}