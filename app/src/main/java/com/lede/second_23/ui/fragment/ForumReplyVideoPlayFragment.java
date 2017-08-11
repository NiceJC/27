package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.lede.second_23.R;
import com.lede.second_23.bean.ForumVideoReplyBean;
import com.lede.second_23.utils.TimeUtils;

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

public class ForumReplyVideoPlayFragment extends Fragment {

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
        bundle = getArguments();
        listBean = (ForumVideoReplyBean.DataBean.SimplePageInfoBean.ListBean) bundle.getSerializable("info");
        initView();


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
                Toast.makeText(context, "弹出菜单", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
