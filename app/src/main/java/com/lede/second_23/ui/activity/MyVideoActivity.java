package com.lede.second_23.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.DeleteBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MyVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private String videoUrl;
    private String picUrl;
    private String text;
    private JCVideoPlayerStandard videoController;
    private ImageView iv_bt;
    private ImageView iv_back;
    private ImageView iv_menu;
    private String userid;
    private long forumId;
    private PopupMenu popup;
    private RequestQueue requestQueue;
    private boolean isSelf=true;
    private Dialog mDialog;
    private TextView tv_time;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        Intent intent=getIntent();
        videoUrl=intent.getStringExtra("videourl");
        picUrl=intent.getStringExtra("picurl");
        text=intent.getStringExtra("text");
        userid=intent.getStringExtra("userId");
        time=intent.getStringExtra("time");
        forumId=intent.getLongExtra("forumId",0);
        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();
        initView();
        if (text.equals("")) {
            iv_bt.setVisibility(View.GONE);
        }

    }

    private void initView() {
        iv_bt = (ImageView) findViewById(R.id.iv_my_video_bt);
        iv_back = (ImageView) findViewById(R.id.iv_my_video_back);
        iv_back.setOnClickListener(this);
        iv_menu = (ImageView) findViewById(R.id.iv_my_video_menu);
        tv_time = (TextView) findViewById(R.id.tv_my_video_time);
        tv_time.setText(time);
        isSelf = ((String) SPUtils.get(this, GlobalConstants.USERID, "")).equals(userid);
//        if (((String) SPUtils.get(this, GlobalConstants.USERID,"")).equals(userid)) {
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
        iv_bt.setOnClickListener(this);
        videoController = (JCVideoPlayerStandard) findViewById(R.id.videocontroller1);
        videoController.setUp(videoUrl,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
        Glide.with(this).load(picUrl).into(videoController.thumbImageView);
//        videoController.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }
    private void deleteDynamic(){
        Request<String> deleteRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/deleteForumbyId", RequestMethod.POST);
        deleteRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        deleteRequest.add("forumId",forumId);
        requestQueue.add(200, deleteRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                L.i("TAB",response.get());
                Gson mGson=new Gson();
                DeleteBean deleteBean=mGson.fromJson(response.get(),DeleteBean.class);
                if (deleteBean.getMsg().equals("用户没有登录")) {
                    Toast.makeText(MyVideoActivity.this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyVideoActivity.this,WelcomeActivity.class));
                }else {
                    if (deleteBean.getMsg().equals("删除成功")) {
                        Toast.makeText(MyVideoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(MyVideoActivity.this, "网络出错,未删除成功", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_video_bt:
                tv_time.setVisibility(View.GONE);
                showPopwindow();
                break;
            case R.id.iv_my_video_back:
                finish();
                break;
            case R.id.iv_my_video_menu:
//                popup.show();
                showDialog();
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
                case R.id.btn_save: //删除动态
                    mDialog.dismiss();
//                    saveBitmap(bitmap);
                    if (isSelf) {
                        deleteDynamic();
                    } else {
                        Intent intent = new Intent(MyVideoActivity.this, ReportActivity.class);
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
        window.showAtLocation(MyVideoActivity.this.findViewById(R.id.activity_my_video),
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
                tv_time.setVisibility(View.VISIBLE);
                System.out.println("popWindow消失");
            }
        });

    }
}
