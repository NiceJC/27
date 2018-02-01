package com.lede.second_23.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.OnlyUserBean;
import com.lede.second_23.bean.PNotes;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.bean.TopicsBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.AlbumService;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.service.PickService;
import com.lede.second_23.service.UploadService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.SnackBarUtil;
import com.lede.second_23.utils.UiUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.luck.picture.lib.model.PictureConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yalantis.ucrop.entity.LocalMedia;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 *
 * 版块创建
 * Created by ld on 18/1/19.
 */

public class TopicItemCreateActivity extends BaseActivity {



    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView_topic)
    RecyclerView recyclerViewTopic;
    @Bind(R.id.topic_layout)
    LinearLayout topicLayout;

    @Bind(R.id.user_name)
    TextView businessName;
    @Bind(R.id.business_image)
    ImageView businessImage;

    @Bind(R.id.topic_name)
    TextView topicName;
    @Bind(R.id.introduction)
    TextView introduction;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.tag)
    TextView tag;




    private Gson mGson;

    private Activity context;
    private MultiItemTypeAdapter mAdapter;


    private List<LocalMedia> seletedImage=new ArrayList<>();
    private List<String> imageKeys=new ArrayList<>();

    private ArrayList<Object> objectList=new ArrayList<>();
    private int mPicSize =0;

    private PersonalAlbumBean.DataBean.UserInfo userInfoBean;
    private int itemWidth;
    private RequestManager requestManager;


    private AlbumService albumService;
    private Snackbar snackbar;
    private PictureConfig.OnSelectResultCallback portraitPickCallback;

    private String currentBusinessName="";
    private String currentTopicName;
    private String currentIntro;
    private String currentCity;
    private String currentTag;
    private String userID;
    private long currentTopicID;
    private boolean isInfoCardShown = false; //默认隐藏用户的详细资料卡
    private ObjectAnimator animator;

    private CommonAdapter topicsAdapter;


    private List<TopicsBean.DataBean.SimpleBean.ListBean> topicsList=new ArrayList<>();


    private FindMoreService findMoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic_item);

        context = this;
        ButterKnife.bind(context);
        requestManager = Glide.with(context);

        mGson=new Gson();

        itemWidth = (UiUtils.getScreenWidth() / 3);

        userID = (String) SPUtils.get(context, USERID, "");

        findMoreService=new FindMoreService(context);

        initView();

        initEvent();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);


    }

    @OnClick({R.id.back,R.id.save, R.id.user_name_layout, R.id.topic_name_layout, R.id.introduction_layout,

            R.id.city_layout, R.id.tag_layout
    })
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                createTopicItem();

                break;
            case R.id.user_name_layout:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("body", businessName.getText().toString().trim());
                startActivityForResult(intent, 4);
                break;
            case R.id.topic_name_layout:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("body", topicName.getText().toString().trim());
                startActivityForResult(intent, 1);
                break;
            case R.id.introduction_layout:
                intent = new Intent(this, NicknameOrHobbyOrSignActivity.class);
                intent.putExtra("type", 7);
                intent.putExtra("body", introduction.getText().toString().trim());
                startActivityForResult(intent, 7);
                break;

            case R.id.city_layout:
                showCityDialog();
                break;

            case R.id.tag_layout:

                bottom_show();
                break;
            default:
                break;
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        mAdapter=new MultiItemTypeAdapter(this,objectList);
        mAdapter.addItemViewDelegate(new UserphotoItemDelegate());
        mAdapter.addItemViewDelegate(new EmptyItemDelegate());


        mRecyclerView.setAdapter(mAdapter);


        animator = ObjectAnimator.ofFloat(topicLayout, "translationY", 0, UiUtils.dip2px(500));
        animator.setDuration(0);
        animator.start();
        isInfoCardShown = false;


        topicsAdapter=new CommonAdapter<TopicsBean.DataBean.SimpleBean.ListBean>(context,R.layout.item_topic_list,topicsList){
            @Override
            protected void convert(ViewHolder holder, TopicsBean.DataBean.SimpleBean.ListBean listBean, int position) {

                TextView textView=holder.getView(R.id.text);
                textView.setText(listBean.getPhotoName());

            }
        };
        topicsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                bottom_show();
                currentTag=topicsList.get(position).getPhotoName();
                currentTopicID=topicsList.get(position).getUuid();
                setInfo();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewTopic.setAdapter(topicsAdapter);


        for (int i=0;i<(6-seletedImage.size());i++){
            objectList.add(new EmptyItem());
        }
        mAdapter.notifyDataSetChanged();

    }

    private void bottom_show() {
        animator = null;
        if (isInfoCardShown) {
            //藏起
            animator = ObjectAnimator.ofFloat(topicLayout, "translationY", 0, topicLayout.getHeight() + UiUtils.dip2px(5));
            animator.setDuration(500);
            animator.start();
            isInfoCardShown = false;
        } else {
            //展开
            animator = ObjectAnimator.ofFloat(topicLayout, "translationY", topicLayout.getHeight() + UiUtils.dip2px(5), 0);
            animator.setDuration(500);
            animator.start();
            isInfoCardShown = true;
        }
    }



    public class UserphotoItemDelegate implements ItemViewDelegate<Object>{
        @Override
        public int getItemViewLayoutId() {
            return R.layout.edit_info_album_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof LocalMedia;
        }

        @Override
        public void convert(ViewHolder holder, Object o, final int position) {
            holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemWidth));
            LocalMedia localMedia= (LocalMedia) o;
            ImageView image=holder.getView(R.id.image);
            ImageView delete=holder.getView(R.id.delete_image);
            Glide.with(context).load(localMedia.getPath()).into(image);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seletedImage.remove(position);

                    objectList.remove(position);
                    objectList.add(new EmptyItem());
                    mAdapter.notifyDataSetChanged();




                }
            });

        }
    }
    public class EmptyItem {

    }
    //凑齐6个item，不够的用这个item凑
    public class EmptyItemDelegate implements  ItemViewDelegate<Object>{

        @Override
        public int getItemViewLayoutId() {
            return R.layout.edit_info_empty_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof EmptyItem;
        }

        @Override
        public void convert(ViewHolder holder, Object o, int position) {
            holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemWidth));

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点击上传相册
                    chooseImage();
                }
            });

        }
    }


    private void initEvent() {

        FindMoreService findMoreService2=new FindMoreService(context);
        findMoreService2.requestAllTopics(1, 100, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<TopicsBean.DataBean.SimpleBean.ListBean> list= (List<TopicsBean.DataBean.SimpleBean.ListBean>) o;

                topicsList.clear();
                topicsList.addAll(list);
                topicsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });



    }




    private void setInfo() {

        businessName.setText(currentBusinessName);
        topicName.setText(currentTopicName);
        introduction.setText(currentIntro);
        city.setText(currentCity);
        tag.setText(currentTag);

    }

    private void createTopicItem(){

        if (topicName.getText().toString().trim().equals("") || topicName.getText().toString().trim().equals("签名")) {
            Toast.makeText(context, "请输入版块名称", Toast.LENGTH_SHORT).show();
            return;
        }  else if (city.getText().toString().trim().equals("") || city.getText().toString().trim().equals("爱好")) {
            Toast.makeText(context, "请输入城市", Toast.LENGTH_SHORT).show();
            return;
        } else if (tag.getText().toString().trim().equals("") || tag.getText().toString().trim().equals("年龄")) {
            Toast.makeText(context, "请选择对应主题", Toast.LENGTH_SHORT).show();
            return;
        }  else if (seletedImage.size()==0) {
            Toast.makeText(context, "请至少上传1张图片", Toast.LENGTH_SHORT).show();
            return;
        }

        upLoadImage();



    }

    public void checkBusinessName(final String mkName){
        UserInfoService userInfoService=new UserInfoService(context);
        userInfoService.requestOnlyUserByName(mkName, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                OnlyUserBean.DataBean.UserBean user= (OnlyUserBean.DataBean.UserBean) o;
                businessImage.setVisibility(View.VISIBLE);
                currentBusinessName =user.getNickName();
                businessName.setText(currentBusinessName);

                requestManager.load(user.getImgUrl()).into(businessImage);



            }

            @Override
            public void onFail(String mistakeInfo) {
                businessImage.setVisibility(View.GONE);

                Toast.makeText(context,"商家名称并不准确",Toast.LENGTH_SHORT).show();

                currentBusinessName="";
                businessName.setText(currentBusinessName);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null) {
            if (requestCode == 4 && resultCode == 4) {
                if (data.getStringExtra("body") != null) {

                    currentBusinessName=data.getStringExtra("body");
                    businessName.setText(currentBusinessName);
                    checkBusinessName(currentBusinessName);
                }
            } else if (requestCode == 1 && resultCode == 1) {
                if (data.getStringExtra("body") != null) {
                    currentTopicName=data.getStringExtra("body");
                    setInfo();

                }
            } else if(requestCode == 7 && resultCode == 7){
                if (data.getStringExtra("body") != null) {
                    currentIntro=data.getStringExtra("body");
                    setInfo();

                }
            }
        }


    }


    public void chooseImage(){
        PickService pickService = new PickService(context);
        pickService.pickPhoto(6- mPicSize,new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {

                seletedImage.addAll(list);
                mPicSize=seletedImage.size();
                objectList.clear();
                objectList.addAll(seletedImage);
                for (int i=0;i<(6-seletedImage.size());i++){
                    objectList.add(new EmptyItem());
                }
                mAdapter.notifyDataSetChanged();


            }
            @Override
            public void onSelectSuccess(LocalMedia localMedia) {
            }
        });
    }






    public void upLoadImage(){

         snackbar= SnackBarUtil.getLongTimeInstance(refreshLayout,"上传中  请稍后");
        snackbar.show();
        UploadService uploadService=new UploadService(context);
        uploadService.upload(seletedImage, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<String> list= (List<String>) o;
                imageKeys.clear();
                imageKeys.addAll(list);



                postCreate();


            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });



    }


    public void postCreate(){

         userID= (String) SPUtils.get(context,USERID,"");
        int num = new Random().nextInt(100001);
        long uuidBySbj = System.currentTimeMillis() * 100000 + num;
        int size=imageKeys.size();
        for (int i=0;i<(6-size);i++){
            imageKeys.add("null");
        }

        PNotes pNotes=new PNotes(userID,currentTopicID,uuidBySbj,currentBusinessName,currentTopicName,currentIntro,currentCity,currentTag,
                imageKeys.get(0),imageKeys.get(1),imageKeys.get(2),imageKeys.get(3),imageKeys.get(4),imageKeys.get(5));


        String str = mGson.toJson(pNotes);

        findMoreService.createNewTopicItem(str, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                snackbar.setText("发布成功");

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
                snackbar.setText("发布失败");

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.dismiss();
                    }
                },1000);



            }
        });


    }





    /**
     * 城市选择Dialog
     */
    private void showCityDialog() {
        CityPicker cityPicker = new CityPicker.Builder(TopicItemCreateActivity.this)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("浙江省")
                .city("杭州市")
                .onlyShowProvinceAndCity(true)
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
//                String district = citySelected[2];
                //邮编
//                String code = citySelected[3];
                currentCity=province+" "+city;

                setInfo();
            }

            @Override
            public void onCancel() {
//                Toast.makeText(EditInformationActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
}
