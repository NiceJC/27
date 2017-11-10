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
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
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
 *
 * 以下是选择本地图片或者视频 并上传七牛 最后反馈到服务器个人相册的流程
 */


public class PickService {
    private static final int GET_QIUNIUTOKEN = 132323;
    private static final int UPLOADREQUEST = 224444;
    private SimpleResponseListener<String> simpleResponseListener;

    private UploadManager uploadManager;
    private FunctionOptions optionsPic; //选择上传图片配置
    private FunctionOptions optionsPortrait; //选择头像配置
    private PictureConfig.OnSelectResultCallback resultCallback;
    private Activity  mActivity;
    private OnUploadFinish onUploadFinish;
    private List<LocalMedia> selectedMedias=new ArrayList<>();
    private Gson mGson;
    private int remainPicNum; //用于统计尚未上传并回报的图片数量
    public PickService() {

    }

    public PickService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();

    }

    //图片选择  传入一个回调接口
    public void pick(PictureConfig.OnSelectResultCallback callback){
        this.resultCallback=callback;
        initPhotoPickOptions();
        chooseImg();
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


    //上传照片的配置
    private void initPhotoPickOptions() {
        optionsPic = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_1_1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(9) // 可选择图片的数量
                .setMinSelectNum(1)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                .setVideoS(0)// 查询多少秒内的视频 单位:秒
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCropW(720) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(1280) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setCheckNumMode(true) //QQ选择风格
//                        .setCompressQuality() // 图片裁剪质量,默认无损
                .setImageSpanCount(3) // 每行个数
                .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .create();

    }
    private void initPortraitPickPotions(){
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_1_1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(false) //是否启用像素压缩
                .setEnableQualityCompress(false) //是否启质量压缩
                .setMaxSelectNum(1) // 可选择图片的数量
                .setMinSelectNum(1)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                .setVideoS(0)// 查询多少秒内的视频 单位:秒
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(true) // 是否打开剪切选项
                .setCircularCut(true)// 是否采用圆形裁剪
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCheckNumMode(true) //QQ选择风格
//                        .setCompressQuality() // 图片裁剪质量,默认无损
                .setImageSpanCount(3) // 每行个数

                .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(true)// 点击声音
                .create();
    }


    private void chooseImg() {

        PictureConfig.getInstance().init(optionsPic).openPhoto(mActivity, resultCallback);

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
