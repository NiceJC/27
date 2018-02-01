package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PForum;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.service.PickService;
import com.lede.second_23.service.UploadService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.TOPICID;
import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 18/1/23.
 */

public class PostFootMarkActivity extends BaseActivity {


    @Bind(R.id.text)
    EditText text;
    @Bind(R.id.image)
    ImageView image;


    private List<LocalMedia> seletedImage=new ArrayList<>();
    private List<String> imageKeys;
    private Activity context;

    private Gson mGson;
    private long topicID;
    private long topicItemID;
    private String userID;
    private FindMoreService findMoreService;
    private boolean isImageOK=false;
    public static PostFootMarkActivity instance;
    private Snackbar snackbar;
    private String isHere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_footmark);


        context=this;
        instance=this;
        mGson=new Gson();
        userID= (String) SPUtils.get(context,USERID,"");
        Intent intent=getIntent();
        topicID=intent.getLongExtra(TOPICID,0);
        topicItemID=intent.getLongExtra(TOPICITEMID,0);
        isHere=intent.getStringExtra("isHere");
        findMoreService=new FindMoreService(context);



        ButterKnife.bind(this);
    }
    @OnClick({R.id.back,R.id.post, R.id.image})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.post:
                if(text.getText().toString().equals("")){
                    Toast.makeText(context,"说点什么吧",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isImageOK){
                    upLoadImage();
                }else{
                    postFootMark();
                }

                finish();
                break;
            case R.id.image:
                chooseImage();
                break;
            default:
                break;
        }
    }


    public void chooseImage(){
        PickService pickService = new PickService(this);
        pickService.pickPhoto(1,new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {

                seletedImage.addAll(list);
                isImageOK=true;

                Glide.with(context).load(seletedImage.get(0).getPath()).into(image);
            }

            @Override
            public void onSelectSuccess(LocalMedia localMedia) {
            }
        });
    }

    public void upLoadImage(){
        TopicItemDetailActivity.instance.showSnackBar();


        UploadService uploadService=new UploadService(context);
        uploadService.upload(seletedImage, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                imageKeys= (List<String>) o;
                postFootMark();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }
    public void postFootMark(){


        if(!isImageOK){
            TopicItemDetailActivity.instance.showSnackBar();

        }


        int num = new Random().nextInt(100001);
        long forumID = System.currentTimeMillis() * 100000 + num;

        PForum pForum;
        if(isImageOK){
            pForum=new PForum(userID,forumID,topicItemID,topicID,text.getText().toString(),imageKeys.get(0),isHere);

        }else{
            pForum=new PForum(userID,forumID,topicItemID,topicID,text.getText().toString(),isHere);

        }

        String str = mGson.toJson(pForum);

        findMoreService.createNewFootMark(str, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

                if(!TopicItemDetailActivity.instance.isDestroyed()){
                    TopicItemDetailActivity.instance.closeSnackBar("发布成功");

                }
            }

            @Override
            public void onFail(String mistakeInfo) {
                if(!TopicItemDetailActivity.instance.isDestroyed()){
                    TopicItemDetailActivity.instance.closeSnackBar("发布失败");

                }

            }
        });

    }



}
