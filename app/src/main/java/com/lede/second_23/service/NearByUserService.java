package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.NearbyPhotoBean;
import com.lede.second_23.bean.SameCityUserBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 * Created by ld on 17/11/24.
 */

public class NearByUserService {

    private static final int REQUEST_NEARBY_PHOTO = 11515;
    private static final int REQUEST_CITY_ALL=21212;
    private static final int REQUEST_CITY_GIRL=12111;


    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;
    private Request<String> nearbyPhotoRequest = null;
    private Request<String> cityAllRequest = null;
    private Request<String> cityGirlRequest=null;



    private String userId;
    private MyCallBack nearbyPhotoCallBack;
    private MyCallBack cityAllCallBack;
    private MyCallBack cityGirlCallBack;


    private Activity mActivity;



    public NearByUserService() {

    }

    public NearByUserService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                switch (what) {
                    case REQUEST_NEARBY_PHOTO:
                        parseNearbyPhoto(response.get());
                        break;

                    case REQUEST_CITY_ALL:
                        parseCityAll(response.get());

                        break;
                    case REQUEST_CITY_GIRL:
                        parseCityGirl(response.get());


                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailed(int what, Response response) {
                switch (what) {
                    case REQUEST_NEARBY_PHOTO:
                        nearbyPhotoCallBack.onFail(response.get().toString());
                        break;
                    case REQUEST_CITY_ALL:
                        cityAllCallBack.onFail(response.get().toString());
                        break;
                    case REQUEST_CITY_GIRL:
                        cityGirlCallBack.onFail(response.get().toString());
                        break;
                    default:
                        break;
                }
            }
        };
    }



    //请求附近（同城）的用户相册
    public void requestNearbyPhoto(String address,double myLatitude, double myLongitude,final MyCallBack myCallBack){

        this.nearbyPhotoCallBack=myCallBack;
        nearbyPhotoRequest = NoHttp.createStringRequest(GlobalConstants.URL + "/photo/newPhotoNearHome", RequestMethod.POST);

//        String token=(String) SPUtils.get(getActivity(), GlobalConstants.TOKEN, "");
//        String userId=(String) SPUtils.get(getActivity(), GlobalConstants.USERID, "");
//        String sex=(String) SPUtils.get(getActivity(), GlobalConstants.SET_SEX, "All");
//        String r=((int) SPUtils.get(getActivity(), GlobalConstants.SET_DISTANCE, 10)) * 1000 + "";
//        String agemin=(int) (float) SPUtils.get(getActivity(), GlobalConstants.SET_MINAGE, 0.0f) + "";
//        String intageMax=(int) (float) SPUtils.get(getActivity(), GlobalConstants.SET_MAXAGE, 99.0f) + "";
//        double lon=myLongitude;
//        double lat=myLatitude;
        nearbyPhotoRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
//        final String userId, String sex, String radius, String ageMin, String ageMax, String lon, String lat
        nearbyPhotoRequest.add("userId", (String) SPUtils.get(mActivity, USERID, ""));
//        userRequest.add("userId", "ee59fb2659654db69352fd34f85d642c");
        nearbyPhotoRequest.add("sex", (String) SPUtils.get(mActivity, GlobalConstants.SET_SEX, "All"));
        nearbyPhotoRequest.add("radius", ((int) SPUtils.get(mActivity, GlobalConstants.SET_DISTANCE, 10)) * 1000 + "");
//        Log.i("TAB", "userService: "+(float)SPUtils.get(mContext,GlobalConstants.SET_MINAGE,0.0f));

        nearbyPhotoRequest.set("ageMin", (int) (float) SPUtils.get(mActivity, GlobalConstants.SET_MINAGE, 0.0f) + "");

        nearbyPhotoRequest.add("ageMax", (int) (float) SPUtils.get(mActivity, GlobalConstants.SET_MAXAGE, 99.0f) + "");
        nearbyPhotoRequest.add("lon", myLongitude+"");
        nearbyPhotoRequest.add("lat", myLatitude+"");
        nearbyPhotoRequest.add("address",address);

        RequestServer.getInstance().request(REQUEST_NEARBY_PHOTO, nearbyPhotoRequest, simpleResponseListener);

    }

    public void requestCityGirl(String address,int pageNum,int pageSize,MyCallBack myCallBack){
        this.cityGirlCallBack=myCallBack;
        cityGirlRequest= NoHttp.createStringRequest(GlobalConstants.URL + "/users/findUserHomeByGirl", RequestMethod.POST);
        cityGirlRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        cityGirlRequest.add("address",address);
        cityGirlRequest.add("pageNum", pageNum);
        cityGirlRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_CITY_GIRL, cityGirlRequest, simpleResponseListener);

    }
    public void requestCityAll(String address,int pageNum,int pageSize,MyCallBack myCallBack){
        this.cityAllCallBack=myCallBack;
        cityAllRequest= NoHttp.createStringRequest(GlobalConstants.URL + "/users/findUserByAddress", RequestMethod.POST);
        cityAllRequest.add("access_token", (String) SPUtils.get(mActivity, GlobalConstants.TOKEN, ""));
        cityAllRequest.add("address",address);
        cityAllRequest.add("pageNum", pageNum);
        cityAllRequest.add("pageSize", pageSize);
        RequestServer.getInstance().request(REQUEST_CITY_ALL, cityAllRequest, simpleResponseListener);



    }



    /**
     *
     * @param s
     */
    private void parseNearbyPhoto(String s) {
        NearbyPhotoBean nearbyPhotoBean = mGson.fromJson(s, NearbyPhotoBean.class);

        if(nearbyPhotoBean.getResult()==10000){

            List<NearbyPhotoBean.DataBean.UserPhotoBean> userPhotoList=nearbyPhotoBean.getData().getUserPhotoList();

            nearbyPhotoCallBack.onSuccess(userPhotoList);
        }else{
            nearbyPhotoCallBack.onFail(null);
        }
    }

    private void parseCityAll(String s){

        SameCityUserBean sameCityUserBean = mGson.fromJson(s, SameCityUserBean.class);

        //显示列表排除掉自己
        List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list=new ArrayList<>();
        list.addAll(sameCityUserBean.getData().getUserInfoList().getList());
        if(list.size()!=0){
            Iterator<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> it=list.iterator();
            while (it.hasNext()){
                SameCityUserBean.DataBean.UserInfoList.UserInfoListBean user=it.next();
                if(user.getUserId().equals((String) SPUtils.get(mActivity, USERID, ""))){
                    it.remove();
                }
            }
        }

        cityAllCallBack.onSuccess(list);
    }

    private void parseCityGirl(String s){

        SameCityUserBean sameCityUserBean = mGson.fromJson(s, SameCityUserBean.class);

        //显示列表排除掉自己
        List<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> list=new ArrayList<>();
        list.addAll(sameCityUserBean.getData().getUserInfoList().getList());
        if(list.size()!=0){
            Iterator<SameCityUserBean.DataBean.UserInfoList.UserInfoListBean> it=list.iterator();
            while (it.hasNext()){
                SameCityUserBean.DataBean.UserInfoList.UserInfoListBean user=it.next();
                if(user.getUserId().equals((String) SPUtils.get(mActivity, USERID, ""))){
                    it.remove();
                }
            }
        }

        cityGirlCallBack.onSuccess(list);
    }
}
