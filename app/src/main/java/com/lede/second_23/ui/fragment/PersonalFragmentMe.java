package com.lede.second_23.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.activity.SameCityActivity;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.ISGIRL;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOVERDUE;

/**
 * Created by ld on 17/11/16.
 */

public class PersonalFragmentMe extends Fragment {



    @Bind(R.id.user_location)
    TextView userLocation;
    @Bind(R.id.user_age)
    TextView userAge;
    @Bind(R.id.user_hobby)
    TextView userHobby;
    @Bind(R.id.user_school)
    TextView userSchool;

    @Bind(R.id.location_click)
    LinearLayout locationClick;

    @Bind(R.id.vip_message)
    LinearLayout vipMessage;

    private Request<String> userInfoRequest = null;
    private final static int REQUEST_USER_INFO=23451;
    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private String userId;
    private String userName;
    private RequestManager requestManager;
    private UserInfoBean.DataBean.InfoBean userInfo;
    private String address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_personal_me,container,false);
        ButterKnife.bind(this,view);
        requestManager= Glide.with(this);

        mGson = new Gson();
        String userID=getActivity().getIntent().getStringExtra(USERID);
        String VIPStatus=(String) SPUtils.get(getContext(), VIPSTATUS, "");
        if(userID!=null&&!userID.equals("")){//他人主页
            userId=userID;
            //当前展示的不是自己的主页，并且自己是会员  ，显示会员聊天按钮
            if(!userId.equals((String) SPUtils.get(getContext(), USERID, ""))&&VIPStatus.equals(NOTOVERDUE)){
                vipMessage.setVisibility(View.VISIBLE);
            }else{
                vipMessage.setVisibility(View.GONE);
            }

        }else { //自己主页
            userId=(String) SPUtils.get(getContext(), USERID, "");
            vipMessage.setVisibility(View.GONE);

        }
        doRequest();
        return view;
    }
    @OnClick({R.id.location_click,R.id.vip_message_click})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.location_click:
                if(address!=null&&!address.trim().equals("")){
                    Intent intent=new Intent(getActivity(), SameCityActivity.class);
                    intent.putExtra(ADDRESS,address);
                    intent.putExtra(ISGIRL,false);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"暂无位置信息",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.vip_message_click:

                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, userId, userName);

                break;

            default:
                break;
        }
    }
    public void doRequest() {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        parseUserInfo(response.get());
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        break;
                    default:
                        break;
                }
            }
        };


        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/" + userId + "/heDetail", RequestMethod.POST);
        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);


    }


    private void parseUserInfo(String s) {
        UserInfoBean userInfoBean = mGson.fromJson(s, UserInfoBean.class);
        userInfo = userInfoBean.getData().getInfo();
        setUserInfo();
    }

    public void setUserInfo() {

        userName=userInfo.getNickName();
        address=userInfo.getAddress();
        userLocation.setText(userInfo.getAddress());
        userAge.setText(userInfo.getHobby());
        userHobby.setText(userInfo.getWechat());
        userSchool.setText(userInfo.getHometown());
    }


}
