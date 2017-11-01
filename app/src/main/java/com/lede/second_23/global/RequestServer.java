package com.lede.second_23.global;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.SimpleResponseListener;

/**
 * Created by ld on 17/10/23.
 */

public class RequestServer {


        private static RequestServer instance;

        private RequestQueue queue;

        public static RequestServer getInstance() {
            if (instance == null)
                synchronized (RequestServer.class) {
                    if (instance == null)
                        instance = new RequestServer();
                }
            return instance;
        }


        private RequestServer() {
            queue = NoHttp.newRequestQueue(5);
        }

        public <T> void request(int what, Request<T> request, SimpleResponseListener<T> listener) {
            queue.add(what, request, listener);
        }

        // 完全退出app时，调用这个方法释放CPU。
        public void stop() {
            queue.stop();
        }

}
