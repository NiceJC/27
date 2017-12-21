package com.lede.second_23.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.MatchedUserBean;
import com.lede.second_23.bean.NewMatingUserBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.MatingService;
import com.lede.second_23.service.PushService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.StatusBarUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.umeng.analytics.MobclickAgent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;
import static com.lede.second_23.global.GlobalConstants.USER_HEAD_IMG;
import static com.lede.second_23.global.GlobalConstants.USER_SEX;
import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOVERDUE;

/**
 * Created by ld on 17/12/1.
 */

public class MateActivity extends BaseActivity {
    private Gson mGson;

    private String headImgURl;

    @Bind(R.id.head_img)
    ImageView headImageView;

    @Bind(R.id.result_text)
    TextView resultText;
    @Bind(R.id.mating_gif)
    ImageView matingGIF;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.start_mate)
    Button mateButton;

    @Bind(R.id.mate_before)
    ImageView mateBeforeView;
    @Bind(R.id.mating)
    LinearLayout matingView;
    @Bind(R.id.mate_no_result)
    TextView mateNoResultView;
    @Bind(R.id.mating_timer)
    TextView matingTimer;
    @Bind(R.id.mate_result_bottom)
    LinearLayout mateResultView;
    @Bind(R.id.to_refresh)
    ImageView refreshView;

    @Bind(R.id.remain_time)
    TextView remainTimes;
    @Bind(R.id.start_chat)
    ImageView startChat;
    @Bind(R.id.to_apply_vip)
    TextView toApplyVIP;

    @Bind(R.id.remain_time_linear)
            LinearLayout remainTimeLinear;


    Runnable runnable;
    Runnable matingRunnable;
    Handler handler = new Handler();
    private boolean isMating = false; //匹配中flag

    private int spendSeconds = 0;
    private boolean isVIP = false;
    private int remainedTimes = 3;

    private int choosenItem=-1;
    private List<NewMatingUserBean.DataBean.UserInfoListBean> userList = new ArrayList<>(); //要显示的User


    private NewMatingUserBean.DataBean.UserInfoListBean choosenUser;

    private List<NewMatingUserBean.DataBean.UserInfoListBean> choosenUserList = new ArrayList<>(); //选中的User

    private MatingService matingService;

    private PushService pushService;
    private CommonAdapter adapter;
    private String matchedSex="女";
    private int pageNum=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mate);
        StatusBarUtil.transparencyBar(this);

        matingService=new MatingService(this);
        pushService=new PushService(this);

        ButterKnife.bind(this);
        mGson = new Gson();
        headImgURl = (String) SPUtils.get(this, USER_HEAD_IMG, "");
        String sex= (String) SPUtils.get(this,USER_SEX,"男");

        String vipstatus=(String)SPUtils.get(this,VIPSTATUS,"");



        if(vipstatus.equals(NOTOVERDUE)){
            isVIP=true;
        }

        if(sex.equals("女")){
            matchedSex="男";
        }
        initView();
        initEvent();

    }



    @OnClick({R.id.back, R.id.start_mate, R.id.to_refresh, R.id.start_chat,R.id.to_apply_vip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:

                matingService.requestVerify(new MyCallBack() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFail(String mistakeInfo) {

                    }
                });

                finish();
                break;
            case R.id.start_mate:
                if (isMating) {
                    //取消匹配
                    isMating = false;
                    mateButton.setSelected(false);
                    matingGIF.setVisibility(View.GONE);
                    mateBeforeView.setVisibility(View.VISIBLE);
                    matingView.setVisibility(View.GONE);
                    matingTimer.setText("0");
                    spendSeconds = 0;


                } else {


                    if(!isVIP&&remainedTimes<=0){
                        showNomoreDialog();
                        return;

                    }

                    //开始匹配
                    Glide.with(this).load(R.mipmap.flash8).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(matingGIF);

                    isMating = true;
//                    choosenUserList.clear();

                    choosenItem=-1;
                    startChat.setSelected(false);
                    startChat.setClickable(false);


                    mateButton.setSelected(true);
                    matingGIF.setVisibility(View.VISIBLE);
                    matingView.setVisibility(View.VISIBLE);
                    mateBeforeView.setVisibility(View.GONE);
                    handler.postDelayed(runnable, 1000);


                    pageNum=1;
                    startMating();


                }
                break;

            case R.id.to_refresh:
                if(!isVIP&&remainedTimes<=0){
                    showNomoreDialog();
                    return;

                }




                choosenItem=-1;
                startChat.setSelected(false);
                startChat.setClickable(false);


                Glide.with(this).load(R.mipmap.flash8).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(matingGIF);

                isMating = true;
                resultText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                mateResultView.setVisibility(View.GONE);
                mateButton.setVisibility(View.VISIBLE);
                matingView.setVisibility(View.VISIBLE);
                matingGIF.setVisibility(View.VISIBLE);
                headImageView.setVisibility(View.VISIBLE);
                spendSeconds = 0;
                handler.postDelayed(runnable, 1000);
                pageNum++;
                startMating();
                break;
            case R.id.start_chat:

                // 构造 TextMessage 实例
                TextMessage myTextMessage = TextMessage.obtain("hi");


                String username=choosenUser.getNickName();
                String userId=choosenUser.getUserId();

                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, userId, username);

                pushMatchedInfo(userId);
                /* 生成 Message 对象。
                 * "7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
                 * Conversation.ConversationType.PRIVATE 为私聊会话类型，根据需要，也可以传入其它会话类型，如群组，讨论组等。
                */

                


