package com.lede.second_23.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.bumptech.glide.Glide;
import com.example.myapplication.views.diyimage.DIYImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRrefreshRecyclerView;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;
import com.lede.second_23.bean.BannerBean;
import com.lede.second_23.bean.FriendBean;
import com.lede.second_23.bean.HomePagerBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.AllIssueActivity;
import com.lede.second_23.ui.activity.BilateralActivity;
import com.lede.second_23.ui.activity.ConcernActivity_2;
import com.lede.second_23.ui.activity.MainActivity;
import com.lede.second_23.ui.activity.ShowAllForumByVideoActivity;
import com.lede.second_23.ui.activity.WelcomeActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.T;
import com.lede.second_23.utils.UiUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by ld on 17/2/22.
 */

public class MainFragment extends Fragment implements View.OnClickListener, AMapLocationListener, OnResponseListener<String> {

    private static final int GET_NEAR_FORUM=1000;
    private static final int GET_FOLLOWERS=2000;
    private static final int GET_BANNER=3000;

//    private ViewPager vp_mainFragment_carousel;
    private PullToRrefreshRecyclerView rv_mainFragment_show;
    private AMapLocationClient mlocationClient;
    private double myLatitude;//纬度
    private double myLongitude;//经度

    private View v_mainFragment_line;
    private ObjectAnimator animator;
    public ImageView iv_mainFragment_person;
    private ImageView iv_mainFragment_camera;
    private Context mContext;
    private RequestQueue requestQueue;
    private int page=1;
    private Gson mGson=new Gson();
    private ArrayList<HomePagerBean.DataBean.ForumListBean> userList=new ArrayList<>();
    private ArrayList<FriendBean.DataBean> friendList=new ArrayList<>();
    private CommonAdapter mAdapter=null;
    private CommonAdapter friendAdapter;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LayoutInflater layoutInflater;
    private DIYImageView diyiv_main_head_myimg;
    private RecyclerView rv_main_head_allsmile;
    private GridLayoutManager gridLayoutManager;
    private NearbySearch mNearbySearch;
    private boolean isRefreshCompleted=true;
    public static MainFragment instance;
    private float lineLeft;

