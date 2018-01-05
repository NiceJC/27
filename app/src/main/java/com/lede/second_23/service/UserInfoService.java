package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.bean.PersonalAlbumBean;
import com.lede.second_23.bean.UpUserInfoBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import java.io.File;

import static com.lede.second_23.global.GlobalConstants.ADDRESS;
import static com.lede.second_23.global.GlobalConstants.USERNAME;
import static com.lede.second_23.global.GlobalConstants.USER_SEX;

/**
 *
 *
 * Created by ld on 17/11/21.
 */

public class UserInfoService {

    private static final int REQUEST_USER_INFO = 555;
    private static final int UPDATE_USER_INFO=2311;
    private static final int CREATE_USER=124179;
    private static final int UPDATE_USER_IMG=23141;



    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String>  updateHeadImgRequest=null;
    private Request<String> updateUserInfoRequest=null;
    private Request<String> createUserInfoRequest=null;
    private Request<String> userInfoRequest = null;

    private PersonalAlbumBean.DataBean.UserInfo userInfo;

    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public UserInfoService() {

    }

    public UserInfoService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();

        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {

                    case REQUEST_USER_INFO:
                        parseUserInfo(response.get());
                        break;
                    case UPDATE_USER_IMG:
                        parseUpdateUserImg(response.get());
                        break;
                    case UPDATE_USER_INFO:
                        parseUpdateUserInfo(response.get());
                        break;
                    case CREATE_USER:
                        parseCreateUser(response.get());
                        break;


                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                if(mActivity.isDestroyed()){
                    return;
                }
                switch (what) {
                    case REQUEST_USER_INFO:
                    case UPDATE_USER_IMG:
                    case UPDATE_USER_INFO:
                    case CREATE_USER:
                        myCallBack.onFail(response.get().toString());
                        break;

                    default:
                        break;
                }
            }
        };
    }



    //获取用户信息  需要userId 和一个带参的回调接口
    public void getUserInfo(String userId, final MyCallBack myCallBack){

        this.userId=userId;
        this.myCallBack = myCallBack;


        userInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/showUserPhotoHome", RequestMethod.POST);
        userInfoRequest.add("userId", userId);
        userInfoRequest.add("pageNum", 1);
        userInfoRequest.add("pageSize", 1);

        RequestServer.getInstance().request(REQUEST_USER_INFO, userInfoRequest, simpleResponseListener);

    }

    public void updateUserImg(String img,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        updateHeadImgRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/changeImg", RequestMethod.POST);
        updateHeadImgRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        updateHeadImgRequest.add("pic", new File(img));
        RequestServer.getInstance().request(UPDATE_USER_IMG, updateHeadImgRequest, simpleResponseListener);

    }


    /**
     *
     * 当用户未更改用户名的时候，不能提交nickName字段，否则会报错
     * {"msg":"用户名已经存在","result":10019}
     */
    public void updateUserInfo(
            boolean hasChangedUserName,
            String currentUserName,
            String currentUserInfo,
            String currentUserSex,
            String currentUserAge,
            String currentUserCity,
            String currentUserHobby,
            String currentUserSchool,
            MyCallBack myCallBack
            ){

        this.myCallBack=myCallBack;
        updateUserInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/update", RequestMethod.POST);
        updateUserInfoRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        updateUserInfoRequest.add("wechat", currentUserHobby);
        updateUserInfoRequest.add("address", currentUserCity);
//        updateUserInfoRequest.add("qq", tv_edit_information_activity_constellation.getText().toString().trim());
        updateUserInfoRequest.add("note", currentUserInfo);
//        updateUserRequest.add("hometown",tv_edit_information_activity_marry.getText().toString().trim());
        updateUserInfoRequest.add("hobby", currentUserAge);
        if (hasChangedUserName) {
        updateUserInfoRequest.add("nickName", currentUserName);
        }
        updateUserInfoRequest.add("sex", currentUserSex.equals("男"));
        updateUserInfoRequest.add("hometown", currentUserSchool);
        RequestServer.getInstance().request(UPDATE_USER_INFO, updateUserInfoRequest, simpleResponseListener);




    }


    public void createUserInfo(

            String selectedImg,
            String currentUserName,
            String currentUserInfo,
            String currentUserSex,
            String currentUserAge,
            String currentUserCity,
            String currentUserHobby,
            String currentUserSchool,
            MyCallBack myCallBack
        ){

        this.myCallBack=myCallBack;
        createUserInfoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/users/create", RequestMethod.POST);
        createUserInfoRequest.add("pic", new File(selectedImg));
        createUserInfoRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        createUserInfoRequest.add("wechat", currentUserHobby);
        createUserInfoRequest.add("address", currentUserCity);
        createUserInfoRequest.add("note", currentUserInfo);
        createUserInfoRequest.add("hobby", currentUserAge);
        createUserInfoRequest.add("nickName", currentUserName);
        createUserInfoRequest.add("sex", currentUserSex.equals("男"));
        createUserInfoRequest.add("hometown", currentUserSchool);
        RequestServer.getInstance().request(CREATE_USER, createUserInfoRequest, simpleResponseListener);



    }





    //每次请求用户信息的时候，顺便存一波到本地
    private void parseUserInfo(String s) {
        PersonalAlbumBean personalAlbumBean = mGson.fromJson(s, PersonalAlbumBean.class);
        userInfo = personalAlbumBean.getData().getUserInfo().get(0);
        if(userInfo!=null){


            SPUtils.put(mActivity,ADDRESS,userInfo.getAddress());
            SPUtils.put(mActivity,USERNAME, userInfo.getNickName());
            SPUtils.put(mActivity,USER_SEX,userInfo.getSex());
            myCallBack.onSuccess(userInfo);
        }

    }

    private void parseUpdateUserImg(String s){

        MsgBean messageBean=mGson.fromJson(s,MsgBean.class);
        if(messageBean.getResult()==10000){
            myCallBack.onSuccess(s);
        }else{
            myCallBack.onFail(messageBean.getMsg());
        }
    }


    private void parseUpdateUserInfo(String s){
        MsgBean messageBean=mGson.fromJson(s,MsgBean.class);
        if(messageBean.getResult()==10000){
            myCallBack.onSuccess(s);
        }else{
            myCallBack.onFail(messageBean.getMsg());
        }

    }

    private void parseCreateUser(String s){
        Gson mGson = new Gson();
        UpUserInfoBean upUserInfoBean = mGson.fromJson(s, UpUserInfoBean.class);
        if (upUserInfoBean.getMsg().equals("用户没有登录")) {

            myCallBack.onFail("用户没有登录");

//            Toast.makeText(this, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, WelcomeActivity.class));
//            finish();
        } else {
            if (upUserInfoBean.getResult() == 10000) {


                myCallBack.onSuccess("保存信息成功");


//                Toast.makeText(mContext, "保存信息成功", Toast.LENGTH_SHORT).show();
//                SPUtils.put(mContext, GlobalConstants.IS_EDITINFO, true);
//
//                Intent intent = new Intent(this, WelcomeActivity.class);
//                startActivity(intent);
//                MyApplication.instance.getRongIMTokenService();

            } else if (upUserInfoBean.getResult() == 10019) {
                myCallBack.onFail("用户名已存在");


//                Toast.makeText(mContext, "用户名已存在", Toast.LENGTH_SHORT).show();
            }
        }

    }








}
