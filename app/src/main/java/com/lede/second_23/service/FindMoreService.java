package com.lede.second_23.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.lede.second_23.bean.FootMarksBean;
import com.lede.second_23.bean.MsgBean;
import com.lede.second_23.bean.PSubject;
import com.lede.second_23.bean.TopicItemsBean;
import com.lede.second_23.bean.TopicSearchingBean;
import com.lede.second_23.bean.TopicsBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.global.RequestServer;
import com.lede.second_23.interface_utils.MyCallBack;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import static com.lede.second_23.global.GlobalConstants.USERID;

/**
 *
 * 主题 版块 足迹 增删改查
 * Created by ld on 18/1/19.
 */

public class FindMoreService  {

    private static final int CREATE_NEW_TOPIC =128394;
    private static final int CREATE_NEW_TOPIC_ITEM=183494;
    private static final int CREATE_FOOT_MARK=2847389;
    private static final int CREATE_TOPIC_ITEM_IMAGE=747389;


    private static final int REQUEST_ALL_FOOTMARK =618894;
    private static final int REQUEST_ALL_TOPICS =14994;
    private static final int REQUEST_TOPICITEMS_BY_TOPIC =1407894;
    private static final int REQUEST_TOPICITEMS_BY_CITY =140894;
    private static final int REQUEST_TOPICITEMS_BY_TOPIC_AND_CITY =1418994;

    private static final int REQUEST_SAVE_RANK=45957;
    private static final int REQUEST_TOPIC_BY_NAME=438957;

    private static final int DELETE_TOPIC=2354905;
    private static final int DELETE_TOPIC_ITEM=3498372;
    private static final int DELETE_FOOT_MARK_BY_MANAGER=237382;
    private static final int DELETE_FOOT_MARK_BY_USER=237121;



    private Gson mGson;
    private SimpleResponseListener<String> simpleResponseListener;




    private Request<String> allTopicsRequest= null;
    private Request<String> topicItemsByTopicRequest = null;
    private Request<String> topicItemsByCItyRequest= null;
    private Request<String> topicItemsByTopicAndCityRequest=null;
    private Request<String> allFootMarkRequest=null;

    private Request<String> createNewTopicItemRequest=null;
    private Request<String> createNewTopicRequest=null;
    private Request<String> createNewFootMarkRequest=null;
    private Request<String> createTopicItemImageRequest=null;

    private Request<String> topicByNameRequest=null;
    private Request<String> saveRankRequest=null;

    private Request<String> deleteTopicRequest=null;
    private Request<String> deleteTopicItemRequest=null;
    private Request<String> deleteFootMarkByManagerRequest =null;
    private Request<String> deleteFootMarkByUserRequest =null;





    private String userId;
    private MyCallBack myCallBack;
    private Activity mActivity;



    public FindMoreService() {



    }

