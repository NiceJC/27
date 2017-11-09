package com.lede.second_23.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.bean.QiNiuTokenBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.activity.ConcernActivity_2;
import com.lede.second_23.ui.activity.UserInfoActivty;
import com.lede.second_23.utils.FileUtils;
import com.lede.second_23.utils.SPUtils;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.IMAGE_URLS;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * 个人中心下的个人相册页面
 * Created by wjc on 17/10/27.
 */

public class PersonalFragmentAlbum extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.post_my_photo)
    LinearLayout postMyPhoto;
    @Bind(R.id.when_no_data)
     ImageView whenNoData;

    private Gson mGson;
    private CommonAdapter mAdapter;
    private SimpleResponseListener<String> simpleResponseListener;
    public MultiTransformation transformation;
    private static final int REQUEST_MY_ALBUM = 26336;
    private static final int DELETE_MY_ALBUM = 12211;
    private int currentPage = 1;
    private int pageSize = 100;
    private boolean isHasNextPage = false;
    private static final int GET_QIUNIUTOKEN = 2323;
    private static final int UPLOADREQUEST = 24444;

    private ArrayList<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> mDataList = new ArrayList<>();

    private boolean isRefresh = true; //刷新种类 刷新还是加载更多

    private Request<String> getMyAlbumRequest = null;
    private Request<String> deleteAlbum = null;


    private RequestManager requestManager;


    private int width;
    private int picWidth;
    private ArrayList<String> userphotoURLS = new ArrayList<>();
    private String userId;
    private boolean isMyPhoto;  //区分 当前展示的是他人的图片还是自己的
    private UploadManager uploadManager;
    private FunctionOptions optionsPic; //选择图片

    private int imgOrVideoType = 0; //上传的文件类型  0图片  1视频
    private List<LocalMedia> selectMedia = new ArrayList<>(); //选择操作返回的媒体存放

    private boolean imgIsOk = false;
    private boolean videoIsOk = false;


    PersonalFragment1 personalFragment1=null;
    UserInfoActivty userInfoActivty=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_album, container, false);

        uploadManager = MyApplication.getUploadManager();

        String userID = getActivity().getIntent().getStringExtra(USERID);


        ButterKnife.bind(this, view);
        if (userID != null && !userID.equals("")) {
            userId = userID;
            isMyPhoto = false;

            postMyPhoto.setVisibility(View.GONE);

            userInfoActivty= (UserInfoActivty) getActivity();
        } else {
            personalFragment1= (PersonalFragment1) getParentFragment();
            userId = (String) SPUtils.get(getContext(), USERID, "");
            isMyPhoto = true;
        }
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        picWidth=width/3;

        mGson = new Gson();
        requestManager = Glide.with(getContext());
        initView();
        initEvent();
        toRefresh();
        return view;
    }


    private void initView() {




        transformation = new MultiTransformation(
                new CenterCrop(getContext()),
                new RoundedCornersTransformation(getContext(), 20, 0, RoundedCornersTransformation.CornerType.ALL)
        );
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));


        mAdapter = new CommonAdapter<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean>(getActivity(), R.layout.item_person_fragment_show, mDataList) {

            @Override
            protected void convert(ViewHolder holder, PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean, int position) {
                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(picWidth,picWidth));

                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);

                ImageView showView_photos = holder.getView(R.id.iv_person_fragment_item_photos);
                showView_photos.setVisibility(View.GONE);
                if (userPhotoBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
                    requestManager.load(userPhotoBean.getUrlImg())
                            .into(showView_show);
                    showView_play.setVisibility(View.GONE);

                } else {
                    requestManager.load(userPhotoBean.getUrlFirst())
                            .into(showView_show);
                    showView_play.setVisibility(View.VISIBLE);
                }


            }
        };

        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), ConcernActivity_2.class);
                intent.putExtra(IMAGE_URLS,mDataList.get(position).getUrlImg());
                intent.putExtra(USERID,userId);
                getActivity().startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {
                if (isMyPhoto) {
                    DialogPlus dialogPlus=DialogPlus.newDialog(getActivity())
                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_delete_photo))
                            .setCancelable(true)
                            .setGravity(Gravity.CENTER)
                            .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {

                            switch (view.getId()){
                                case R.id.confirm:

                                    deletePhoto( Integer.parseInt(mDataList.get(position).getId()));
                                    dialog.dismiss();
                                    break;
                                case R.id.cancel:
                                    dialog.dismiss();
                                    break;
                                default:
                                    break;
                            }

                        }
                    })
                            .setExpanded(true).create();
                    ImageView imageView= (ImageView) dialogPlus.getHolderView().findViewById(R.id.image);
                    Glide.with(getContext())
                            .load(userphotoURLS.get(position))
                            .bitmapTransform(transformation)
                            .into(imageView);
                    dialogPlus.show();
                    return true;

                }


                return false;
            }
        });


    }


    public void initEvent() {
        postMyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFunctionOptions();
                chooseImgOrVideo();


            }
        });

    }



    public void toRefresh() {

        //重复请求之前，取消之前的请求
        if (getMyAlbumRequest != null) {
            getMyAlbumRequest.cancel();
        }
        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {

        if (getMyAlbumRequest != null) {
            getMyAlbumRequest.cancel();
        }
        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        } else {
            Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();
        }
    }


    private void deletePhoto(Integer id){

        deleteAlbum = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/deleteUserPhoto", RequestMethod.POST);

        deleteAlbum.add("access_token", (String) SPUtils.get(getContext(), GlobalConstants.TOKEN, ""));
        deleteAlbum.add("id",id);
        RequestServer.getInstance().request(DELETE_MY_ALBUM, deleteAlbum, simpleResponseListener);


    }

    private void doRequest(int pageNum) {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_MY_ALBUM:
                        if(isMyPhoto){
                            personalFragment1.isOver();
                        }else{
                            userInfoActivty.isOver();
                        }

                        parsePersonAlbum(response.get());
                        break;
                    case DELETE_MY_ALBUM:
                        Toast.makeText(getContext(),"图片已删除",Toast.LENGTH_SHORT).show();
                        doRequest(1);
                        break;
                    case GET_QIUNIUTOKEN:
                        parseQiNiuToken(response.get());
                        break;
                    case UPLOADREQUEST:
                        response.get();
                        Toast.makeText(getContext(),"上传成功",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_MY_ALBUM:

                        break;
                    default:
                        break;

                }
            }
        };

        getMyAlbumRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        getMyAlbumRequest.add("userId", userId);
        getMyAlbumRequest.add("pageNum", pageNum);
        getMyAlbumRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_MY_ALBUM, getMyAlbumRequest, simpleResponseListener);

    }


    /**
     * 解析个人相册
     *
     * @param json
     */
    private void parsePersonAlbum(String json) {
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(json, PersonalAlbumBean.class);
        if (personalAlbumBean.getResult() == 10000) {
            if (personalAlbumBean.getData().getSimple().getList().size() == 0) {
                Toast.makeText(getContext(), "无更多内容", Toast.LENGTH_SHORT).show();
                if(isRefresh){
                    mDataList.clear();
                    mAdapter.notifyDataSetChanged();
                    whenNoData.setVisibility(View.VISIBLE);
                }else{
                    whenNoData.setVisibility(View.GONE);
                }

            } else {
                whenNoData.setVisibility(View.GONE);

                isHasNextPage = personalAlbumBean.getData().getSimple().isHasNextPage();
                if (isRefresh) {
                    currentPage = 1;
                    mDataList.clear();
                } else {
                    currentPage++;
                }
                mDataList.addAll(personalAlbumBean.getData().getSimple().getList());

                userphotoURLS.clear();
                for (PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean : mDataList
                        ) {

                    String url;
                    if (userPhotoBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
                        url = userPhotoBean.getUrlImg();
                    } else {
                        url = userPhotoBean.getUrlFirst();

                    }
                    userphotoURLS.add(url);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
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

    private void chooseImgOrVideo() {

                PictureConfig.getInstance().init(optionsPic).openPhoto((Activity) getContext(), resultCallback);


        }


    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            // 多选回调
                selectMedia.clear();
                selectMedia.addAll(resultList);
                getQiniuToken();
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
        uploadPics(qiNiuTokenBean.getData().getUptoken(), selectMedia);



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
        uploadRequest.add("access_token", (String) SPUtils.get(getContext(), GlobalConstants.TOKEN, ""));
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