package com.lede.second_23.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lede.second_23.MyApplication;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


/**
 * Created by ld on 17/6/8.
 */

public class NotificationClickReceiver extends BroadcastReceiver {
    private String userid="";
    private String username="";
    @Override
    public void onReceive(Context context, Intent intent) {
        userid= intent.getStringExtra("userid");
        username=intent.getStringExtra("name");
        RongIM.getInstance().startConversation(MyApplication.instance, Conversation.ConversationType.PRIVATE,userid,username);
    }
}
