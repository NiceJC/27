package com.lede.second_23.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lede.second_23.R;
import com.lede.second_23.ui.activity.BilateralActivity;
import com.lede.second_23.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wjc on 17/10/19.
 */

public class MainFragment1 extends Fragment {


    private int currentPage = 0;//当前页 初始默认为0

    private MainFragmentYouLike mainFragmentYouLike;
    private MainFragmentNearBy mainFragmentNearBy;
    private MainFragmentSayHi mainFragmentSayHi;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;
    private List<ImageView> iconList;
    private List<View> lineList;

    public static MainFragment1 instance=null;
    private ObjectAnimator animator;

    @Bind(R.id.main_fragment_indicator_like_click)
    LinearLayout likeClick;
    @Bind(R.id.main_fragment_indicator_like_icon)
    ImageView likeIcon;
    @Bind(R.id.main_fragment_indicator_like_line)
    View likeLine;

    @Bind(R.id.main_fragment_indicator_nearby_click)
    LinearLayout nearbyClick;
    @Bind(R.id.main_fragment_indicator_nearby_icon)
    ImageView nearbyIcon;
    @Bind(R.id.main_fragment_indicator_nearby_line)
    View nearbyLine;

    @Bind(R.id.main_fragment_indicator_hi_click)
    LinearLayout hiClick;
    @Bind(R.id.main_fragment_indicator_hi_icon)
    ImageView hiIcon;
    @Bind(R.id.main_fragment_indicator_hi_line)
    View hiLine;

    @Bind(R.id.main_fragment_indicator_message_click)
    LinearLayout messageClick;
    @Bind(R.id.main_fragment_indicator_message_icon)
    ImageView messageIcon;
    @Bind(R.id.main_fragment_indicator_message_line)
    View messageLine;

    @Bind(R.id.topView)
    RelativeLayout topView;
    @Bind(R.id.underTopView)
    LinearLayout underTopView;

    @Bind(R.id.main_fragment_viewpager)
    ViewPager viewPager;

    @Bind(R.id.main_fragment_toLeft)
    ImageView toLeft;
    @Bind(R.id.main_fragment_toRight)
    ImageView toRight;





    @OnClick({R.id.main_fragment_toLeft, R.id.main_fragment_toRight, R.id.main_fragment_indicator_like_click,
            R.id.main_fragment_indicator_nearby_click, R.id.main_fragment_indicator_hi_click,
            R.id.main_fragment_indicator_message_click

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_fragment_toLeft:
                MainActivity.instance.vp_main_fg.setCurrentItem(0);
                break;
            case R.id.main_fragment_toRight:
                MainActivity.instance.vp_main_fg.setCurrentItem(2);
                break;
            case R.id.main_fragment_indicator_like_click: //0

                if(currentPage!=0){
                    resetOthers(currentPage);
                    setSelected(0);
                    viewPager.setCurrentItem(0,true);
                    currentPage=0;
                }
                break;
            case R.id.main_fragment_indicator_nearby_click: //1
                if(currentPage!=1){
                    resetOthers(currentPage);
                    setSelected(1);
                    viewPager.setCurrentItem(1,true);
                    currentPage=1;
                }
                break;
            case R.id.main_fragment_indicator_hi_click: //2
                if(currentPage!=2){
                    resetOthers(currentPage);
                    setSelected(2);
                    viewPager.setCurrentItem(2,true);
                    currentPage=2;
                }
                break;
            case R.id.main_fragment_indicator_message_click://3
                startActivity(new Intent(getActivity(), BilateralActivity.class));

                break;

            default:
                break;
        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        ButterKnife.bind(this,view);

        instance=this;

        initView();
        initialIndicators();
        viewPager.setAdapter(mAdapter);

        return view;


    }


    private void initView() {
        fragmentList=new ArrayList<Fragment>();
        iconList=new ArrayList<ImageView>();
        lineList=new ArrayList<View>();

        if (mainFragmentYouLike == null) {
            mainFragmentYouLike = new MainFragmentYouLike();
        }
        fragmentList.add(mainFragmentYouLike);
        if (mainFragmentNearBy == null) {
            mainFragmentNearBy = new MainFragmentNearBy();
        }
        fragmentList.add(mainFragmentNearBy);
        if (mainFragmentSayHi == null) {
            mainFragmentSayHi = new MainFragmentSayHi();
        }
        fragmentList.add(mainFragmentSayHi);

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                resetOthers(currentPage);
                setSelected(position);
                currentPage=position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }


    private void setChildFragment() {

    }




    public void scrollTopView(int i){

        animator = null;
        if (i == 0) {
            topView.scrollBy(0,-topView.getHeight());
            underTopView.scrollBy(0,-topView.getHeight());
//            animator = ObjectAnimator.ofFloat(topView, "translationY", 0, topView.getHeight());
//            animator.setDuration(500);
//            animator.start();
        } else {
            topView.scrollBy(0,topView.getHeight());
            underTopView.scrollBy(0,topView.getHeight());
//            animator = ObjectAnimator.ofFloat(topView, "translationY", topView.getHeight(), 0);
//            animator.setDuration(500);
//            animator.start();
        }


    }


    //初始化indicator
    public void initialIndicators() {

        ObjectAnimator.ofFloat(nearbyLine, "scaleX", 1.0f, 0f).setDuration(0).start();
        ObjectAnimator.ofFloat(hiLine, "scaleX", 1.0f, 0f).setDuration(0).start();
        ObjectAnimator.ofFloat(messageLine, "scaleX", 1.0f, 0f).setDuration(0).start();

        setSelected(0);
        viewPager.setCurrentItem(0,false);
        currentPage=0;

    }

    //线条动画
    public void scaleLine(boolean isToBeLonger, View view) {

        if (isToBeLonger) {
            ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.0f).setDuration(500).start();
        } else {
            ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0f).setDuration(500).start();

        }
    }


    //设置选中indicator状态
    public void setSelected(int selectedPage) {
        switch (selectedPage) {
            case 0:
                likeIcon.setSelected(true);

                scaleLine(true, likeLine);
                break;
            case 1:
                nearbyIcon.setSelected(true);
                scaleLine(true, nearbyLine);
                break;
            case 2:
                hiIcon.setSelected(true);
                scaleLine(true, hiLine);
                break;
            default:
                break;
        }
    }
    //重置未被选中的其他indicator
    public void resetOthers(int curPage) {
        switch (curPage) {
            case 0:
                likeIcon.setSelected(false);
                scaleLine(false,likeLine);
                break;
            case 1:
                nearbyIcon.setSelected(false);
                scaleLine(false,nearbyLine);
                break;
            case 2:
                hiIcon.setSelected(false);
                scaleLine(false,hiLine);
                break;
            default:
                break;
        }
    }

}