    private float lineX;
    private boolean isshowBottom=true;
    private RelativeLayout ll_mainfragment_bottom;
    private ImageView iv_mainFragment_send;
    private ImageView iv_show_video;
    private ImageView diyiv_myslef;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
        layoutInflater = LayoutInflater.from(context);
        mNearbySearch = NearbySearch.getInstance(mContext.getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout,container,false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        instance=this;
        Log.i("TAB", "onCreateView: MainFragment");
        getLocation();

        animator=new ObjectAnimator();
//        userList.clear();

        getBanner();
        mAdapter= new CommonAdapter<HomePagerBean.DataBean.ForumListBean>(mContext, R.layout.mainfragment_item, userList) {
            @Override
            protected void convert(ViewHolder holder, final HomePagerBean.DataBean.ForumListBean forumListBean, int position) {
                if (position!=0&&(position-1)%2==1) {
                    GridLayoutManager.LayoutParams pl= (GridLayoutManager.LayoutParams) holder.getConvertView().getLayoutParams();
                    pl.setMarginEnd(UiUtils.dip2px(5));
                    holder.getConvertView().setLayoutParams(pl);
                }

                DIYImageView diy_iv=holder.getView(R.id.iv_item_test);
                ImageView iv_photos=holder.getView(R.id.iv_main_fragment_item_photos);
                ImageView iv_play=holder.getView(R.id.iv_main_fragment_item_play);
//                HomePagerBean.DataBean dataBean=(HomePagerBean.DataBean)o;
                if (forumListBean.getForumMedia().getPath().equals("http://my-photo.lacoorent.com/null")) {
                    Log.i("TAB", "convert: "+forumListBean.getImgs().get(0).getUrl());
                    Glide.with(mContext)
                            .load(forumListBean.getImgs().get(0).getUrl())
                            .asBitmap()
                            .error(R.mipmap.loading)
                            .placeholder(R.mipmap.loading)
                            .into(diy_iv);
                    iv_play.setVisibility(View.GONE);
                    if (forumListBean.getImgs().size()==1) {
                        iv_photos.setVisibility(View.GONE);
                    }else {
                        iv_photos.setVisibility(View.VISIBLE);
                    }
                }else {
                    Glide.with(mContext)
                            .load(forumListBean.getForumMedia().getPic())
                            .asBitmap()
                            .error(R.mipmap.loading)
                            .placeholder(R.mipmap.loading)
                            .into(diy_iv);
                    iv_photos.setVisibility(View.GONE);
                    iv_play.setVisibility(View.VISIBLE);
                }
//                Glide.with(mContext)
//                        .load(forumListBean.getImgUrl())
//                        .asBitmap()
//                        .skipMemoryCache(true)
//                        .into(iv);

            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mContext, ConcernActivity_2.class);
                intent.putExtra("userId",userList.get(position-1).getUserId());
                mContext.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        initView(view);
        //添加头布局
        addHeadView(mAdapter);
        return view;
    }

    private void getBanner() {
        Request<String> getBannerRequest=NoHttp.createStringRequest(GlobalConstants.URL+"/banner/showBanner",RequestMethod.POST);
        requestQueue.add(GET_BANNER,getBannerRequest,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((Boolean) SPUtils.get(mContext, GlobalConstants.IS_ISSUE,false))) {
            friendList.clear();
            userList.clear();
            getLocation();
            SPUtils.put(mContext, GlobalConstants.IS_ISSUE,false);
        }
        if ((int) SPUtils.get(mContext, GlobalConstants.GETREPLY,0)>0) {

            Glide.with(mContext).load(R.mipmap.new_myself).into(diyiv_myslef);
        }else {
            Glide.with(mContext).load(R.mipmap.myself).into(diyiv_myslef);
        }
    }



    private void addHeadView(CommonAdapter myCommonAdapter) {
        //装饰者设计模式，把原来的adapter传入
        headerAndFooterWrapper = new HeaderAndFooterWrapper(myCommonAdapter);
        //添加头布局View对象
        headerAndFooterWrapper.addHeaderView(setHeadView());

        rv_mainFragment_show.getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.i("newState", "onScrollStateChanged: "+newState);
//                if (newState==0) {
//                    if (isshowBottom) {
//
//                    }
//                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                Log.i("onScrolled", "onScrolled: dx"+dx+"---->dy"+dy);
                if (dy>=30) {
                    if (isshowBottom) {
                        return;
                    }else {
                        isshowBottom=true;
                        Log.i("onScrolled", "onScrolled: 向下滑动");
                        bottom_show(0);
                    }

                }else if(dy<=-30){
                    if (isshowBottom) {
                        isshowBottom=false;
                        Log.i("onScrolled", "onScrolled: 向上滑动");
                        bottom_show(1);
                    }else {
                        return;
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
//        rv_mainFragment_show.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                switch (motionEvent.getAction()) {
////                    case MotionEvent.ACTION_MOVE:
////                        Log.i("onTouch", "onTouch: "+motionEvent.getY());
////                        break;
////                }
//                Log.i("onTouch", "onTouch: "+motionEvent.getY());
////                int action=motionEvent.getAction();
////                if (action== MotionEvent.ACTION_MOVE) {
////                    Log.i("onTouch", "onTouch: "+motionEvent.getY());
////                }
//                return false;
//            }
//        });
//        rv_mainFragment_show.getRefreshableView().onTouchEvent()
        rv_mainFragment_show.getRefreshableView().setAdapter(headerAndFooterWrapper);
        rv_mainFragment_show.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        rv_mainFragment_show.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
//                Toast.makeText(mContext, "下拉刷新", Toast.LENGTH_SHORT).show();
                isRefreshCompleted=false;
                friendList.clear();
                userList.clear();
                Glide.with(mContext)
                        .load(SPUtils.get(mContext,GlobalConstants.HEAD_IMG,""))
                        .asBitmap()
                        .error(R.mipmap.loading)
                        .placeholder(R.mipmap.loading)
                        .into(diyiv_main_head_myimg);
                getLocation();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
        headerAndFooterWrapper.notifyDataSetChanged();
        //设置上拉加载更多
//        setLoadMore();
    }

    private void bottom_show(int i) {
        animator=null;
        if (i==0) {
            animator = ObjectAnimator.ofFloat(ll_mainfragment_bottom,"translationY",0,ll_mainfragment_bottom.getHeight());
            animator.setDuration(500);
            animator.start();
        }else {
            animator = ObjectAnimator.ofFloat(ll_mainfragment_bottom,"translationY",ll_mainfragment_bottom.getHeight(),0);
            animator.setDuration(500);
            animator.start();
        }
    }

    private View setHeadView() {
        View view=layoutInflater.inflate(R.layout.layout_main_head,rv_mainFragment_show,false);
        diyiv_myslef = (ImageView) view.findViewById(R.id.diyiv_main_head_myself);
        diyiv_main_head_myimg = (DIYImageView) view.findViewById(R.id.xcriv_main_head_myimg);
        iv_show_video = (ImageView) view.findViewById(R.id.iv_main_head_show_video);
        iv_show_video.setOnClickListener(this);
        Glide.with(mContext)
                .load(SPUtils.get(mContext,GlobalConstants.HEAD_IMG,""))
                .asBitmap()
                .error(R.mipmap.loading)
                .placeholder(R.mipmap.loading)
                .into(diyiv_main_head_myimg);
//        xcriv_main_head_myimg.setImageResource(R.mipmap.ic_launcher);
        diyiv_main_head_myimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BilateralActivity.class));
            }
        });
        rv_main_head_allsmile = (RecyclerView) view.findViewById(R.id.rv_main_head_allsmile);
        rv_main_head_allsmile.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        friendAdapter= new CommonAdapter<FriendBean.DataBean>(mContext, R.layout.mainfragment_item_2, friendList) {
            @Override
            protected void convert(ViewHolder holder, FriendBean.DataBean dataBean, int position) {
                DIYImageView iv=holder.getView(R.id.iv_item_test);
                Glide.with(mContext)
                        .load(dataBean.getImgUrl())
                        .asBitmap()
                        .error(R.mipmap.loading)
                        .placeholder(R.mipmap.loading)
                        .into(iv);
            }
        } ;

        friendAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mContext, ConcernActivity_2.class);
                intent.putExtra("userId",friendList.get(position).getUserId());
                Toast.makeText(mContext, "已有人向你打招呼了\n" +
                        "点击笑脸可以互动了", Toast.LENGTH_LONG).show();
                mContext.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rv_main_head_allsmile.setAdapter(friendAdapter);
        return view;
    }

    private void userService() {
        Request<String> userRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/nearHome", RequestMethod.POST);
//        final String userId, String sex, String radius, String ageMin, String ageMax, String lon, String lat
        userRequest.add("userId", (String) SPUtils.get(mContext,GlobalConstants.USERID,""));
//        userRequest.add("userId", "ee59fb2659654db69352fd34f85d642c");
        userRequest.add("sex",(String)SPUtils.get(mContext,GlobalConstants.SET_SEX,"全部人"));
        userRequest.add("radius",((int)SPUtils.get(mContext,GlobalConstants.SET_DISTANCE,9))*1000+"");
//        Log.i("TAB", "userService: "+(float)SPUtils.get(mContext,GlobalConstants.SET_MINAGE,0.0f));
        userRequest.add("ageMin",(int)(float)SPUtils.get(mContext,GlobalConstants.SET_MINAGE,0.0f)+"");
        if ((int)(float) SPUtils.get(mContext, GlobalConstants.SET_MAXAGE,0.0f)==41) {
            userRequest.add("ageMax","99");
        }else {
            userRequest.add("ageMax",(int)(float) SPUtils.get(mContext, GlobalConstants.SET_MAXAGE,41.0f)+"");
        }
        userRequest.add("lon",myLongitude);
        userRequest.add("lat",myLatitude);
//        userRequest.add("pageNum",1);
//        userRequest.add("pageSize",20);
        requestQueue.add(GET_NEAR_FORUM,userRequest,this);
//        requestQueue.add(100, userRequest, new OnResponseListener<String>() {
//            @Override
//            public void onStart(int what) {
//                Log.i("TAB", "onStart: MainFragment");
//            }
//
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//                L.i("responseCode"+response.responseCode());
//                Log.i("TAG", "UserServiceonSucceed: "+response.get());
//                if (response.responseCode()==200) {
//                    if (!isRefreshCompleted) {
//                        rv_mainFragment_show.onRefreshComplete();
//                        isRefreshCompleted=true;
//                    }
//
//                    if (response.get()==null) {
//                        Toast.makeText(mContext, "当前地区没有附近的人发布过动态哦", Toast.LENGTH_SHORT).show();
//                    }else {
//                        parseUserJson(response.get());
//                    }
//                }else {
//                    Toast.makeText(mContext, "网络访问出错", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//            @Override
//            public void onFailed(int what, Response<String> response) {
//                Log.i("TAB", "MainFragmentOnFailed: ");
//            }
//
//            @Override
//            public void onFinish(int what) {
//                Log.i("TAB", "onFinish: MainFragment");
//            }
//        });

        /**
         * 获取收到的招呼
         */
        Request<String> followers= NoHttp.createStringRequest(GlobalConstants.URL + "/friendships/"+(String) SPUtils.get(mContext,GlobalConstants.USERID,"")+"/followers", RequestMethod.POST);
        followers.add("access_token",(String) SPUtils.get(mContext,GlobalConstants.TOKEN,""));
        followers.add("pageNum",1);
        followers.add("pageSize",100);
        requestQueue.add(GET_FOLLOWERS,followers,this);
//        requestQueue.add(200, followers, new OnResponseListener<String>() {
//            @Override
//            public void onStart(int what) {
//
//            }
//
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//                L.i("TAB",response.get());
//                parseFriendJson(response.get());
//
//            }
//
//            @Override
//            public void onFailed(int what, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFinish(int what) {
//
//            }
//        });
    }

    private void parseFriendJson(String json) {
        FriendBean friendBean=mGson.fromJson(json,FriendBean.class);
        if (friendBean.getMsg().equals("用户没有登录")) {
            Toast.makeText(mContext, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,WelcomeActivity.class));
        }else {
            if (friendBean.getResult()==10000) {
                for (int i = 0; i < friendBean.getData().size(); i++) {
                    if (!friendBean.getData().get(i).isFriend()) {
                        friendList.add(friendBean.getData().get(i));
                    }
                }
//                for (int i = 0; i < friendBean.getData().size(); i++) {
//                    if (!friendBean.getData().get(i).isFriend()) {
//                        friendList.add(friendBean.getData().get(i));
//                    }
//                }
//            friendList.addAll(friendBean.getData());
                Glide.with(mContext)
                        .load(SPUtils.get(mContext,GlobalConstants.HEAD_IMG,""))
                        .error(R.mipmap.loading)
                        .placeholder(R.mipmap.loading)
                        .into(diyiv_main_head_myimg);
                friendAdapter.notifyDataSetChanged();
            }
        }

    }

    private void parseUserJson(String json) {
        HomePagerBean homePagerBean=mGson.fromJson(json, HomePagerBean.class);
        if (homePagerBean.getMsg().equals("请求成功")) {
            if (homePagerBean.getData().getForumList().size()==0) {
                Toast.makeText(mContext, "当前地区没有附近的人发布过动态哦", Toast.LENGTH_SHORT).show();
            }else {
                userList.addAll(homePagerBean.getData().getForumList());
                headerAndFooterWrapper.notifyDataSetChanged();
            }
        }else {
            Toast.makeText(mContext, "当前地区没有附近的人发布过动态哦", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView(View view) {
//        vp_mainFragment_carousel = (ViewPager) view.findViewById(vp_mainFragment_carousel);
        iv_mainFragment_send = (ImageView)view.findViewById(R.id.iv_mainfragment_send);
        iv_mainFragment_send.setOnClickListener(this);
        rv_mainFragment_show = (PullToRrefreshRecyclerView) view.findViewById(R.id.rv_mainFragment_show);
        v_mainFragment_line =  view.findViewById(R.id.v_mainFragment_line);
        ll_mainfragment_bottom = (RelativeLayout)view.findViewById(R.id.ll_mainfragment_bottom);
        lineX=v_mainFragment_line.getTranslationX();
        lineLeft=v_mainFragment_line.getLeft();
        iv_mainFragment_person = (ImageView) view.findViewById(R.id.iv_mainFragment_person);
        iv_mainFragment_camera = (ImageView) view.findViewById(R.id.iv_mainFragment_camera);
        iv_mainFragment_person.setOnClickListener(this);
        iv_mainFragment_camera.setOnClickListener(this);
        rv_mainFragment_show.getRefreshableView().setLayoutManager(new GridLayoutManager(getActivity(),2));
//        initData();

    }


    private void initData() {
//        /**
//         * 测试用图片轮播
//         */
//        ArrayList<ImageView> iv_List=new ArrayList<>();
//        for (int i = 0; i <3 ; i++) {
//            ImageView imageView=new ImageView(getActivity());
//            imageView.setImageResource(R.mipmap.ic_launcher);
//            iv_List.add(imageView);
//        }
//        vp_mainFragment_carousel.setAdapter(new MyViewPagerAdapter(iv_List));

        /**
         * 测试用recyclerView数据
         */
//        ArrayList<String> string_List=new ArrayList<>();

//        ArrayList<Integer> mipmap_List=new ArrayList<>();
//        Random random=new Random();
//        int src[]={R.mipmap.test1, R.mipmap.test2, R.mipmap.test3, R.mipmap.test4, R.mipmap.test5, R.mipmap.test6, R.mipmap.test7, R.mipmap.test8, R.mipmap.test9};
//        for (int i = 0; i < 100; i++) {
////            string_List.add("测试数据"+(i+1));
//            mipmap_List.add(src[random.nextInt(9)]);
//        }
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spansize=1;
                if (position==0) {
                    spansize=2;
                }
                return spansize;
            }
        });
        rv_mainFragment_show.getRefreshableView().setLayoutManager(gridLayoutManager);
//        rv_mainFragment_show.setAdapter(new CommonAdapter<Integer>(getActivity(), R.layout.mainfragment_item,mipmap_List) {
//            @Override
//            protected void convert(ViewHolder holder, Integer i, int position) {
////                TextView tvTest=holder.getView(R.id.tv_item_test);
////                tvTest.setText(s);
//                ImageView imageView=holder.getView(R.id.iv_item_test);
//                Glide.with(mContext)
//                        .load()
//                        .skipMemoryCache(true)
//                        .into(camera_fragment_choosephoto);
//                imageView.setImageResource(i);
//            }
//        });
//        rv_mainFragment_show.setAdapter(mAdapter);
//        rv_mainFragment_show.addItemDecoration(new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.HORIZONTAL));
//        rv_mainFragment_show.addItemDecoration(new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.VERTICAL));
    }


    public void setLineCallBack(float f) {
//        animator.ofFloat(v_mainFragment_line,"translationX",v_mainFragment_line.getTranslationX(),v_mainFragment_line.getLeft()*f);
        animator=null;


        animator = ObjectAnimator.ofFloat(v_mainFragment_line,"translationX",lineX,lineLeft*f);
        animator.setDuration(0);
        animator.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_mainFragment_person:
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                ChildFragment.instance.vp_childFragment_ViewPager.setCurrentItem(1);
                MainActivity.instance.vp_main_fg.setCurrentItem(2);
                break;
            case R.id.iv_mainFragment_camera:
//                ViewPager viewPager=(ViewPager)getActivity().findViewById(R.id.vp_main_fg);
//                viewPager.setCurrentItem(0);
//                startActivity(new Intent(getActivity(), ForumActivity.class));
                MainActivity.instance.vp_main_fg.setCurrentItem(0);
                break;
            case R.id.iv_mainfragment_send:
                Intent intent=new Intent(getActivity(), AllIssueActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.iv_main_head_show_video:


                startActivity(new Intent(getActivity(), ShowAllForumByVideoActivity.class));
                break;
        }
    }

    /**
     * 定位方法
     */

    public void getLocation(){
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(mContext);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000*60*10);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
//        mlocationClient.startAssistantLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                myLatitude=aMapLocation.getLatitude();//获取纬度
                SPUtils.put(mContext,""+GlobalConstants.LATITUDE,myLatitude);
                myLongitude=aMapLocation.getLongitude();//获取经度
                SPUtils.put(mContext,""+GlobalConstants.LONGITUDE,myLongitude);
