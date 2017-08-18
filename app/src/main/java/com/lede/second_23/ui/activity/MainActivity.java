package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.lede.second_23.R;
import com.lede.second_23.adapter.MyFragmentPagerAdapter;
import com.lede.second_23.ui.fragment.ChildFragment;
import com.lede.second_23.ui.fragment.ForumFragment;
import com.lede.second_23.ui.fragment.MainFragment;
import com.lede.second_23.ui.fragment.PersonFragment_1;
import com.lede.second_23.utils.MyViewPager;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    public MyViewPager vp_main_fg;
    private ArrayList<Fragment> fragmentList;
    public  static MainActivity instance=null;
    private int widthPixels;
    private boolean isScrolling=false;
    private boolean isRight=false;
    private ChildFragment childFragment;
//    private OldCameraFragment cameraFragment;
//    private IssueFragment issueFragment;
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    private ForumFragment forumFragment;
    private MainFragment mainFragment;
    private PersonFragment_1 personFragment_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;
        Log.i("TAC", "onCreate: ");
        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#3b5998"));
        UpdateHelper.getInstance().autoUpdate("com.lede.second_23", false, 5000);
        UpdateHelper.getInstance().setDebugMode(true);
//        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#77cbd8"), lightStatusBar);
//        Window window = getWindow();
//        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
//        View statusBarView = new View(window.getContext());
//        int statusBarHeight = getStatusBarHeight(window.getContext());
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
//        params.gravity = Gravity.TOP;
//        statusBarView.setLayoutParams(params);
//        statusBarView.setBackgroundColor(Color.parseColor("#77cbd8"));
//        decorViewGroup.addView(statusBarView);

                initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAC", "onResume: ");
//        if ((int) SPUtils.get(instance, GlobalConstants.GETREPLY,0)>0) {
//            Toast.makeText(instance, "收到评论", Toast.LENGTH_SHORT).show();
//        }

    }

    private void initView() {
        vp_main_fg = (MyViewPager) findViewById(R.id.vp_main_fg);
        initFragmentViewPager();
    }

    private void initFragmentViewPager() {
        fragmentList = new ArrayList<Fragment>();

//        cameraFragment = new OldCameraFragment();
//        fragmentList.add(cameraFragment);

//        mainFragment=new MainFragment();
//        mainFragment=getFragmentManager().findFragmentById(R.layout.main_fragment_layout)
//        fragmentList.add(mainFragment);
//        line=getFragmentManager().findFragmentById(R.layout.main_fragment_layout).getView().findViewById(R.id.v_mainFragment_line);

//        PersonFragment personFragment=new PersonFragment();
//        fragmentList.add(personFragment);

//        issueFragment=new IssueFragment();
//        fragmentList.add(issueFragment);
        forumFragment = new ForumFragment();
        fragmentList.add(forumFragment);
        childFragment=new ChildFragment();
        fragmentList.add(childFragment);
        mainFragment = new MainFragment();
        personFragment_1 = new PersonFragment_1();
        fragmentList.add(mainFragment);
        fragmentList.add(personFragment_1);

        /**
         * 获取屏幕像素密度
         */
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        widthPixels= dm.widthPixels;

        vp_main_fg.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        vp_main_fg.setCurrentItem(1,false);
//        vp_main_fg.setOffscreenPageLimit(0);
        vp_main_fg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //mainFragment.v_mainFragment_line
//                animator.ofFloat(line,"translationX",100,100);
//                animator.setDuration(400);
//                animator.start_1();
//                if (vp_main_fg.getMoveRight()) {
//                    positionOffset=positionOffset-1;
//                }else if(vp_main_fg.getMoveLeft()){
//
//                }else{
//                    positionOffset=0;
//                }
//                if ((vp_main_fg.getMoveRight()&&isScrolling)||isRight) {
//                    if (positionOffset==0) {
//
//                    }else{
//                        positionOffset=positionOffset-1;
//                    }
//                    isRight=true;
//                }
//                if (positionOffsetPixels==0) {
//                    positionOffset=0;
//                    isRight=false;
//                    isScrolling=false;
//                }
                if (positionOffset==0){

                }else{
                    positionOffset=positionOffset-1;
                }
                Log.i("TAG", "onPageScrolled: positionOffsetPixels----"+positionOffsetPixels+"--positionOffset--"+positionOffset);
//                mainFragment=(MainFragment) childFragment.getChildFragmentManager().getFragments().get(0);
//
//                mainFragment.setLineCallBack(positionOffset);

                MainFragment.instance.setLineCallBack(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
//                if (position==0) {
//                    vp_main_fg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//
//                }else{
//                    vp_main_fg.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state==0) {
//                    isScrolling=true;
//                }else if(state==2){
//                    isScrolling=false;
//                    isRight=false;
//                 }

            }
        });
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RongIM.getInstance().disconnect();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
