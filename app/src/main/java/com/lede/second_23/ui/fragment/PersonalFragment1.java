package com.lede.second_23.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.QiNiuTokenBean;
import com.lede.second_23.bean.UserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.RefreshAndLoadMoreListener;
import com.lede.second_23.ui.activity.ConcernOrFansActivity;
import com.lede.second_23.ui.activity.ConversationListDynamicActivtiy;
import com.lede.second_23.ui.activity.MainActivity;
import com.lede.second_23.ui.activity.SetActivity;
import com.lede.second_23.ui.activity.TrimmerActivity;
import com.lede.second_23.ui.activity.UserInfoCardActivity;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.R.id.tv_converned_num;
import static com.lede.second_23.R.id.tv_fans_num;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/10/26.
 */

public class PersonalFragment1 extends Fragment implements RefreshAndLoadMoreListener {
    @Bind(R.id.post_my_photo)
    LinearLayout postMyPhoto;

    @Bind(R.id.iv_personfragment_back)
    ImageView back;
    @Bind(R.id.iv_personfragment_set)
    ImageView toSet;

    @Bind(R.id.personfragment_dongtai)
    ImageView dongtaiClick;
    @Bind(R.id.personfragment_album)
    ImageView albumClick;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.iv_person_fragment_item_sex)
    ImageView boyOrGirl;
    @Bind(R.id.tv_personfragment_username)
    TextView userName;
    @Bind(R.id.tv_personfragment_sign)
    TextView userSign;
    @Bind(R.id.ctiv_personfragment_userimg)
    ImageView userImg;
    @Bind(tv_converned_num)
    TextView followingsNum;
    @Bind(tv_fans_num)
    TextView fansNum;
    @Bind(R.id.ll_person_fragment_concerned)
    LinearLayout followingClick;
    @Bind(R.id.ll_person_fragment_fans)
    LinearLayout fansClick;
    @Bind(R.id.iv_personfragment_msg)
    ImageView msgClick;
    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @OnClick({R.id.ll_person_fragment_fans, R.id.ll_person_fragment_concerned, R.id.iv_personfragment_msg, R.id.iv_personfragment_set,
            R.id.post_my_photo, R.id.personfragment_dongtai, R.id.personfragment_album, R.id.iv_personfragment_back,
            R.id.ctiv_personfragment_userimg, R.id.tv_personfragment_username, R.id.tv_personfragment_sign
    })
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ctiv_personfragment_userimg:

            case R.id.tv_personfragment_username:

            case R.id.tv_personfragment_sign:
                intent = new Intent(getActivity(), UserInfoCardActivity.class);
                intent.putExtra(USERID,(String) SPUtils.get(getContext(), GlobalConstants.USERID, ""));

                startActivity(intent);
                break;

            case R.id.iv_personfragment_back:
                MainActivity.instance.vp_main_fg.setCurrentItem(1);
                break;
            case R.id.iv_personfragment_set:
                intent = new Intent(context, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_person_fragment_concerned:
                intent = new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("id", (String) SPUtils.get(context, USERID, ""));
                startActivity(intent);
                break;
            case R.id.ll_person_fragment_fans:
                intent = new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", (String) SPUtils.get(context, USERID, ""));
                startActivity(intent);
                break;
            case R.id.iv_personfragment_msg:
                startActivity(new Intent(context, ConversationListDynamicActivtiy.class));

                break;
            case R.id.personfragment_dongtai:
                if (currentPage != 0) {
                    viewPager.setCurrentItem(0, true);
                    dongtaiClick.setSelected(true);
                    albumClick.setSelected(false);
                    currentPage = 0;
                }
                break;
            case R.id.personfragment_album:
                if (currentPage != 1) {
                    viewPager.setCurrentItem(1, true);
                    albumClick.setSelected(true);
                    dongtaiClick.setSelected(false);
                    currentPage = 1;
                }
                break;
            case R.id.post_my_photo:


                initFunctionOptions();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.add);
                builder.setTitle("选择操作");
                //    指定下拉列表的显示数据
                final String[] cities = {"图片", "视频"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imgOrVideoType = which;
                        chooseImgOrVideo(imgOrVideoType);
                    }
                });
                builder.show();


                break;
            default:
                break;
        }
    }

    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private static final int REQUEST_USER_INFO = 555;
    private static final int GET_QIUNIUTOKEN = 2323;
    private static final int UPLOADREQUEST = 24444;


    private String userId;
    private UserInfoBean.DataBean.InfoBean userInfo;
    private int currentPage = 0;//当前页 初始默认为0

    private PersonalFragmentAlbum personalFragmentAlbum;
    private PersonalFragmentAllBlog personalFragmentAllBlog;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;

    private Request<String> userInfoRequest = null;

    private Context context;
    private UploadManager uploadManager;
    private FunctionOptions optionsPic; //选择图片
    private FunctionOptions optionsVideo; //选择视频
    private int imgOrVideoType = 0; //上传的文件类型  0图片  1视频
    private List<LocalMedia> selectMedia = new ArrayList<>(); //选择操作返回的媒体存放

    private boolean imgIsOk = false;
    private boolean videoIsOk = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);


        ButterKnife.bind(this, view);

        mGson = new Gson();
        context = getActivity();
        uploadManager = MyApplication.getUploadManager();
        initView();
        initEvent();
        doRequest();
        mRefreshLayout.isRefreshing();
        return view;
    }


    private void initView() {
        fragmentList = new ArrayList<>();

        if (personalFragmentAllBlog == null) {
            personalFragmentAllBlog = new PersonalFragmentAllBlog();
        }
        fragmentList.add(personalFragmentAllBlog);

        if (personalFragmentAlbum == null) {
            personalFragmentAlbum = new PersonalFragmentAlbum();
        }
        fragmentList.add(personalFragmentAlbum);
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    dongtaiClick.setSelected(true);
                    albumClick.setSelected(false);
                }
                if (position == 1) {
                    dongtaiClick.setSelected(false);
                    albumClick.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        currentPage = 0;
        viewPager.setCurrentItem(currentPage, false);
        dongtaiClick.setSelected(true);
    }


    public void initEvent() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (userInfoRequest != null) {
                    userInfoRequest.cancel();
                }
                doRequest();
                if (personalFragmentAllBlog.isResumed()) {
                    personalFragmentAllBlog.toRefresh();
                }
                if (personalFragmentAlbum.isResumed()) {
                    personalFragmentAlbum.toRefresh();
                }


            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (personalFragmentAllBlog.isResumed()) {
                    personalFragmentAllBlog.toLoadMore();
                }
                if (personalFragmentAlbum.isResumed()) {
                    personalFragmentAlbum.toLoadMore();
                }
            }
        });
    }


    private void doRequest() {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        mRefreshLayout.finishRefresh();
                        parseUserInfo(response.get());
                        break;
                    case GET_QIUNIUTOKEN:
                        parseQiNiuToken(response.get());

                        break;
                    case UPLOADREQUEST:
                        response.get();
                        Toast.makeText(getContext(),"上传成功",Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        mRefreshLayout.finishRefresh();
                        break;
                    case UPLOADREQUEST:
                        break;
                    default:
                        break;
                }
            }
        };
        userId = (String) SPUtils.get(getContext(), USERID, "");
        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/" + userId + "/heDetail", RequestMethod.POST);
        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);


    }


    private void parseUserInfo(String s) {
        UserInfoBean userInfoBean = mGson.fromJson(s, UserInfoBean.class);
        userInfo = userInfoBean.getData().getInfo();
        setUserInfo();
    }

    public void setUserInfo() {
        userName.setText(userInfo.getNickName());
        if (userInfo.getSex().equals("男")) {

            boyOrGirl.setSelected(false);
        } else {
            boyOrGirl.setSelected(true);
        }
        userSign.setText(userInfo.getNote());
        Glide.with(getContext())
                .load(userInfo.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(userImg);
        followingsNum.setText(userInfo.getFriendsCount() + "");
        fansNum.setText(userInfo.getFollowersCount() + "");
    }

    @Override
    public void isOver() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }


    /**
     * 以下是选择本地图片或者视频 并上传七牛 最后反馈到服务器个人相册的流程
     */


    //先进行媒体选择的配置
    private void initFunctionOptions() {
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
//                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
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
        optionsVideo = new FunctionOptions.Builder()
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
//                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setImmersive(false)// 是否改变状态栏字体颜色(黑色)
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setRecordVideoDefinition(0) // 视频清晰度
                .setRecordVideoSecond(15)
                .create();
    }

    private void chooseImgOrVideo(int type) {
        switch (type) {
            case 0:
                PictureConfig.getInstance().init(optionsPic).openPhoto((Activity) getContext(), resultCallback);

                break;
            case 1:
                PictureConfig.getInstance().init(optionsVideo).openPhoto((Activity) getContext(), resultCallback);

                break;
        }
    }

    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            // 多选回调
            if (imgOrVideoType == 0) { //pic
                selectMedia.clear();
                selectMedia.addAll(resultList);
                getQiniuToken();
            } else { //video
                Intent intent = new Intent(getActivity(), TrimmerActivity.class);
                intent.putExtra("path", resultList.get(0).getPath());
                startActivityForResult(intent, 666);

            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            //单选回调  不走
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 666:
                LocalMedia localMedia = new LocalMedia();
                Log.i("onActivityResult", "onActivityResult: " + data.getStringExtra("path"));
                localMedia.setPath(data.getStringExtra("path"));
                selectMedia.clear();
                selectMedia.add(localMedia);
                getQiniuToken();
                break;
        }

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
        if (imgOrVideoType == 0) {
            uploadPics(qiNiuTokenBean.getData().getUptoken(), selectMedia);
        } else {

            uploadVideo(qiNiuTokenBean.getData().getUptoken(), selectMedia);

        }

    }

    /**
     * 上传视频文件
     * 选择视频限制只能选择一个
     *
     * @param uptoken
     */
    private void uploadVideo(String uptoken, List<LocalMedia> localMediaList) {

        File imgFirst = new File(VideoUtils.bitmap2File(VideoUtils.getVideoThumb(localMediaList.get(0).getPath()), "cacher"));
        File video = new File(localMediaList.get(0).getPath());

        final int num = new Random().nextInt(100001);
        final long imgID = System.currentTimeMillis() * 100000 + num;
        final String imgFirstkey = (System.currentTimeMillis() * 100000 + num) + ".jpg";
        final String videoKey = (System.currentTimeMillis() * 100000 + num) + "." + FileUtils.getExtensionName(video.getName());

        uploadManager.put(imgFirst, imgFirstkey, uptoken,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            imgIsOk = true;
                            if (videoIsOk) {
                                uploadService(imgID, imgFirstkey,videoKey);
                            }
                        } else {
                            Log.i("qiniu", "VideoFirst_Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        Log.i("七牛", "VideoFirst_progress: " + key + ": " + percent);
                    }
                }, null));

        uploadManager.put(video, videoKey, uptoken,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            videoIsOk = true;
                            if (imgIsOk)
                                uploadService(imgID, imgFirstkey,videoKey);
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


    /**
     * 上传图片文件
     */
    private void uploadPics(String token, List<LocalMedia> localMediaList) {

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
                                Log.i("qiniu", "Success---->" + key);
                                uploadService(imgID,key,null);
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
    }

    private void uploadService(long imgID, String imgKey, String videoKey) {
        Request<String> uploadRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/creatUserPhoto", RequestMethod.POST);
        uploadRequest.add("access_token", (String) SPUtils.get(context, GlobalConstants.TOKEN, ""));
        uploadRequest.add("photoId",imgID);




        if (imgOrVideoType == 0) {
            uploadRequest.add("photoImg",imgKey);
        } else {
            uploadRequest.add("photoFirst",imgKey);
            uploadRequest.add("photoVideo",videoKey);
        }
        RequestServer.getInstance().request(UPLOADREQUEST, uploadRequest,simpleResponseListener);
    }

}
