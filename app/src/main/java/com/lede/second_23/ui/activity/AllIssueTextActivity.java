package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.AllForum;
import com.lede.second_23.bean.AllRecord;
import com.lede.second_23.bean.ForumSuccessBean;
import com.lede.second_23.bean.QiNiuTokenBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.FileUtils;
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
import com.yolanda.nohttp.NoHttp;
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

public class AllIssueTextActivity extends BaseActivity implements OnResponseListener<String> {

    @Bind(R.id.iv_all_issue_text_activity_back)
    ImageView ivAllIssueTextActivityBack;
    @Bind(R.id.iv_all_issue_text_activity_send)
    ImageView ivAllIssueTextActivitySend;
    @Bind(R.id.et_all_issue_text_activity_text)
    EditText etAllIssueTextActivityText;
    @Bind(R.id.ll_all_issue_text_activity_text_area)
    LinearLayout llAllIssueTextActivityTextArea;
    @Bind(R.id.rv_all_issue_text_activity_show)
    RecyclerView rvAllIssueTextActivityShow;

    @Bind(R.id.location_check)
    LinearLayout locationCheck;
    @Bind(R.id.location_text)
    TextView locationTextView;
    @Bind(R.id.location_delete)
    ImageView locationDelete;

    private static final int GET_QIUNIUTOKEN = 1000;
    private static final int PIC_UP_SERVICE = 2000;

    UploadManager uploadManager;
    private ArrayList<String> tokenList = new ArrayList<>();
    ArrayList<AllRecord> recordArrayList = new ArrayList<>();
    private ArrayList<Integer> successList = new ArrayList<>();
    private final int num = 0;
    private Random random;
    private long forumId;


    private Context mContext;
    private RequestQueue requestQueue;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private Gson mGson = new Gson();
    private CommonAdapter mAdapter;
    private int imgOrVideoType = 3;//3表示未选择图片或者视频 0图片 1视频

    private FunctionOptions options1; //选择图片
    private FunctionOptions options; //选择视频
    private String img_path;
    private GridLayoutManager gridLayoutManager;
    private boolean isCrop;  //表示是否裁剪 对应发布后显示状态为 true 图层  false 宫格
    private boolean isVideoFirstOK;
    private String qiniuvideoFirst;
    private boolean isVideoOK;
    private String qiniuVieoPatch;
    private Dialog loadingDialog2;
    private Intent intent;
    private String video_path;
    private boolean isFirstIn = true;