    public FindMoreService(Activity activity) {
        this.mActivity = activity;
        mGson=new Gson();
        simpleResponseListener = new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(mActivity.isDestroyed()&&what!=CREATE_FOOT_MARK){
                    return;
                }
                switch (what) {

                    case CREATE_NEW_TOPIC:
                        parseCreateTopicItem(response.get());
                        break;
                    case CREATE_NEW_TOPIC_ITEM:
                        parseCreateTopicItem(response.get());
                        break;
                    case CREATE_FOOT_MARK:

                        parseCreateTopicItem(response.get());
                        break;
                    case CREATE_TOPIC_ITEM_IMAGE:
                        parseCreateTopicItem(response.get());
                        break;

                    case DELETE_TOPIC:
                    case DELETE_TOPIC_ITEM:
                    case DELETE_FOOT_MARK_BY_MANAGER:
                    case DELETE_FOOT_MARK_BY_USER:
                        parseCreateTopicItem(response.get());
                        break;

                    case REQUEST_ALL_TOPICS:
                        parseAllTopics(response.get());
                        break;

                    case REQUEST_TOPIC_BY_NAME:
                        parseSearchTopicItems(response.get());

                        break;
                    case REQUEST_TOPICITEMS_BY_TOPIC:
                    case REQUEST_TOPICITEMS_BY_CITY:
                    case REQUEST_TOPICITEMS_BY_TOPIC_AND_CITY:
                        parseTopicItems(response.get());
                        break;
                    case REQUEST_SAVE_RANK:
                        parseCreateTopicItem(response.get());

                        break;
                    case REQUEST_ALL_FOOTMARK:
                        parseAllFootMark(response.get());

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

                    case CREATE_NEW_TOPIC:
                    case REQUEST_ALL_TOPICS:
                    case CREATE_NEW_TOPIC_ITEM:
                    case CREATE_FOOT_MARK:
                    case REQUEST_SAVE_RANK:
                    default:

                        myCallBack.onFail("数据请求出错");
                        break;

                }
            }
        };
    }


    //创建主题
    public void createNewTopic(long topicUID,String topicName,String photoName,MyCallBack myCallBack){

        this.myCallBack=myCallBack;
        createNewTopicRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/creatSubject", RequestMethod.POST);

        PSubject pSubject = null;

        pSubject=new PSubject(topicUID,topicName,photoName);
        String str = mGson.toJson(pSubject);

        createNewTopicRequest.setDefineRequestBodyForJson(str);


        RequestServer.getInstance().request(CREATE_NEW_TOPIC, createNewTopicRequest, simpleResponseListener);


    }
    //修改主题
    public void editTopic(long topicUID,String topicName,String photoName,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        createNewTopicRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/updateSubject", RequestMethod.POST);

        PSubject pSubject = null;

        if(photoName.equals("")){
            pSubject=new PSubject(topicUID,topicName);

        }else{
            pSubject=new PSubject(topicUID,topicName,photoName);

        }
        String str = mGson.toJson(pSubject);
        createNewTopicRequest.setDefineRequestBodyForJson(str);
        RequestServer.getInstance().request(CREATE_NEW_TOPIC, createNewTopicRequest, simpleResponseListener);

    }

    //删除主题
    public void deleteTopic(int id,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        deleteTopicRequest =NoHttp.createStringRequest(GlobalConstants.URL + "/notes/delectSubject", RequestMethod.POST);

        deleteTopicRequest.add("id",id);

        RequestServer.getInstance().request(DELETE_TOPIC, deleteTopicRequest, simpleResponseListener);
    }



    //创建版块
    public void createNewTopicItem(String subjectJson,MyCallBack myCallBack){

        this.myCallBack=myCallBack;
        createNewTopicItemRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/creatNotes", RequestMethod.POST);
        createNewTopicItemRequest.setDefineRequestBodyForJson(subjectJson);


        RequestServer.getInstance().request(CREATE_NEW_TOPIC_ITEM, createNewTopicItemRequest, simpleResponseListener);
    }

    //修改版块
    public void editTopicItem(String subjectJson,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        createNewTopicItemRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/updateNotes", RequestMethod.POST);
        createNewTopicItemRequest.setDefineRequestBodyForJson(subjectJson);


        RequestServer.getInstance().request(CREATE_NEW_TOPIC_ITEM, createNewTopicItemRequest, simpleResponseListener);

    }
    //删除板块
    public void deleteTopicItem(int id,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        deleteTopicItemRequest =NoHttp.createStringRequest(GlobalConstants.URL + "/notes/delectNotes", RequestMethod.POST);

        deleteTopicItemRequest.add("id",id);

        RequestServer.getInstance().request(DELETE_TOPIC_ITEM, deleteTopicItemRequest, simpleResponseListener);
    }



    //创建足迹
    public void createNewFootMark(String json,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        createNewFootMarkRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/creatNotesByForum", RequestMethod.POST);
        createNewFootMarkRequest.setDefineRequestBodyForJson(json);
        RequestServer.getInstance().request(CREATE_FOOT_MARK, createNewFootMarkRequest, simpleResponseListener);

    }

    //管理版本删除足迹
    public void deleFootMarkByManager(int id ,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        deleteFootMarkByManagerRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/delectForum", RequestMethod.POST);

        deleteFootMarkByManagerRequest.add("id",id);

        RequestServer.getInstance().request(DELETE_FOOT_MARK_BY_MANAGER, deleteFootMarkByManagerRequest, simpleResponseListener);


    }

    //用户删除自己发布的足迹
    public void deleteFootMarkByUser(int id ,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        deleteFootMarkByUserRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/delectForumByUser", RequestMethod.POST);
        deleteFootMarkByUserRequest.add("userId", (String) SPUtils.get(mActivity,USERID,""));
        deleteFootMarkByUserRequest.add("id",id);


        RequestServer.getInstance().request(DELETE_FOOT_MARK_BY_USER, deleteFootMarkByUserRequest, simpleResponseListener);


    }


    //将商家头像保存到板块下
    public void createTopicItemBusinessImage(long topicItemID, String userId, String userImg, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        createTopicItemImageRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/creatNotesByImgInfo", RequestMethod.POST);
        createTopicItemImageRequest.add("uuidBySub",topicItemID);
        createTopicItemImageRequest.add("userId",userId);
        createTopicItemImageRequest.add("userImg",userImg);


        RequestServer.getInstance().request(CREATE_TOPIC_ITEM_IMAGE, createTopicItemImageRequest, simpleResponseListener);

    }



    //分页查询所有主题
    public void requestAllTopics(int pageNum,int PageSize,MyCallBack myCallBack){

        this.myCallBack=myCallBack;

        allTopicsRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showSubject", RequestMethod.POST);

        allTopicsRequest.add("pageNum",pageNum);
        allTopicsRequest.add("pageSize",PageSize);
        RequestServer.getInstance().request(REQUEST_ALL_TOPICS, allTopicsRequest, simpleResponseListener);


    }

    //根据名称查询主题
    public void requestTopicsByName(String name,int pageNum,int PageSize,MyCallBack myCallBack){

        this.myCallBack=myCallBack;

        topicByNameRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showSubjectNameByLike", RequestMethod.POST);
        topicByNameRequest.add("name",name);
        topicByNameRequest.add("pageNum",pageNum);
        topicByNameRequest.add("pageSize",PageSize);
        RequestServer.getInstance().request(REQUEST_TOPIC_BY_NAME, topicByNameRequest, simpleResponseListener);


    }
    //根据名称查询主题
    public void requestTopicItemsByName(String name,int pageNum,int PageSize,MyCallBack myCallBack){

        this.myCallBack=myCallBack;

        topicItemsByTopicRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showNotesBylike", RequestMethod.POST);
        topicItemsByTopicRequest.add("name",name);
        topicItemsByTopicRequest.add("pageNum",pageNum);
        topicItemsByTopicRequest.add("pageSize",PageSize);
        RequestServer.getInstance().request(REQUEST_TOPICITEMS_BY_TOPIC, topicItemsByTopicRequest, simpleResponseListener);


    }



    //分页查询主题下所有版块
    public void requestTopicItemsByTopic(long uuid, int pageNum, int PageSize, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        topicItemsByTopicRequest =NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showNotes", RequestMethod.POST);
        topicItemsByTopicRequest.add("uuid",uuid);
        topicItemsByTopicRequest.add("pageNum",pageNum);
        topicItemsByTopicRequest.add("pageSize",PageSize);
        RequestServer.getInstance().request(REQUEST_TOPICITEMS_BY_TOPIC, topicItemsByTopicRequest, simpleResponseListener);

    }

    //分页查询主题下特定城市下版块
    public void requestTopicItemsByTopicAndCity(long uuid,String city, int pageNum, int PageSize, MyCallBack myCallBack){

        this.myCallBack=myCallBack;
        topicItemsByTopicAndCityRequest =NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showNotesCityBySubId", RequestMethod.POST);
        topicItemsByTopicAndCityRequest.add("uuid",uuid);
        topicItemsByTopicAndCityRequest.add("city",city);
        topicItemsByTopicAndCityRequest.add("pageNum",pageNum);
        topicItemsByTopicAndCityRequest.add("pageSize",PageSize);
        RequestServer.getInstance().request(REQUEST_TOPICITEMS_BY_TOPIC_AND_CITY, topicItemsByTopicAndCityRequest, simpleResponseListener);




    }

    //分页查询城市下所有版块  管理版本用
    public void requestTopicItemsByCity(String city, int pageNum, int PageSize, MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        topicItemsByCItyRequest =NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showCityByNotes", RequestMethod.POST);
        topicItemsByCItyRequest.add("city",city);
        topicItemsByCItyRequest.add("pageNum",pageNum);
        topicItemsByCItyRequest.add("pageSize",PageSize);
        RequestServer.getInstance().request(REQUEST_TOPICITEMS_BY_CITY, topicItemsByCItyRequest, simpleResponseListener);


    }
    //分页查询版块下所有足迹
    public void requestAllFootMark(long topicItemID,int pageNum,int pageSize,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        allFootMarkRequest =NoHttp.createStringRequest(GlobalConstants.URL + "/notes/showForumByNotes", RequestMethod.POST);
        allFootMarkRequest.add("uuidBySub",topicItemID);
        allFootMarkRequest.add("pageNum",pageNum);
        allFootMarkRequest.add("pageSize",pageSize);
        RequestServer.getInstance().request(REQUEST_ALL_FOOTMARK, allFootMarkRequest, simpleResponseListener);


    }


    //修改主题排序
    public void saveTopicRank(long uuid, int order, MyCallBack myCallBack ){
        this.myCallBack=myCallBack;
        saveRankRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/creatSubjectByOrder", RequestMethod.POST);
        saveRankRequest.add("uuid",uuid);
        saveRankRequest.add("subOrder",order);
        RequestServer.getInstance().request(REQUEST_SAVE_RANK, saveRankRequest, simpleResponseListener);

    }
    //修改每个城市下的板块排序
    public void saveTopicItemRank(long uuidBySub,int order,MyCallBack myCallBack){
        this.myCallBack=myCallBack;
        saveRankRequest=NoHttp.createStringRequest(GlobalConstants.URL + "/notes/creatNotesByOrder", RequestMethod.POST);
        saveRankRequest.add("uuidBySub",uuidBySub);
        saveRankRequest.add("notesOrder",order);
        RequestServer.getInstance().request(REQUEST_SAVE_RANK, saveRankRequest, simpleResponseListener);


    }




    private void parseAllTopics(String json){
        TopicsBean topicsBean = mGson.fromJson(json, TopicsBean.class);
        if(topicsBean.getResult()==10000){
            myCallBack.onSuccess(topicsBean.getData().getSimple().getList());
        }else{
            myCallBack.onFail("数据请求出错");
        }
    }

    private void parseTopicItems(String json){
        TopicItemsBean topicItemsBean=mGson.fromJson(json,TopicItemsBean.class);

        if(topicItemsBean.getResult()==10000){
            myCallBack.onSuccess(topicItemsBean.getData().getSimple().getList());
        }else{
            myCallBack.onFail("数据请求出错");
        }

    }
    private void parseSearchTopicItems(String json){
        TopicSearchingBean topicItemsBean=mGson.fromJson(json,TopicSearchingBean.class);

        if(topicItemsBean.getResult()==10000){
            myCallBack.onSuccess(topicItemsBean.getData().getSimple().getList());
        }else{
            myCallBack.onFail("数据请求出错");
        }

    }
    private void parseAllFootMark(String json){
        FootMarksBean footMarksBean=mGson.fromJson(json,FootMarksBean.class);
        if(footMarksBean.getResult()==10000){
            myCallBack.onSuccess(footMarksBean);
        }else{
            myCallBack.onFail("数据请求出错");
        }


    }


    private void parsePostAlbum(String json){
        myCallBack.onSuccess("");

    }

    //{"msg":"发布主题内容成功","result":10000}
    //{"msg":"发布记录成功","result":10000}  //发布足迹
    private void parseCreateTopicItem(String s){

        MsgBean msgBean=mGson.fromJson(s,MsgBean.class);
        if(msgBean.getResult()==10000){
            myCallBack.onSuccess(msgBean.getMsg());
        }else{
            myCallBack.onFail(msgBean.getMsg());
        }

    }




}
