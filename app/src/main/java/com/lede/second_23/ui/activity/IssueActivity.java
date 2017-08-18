package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.ForumImg;
import com.lede.second_23.bean.QiNiuTokenBean;
import com.lede.second_23.bean.UploadTextBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.FileUtils;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.VideoUtils;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnUploadListener;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssueActivity extends AppCompatActivity implements OnResponseListener<String> {

    @Bind(R.id.iv_issue_activity_back)
    ImageView iv_back;
    @Bind(R.id.et_issue_activity_text)
    EditText et_text;
    @Bind(R.id.rv_issue_activity_show)
    RecyclerView rv_show;
    @Bind(R.id.iv_issue_activity_send)
    ImageView iv_send;
    @Bind(R.id.progressBar)
    ProgressBar bar;
    private static final int GET_QIUNIUTOKEN = 1000;
    private static final int PIC_UP_SERVICE = 2000;
//    sex  agemin 0 agemax 99 juli  userid

    String path;//视频录制输出地址
    //视频压缩数据地址
    private String currentOutputVideoPath = "/sdcard/27/out.mp4";
    private Context mContext;
    private RequestQueue requestQueue;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private Gson mGson = new Gson();
    private CommonAdapter mAdapter;
    private int imgOrVideoType = 3;//3表示未选择图片或者视频 0图片 1视频

    private ArrayList<ForumImg> forumImgs=new ArrayList<>();
    private FunctionOptions options1;
    private FunctionOptions options;

    private static final int UPLOADVIDEO_REQUEST = 3000;
    private static final int UPLOADIMG_REQUEST = 2000;
    private static final int UPLOADTEXT_REQUEST = 1000;

    private ArrayList<String> successList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private Dialog loadingDialog2;
    private int num = 0;//计数
//    TextView back;
    Double videoLength=0.0;//视频时长
    private String img_path;
    private Random random;
    private Context context;
    private UploadManager uploadManager;
    private boolean isVideoFirstOK;
    private boolean isVideoOK;
    private String qiniuVieoPatch;
    private String qiniuvideoFirst;
    private long forumId;
    private boolean isFirstIn=true;
    private String video_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        mContext = IssueActivity.this;
        selectMedia.add(null);
        ButterKnife.bind(this);
        context=this;
        img_path=getIntent().getStringExtra("img_path");
        imgOrVideoType=getIntent().getIntExtra("imgOrVideoType",3);
        video_path = getIntent().getStringExtra("video_path");
        random = new Random();
//        forumId = System.currentTimeMillis() * 1000000 + random.nextInt(1000001);
        uploadManager = MyApplication.getUploadManager();
        initFunctionOptions();
        if (img_path!=null) {
            LocalMedia localMedia=new LocalMedia();
            File file=new File(img_path);
            localMedia.setPath("/storage/emulated/0/27/"+file.getName());
            file=null;
            localMedia.setChecked(true);
            localMedia.setNum(1);
            localMedia.setDuration(0);
            localMedia.setPosition(0);
            localMedia.setLastUpdateAt(0);
            localMedia.setType(0);
            localMedia.setCompressPath("123");
            localMedia.setCutPath("123");
            selectMedia.add(selectMedia.size()-1,localMedia);

            Log.i("onCreate", "onCreate: "+mGson.toJson(localMedia));
            selectMedia.remove(selectMedia.get(selectMedia.size() - 1));

            PictureConfig.getInstance().init(options1).openPhoto((Activity) mContext, resultCallback);
        }else if(video_path!=null){
            LocalMedia localMedia=new LocalMedia();
            File file=new File(video_path);
            Log.i("path", "onCreate: "+"/storage/emulated/0/27/"+file.getName());
            localMedia.setPath("/storage/emulated/0/27/"+file.getName());
            file=null;
            localMedia.setNum(1);
            localMedia.setType(2);
            selectMedia.add(selectMedia.size()-1,localMedia);

            Log.i("onCreate", "onCreate: "+mGson.toJson(localMedia));
            selectMedia.remove(selectMedia.get(selectMedia.size() - 1));

            PictureConfig.getInstance().init(options).openPhoto((Activity) mContext, resultCallback);
        }

        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();


        initRecyclerView();

    }

    private void initFunctionOptions() {
        options1 = new FunctionOptions.Builder()
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
                .setEnableCrop(true) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
//                        .setCheckedBoxDrawable() // 选择图片样式
//                        .setRecordVideoDefinition() // 视频清晰度
//                        .setRecordVideoSecond() // 视频秒数
//                        .setCustomQQ_theme()// 可自定义QQ数字风格，不传就默认是蓝色风格
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCropW(720) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(1280) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
//                        .setMaxB() // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb左右
//                        .setPreviewColor(Color.parseColor("")) //预览字体颜色
//                        .setCompleteColor() //已完成字体颜色
//                        .setPreviewTopBgColor()//预览图片标题背景色
//                        .setPreviewBottomBgColor() //预览底部背景色
//                        .setBottomBgColor() //图片列表底部背景色
//                        .setGrade() // 压缩档次 默认三档
                .setCheckNumMode(true) //QQ选择风格
//                        .setCompressQuality() // 图片裁剪质量,默认无损
                .setImageSpanCount(3) // 每行个数
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
//                        .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
//                        .setCompressW() // 压缩宽 如果值大于图片原始宽高无效
//                        .setCompressH() // 压缩高 如果值大于图片原始宽高无效
//                        .setThemeStyle() // 设置主题样式
//                        .setPicture_title_color() // 设置标题字体颜色
//                        .setPicture_right_color() // 设置标题右边字体颜色
//                        .setLeftBackDrawable() // 设置返回键图标
//                        .setStatusBar() // 设置状态栏颜色，默认是和标题栏一致
                .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .create();
        options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_VIDEO)
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(1) // 可选择图片的数量
                .setMinSelectNum(1)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选 FunctionConfig.MODE_SINGLE FunctionConfig.MODE_MULTIPLE
                .setVideoS(15)// 查询多少秒内的视频 单位:秒
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
                .setCheckNumMode(true) //QQ选择风格