//                for(NewMatingUserBean.DataBean.UserInfoListBean user :choosenUserList){
//                    Message myMessage = Message.obtain(user.getUserId(), Conversation.ConversationType.PRIVATE, myTextMessage);
//                    pushMatchedInfo(user.getUserId());
//                    RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
//                        @Override
//                        public void onAttached(Message message) {
//                            //消息本地数据库存储成功的回调
//                        }
//
//                        @Override
//                        public void onSuccess(Message message) {
//                            //消息通过网络发送成功的回调
//
//                        }
//
//                        @Override
//                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//                            //消息发送失败的回调
//                        }
//                    });
//
//                }





                break;

            case R.id.to_apply_vip:
                Intent intent=new Intent(MateActivity.this,VIPApplyActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }


    }


    /**
     * 点击开始匹配按钮之后 ，进入匹配流程
     * 先创建匹配
     * 然后每隔5秒查询一次 匹配结果，
     * 返回结果不为空，即停止匹配
     */
    private void startMating() {



        matingService.requestCreate(new MyCallBack() {
            @Override
            public void onSuccess(Object o) {


                handler.postDelayed(matingRunnable, 1000);


            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });
    }


    /**
     * 匹配返回结果不为空，则视为匹配成功
     * 成功后 停止匹配，并且如果用户不是VIP，需要次数减一
     *
     */
    private void parseMatchedUsers(List<NewMatingUserBean.DataBean.UserInfoListBean> list) {



        if(!isVIP&&remainedTimes<=0){
            showNomoreDialog();
            isMating = false;
            mateButton.setSelected(false);
            matingGIF.setVisibility(View.GONE);
            mateBeforeView.setVisibility(View.VISIBLE);
            matingView.setVisibility(View.GONE);
            matingTimer.setText("0");
            spendSeconds = 0;
            return;

        }

        if (list.size() != 0) {



            isMating = false;
            userList.clear();
            userList.addAll(list);
            adapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
            mateResultView.setVisibility(View.VISIBLE);

            resultText.setVisibility(View.VISIBLE);
            matingView.setVisibility(View.GONE);
            matingGIF.setVisibility(View.GONE);
            headImageView.setVisibility(View.GONE);
            mateButton.setVisibility(View.GONE);
            if(isVIP){

                toApplyVIP.setVisibility(View.GONE);
                remainTimeLinear.setVisibility(View.INVISIBLE);
            }
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(recyclerView, "alpha", 0.0f, 1.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(recyclerView, "translationX", 360f, 0f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animator1, animator2);
            set.setDuration(1000);
            set.start();


            reduceCount();

        }
    }

    private void reduceCount() {
        if (!isVIP) {
//            MatingService matingService = new MatingService(this);
            matingService.requestRefreshCount(new MyCallBack() {
                @Override
                public void onSuccess(Object o) {
                    remainedTimes--;
                    remainTimes.setText(remainedTimes+"");
                }

                @Override
                public void onFail(String mistakeInfo) {

                }
            });
        }


    }

    //非VIP用户次数用完提示
    private void showNomoreDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.nomore_time_dialog))
                .setContentBackgroundResource(R.drawable.shape_linearlayout_all)
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.no:

                                dialog.dismiss();
                                break;
                            case R.id.yes:

                                Intent intent=new Intent(MateActivity.this,VIPApplyActivity.class);
                                startActivity(intent);

                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setExpanded(false).create();
        dialogPlus.show();

    }

    //在这里进行验证
    private void initEvent() {

        matingService.requestVerify(new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

                remainedTimes= (int) o;
                mateButton.setEnabled(true);

            }

            @Override
            public void onFail(String mistakeInfo) {
                mateButton.setEnabled(true);
                Toast.makeText(MateActivity.this,mistakeInfo,Toast.LENGTH_SHORT).show();
            }
        });
        mateButton.setEnabled(false);

    }

    public void initView() {

        remainTimes.setText(remainedTimes+"");

        startChat.setSelected(false);
        startChat.setClickable(false);

        runnable = new Runnable() { //计数
            @Override
            public void run() {
                if (isMating) {
                    spendSeconds++;
                    matingTimer.setText("" + spendSeconds);
                    handler.postDelayed(this, 1000);
                }

            }
        };
        matingRunnable = new Runnable() { //匹配
            @Override
            public void run() {
                if (isMating) {
                    MatingService matingService1 = new MatingService(MateActivity.this);


                    //如果15s仍未有相互匹配的实时用户，则调用另一个随机返回用户的借接口
                    if(spendSeconds>=12){

                        matingService1.requestNewMating(matchedSex, new MyCallBack() {
                            @Override
                            public void onSuccess(Object o) {
                                NewMatingUserBean newMatingUserBean= (NewMatingUserBean) o;

                                remainedTimes=Integer.parseInt(newMatingUserBean.getData().getUserMarries().get(0).getMarryDesp());
                                remainTimes.setText(remainedTimes+"");


                                parseMatchedUsers(newMatingUserBean.getData().getUserInfoList());


                            }

                            @Override
                            public void onFail(String mistakeInfo) {

                            }
                        });


                    }else{

                        matingService1.requestMating(matchedSex, pageNum, 3, new MyCallBack() {
                            @Override
                            public void onSuccess(Object o) {


                                MatchedUserBean matchedUserBean= (MatchedUserBean) o;

                                remainedTimes=Integer.parseInt(matchedUserBean.getData().getUserMarry().get(0).getMarryDesp());
                                remainTimes.setText(remainedTimes+"");
                                parseMatchedUsers(matchedUserBean.getData().getUserInfoList().getList());
                            }

                            @Override
                            public void onFail(String mistakeInfo) {

                            }
                        });
                        handler.postDelayed(this, 3000);
                    }


                }
            }
        };
        Glide.with(this)
                .load(headImgURl)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headImageView);



        adapter = new CommonAdapter<NewMatingUserBean.DataBean.UserInfoListBean>(this, R.layout.matched_user_item, userList) {
            @Override
            protected void convert(ViewHolder holder, final NewMatingUserBean.DataBean.UserInfoListBean userInfoBean, final int position) {




                ImageView headImage = holder.getView(R.id.head_img);
                final ImageView indicator = holder.getView(R.id.choose_indicator);
                ImageView sexBg=holder.getView(R.id.sex_bg);
                ImageView vipTag=holder.getView(R.id.vip_tag);
                if(userInfoBean.getSex().equals("男")){
                    sexBg.setSelected(true);
                }else{
                    sexBg.setSelected(false);
                }
                if(userInfoBean.getTrueName()!=null&&userInfoBean.getTrueName().equals("1")){
                    vipTag.setVisibility(View.VISIBLE);
                }else{
                    vipTag.setVisibility(View.GONE);
                }

                Glide.with(MateActivity.this).load(userInfoBean.getImgUrl()).bitmapTransform(new CropCircleTransformation(MateActivity.this)).into(headImage);

//                if(!choosenUserList.contains(userInfoBean)){
//                    indicator.setSelected(false);
//                }

                if(choosenItem==position){
                    indicator.setSelected(true);
                }else{
                    indicator.setSelected(false);
                }

                indicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (!indicator.isSelected()) {
//                            indicator.setSelected(true);
//                            choosenUserList.add(userInfoBean);
//                        } else {
//                            indicator.setSelected(false);
//                            choosenUserList.remove(userInfoBean);
//                        }
//                        if (choosenUserList.size() == 0) {
//                            startChat.setSelected(false);
//                            startChat.setClickable(false);
//                        } else {
//                            startChat.setSelected(true);
//                            startChat.setClickable(true);
//                        }
                        startChat.setSelected(true);
                        startChat.setClickable(true);
                        choosenItem=position;
                        choosenUser=userInfoBean;
                        adapter.notifyDataSetChanged();


                    }
                });
                headImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MateActivity.this,UserInfoActivty.class);
                        intent.putExtra(USERID,userInfoBean.getUserId());

                        startActivity(intent);

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));



    }


    //匹配成功后  发起推送ce118769b71f4a90b35960aca22b3778
    public  void pushMatchedInfo(String userID){

        pushService.pushMatchedInfo(userID  , new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }
}
