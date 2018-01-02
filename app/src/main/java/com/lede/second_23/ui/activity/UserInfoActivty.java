package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.IfConcernBean;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.RefreshAndLoadMoreListener;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.ui.fragment.PersonalFragmentAlbum;
import com.lede.second_23.ui.fragment.PersonalFragmentAllBlog;
import com.lede.second_23.ui.fragment.PersonalFragmentMe;
import com.lede.second_23.utils.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 版本改动太大，此页面废弃
 * Created by ld on 17/11/2.
 */

public class UserInfoActivty extends BaseActivity implements RefreshAndLoadMoreListener{




    @Bind(R.id.iv_personfragment_back)
    ImageView back;
    @Bind(R.id.personfragment_me)
    ImageView meClick;
    @Bind(R.id.personfragment_dongtai)
    ImageView dongtaiClick;
    @Bind(R.id.personfragment_album)
    ImageView albumClick;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.iv_person_fragment_item_sex)
    ImageView boyOrGirl;
    @Bind(R.id.tv_personfragment_username)
    TextView userName;
    @Bind(R.id.vip_logo)
    ImageView vipLogo;

    @Bind(R.id.tv_personfragment_sign)
    TextView userSign;
    @Bind(R.id.ctiv_personfragment_userimg)
    ImageView userImg;
    @Bind(R.id.tv_converned_num)
    TextView followingsNum;
    @Bind(R.id.tv_fans_num)
    TextView fansNum;
    @Bind(R.id.ll_person_fragment_concerned)
    LinearLayout followingClick;
    @Bind(R.id.ll_person_fragment_fans)
    LinearLayout fansClick;
    @Bind(R.id.iv_personfragment_concern)
    ImageView concernClick;
    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @OnClick({R.id.ll_person_fragment_fans, R.id.ll_person_fragment_concerned, R.id.iv_personfragment_concern,
            R.id.personfragment_me,R.id.personfragment_dongtai, R.id.personfragment_album, R.id.iv_personfragment_back,
            R.id.ctiv_personfragment_userimg
    })
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ctiv_personfragment_userimg:
                intent = new Intent(UserInfoActivty.this, UserInfoCardActivity.class);
                intent.putExtra(USERID,userId);
                startActivity(intent);
                break;

            case R.id.iv_personfragment_back:
                finish();
                break;

            case R.id.ll_person_fragment_concerned:
                intent = new Intent(this, ConcernOrFansActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("id", userId);
                startActivity(intent);
                break;
            case R.id.ll_person_fragment_fans:
                intent = new Intent(this, ConcernOrFansActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", userId);
                startActivity(intent);
                break;
            case R.id.iv_personfragment_concern:

                boolean isConcerned=concernClick.isSelected();
                if (isConcerned){
                    cancelCollect(userId);
                }else{
                    createCollect(userId);
                }

                break;
            case R.id.personfragment_me:
                if (currentPage != 0) {
                    viewPager.setCurrentItem(0, true);
                    meClick.setSelected(true);
                    dongtaiClick.setSelected(false);
                    albumClick.setSelected(false);
                    currentPage = 0;
                }
                break;

            case R.id.personfragment_dongtai:
                if (currentPage != 1) {
                    viewPager.setCurrentItem(1, true);
                    meClick.setSelected(false);
                    dongtaiClick.setSelected(true);
                    albumClick.setSelected(false);
                    currentPage = 1;
                }
                break;
            case R.id.personfragment_album:
                if (currentPage != 2) {
                    viewPager.setCurrentItem(2, true);
                    meClick.setSelected(false);
                    albumClick.setSelected(true);
                    dongtaiClick.setSelected(false);
                    currentPage = 2;
                }
                break;
            default:
                break;
        }
    }

    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private static final int REQUEST_USER_INFO = 51515;
    private static final int REQUEST_USER_RELATION=534341;
    private static final int REQUEST_CANCAL=23223;
    private static final int REQUEST_Concern=23255;



    private String userId;
    private PersonalAlbumBean.DataBean.UserInfo userInfo;
    private int currentPage = 0;//当前页 初始默认为0

    private PersonalFragmentAlbum personalFragmentAlbum;
    private PersonalFragmentAllBlog personalFragmentAllBlog;
    private PersonalFragmentMe personalFragmentMe;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;
    private PersonalAlbumBean.DataBean dataBean;
    private Request<String> userInfoRequest = null;
    private Request<String> userRelationRequest=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        userId=getIntent().getStringExtra(USERID);
        ButterKnife.bind(this);

        mGson = new Gson();

        initView();
        initEvent();
        doRequest();
        mRefreshLayout.isRefreshing();
        mRefreshLayout.setEnableLoadmore(false);
    }


    private void initView() {
        fragmentList = new ArrayList<>();
        if (personalFragmentMe == null) {
            personalFragmentMe = new PersonalFragmentMe();
        }
        fragmentList.add(personalFragmentMe);

        if (personalFragmentAllBlog == null) {
            personalFragmentAllBlog = new PersonalFragmentAllBlog();
        }
        fragmentList.add(personalFragmentAllBlog);

        if (personalFragmentAlbum == null) {
            personalFragmentAlbum = new PersonalFragmentAlbum();
        }
        fragmentList.add(personalFragmentAlbum);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    currentPage=0;
                    meClick.setSelected(true);
                    dongtaiClick.setSelected(false);
                    albumClick.setSelected(false);
                }

                if (position == 1) {
                    currentPage=1;
                    meClick.setSelected(false);
                    dongtaiClick.setSelected(true);
                    albumClick.setSelected(false);
                }
                if (position == 2) {
                    currentPage=2;
                    meClick.setSelected(false);
                    dongtaiClick.setSelected(false);
                    albumClick.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        currentPage = 0;
        viewPager.setCurrentItem(currentPage, false);
        meClick.setSelected(true);
    }


    public void initEvent() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (userInfoRequest != null) {
                    userInfoRequest.cancel();
                }
                doRequest();
                if (personalFragmentAllBlog.isResumed()) {
                    personalFragmentAllBlog.toRefresh();
                }
                if (personalFragmentAlbum.isResumed()) {
                    personalFragmentAlbum.toRefresh();
                }
                if(personalFragmentMe.isResumed()){
                    personalFragmentMe.doRequest();
                }


            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (personalFragmentAllBlog.isResumed()) {
                    personalFragmentAllBlog.toLoadMore();
                }
                if (personalFragmentAlbum.isResumed()) {
                    personalFragmentAlbum.toLoadMore();
                }
            }
        });
    }


    private void doRequest() {

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        mRefreshLayout.finishRefresh();
                        if(!UserInfoActivty.this.isDestroyed()){
                            parseUserInfo(response.get());
                        }

                        break;
                    case REQUEST_USER_RELATION:
                        if(!UserInfoActivty.this.isDestroyed()){
                            parseIfConcerned(response.get());
                        }
                        break;
                    case REQUEST_CANCAL:
                        if(userInfo!=null){
                            userInfo.setFollowersCount(userInfo.getFollowersCount()-1);;
                        }
                        fansNum.setText(userInfo.getFollowersCount()+"");
                        concernClick.setSelected(false);
                        Toast.makeText(UserInfoActivty.this,"已取消关注",Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_Concern:

                        if(userInfo!=null){
                            userInfo.setFollowersCount(userInfo.getFollowersCount()+1);;
                        }
                        fansNum.setText(userInfo.getFollowersCount()+"");
                        concernClick.setSelected(true);
                        Toast.makeText(UserInfoActivty.this,"关注成功",Toast.LENGTH_SHORT).show();
                        break;


                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_USER_INFO:
                        mRefreshLayout.finishRefresh();
                        break;
                    case REQUEST_USER_RELATION:


                        break;
                    default:
                        break;
                }
            }
        };
        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);

        userInfoRequest.add("userId", userId);
        userInfoRequest.add("pageNum", 1);
        userInfoRequest.add("pageSize", 1);
        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);

        userRelationRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/collection/collectReship", RequestMethod.POST);
        userRelationRequest.add("userId",(String) SPUtils.get(this, GlobalConstants.USERID, ""));
        userRelationRequest.add("toUserId",userId);
        RequestServer.getInstance().request(REQUEST_USER_RELATION, userRelationRequest, simpleResponseListener);



    }
    /**
     * 关注用户
     */
    private void createCollect(String id) {
        Request<String> createCollectRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/createCollect", RequestMethod.POST);
        createCollectRequest.add("id",id);
        createCollectRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        RequestServer.getInstance().request(REQUEST_Concern,createCollectRequest,simpleResponseListener);
    }

    /**
     * 取消关注
     */
    private void cancelCollect(String id) {
        Request<String> cancelCollectRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/collection/cancelCollect", RequestMethod.POST);
        cancelCollectRequest.add("id",id);
        cancelCollectRequest.add("access_token",(String) SPUtils.get(this,GlobalConstants.TOKEN,""));
        RequestServer.getInstance().request(REQUEST_CANCAL,cancelCollectRequest,simpleResponseListener);
    }


    private void parseUserInfo(String s) {
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(s, PersonalAlbumBean.class);
        userInfo = personalAlbumBean.getData().getUserInfo().get(0);
        dataBean=personalAlbumBean.getData();
        setUserInfo();
    }
    public void parseIfConcerned(String s){

        IfConcernBean ifConcernBean= mGson.fromJson(s, IfConcernBean.class);
        concernClick.setSelected(ifConcernBean.getData().isCollect());


    }
    public void setUserInfo() {
        userName.setText(userInfo.getNickName());
        if (userInfo.getSex().equals("男")) {

            boyOrGirl.setSelected(false);
        } else {
            boyOrGirl.setSelected(true);
        }

        if(userInfo.getTrueName()!=null&&userInfo.getTrueName().equals("1")){
            vipLogo.setVisibility(View.VISIBLE);
        }else{
            vipLogo.setVisibility(View.GONE);
        }

        userSign.setText(userInfo.getNote());
        Glide.with(this)
                .load(userInfo.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(userImg);
        followingsNum.setText(dataBean.getFriendsCount() + "");
        fansNum.setText(dataBean.getFollowersCount() + "");
//        PersonalAlbumBean.DataBean.UserInfo
    }



    @Override
    public void isOver() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }







}
