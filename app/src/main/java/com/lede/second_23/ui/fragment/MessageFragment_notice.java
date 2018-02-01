//package com.lede.second_23.ui.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.lede.second_23.R;
//import com.lede.second_23.ui.activity.GetReplyActivity;
//import com.lede.second_23.ui.activity.GetVideoReplyActivity;
//import com.lede.second_23.ui.activity.GetZanActivity;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//
//import butterknife.Bind;
//import butterknife.OnClick;
//
///**
// * 通知消息页面
// * Created by ld on 18/1/10.
// */
//
//public class MessageFragment_notice extends Fragment {
//
//
//    @Bind(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @Bind(R.id.refresh_layout)
//    SmartRefreshLayout refreshLayout;
//
//
//    private Context context;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_message_2, container, false);
//        context=getActivity();
//        return view;
//    }
//
//    @OnClick({R.id.commit,R.id.praise,R.id.video,R.id.greet})
//    public void onClick(View view){
//        Intent intent;
//        switch (view.getId()){
//            case R.id.commit:
//                intent=new Intent(context, GetReplyActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.praise:
//                intent=new Intent(context,GetZanActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.video:
//                intent=new Intent(context,GetVideoReplyActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.greet:
//                break;
//            default:
//                break;
//
//        }
//
//
//
//    }
//
//}
