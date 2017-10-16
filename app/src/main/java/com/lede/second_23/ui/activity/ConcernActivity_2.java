package com.lede.second_23.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.adapter.ImageViewPagerAdapter_2;
import com.lede.second_23.bean.ConcernMsgBean;
import com.lede.second_23.bean.ConcernUserInfoBean;
import com.lede.second_23.bean.LocationBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.view.HackyViewPager;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.TimeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 动态点击页
 */
public class ConcernActivity_2 extends AppCompatActivity implements OnResponseListener<String> {


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
    @Bind(R.id.iv_concern_activity_person)
    ImageView iv_concern_activity_person;
    //    @Bind(R.id.tv_concern_activity_username)
//    TextView tv_concern_activity_username;
//    @Bind(R.id.tv_concern_activity_useage)
//    TextView tv_concern_activity_useage;
//    @Bind(R.id.tv_concern_activity_usecity)
//    TextView tv_concern_activity_usecity;
    @Bind(R.id.hvp_concern_activity_2)
    HackyViewPager hvp_concern_activity_2;
    @Bind(R.id.jcplay_concerv_activity_2)
    JCVideoPlayerStandard jcplay_concerv_activity_2;
    @Bind(R.id.iv_concern_activity_2_up)
    ImageView iv_concern_activity_2_up;
    @Bind(R.id.diyiv_conern_activity_2_userimg)
    DIYImageView diyiv_userimg;
    @Bind(R.id.tv_concern_activity_2_username)
    TextView tv_username;
    @Bind(R.id.tv_concern_activity_2_distance)
    TextView tv_distance;
    @Bind(R.id.ll_concern_activity_2_bottom)
    LinearLayout ll_bottom;
    @Bind(R.id.ll_concern_activity_2_indicator)
    LinearLayout ll_inDicator;
    @Bind(R.id.iv_concern_activity_2_report)
    ImageView iv_report;
    @Bind(R.id.tv_concern_activity_2_time)
    TextView tv_time;
    @Bind(R.id.tv_concern_activity_2_title)
    TextView tv_title;
    @Bind(R.id.iv_concern_activity_2_declaration)
    ImageView iv_declaration;

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
    private ConcernUserInfoBean concernUserInfoBean;
    private LatLng mStartPoint;
    private LatLng mEndPoint;
    private int location_type=0;
    private PopupWindow popupWindow;
    private Intent intent;
    private ArrayList<String> banner1;
    private String text;
    private String time;
    private String videourl;
    private String picurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern_2);
        ButterKnife.bind(this);
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        videourl = intent.getStringExtra("videourl");
        picurl = intent.getStringExtra("picurl");
        time = intent.getStringExtra("time");
        banner1 = intent.getStringArrayListExtra("banner");
        text = intent.getStringExtra("text");
        if (userId.equals((String) SPUtils.get(this, GlobalConstants.USERID, ""))) {
            iv_report.setVisibility(View.GONE);
            iv_concern_activity_concern.setVisibility(View.GONE);
            iv_concern_activity_message.setVisibility(View.GONE);
            iv_concern_activity_location.setVisibility(View.GONE);
            tv_title.setText("自己的动态");
        }
        if (((Boolean) SPUtils.get(this, GlobalConstants.DECLARATION,true))) {
            iv_declaration.setVisibility(View.VISIBLE);
        }else {
            iv_declaration.setVisibility(View.GONE);
            iv_declaration.setClickable(false);
        }
        mGson = new Gson();
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        userLocation();
        userInfoService(userId);
    }

    private void userInfoService(String userId) {
        Request<String> userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/relationship", RequestMethod.POST);
        userInfoRequest.add("user_id", (String) SPUtils.get(this, GlobalConstants.USERID, ""));
        userInfoRequest.add("to_user_id", userId);
        requestQueue.add(100, userInfoRequest, this);
    }

    @OnClick({R.id.iv_concern_activity_person, R.id.iv_concern_activity_concern
            , R.id.iv_concern_activity_back, R.id.iv_concern_activity_message
            , R.id.iv_concern_activity_location, R.id.iv_concern_activity_2_up
            , R.id.ll_concern_activity_2_info, R.id.iv_concern_activity_2_report
            ,R.id.iv_concern_activity_2_declaration,R.id.bt_concern_acitivity_2_like})
    public void onclick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_concern_activity_person:
                intent = new Intent(this, CheckOthersInfoActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
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
                if (concernUserInfoBean.getData().isEnd()) {
                    RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, userId, username);
                } else {
                    showHintDialog(1);
                }

                break;
            case R.id.iv_concern_activity_location:
                if (concernUserInfoBean.getData().isEnd()) {
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
            case R.id.iv_concern_activity_2_up:
                showPopwindow();
                iv_concern_activity_2_up.setImageResource(R.mipmap.btn_click);
                ll_bottom.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_concern_activity_2_info:
                intent = new Intent(this, OtherPersonActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
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


    /**
     * 点赞
     */
    private void likeRequest() {
        Request<String> likeRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/"+concernUserInfoBean.getData().getForumList().get(0).getForumId()+"/like", RequestMethod.POST);
//        concernRequest.add("id", userid);
        likeRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        requestQueue.add(500,likeRequest,this);
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
                System.out.println("popWindow消失");
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
        locationBean.getData().getUserAmap().getLat();
        location_type=Integer.parseInt(locationBean.getData().getUserAmap().getType());
        lat = locationBean.getData().getUserAmap().getLat();
        lon = locationBean.getData().getUserAmap().getLon();
        mStartPoint = new LatLng(Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LATITUDE, "")), Double.parseDouble((String) SPUtils.get(this, GlobalConstants.LONGITUDE, "")));
        mEndPoint = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        distance = AMapUtils.calculateLineDistance(mStartPoint, mEndPoint);
        tv_distance.setText((int) distance + "米");
    }

    private void parserConcernMsg(String json) {
        ConcernMsgBean concernMsgBean = mGson.fromJson(json, ConcernMsgBean.class);
        if (concernMsgBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,WelcomeActivity.class));
        }else {

            if (concernMsgBean.getMsg().equals("关注成功")) {
                Toast.makeText(this, "关联成功", Toast.LENGTH_SHORT).show();
                ll_inDicator.removeAllViews();
                userInfoService(userId);
                iv_concern_activity_concern.setImageResource(R.mipmap.smile_on);
            } else {
                ll_inDicator.removeAllViews();
                userInfoService(userId);

                Toast.makeText(this, "取消关联成功", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onFailed(int what, Response<String> response) {
    }

    @Override
    public void onFinish(int what) {

    }

    private void parserConcernUserInfo(String json) {
        concernUserInfoBean = mGson.fromJson(json, ConcernUserInfoBean.class);
        SPUtils.put(this, GlobalConstants.CURRENT_USERID, userId);
        if (concernUserInfoBean.getData().getForumList() != null) {
            SPUtils.put(this, GlobalConstants.CURRENT_FORUMID, concernUserInfoBean.getData().getForumList().get(0).getForumId() + "");

        }
        SPUtils.put(this, GlobalConstants.CURRENT_USERIMG, concernUserInfoBean.getData().getInfo().getImgUrl() + "");

        Glide.with(this)
                .load(concernUserInfoBean.getData().getInfo().getImgUrl())
                .error(R.mipmap.loading)
                .into(diyiv_userimg);
        if (concernUserInfoBean.getData().getInfo().getTrueName().equals("1")) {
            Drawable drawableRight = getResources().getDrawable(
                    R.mipmap.v2);

            tv_username.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
            tv_username.setCompoundDrawablePadding(2);
        }
        tv_username.setText(concernUserInfoBean.getData().getInfo().getNickName());

        if (concernUserInfoBean.getData().isEnd()) {
            Glide.with(this)
                    .load(R.mipmap.comment_on)
                    .error(R.mipmap.loading)
                    .into(iv_concern_activity_message);
            iv_concern_activity_message.setClickable(true);
            Glide.with(this)
                    .load(R.mipmap.position_on)
                    .error(R.mipmap.loading)
                    .into(iv_concern_activity_location);
            iv_concern_activity_location.setClickable(true);
        } else {
            Glide.with(this)
                    .load(R.mipmap.comment_off)
                    .error(R.mipmap.loading)
                    .into(iv_concern_activity_message);
            Glide.with(this)
                    .load(R.mipmap.position_off)
                    .error(R.mipmap.loading)
                    .into(iv_concern_activity_location);
        }
        ConcernUserInfoBean.DataBean dataBean = concernUserInfoBean.getData();
        isFriend = dataBean.isFirend();
        if (isFriend) {
            iv_concern_activity_concern.setImageResource(R.mipmap.smile_on);
        } else {
            iv_concern_activity_concern.setImageResource(R.mipmap.smile_off);
            iv_concern_activity_concern.setClickable(true);
        }
        if (banner1==null&&videourl==null) {
            if (dataBean.getForumList()!=null) {
                Date createDate=null;
                //"2017-05-19 17:15:40"
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    createDate=formatter.parse(concernUserInfoBean.getData().getForumList().get(0).getCreateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (createDate!=null) {
                    tv_time.setText(TimeUtils.getTimeFormatText(createDate));
                }
                //"http://7xr1tb.com1.z0.glb.clouddn.com/null"
                if (dataBean.getForumList().get(0).getForumMedia().getPath().equals("http://my-photo.lacoorent.com/null")) {
                    hvp_concern_activity_2.setVisibility(View.VISIBLE);
                    jcplay_concerv_activity_2.setVisibility(View.GONE);
                    if (dataBean.getForumList().get(0).getImgs().size() != 1) {
                        ll_inDicator.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < dataBean.getForumList().get(0).getImgs().size(); i++) {
                        banner.add(dataBean.getForumList().get(0).getImgs().get(i).getUrl());
                        ImageView inDicator = (ImageView) LayoutInflater.from(this).inflate(R.layout.layout_indicator, ll_inDicator, false);
                        if (i == 0) {
                            inDicator.setImageResource(R.mipmap.current);
                        }
                        ll_inDicator.addView(inDicator);
                    }

                    ImageViewPagerAdapter_2 adapter = new ImageViewPagerAdapter_2(getSupportFragmentManager(), banner);

                    hvp_concern_activity_2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            for (int i = 0; i < ll_inDicator.getChildCount(); i++) {
                                if (i == position) {
                                    ((ImageView) ll_inDicator.getChildAt(i)).setImageResource(R.mipmap.current);
                                } else {
                                    ((ImageView) ll_inDicator.getChildAt(i)).setImageResource(R.mipmap.unckecked);
                                }
                            }
                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    hvp_concern_activity_2.setAdapter(adapter);
                } else {
                    hvp_concern_activity_2.setVisibility(View.GONE);
                    jcplay_concerv_activity_2.setVisibility(View.VISIBLE);
                    jcplay_concerv_activity_2.setUp(dataBean.getForumList().get(0).getForumMedia().getPath(),
                            JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
                    Glide.with(this).load(dataBean.getForumList().get(0).getForumMedia().getPic()).into(jcplay_concerv_activity_2.thumbImageView);

                }
            }else {
                Toast.makeText(this, "用户还没发表过内容", Toast.LENGTH_SHORT).show();
            }
        }else if (banner1!=null){
            Date createDate=null;
            //"2017-05-19 17:15:40"
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                createDate=formatter.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (createDate!=null) {
                tv_time.setText(TimeUtils.getTimeFormatText(createDate));
            }
            hvp_concern_activity_2.setVisibility(View.VISIBLE);
            jcplay_concerv_activity_2.setVisibility(View.GONE);
            if (banner1.size() != 1) {
                ll_inDicator.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < banner1.size(); i++) {
                ImageView inDicator = (ImageView) LayoutInflater.from(this).inflate(R.layout.layout_indicator, ll_inDicator, false);
                if (i == 0) {
                    inDicator.setImageResource(R.mipmap.current);
                }
                ll_inDicator.addView(inDicator);
            }

            ImageViewPagerAdapter_2 adapter = new ImageViewPagerAdapter_2(getSupportFragmentManager(), banner1);

            hvp_concern_activity_2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int i = 0; i < ll_inDicator.getChildCount(); i++) {
                        if (i == position) {
                            ((ImageView) ll_inDicator.getChildAt(i)).setImageResource(R.mipmap.current);
                        } else {
                            ((ImageView) ll_inDicator.getChildAt(i)).setImageResource(R.mipmap.unckecked);
                        }
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            hvp_concern_activity_2.setAdapter(adapter);

        }else {
            hvp_concern_activity_2.setVisibility(View.GONE);
            jcplay_concerv_activity_2.setVisibility(View.VISIBLE);
            jcplay_concerv_activity_2.setUp(videourl,
                    JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            Glide.with(this).load(picurl).into(jcplay_concerv_activity_2.thumbImageView);

        }


        username = dataBean.getInfo().getNickName();
    }

    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_concern_text, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(this.findViewById(R.id.activity_concern_2),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        TextView tv_text = (TextView) view.findViewById(R.id.tv_concern_text);
        if (text!=null) {
            tv_text.setText(text);
        }else {
            if (concernUserInfoBean.getData().getForumList()!=null) {
                tv_text.setText(concernUserInfoBean.getData().getForumList().get(0).getText().toString().trim());
            }
        }

         //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                ll_bottom.setVisibility(View.VISIBLE);
                iv_concern_activity_2_up.setImageResource(R.mipmap.btn_click_up);
            }
        });

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
