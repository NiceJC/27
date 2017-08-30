package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.ForumVideoReplyBean;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.ReportActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.TimeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by ld on 17/8/8.
 */

public class ForumReplyVideoPlayFragment extends Fragment implements OnResponseListener<String> {

    private static final int DELETE_VIDEO=1000;

    @Bind(R.id.jcplay_reply_video_fragment_play)
    JCVideoPlayerStandard jcplayReplyVideoFragmentPlay;
    @Bind(R.id.diyiv_reply_video_fragment_userimg)
    DIYImageView diyivReplyVideoFragmentUserimg;
    @Bind(R.id.tv_reply_video_fragment_nickname)
    TextView tvReplyVideoFragmentNickname;
    @Bind(R.id.tv_reply_video_fragment_time)
    TextView tvReplyVideoFragmentTime;
    @Bind(R.id.iv_reply_video_fragment_close)
    ImageView ivReplyVideoFragmentClose;
    @Bind(R.id.iv_reply_video_fragment_menu)
    ImageView ivReplyVideoFragmentMenu;
    private Bundle bundle;
    private ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean listBean;
    private Context context;
    private RequestQueue requestQueue;
    private PopupWindow popupWindow;
    private Gson mGson;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_forum_reply_video_play_fragment, container, false);
        ButterKnife.bind(this, view);
        mGson = new Gson();
        bundle = getArguments();
        listBean = (ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean) bundle.getSerializable("info");
        initView();
        requestQueue = GlobalConstants.getRequestQueue();

        return view;
    }

    private void initView() {
        jcplayReplyVideoFragmentPlay.setUp(listBean.getUrlVideoRecord(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL , "");
        Glide.with(context).load(listBean.getUrlVideoPic()).into(jcplayReplyVideoFragmentPlay.thumbImageView);

        Glide.with(context).load(listBean.getUserInfo().getImgUrl()).into(diyivReplyVideoFragmentUserimg);
        tvReplyVideoFragmentNickname.setText(listBean.getUserInfo().getNickName());
        Date createDate=null;
        //"2017-05-19 17:15:40"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            createDate=formatter.parse(listBean.getCreatTime());
            tvReplyVideoFragmentTime.setText(TimeUtils.getTimeFormatText(createDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.iv_reply_video_fragment_close,R.id.iv_reply_video_fragment_menu})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_reply_video_fragment_close:
                getActivity().finish();
                break;
            case R.id.iv_reply_video_fragment_menu:
                showReportPopwindow();
                break;
        }
    }

    private void deleteReplyVideo() {
        Request<String> deleteReplyVideoRequest= NoHttp.createStringRequest(GlobalConstants.URL+"/VideoReply/deleteVideoReply", RequestMethod.POST);
        deleteReplyVideoRequest.add("access_token", (String) SPUtils.get(context,GlobalConstants.TOKEN,""));
        deleteReplyVideoRequest.add("Id",listBean.getId());
        requestQueue.add(DELETE_VIDEO,deleteReplyVideoRequest,this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    private void parseJson(String json) {
        MsgBean msg=mGson.fromJson(json,MsgBean.class);
        if (msg.getMsg().equals("请求成功")) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }else {
            Toast.makeText(context, "删除失败，请检查网络重新尝试", Toast.LENGTH_SHORT).show();
        }

    }




    /**
     * 提示举报底部弹窗
     */
    private void showReportPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_save_or_report, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        if (SPUtils.get(context,GlobalConstants.USERID,"").equals(listBean.getUserInfo().getUserId())||SPUtils.get(context,GlobalConstants.USERID,"").equals(listBean.getUserId())){
            ((TextView) view.findViewById(R.id.btn_save)).setText("删除");
        }else {
            ((TextView) view.findViewById(R.id.btn_save)).setText("举报");
        }

        view.findViewById(R.id.btn_save).setOnClickListener(btnlistener);
//        root.findViewById(R.id.btn_report).setOnClickListener(btnlistener);
        view.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;

        getActivity().getWindow().setAttributes(params);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);


        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
//        window.showAtLocation(getActivity().findViewById(R.id.ll_prv_forum_detail_bottom),
//                Gravity.BOTTOM, 0, 0);
        popupWindow.showAtLocation(jcplayReplyVideoFragmentPlay,
                Gravity.BOTTOM, 0, 0);
        //popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1.0f;

                getActivity().getWindow().setAttributes(params);
            }
        });

    }


    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
//                    mDialog.dismiss();
//                    saveBitmap(bitmap);
                    popupWindow.dismiss();
                    if (SPUtils.get(context,GlobalConstants.USERID,"").equals(listBean.getUserInfo().getUserId())||SPUtils.get(context,GlobalConstants.USERID,"").equals(listBean.getUserId())){
                        deleteReplyVideo();
                    }else {
                        Intent intent = new Intent(getActivity(), ReportActivity.class);
                        intent.putExtra("forumId",listBean.getForumId());
                        intent.putExtra("videoId",listBean.getId());
                        startActivity(intent);
                    }


                    break;

                case R.id.btn_cancel:
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("ForumReplyVideoPlay", "onSucceed: "+response.get());
        switch (what) {
            case DELETE_VIDEO:
                parseJson(response.get());
                break;
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }
}
