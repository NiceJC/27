package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.TopicsBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.UiUtils;
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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.TOPICID;

/**
 * Created by ld on 18/1/29.
 */

public class TopicsRankActivity extends BaseActivity {


    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.save)
    TextView save;

    private Gson mGson;

    private Activity context;
    private CommonAdapter mAdapter;
    private ArrayList<TopicsBean.DataBean.SimpleBean.ListBean> topicsData = new ArrayList<>();
    private ArrayList<TopicsBean.DataBean.SimpleBean.ListBean> topicsRankData = new ArrayList<>();
    private ArrayList<TopicsBean.DataBean.SimpleBean.ListBean> topicsRestData = new ArrayList<>();


    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean isRefresh = true;

    private boolean isHasNextPage = true;

    private RequestManager requestManager;
    public MultiTransformation transformation;
    private boolean isEditing=false;

    private FindMoreService findMoreService;
    ItemTouchHelper.Callback callback;
    int curremtNum=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_rank);

        context = this;
        ButterKnife.bind(context);
        mGson = new Gson();

        initView();
        initEvent();
        toRefresh();
        mRefreshLayout.setEnableLoadmore(false);


    }


    @OnClick({R.id.back,R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:

                if(isEditing){
                    //完成排序提交修改
                    isEditing=false;
                    save.setText("排序");
                    topicsRankData.addAll(topicsRestData);
                    saveRank();
                }else{
                    //开始排序
                    isEditing=true;
                    topicsRankData.clear();
                    topicsRestData.clear();
                    topicsRestData.addAll(topicsData);
                    save.setText("完成");
                }


                break;
            default:
                break;
        }
    }


    private void initView() {
        requestManager = Glide.with(context);
        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(3) , 0, RoundedCornersTransformation.CornerType.TOP)
        );

        mRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
        findMoreService=new FindMoreService(context);


        mAdapter = new CommonAdapter<TopicsBean.DataBean.SimpleBean.ListBean>(this, R.layout.item_topic_rank, topicsData) {
            @Override
            protected void convert(ViewHolder holder, final TopicsBean.DataBean.SimpleBean.ListBean listBean, final int position) {

                ImageView imageView=holder.getView(R.id.image);
                TextView textView=holder.getView(R.id.text);
                ImageView edit=holder.getView(R.id.edit);
                final TextView rank=holder.getView(R.id.rank);
                requestManager.load(listBean.getUrlOne()).bitmapTransform(transformation).into(imageView);
                textView.setText(listBean.getPhotoName());
                if(listBean.getStauts()!=0){
                    rank.setText(listBean.getStauts()+"");

                }else{
                    rank.setText("");
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(isEditing){


                            if (topicsRankData.contains(listBean)&&topicsRankData.indexOf(listBean)==topicsRankData.size()-1) {
                                topicsRankData.remove(listBean);
                                topicsRestData.add(listBean);

                                listBean.setStauts(0);
                                rank.setText("");

                            } else {
                                topicsRankData.add(listBean);
                                topicsRestData.remove(listBean);
                                int id=topicsRankData.indexOf(listBean) + 1;
                                listBean.setStauts(id);
                                rank.setText(id+"");

                            }




                        }


                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,TopicEditActivity.class);

                        intent.putExtra(TOPICID,listBean.getUuid());
                        intent.putExtra("imageUrl",listBean.getUrlOne());
                        intent.putExtra("topicName",listBean.getPhotoName());

                        startActivity(intent);
                    }
                });
            }

        };

        //长按 弹出删除对话框
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {







            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                showDeleteDialog(topicsData.get(position).getPhotoName(),topicsData.get(position).getId());

                return false;
            }
        });





        mRecyclerView.setAdapter(mAdapter);


    }

//
//    public void checkNum(TopicsBean.DataBean.SimpleBean.ListBean listBean){
//        if(topicsRankData.contains(listBean)){
//
//
//            mAdapter.
//            topicsRankData.remove(listBean);
//            topicsRestData.add(listBean);
//
//            rank.setText("");
//
//        }else{
//            topicsRankData.add(listBean);
//            topicsRestData.remove(listBean);
//            rank.setText(topicsRankData.indexOf(topicsData.get(position))+1+"");
//
//        }
//
//
//    }

    public void showDeleteDialog(String topicName, final int topicId){

        final AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("删除主题");
        localBuilder.setIcon(R.mipmap.logo);
        localBuilder.setMessage("确认删除 " +topicName+" ?");
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                /**
                 * 确定操作
                 * */
                deleteTopic(topicId);
                paramAnonymousDialogInterface.dismiss();

            }
        });
        localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {

                paramAnonymousDialogInterface.dismiss();

            }
        });

        /***
         * 设置点击返回键不会消失
         * */
        localBuilder.setCancelable(false).create();

        localBuilder.show();
    }

    public void deleteTopic(int topicId){

        findMoreService.deleteTopic(topicId, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                toRefresh();
            }

            @Override
            public void onFail(String mistakeInfo) {
                Toast.makeText(context,"请求出错",Toast.LENGTH_SHORT).show();


            }
        });


    }


    public void saveRank(){

        FindMoreService findMoreService2=new FindMoreService(context);

        for (TopicsBean.DataBean.SimpleBean.ListBean topic:topicsRankData
                ) {

            int order=topicsRankData.indexOf(topic) + 1;
            findMoreService2.saveTopicRank(topic.getUuid(), order, new MyCallBack() {
                @Override
                public void onSuccess(Object o) {

                    toRefresh();
                }

                @Override
                public void onFail(String mistakeInfo) {

                }
            });

        }



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

    private void doRequest(int page) {


        findMoreService.requestAllTopics(1, 100, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();

                List<TopicsBean.DataBean.SimpleBean.ListBean> topics= (List<TopicsBean.DataBean.SimpleBean.ListBean>) o;

                if(topics.size()!=0){

                    for (TopicsBean.DataBean.SimpleBean.ListBean listBean:topics
                            ) {
                        listBean.setStauts(0);
                    }

                }
                topicsData.clear();
                topicsData.addAll(topics);
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFail(String mistakeInfo) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();

            }
        });




    }

}
