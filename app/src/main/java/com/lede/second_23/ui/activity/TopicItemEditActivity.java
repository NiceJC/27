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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.FootMarksBean;
import com.lede.second_23.bean.OnlyUserBean;
import com.lede.second_23.bean.PNotes;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.bean.TopicsBean;
import com.lede.second_23.interface_utils.MyCallBack;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 18/1/24.
 */

public class TopicItemEditActivity extends BaseActivity {



    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView_1)
    RecyclerView mRecyclerView;
    @Bind(R.id.recyclerView_topic)
    RecyclerView recyclerViewTopic;
    @Bind(R.id.topic_layout)
    LinearLayout topicLayout;
    @Bind(R.id.business_image)
    ImageView businessImage;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.topic_name)
    TextView topicName;
    @Bind(R.id.introduction)
    TextView introduction;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.tag)
    TextView tag;
    @Bind(R.id.tittle)
    TextView tittle;










    private boolean hasChangedUserName=false;



    private Gson mGson;

    private Activity context;
    private MultiItemTypeAdapter mAdapter;


    private List<LocalMedia> seletedImage=new ArrayList<>();
    private List<String> imageKeys=new ArrayList<>();

    private ArrayList<Object> objectList=new ArrayList<>();
    private List<Object> imageObjectList=new ArrayList<>();

    private int mPicSize =0;

    private PersonalAlbumBean.DataBean.UserInfo userInfoBean;
    private int itemWidth;
    private RequestManager requestManager;

    private String userID;
    private Snackbar snackbar;
    private PictureConfig.OnSelectResultCallback portraitPickCallback;

    private String currentBusinessName;
    private String currentTopicName;
    private String currentIntro;
    private String currentCity;
    private String currentTag;
    private long currentTopicID;
    private long currentTopicItemID;
    private LatLonPoint choosedLatLonPoint;
    private String choosedProvince;
    private String choosedCity;
    private String choosedArea;
    private boolean isInfoCardShown = false; //默认隐藏用户的详细资料卡
    private ObjectAnimator animator;

    private CommonAdapter topicsAdapter;

    private List<TopicsBean.DataBean.SimpleBean.ListBean> topicsList=new ArrayList<>();

    private FootMarksBean footMarksBean;

    private List<FootMarksBean.DataBean.SimpleBean.ListBean> footMarkList = new ArrayList<>();


    private String userId;
    private FindMoreService findMoreService;
    private List<String> imageUrls=new ArrayList<>();
    private String businessUserId;
    private String businessUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic_item);

        context = this;
        ButterKnife.bind(context);
        requestManager = Glide.with(context);
        userId= (String) SPUtils.get(context,USERID,"");


        currentTopicItemID =getIntent().getLongExtra(TOPICITEMID,0);
        mGson=new Gson();

        itemWidth = (UiUtils.getScreenWidth() / 3);

        userID = (String) SPUtils.get(context, USERID, "");


        findMoreService=new FindMoreService(context);
        getDataFromItent(getIntent());

        initView();

        initEvent();
        doRequest();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromItent(intent);



    }
    public void getDataFromItent(Intent intent){
//        choosedTittle=intent.getStringExtra("tittle");
        choosedLatLonPoint=intent.getParcelableExtra("latLonPoint");
        choosedProvince=intent.getStringExtra("province");
        choosedCity=intent.getStringExtra("city");
        choosedArea=intent.getStringExtra("area");
//        choosedDetailAddress=intent.getStringExtra("detailAddress");


        //浙江省 杭州市 滨江区|30.208837,120.208695
        if(choosedLatLonPoint!=null){
            currentCity=choosedProvince+" "+choosedCity+" "+choosedArea +"|"+choosedLatLonPoint.getLatitude()+","+choosedLatLonPoint.getLongitude();
            Log.d("location",currentCity);

            setInfo();
        }





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
                intent.putExtra("body", userName.getText().toString().trim());
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
//                showCityDialog();
                intent = new Intent(this, LocationChooseActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
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
        mAdapter.addItemViewDelegate(new ImageUrlDelegate());

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


    public class ImageUrlDelegate implements ItemViewDelegate<Object>{
        @Override
        public int getItemViewLayoutId() {
            return R.layout.edit_info_album_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(ViewHolder holder, Object o, final int position) {
            holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemWidth));
            String url= (String) o;
            ImageView image=holder.getView(R.id.image);
            ImageView delete=holder.getView(R.id.delete_image);
            Glide.with(context).load(url).into(image);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    objectList.remove(position);

                    imageObjectList.remove(position);
                    objectList.add(new EmptyItem());
                    mAdapter.notifyDataSetChanged();




                }
            });
        }
    }



    public class UserphotoItemDelegate implements ItemViewDelegate<Object> {
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
                    imageObjectList.remove(position);

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

    public void checkBusinessName(final String mkName){
        UserInfoService userInfoService=new UserInfoService(context);
        userInfoService.requestOnlyUserByName(mkName, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                OnlyUserBean.DataBean.UserBean user= (OnlyUserBean.DataBean.UserBean) o;
                businessImage.setVisibility(View.VISIBLE);
                currentBusinessName =user.getNickName();
                userName.setText(currentBusinessName);

                requestManager.load(user.getImgUrl()).into(businessImage);



            }

            @Override
            public void onFail(String mistakeInfo) {
                businessImage.setVisibility(View.GONE);


                currentBusinessName="";
                userName.setText(currentBusinessName);
            }
        });



    }

    /**
     * 如果商家image ok
     * 将商家image设到板块中
     */
    public void setBusinessImage(long topicItemID){
        findMoreService.createTopicItemBusinessImage(topicItemID, businessUserId, businessUserImage, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });




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

        userName.setText(currentBusinessName);
        topicName.setText(currentTopicName);
        introduction.setText(currentIntro);
        city.setText(currentCity);
        tag.setText(currentTag);

    }

    private void createTopicItem(){

//        if (userName.getText().toString().trim().equals("") || userName.getText().toString().trim().equals("昵称")) {
//            Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show();
//            return;
//        } else
            if (topicName.getText().toString().trim().equals("") || topicName.getText().toString().trim().equals("签名")) {
            Toast.makeText(context, "请输入版块名称", Toast.LENGTH_SHORT).show();
            return;
        }  else if (city.getText().toString().trim().equals("") || city.getText().toString().trim().equals("爱好")) {
            Toast.makeText(context, "请输入城市", Toast.LENGTH_SHORT).show();
            return;
        } else if (tag.getText().toString().trim().equals("") || tag.getText().toString().trim().equals("年龄")) {
            Toast.makeText(context, "请选择对应主题", Toast.LENGTH_SHORT).show();
            return;
        }

        if(seletedImage.size()!=0){
            upLoadImage();
        }else{
            postCreate();
        }




    }
