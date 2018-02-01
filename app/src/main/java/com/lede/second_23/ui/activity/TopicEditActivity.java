package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import com.lede.second_23.utils.SnackBarUtil;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.TOPICID;

/**
 * Created by ld on 18/1/24.
 */

public class TopicEditActivity extends BaseActivity {



    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.tittle)
    TextView tittle;



    private List<LocalMedia> seletedImage=new ArrayList<>();
    private List<String> imageKeys;
    private Activity context;

    private long topicID;
    private String imageUrl;
    private String topicName;

    private boolean imageChanged=false;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        ButterKnife.bind(this);
        context=this;

        topicID=getIntent().getLongExtra(TOPICID,0);
        imageUrl=getIntent().getStringExtra("imageUrl");
        topicName=getIntent().getStringExtra("topicName");


        initView();



    }

    private void initView() {
        tittle.setText("修改主题");
        Glide.with(context).load(imageUrl).into(add);
        text.setText(topicName);
    }

    @OnClick({R.id.back,R.id.finish,R.id.add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                if(text.getText().toString().equals("")){
                    Toast.makeText(context,"填写标题",Toast.LENGTH_SHORT).show();

                    return;
                }

                if(imageChanged){
                    upLoadImage();

                }else{
                    editTopic();
                }


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

                Glide.with(context).load(seletedImage.get(0).getPath()).into(add);
                imageChanged=true;
            }

            @Override
            public void onSelectSuccess(LocalMedia localMedia) {
            }
        });
    }
    public void upLoadImage(){
        snackbar= SnackBarUtil.getLongTimeInstance(text,"上传中  请稍后");
        snackbar.show();
        UploadService uploadService=new UploadService(context);
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
        if(!imageChanged){
            snackbar= SnackBarUtil.getLongTimeInstance(text,"上传中  请稍后");
            snackbar.show();
        }


        FindMoreService findMoreService=new FindMoreService(this);
        MyCallBack myCallBack=new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                snackbar.setText("修改成功");
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        snackbar.dismiss();
                        finish();
                    }
                },1000);
            }

            @Override
            public void onFail(String mistakeInfo) {
                snackbar.setText("修改失败");
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        snackbar.dismiss();

                    }
                },1000);
            }
        };
        if(imageChanged){
            findMoreService.editTopic(topicID, text.getText().toString(), imageKeys.get(0),myCallBack);
        }else{
            findMoreService.editTopic(topicID, text.getText().toString(), "",myCallBack);
        }




    }
}
