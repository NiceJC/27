package com.lede.second_23.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.ui.activity.AllIssueTextActivity;
import com.lede.second_23.utils.BitmapUtils;
import com.lede.second_23.utils.CameraUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.R.attr.path;

/**
 * Created by ld on 17/7/20.
 */

public class ShortVideoFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener {

    private View view;
    private RelativeLayout bottomLayout;
    private int screenWidth;
    private int screenheight;
    private MediaRecorder mediaRecorder;
    private ImageView img_video_shutter;
    private ProgressBar progressBar;
    private int PROGRESS_MAX = 100;
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    //默认前置或者后置相机 0:后置 1:前置
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private File file;
    private String pathName;
    private int video_width;
    private int video_height;
    private CamcorderProfile profile;
    private File file2;
    //    private LoadingDialog dialog;
//    private FfmpegController fc;
    private int recorderRotation;
    private boolean isStop=false;
    private Context context;


    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_short_video_fragment, container, false);
        screenWidth = MyApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
        screenheight = MyApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
        initView();
        initData();
        onAfter();
        return view;
    }

    private void onAfter() {
    }

    private void initData() {

    }

    private void initView() {
        bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomLayout);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_video_shutter = (ImageView) view.findViewById(R.id.img_video_shutter);
        img_video_shutter.setOnClickListener(this);
        img_video_shutter.setOnTouchListener(new View.OnTouchListener() {
                                                 @Override
                                                 public boolean onTouch(View view, MotionEvent motionEvent) {
                                                     Log.i("TAB", "onTouch: " + motionEvent.getAction());
                                                     int action = motionEvent.getAction();
                                                     if (action == MotionEvent.ACTION_DOWN) {
                                                         Log.i("TAB", "onTouch:  ------->ACTION_DOWN");
                                                         start();
                                                         isStop=false;
//                                                         img_video_shutter.setEnabled(false);
                                                     }
                                                     if (action == MotionEvent.ACTION_MOVE) {
                                                         if (motionEvent.getY() < 0) {
                                                         }
                                                     }
                                                     if (action == MotionEvent.ACTION_UP) {
                                                         Log.i("TAB", "onTouch:  ------->ACTION_UP");
                                                         //停止录制
                                                         //录制完成
//                                                         img_video_shutter.setEnabled(true);
                                                         isStop=true;
                                                         if (mediaRecorder != null) {
                                                             mediaRecorder.stop();
                                                             mediaRecorder.release();
                                                             mediaRecorder = null;
                                                             releaseCamera();
                                                         }
                                                         Intent intent = new Intent(getActivity(), AllIssueTextActivity.class);
                                                         intent.putExtra("video_path", file.getPath());
                                                         intent.putExtra("imgOrVideoType",1);
                                                         startActivity(intent);
                                                     }
//                                                     switch (motionEvent.getAction()) {
//                                                         case MotionEvent.ACTION_DOWN:
//                                                             Log.i("TAB", "onTouch:  ------->ACTION_DOWN");
//                                                             start();
//                                                             img_video_shutter.setEnabled(false);
//                                                             break;
//                                                         case MotionEvent.ACTION_UP:
//                                                             Log.i("TAB", "onTouch:  ------->ACTION_UP");
//                                                             //录制完成
//                                                             img_video_shutter.setEnabled(true);
//
//                                                             if (mediaRecorder != null) {
//                                                                 mediaRecorder.stop();
//                                                                 mediaRecorder.release();
//                                                                 mediaRecorder = null;
//                                                                 releaseCamera();
//                                                             }
//                                                             break;
//                                                     }
                                                     return true;
                                                 }
                                             }

        );

        view.findViewById(R.id.img_camera_turn).

                setOnClickListener(this);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(PROGRESS_MAX);

    }

    protected void start() {
        try {
            pathName = System.currentTimeMillis() + "";
            //视频存储路径
            file = new File("/sdcard/27" + File.separator + pathName + ".mp4");

            //如果没有要创建
            BitmapUtils.makeDir(file);

            //初始化一个MediaRecorder
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            } else {
                mediaRecorder.reset();
            }

            mCamera.unlock();
            mediaRecorder.setCamera(mCamera);
            //设置视频输出的方向 很多设备在播放的时候需要设个参数 这算是一个文件属性
            mediaRecorder.setOrientationHint(recorderRotation);

            //视频源类型
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setAudioChannels(2);
            // 设置视频图像的录入源
            // 设置录入媒体的输出格式
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置音频的编码格式
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置视频的编码格式
//            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
            } /*else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
            } */ else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
            } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_HIGH)) {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_LOW)) {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
            }

            if (profile != null) {
                profile.audioCodec = MediaRecorder.AudioEncoder.AAC;
                profile.audioChannels = 1;
                profile.audioSampleRate = 16000;

                profile.videoCodec = MediaRecorder.VideoEncoder.H264;
                mediaRecorder.setProfile(profile);
            }

            //视频尺寸
            mediaRecorder.setVideoSize(video_width, video_height);

            //数值越大 视频质量越高
            mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);

            // 设置视频的采样率，每秒帧数
