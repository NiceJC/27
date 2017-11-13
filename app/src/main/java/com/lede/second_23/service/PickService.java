package com.lede.second_23.service;

import android.app.Activity;

import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.qiniu.android.storage.UploadManager;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

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
    private FunctionOptions optionsPhoto; //选择上传图片配置
    private FunctionOptions optionsPortrait; //选择头像配置
    private PictureConfig.OnSelectResultCallback resultCallback;
    private Activity  mActivity;
    private List<LocalMedia> selectedMedias=new ArrayList<>();
    private static final int TYPE_PHOTO=1;
    private static final int TYPE_PORTRAIT=2;


    public PickService() {

    }

    public PickService(Activity activity) {
        this.mActivity = activity;


    }

    //图片选择  传入一个回调接口
    public void pickPhoto(PictureConfig.OnSelectResultCallback callback){
        this.resultCallback=callback;
        chooseImg(TYPE_PHOTO);
    }

    //头像选择  传入一个回调接口
    public void pickPortrait(PictureConfig.OnSelectResultCallback callback){
        this.resultCallback=callback;
        chooseImg(TYPE_PORTRAIT);

    }


    private void chooseImg(int type) {

        if(type==TYPE_PHOTO){
            initPhotoPickOptions();
            PictureConfig.getInstance().init(optionsPhoto).openPhoto(mActivity, resultCallback);
        }else{
            initPortraitPickPotions();
            PictureConfig.getInstance().init(optionsPortrait).openPhoto(mActivity, resultCallback);
        }


    }

    //选取照片的配置
    private void initPhotoPickOptions() {
        optionsPhoto = new FunctionOptions.Builder()
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

    //选取头像的配置
    private void initPortraitPickPotions(){
        optionsPortrait = new FunctionOptions.Builder()
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







}
