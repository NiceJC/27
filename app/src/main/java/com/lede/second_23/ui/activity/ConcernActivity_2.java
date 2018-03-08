package com.lede.second_23.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.ConcernMsgBean;
import com.lede.second_23.bean.LocationBean;
import com.lede.second_23.bean.UserRelationBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.IMAGE_URLS;
import static com.lede.second_23.global.GlobalConstants.TYPE;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOPEN;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOVERDUE;

/**
 * 动态点击页
 */
public class ConcernActivity_2 extends BaseActivity implements OnResponseListener<String> {


    @Bind(R.id.iv_concern_activity_back)
    ImageView iv_concern_activity_back;
//    @Bind(R.id.iv_concern_activity_user_img)
//    ImageView iv_concern_activity_user_img;
//    @Bind(R.id.iv_concern_activity_user_sex)
//    ImageView iv_concern_activity_user_sex;
    @Bind(R.id.iv_concern_activity_concern)
    ImageView iv_concern_activity_concern;
    @Bind(R.id.iv_concern_activity_message)
    ImageView iv_concern_activity_message;
    @Bind(R.id.iv_concern_activity_location)
    ImageView iv_concern_activity_location;
//    @Bind(R.id.iv_concern_activity_person)
//    ImageView iv_concern_activity_person;
//        @Bind(R.id.tv_concern_activity_username)
//    TextView tv_concern_activity_username;
//    @Bind(R.id.tv_concern_activity_useage)
//    TextView tv_concern_activity_useage;
//    @Bind(R.id.tv_concern_activity_usecity)
//    TextView tv_concern_activity_usecity;
//    @Bind(R.id.hvp_concern_activity_2)
//    HackyViewPager hvp_concern_activity_2;
//    @Bind(R.id.jcplay_concerv_activity_2)
//    JCVideoPlayerStandard jcplay_concerv_activity_2;
//    @Bind(R.id.iv_concern_activity_2_up)
//    ImageView iv_concern_activity_2_up;
    @Bind(R.id.diyiv_conern_activity_2_userimg)
    ImageView diyiv_userimg;
    @Bind(R.id.tv_concern_activity_2_username)
    TextView tv_username;
    @Bind(R.id.tv_concern_activity_2_distance)
    TextView tv_distance;
    @Bind(R.id.ll_concern_activity_2_bottom)
    LinearLayout ll_bottom;
//    @Bind(R.id.ll_concern_activity_2_indicator)
//    LinearLayout ll_inDicator;
    @Bind(R.id.iv_concern_activity_2_report)
    ImageView iv_report;

    @Bind(R.id.ll_concern_activity_2_info)
    LinearLayout userInfoLayout;
    @Bind(R.id.tv_concern_activity_2_title)
    TextView tv_title;
    @Bind(R.id.iv_concern_activity_2_declaration)
    ImageView iv_declaration;

    @Bind(R.id.photo)
    ImageView photo;
    @Bind(R.id.video_play)
    ImageView videoPlay;

    private boolean isFriend = false;

    private float distance;
    private String lat;
    private String lon;
    private Gson mGson;
    private RequestQueue requestQueue;
    private String userId;
    private ArrayList<String> banner = new ArrayList<>();
    private String username;
    private Dialog mDialog;
//    private ConcernUserInfoBean concernUserInfoBean;
    private LatLng mStartPoint;
    private LatLng mEndPoint;
    private int location_type=0;
    private PopupWindow popupWindow;
    private Intent intent;
//    private ArrayList<String> banner1;
//    private String text;
//    private String time;
//    private String videourl;
//    private String picurl;
    private UserRelationBean userRelationBean;
    private String imageUrl=null;
    private String VIPStatus;


    private boolean isFromUserInfoActivity;//标记是否从个人主页跳转而来，如果是 要防止重复跳转（点击头像又跳个人主页）
    private String intentURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern_2);
        ButterKnife.bind(this);
        intent = getIntent();
        userId = intent.getStringExtra(USERID);
        intentURL=intent.getStringExtra(IMAGE_URLS);
        isFromUserInfoActivity=intent.getBooleanExtra("isFromUserInfoActivity",false);
        VIPStatus = (String) SPUtils.get(this, VIPSTATUS, NOTOPEN);

