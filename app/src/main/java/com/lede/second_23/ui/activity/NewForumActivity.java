package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonAllForumBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.ForumService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.SnackBarUtil;
import com.lede.second_23.utils.UiUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 18/1/18.
 */

public class NewForumActivity extends BaseActivity {


    @Bind(R.id.forum_layout)
    LinearLayout forumLayout;
    @Bind(R.id.forum_image)
    ImageView forumImage;
    @Bind(R.id.forum_text)
    TextView forumText;

    private ForumService forumService;
    private Activity context;
    private String userID;
    private PersonAllForumBean.DataBean.SimpleBean.ListBean forum;
    private MultiTransformation transformation;
    private Snackbar snackbar;
    public static NewForumActivity instance;
    private boolean isPosting=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_forum);
        ButterKnife.bind(this);
        context=this;
        instance=this;
        isPosting=getIntent().getBooleanExtra("posting",false);
        forumService=new ForumService(context);
        userID = (String) SPUtils.get(context, USERID, "");


        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(15) , 0, RoundedCornersTransformation.CornerType.TOP)
        );
        getNewForum();
        if(isPosting){
            showSnackBar();
        }

    }

    @OnClick({R.id.back,R.id.forum_layout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.forum_layout:
                Intent intent=new Intent(context, ForumDetailActivity.class);
                intent.putExtra("forumId", forum.getForumId());

                startActivity(intent);


                break;
            default:
                break;
        }

    }


    public void getNewForum(){
        forumService.requestMyForum(userID, 1, 1, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<PersonAllForumBean.DataBean.SimpleBean.ListBean> list = (List<PersonAllForumBean.DataBean.SimpleBean.ListBean>) o;

                if (list.size() != 0) {

                    forum=list.get(0);
                    setForum();

                }
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });
    }

    public void setForum(){
        if (!forum.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
            Glide.with(context).load(forum.getAllRecords().get(0).getUrl()).bitmapTransform(transformation).into(forumImage);

        } else {

            Glide.with(context).load(forum.getAllRecords().get(0).getUrlThree()).bitmapTransform(transformation).into(forumImage);

        }
        forumText.setText(forum.getForumText());
    }

    public void showSnackBar(){

        snackbar=SnackBarUtil.getLongTimeInstance(forumLayout,"正在发送");

        snackbar.show();

    }
    public void closeSnackBar(String s){
       if(snackbar!=null){
           snackbar.setText(s);

       }
      getNewForum();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               snackbar.dismiss();
           }
       },1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewForum();
    }
}
