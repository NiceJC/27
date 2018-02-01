package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.service.PickService;
import com.lede.second_23.service.UploadService;
import com.lede.second_23.ui.base.BaseActivity;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ld on 18/1/19.
 */

public class TopicCreateActivity extends BaseActivity {


    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.text)
    TextView text;



    private List<LocalMedia> seletedImage=new ArrayList<>();
    private List<String> imageKeys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back,R.id.finish,R.id.add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                if(text.getText().toString().equals("")){
                    Toast.makeText(TopicCreateActivity.this,"填写标题",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(seletedImage.size()==0){
                    Toast.makeText(TopicCreateActivity.this,"选择图片",Toast.LENGTH_SHORT).show();

                    return;
                }


                upLoadImage();

                break;
            case R.id.add:
                chooseImage();
                break;
            case R.id.text:
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

                Glide.with(TopicCreateActivity.this).load(seletedImage.get(0).getPath()).into(add);
            }

            @Override
            public void onSelectSuccess(LocalMedia localMedia) {
            }
        });
    }
    public void upLoadImage(){
        UploadService uploadService=new UploadService(TopicCreateActivity.this);
        uploadService.upload(seletedImage, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                imageKeys= (List<String>) o;
                editTopic();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }

    public void editTopic(){
        int num = new Random().nextInt(100001);
        long uuid = System.currentTimeMillis() * 100000 + num;
        FindMoreService findMoreService=new FindMoreService(this);
        findMoreService.createNewTopic(uuid, text.getText().toString(), imageKeys.get(0), new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(TopicCreateActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });



    }

}
