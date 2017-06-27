package com.lede.second_23.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.UploadTextBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.ProgressDialogUtils;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.VideoUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by ld on 17/2/22.
 */

public class CameraFragment extends Fragment implements View.OnClickListener,
        SensorEventListener, SurfaceHolder.Callback, OnResponseListener<String> {


    private static final int UPLOADVIDEO_REQUEST = 3000;
    private static final int UPLOADIMG_REQUEST = 2000;
    private static final int UPLOADTEXT_REQUEST = 1000;

    private Gson mGson = new Gson();

    private Dialog loadingDialog2;


    private ImageView camera_fragment_choosephoto;
    private String selectedImg;
    private Context context;
    private View view;
    private boolean isphoto;//是拍照还是录像模式 true 拍照 false 录像
    private SurfaceView surfaceView; // 用于绘制缓冲图像的
    private Button luXiang_bt; // 开始录制的按钮
    private Button tingZhi_bt; // 停止录制的按钮
    private Button auto_focus; // 进行对焦
    private Button screenshot; // 截图
    private TextView time_tv; // 显示时间的文本框
    private MediaRecorder mRecorder;
    private boolean recording; // 记录是否正在录像,fasle为未录像, true 为正在录像
    private File videoFolder; // 存放视频的文件夹
    private File videFile; // 视频文件
    private Handler handler;
    private int time; // 时间
    private Camera myCamera; // 相机声明
    private SurfaceHolder holder; // 用来访问surfaceview的接口
    private SensorManager sManager; // 传感器管理者
    private Sensor sensor; // 传感器对象
    private int mX, mY, mZ; // x y z 坐标
    private Calendar calendar; // 日历
    private long lasttimestamp = 0; // 上一次用时的标志
    private RequestQueue requestQueue;
    private long startTime;
    private long endTime;
    private boolean isCAMERA_FACING_BACK = true;


    /**
     * 录制过程中,时间变化
     */
    private Runnable timeRun = new Runnable() {

        @Override
        public void run() {
            time++;
            time_tv.setText(time + "秒");
            if (time >= 15) {
                shot(2);
            }
            handler.postDelayed(timeRun, 1000);

        }
    };
    private ImageView camera_fragment_takephoto;
    private ImageView iv_camear_fragment_upload;
    private ImageView iv_camera_fragment_switchcamera;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getActivity();
        super.onCreate(savedInstanceState);
        loadingDialog2 = ProgressDialogUtils.createLoadingDialog(context, "正在上传请稍等...");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera_fragment_layout, container, false);
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        initView();
        initSensor();
        initCreateFile();

        camera_fragment_choosephoto = (ImageView) view.findViewById(R.id.iv_camera_fragment_choosephoto);
        camera_fragment_choosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectorPhoto();
            }
        });
        iv_camear_fragment_upload = (ImageView) view.findViewById(R.id.iv_camera_fragment_upload);
        iv_camear_fragment_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTextService();
//                uploadfielServce();
//                uploadImgServce(124);
            }
        });
        return view;

    }

    /**
     * 上传文字请求
     */
    private void uploadTextService() {
        Request<String> uploadTextRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/update", RequestMethod.POST);
        uploadTextRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        uploadTextRequest.add("text", "乐可快乐出行0123456789asdzxcvbhfgrty");
        requestQueue.add(UPLOADTEXT_REQUEST, uploadTextRequest, this);

    }

    /**
     * 上传图片
     */
    public void uploadImgServce(int forumId) {
        loadingDialog2 = ProgressDialogUtils.createLoadingDialog(context, "正在上传请稍等...");
        loadingDialog2.show();
        Request<String> uploadRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/upload", RequestMethod.POST);
        uploadRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
//        uploadRequest.add("pic", new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(selectedImg), "cacher")));
        uploadRequest.add("pic", new File(selectedImg));
//        uploadRequest.add("media", new File(selectedImg));
        uploadRequest.add("forumId", forumId);
//        uploadRequest.add("labels","");
        requestQueue.add(UPLOADIMG_REQUEST, uploadRequest, this);
//
    }

    /**
     * 上传视频请求
     *
     * @param forumId
     */
    public void uploadVideoServce(int forumId) {

        loadingDialog2.show();
        Request<String> uploadVideoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/forums/upload_media", RequestMethod.POST);
        uploadVideoRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        uploadVideoRequest.add("pic", new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(selectedImg), "cacher")));
