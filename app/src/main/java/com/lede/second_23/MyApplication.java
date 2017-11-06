package com.lede.second_23;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.gson.Gson;
import com.lede.second_23.bean.RongIMBean;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.GetReplyActivity;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.utils.SPUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import iknow.android.utils.BaseUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

import static com.lede.second_23.global.GlobalConstants.USERID;
import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by ld on 17/2/21.
 */

public class MyApplication extends Application {
    public Context context;
    public Handler handler;
    public int mainThreadId;

    public static MyApplication instance;
    private RequestQueue requestQueue;

    private Gson mGson = new Gson();
    private NotificationManager myManager;
    private Bitmap LargeBitmap;
    private Notification myNotification;


    private static UploadManager uploadManager;
    private static Configuration config;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);
        initImageLoader(this);
        initFFmpegBinary(this);
//        RongIM.init(this);

        //获取全局Context对象
        context = getApplicationContext();

        //初始化主线程的Handler对象
        handler = new Handler();

        //获取主线程的线程id
        mainThreadId = android.os.Process.myTid();

        //Application对象
        instance = this;



        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);

        //初始化融云
        RongIM.init(this);
//        connect("3NlrnSrQOqWs7qJ2W3GpZ0oQlNFVrUnju7XRFfUZY+BQ1P5wuVlAwX7JZXgM5HPxbtl20/OGp4uKQ/rLdnmjiLSZRxdQfKpw1unonHQ1+XsCSOATdjY22H0taRtfdHbvdNRKdAhZQVA=");

        //处理消息
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());

        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Intent intent = new Intent(context, UserInfoActivty.class);
                intent.putExtra(USERID, userInfo.getUserId());
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });

        //初始化nohttp
        NoHttp.initialize(this, new NoHttp.Config()
                        //切换底层为okHttp
                        .setNetworkExecutor(new OkHttpNetworkExecutor())
                        // 设置全局连接超时时间，单位毫秒
                        .setConnectTimeout(15 * 1000 * 1000)
                        // 设置全局服务器响应超时时间，单位毫秒
                        .setReadTimeout(15 * 1000 * 1000)
                // 保存到数据库
//                .setCacheStore(
//                        new DBCacheStore(this).setEnable(true) // 如果不使用缓存，设置false禁用。
//                )
        );
        //日志工具
        Logger.setDebug(true);
        Logger.setTag("ledi");
        config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