//                        .setCompressQuality() // 图片裁剪质量,默认无损
                .setImageSpanCount(3) // 每行个数
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setRecordVideoDefinition(0) // 视频清晰度
                .setRecordVideoSecond(15)
                .create();
    }

    private void initRecyclerView() {
        mAdapter = new CommonAdapter<LocalMedia>(mContext, R.layout.item, selectMedia) {
            @Override
            protected void convert(ViewHolder holder, LocalMedia localMedia, final int position) {
                ImageView iv_show = holder.getView(R.id.iv_item);
                ImageView iv_delete = holder.getView(R.id.iv_delete);
//                Log.i("TAG", "convert: path"+localMedia.getPath()+" "+localMedia.getCompressPath()+" "+localMedia.getCutPath());
                if (position == selectMedia.size() - 1) {
                    Glide.with(mContext)
                            .load(R.mipmap.add)
                            .into(iv_show);
                    iv_delete.setVisibility(View.GONE);
                } else {
                    Glide.with(mContext)
                            .load(new File(localMedia.getPath()))
                            .into(iv_show);
                    iv_delete.setVisibility(View.VISIBLE);
                }
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectMedia.remove(position);
                        mAdapter.notifyDataSetChanged();
                        if (selectMedia.size() == 1 && selectMedia.get(0) == null) {
                            imgOrVideoType = 3;
                        }
                    }
                });
            }
        };
        gridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
//        new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rv_show.setLayoutManager(gridLayoutManager);
        rv_show.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == selectMedia.size() - 1) {
                    if (imgOrVideoType == 3) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.mipmap.add);
                        builder.setTitle("选择操作");
                        //    指定下拉列表的显示数据
                        final String[] cities = {"图片", "视频"};
                        //    设置一个下拉的列表选择项
                        builder.setItems(cities, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseImgOrVideo(which);
                            }
                        });
                        builder.show();
                    } else {
                        chooseImgOrVideo(imgOrVideoType);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void chooseImgOrVideo(int type) {
        switch (type) {
            case 0:
                imgOrVideoType = 0;

                selectMedia.remove(selectMedia.get(selectMedia.size() - 1));
                PictureConfig.getInstance().init(options1).openPhoto((Activity) mContext, resultCallback);
//                PictureConfig.getInstance().init(options1).startOpenCamera();
//                selectMedia.add(null);
                break;
            case 1:
                imgOrVideoType = 1;

                selectMedia.remove(selectMedia.get(selectMedia.size() - 1));
                PictureConfig.getInstance().init(options).openPhoto((Activity) mContext, resultCallback);
//                selectMedia.add(null);
                break;
        }
    }


    @OnClick({R.id.iv_issue_activity_back, R.id.iv_issue_activity_send})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_issue_activity_back:
                finish();
                break;
            case R.id.iv_issue_activity_send:
                if ((selectMedia.size() == 1 && selectMedia.get(0) == null)) {
                    Toast.makeText(mContext, "请选择图片视频", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    loadingDialog2 = createLoadingDialog(mContext, "正在上传请稍等...");
                    loadingDialog2.show();
                    if (imgOrVideoType == 0) {
//                        for (int i = 0; i < selectMedia.size() - 1; i++) {
//                                getQiniuToken(i, new File(selectMedia.get(i).getCutPath()));
//                        }
                        uploadTextService();
                    } else if (imgOrVideoType == 1) {
                        ArrayList<File> videoList = new ArrayList<>();
                        videoList.add(new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(selectMedia.get(0).getPath()), "cacher")));
                        videoList.add(new File(selectMedia.get(0).getPath()));
                        for (int i = 0; i < videoList.size(); i++) {
                            getQiniuToken(i, videoList.get(i));
                        }
                    }

                }