//        uploadVideoRequest.add("pic", new File(selectedImg));
        uploadVideoRequest.add("media", new File(selectedImg));
        uploadVideoRequest.add("forumId", forumId);
//        uploadRequest.add("labels","");
        requestQueue.add(UPLOADVIDEO_REQUEST, uploadVideoRequest, this);
//
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private boolean isSelPhoto = false;

    //跳转相册选择图片
    public void selectorPhoto() {
//        RxGalleryFinal
//                .with(context)
//                .image()
//                .radio()
//                .imageLoader(ImageLoaderType.GLIDE)
//                .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
//                    @Override
//                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
//                        selectedImg = imageRadioResultEvent.getResult().getThumbnailBigPath();
//                        String originalPath = imageRadioResultEvent.getResult().getOriginalPath();
//                        if (TextUtils.isEmpty(selectedImg)) {
//                            selectedImg = originalPath;
//                        }
//                        isSelPhoto = true;
//                        Glide.with(context)
//                                .load(selectedImg)
//                                .skipMemoryCache(true)
//                                .into(camera_fragment_choosephoto);
//                    }
//                })
//                .openGallery();
    }

    /**
     * 对传感器进行初始化
     */
    private void initSensor() {
        sManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sManager == null) {
            // throw new IllegalArgumentException("SensorManager is null");
        }
        sManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 文件的创建
     */
    private void initCreateFile() {
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 设定存放视频的文件夹的路径
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + "27"
                    + File.separator;
            // 声明存放视频的文件夹的File对象
            videoFolder = new File(path);
            // 如果不存在此文件夹,则创建
            if (!videoFolder.exists()) {
                videoFolder.mkdirs();
            }
            // 设置surfaceView不管理的缓冲区
            surfaceView.getHolder().setType(
                    SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            // 设置surfaceView分辨率
            //surfaceView.getHolder().setFixedSize(1000, 500);
            luXiang_bt.setOnClickListener(this);
        } else
            Toast.makeText(context, "未找到sdCard!", Toast.LENGTH_LONG).show();
    }

    /**
     * 初始化工作
     */
    private void initView() {
        // 获取控件
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceview);
        luXiang_bt = (Button) view.findViewById(R.id.luXiang_bt);
        tingZhi_bt = (Button) view.findViewById(R.id.tingZhi_bt);
        time_tv = (TextView) view.findViewById(R.id.time);
        auto_focus = (Button) view.findViewById(R.id.auto_focus);
        screenshot = (Button) view.findViewById(R.id.screenshot);
        iv_camera_fragment_switchcamera = (ImageView) view.findViewById(R.id.iv_camera_fragment_switchcamera);
        iv_camera_fragment_switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //切换前后摄像头
//                int cameraCount = 0;
//                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
//
//                for (int i = 0; i < cameraCount; i++) {
//                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
//                    if (isCAMERA_FACING_BACK) {
//                        //现在是后置，变更为前置
//                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                            myCamera.stopPreview();//停掉原来摄像头的预览
//                            myCamera.release();//释放资源
//                            myCamera = null;//取消原来摄像头
//                            myCamera = Camera.open(i);//打开当前选中的摄像头
//                            myCamera.setDisplayOrientation(90);
//                            try {
//                                myCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            myCamera.startPreview();//开始预览
//                            isCAMERA_FACING_BACK = false;
//                            break;
//                        }
//                    } else {
//                        //现在是前置， 变更为后置
//                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                            myCamera.stopPreview();//停掉原来摄像头的预览
//                            myCamera.release();//释放资源
//                            myCamera = null;//取消原来摄像头
//                            myCamera = Camera.open(i);//打开当前选中的摄像头
//                            myCamera.setDisplayOrientation(90);
//                            try {
//                                myCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            myCamera.startPreview();//开始预览
//                            isCAMERA_FACING_BACK = true;
//                            break;
//                        }
//                    }
//                }
                if (isCAMERA_FACING_BACK) {
                    if (myCamera != null) {
                        myCamera.stopPreview();
                        myCamera.release();
                    }
                    myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    myCamera.setDisplayOrientation(90);
                    try {
                        myCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myCamera.startPreview();
                    isCAMERA_FACING_BACK = false;
                } else {
                    if (myCamera != null) {
                        myCamera.stopPreview();
                        myCamera.release();
                    }
                    myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    myCamera.setDisplayOrientation(90);
                    try {
                        myCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myCamera.startPreview();
                    isCAMERA_FACING_BACK = true;
                }
            }
        });
