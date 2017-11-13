package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.bean.QiNiuTokenBean;
import com.lede.second_23.bean.UploadAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.OnUploadFinish;
import com.lede.second_23.utils.FileUtils;
import com.lede.second_23.utils.SPUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ld on 17/11/10.
 */

public class UploadService {
    private static final int GET_QIUNIUTOKEN = 132323;
    private static final int UPLOADREQUEST = 224444;
    private SimpleResponseListener<String> simpleResponseListener;

    private UploadManager uploadManager;

    private Activity mActivity;
    private OnUploadFinish onUploadFinish;
    private List<LocalMedia> selectedMedias=new ArrayList<>();
    private Gson mGson;
    private int remainPicNum; //用于统计尚未上传并回报的图片数量
    public UploadService() {

    }

    public UploadService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();

    }



    //图片上传 传入需要上传的资源 和一个回调接口
    public void upload(List<LocalMedia> selectedMedias, final OnUploadFinish onUploadFinish){

        this.selectedMedias.addAll(selectedMedias);
        this.onUploadFinish=onUploadFinish;
        uploadManager = MyApplication.getUploadManager();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {

                    case GET_QIUNIUTOKEN:
                        parseQiNiuToken(response.get());
                        break;
                    case UPLOADREQUEST:
                        parseUploadService(response.get());
                        if(remainPicNum==0){
                            onUploadFinish.success();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case GET_QIUNIUTOKEN:
                        onUploadFinish.failed();
                        break;
                    case UPLOADREQUEST:
                        onUploadFinish.failed();
                        break;

                    default:
                        break;

                }
            }
        };

        getQiniuToken();
    }




    /**
     * 获取七牛上传token
     */
    private void getQiniuToken() {
        Request<String> getQiniuRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/getToken", RequestMethod.GET);
        RequestServer.getInstance().request(GET_QIUNIUTOKEN, getQiniuRequest, simpleResponseListener);
    }

    private void parseQiNiuToken(String json) {
        QiNiuTokenBean qiNiuTokenBean = mGson.fromJson(json, QiNiuTokenBean.class);
        uploadPics(qiNiuTokenBean.getData().getUptoken(), selectedMedias);

    }
    private void parseUploadService(String json){
        UploadAlbumBean uploadAlbumBean=mGson.fromJson(json,UploadAlbumBean.class);
        if(uploadAlbumBean.getResult()==10000){
            remainPicNum=remainPicNum-1;
        }
    }

    /**
     * 上传图片文件
     */
    private void uploadPics(String token, List<LocalMedia> localMediaList) {

        remainPicNum=localMediaList.size();
        final int num = new Random().nextInt(100001);
        final long imgID = System.currentTimeMillis() * 100000 + num;
        for (LocalMedia pic:localMediaList
                ) {
            File img=new File(pic.getPath());
            String key = (System.currentTimeMillis() * 100000 + num) + "." + FileUtils.getExtensionName(img.getName());

            uploadManager.put(img, key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                uploadService(imgID,key);
                            } else {
                                onUploadFinish.failed();
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                        }
                    }, null));
        }
    }

    private void uploadService(long imgID, String imgKey) {
        Request<String> uploadRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/creatUserPhoto", RequestMethod.POST);
        uploadRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        uploadRequest.add("photoId",imgID);
        uploadRequest.add("photoImg",imgKey);
        RequestServer.getInstance().request(UPLOADREQUEST, uploadRequest,simpleResponseListener);
    }


}
