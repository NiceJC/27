package com.lede.second_23.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonAllForumBean;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.AlbumService;
import com.lede.second_23.service.ForumService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.ui.activity.ConcernActivity_2;
import com.lede.second_23.ui.activity.ConversationListDynamicActivtiy;
import com.lede.second_23.ui.activity.ForumDetailActivity;
import com.lede.second_23.ui.activity.MainActivity;
import com.lede.second_23.ui.activity.SetActivity;
import com.lede.second_23.utils.SPUtils;
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

import static com.lede.second_23.global.GlobalConstants.IMAGE_URLS;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/12/28.
 */

public class PersonalFragment2 extends Fragment {
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.view_pager_box)
    RelativeLayout viewpagerBox;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.user_info)
    TextView userInfo;
    @Bind(R.id.more_user_info)
    ImageView moreUserInfo;
    @Bind(R.id.new_message)
    ImageView newMessage;
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
    private UserInfoService userInfoService;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal2, container, false);
        context = getActivity();
        ButterKnife.bind(this, view);
        requestManager = Glide.with(getContext());

        forumItemWidth = ((UiUtils.getScreenWidth() - UiUtils.dip2px(40)) / 3);

        userID = (String) SPUtils.get(context, USERID, "");


        initView();

        initEvent();
        toRefresh();
        refreshLayout.isRefreshing();
        refreshLayout.setEnableAutoLoadmore(false);
        return view;
    }


    @OnClick({R.id.back, R.id.set, R.id.more_user_info,R.id.user_info_card, R.id.new_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                MainActivity.instance.vp_main_fg.setCurrentItem(1);

                break;
            case R.id.set:
                Intent intent = new Intent(context, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.more_user_info:

                bottom_show();
                break;
            case R.id.user_info_card:
                bottom_show();
                break;
            case R.id.new_message:
                startActivity(new Intent(context, ConversationListDynamicActivtiy.class));

                break;
            default:
                break;


        }


    }

    private void initView() {

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,forumItemWidth*3);
        viewpagerBox.setLayoutParams(layoutParams);

        animator = ObjectAnimator.ofFloat(userInfoCard, "translationY", 0, UiUtils.dip2px(300));
        animator.setDuration(0);
        animator.start();
        isInfoCardShown=false;


        forumAdapter = new CommonAdapter<PersonAllForumBean.DataBean.SimpleBean.ListBean>(getActivity(), R.layout.item_person_fragment_show, mForumList) {
            @Override
            protected void convert(ViewHolder holder, PersonAllForumBean.DataBean.SimpleBean.ListBean listBean, int position) {
                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(forumItemWidth, forumItemWidth));


                ImageView showView_photos = (ImageView) holder.getView(R.id.iv_person_fragment_item_photos);
                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);
                if (!listBean.getAllRecords().get(0).getUrl().equals("http://my-photo.lacoorent.com/null")) {
                    requestManager.load(listBean.getAllRecords().get(0).getUrl()).into(showView_show);
                    showView_play.setVisibility(View.GONE);
                    if (listBean.getAllRecords().size() == 1 || listBean.getAllRecords().size() == 0) {
                        showView_photos.setVisibility(View.GONE);
                    } else {
                        showView_photos.setVisibility(View.VISIBLE);
                    }


                } else {
                    Log.i("videopic", "convert: " + listBean.getAllRecords().get(0).getUrlThree());
                    requestManager.load(listBean.getAllRecords().get(0).getUrlThree()).into(showView_show);
                    showView_play.setVisibility(View.VISIBLE);
                    showView_photos.setVisibility(View.GONE);
                }

            }
        };
        forumAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
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
        forumService = new ForumService(getActivity());
        albumService = new AlbumService(getActivity());
        userInfoService=new UserInfoService(getActivity());
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
                        if(list.size()<18){
                            isHasNextPage=false;
                        }

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
                setAlbum();
                if(isRefresh){
                    userInfoBean =personalAlbumBean.getData().getUserInfo().get(0);
                    dataBean=personalAlbumBean.getData();
                    setUserInfo();
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
//
//        if(userInfoBean.getTrueName()!=null&& userInfoBean.getTrueName().equals("1")){
//            vipLogo.setVisibility(View.VISIBLE);
//
//        }else{
//            vipLogo.setVisibility(View.GONE);
//        }
//
        Glide.with(getContext())
                .load(userInfoBean.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(userImg);
        userConcern.setText(dataBean.getFriendsCount() + " 关注");
        userFans.setText(dataBean.getFollowersCount() + " 粉丝");
        userCity.setText(userInfoBean.getAddress());
        userAge.setText(userInfoBean.getHobby());
        userHobby.setText(userInfoBean.getWechat());


    }


    //将相册的图片设置在viewpager里面
    public void setAlbum(){
        viewPager.setOffscreenPageLimit(3);

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return mAlbumList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override

            public void destroyItem(ViewGroup container,int position,Object o){
            }
            @Override

            public Object instantiateItem(ViewGroup container, final int position){

                LayoutInflater inflater=LayoutInflater.from(getActivity());
                View imgeLayout=inflater.inflate(R.layout.activity_viewpager_pic_item,container,false);
                imgeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ConcernActivity_2.class);
                        intent.putExtra(IMAGE_URLS,mAlbumList.get(position).getUrlImg());
                        intent.putExtra(USERID,userID);
                        getActivity().startActivity(intent);                    }
                });
                ImageView imageView= (ImageView) imgeLayout.findViewById(R.id.imageView);


                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(forumItemWidth*3,forumItemWidth*3);


                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                requestManager.load(mAlbumList.get(position).getUrlImg()).into(imageView);

                container.addView(imgeLayout);

                return imgeLayout;

            }


        };
        viewPager.setAdapter(pagerAdapter);



    }


}
