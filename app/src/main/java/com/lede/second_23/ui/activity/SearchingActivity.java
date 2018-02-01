package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lede.second_23.R;
import com.lede.second_23.bean.SearchingResultBean;
import com.lede.second_23.bean.TopicItemsBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.FindMoreService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.UiUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.lede.second_23.global.GlobalConstants.TOPICID;
import static com.lede.second_23.global.GlobalConstants.TOPICITEMID;
import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/10/18.
 */

public class SearchingActivity extends BaseActivity {

    @Bind(R.id.searching_back)
    ImageView back;

    @Bind(R.id.searching_button)
    ImageView goSearching;

    @Bind(R.id.searching_text)
    EditText editText;

    @Bind(R.id.searching_result)
    RecyclerView mRecyclerView;

    @Bind(R.id.when_no_data)
    TextView whenNoData;

    @Bind(R.id.type_man)
    ImageView typeMan;
    @Bind(R.id.type_topic)
    ImageView typeTopic;

    private ArrayList<String> keyWordsList = new ArrayList<>();
    private Set<String> wordsSet;

    private RequestManager requestManager;

    private List<Object> objectList=new ArrayList<>();

    private MultiItemTypeAdapter mAdapter;
    private FindMoreService findMoreService;
    private UserInfoService userInfoService;
    private Activity context;

    private MultiTransformation transformation;
    private boolean isSearchingUsers=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);


        requestManager= Glide.with(this);
        context=this;
        typeMan.setSelected(true);


        initView();
        initEvent();
        getWordsFromSP();

    }

    @OnClick({R.id.searching_back,R.id.searching_button,R.id.type_man,R.id.type_topic})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.searching_back:
                finish();
                break;
            case R.id.search_button:
                String keyWord=editText.getText().toString();

                goSearching(keyWord);

                break;
            case R.id.type_man:
                isSearchingUsers=true;
                typeMan.setSelected(true);
                typeTopic.setSelected(false);
                goSearching(editText.getText().toString());
                break;
            case R.id.type_topic:
                isSearchingUsers=false;
                typeMan.setSelected(false);
                typeTopic.setSelected(true);
                goSearching(editText.getText().toString());

                break;
            default:
                break;
        }


    }







    private void initView() {
        transformation = new MultiTransformation(
                new CenterCrop(context),
                new RoundedCornersTransformation(context, UiUtils.dip2px(5), 0, RoundedCornersTransformation.CornerType.ALL)
        );
        findMoreService=new FindMoreService(context);
        userInfoService=new UserInfoService(context);


        mAdapter=new MultiItemTypeAdapter(this,objectList);
        mAdapter.addItemViewDelegate(new HistoryDelegate());
        mAdapter.addItemViewDelegate(new TopicResultDelegate());
        mAdapter.addItemViewDelegate(new UserResultDelegate());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);

    }


    public void goSearching(String mkName){
        if(mkName==null||mkName.equals("")){
            return;
        }
       if(isSearchingUsers){
           goSearchingUsers(mkName);
       } else{
           goSearchingTopics(mkName);
       }
    }


    public class HistoryDelegate implements  ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.searching_history_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(ViewHolder holder, Object o, final int position) {
            final String keyWord= (String) o;
            TextView textView=holder.getView(R.id.searching_list_item_text);
            ImageView delete=holder.getView(R.id.searching_list_item_delete);
            textView.setText(keyWord);
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setText(keyWord);
                    goSearching(keyWord);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteWordFromSP(keyWord);
                    objectList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    public class UserResultDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.searching_result_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof SearchingResultBean.DataBean.UserBean;
        }

        @Override
        public void convert(ViewHolder holder, Object o, final int position) {
            final SearchingResultBean.DataBean.UserBean userBean= (SearchingResultBean.DataBean.UserBean) o;
            ImageView imageView=holder.getView(R.id.user_img);
            TextView textView=holder.getView(R.id.user_name);

            requestManager.load(userBean.getImgUrl())
                    .bitmapTransform(new CropCircleTransformation(SearchingActivity.this))
                    .into(imageView);
            textView.setText(userBean.getNickName());

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveWordsToSP(userBean.getNickName());
                    Intent intent=new Intent(SearchingActivity.this,UserInfoActivty.class);
                    intent.putExtra(USERID,userBean.getUserId());
                    startActivity(intent);



                }
            });

        }
    }

    public class TopicResultDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_topic_content;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof TopicItemsBean.DataBean.SimpleBean.ListBean;
        }

        @Override
        public void convert(ViewHolder holder, Object o, final int position) {
            final TopicItemsBean.DataBean.SimpleBean.ListBean topicItem= (TopicItemsBean.DataBean.SimpleBean.ListBean) o;

            ImageView imageView=holder.getView(R.id.image);
            TextView textView=holder.getView(R.id.text);
            requestManager.load(topicItem.getUrlOne()).bitmapTransform(transformation).into(imageView);
            textView.setText(topicItem.getDetailsName());

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveWordsToSP(topicItem.getDetailsName());
                    Intent intent=new Intent(context, TopicItemDetailActivity.class);
                    intent.putExtra(TOPICID,topicItem.getUuid());
                    intent.putExtra(TOPICITEMID,topicItem.getUuidBySub());
                    startActivity(intent);

                }
            });

        }
    }



    public void initEvent(){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord=editText.getText().toString();
                goSearching(keyWord);

            }
        });


        getWordsFromSP();
    }

    public void goSearchingUsers(String keyWord){


        userInfoService.requestUserByNmae(keyWord, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                List<SearchingResultBean.DataBean.UserBean> list= (List<SearchingResultBean.DataBean.UserBean>) o;
                objectList.clear();
                objectList.addAll(list);
                mAdapter.notifyDataSetChanged();
                whenNoData.setVisibility(View.GONE);
                if(list.size()==0){
                    whenNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String mistakeInfo) {
                objectList.clear();
                mAdapter.notifyDataSetChanged();
                whenNoData.setVisibility(View.VISIBLE);

            }
        });
    }

    public void goSearchingTopics(String keyWord){

        findMoreService.requestTopicItemsByName(keyWord, 1, 100, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {

                List<TopicItemsBean.DataBean.SimpleBean.ListBean> list = (List<TopicItemsBean.DataBean.SimpleBean.ListBean>) o;



                whenNoData.setVisibility(View.GONE);
                objectList.clear();
                objectList.addAll(list);
                mAdapter.notifyDataSetChanged();
                if(list.size()==0){
                    whenNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String mistakeInfo) {
                objectList.clear();
                mAdapter.notifyDataSetChanged();
                whenNoData.setVisibility(View.VISIBLE);

            }
        });



    }




    private void saveWordsToSP(String keyWord) {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        wordsSet = preferences.getStringSet("usedWords", new HashSet<String>());
        wordsSet.add(keyWord);
        editor.putStringSet("usedWords", wordsSet);

        editor.commit();

    }


    private void deleteWordFromSP(String keyWord){
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        wordsSet = preferences.getStringSet("usedWords", new HashSet<String>());
        wordsSet.remove(keyWord);
        editor.putStringSet("usedWords", wordsSet);

    }

    private void getWordsFromSP() {

        keyWordsList.clear();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        wordsSet = preferences.getStringSet("usedWords", new HashSet<String>());
        Iterator<String> it = wordsSet.iterator();
        while (it.hasNext()) {
            String keyWord = it.next();
            keyWordsList.add(keyWord);
        }
        objectList.clear();
        objectList.addAll(keyWordsList);
        mAdapter.notifyDataSetChanged();


    }




}
