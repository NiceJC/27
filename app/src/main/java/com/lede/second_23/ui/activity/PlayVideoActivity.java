package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.lede.second_23.AppConstant;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.bean.QiNiuTokenBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.FileUtils;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.VideoUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频回复录制完成后预览
 *
 */
public class PlayVideoActivity extends Activity {
    private static final int GET_QIUNIUTOKEN = 1000;
    private static final int VIDEO_REPLY=2000;

    @Bind(R.id.vv_play_video)
    VideoView vvPlayVideo;
    @Bind(R.id.iv_play_video_cancel)
    ImageView ivPlayVideoCancel;
    @Bind(R.id.iv_play_video_send)
    ImageView ivPlayVideoSend;
    private String video_path;
    private UploadManager uploadManager;
    private boolean isVideoFirstOK = false;
    private boolean isVideoOK = false;
    private String qiniuvideoFirst;
    private String qiniuVieoPatch;
    private RequestQueue requestQueue;

    private Gson mGson;
    private long forumId;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        video_path = getIntent().getStringExtra(AppConstant.KEY.VIDEO_PATH);
        uploadManager = MyApplication.getUploadManager();
        requestQueue = GlobalConstants.getRequestQueue();
        forumId = getIntent().getLongExtra("forumId", 0);
        mGson = new Gson();
        initData();
    }

    public void initData() {
        vvPlayVideo.setVideoPath(video_path);
        vvPlayVideo.start();
        vvPlayVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);

            }
        });

        vvPlayVideo
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        vvPlayVideo.setVideoPath(video_path);
                        vvPlayVideo.start();

                    }
                });
    }

    @OnClick({R.id.iv_play_video_cancel, R.id.iv_play_video_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play_video_cancel:
                finish();
                break;
            case R.id.iv_play_video_send:
                loadingDialog = createLoadingDialog(this, "正在上传请稍后");
                loadingDialog.show();
                ArrayList<File> videoList = new ArrayList<>();
                videoList.add(new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(video_path), "cacher")));
                videoList.add(new File(video_path));
                for (int i = 0; i < videoList.size(); i++) {
                    getQiniuToken(i, videoList.get(i));
                }
                break;
        }
    }

    /**
     *
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog_2, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
//        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog


        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
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

    /**
     * 解析上传七牛时需要的token
     * @param json
     * @param index
     * @param file
     */
    private void parseQiNiuToken(String json, int index, File file) {
        QiNiuTokenBean qiNiuTokenBean = mGson.fromJson(json, QiNiuTokenBean.class);
        uploadVideo(qiNiuTokenBean.getData().getUptoken(), index, file);

    }




    /**
     * 上传视频文件
     *
     * @param uptoken
     * @param index
     * @param file
     */
    private void uploadVideo(String uptoken, int index, File file) {
        Random random=new Random();
        final int num = random.nextInt(1000001);


        if (index == 0) {
            String key = (System.currentTimeMillis() * 1000000 + num) + ".jpg";
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
                                    videoReply();
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
     * 视频回复请求
     */
    private void videoReply() {
        Random random = new Random();
        long videoId = System.currentTimeMillis() * 1000000 + random.nextInt(1000001);

        Request<String> videoReplyRequest=NoHttp.createStringRequest(GlobalConstants.URL+"/VideoReply/creatVideoReply",RequestMethod.POST);
        videoReplyRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        videoReplyRequest.add("forumId",forumId);
        videoReplyRequest.add("videoId",videoId);
        videoReplyRequest.add("videoPic",qiniuvideoFirst);
        videoReplyRequest.add("videoRecord",qiniuVieoPatch);

        requestQueue.add(VIDEO_REPLY, videoReplyRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.i("video", "onSucceed: " + response.get());
                parseJson(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void parseJson(String json) {
        MsgBean msg=mGson.fromJson(json,MsgBean.class);
        if (msg.getResult()==10000) {
            Toast.makeText(this, "视频互动成功", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            finish();
            ForumVideoReplyActivity.instance.finish();

        }
    }
}