//            mediaRecorder.setVideoFrameRate(5);

            // 设置录制视频文件的输出路径
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.setMaxDuration(15000);

            // 设置捕获视频图像的预览界面
            mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // 发生错误，停止录制
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        Log.i("TAG", "onError: Error");
                    }
                }
            });

            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    //录制完成
                    Log.i("TAG", "onInfo: "+what+","+extra);
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

                }
            });

            // 准备、开始
            mediaRecorder.prepare();
            mediaRecorder.start();
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < PROGRESS_MAX; i++) {
                        try {
                            Thread.currentThread().sleep(150);
                            if (isStop) {
                                return;
                            }
                            Message message = new Message();
                            message.what = 1;
                            message.obj = i;
                            handler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Thread thread=new Thread(runnable);
            thread.start();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < PROGRESS_MAX; i++) {
//                        try {
//                            Thread.currentThread().sleep(150);
//
//                            Message message = new Message();
//                            message.what = 1;
//                            message.obj = i;
//                            handler.sendMessage(message);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setProgress((int) msg.obj);
//            if ((int) msg.obj == PROGRESS_MAX - 1) {
//                //录制完成
//                img_video_shutter.setEnabled(true);
//
//                if (mediaRecorder != null) {
//                    mediaRecorder.stop();
//                    mediaRecorder.release();
//                    mediaRecorder = null;
//                    releaseCamera();
//                }
//                Intent intent = new Intent(activity, PlayVideoActivity.class);
//                intent.putExtra(AppConstant.KEY.VIDEO_PATH, file.getPath());
//                startActivity(intent);
//                compressThread(file.getName());
//            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null && mCamera != null) {
                //开启预览
                startPreview(mCamera, mHolder);
            }
        }

        if (progressBar != null) {
            progressBar.setProgress(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //获取相机预览角度， 后面录制视频需要用
            recorderRotation = CameraUtil.getInstance().getRecorderRotation(mCameraId);
            CameraUtil.getInstance().setCameraDisplayOrientation(getActivity(), mCameraId, camera);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();

            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes != null && focusModes.size() > 0) {
                if (focusModes.contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    //设置自动对焦
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
            }

            List<Camera.Size> videoSiezes = null;
            if (parameters != null) {
                //获取相机所有支持尺寸
                videoSiezes = parameters.getSupportedVideoSizes();
                for (Camera.Size size : videoSiezes) {
                }
            }

            if (videoSiezes != null && videoSiezes.size() > 0) {
                //拿到一个预览宽度最小为720像素的预览值
                Camera.Size videoSize = CameraUtil.getInstance().getPropVideoSize(videoSiezes, 720);
                video_width = videoSize.width;
                video_height = videoSize.height;
//                video_width = videoSize.width;
//                video_height = videoSize.height;
                Log.i("TAG", "video_width===" + video_width);
                Log.i("TAG", "video_height===" + video_height);
            }

            //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
            Camera.Size previewSize = CameraUtil.getInstance().getPropPreviewSize(parameters.getSupportedPreviewSizes(), video_width);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            Camera.Size pictrueSize = CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), video_width);
            parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

            camera.setParameters(parameters);

            /**
             * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
             * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
             * previewSize.width才是surfaceView的高度
             * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
             */
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * video_width) / video_height, Gravity.CENTER);
//            //这里当然可以设置拍照位置 比如居中 我这里就置顶了
//            surfaceView.setLayoutParams(params);

//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, screenheight - screenWidth);
//            layoutParams.addRule(RelativeLayout.BELOW, surfaceView.getId());
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            bottomLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview(mCamera, mHolder);
        Log.i("TAG", "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i("TAG", "surfaceChanged: ");
        mCamera.stopPreview();
        startPreview(mCamera, surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
        Log.i("TAG", "surfaceDestroyed: ");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.img_video_shutter:
//                start();
//                img_video_shutter.setEnabled(false);
//                break;


            case R.id.img_camera_turn:
                switchCamera();
                break;
        }
    }
}