//        camera_fragment_choosephoto = (ImageView) view.findViewById(R.id.iv_camera_fragment_choosephoto);
//        camera_fragment_choosephoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectorPhoto();
//            }
//        });
//        iv_camear_fragment_upload = (ImageView) view.findViewById(R.id.iv_camera_fragment_upload);
//        iv_camear_fragment_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                uploadfielServce();
//            }
//        });
        camera_fragment_takephoto = (ImageView) view.findViewById(R.id.iv_camera_fragment_takephoto);
        camera_fragment_takephoto.setAlpha(0.5f);
        camera_fragment_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "按钮被单击了", Toast.LENGTH_SHORT).show();
                shot(0);
            }
        });
        camera_fragment_takephoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startTime = System.currentTimeMillis();
                shot(1);
                Toast.makeText(context, "按钮被长按", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        camera_fragment_takephoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {

                }
                if (action == MotionEvent.ACTION_MOVE) {
                    if (motionEvent.getY() < 0) {
//                        mAtRemove = true;
//                    else
//                        mAtRemove = false;
//                    changeTip();
//                    mProgressView.setRemove(mAtRemove);
//                    mMediaObject.setRemove(mAtRemove);
//                    mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
                        Toast.makeText(context, "上滑", Toast.LENGTH_SHORT).show();
                    }
                }
                if (action == MotionEvent.ACTION_UP) {
                    //停止录制
//                    if (mPressedStatus){
//                        mRecordTipView.setVisibility(View.GONE);
//                        mCameraSwitch.setVisibility(View.VISIBLE);
//                        stopRecord();
//                    }
                    shot(2);
                    Toast.makeText(context, "抬起手指", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        handler = new Handler();
        holder = surfaceView.getHolder();
        tingZhi_bt.setOnClickListener(this);
        auto_focus.setOnClickListener(this);
        screenshot.setOnClickListener(this);
        // 添加回调
        holder.addCallback(this);
    }


    /**
     * 将Camera和mediaRecoder释放
     */
//    @Override
//    protected void onDestroy() {
//        handler.removeCallbacks(timeRun);
//        if (mRecorder != null) {
//            mRecorder.release();
//        }
//        if (myCamera != null) {
//            myCamera.stopPreview();
//            myCamera.release();
//        }
//        super.onDestroy();
//    }


    /**
     * 将Camera和mediaRecoder释放
     */
    @Override
    public void onDestroyView() {
        handler.removeCallbacks(timeRun);
        if (mRecorder != null) {
            // 停止录制
            mRecorder.stop();
            // 释放资源
            mRecorder.release();
            mRecorder = null;
        }
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            // 停止录制
            mRecorder.stop();
            // 释放资源
            mRecorder.release();
            mRecorder = null;
        }
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
        }
        super.onDestroy();
    }

    /**
     * 拍摄方法
     *
     * @param type 0:拍照 1:录像 2:停止录像 3:对焦
     */
    private void shot(int type) {

        switch (type) {
            case 0:
                myCamera.autoFocus(new Camera.AutoFocusCallback() {

                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        Log.i("TAS", "onAutoFocus: " + success);
                        if (success || !isCAMERA_FACING_BACK) {
                            camera.takePicture(null, null, jpegCallBack);
                        }
                    }
                });
                break;
            case 1: // 录像点击事件
                if (!recording) {
                    try {
                        // 获取当前时间,作为视频文件的文件名
                        String nowTime = java.text.MessageFormat.format(
                                "{0,date,yyyyMMdd_HHmmss}",
                                new Object[]{new java.sql.Date(System
                                        .currentTimeMillis())});
                        // 声明视频文件对象
                        videFile = new File(videoFolder.getAbsoluteFile()
                                + File.separator + "video" + nowTime + ".mp4");
                        // 关闭预览并释放资源
                        myCamera.unlock();
//                        mRecorder.release();
                        mRecorder = new MediaRecorder();
                        mRecorder.setOrientationHint(90);
//                        mRecorder.setCamera();
                        mRecorder.setCamera(myCamera);
                        // 创建此视频文件
                        videFile.createNewFile();
                        mRecorder.setPreviewDisplay(surfaceView.getHolder()
                                .getSurface()); // 预览
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 视频源
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
                        mRecorder
                                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 输出格式为mp4
                    /* 引用android.util.DisplayMetrics 获取分辨率 */
                        // DisplayMetrics dm = new DisplayMetrics();
                        // getWindowManager().getDefaultDisplay().getMetrics(dm);
                        mRecorder.setVideoSize(800, 480); // 视频尺寸
                        mRecorder.setVideoEncodingBitRate(2 * 1280 * 720); //设置视频编码帧率
                        mRecorder.setVideoFrameRate(30); // 视频帧频率
                        mRecorder
                                .setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP); // 视频编码
                        mRecorder
                                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码
                        mRecorder.setMaxDuration(1800000); // 设置最大录制时间
                        mRecorder.setOutputFile(videFile.getAbsolutePath()); // 设置录制文件源
                        mRecorder.prepare(); // 准备录像
                        mRecorder.start(); // 开始录像
                        time_tv.setVisibility(View.VISIBLE); // 设置文本框可见
                        handler.post(timeRun); // 调用Runable
                        recording = true; // 改变录制状态为正在录制
                        setAutofocus();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        // 关闭预览并释放资源
                        if (myCamera != null) {
                            myCamera.stopPreview();
                            myCamera.release();
                            myCamera = null;
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        // 关闭预览并释放资源
                        if (myCamera != null) {
                            myCamera.stopPreview();
                            myCamera.release();
                            myCamera = null;
                        }
                    }
                } else
                    Toast.makeText(context, "视频正在录制中...",
                            Toast.LENGTH_LONG).show();
                break;
            case 2: // 停止录像事件
                if (recording) {
                    mRecorder.stop();
                    mRecorder.release();
                    handler.removeCallbacks(timeRun);
                    time_tv.setVisibility(View.GONE);
                    int videoTimeLength = time;
                    time = 0;
                    recording = false;
                    Toast.makeText(
                            context,
                            videFile.getAbsolutePath() + "  " + videoTimeLength
                                    + "秒", Toast.LENGTH_LONG).show();
                }
                // 开启相机
                if (myCamera == null) {
                    myCamera = Camera.open();
                    try {
                        myCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                myCamera.startPreview(); // 开启预览
                break;
            case 3:
                setAutofocus();
                break;

        }
    }

    /**
     * 控件点击事件的监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.luXiang_bt: // 录像点击事件
                if (!recording) {
                    try {
                        // 获取当前时间,作为视频文件的文件名
                        String nowTime = java.text.MessageFormat.format(
                                "{0,date,yyyyMMdd_HHmmss}",
                                new Object[]{new java.sql.Date(System
                                        .currentTimeMillis())});
                        // 声明视频文件对象
                        videFile = new File(videoFolder.getAbsoluteFile()
                                + File.separator + "video" + nowTime + ".mp4");
                        // 关闭预览并释放资源
                        myCamera.unlock();
                        mRecorder = new MediaRecorder();
                        mRecorder.setOrientationHint(90);
                        mRecorder.setCamera(myCamera);
                        // 创建此视频文件
                        videFile.createNewFile();
                        mRecorder.setPreviewDisplay(surfaceView.getHolder()
                                .getSurface()); // 预览
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 视频源
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
                        mRecorder
                                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 输出格式为mp4
                    /* 引用android.util.DisplayMetrics 获取分辨率 */
                        // DisplayMetrics dm = new DisplayMetrics();
                        // getWindowManager().getDefaultDisplay().getMetrics(dm);
                        mRecorder.setVideoSize(960, 544); // 视频尺寸
                        mRecorder.setVideoEncodingBitRate(2 * 1280 * 720); //设置视频编码帧率
                        mRecorder.setVideoFrameRate(30); // 视频帧频率
                        mRecorder
                                .setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP); // 视频编码
                        mRecorder
                                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码

                        mRecorder.setMaxDuration(1800000); // 设置最大录制时间
                        mRecorder.setOutputFile(videFile.getAbsolutePath()); // 设置录制文件源
                        mRecorder.prepare(); // 准备录像
                        mRecorder.start(); // 开始录像
                        time_tv.setVisibility(View.VISIBLE); // 设置文本框可见
                        handler.post(timeRun); // 调用Runable
                        recording = true; // 改变录制状态为正在录制
                        setAutofocus();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(context, "视频正在录制中...",
                            Toast.LENGTH_LONG).show();
                break;
            case R.id.tingZhi_bt: // 停止点击事件
                if (recording) {
                    mRecorder.stop();
                    mRecorder.release();
                    handler.removeCallbacks(timeRun);
                    time_tv.setVisibility(View.GONE);
                    int videoTimeLength = time;
                    time = 0;
                    recording = false;
                    Toast.makeText(
                            context,
                            videFile.getAbsolutePath() + "  " + videoTimeLength
                                    + "秒", Toast.LENGTH_LONG).show();
                }
                // 开启相机
                if (myCamera == null) {
                    myCamera = Camera.open();
                    try {
                        myCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                myCamera.startPreview(); // 开启预览
                break;
            case R.id.auto_focus:
                setAutofocus();
                break;
            case R.id.screenshot:
                myCamera.autoFocus(new Camera.AutoFocusCallback() {

                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            camera.takePicture(null, null, jpegCallBack);
                        }
                    }
                });
                break;
        }
    }

    /**
     * 设置自动对焦
     */
    private void setAutofocus() {
        if (myCamera != null) {
            myCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                    }
                }
            });
        }
    }

    /**
     * 传感器改变调用的方法
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            calendar = Calendar.getInstance();
            long stamp = calendar.getTimeInMillis();
            int px = Math.abs(mX - x);
            int py = Math.abs(mY - y);
            int pz = Math.abs(mZ - z);
            int maxValue = getMaxValue(px, py, pz);
            if (maxValue > 2 && (stamp - lasttimestamp) > 30) {
                lasttimestamp = stamp;
                setAutofocus();
            }
            mX = x;
            mY = y;
            mZ = z;
        }
    }

    /**
     * 获取最大改变的值
     */
    private int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }
        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * suraceView 创建执行的操作
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 开启相机
        if (myCamera == null) {
            myCamera = Camera.open();
            try {
                myCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * suraceView 状态改变执行的操作
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 开始预览
        myCamera.startPreview();
        Camera.Parameters parameters = myCamera.getParameters();// 获取mCamera的参数对象
        Camera.Size largestSize = getBestSupportedSize(parameters
                .getSupportedPreviewSizes());
        parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
        largestSize = getBestSupportedSize(parameters
                .getSupportedPictureSizes());// 设置捕捉图片尺寸
//        parameters.set("rotation", 90);
        parameters.setPictureSize(largestSize.width, largestSize.height);
        myCamera.setParameters(parameters);
        myCamera.setDisplayOrientation(90);
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        // 取能适用的最大的SIZE
        Camera.Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }

    /**
     * suraceView 销毁执行的操作
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 关闭预览并释放资源
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }

    }

    /**
     * 创建jpeg图片回调数据对象
     */
    private String filepath = "";
    private Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
                    oldBitmap.getWidth(), oldBitmap.getHeight(),
                    matrix, true);
            filepath = videoFolder.getAbsoluteFile() + File.separator + "27" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + ".jpg";
            File file = new File(filepath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(file));
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos);
                bos.flush();
                bos.close();
                camera.stopPreview();
                camera.startPreview();
                newBitmap.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPictureDegreeZero(filepath);
        }
    };
    /**
     * 将图片的旋转角度置为0  ，此方法可以解决某些机型拍照后图像，出现了旋转情况
     *
     * @Title: setPictureDegreeZero
     * @param path
     * @return void
     * @date 2012-12-10 上午10:54:46
     */
    private void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case UPLOADTEXT_REQUEST:
                L.i(response.get());
                parseUpTextJson(response.get());
                break;
            case UPLOADIMG_REQUEST:
                L.i(response.get());
                break;
            case UPLOADVIDEO_REQUEST:

                L.i(response.get());
                break;
        }
    }

    private void parseUpTextJson(String json) {
        UploadTextBean uploadTextBean = mGson.fromJson(json, UploadTextBean.class);
        uploadImgServce(uploadTextBean.getData().getForumId());
//        uploadVideoServce(uploadTextBean.getData().getForumId());
    }

    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

        loadingDialog2.cancel();
    }
}
