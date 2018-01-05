package com.lede.second_23.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonAllForumBean;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.AlbumService;
import com.lede.second_23.service.ForumService;
import com.lede.second_23.service.SocialService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.UiUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.IMAGE_URLS;
import static com.lede.second_23.global.GlobalConstants.TYPE;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 18/1/4.
 */

public class UserInfoActivty extends BaseActivity {


    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.view_pager_box)
    RelativeLayout viewpagerBox;

    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.vip_logo)
    ImageView vipLogo;

    @Bind(R.id.user_info)
    TextView userInfo;
    @Bind(R.id.more_user_info)
    ImageView moreUserInfo;
    @Bind(R.id.new_message)
    TextView concernView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;


    @Bind(R.id.user_info_card)
    LinearLayout userInfoCard;
    @Bind(R.id.user_img)
    ImageView userImg;
    @Bind(R.id.sex_bg)
    ImageView sexBg;
    @Bind(R.id.user_concern)
    TextView userConcern;
    @Bind(R.id.user_fans)
    TextView userFans;
    @Bind(R.id.user_age)
    TextView userAge;
    @Bind(R.id.user_city)
    TextView userCity;
    @Bind(R.id.user_hobby)
    TextView userHobby;
    @Bind(R.id.user_school)
    TextView userSchool;

    private Context context;
    private CommonAdapter forumAdapter;
    private int currentPage = 1;
    private int pageSize = 15;
    private boolean isHasNextPage = false;
    private RequestManager requestManager;
    private boolean isInfoCardShown=false; //默认隐藏用户的详细资料卡
    private ObjectAnimator animator;

    private int forumItemWidth;

    private ArrayList<PersonAllForumBean.DataBean.SimpleBean.ListBean> mForumList = new ArrayList<>();
    private ArrayList<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> mAlbumList = new ArrayList<>();

    private boolean isRefresh = true; //刷新种类 刷新还是加载更多

    private PersonalAlbumBean.DataBean.UserInfo userInfoBean;
    private PersonalAlbumBean.DataBean dataBean;
    private ForumService forumService;
    private AlbumService albumService;
    private SocialService socialService;
    private UserInfoService userInfoService;
    private String userID;
    private boolean isVIP=false;


    public MultiTransformation transformation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);
        context = this;
        ButterKnife.bind(this);
        requestManager = Glide.with(context);

        forumItemWidth = ((UiUtils.getScreenWidth() - UiUtils.dip2px(20)) / 3);

        userID=getIntent().getStringExtra(USERID);
        ButterKnife.bind(this);



        initView();

        initEvent();
        toRefresh();
        refreshLayout.isRefreshing();
        refreshLayout.setEnableAutoLoadmore(false);

    }



    @OnClick({R.id.back,R.id.bigLinear, R.id.more_user_info, R.id.new_message,R.id.user_info_card,R.id.user_img,R.id.user_concern,R.id.user_fans})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back:

                finish();
                break;

            case R.id.more_user_info:

                bottom_show();
                break;

            case R.id.bigLinear:
                if(isInfoCardShown){
                    bottom_show();
                }
                break;
            case R.id.new_message:

                if(concernView.isSelected()){
                    socialService.cancelCollect(userID, new MyCallBack() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(context,"已取消关注",Toast.LENGTH_SHORT).show();
                            toRefresh();
                        }

                        @Override
                        public void onFail(String mistakeInfo) {

                        }
                    });
                }else {
                    socialService.createCollect(userID, new MyCallBack() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(context,"关注成功",Toast.LENGTH_SHORT).show();
                            toRefresh();
                        }

                        @Override
                        public void onFail(String mistakeInfo) {

                        }
                    });


                }



                break;
            case R.id.user_info_card:

                break;
            case R.id.user_img:

                break;
            case R.id.user_concern:
                intent = new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("id", userID);
                startActivity(intent);
                break;
            case R.id.user_fans:
                intent = new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id",userID);
                startActivity(intent);
                break;
            default:
                break;


        }


    }

    private void initView() {

        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(3), 0, RoundedCornersTransformation.CornerType.ALL)
        );
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,forumItemWidth*3);
        viewpagerBox.setLayoutParams(layoutParams);

        animator = ObjectAnimator.ofFloat(userInfoCard, "translationY", 0, UiUtils.dip2px(500));
        animator.setDuration(0);
        animator.start();
        isInfoCardShown=false;


        forumAdapter = new CommonAdapter<PersonAllForumBean.DataBean.SimpleBean.ListBean>(context, R.layout.item_person_fragment_show, mForumList) {
            @Override
            protected void convert(ViewHolder holder, PersonAllForumBean.DataBean.SimpleBean.ListBean listBean, int position) {
                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(forumItemWidth, forumItemWidth));


                ImageView showView_photos = (ImageView) holder.getView(R.id.iv_person_fragment_item_photos);
                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);
                if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    requestManager.load(listBean.getAllRecords().get(0).getUrl()).bitmapTransform(transformation).into(showView_show);
                    showView_play.setVisibility(View.GONE);
                    if (listBean.getAllRecords().size() == 1 || listBean.getAllRecords().size() == 0) {
                        showView_photos.setVisibility(View.GONE);
                    } else {
                        showView_photos.setVisibility(View.VISIBLE);
                    }


                } else {
                    Log.i("videopic", "convert: " + listBean.getAllRecords().get(0).getUrlThree());
                    requestManager.load(listBean.getAllRecords().get(0).getUrlThree()).bitmapTransform(transformation).into(showView_show);
                    showView_play.setVisibility(View.VISIBLE);
                    showView_photos.setVisibility(View.GONE);
                }

            }
        };
        forumAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(context, ForumDetailActivity.class);
                intent.putExtra("forumId", mForumList.get(position).getForumId()); //position-1?
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        GridLayoutManager manager = new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(forumAdapter);


    }

    private void initEvent() {
        forumService = new ForumService(this);
        albumService = new AlbumService(this);
        userInfoService=new UserInfoService(this);
        socialService=new SocialService(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                toRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                toLoadMore();
            }
        });
    }
    private void bottom_show() {
        animator = null;
        if (isInfoCardShown) {
            //藏起
            animator = ObjectAnimator.ofFloat(userInfoCard, "translationY", 0, userInfoCard.getHeight()+UiUtils.dip2px(5));
            animator.setDuration(500);
            animator.start();
            isInfoCardShown=false;
        } else {
            //展开
            animator = ObjectAnimator.ofFloat(userInfoCard, "translationY", userInfoCard.getHeight()+UiUtils.dip2px(5), 0);
            animator.setDuration(500);
            animator.start();
            isInfoCardShown=true;
        }
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
            refreshLayout.finishLoadmore();
        }
    }

    private void doRequest(int pageNum) {

        forumService.requestMyForum(userID, pageNum, 18, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
                List<PersonAllForumBean.DataBean.SimpleBean.ListBean> list = (List<PersonAllForumBean.DataBean.SimpleBean.ListBean>) o;

                if (list.size() != 0) {

                    if (isRefresh) {
                        mForumList.clear();

                        currentPage = 1;
                        isHasNextPage = true;


                    } else {
                        currentPage++;
                    }
                    mForumList.addAll(list);

                    forumAdapter.notifyDataSetChanged();
                } else {
                    isHasNextPage = false;
                }
            }

            @Override
            public void onFail(String mistakeInfo) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
            }
        });

        albumService.getMyAlbum(userID, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                PersonalAlbumBean personalAlbumBean= (PersonalAlbumBean) o;

                List<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> list =personalAlbumBean.getData().getSimple().getList() ;



                mAlbumList.clear();
                mAlbumList.addAll(list);
                userInfoBean =personalAlbumBean.getData().getUserInfo().get(0);
                dataBean=personalAlbumBean.getData();
                setAlbum();
                if(isRefresh){

                    setUserInfo();
                }

            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });

        socialService.checkIfConcern(userID, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                boolean isConcern= (boolean) o;
                if(isConcern){ //已关注
                    concernView.setText("已关注");
                    concernView.setTextColor(Color.parseColor("#ffffff"));
                    concernView.setSelected(true);
                }else{
                    concernView.setSelected(false);
                    concernView.setText("关注");
                    concernView.setTextColor(Color.parseColor("#4aa3e7"));

                }
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }




    public void setUserInfo(){
        userName.setText(userInfoBean.getNickName());
        userInfo.setText(userInfoBean.getNote());

        if (userInfoBean.getSex().equals("男")) {

            sexBg.setSelected(true);
        } else {
            sexBg.setSelected(false);
        }

        if(userInfoBean.getTrueName()!=null&& userInfoBean.getTrueName().equals("1")){
            vipLogo.setVisibility(View.VISIBLE);

        }else{
            vipLogo.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(userInfoBean.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(userImg);
        userConcern.setText(dataBean.getFriendsCount() + " 关注");
        userFans.setText(dataBean.getFollowersCount() + " 粉丝");
        userCity.setText(userInfoBean.getAddress());
        userAge.setText(userInfoBean.getHobby());
        userHobby.setText(userInfoBean.getWechat());
        userSchool.setText(userInfoBean.getHometown());



    }


    //将相册的图片设置在viewpager里面
    public void setAlbum(){
        viewPager.setOffscreenPageLimit(3);

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return mAlbumList.size()+1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override

            public void destroyItem(ViewGroup container,int position,Object o){
            }
            @Override

            public Object instantiateItem(final ViewGroup container, final int position){


                LayoutInflater inflater=LayoutInflater.from(context);
                View imgeLayout=inflater.inflate(R.layout.activity_viewpager_pic_item,container,false);
                imgeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isInfoCardShown) {
                            //藏起
                            animator = ObjectAnimator.ofFloat(userInfoCard, "translationY", 0, userInfoCard.getHeight()+UiUtils.dip2px(5));
                            animator.setDuration(500);
                            animator.start();
                            isInfoCardShown=false;
                        }else{
                            if(position!=0){

                                Intent intent = new Intent(context, ConcernActivity_2.class);
                                intent.putExtra(IMAGE_URLS,mAlbumList.get(position-1).getUrlImg());
                                intent.putExtra(USERID,userID);
                                intent.putExtra(TYPE,2);
                                startActivity(intent);
                            }

                        }                   }
                });
                ImageView imageView= (ImageView) imgeLayout.findViewById(R.id.imageView);
                LinearLayout linearLayout= (LinearLayout) imgeLayout.findViewById(R.id.linearLayout);


                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(forumItemWidth*3+UiUtils.dip2px(10),forumItemWidth*3);


//                linearLayout.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                if(position==0){
                    requestManager.load(userInfoBean.getImgUrl()).bitmapTransform(transformation).into(imageView);

                }else{
                    requestManager.load(mAlbumList.get(position-1).getUrlImg()).bitmapTransform(transformation).into(imageView);

                }

                container.addView(imgeLayout);

                return imgeLayout;

            }


        };

        viewPager.setAdapter(pagerAdapter);
        if(mAlbumList.size()!=0){
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);
        }




    }

}
