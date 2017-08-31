package com.lede.second_23.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.adapter.ImageViewPagerAdapter;
import com.lede.second_23.bean.DeleteBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.view.HackyViewPager;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;



public class MyPhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private HackyViewPager pager;
    private LinearLayout ll_bottom;
    private ImageView iv_bt;
    private TextView tv_text;
    private ArrayList<String> banner;
    private String text;
    private int current = 0;
    private LinearLayout ll_save;
    private TextView tv_num;
    private int sum;
    private RequestQueue requestQueue;
    private ImageView iv_menu;
    private String userid;
    private String time;
    private long forumId;
    private PopupMenu popup;
    private ImageView iv_back;
    private Dialog mDialog;
    private boolean isSelf = true;
    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo);
        Intent intent = getIntent();
        banner = intent.getStringArrayListExtra("banner");
        sum = banner.size();
        text = intent.getStringExtra("text");
        time=intent.getStringExtra("time");
        userid = intent.getStringExtra("userId");
        forumId = intent.getLongExtra("forumId", 0);
        SPUtils.put(this, GlobalConstants.CURRENT_USERID, userid);
        SPUtils.put(this, GlobalConstants.CURRENT_FORUMID, forumId + "");
        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();
        initView();
        if (text.equals("")) {
            iv_bt.setVisibility(View.GONE);
        }
        initData();
    }

    //初始化布局
    public void initView() {
        pager = (HackyViewPager) findViewById(R.id.hvp_my_photo_photo);
        iv_back = (ImageView) findViewById(R.id.iv_my_photo_back);
        iv_back.setOnClickListener(this);
        iv_menu = (ImageView) findViewById(R.id.iv_my_photo_menu);
        tv_time = (TextView) findViewById(R.id.tv_my_photo_time);
        tv_time.setText(time);

        isSelf = ((String) SPUtils.get(this, GlobalConstants.USERID, "")).equals(userid);
//        if (((String) SPUtils.get(this, GlobalConstants.USERID,"")).equals(userid)) {
//            /**
//             * 按钮弹出菜单
//             *
//             */
//            popup = new PopupMenu(this, iv_menu);
//
//            popup.getMenuInflater().inflate(R.menu.menu_delete,popup.getMenu());//压入XML资源文件
////                popup.getMenu().getItem(0).sett
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.menu_delete:
//                            deleteDynamic();
//                            return true;
//                    }
//                    return false;
//                }
//            });    //设置点击菜单选项事件
//        }else {
//            iv_menu.setVisibility(View.GONE);
//        }
        iv_menu.setOnClickListener(this);
//        ll_bottom = (LinearLayout) findViewById(R.id.ll_my_photo_bottom);
        iv_bt = (ImageView) findViewById(R.id.iv_my_photo_bt);
        iv_bt.setOnClickListener(this);
        tv_num = (TextView) findViewById(R.id.tv_my_photo_num);
        tv_num.setText("1/" + sum);
//        tv_text = (TextView) findViewById(R.id.tv_my_photo_text);
//        ll_save = (LinearLayout) findViewById(R.id.ll_save);
//        ll_save.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(MyPhotoActivity.this, "当前长按的图片是第"+current+"个", Toast.LENGTH_SHORT).show();
//                showDialog();
//                return false;
//            }
//        });
//        ll_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MyPhotoActivity.this, "当前单击的图片是第"+current+"个", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    //初始化数据
    public void initData() {

//        int pos = intent.getIntExtra("pos", 0);
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), banner);

//        tv_text.setText(text);
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
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
                tv_num.setText(current + 1 + "/" + sum);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setCurrentItem(0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_photo_bt:
                showPopwindow();
                tv_time.setVisibility(View.GONE);
                break;
            case R.id.iv_my_photo_menu:

                showDialog();
//                popup.show();                                            //显示菜单

                break;
            case R.id.iv_my_photo_back:
                finish();
                break;
        }
    }

    private void deleteDynamic() {
        Request<String> deleteRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/deleteForumbyId", RequestMethod.POST);
        deleteRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        deleteRequest.add("forumId", forumId);
        requestQueue.add(200, deleteRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                L.i("TAB", response.get());
                Gson mGson = new Gson();
                DeleteBean deleteBean = mGson.fromJson(response.get(), DeleteBean.class);
                if (deleteBean.getMsg().equals("用户没有登录")) {
                    Toast.makeText(MyPhotoActivity.this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyPhotoActivity.this,WelcomeActivity.class));
                }else {
                    if (deleteBean.getMsg().equals("删除成功")) {
                        Toast.makeText(MyPhotoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        SPUtils.put(MyPhotoActivity.this,GlobalConstants.ISDELETE_NEAR_FORUM,true);
                        finish();
                    } else {
                        Toast.makeText(MyPhotoActivity.this, "网络出错,未删除成功", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void showDialog() {
        mDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_save_or_report, null);
        if (isSelf) {

            ((TextView) root.findViewById(R.id.btn_save)).setText("删除动态");

        } else {
            ((TextView) root.findViewById(R.id.btn_save)).setText("举报");
        }

        root.findViewById(R.id.btn_save).setOnClickListener(btnlistener);

//        root.findViewById(R.id.btn_report).setOnClickListener(btnlistener);
        root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save: // 保存图片
                    mDialog.dismiss();
//                    saveBitmap(bitmap);
                    if (isSelf) {
                        deleteDynamic();
                    } else {
                        Intent intent = new Intent(MyPhotoActivity.this, ReportActivity.class);
                        startActivity(intent);
                    }
                    break;
                // 举报
//                case R.id.btn_report:
//                    Intent intent=new Intent(getActivity(), ReportActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
                // 取消
                case R.id.btn_cancel:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    break;
            }
        }
    };

    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popwindow, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);


        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(MyPhotoActivity.this.findViewById(R.id.activity_my_photo),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        TextView tv_text = (TextView) view.findViewById(R.id.tv_my_pop_text);
//        first.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                System.out.println("第一个按钮被点击了");
//            }
//        });
        tv_text.setText(text);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
                tv_time.setVisibility(View.VISIBLE);
            }
        });

    }


}