//                try {
//                    Log.i("TAB", "click:" + et_text.getText() + "123");
//                } catch (Exception e) {
//                    Log.i("TAB", "click: error" + e.getMessage());
//                }

                break;

        }
    }

    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            // 多选回调
            Log.i("path", "onSelectSuccess: "+mGson.toJson(resultList.get(0)));
            selectMedia.clear();
            selectMedia.addAll(resultList);
            selectMedia.add(null);
//            Log.i("resultList", "onSelectSuccess: " +mGson.toJson(resultList.get(1)));
//            Log.i("TAG", "onSelectSuccess: "+selectMedia.size());
            Log.i("callBack_result", selectMedia.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia != null) {
//                madapter.setList(selectMedia);
                Log.i("TAG", "onSelectSuccess: selectMedia != null");
                mAdapter.notifyDataSetChanged();
            }
//

        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            // 单选回调
            selectMedia.clear();
            selectMedia.add(media);
            selectMedia.add(null);
            if (selectMedia != null) {
//                adapter.setList(selectMedia);
                mAdapter.notifyDataSetChanged();
            }


        }
    };

    /**
     * 获取七牛上传token
     *
     * @param i
     * @param file
     */
    private void getQiniuToken(final int i, final File file) {
        Request<String> getQiniuRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/getToken", RequestMethod.GET);
        requestQueue.add(GET_QIUNIUTOKEN, getQiniuRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                parseQiNiuToken(response.get(), i, file);
            }
            private void parseQiNiuToken(String json, int index, File file) {
                QiNiuTokenBean qiNiuTokenBean = mGson.fromJson(json, QiNiuTokenBean.class);
                if (imgOrVideoType == 0) {
                    uploadPic(qiNiuTokenBean.getData().getUptoken(), index, file);
                } else {
                    uploadVideo(qiNiuTokenBean.getData().getUptoken(), index, file);
                }

            }
            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
    /**
     * 上传图片文件
     *
     * @param token
     * @param i
     * @param file
     */
    private void uploadPic(String token, final int i, File file) {

        final int num = random.nextInt(100001);
        String key = (System.currentTimeMillis() * 100000 + num) + "." + FileUtils.getExtensionName(file.getName());
        uploadManager.put(file, key, token,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            Log.i("qiniu", "Success---->" + key);
                            successList.add(key);
//                            if (isCrop) {
//                                recordArrayList.add(new AllRecord(imgOrVideoType, i, null, null, key, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), forumId, null,"1"));
//                            }else {
//                                recordArrayList.add(new AllRecord(imgOrVideoType, i, null, null, key, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), forumId, null,"0"));
//
//                            }
                            if (successList.size() == selectMedia.size() - 1) {
                                uploadTextService();
                                successList.clear();
                            }
                        } else {
                            Log.i("qiniu", "Fail----->" + key);
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", "name--->" + key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        Log.i("七牛", "progress: " + key + ": " + percent);
                    }
                }, null));
    }

    /**
     * 上传视频文件
     *
     * @param uptoken
     * @param index
     * @param file
     */
    private void uploadVideo(String uptoken, int index, File file) {
        final int num = random.nextInt(100001);


        if (index == 0) {
            String key = (System.currentTimeMillis() * 100000 + num) + ".jpg";
            uploadManager.put(file, key, uptoken,
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                Log.i("qiniu", "VideoFirst_Upload Success");
                                isVideoFirstOK = info.isOK();
                                qiniuvideoFirst = key;
                            } else {
                                Log.i("qiniu", "VideoFirst_Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                            Log.i("qiniu", "VideoFirst_" + key + ",\r\n " + info + ",\r\n " + res);
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                            Log.i("七牛", "VideoFirst_progress: " + key + ": " + percent);
                        }
                    }, null));
        } else {
            String key = (System.currentTimeMillis() * 100000 + num) + "." + FileUtils.getExtensionName(file.getName());
            uploadManager.put(file, key, uptoken,
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                Log.i("qiniu", "Video_Upload Success");
                                isVideoOK = info.isOK();
                                qiniuVieoPatch = key;
                                if (isVideoOK && isVideoFirstOK) {
                                    uploadTextService();
                                } else {
                                    Log.i("qiniu", "complete: 视频第一帧图片或者视频上传中出错");
                                }
                            } else {
                                Log.i("qiniu", "Video_Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                            Log.i("七牛", "Video_progress: " + key + ": " + percent);
                        }
                    }, null));
        }
    }

    /**
     * 上传信息到服务器
     */
    private void uploadService() {
        Request<String> uploadRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/newCreatForumByVideo", RequestMethod.POST);
//        AllForum allForum = null;
//        Forum forum=null;
//        if (imgOrVideoType == 0) {
//            for (int i = 0; i < successList.size(); i++) {
//                long imgId=System.currentTimeMillis() * 1000 + random.nextInt(1001);
//                forumImgs.add(new ForumImg(imgId,forumId,(String) SPUtils.get(context,GlobalConstants.USERID,""),successList.get(i)));
//
//            }
//            forum=new Forum((String) SPUtils.get(this, GlobalConstants.TOKEN,""),forumId,(String)SPUtils.get(context,GlobalConstants.USERID,""),1,et_text.getText().toString().trim(),forumImgs);
//        } else {
////            ArrayList<AllRecord> recordArrayList = new ArrayList<>();
////            recordArrayList.add(new AllRecord(1, 0, qiniuvideoFirst, qiniuVieoPatch, null, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), forumId, null,null));
////            allForum = new AllForum((String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""), forumId, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), et_text.getText().toString().trim(), "1321321", "4654231", recordArrayList);
//            long mediaId=System.currentTimeMillis() * 1000 + random.nextInt(1001);
//            forum=new Forum((String) SPUtils.get(this, GlobalConstants.TOKEN,""),forumId,(String)SPUtils.get(context,GlobalConstants.USERID,""),2,et_text.getText().toString().trim(),new ForumMedia(mediaId,forumId,qiniuVieoPatch,qiniuvideoFirst,(String) SPUtils.get(mContext, GlobalConstants.USERID, "")));
//        }
////        String str = mGson.toJson(allForum);
////        Log.i("json", "uploadService: " + str);
////        uploadRequest.setDefineRequestBodyForJson(str);
////        uploadRequest.add("access_token",(String) SPUtils.get(this, GlobalConstants.TOKEN,""));
//        String str = mGson.toJson(forum);
////        uploadRequest.add("Forum",str);
//        Log.i("IssueActivity", "uploadService: "+str);
//        uploadRequest.setDefineRequestBodyForJson(str);
        uploadRequest.add("userId",(String)SPUtils.get(context,GlobalConstants.USERID,""));
        uploadRequest.add("mediaName",qiniuVieoPatch);
        uploadRequest.add("forumId",forumId);
        uploadRequest.add("mediaPicturePath",qiniuvideoFirst);
        requestQueue.add(PIC_UP_SERVICE, uploadRequest, this);
    }