    private String locationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_issue_text);
        ButterKnife.bind(this);
        selectMedia.add(null);
        mContext = this;
        ButterKnife.bind(this);

        uploadManager = MyApplication.getUploadManager();
        random = new Random();
        intent = getIntent();

        imgOrVideoType = intent.getIntExtra("imgOrVideoType", 0);
        img_path = intent.getStringExtra("img_path");
        isCrop = intent.getBooleanExtra("isCrop", true);
        initFunctionOptions();
        if (img_path != null) {
            LocalMedia localMedia = new LocalMedia();
            File file = new File(img_path);
            localMedia.setPath("/storage/emulated/0/27/" + file.getName());
            file = null;
            localMedia.setChecked(true);
            localMedia.setNum(1);
            localMedia.setDuration(0);
            localMedia.setPosition(0);
            localMedia.setLastUpdateAt(0);
            localMedia.setType(0);
            localMedia.setCompressPath("123");
            localMedia.setCutPath("123");
            selectMedia.add(selectMedia.size() - 1, localMedia);
            Log.i("onCreate", "onCreate: " + mGson.toJson(localMedia));
            selectMedia.remove(selectMedia.get(selectMedia.size() - 1));
            PictureConfig.getInstance().init(options1).openPhoto((Activity) mContext, resultCallback);

        } else {
            selectMedia.remove(selectMedia.get(selectMedia.size() - 1));
            if (imgOrVideoType == 0) {
                PictureConfig.getInstance().init(options1).openPhoto((Activity) mContext, resultCallback);
            } else {
                video_path = intent.getStringExtra("video_path");
                if (video_path != null) {
                    Intent intent = new Intent(AllIssueTextActivity.this, TrimmerActivity.class);
                    intent.putExtra("path", video_path);
                    startActivityForResult(intent, 3333);
                } else {
                    PictureConfig.getInstance().init(options).openPhoto((Activity) mContext, resultCallback);
                }

            }

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
                .setEnableCrop(isCrop) // 是否打开剪切选项
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
                .setVideoS(60)// 查询多少秒内的视频 单位:秒
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

         locationText=intent.getStringExtra("location");
        if(locationText.equals("")){
            return;
        }
        locationTextView.setText(locationText);
        locationDelete.setVisibility(View.VISIBLE);
        locationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationTextView.setText("显示位置");
                locationDelete.setVisibility(View.GONE);
            }
        });

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
        rvAllIssueTextActivityShow.setLayoutManager(gridLayoutManager);
        rvAllIssueTextActivityShow.setAdapter(mAdapter);
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

    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {

            if (imgOrVideoType == 0) {
                // 多选回调
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
            } else {
                Intent intent = new Intent(AllIssueTextActivity.this, TrimmerActivity.class);
                intent.putExtra("path", resultList.get(0).getPath());
                startActivityForResult(intent, 3333);
            }

//            if (imgOrVideoType==1){
//                path=selectMedia.get(0).getPath();
//                CompressorUtils compressorUtils = new CompressorUtils(path, currentOutputVideoPath, IssueActivity.this);
//                compressorUtils.execCommand(new CompressListener() {
//                    @Override
//                    public void onExecSuccess(String message) {
//                        Log.i("TAG", "success " + message);
//                        bar.setVisibility(View.INVISIBLE);
//                        textAppend(getString(R.string.compress_succeed));
////                    back.setText(getFileSize(currentOutputVideoPath));
//                        //获取缩略图
//                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(currentOutputVideoPath, MediaStore.Video.Thumbnails.MINI_KIND);
////                    imag2.setImageBitmap(bitmap);
//                    }
//
//                    @Override
//                    public void onExecFail(String reason) {
//                        Log.i("TAG", "fail " + reason);
//                    }
//
//                    @Override
//                    public void onExecProgress(String message) {
//                        bar.setVisibility(View.VISIBLE);
//                        textAppend(getString(R.string.compress_progress, message));
//
//                        int i = getProgress(message);
//                        Log.e("进度", i + "");
//                        bar.setProgress(i);
//                    }
//                });
//            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            // 单选回调
//            selectMedia.clear();
//            selectMedia.add(media);
//            selectMedia.add(null);
//            if (selectMedia != null) {
////                adapter.setList(selectMedia);
//
//                mAdapter.notifyDataSetChanged();
//            }
            Intent intent = new Intent(AllIssueTextActivity.this, TrimmerActivity.class);
            intent.putExtra("path", media.getPath());
            startActivityForResult(intent, 3333);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 3333:
                LocalMedia localMedia = new LocalMedia();
                Log.i("onActivityResult", "onActivityResult: " + data.getStringExtra("path"));
                localMedia.setPath(data.getStringExtra("path"));
                selectMedia.clear();
                selectMedia.add(localMedia);
                selectMedia.add(null);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick({R.id.iv_all_issue_text_activity_back, R.id.iv_all_issue_text_activity_send,R.id.location_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_all_issue_text_activity_back:
                finish();
                break;
            case R.id.location_check:
                Intent intent=new Intent(this,LocationCheckinActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_all_issue_text_activity_send:
                if ((selectMedia.size() == 1 && selectMedia.get(0) == null)) {
                    Toast.makeText(mContext, "请选择图片视频", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Intent intent2=new Intent(AllIssueTextActivity.this,MainActivity.class);
                    startActivity(intent2);
                    if(MainActivity.instance!=null){
                        MainActivity.instance.showSnackBar("正在上传 请稍候");
                        MainActivity.instance.vp_main_fg.setCurrentItem(0);

                    }

                    forumId = System.currentTimeMillis() * 1000000 + random.nextInt(1000001);
                    if (imgOrVideoType == 0) {
                        for (int i = 0; i < selectMedia.size() - 1; i++) {
                            if (isCrop) {
                                getQiniuToken(i, new File(selectMedia.get(i).getCutPath()));
                            } else {
                                getQiniuToken(i, new File(selectMedia.get(i).getPath()));
                            }
                        }

                    } else if (imgOrVideoType == 1) {
                        ArrayList<File> videoList = new ArrayList<>();
                        videoList.add(new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(selectMedia.get(0).getPath()), "cacher")));
                        videoList.add(new File(selectMedia.get(0).getPath()));
                        for (int i = 0; i < videoList.size(); i++) {
                            getQiniuToken(i, videoList.get(i));
                        }
                    }

                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAB", "onResume: " + selectMedia.size() + "  imgOrVideoType=" + imgOrVideoType);
//        if (selectMedia.size()==0) {
//            imgOrVideoType=3;
//            selectMedia.add(null);
//            mAdapter.notifyDataSetChanged();
//        }
        if (selectMedia.size() == 1 && selectMedia.get(0) == null) {
            imgOrVideoType = 3;
        } else if (selectMedia.size() == 0) {
            if (!isFirstIn) {
                selectMedia.add(null);
                imgOrVideoType = 3;
                mAdapter.notifyDataSetChanged();
            }
            isFirstIn = false;

        }
//        else {
//            selectMedia.add(null);
//            mAdapter.notifyDataSetChanged();
//        }
        Log.i("TAB", "after  onResume: " + selectMedia.size() + "  imgOrVideoType=" + imgOrVideoType);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAB", "onRestart: ");
    }


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

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void parseQiNiuToken(String json, int index, File file) {
        QiNiuTokenBean qiNiuTokenBean = mGson.fromJson(json, QiNiuTokenBean.class);
        if (imgOrVideoType == 0) {
            uploadPic(qiNiuTokenBean.getData().getUptoken(), index, file);
        } else {
            uploadVideo(qiNiuTokenBean.getData().getUptoken(), index, file);
        }

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

        if (index == 0) { //pic
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
        } else { //video
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
                                    uploadService(forumId);
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
                            successList.add(i);
                            if (isCrop) {
                                recordArrayList.add(new AllRecord(imgOrVideoType, i, null, null, key, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), forumId, null, "1"));
                            } else {
                                recordArrayList.add(new AllRecord(imgOrVideoType, i, null, null, key, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), forumId, null, "0"));

                            }
                            if (successList.size() == selectMedia.size() - 1) {
                                uploadService(forumId);
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

    private void uploadService(long forumID) {

        Request<String> uploadRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/allForum/creatFroum", RequestMethod.POST);
        AllForum allForum = null;
        if (imgOrVideoType == 0) {
            allForum = new AllForum((String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""), forumId, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), etAllIssueTextActivityText.getText().toString().trim(), (String) SPUtils.get(mContext, GlobalConstants.LATITUDE, ""), (String) SPUtils.get(mContext, GlobalConstants.LONGITUDE, ""), recordArrayList);

        } else {
            ArrayList<AllRecord> recordArrayList = new ArrayList<>();
            recordArrayList.add(new AllRecord(1, 0, qiniuvideoFirst, qiniuVieoPatch, null, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), forumId, null, null));
            allForum = new AllForum((String) SPUtils.get(mContext, GlobalConstants.TOKEN, ""), forumId, (String) SPUtils.get(mContext, GlobalConstants.USERID, ""), etAllIssueTextActivityText.getText().toString().trim(), (String) SPUtils.get(mContext, GlobalConstants.LATITUDE, ""), (String) SPUtils.get(mContext, GlobalConstants.LONGITUDE, ""), recordArrayList);
        }
        String str = mGson.toJson(allForum);
        Log.i("json", "uploadService: " + str);
        uploadRequest.setDefineRequestBodyForJson(str);
        requestQueue.add(PIC_UP_SERVICE, uploadRequest, this);
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("up", "onSucceed: " + response.get());
        switch (what) {
            case PIC_UP_SERVICE:
                parseForum(response.get());
        }
    }

    private void parseForum(String json) {
        ForumSuccessBean foumSuccessBean = mGson.fromJson(json, ForumSuccessBean.class);
        if (foumSuccessBean.getResult() == 10000) {
            if(MainActivity.instance!=null){
                MainActivity.instance.showSnackBar("发布成功");
            }


            AllIssueActivity.instance.finish();
        } else {
            if(MainActivity.instance!=null) {
                MainActivity.instance.showSnackBar("发布失败");
            }
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }
}