package com.lede.second_23.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 融云单聊页
 */
public class ConversationActivity extends AppCompatActivity {

    @Bind(R.id.tv_conversation_activity_title)
    TextView tv_conversation_title;
    @Override
    protected void onStart() {
        super.onStart();
        if (!(boolean) SPUtils.get(this, GlobalConstants.ISCONNECTED_RONGIM,true)) {
            MyApplication.instance.getRongIMTokenService();
        }
        if (RongIM.getInstance()==null) {
            Log.i("TAG", "onStart: 会话融云为空");
            MyApplication.instance.getRongIMTokenService();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        StatusBarUtil.StatusBarLightMode(this);


        ButterKnife.bind(this);
//        RongIM.getInstance().refreshUserInfoCache();
        Intent intent=getIntent();
        tv_conversation_title.setText(intent.getData().getQueryParameter("title"));

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }

    @OnClick({R.id.iv_conversation_activity_back})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.iv_conversation_activity_back:
                finish();
                break;
        }

    }
}
