package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.RefreshAndLoadMoreListener;
import com.lede.second_23.ui.activity.ConcernOrFansActivity;
import com.lede.second_23.ui.activity.ConversationListDynamicActivtiy;
import com.lede.second_23.ui.activity.MainActivity;
import com.lede.second_23.ui.activity.SetActivity;
import com.lede.second_23.ui.activity.UserInfoCardActivity;
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
import static com.lede.second_23.global.GlobalConstants.VIPSTATUS;
import static com.lede.second_23.ui.activity.VIPSettingActivity.NOTOVERDUE;

/**
 * Created by ld on 17/10/26.
 */

public class PersonalFragment1 extends Fragment implements RefreshAndLoadMoreListener {

    @Bind(R.id.iv_personfragment_back)
    ImageView back;
    @Bind(R.id.iv_personfragment_set)
    ImageView toSet;

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
    @Bind(R.id.iv_personfragment_msg)
    ImageView msgClick;
    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @OnClick({R.id.ll_person_fragment_fans, R.id.ll_person_fragment_concerned, R.id.iv_personfragment_msg, R.id.iv_personfragment_set,
             R.id.personfragment_me,R.id.personfragment_dongtai, R.id.personfragment_album, R.id.iv_personfragment_back,
            R.id.ctiv_personfragment_userimg, R.id.tv_personfragment_username, R.id.tv_personfragment_sign
    })
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ctiv_personfragment_userimg:

            case R.id.tv_personfragment_username:

            case R.id.tv_personfragment_sign:
                intent = new Intent(getActivity(), UserInfoCardActivity.class);
                intent.putExtra(USERID,(String) SPUtils.get(getContext(), GlobalConstants.USERID, ""));

                startActivity(intent);
                break;

            case R.id.iv_personfragment_back:
                MainActivity.instance.vp_main_fg.setCurrentItem(1);
                break;
            case R.id.iv_personfragment_set:
                intent = new Intent(context, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_person_fragment_concerned:
                intent = new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("id", (String) SPUtils.get(context, USERID, ""));
                startActivity(intent);
                break;
            case R.id.ll_person_fragment_fans:
                intent = new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", (String) SPUtils.get(context, USERID, ""));
                startActivity(intent);
                break;
            case R.id.iv_personfragment_msg:
                startActivity(new Intent(context, ConversationListDynamicActivtiy.class));

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
    private static final int REQUEST_USER_INFO = 555;

    public static PersonalFragment1 instance = null;

    private String userId;
    private PersonalAlbumBean.DataBean.UserInfo userInfo;
    private PersonalAlbumBean.DataBean dataBean;
    private int currentPage = 0;//当前页 初始默认为0

    private PersonalFragmentAlbum personalFragmentAlbum;
    private PersonalFragmentAllBlog personalFragmentAllBlog;

    private PersonalFragmentMe personalFragmentMe;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;

    private Request<String> userInfoRequest = null;

    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);


        ButterKnife.bind(this, view);

        instance=this;
        mGson = new Gson();
        context = getActivity();
        String vipStatus= (String) SPUtils.get(context,VIPSTATUS,"");
        if(vipStatus.equals(NOTOVERDUE)){
            vipLogo.setVisibility(View.VISIBLE);
        }else{
            vipLogo.setVisibility(View.GONE);
        }

        initView();
        initEvent();


        mRefreshLayout.setEnableLoadmore(false);

        return view;
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





        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
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
                        parseUserInfo(response.get());
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
                    default:
                        break;
                }
            }
        };
        userId = (String) SPUtils.get(getContext(), USERID, "");
        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        userInfoRequest.add("userId", userId);
        userInfoRequest.add("pageNum", 1);
        userInfoRequest.add("pageSize", 1);

        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);


    }


    private void parseUserInfo(String s) {
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(s, PersonalAlbumBean.class);

        userInfo = personalAlbumBean.getData().getUserInfo().get(0);
        dataBean=personalAlbumBean.getData();

        setUserInfo();
    }

    public void setUserInfo() {
        userName.setText(userInfo.getNickName());
        if (userInfo.getSex().equals("男")) {

            boyOrGirl.setSelected(false);
        } else {
            boyOrGirl.setSelected(true);
        }
        userSign.setText(userInfo.getNote());
        Glide.with(getContext())
                .load(userInfo.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(userImg);
        followingsNum.setText(dataBean.getFriendsCount() + "");
        fansNum.setText(dataBean.getFollowersCount() + "");
    }

    @Override
    public void isOver() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }


    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.isRefreshing();
        doRequest();
    }
}
