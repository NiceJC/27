package com.lede.second_23.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.adapter.ConversationListAdapterEx;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class ConversationListDynamicActivtiy extends AppCompatActivity {


    @Bind(R.id.iv_conversation_list_activity_back)
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongIM.init(getApplicationContext());
        if (!(boolean) SPUtils.get(this, GlobalConstants.ISCONNECTED_RONGIM,true)) {
            MyApplication.instance.getRongIMTokenService();
        }
        if (RongIM.getInstance()==null) {
            Log.i("TAG", "onStart: 会话融云为空");
            MyApplication.instance.getRongIMTokenService();
        }
        setContentView(R.layout.activity_conversation_list_dynamic_activtiy);

        StatusBarUtil.StatusBarLightMode(this);

        ConversationListFragment fragment = new ConversationListFragment();
        fragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(),"false") //设置系统会话
                .build();
        fragment.setUri(uri);  //设置 ConverssationListFragment 的显示属性

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_conversation_list_activity_back})
    public  void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_conversation_list_activity_back:
                finish();
                break;
        }
    }
}
