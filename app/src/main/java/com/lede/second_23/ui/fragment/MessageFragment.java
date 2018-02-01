package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.adapter.ConversationListAdapterEx;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.GetReplyActivity;
import com.lede.second_23.ui.activity.GetZanActivity;
import com.lede.second_23.ui.activity.GreetEachActivity;
import com.lede.second_23.ui.activity.GreetReceiveActivity;
import com.lede.second_23.utils.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 新版页面
 * 收件箱页面
 * Created by ld on 18/1/10.
 */

public class MessageFragment extends Fragment {



    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.rong_content)
    FrameLayout frameLayout;


    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_2, container, false);
        context=getActivity();
        ButterKnife.bind(this,view);
        initConversation();
        return view;
    }

    private void initConversation() {
        RongIM.init(getActivity().getApplicationContext());
        if (!(boolean) SPUtils.get(context, GlobalConstants.ISCONNECTED_RONGIM,true)) {
            MyApplication.instance.getRongIMTokenService();
        }
        if (RongIM.getInstance()==null) {
            Log.i("TAG", "onStart: 会话融云为空");
            MyApplication.instance.getRongIMTokenService();
        }

        ConversationListFragment fragment = new ConversationListFragment();
        fragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(),"false") //设置系统会话
                .build();
        fragment.setUri(uri);  //设置 ConverssationListFragment 的显示属性

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }

    @OnClick({R.id.commit,R.id.praise,R.id.friends,R.id.greet})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.commit:
                intent=new Intent(context, GetReplyActivity.class);
                startActivity(intent);
                break;
            case R.id.praise:
                intent=new Intent(context,GetZanActivity.class);
                startActivity(intent);
                break;
            case R.id.friends:
                intent=new Intent(context,GreetEachActivity.class);
                startActivity(intent);
                break;
            case R.id.greet:
                intent=new Intent(context,GreetReceiveActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }



    }

}
