package com.lede.second_23.ui.activity;

import android.content.Context;
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
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.SearchingResultBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
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

/**
 * Created by ld on 17/10/18.
 */

public class SearchingActivity extends BaseActivity {

    @Bind(R.id.searching_back)
    ImageView back;

    @Bind(R.id.searching_button)
    ImageView goSearching;

    @Bind(R.id.searching_history_list)
    RecyclerView searchingHistory;

    @Bind(R.id.searching_text)
    EditText editText;

    @Bind(R.id.searching_result)
    RecyclerView searchingResult;

    @Bind(R.id.when_no_data)
    TextView whenNoData;

    @OnClick({R.id.searching_back,R.id.searching_button})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.searching_back:
                finish();
                break;
            case R.id.search_button:
                String keyWord=editText.getText().toString();
                saveWordsToSP(keyWord);
                goSearching(keyWord);

                break;
            default:
                break;
        }


    }


    private Gson mGson;
    private RequestManager requestManager;

    private CommonAdapter historyAdapter;
    private CommonAdapter resultAdapter;
    private SimpleResponseListener<String> simpleResponseListener;

    private static final int REQUEST_MATCHED_USER=616;
    private ArrayList<String> keyWordsList = new ArrayList<>();
    private List<SearchingResultBean.DataBean.UserBean> resultUsers=new ArrayList<>();
    private Set<String> wordsSet;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mGson=new Gson();
        requestManager= Glide.with(this);
        getWordsFromSP();
        initView();
        initEvent();

    }






    private void initView() {

        searchingHistory.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter=new CommonAdapter<String>(this,R.layout.searching_history_item,keyWordsList) {
            @Override
            protected void convert(ViewHolder holder, final String keyWord, final int position) {
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
                        keyWordsList.remove(position);
                        historyAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        searchingHistory.setAdapter(historyAdapter);

        searchingResult.setLayoutManager(new LinearLayoutManager(this));
        resultAdapter= new CommonAdapter<SearchingResultBean.DataBean.UserBean>(this, R.layout.searching_result_item, resultUsers) {
            @Override
            protected void convert(ViewHolder holder, final SearchingResultBean.DataBean.UserBean userBean, int position) {
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
                    }
                });
            }
        };



        searchingResult.setAdapter(resultAdapter);


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
    }

    public void goSearching(String keyWord){

        searchingHistory.setVisibility(View.GONE);
        simpleResponseListener=new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what){
                    case REQUEST_MATCHED_USER:
                        parseMatchedUser(response.get());
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onFailed(int what, Response response) {
                switch (what){
                    case REQUEST_MATCHED_USER:
                        break;
                    default:
                        break;

                }            }
        };

        Request<String> getMatchedUserRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/findUserNameLike", RequestMethod.POST);
        getMatchedUserRequest.add("access_token", (String) SPUtils.get(this, GlobalConstants.TOKEN, ""));
        getMatchedUserRequest.add("mkName",keyWord);
        RequestServer.getInstance().request(REQUEST_MATCHED_USER, getMatchedUserRequest,simpleResponseListener);

    }


    /**
     * 解析推送用户
     *
     * @param json
     */
    private void parseMatchedUser(String json) {
        resultUsers.clear();
        SearchingResultBean searchingResultBean = mGson.fromJson(json, SearchingResultBean.class);
        if(searchingResultBean.getResult()==100205){
            whenNoData.setVisibility(View.VISIBLE);
            resultAdapter.notifyDataSetChanged();
        }else{
            whenNoData.setVisibility(View.GONE);
            resultUsers.addAll(searchingResultBean.getData().getUserInfos());
            resultAdapter.notifyDataSetChanged();
        }
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
    }




}
