package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 *
 * Created by wjc on 17/10/18.
 */

public class BaseActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建请求队列, 默认并发3个请求, 传入数字改变并发数量: NoHttp.newRequestQueue(5);
        mRequestQueue = NoHttp.newRequestQueue();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRequestQueue.cancelAll();
        mRequestQueue.stop();
    }



    /**
     * 发起一个请求。
     *
     * @param what     what.
     * @param request  请求对象。
     * @param listener 结果监听。
     * @param <T>      要请求到的数据类型。
     */
    public <T> void request(int what, Request<T> request, OnResponseListener<T> listener) {
        mRequestQueue.add(what, request, listener);
    }


}