//        videourl = intent.getStringExtra("videourl");
//        picurl = intent.getStringExtra("picurl");
//        time = intent.getStringExtra("time");
//        banner1 = intent.getStringArrayListExtra("banner");
//        text = intent.getStringExtra("text");
        if (userId.equals((String) SPUtils.get(this, USERID, ""))) {
            iv_report.setVisibility(View.GONE);

            diyiv_userimg.setVisibility(View.GONE);
            tv_username.setVisibility(View.GONE);
            tv_distance.setVisibility(View.GONE);
            iv_concern_activity_concern.setVisibility(View.INVISIBLE);
            iv_concern_activity_message.setVisibility(View.GONE);
            iv_concern_activity_location.setVisibility(View.GONE);
            tv_title.setText("我的照片");
        }else{

            if(intent.getIntExtra(TYPE,0)!=0&&intent.getIntExtra(TYPE,0)==2){
                tv_title.setText("照片");
            }




            if (((Boolean) SPUtils.get(this, GlobalConstants.DECLARATION,true))) {
                iv_declaration.setVisibility(View.VISIBLE);
            }else {
                iv_declaration.setVisibility(View.GONE);
                iv_declaration.setClickable(false);
            }
        }

        mGson = new Gson();
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        photo.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,width));

        userLocation();
        userInfoService(userId);
    }

    private void userInfoService(String userId) {
        Request<String> userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/newRelationship", RequestMethod.POST);
        userInfoRequest.add("user_id", (String) SPUtils.get(this, USERID, ""));
        userInfoRequest.add("to_user_id", userId);
        requestQueue.add(100, userInfoRequest, this);
    }

    @OnClick({
            R.id.photo,

            R.id.iv_concern_activity_concern
            , R.id.iv_concern_activity_back, R.id.iv_concern_activity_message
            , R.id.iv_concern_activity_location
//            , R.id.iv_concern_activity_2_up
            , R.id.ll_concern_activity_2_info, R.id.iv_concern_activity_2_report
            ,R.id.iv_concern_activity_2_declaration,R.id.bt_concern_acitivity_2_like})
    public void onclick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.photo:
                if(imageUrl!=null){
                    ArrayList<String> list=new ArrayList<>();

                    list.add(imageUrl);
                    intent=new Intent(this,ForumPicActivity.class);
                    intent.putExtra("position",0);
                    intent.putStringArrayListExtra("banner",list);
                    startActivity(intent);
                }

                break;

            case R.id.ll_concern_activity_2_info:
                if(!isFromUserInfoActivity){
                    intent = new Intent(this, UserInfoActivty.class);
                    intent.putExtra(USERID, userId);
                    startActivity(intent);
                }

                break;
            case R.id.iv_concern_activity_concern:
                if (!isFriend) {
                    concernService();
                }else {
                    destroyService();
                }
                break;
            case R.id.iv_concern_activity_back:
                finish();
                break;
            case R.id.iv_concern_activity_message:
                if (VIPStatus.equals(NOTOVERDUE)) {
                    RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, userId, username);
                }else{
                    if (userRelationBean.getData().isEnd()) {
                        RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, userId, username);
                    } else {
                        showHintDialog(1);
                    }
                }


                break;
            case R.id.iv_concern_activity_location:
                if (userRelationBean.getData().isEnd()) {
                    if (location_type==0) {
                        intent = new Intent(this, PathActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lon", lon);
                        intent.putExtra("dis", tv_distance.getText().toString().trim());
                        startActivity(intent);
                    }else {
                        Toast.makeText(this, "对方隐藏了位置信息哦", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showHintDialog(0);
                }

                break;
//            case R.id.iv_concern_activity_2_up:
//                showPopwindow();
//                iv_concern_activity_2_up.setImageResource(R.mipmap.btn_click);
//                ll_bottom.setVisibility(View.INVISIBLE);
//                break;

            case R.id.iv_concern_activity_2_report:
                showReportPopwindow();
                break;
            case R.id.iv_concern_activity_2_declaration:
                iv_declaration.setVisibility(View.GONE);
                iv_declaration.setClickable(false);
                SPUtils.put(this, GlobalConstants.DECLARATION,false);
                break;
//            case R.id.bt_concern_acitivity_2_like:
//                pushSystemMessage();
//                break;
        }
    }

    private void pushSystemMessage() {
        // 构造 TextMessage 实例
            TextMessage myTextMessage = TextMessage.obtain("我是消息内容");
            /* 生成 Message 对象。
             * "7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
             * Conversation.ConversationType.PRIVATE 为私聊会话类型，根据需要，也可以传入其它会话类型，如群组，讨论组等。
             */
            Message myMessage = Message.obtain("40a0f4aef97e4f7f8ff2e86220e8bfd2", Conversation.ConversationType.SYSTEM, myTextMessage);
            /**
             * <p>发送消息。
             * 通过 {@link IRongCallback.ISendMessageCallback}
             * 中的方法回调发送的消息状态及消息体。</p>
             *
             * @param message     将要发送的消息体。
             * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
             *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
             *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
             * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
             * @param callback    发送消息的回调，参考 {@link IRongCallback.ISendMessageCallback}。
             */
            RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {
                    //消息本地数据库存储成功的回调
                }

                @Override
                public void onSuccess(Message message) {
                    //消息通过网络发送成功的回调
                    Toast.makeText(ConcernActivity_2.this, "点心成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    //消息发送失败的回调
                    Toast.makeText(ConcernActivity_2.this, "点心失败", Toast.LENGTH_SHORT).show();
                }
            });
//            RongIM.getInstance().sendMessage(new Message());
    }

