package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.SPUtils;

import io.rong.imkit.RongIM;

public class ConversationListActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if (!(boolean) SPUtils.get(this, GlobalConstants.ISCONNECTED_RONGIM,true)) {
            MyApplication.instance.getRongIMTokenService();
        }

        if (RongIM.getInstance()==null) {
            Log.i("TAG", "onStart: 会话列表融云为空");
            MyApplication.instance.getRongIMTokenService();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
//        ConversationListFragment listFragment = ConversationListFragment.getInstance();
    }
}
