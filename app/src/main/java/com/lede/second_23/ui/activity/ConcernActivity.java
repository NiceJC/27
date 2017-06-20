package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.ConcernMsgBean;
import com.lede.second_23.bean.ConcernUserInfoBean;
import com.lede.second_23.bean.LocationBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class ConcernActivity extends AppCompatActivity implements OnResponseListener<String> {



    @Bind(R.id.iv_concern_activity_back)
    ImageView iv_concern_activity_back;
    @Bind(R.id.iv_concern_activity_user_img)
    ImageView iv_concern_activity_user_img;
    @Bind(R.id.iv_concern_activity_user_sex)
    ImageView iv_concern_activity_user_sex;
    @Bind(R.id.iv_concern_activity_concern)
    ImageView iv_concern_activity_concern;
    @Bind(R.id.iv_concern_activity_message)
    ImageView iv_concern_activity_message;
    @Bind(R.id.iv_concern_activity_location)
    ImageView iv_concern_activity_location;
    @Bind(R.id.iv_concern_activity_person)
    ImageView iv_concern_activity_person;
    @Bind(R.id.tv_concern_activity_username)
    TextView tv_concern_activity_username;
    @Bind(R.id.tv_concern_activity_useage)
    TextView tv_concern_activity_useage;
    @Bind(R.id.tv_concern_activity_usecity)
    TextView tv_concern_activity_usecity;

    private boolean isFriend=false;

    private Gson mGson;
    private RequestQueue requestQueue;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);
        ButterKnife.bind(this);
        userid=getIntent().getStringExtra("userid");
