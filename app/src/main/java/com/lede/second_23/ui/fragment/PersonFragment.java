package com.lede.second_23.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.adapter.PersonFragmentRVAdapter;
import com.lede.second_23.bean.PersonBean;
import com.lede.second_23.bean.PersonUtilsBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.ui.activity.ConcernOrFansActivity;
import com.lede.second_23.ui.activity.ConversationListDynamicActivtiy;
import com.lede.second_23.ui.activity.EditInformationActivity;
import com.lede.second_23.ui.activity.MyPhotoActivity;
import com.lede.second_23.ui.activity.MyVideoActivity;
import com.lede.second_23.ui.activity.SetActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.WrapContentLinearLayoutManager;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ld on 17/2/22.
 */

public class PersonFragment extends Fragment implements View.OnClickListener , LoadMoreWrapper.OnLoadMoreListener{
    private View view;
    private ImageView iv_personfragment_msgtest;
    private TextView tv_personfragment_username;
    private Context context;
    private LinearLayout ll_person_fragment_concerned;
    private LinearLayout ll_person_fragment_fans;
    public ImageView iv_personfragment_msg_test;
    private List<PersonUtilsBean.DataBean> dataList=new ArrayList<>();
    private CommonAdapter myCommonAdapter;
    private boolean isHasNextPage=true;


    private RequestQueue requestQueue;
    private int pageNum=1;
    private int pageSize=20;
    private RecyclerView rv_personfragment_show;
    private GridLayoutManager gridLayoutManager;
    private PersonFragmentRVAdapter myAdapter;
    private WrapContentLinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout srl_home;
    private LayoutInflater layoutInflater;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper<Object> loadMoreWrapper;
    private View inflate;

    private boolean isFresh=true;
    public static PersonFragment instance;
    private PersonBean personBean;
    private TextView tv_username;
    private TextView tv_sign;
    private CircleTextImageView ctiv_userimg;
    private TextView tv_converned_num;
    private TextView tv_fans_num;
    private TextView tv_tie_num;
    private ImageView iv_set;
    private ImageView iv_userSex;
    private ImageView iv_back;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.person_fragment_layout,container,false);
        instance=this;
        initView();
        Log.i("TAB", "onCreateView: PersonFragment");
