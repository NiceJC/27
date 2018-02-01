package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.FootMarksBean;
import com.lede.second_23.bean.OnlyUserBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.interface_utils.OnItemSelectedListener;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.utils.DateUtils;
import com.lede.second_23.utils.DialogUtil;
import com.lede.second_23.utils.GlideImageLoader;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.SnackBarUtil;
import com.lede.second_23.utils.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.CURRENT_CITY;
import static com.lede.second_23.global.GlobalConstants.ISMANAGER;
import static com.lede.second_23.global.GlobalConstants.TOPICID;
import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;
import static com.lede.second_23.global.GlobalConstants.USERID;
import static io.rong.imkit.utilities.RongUtils.screenWidth;

/**
 * 足迹页面，点击小版块的详情展开页
 * 记录到过这里的用户的足迹
 * Created by ld on 18/1/16.
 */

public class TopicItemDetailActivity extends AppCompatActivity {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.back_layout)
    RelativeLayout backLayout;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.back_title)
    TextView backTittle;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.business_image)
    ImageView businessImage;
    @Bind(R.id.introduction)
    TextView introduction;
    @Bind(R.id.location)
    ImageView locationToast;



    private Gson mGson;

    private Activity context;
    private CommonAdapter mAdapter;

    private FootMarksBean footMarksBean;

    private List<FootMarksBean.DataBean.SimpleBean.ListBean> footMarkList = new ArrayList<>();

    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;


    private ArrayList<String> imageUrls=new ArrayList<>();
    private long topicItemID;
    private long topicID;
    private FindMoreService findMoreService;
    private UserInfoService userInfoService;
    private RequestManager requestManager;
    private String businessUserId="";

    private boolean isImageOK=false;
    private String currentCity;

    public static TopicItemDetailActivity instance;
    private String isHere="0";
    private String userId;
    private OnItemSelectedListener onItemSelectedListener;
    private Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item_detail);
        StatusBarUtil.transparencyBar(this);

        context = this;
        instance=this;
        ButterKnife.bind(context);
        mGson = new Gson();

        topicID=getIntent().getLongExtra(TOPICID,0);
        topicItemID=getIntent().getLongExtra(TOPICITEMID,0);
        currentCity= (String) SPUtils.get(this,CURRENT_CITY,"杭州市");

        initView();
        initEvent();
        initBanner();
        toRefresh();

    }


    public void initBanner(){
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        //设置轮播时间
        banner.setDelayTime(3000);

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);


        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                Intent intent = new Intent(context, ForumPicActivity.class);
                intent.putExtra("banner", imageUrls);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


    @OnClick({R.id.back,R.id.edit,R.id.business_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                Intent intent=new Intent(context,PostFootMarkActivity.class);
                intent.putExtra(TOPICID,topicID);
                intent.putExtra(TOPICITEMID,topicItemID);
                intent.putExtra("isHere",isHere);
                startActivity(intent);
                break;
            case R.id.business_image:
                Intent intent2 = new Intent(context, UserInfoActivty.class);
                intent2.putExtra(USERID, businessUserId);
                startActivity(intent2);

            default:
                break;
        }
    }


    private void initView() {

        userId= (String) SPUtils.get(this,USERID,"");
        findMoreService =new FindMoreService(context);
        userInfoService=new UserInfoService(context);
        requestManager= Glide.with(context);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });



        mAdapter=new CommonAdapter<FootMarksBean.DataBean.SimpleBean.ListBean>(context,R.layout.item_footmark,footMarkList) {
            @Override
            protected void convert(ViewHolder holder, final FootMarksBean.DataBean.SimpleBean.ListBean footMark, int position) {
                ImageView userImage=holder.getView(R.id.user_img);
                TextView userName=holder.getView(R.id.user_name);
                TextView time=holder.getView(R.id.time);
                TextView contentText=holder.getView(R.id.content_text);
                ImageView contentImage=holder.getView(R.id.content_img);
                final ImageView location=holder.getView(R.id.location);

                if(footMark.getDesp().equals("0")){
                    location.setVisibility(View.VISIBLE);

                }else{
                    location.setVisibility(View.GONE);
                }

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(ISMANAGER||footMark.getUserId().equals(userId)){

                           onItemSelectedListener = new OnItemSelectedListener() {
                               @Override
                               public void getSelectedItem(String content) {

                                   if (content.equals("删除")) {
                                       deleteFootMark(footMark.getId());

                                   }

                               }
                           };
                          DialogUtil.showItemSelectDialog(TopicItemDetailActivity.this, screenWidth / 25 * 24, onItemSelectedListener, "删除");



                       }


                    }
                });

                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        locationToast.setVisibility(View.VISIBLE);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                locationToast.setVisibility(View.GONE);
                            }
                        },1000);

                    }
                });


                if(footMark.getUrlOne().equals("http://my-photo.lacoorent.com/null")){
                    contentImage.setVisibility(View.GONE);
                }else{
                    contentImage.setVisibility(View.VISIBLE);
                    requestManager.load(footMark.getUrlOne()).into(contentImage);
                    contentImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> urls=new ArrayList<String>();
                            urls.add(footMark.getUrlOne());
                            Intent intent = new Intent(context, ForumPicActivity.class);
                            intent.putExtra("banner", urls);
                            intent.putExtra("position", 0);
                            startActivity(intent);

                        }
                    });


                }
                if(footMark.getForumName()==null||footMark.getForumName().equals("")){
                    contentText.setVisibility(View.GONE);
                }else{
                    contentText.setVisibility(View.VISIBLE);
                    contentText.setText(footMark.getForumName());

                }

                requestManager.load(footMark.getUserInfo().getImgUrl()).bitmapTransform(new CropCircleTransformation(context)).into(userImage);
                userName.setText(footMark.getUserInfo().getNickName());
                time.setText(DateUtils.geRegularTime(footMark.getCreatTime()));
                userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2 = new Intent(context, UserInfoActivty.class);
                        intent2.putExtra(USERID, footMark.getUserInfo().getUserId());
                        startActivity(intent2);
                    }
                });

            }

        };
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);


    }


    public void showSnackBar(){

        snackbar= SnackBarUtil.getLongTimeInstance(mRecyclerView,"正在发送");

        snackbar.show();

    }
    public void closeSnackBar(String s){
        if(snackbar!=null){
            snackbar.setText(s);

        }
        toRefresh();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                snackbar.dismiss();
            }
        },1000);

    }
    public void initEvent() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                toRefresh();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                toLoadMore();
            }
        });
    }

    public void toRefresh() {

        isRefresh = true;
        doRequest(1);
    }

    public void toLoadMore() {

        if (isHasNextPage) {
            isRefresh = false;
            doRequest(currentPage + 1);
        } else {
            mRefreshLayout.finishLoadmore();
        }


    }



    public void checkBusinessImage(String mkName){

        UserInfoService userInfoService=new UserInfoService(context);
        userInfoService.requestOnlyUserByName(mkName, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                OnlyUserBean.DataBean.UserBean user= (OnlyUserBean.DataBean.UserBean) o;
                businessImage.setVisibility(View.VISIBLE);
                businessUserId=user.getUserId();

                requestManager.load(user.getImgUrl()).bitmapTransform(new CropCircleTransformation(context))
                        .into(businessImage);

            }

            @Override
            public void onFail(String mistakeInfo) {
                businessImage.setVisibility(View.GONE);

            }
        });


    }

    private void doRequest(int page) {


        userInfoService.requestOnlyUserByName("阿", new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


        findMoreService.requestAllFootMark(topicItemID, page, PAGE_SIZE, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {


                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();





                footMarksBean= (FootMarksBean) o;

                List<FootMarksBean.DataBean.SimpleBean.ListBean> list=footMarksBean.getData().getSimple().getList();
                if (isRefresh) {
                    setTopicItemInfo();
                    footMarkList.clear();
                    currentPage = 1;
                    isHasNextPage = true;
                } else {
                    if (list.size() == 0) {
                        isHasNextPage = false;
                    } else {
                        currentPage++;
                    }
                }

                footMarkList.addAll(list);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }

    public void deleteFootMark(int id){
        FindMoreService findMoreService2=new FindMoreService(this);
        MyCallBack myCallBack=new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(TopicItemDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                toRefresh();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        };
        if(ISMANAGER){
            findMoreService2.deleFootMarkByManager(id,myCallBack);

        }else{
            findMoreService2.deleteFootMarkByUser(id,myCallBack);

        }


    }



    public void setTopicItemInfo(){

        FootMarksBean.DataBean.PNotesListBean topicItem=footMarksBean.getData().getpNotesList().get(0);
        name.setText(topicItem.getDetailsName());
        introduction.setText(topicItem.getDetailsAll());
        if(topicItem.getCityName().contains(currentCity)){
            isHere="0";
        }else{
            isHere="1";
        }

        if(topicItem.getBusinessName()!=null&&!topicItem.getBusinessName().equals("")){
            checkBusinessImage(topicItem.getBusinessName());
        }else{
            businessImage.setVisibility(View.GONE);
        }

        imageUrls.clear();
        if(!topicItem.getUrlOne().equals("http://my-photo.lacoorent.com/null")){
            imageUrls.add(topicItem.getUrlOne());
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

        banner.setImages(imageUrls);
        if(imageUrls.size()>1){
            banner.isAutoPlay(true);

        }else{
            banner.isAutoPlay(false);

        }


        banner.start();



    }



}
