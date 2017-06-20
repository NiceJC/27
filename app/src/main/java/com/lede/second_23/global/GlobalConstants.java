package com.lede.second_23.global;

import android.content.Context;

import com.lede.second_23.utils.UiUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by ld on 2016/11/16.
 * 全局类
 */

public class GlobalConstants {

    //noHttp请求队列
    private static RequestQueue mRequestQueue = NoHttp.newRequestQueue();

    //获取noHttp请求队列对象
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = NoHttp.newRequestQueue();
        }
        return mRequestQueue;
    }

    //网络访问
//    public static final String URL = "https://api.lacoorent.com";
    public static final String URL = "http://101.37.85.56:8000";
//    public static final String URL = "http://192.168.0.103:8080";
        //access_token
    public static final String TOKEN = "ACCESS_TOKEN";
    //用户头像imgac
    public static final String HEAD_IMG = "HEAD_IMG";
    //用户的融云Token
    public static final String RONGIM_TOKEN="RONGIM_TOKEN";

    public static final String ISCONNECTED_RONGIM="ISCONNECTED_RONGIM";
    //当前所浏览动态的用户的userid
    public static final String CURRENT_USERID = "CURRENT_USERID";
    //当前所浏览动态的用户的forumid
    public static final String CURRENT_FORUMID = "CURRENT_FORUMID";
    //当前所浏览动态的用户的头像url
    public static final String CURRENT_USERIMG = "CURRENT_USERIMG";
    //用户昵称
    public static final String NAME = "NAME";
    //是否身份认证
    public static final String CERTIFICATION = "CERTIFICATION";
    //设置中筛选条件性别
    public static final String SET_SEX="SET_SEX";
    //设置中筛选条件最小年龄
    public static final String SET_MINAGE="SET_MINAGE";
    //设置中筛选条件最大年龄
    public static final String SET_MAXAGE="SET_MAXAGE";
    //设置中筛选的距离
    public static final String SET_DISTANCE="SET_DISTANCE";
    //经度
    public static final String LONGITUDE = "LONGITUDE";
    //纬度
    public static final String LATITUDE = "LATITUDE";
    //ACCESS_TOKEN是否过期
    public static final String TOKENUNUSEFULL = "TOKENUNUSEFULL";

    //是否刚刚发布过动态
    public static final String IS_ISSUE="IS_ISSUE";
    //是否刚刚修改过用户信息
    public static final String IS_EDITINFO="IS_EDITINFO";
    public static final String EXPIRES_IN="EXPIRES_IN";
    //webView
    public static final String WEBVIEW = "WEBVIEW";
    //是否首次打开app
    public static final String ISFRIST = "isFrist";
    //更新时的downloadManager id
    public static final String KEY = "key";

    public static final String USERID="USERID";

    //七牛云控件域名
    public static final String QINIU = "http://my-photo.lacoorent.com/";

    //获取设备id
    public static String getDeviceToken() {
        //获取设备token
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) UiUtils.getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceToken = tm.getDeviceId();
        return deviceToken;
    }

}