//
//    public void deleteAlbum(int id){
//        albumService.deleteAlbum(id, new MyCallBack() {
//            @Override
//            public void onSuccess(Object o) {
//                Toast.makeText(EditRegisterInfoActivity2.this,"删除成功",Toast.LENGTH_SHORT).show();
//                doRequest();
//            }
//
//            @Override
//            public void onFail(String mistakeInfo) {
//
//            }
//        });
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null) {
            if (requestCode == 4 && resultCode == 4) {
                if (data.getStringExtra("body") != null) {

                    String name=data.getStringExtra("body");
                    checkBusinessName(name);

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
        pickService.pickPhoto(6- imageObjectList.size(),new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {

                seletedImage.addAll(list);
                imageObjectList.addAll(list);

                objectList.clear();
                objectList.addAll(imageObjectList);
                for (int i=0;i<(6-imageObjectList.size());i++){
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

        if(seletedImage.size()==0){
            snackbar= SnackBarUtil.getLongTimeInstance(refreshLayout,"上传中  请稍后");
            snackbar.show();

        }

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

        String str;


            /**
             * 将objectList进行处理
             *
             * 1.将LocalMedia替换为上传成功后的key
             * 2.将String即原先的imageurl提取出后半部分的key
             * 3 将Empty替换为null
             *
             */

            int imageSize=imageKeys.size();

            List<String> keyList=new ArrayList<>();
            int keyPosition=0;
            for(Object object:objectList){
//                int position=objectList.indexOf(object);
                if (object instanceof LocalMedia){
                    keyList.add(imageKeys.get(keyPosition));
                    keyPosition++;

                }else if(object instanceof String){
                    String url= (String) object;
                    String str2=  url.substring(30);
                    keyList.add(str2);
                }else{
                    keyList.add("null");

                }

            }

//            for (int i=0;i<(6-imageSize);i++){
//                imageKeys.add("null");
//            }

            PNotes pNotes=new PNotes(userId,currentTopicID,currentTopicItemID,currentBusinessName,currentTopicName,currentIntro,currentCity,currentTag,
                    keyList.get(0),keyList.get(1),keyList.get(2),keyList.get(3),keyList.get(4),keyList.get(5));


            str = mGson.toJson(pNotes);


        findMoreService.editTopicItem(str, myCallBack);


    }


    public void doRequest(){

        findMoreService.requestAllFootMark(currentTopicItemID, 1, 1, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {


                footMarksBean= (FootMarksBean) o;
                FootMarksBean.DataBean.PNotesListBean topicItem=footMarksBean.getData().getpNotesList().get(0);

                currentBusinessName =topicItem.getBusinessName();
                checkBusinessName(currentBusinessName);
                currentCity=topicItem.getCityName();
                currentTopicName=topicItem.getDetailsName();
                currentIntro=topicItem.getDetailsAll();
                currentTag=topicItem.getLabelName();
                currentTopicID=topicItem.getUuid();


                imageUrls.clear();
                if(!topicItem.getUrlOne().equals("http://my-photo.lacoorent.com/null")){
                    imageUrls.add(topicItem.getUrlOne());

//                    String url=topicItem.getUrlOne();
//                  if(url.length()>40){
//                      String str2=  url.substring(30);
//
//                  }
//




                }
                if(!topicItem.getUrlTwo().equals("http://my-photo.lacoorent.com/null")){
                    imageUrls.add(topicItem.getUrlTwo());
                }
                if(!topicItem.getUrlThree().equals("http://my-photo.lacoorent.com/null")){
                    imageUrls.add(topicItem.getUrlThree());
                }
                if(!topicItem.getUrlFour().equals("http://my-photo.lacoorent.com/null")){
                    imageUrls.add(topicItem.getUrlFour());
                }
                if(!topicItem.getUrlFive().equals("http://my-photo.lacoorent.com/null")){
                    imageUrls.add(topicItem.getUrlFive());
                }
                if(!topicItem.getUrlSix().equals("http://my-photo.lacoorent.com/null")){
                    imageUrls.add(topicItem.getUrlSix());
                }


                objectList.clear();
                objectList.addAll(imageUrls);
                imageObjectList.clear();
                imageObjectList.addAll(imageUrls);
                for (int i=0;i<(6-imageUrls.size());i++){
                    objectList.add(new EmptyItem());
                }

                mAdapter.notifyDataSetChanged();


                setInfo();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });

    }







    /**
     * 城市选择Dialog
     */
    private void showCityDialog() {
        CityPicker cityPicker = new CityPicker.Builder(context)
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