//                L.i("123"+SPUtils.get(mContext, GlobalConstants.LATITUDE,0.0d));

                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                Log.i("TAB", "onLocationChanged: 纬度:"+myLatitude+",经度:"+myLongitude+","+aMapLocation.getAccuracy()+"地址"+aMapLocation.getAddress());
                mlocationClient.stopLocation();
                upLocation();
                userService();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                T.showShort(MyApplication.getInstance(),"定位失败,请检查是否开启应用定位权限");
                rv_mainFragment_show.onRefreshComplete();
            }
        }
    }

    private void upLocation() {
        //构造上传位置信息
        UploadInfo loadInfo = new UploadInfo();
        //设置上传位置的坐标系支持AMap坐标数据与GPS数据
        loadInfo.setCoordType(NearbySearch.AMAP);
        //设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取
        loadInfo.setPoint(new LatLonPoint(myLatitude,myLongitude));
        //设置上传用户id
        loadInfo.setUserID((String) SPUtils.get(mContext,GlobalConstants.USERID,""));
        //调用异步上传接口
        mNearbySearch.uploadNearbyInfoAsyn(loadInfo);
        mNearbySearch.addNearbyListener(new NearbySearch.NearbyListener() {
            @Override
            public void onUserInfoCleared(int i) {

            }

            @Override
            public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int i) {

            }

            @Override
            public void onNearbyInfoUploaded(int i) {
                Log.i("TAB", "onNearbyInfoUploaded: 上报位置"+i);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocationClient.onDestroy();
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.i("TAG", "UserServiceonSucceed: "+response.get());
        switch (what) {
            case GET_NEAR_FORUM:

                if (response.responseCode()==200) {
                    if (!isRefreshCompleted) {
                        rv_mainFragment_show.onRefreshComplete();
                        isRefreshCompleted=true;
                    }

                    if (response.get()==null) {
                        Toast.makeText(mContext, "当前地区没有附近的人发布过动态哦", Toast.LENGTH_SHORT).show();
                    }else {
                        parseUserJson(response.get());
                    }
                }else {
                    Toast.makeText(mContext, "网络访问出错", Toast.LENGTH_SHORT).show();
                }
                break;
            case GET_FOLLOWERS:
                parseFriendJson(response.get());
                break;
            case GET_BANNER:
                parseBanner(response.get());
                break;
        }
    }



    @Override
    public void onFailed(int what, Response<String> response) {

    }

    @Override
    public void onFinish(int what) {

    }
    private void parseBanner(String json) {
        BannerBean bannerBean=mGson.fromJson(json,BannerBean.class);
        for (int i = 0; i < bannerBean.getData().getAllBannerList().size(); i++) {
            if (bannerBean.getData().getAllBannerList().get(i).getSort()==0) {
                Glide.with(mContext).load(bannerBean.getData().getAllBannerList().get(i).getUrlPic()).into(iv_show_video);
            }
        }
    }
}