//
//    /**
//     * 点赞
//     */
//    private void likeRequest() {
//        Request<String> likeRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/"+concernUserInfoBean.getData().getForumList().get(0).getForumId()+"/like", RequestMethod.POST);
////        concernRequest.add("id", userid);
//        likeRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
//        requestQueue.add(500,likeRequest,this);
//    }
    private void userLocation() {
        Request<String> userLocationRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/userDistance", RequestMethod.POST);
        userLocationRequest.add("userId", userId);
        requestQueue.add(300, userLocationRequest, this);

    }

    /**
     * 关注请求
     */
    private void concernService() {
        Request<String> concernRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/create", RequestMethod.POST);
        concernRequest.add("id", userId);
        concernRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(200, concernRequest, this);
    }
    private void destroyService() {
        Request<String> concernRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/destroy", RequestMethod.POST);
        concernRequest.add("id", userId);
        concernRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(400, concernRequest, this);
    }

    /**
     * 提示举报底部弹窗
     */
    private void showReportPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_save_or_report, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        ((TextView) view.findViewById(R.id.btn_save)).setText("举报");
        view.findViewById(R.id.btn_save).setOnClickListener(btnlistener);
//        root.findViewById(R.id.btn_report).setOnClickListener(btnlistener);
        view.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.7f;

        this.getWindow().setAttributes(params);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);


        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
