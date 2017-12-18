package com.lede.second_23.broadcastReceiver;

import android.content.Context;
import android.util.Log;

import com.lede.second_23.MyApplication;

import io.rong.push.RongPushClient;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by ld on 17/4/about_we.
 */

public class SealNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {

        PushNotificationMessage message1=message;

        if (message.getConversationType() == RongPushClient.ConversationType.SYSTEM) {
            MyApplication.instance.showSystemNotification(message.getSenderId());
            Log.i("TAG", "push收到新消息:pushID" + message.getPushId()+"targetID"+message.getTargetId());
            return true;
        }
//        message.getPushContent()
        Log.i("TAG", "onNotificationMessageArrived: "+message.getConversationType().toString());
        return false; // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
//        message.ge
        return false; // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }

}