//

    /**
     * 上传文字请求
     */
    private void uploadTextService() {
//        loadingDialog2 = createLoadingDialog(mContext, "正在上传请稍等...");
//        loadingDialog2.show();
        Request<String> uploadTextRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/update", RequestMethod.POST);
        uploadTextRequest.add("access_token", (String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""));
        uploadTextRequest.add("text", et_text.getText().toString().trim());
        requestQueue.add(UPLOADTEXT_REQUEST, uploadTextRequest, this);
    }

    /**
     * 上传图片
     */
    public void uploadImgServce(int forumId) {
//        loadingDialog2 = ProgressDialogUtils.createLoadingDialog(mContext, "正在上传请稍等...");
//        loadingDialog2.show();
        for (int i = 0; i < selectMedia.size() - 1; i++) {
            Request<String> uploadRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/upload", RequestMethod.POST);
            uploadRequest.add("access_token", (String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""));
            uploadRequest.add("pic", new File(selectMedia.get(i).getCutPath()));
            uploadRequest.add("forumId", forumId);
            requestQueue.add(UPLOADIMG_REQUEST, uploadRequest, this);
        }

    }

    /**
     * 上传视频请求
     *
     * @param forumId
     */
    public void uploadVideoServce(int forumId) {

//        loadingDialog2.show();
        Request<String> uploadVideoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/upload_media", RequestMethod.POST);
        uploadVideoRequest.add("access_token", (String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""));
        uploadVideoRequest.add("pic", new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(selectMedia.get(0).getPath()), "cacher")));
