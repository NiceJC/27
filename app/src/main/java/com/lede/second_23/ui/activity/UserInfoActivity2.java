package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.service.AlbumService;
import com.lede.second_23.service.UserInfoService;
import com.lede.second_23.ui.base.BaseActivity;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.UiUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 新版的个人资料页面，包括图片墙，以及个人资料的展示和编辑
 * Created by ld on 17/12/27.
 */

public class UserInfoActivity2 extends BaseActivity {


    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_info)
    TextView userInfo;
    @Bind(R.id.user_sex)
    TextView usetSex;
    @Bind(R.id.user_age)
    TextView userAge;
    @Bind(R.id.user_city)
    TextView userCity;
    @Bind(R.id.user_hobby)
    TextView userHobby;


    private Gson mGson;

    private Activity context;
    private MultiItemTypeAdapter mAdapter;
    private ArrayList<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> mAlbumList = new ArrayList<>();
    private ArrayList<Object> imageList=new ArrayList<>();
    private PersonalAlbumBean.DataBean.UserInfo userInfoBean;
    private int itemWidth;
    private RequestManager requestManager;

    private String userID;
    private AlbumService albumService;
    private UserInfoService userInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        context = this;
        ButterKnife.bind(context);
        requestManager = Glide.with(context);


        itemWidth = (UiUtils.getScreenWidth() / 3);

        userID = (String) SPUtils.get(context, USERID, "");


        initView();

        initEvent();


    }


    private void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));



        mAdapter=new MultiItemTypeAdapter(this,imageList);
        mAdapter.addItemViewDelegate(new HeadImgItemDelegate());
        mAdapter.addItemViewDelegate(new UserphotoItemDelegate());

//        mAdapter = new CommonAdapter<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean>(this, R.layout.item_person_fragment_show, mAlbumList) {
//
//            @Override
//            protected void convert(ViewHolder holder, PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean, int position) {
//                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
//
//                ImageView showView_show = (ImageView) holder.getView(R.id.iv_person_fragment_item_show);
//                ImageView showView_play = (ImageView) holder.getView(R.id.iv_person_fragment_item_play);
//
//                ImageView showView_photos = holder.getView(R.id.iv_person_fragment_item_photos);
//                showView_photos.setVisibility(View.GONE);
//                if (userPhotoBean.getUrlVideo().equals("http://my-photo.lacoorent.com/null")) {
//                    requestManager.load(userPhotoBean.getUrlImg())
//                            .into(showView_show);
//                    showView_play.setVisibility(View.GONE);
//
//                } else {
//                    requestManager.load(userPhotoBean.getUrlFirst())
//                            .into(showView_show);
//                    showView_play.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//        };
        mRecyclerView.setAdapter(mAdapter);

    }


    public class HeadImgItemDelegate implements ItemViewDelegate<PersonalAlbumBean.DataBean.UserInfo>{
        @Override
        public int getItemViewLayoutId() {
            return R.layout.edit_info_head_img;
        }

        @Override
        public boolean isForViewType(PersonalAlbumBean.DataBean.UserInfo item, int position) {
            return item instanceof PersonalAlbumBean.DataBean.UserInfo;
        }

        @Override
        public void convert(ViewHolder holder, PersonalAlbumBean.DataBean.UserInfo userInfo, int position) {

            ImageView imageView=holder.getView(R.id.image);
            Glide.with(context).load(userInfo.getImgUrl()).into(imageView);


        }
    }

    public class UserphotoItemDelegate implements ItemViewDelegate<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean>{
        @Override
        public int getItemViewLayoutId() {
            return R.layout.edit_info_album_item;
        }

        @Override
        public boolean isForViewType(PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean item, int position) {
            return item instanceof PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean;
        }

        @Override
        public void convert(ViewHolder holder, PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean userPhotoBean, int position) {

            ImageView image=holder.getView(R.id.image);
            ImageView delete=holder.getView(R.id.delete_image);
            delete.setVisibility(View.VISIBLE);

            Glide.with(context).load(userPhotoBean.getUrlImg()).into(image);


        }
    }


    private void initEvent() {

        albumService = new AlbumService(context);
        userInfoService = new UserInfoService(context);

        doRequest();


    }

    @OnClick({R.id.back, R.id.user_name_layout, R.id.user_info_layout, R.id.user_sex_layout,

            R.id.user_age_layout, R.id.user_city_layout, R.id.user_hobby_layout
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.user_name_layout:
                break;
            case R.id.user_info_layout:

                break;
            case R.id.user_sex_layout:
                break;

            case R.id.user_age_layout:
                break;
            case R.id.user_city_layout:
                break;
            case R.id.user_hobby_layout:
                break;
            default:
                break;
        }
    }


    private void doRequest() {

        albumService.getMyAlbum(userID, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                PersonalAlbumBean personalAlbumBean = (PersonalAlbumBean) o;
                List<PersonalAlbumBean.DataBean.SimpleBean.UserPhotoBean> list = personalAlbumBean.getData().getSimple().getList();
                userInfoBean = personalAlbumBean.getData().getUserInfo().get(0);


                imageList.clear();
                imageList.add(userInfoBean);
                imageList.addAll(list);
//                mAlbumList.clear();
//
//
//
//                mAlbumList.addAll(list);
//



                mAdapter.notifyDataSetChanged();
                setUserInfo();
            }

            @Override
            public void onFail(String mistakeInfo) {

            }
        });


    }

    private void setUserInfo() {

        userName.setText(userInfoBean.getNickName());

        userInfo.setText(userInfoBean.getNote());

        usetSex.setText(userInfoBean.getSex());

        userAge.setText(userInfoBean.getHobby());

        userCity.setText(userInfoBean.getAddress());

        userHobby.setText(userInfoBean.getWechat());


    }


}