//        window.showAtLocation(getActivity().findViewById(R.id.ll_prv_forum_detail_bottom),
//                Gravity.BOTTOM, 0, 0);
        popupWindow.showAtLocation(ll_bottom,
                Gravity.BOTTOM, 0, 0);
        //popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                WindowManager.LayoutParams params = ConcernActivity_2.this.getWindow().getAttributes();
                params.alpha = 1.0f;

                ConcernActivity_2.this.getWindow().setAttributes(params);
            }
        });

    }


    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    popupWindow.dismiss();
                    Intent intent = new Intent(ConcernActivity_2.this, ReportActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btn_cancel:
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    break;
            }
        }
    };



    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        L.i(response.get());
        switch (what) {
            case 100: //获取是否关注以及选中用户的信息
                parserUserInfo(response.get());
                break;
            case 200:
                parserConcernMsg(response.get());
                break;
            case 300:
                Log.i("TAB", "onSucceed: Location"+response.get());
                parserLocation(response.get());
                break;
            case 400:
                parserConcernMsg(response.get());
                break;
            case 500:

                break;
        }
    }

    /**
     * 84ba77bc08ea4e1d8c03c06f6f6c79e5
     * 9e7a060b521049bb990dedc6055b7886
     *
     * @param json
     */

    private void parserLocation(String json) {
        LocationBean locationBean = mGson.fromJson(json, LocationBean.class);
        if(locationBean.getData().getUserAmap()==null){
            return;
        }
        locationBean.getData().getUserAmap().getLat();
        location_type=Integer.parseInt(locationBean.getData().getUserAmap().getType());
        lat = locationBean.getData().getUserAmap().getLat();
        lon = locationBean.getData().getUserAmap().getLon();
        mStartPoint = new LatLng(Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LATITUDE, "")), Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LONGITUDE, "")));
        mEndPoint = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        distance = AMapUtils.calculateLineDistance(mStartPoint, mEndPoint);


        //超过20公里不显示
        if(distance<20*1000){
            if(distance>1000){
                distance=distance/1000;
                tv_distance.setText((int) distance + "km");

            }else{
                tv_distance.setText((int) distance + "m");

            }
        }








    }

    private void parserConcernMsg(String json) {
        ConcernMsgBean concernMsgBean = mGson.fromJson(json, ConcernMsgBean.class);
        if (concernMsgBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,WelcomeActivity.class));
        }else {

            if (concernMsgBean.getMsg().equals("关注成功")) {
                Toast.makeText(this, "关联成功", Toast.LENGTH_SHORT).show();
//                ll_inDicator.removeAllViews();
                userInfoService(userId);
                iv_concern_activity_concern.setImageResource(R.mipmap.smile_on);
            } else {
//                ll_inDicator.removeAllViews();
                userInfoService(userId);

                Toast.makeText(this, "取消关联成功", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onFailed(int what, Response<String> response) {
        switch (what){
            case 100:
                String s=response.get();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFinish(int what) {

    }

    private void parserUserInfo(String s){

        userRelationBean=mGson.fromJson(s, UserRelationBean.class);
        UserRelationBean.DataBean.UserInfo userInfo=userRelationBean.getData().getInfo();
        if(userRelationBean.getData().getUserPhotoList()==null){
            videoPlay.setVisibility(View.GONE);
            imageUrl=userInfo.getImgUrl();
        }else{
          UserRelationBean.DataBean.UserPhotoListBean userPhotoListBean= userRelationBean.getData().getUserPhotoList().get(0);
            if (userPhotoListBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
                videoPlay.setVisibility(View.GONE);
                imageUrl=userPhotoListBean.getUrlImg();
            } else {
                videoPlay.setVisibility(View.VISIBLE);
                imageUrl=userPhotoListBean.getUrlFirst();
            }
        }
        if(intentURL!=null&&!intentURL.equals("")){
            imageUrl=intentURL;
        }

        Glide.with(this).load(imageUrl).into(photo);
        username=userInfo.getNickName();
        tv_username.setText(username);
        Glide.with(this).load(userInfo.getImgUrl()).bitmapTransform(new CropCircleTransformation(this))
                .into(diyiv_userimg);

        /**
         * isFriend 表示自己点过笑脸了  中间的笑脸是亮的
         * isEnd 表示双方都互相点过了 两侧的定位和聊天是亮的 可以聊天了
         */
        if (userRelationBean.getData().isEnd()) {
            Glide.with(this)
                    .load(R.mipmap.comment_on)
                    .into(iv_concern_activity_message);
            iv_concern_activity_message.setClickable(true);
            Glide.with(this)
                    .load(R.mipmap.position_on)
                    .into(iv_concern_activity_location);
            iv_concern_activity_location.setClickable(true);
        } else {
            Glide.with(this)
                    .load(R.mipmap.comment_off)
                    .into(iv_concern_activity_message);
            Glide.with(this)
                    .load(R.mipmap.position_off)
                    .into(iv_concern_activity_location);
        }

        isFriend = userRelationBean.getData().isFirend();
        if (isFriend) {
            iv_concern_activity_concern.setImageResource(R.mipmap.smile_on);
        } else {
            iv_concern_activity_concern.setImageResource(R.mipmap.smile_off);
            iv_concern_activity_concern.setClickable(true);
        }

    }



    public void showHintDialog(int type){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_hint_dialog,null);
        ImageView iv=(ImageView) layout.findViewById(R.id.iv_layout_hint_dialog);
        if (type==0) {
            iv.setImageResource(R.mipmap.hintlocation);
        }else {
            iv.setImageResource(R.mipmap.hintmessage);
        }
        Dialog hintDialog=new Dialog(this);
        hintDialog.setContentView(layout);
        hintDialog.show();
    }
}