//        pageNum=1;
//        dataList.clear();
////                myAdapter.notifyDataSetChanged();
//        isFresh=true;
//        srl_home.setRefreshing(isFresh);
        //获取请求队列
        requestQueue = GlobalConstants.getRequestQueue();
        loadPersonInfoService();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((Boolean) SPUtils.get(context, GlobalConstants.IS_EDITINFO,false))) {
            pageNum=1;
            dataList.clear();
//                myAdapter.notifyDataSetChanged();
            loadPersonInfoService();
            isFresh=true;
            srl_home.setRefreshing(isFresh);
            SPUtils.put(context, GlobalConstants.IS_EDITINFO,false);
        }
    }

    private void loadPersonInfoService() {
        Request<String> loadPersonInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/showHome", RequestMethod.POST);
        loadPersonInfoRequest.add("userId", (String) SPUtils.get(context, GlobalConstants.USERID, ""));
        loadPersonInfoRequest.add("pageNum",pageNum);
        loadPersonInfoRequest.add("pageSize",pageSize);
//        loadPersonInfoRequest.add("text","乐可快乐出行0123456789asdzxcvbhfgrty");
        requestQueue.add(100, loadPersonInfoRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                Log.i("TAB", "onStart: PersonFragment");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.i("TAG", "loadPersonInfoService_onSucceed: "+response.get());
                parsePersonInfoJson(response.get());

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Log.i("TAG", "loadPersonInfoService_onFailed: "+response.get());
            }

            @Override
            public void onFinish(int what) {
                Log.i("TAB", "onFinish: PersonFragment");
            }
        });

    }

    private void parsePersonInfoJson(String json) {
        Gson gson=new Gson();
        personBean= gson.fromJson(json,PersonBean.class);
        isHasNextPage=personBean.getData().getSimple().isHasNextPage();
        ArrayList<PersonBean.DataBean.SimpleBean.ListBean> list=new ArrayList<>();
        list.addAll(personBean.getData().getSimple().getList());
        String timeTag="";
        Log.i("TAG", "parsePersonInfoJson: timeTag"+timeTag+"list.size"+list.size());
//        dataList.add(null);
//        dataList.add(list.get(0));
//        for (int i = 1; i < list.size(); i++) {
//            if (timeTag.equals(list.get(i).getCreateTime().substring(0,10))) {
//                Log.i("TAG", "parsePersonInfoJson:list.get(i).getCreateTime().substring(0,10) "+list.get(i).getCreateTime().substring(0,10));
//            }else {
//                dataList.add(null);
//            }
//            dataList.add(list.get(i));
//        }
        PersonUtilsBean personUtilsBean=new PersonUtilsBean();
        ArrayList<PersonUtilsBean.DataBean> utilsList=new ArrayList<>();
        PersonUtilsBean.DataBean dataBean=new PersonUtilsBean.DataBean();
        PersonBean.DataBean.SimpleBean.ListBean listBean=new PersonBean.DataBean.SimpleBean.ListBean();
        ArrayList<PersonBean.DataBean.SimpleBean.ListBean> listBeanList=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (timeTag.equals(list.get(i).getCreateTime().substring(0,10))) {
                listBean=list.get(i);
                listBeanList.add(listBean);
            }else {
                if (dataBean!=null) {
                    dataBean=null;
                    dataBean=new PersonUtilsBean.DataBean();
                }
                if (listBean!=null) {
                    listBean=null;
                    listBean=new PersonBean.DataBean.SimpleBean.ListBean();
                }
                if (listBeanList!=null) {
                    listBeanList=null;
                    listBeanList=new ArrayList<>();
                }

                timeTag=list.get(i).getCreateTime().substring(0,10);
                dataBean.setCreateTime(timeTag);
                listBean=list.get(i);
                listBeanList.add(listBean);
                dataBean.setBeanList(listBeanList);
                utilsList.add(dataBean);
            }
        }
        personUtilsBean.setList(utilsList);
        for (int i = 0; i < personUtilsBean.getList().size(); i++) {
//            personUtilsBean.getList().get(i).getCreateTime();
            Log.i("TAF", "parsePersonInfoJson: createTime"+personUtilsBean.getList().get(i).getCreateTime());
            for (int i1 = 0; i1 < personUtilsBean.getList().get(i).getBeanList().size(); i1++) {
//                personUtilsBean.getList().get(i).getBeanList().get(i1).getForumId()
                Log.i("TAF", "parsePersonInfoJson: personUtilsBean.getList().get(i).getBeanList().get(i1).getForumId()"+personUtilsBean.getList().get(i).getBeanList().get(i1).getForumId());
            }
        }
        Log.i("TAG", "parsePersonInfoJson: "+personUtilsBean+"personUtilsBean.list.size"+personUtilsBean.getList().size());
        dataList.addAll(personUtilsBean.getList());
        if (isFresh&&SPUtils.contains(context,GlobalConstants.USERID)) {
            reFreshHead();
        }
        isFresh=false;

        srl_home.setRefreshing(isFresh);
        loadMoreWrapper.notifyDataSetChanged();
    }

    private void reFreshHead() {
        tv_username.setText(personBean.getData().getUser().getNickName());
        if (personBean.getData().getUser().getSex().equals("男")) {
            Glide.with(context)
                    .load(R.mipmap.boy)
                    .into(iv_userSex);
        }else {
            Glide.with(context)
                    .load(R.mipmap.gril)
                    .into(iv_userSex);
        }
        tv_sign.setText(personBean.getData().getUser().getNote());
        Glide.with(context)
                .load(personBean.getData().getUser().getImgUrl())
                .into(ctiv_userimg);
        SPUtils.put(context,GlobalConstants.HEAD_IMG,personBean.getData().getUser().getImgUrl());
        tv_converned_num.setText(personBean.getData().getFriendsCount()+"");
        tv_fans_num.setText(personBean.getData().getFollowersCount()+"");
//        tv_tie_num.setText(personBean.getData().getSimple().getTotal()+"");
        tv_tie_num.setText(personBean.getData().getTotalTwo()+"");
    }

    private void initView() {
        iv_back = (ImageView) view.findViewById(R.id.iv_personfragment_back);
        iv_back.setOnClickListener(this);
        iv_set = (ImageView) view.findViewById(R.id.iv_personfragment_set);
        iv_set.setOnClickListener(this);
        srl_home = (SwipeRefreshLayout) view.findViewById(R.id.srl_person_fragment_home);
        srl_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum=1;
                dataList.clear();
//                myAdapter.notifyDataSetChanged();
                loadPersonInfoService();
                isFresh=true;
                srl_home.setRefreshing(isFresh);
            }
        });
        rv_personfragment_show = (RecyclerView) view.findViewById(R.id.rv_personfragment_show);
        gridLayoutManager = new GridLayoutManager(context,3, LinearLayoutManager.VERTICAL,false);
        linearLayoutManager = new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        gridLayoutManager.set
        myAdapter=new PersonFragmentRVAdapter(context,dataList);
        rv_personfragment_show.setLayoutManager(linearLayoutManager);

        myCommonAdapter= new CommonAdapter<PersonUtilsBean.DataBean>(context, R.layout.item_person_fragment_time, dataList) {
            @Override
            protected void convert(ViewHolder holder, PersonUtilsBean.DataBean dataBean, final int position) {
                TextView tv_time =holder.getView(R.id.tv_person_fragment_item_time);
                GridLayout gl_show=holder.getView(R.id.gl_person_fragment_item_show);
                tv_time.setText(dataList.get(position-1).getCreateTime());


                int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
                gl_show.removeAllViews();
                for ( int i = 0; i < dataList.get(position-1).getBeanList().size(); i++) {
                    view = layoutInflater.inflate(R.layout.item_person_fragment_show, null);
                    final int currentI=i;
//                    showViewHolder=new ShowViewHolder(view);
                    ImageView showView_photos=(ImageView) view.findViewById(R.id.iv_person_fragment_item_photos);
                    ImageView showView_show = (ImageView) view.findViewById(R.id.iv_person_fragment_item_show);
                    ImageView showView_play = (ImageView) view.findViewById(R.id.iv_person_fragment_item_play);
                    if (dataList.get(position - 1).getBeanList().get(currentI).getImgs().size()==1) {
                        showView_photos.setVisibility(View.GONE);
                    }else {
                        showView_photos.setVisibility(View.VISIBLE);
                    }
                    showView_show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> imgurlList=new ArrayList<String>();
                            for (int i1 = 0; i1 < dataList.get(position - 1).getBeanList().get(currentI).getImgs().size(); i1++) {
                                imgurlList.add(dataList.get(position - 1).getBeanList().get(currentI).getImgs().get(i1).getUrl());
                            }
//                            ArrayList<LocalMedia> selectMeida=new ArrayList<LocalMedia>();
//                            for (int i1 = 0; i1 < dataList.get(position - 1).getBeanList().get(currentI).getImgs().size(); i1++) {
//                                LocalMedia localMedia=new LocalMedia();
//                                localMedia.setPath(dataList.get(position - 1).getBeanList().get(currentI).getImgs().get(i1).getUrl());
//                                selectMeida.add(localMedia);
//                            }
                            Intent intent=new Intent(context, MyPhotoActivity.class);
                            intent.putExtra("userId",personBean.getData().getUser().getUserId());
                            intent.putExtra("forumId",(long)dataList.get(position - 1).getBeanList().get(currentI).getForumId());
                            intent.putStringArrayListExtra("banner", imgurlList);
                            intent.putExtra("text",dataList.get(position-1).getBeanList().get(currentI).getText());
                            context.startActivity(intent);
//                            PictureConfig.getInstance().externalPicturePreview((Activity) context, "/23秒", 0, selectMeida);
                        }
                    });
                    showView_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, MyVideoActivity.class);
                            intent.putExtra("videourl",dataList.get(position-1).getBeanList().get(currentI).getForumMedia().getPath());
                            intent.putExtra("picurl",dataList.get(position-1).getBeanList().get(currentI).getForumMedia().getPic());
                            intent.putExtra("text",dataList.get(position-1).getBeanList().get(currentI).getText());
                            intent.putExtra("userId",personBean.getData().getUser().getUserId());
                            intent.putExtra("forumId",(long)dataList.get(position - 1).getBeanList().get(currentI).getForumId());
                            context.startActivity(intent);
                        }
                    });
                    if (dataList.get(position-1).getBeanList().get(i).getForumMedia().getPic().equals("http://my-photo.lacoorent.com/null")) {
                        Glide.with(context)
                                .load(dataList.get(position-1).getBeanList().get(i).getImgs().get(0).getUrl())
                                .error(R.mipmap.loading)
                                .placeholder(R.mipmap.loading)
                                .into(showView_show);
                        showView_play.setVisibility(View.GONE);

                        showView_show.setClickable(true);
                    } else {
                        Glide.with(context)
                                .load(dataList.get(position-1).getBeanList().get(i).getForumMedia().getPic())
                                .error(R.mipmap.loading)
                                .placeholder(R.mipmap.loading)
                                .into(showView_show);
                        showView_photos.setVisibility(View.GONE);
                        showView_show.setClickable(false);

                    }

                    gl_show.addView(view, width / 3, width / 3);
                }
            }
        };
        //添加头布局
        addHeadView(myCommonAdapter);


    }

    private void addHeadView(CommonAdapter myCommonAdapter) {
        //装饰者设计模式，把原来的adapter传入
        headerAndFooterWrapper = new HeaderAndFooterWrapper(myCommonAdapter);
        //添加头布局View对象
        headerAndFooterWrapper.addHeaderView(setHeadView());
        rv_personfragment_show.setAdapter(myCommonAdapter);
        headerAndFooterWrapper.notifyDataSetChanged();
        //设置上拉加载更多
        setLoadMore();
    }

    private View setHeadView() {
        View view=layoutInflater.inflate(R.layout.item_person_fragment_head,null);
        iv_userSex = (ImageView) view.findViewById(R.id.iv_person_fragment_item_sex);
        tv_username = (TextView) view.findViewById(R.id.tv_personfragment_username);
        tv_sign = (TextView) view.findViewById(R.id.tv_personfragment_sign);
        ctiv_userimg = (CircleTextImageView) view.findViewById(R.id.ctiv_personfragment_userimg);
        tv_converned_num = (TextView) view.findViewById(R.id.tv_converned_num);
        tv_fans_num = (TextView) view.findViewById(R.id.tv_fans_num);
        tv_tie_num = (TextView) view.findViewById(R.id.tv_tie_num);
        ll_person_fragment_concerned=(LinearLayout) view.findViewById(R.id.ll_person_fragment_concerned);
        ctiv_userimg.setOnClickListener(this);
        ll_person_fragment_concerned.setOnClickListener(this);
        ll_person_fragment_fans=(LinearLayout)view.findViewById(R.id.ll_person_fragment_fans);
        ll_person_fragment_fans.setOnClickListener(this);
        iv_personfragment_msg_test=(ImageView) view.findViewById(R.id.iv_personfragment_msg_test);
        iv_personfragment_msg_test.setOnClickListener(this);
        tv_personfragment_username=(TextView) view.findViewById(R.id.tv_personfragment_username);
        tv_personfragment_username.setOnClickListener(this);
        return view;
    }

    //设置recyclerView上拉加载更多
    private void setLoadMore() {
        //定义请求页数
        pageNum = 1;
        loadMoreWrapper = new LoadMoreWrapper<>(headerAndFooterWrapper);
        inflate=layoutInflater.inflate(R.layout.item_loading,null);
        loadMoreWrapper.setLoadMoreView(inflate);
        loadMoreWrapper.setOnLoadMoreListener(this);
        rv_personfragment_show.setAdapter(loadMoreWrapper);
    }

    @Override
    public void onLoadMoreRequested() {
        if (!isHasNextPage) {
            inflate.setVisibility(View.GONE);
            Toast.makeText(context, "没有更多的动态了", Toast.LENGTH_SHORT).show();
        }else {
            pageNum++;
            loadPersonInfoService();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.ctiv_personfragment_userimg:
                intent=new Intent(context, EditInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_personfragment_set:
                intent=new Intent(context, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_personfragment_username:
                intent=new Intent(context, EditInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_person_fragment_concerned:
                intent=new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type",0);
                intent.putExtra("id",(String) SPUtils.get(context,GlobalConstants.USERID,""));
                context.startActivity(intent);
                break;
            case R.id.ll_person_fragment_fans:
                intent=new Intent(context, ConcernOrFansActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("id",(String) SPUtils.get(context,GlobalConstants.USERID,""));
                context.startActivity(intent);
                break;
            case R.id.iv_personfragment_msg_test:
//                supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false);
//                Map<String, Boolean> map = new HashMap<>();
//                map.put(Conversation.ConversationType.PRIVATE.getName(), false);
//                RongIM.getInstance().startConversationList(context,map);
                startActivity(new Intent(context, ConversationListDynamicActivtiy.class));
                break;
            case R.id.iv_personfragment_back:
                ChildFragment.instance.vp_childFragment_ViewPager.setCurrentItem(0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