//                .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
        requestQueue = GlobalConstants.getRequestQueue();
        if (SPUtils.contains(this, GlobalConstants.TOKEN)) {
            Log.i("TAG", "onCreate: 保存了用户token 获取融云token");
            getRongIMTokenService();
//            connect("ZgU68prjr5SVhfGceF9m8Ugxox7xGbbHONh06/q+EdEoGkqKdceqFvp2CVydaLqJZWYCtLjFupEEGvYyvOHKi1KPgEBu8qlVrUeFrPqy7S8XdpdCGD+0mr3IH8DLcEEc1QUKJ6gFn2Q=");
        }

    }






    public static void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 10);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(new LRULimitedMemoryCache(memoryCacheSize))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void initFFmpegBinary(Context context) {

        try {
            FFmpeg.getInstance(context).loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }
            });

        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }


    public void getRongIMTokenService() {
//        return rongIMTokenService;
        Request<String> getRongIMTokenRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/rongyun", RequestMethod.POST);
        getRongIMTokenRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        getRongIMTokenRequest.add("userId", (String) SPUtils.get(this, USERID, ""));
        requestQueue.add(100, getRongIMTokenRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
//                L.i("",response.get());
                Log.i("TAG", "getRongIMTokenService_onSucceed: " + response.get());
                parseRongIMTokenJson(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
//                L.d(response.get());
                Log.i("TAG", "getRongIMTokenService_onFailed: " + response.get());

                //没走自己服务测试

//                connect("QYocBi92F5PFBnQvWyixZkgxox7xGbbHONh06/q+EdEoGkqKdceqFpiarE/T+EWDMFPOgux4tZoEGvYyvOHKi1KPgEBu8qlVrUeFrPqy7S8XdpdCGD+0mr3IH8DLcEEc1QUKJ6gFn2Q=");

            }

            @Override
            public void onFinish(int what) {

            }
        });

    }




    private void parseRongIMTokenJson(String json) {
        Gson mGson = new Gson();
        RongIMBean rongIMBean = mGson.fromJson(json, RongIMBean.class);
        if (rongIMBean.getMsg().equals("用户没有登录")) {
            SPUtils.put(getApplicationContext(), GlobalConstants.TOKENUNUSEFULL, true);
        } else {

            SPUtils.put(getApplicationContext(), GlobalConstants.TOKENUNUSEFULL, false);
            if (rongIMBean.getResult() != 100205) {
                connect(rongIMBean.getData());
            }
        }
//        connect("QYocBi92F5PFBnQvWyixZkgxox7xGbbHONh06/q+EdEoGkqKdceqFpiarE/T+EWDMFPOgux4tZoEGvYyvOHKi1KPgEBu8qlVrUeFrPqy7S8XdpdCGD+0mr3IH8DLcEEc1QUKJ6gFn2Q=");

    }


    //        //友盟初始化
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//    }
//
//    //获取七牛上传管理类
    public static UploadManager getUploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager(config);
        }
        return uploadManager;
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token    从服务端获取的用户身份令牌（Token）。
    //     * @param callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    /**
     * @param token callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    public void connect(String token) {
        Log.i("TAG", "connect: 方法执行");
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            Log.i("TAG", "connect: 进入连接方法");
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.i("TAG", "onTokenIncorrect: token 错误");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    Log.i("TAG", "onSuccess: " + userid);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
                    SPUtils.put(context, GlobalConstants.ISCONNECTED_RONGIM, true);
                    //TODO
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String userId) {
//                            return new UserInfo("9e7a060b521049bb990dedc6055b7886","axe",Uri.parse("http://7xr1tb.com1.z0.glb.clouddn.com/20170413181819026917745.jpg"));
                            return getUserInfoFromServer(userId);
                        }
                    }, true);

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("TAG", "onError: " + errorCode);
                }
            });
        }
    }

    private UserInfo getUserInfoFromServer(String userId) {

        Request<String> userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/" + userId + "/heDetail", RequestMethod.POST);

        requestQueue.add(100, userInfoRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                parserJson(response.get());

            }


            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }

            private void parserJson(String s) {
                UserInfoBean userInfoBean = mGson.fromJson(s, UserInfoBean.class);
                UserInfoBean.DataBean.InfoBean infoBean = userInfoBean.getData().getInfo();
                if (userInfoBean.getResult()==10000) {
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(infoBean.getUserId(), infoBean.getNickName(), Uri.parse(infoBean.getImgUrl())));

                }
            }
        });
        return null;
    }


    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        /**
         * 收到消息的处理。
         *
         * @param message 收到的消息实体。
         * @param left    剩余未拉取消息数目。
         * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
         */
        @Override
        public boolean onReceived(Message message, int left) {
            //开发者根据自己需求自行处理
            if (message.getConversationType() == Conversation.ConversationType.SYSTEM) {
                showSystemNotification(message.getSenderUserId());
                Log.i("TAG", "收到新消息:" + message.getSenderUserId());
                return true;
            } else {
//                showMessageNotification(message);
                return false;
            }
//            Toast.makeText(context, "收到新消息:"+message.getSenderUserId(), Toast.LENGTH_SHORT).show();

//            return false;
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (RongIM.getInstance() == null) {
            SPUtils.put(context, GlobalConstants.ISCONNECTED_RONGIM, false);
            RongIM.getInstance().disconnect();
        }
    }

    /**
     * 收到点笑脸请求 显示通知 ios无法实现  废弃
     *
     * @param userid
     */
    public void showSystemNotification(String userid) {
        SPUtils.put(context,GlobalConstants.GETREPLY,(int)SPUtils.get(context,GlobalConstants.GETREPLY,0)+1);
        //创建大图标的Bitmap
        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        //1.从系统服务中获得通知管理器
        myManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //3.定义一个PendingIntent，点击Notification后启动一个Activity
        Intent intent = new Intent(context, GetReplyActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        //2.通过Notification.Builder来创建通知
        Notification.Builder myBuilder = new Notification.Builder(context);
        myBuilder.setContentTitle("27")
                .setContentText("通知")
                .setSubText("您收到一条新的评论")
//                .setTicker("您收到新的消息")
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(LargeBitmap)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
//                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                //android5.0加入了一种新的模式Notification的显示等级，共有三种：
                //VISIBILITY_PUBLIC  只有在没有锁屏时会显示通知
                //VISIBILITY_PRIVATE 任何情况都会显示通知
                //VISIBILITY_SECRET  在安全锁和没有锁屏的情况下显示通知
                .setContentIntent(pi);  //3.关联PendingIntent
        myNotification = myBuilder.build();
        //4.通过通知管理器来发起通知，ID区分通知
        myManager.notify(1, myNotification);
    }


//    public void showMessageNotification(Message message) {
//        //创建大图标的Bitmap
//        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        //1.从系统服务中获得通知管理器
//        myManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        //3.定义一个PendingIntent，点击Notification后启动一个Activity
//        Intent intent = new Intent(context, NotificationClickReceiver.class);
//        intent.putExtra("userid",message.getSenderUserId());
////        intent.putExtra("name",message.getContent().getUserInfo().getName());
//
//        PendingIntent pi = PendingIntent.getActivity(
//                context,
//                100,
//                intent,
//                PendingIntent.FLAG_CANCEL_CURRENT
//        );
////        RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE,message.getSenderUserId(),username);
//
//
//        //2.通过Notification.Builder来创建通知
//        Notification.Builder myBuilder = new Notification.Builder(context);
//        myBuilder.setContentTitle("23's")
////                .setContentText(message.getContent().getUserInfo().getName())
//                .setContentText("收到")
//                .setSubText("收到一条新消息")
////                .setTicker("您收到新的消息")
//                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(LargeBitmap)
//                //设置默认声音和震动
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
//                .setAutoCancel(true)//点击后取消
//                .setWhen(System.currentTimeMillis())//设置通知时间
//                .setPriority(Notification.PRIORITY_HIGH)//高优先级
////                        .setVisibility(Notification.VISIBILITY_PUBLIC)
//                //android5.0加入了一种新的模式Notification的显示等级，共有三种：
//                //VISIBILITY_PUBLIC  只有在没有锁屏时会显示通知
//                //VISIBILITY_PRIVATE 任何情况都会显示通知
//                //VISIBILITY_SECRET  在安全锁和没有锁屏的情况下显示通知
//                .setContentIntent(pi);  //3.关联PendingIntent
//        myNotification = myBuilder.build();
//        //4.通过通知管理器来发起通知，ID区分通知
//        myManager.notify(1, myNotification);
//    }
}