//        userid="84ba77bc08ea4e1d8c03c06f6f6c79e5";
        mGson = new Gson();
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        userInfoService(userid);
//        Glide.with(this).fromResource(R.mipmap.test7).transform(new OvalImageView(this))
    }

    private void userInfoService(String userid) {
        Request<String> userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/relationship", RequestMethod.POST);
        userInfoRequest.add("user_id", (String) SPUtils.get(this,GlobalConstants.USERID,""));
        userInfoRequest.add("to_user_id",userid);
        requestQueue.add(100,userInfoRequest,this);
    }

    @OnClick({R.id.iv_concern_activity_person,R.id.iv_concern_activity_concern,R.id.iv_concern_activity_back,R.id.iv_concern_activity_message,R.id.iv_concern_activity_location})
    public void onclick(View view){
        Intent intent=null;
        switch (view.getId()) {
            case R.id.iv_concern_activity_person:
                intent=new Intent(this,CheckOthersInfoActivity.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
                break;
            case R.id.iv_concern_activity_concern:
                concernService();
                break;
            case R.id.iv_concern_activity_back:
                finish();
                break;
            case R.id.iv_concern_activity_message:
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE,userid,tv_concern_activity_username.getText().toString());
                break;
            case R.id.iv_concern_activity_location:
                userLocation();
//                intent=new Intent(this,PathActivity.class);
                break;
        }
    }

    private void userLocation() {
        Request<String> userLocationRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/userDistance", RequestMethod.POST);
        userLocationRequest.add("userId",userid);
        requestQueue.add(300,userLocationRequest,this);

    }

    /**
     * 关注请求
     */
    private void concernService() {
//        Request<String> concernRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/create", RequestMethod.POST);
        Request<String> concernRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/create", RequestMethod.POST);
        concernRequest.add("id",userid);
        concernRequest.add("access_token", (String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        requestQueue.add(200,concernRequest,this);
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        L.i(response.get());
        switch (what) {
            case 100: //获取是否关注以及选中用户的信息
                parserConcernUserInfo(response.get());
                break;
            case 200:
                parserConcernMsg(response.get());
                break;
            case 300:
                parserLocation(response.get());
                break;
        }
    }

    private void parserLocation(String json) {
        LocationBean locationBean=mGson.fromJson(json,LocationBean.class);
        locationBean.getData().getUserAmap().getLat();
        Intent intent=new Intent(this,PathActivity.class);
        intent.putExtra("lat",locationBean.getData().getUserAmap().getLat());
        intent.putExtra("lon",locationBean.getData().getUserAmap().getLon());
        startActivity(intent);
    }

    private void parserConcernMsg(String json) {
        ConcernMsgBean concernMsgBean=mGson.fromJson(json,ConcernMsgBean.class);
        Toast.makeText(this, concernMsgBean.getMsg(), Toast.LENGTH_SHORT).show();
        if (concernMsgBean.getMsg().equals("关注成功")) {
            userInfoService(userid);
            iv_concern_activity_concern.setImageResource(R.mipmap.click_smile);
            iv_concern_activity_concern.setClickable(false);
            // 构造 TextMessage 实例
            TextMessage myTextMessage = TextMessage.obtain("我是消息内容");
            /* 生成 Message 对象。
             * "7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
             * Conversation.ConversationType.PRIVATE 为私聊会话类型，根据需要，也可以传入其它会话类型，如群组，讨论组等。
             */
            Message myMessage = Message.obtain(userid, Conversation.ConversationType.SYSTEM, myTextMessage);
            /**
             * <p>发送消息。
             * 通过 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}
             * 中的方法回调发送的消息状态及消息体。</p>
             *
             * @param message     将要发送的消息体。
             * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
             *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
             *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
             * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
             * @param callback    发送消息的回调，参考 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}。
             */
            RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {
                    //消息本地数据库存储成功的回调
                }

                @Override
                public void onSuccess(Message message) {
                    //消息通过网络发送成功的回调
                    Toast.makeText(ConcernActivity.this, "点心成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    //消息发送失败的回调
                    Toast.makeText(ConcernActivity.this, "点心失败", Toast.LENGTH_SHORT).show();
                }
            });
//            RongIM.getInstance().sendMessage(new Message());
        }else {
            iv_concern_activity_concern.setImageResource(R.mipmap.smile);
            iv_concern_activity_concern.setClickable(true);
        }
    }


    @Override
    public void onFailed(int what, Response<String> response) {
    }

    @Override
    public void onFinish(int what) {

    }

    private void parserConcernUserInfo(String json) {
        ConcernUserInfoBean concernUserInfoBean=mGson.fromJson(json,ConcernUserInfoBean.class);
        if (concernUserInfoBean.getData().isEnd()) {
            Glide.with(this)
                    .load(R.mipmap.click_message)
                    .error(R.mipmap.loading)
                    .into(iv_concern_activity_message);
            iv_concern_activity_message.setClickable(true);
            Glide.with(this)
                    .load(R.mipmap.click_location)
                    .error(R.mipmap.loading)
                    .into(iv_concern_activity_location);
            iv_concern_activity_location.setClickable(true);
        }else {
            iv_concern_activity_message.setClickable(false);
            iv_concern_activity_location.setClickable(false);
        }
        ConcernUserInfoBean.DataBean dataBean=concernUserInfoBean.getData();
        isFriend=dataBean.isFirend();
        if (isFriend) {
            iv_concern_activity_concern.setImageResource(R.mipmap.click_smile);
            iv_concern_activity_concern.setClickable(false);
        }else {
            iv_concern_activity_concern.setImageResource(R.mipmap.smile);
            iv_concern_activity_concern.setClickable(true);
        }
        if (dataBean.getForumList().get(0).getForumMedia().getPath().equals("http://7xr1tb.com1.z0.glb.clouddn.com/null")) {
            Glide.with(this)
                    .load(dataBean.getForumList().get(0).getImgs().get(0).getUrl())
                    .error(R.mipmap.loading)
                    .placeholder(R.mipmap.loading)
                    .into(iv_concern_activity_user_img);
        }else {
            Glide.with(this)
                    .load(dataBean.getForumList().get(0).getForumMedia().getPic())
                    .error(R.mipmap.loading)
                    .placeholder(R.mipmap.loading)
                    .into(iv_concern_activity_user_img);
        }

        ConcernUserInfoBean.DataBean.InfoBean infoBean=dataBean.getInfo();

        if (infoBean.getSex().equals("男")) {
            iv_concern_activity_user_sex.setImageResource(R.mipmap.sex_boy);
        }else {
            iv_concern_activity_user_sex.setImageResource(R.mipmap.sex_girl);
        }
        tv_concern_activity_username.setText(infoBean.getNickName());
        tv_concern_activity_useage.setText(infoBean.getHobby()+" "+infoBean.getQq());
        tv_concern_activity_usecity.setText(infoBean.getAddress());
    }
}