//        uploadVideoRequest.add("media", new File(selectMedia.get(0).getPath()));
        File file=new File(selectMedia.get(0).getPath());
        FileBinary binary=new FileBinary(file,file.getName());
        binary.setUploadListener(88, new OnUploadListener() {
            @Override
            public void onStart(int what) {
                Log.i("up", "onStart: "+"开始上传");
            }

            @Override
            public void onCancel(int what) {

            }

            @Override
            public void onProgress(int what, int progress) {
                Log.i("up", "onProgress: 上传进度"+progress);
            }

            @Override
            public void onFinish(int what) {
                Log.i("up", "onFinish: 上传结束");
            }

            @Override
            public void onError(int what, Exception exception) {
                Log.i("up", "onError: "+exception.getMessage());
            }
        });
        uploadVideoRequest.add("media",binary);
        uploadVideoRequest.add("forumId", forumId);
//        uploadRequest.add("labels","");
        requestQueue.add(UPLOADVIDEO_REQUEST, uploadVideoRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case UPLOADVIDEO_REQUEST:
                        num++;
                        if (num == selectMedia.size()) {
                            if (loadingDialog2 != null) {
                                loadingDialog2.dismiss();
                                Toast.makeText(mContext, "视频上传成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                        SPUtils.put(mContext, GlobalConstants.IS_ISSUE, true);
                        finish();
                        break;
                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Log.i("TAG", "onFailed: 上传视频"+response.get());
            }

            @Override
            public void onFinish(int what) {

            }
        });
//
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAB", "onResume: "+selectMedia.size());
//        if (selectMedia.size()==0) {
//            imgOrVideoType=3;
//            selectMedia.add(null);
//            mAdapter.notifyDataSetChanged();
//        }
        if (selectMedia.size() == 1 && selectMedia.get(0) == null) {
            imgOrVideoType = 3;
        }else if(selectMedia.size()==0){
            if (!isFirstIn) {
                selectMedia.add(null);
                imgOrVideoType = 3;
                mAdapter.notifyDataSetChanged();
            }
            isFirstIn = false;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAB", "onRestart: ");
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        L.i(response.get());
        Log.i("IssueActivity", "onSucceed: "+response);
        switch (what) {
            case UPLOADTEXT_REQUEST:
                parseUpTextJson(response.get());
                break;
            case UPLOADIMG_REQUEST:
                num++;
                if (num == selectMedia.size()) {
                    if (loadingDialog2 != null) {
                        loadingDialog2.dismiss();
                        Toast.makeText(mContext, "图片上传成功", Toast.LENGTH_SHORT).show();

                    }
                }
                SPUtils.put(mContext, GlobalConstants.IS_ISSUE, true);
                finish();
                break;
            case UPLOADVIDEO_REQUEST:
                num++;
                if (num == selectMedia.size()) {
                    if (loadingDialog2 != null) {
                        loadingDialog2.dismiss();
                        Toast.makeText(mContext, "视频上传成功", Toast.LENGTH_SHORT).show();
                    }
                }
                SPUtils.put(mContext, GlobalConstants.IS_ISSUE, true);
                finish();
                break;
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        Log.i("TAG", "onFailed: "+response.get());
    }

    @Override
    public void onFinish(int what) {

    }

    private void parseUpTextJson(String json) {
        UploadTextBean uploadTextBean = mGson.fromJson(json, UploadTextBean.class);
        if (uploadTextBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
        } else {
            if (imgOrVideoType == 0) {
                uploadImgServce(uploadTextBean.getData().getForumId());
            } else {
                forumId=uploadTextBean.getData().getForumId();
                uploadService();
            }

        }


    }

    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog_2, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
//        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog


        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog2 != null) {
            loadingDialog2.dismiss();
        }
    }

    private String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    private float getlongFileSize(String path){
        File f = new File(path);
        if (!f.exists()) {
            return 0;
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f ;
        }
    }


}
