package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/10/24.
 */

public class NearbyUsersBean {

    private DataBean data;
    private String msg;
    private int result;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public class DataBean{

        private List<UserPhotoListBean> userPhotoList;
        private List<UserInfoListBean> userInfoList;


        public List<UserPhotoListBean> getUserPhotoList() {
            return userPhotoList;
        }

        public void setUserPhotoList(List<UserPhotoListBean> userPhotoList) {
            this.userPhotoList = userPhotoList;
        }

        public List<UserInfoListBean> getUserInfoList() {
            return userInfoList;
        }

        public void setUserInfoList(List<UserInfoListBean> userInfoList) {
            this.userInfoList = userInfoList;
        }

       public class UserPhotoListBean {

            String creatTime;
            String id;
            String photoId;
            String urlFirst;
            String urlHead;
            String urlImg;
            String urlVideo;
            String userId;

            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhotoId() {
                return photoId;
            }

            public void setPhotoId(String photoId) {
                this.photoId = photoId;
            }

            public String getUrlFirst() {
                return urlFirst;
            }

            public void setUrlFirst(String urlFirst) {
                this.urlFirst = urlFirst;
            }

            public String getUrlHead() {
                return urlHead;
            }

            public void setUrlHead(String urlHead) {
                this.urlHead = urlHead;
            }

            public String getUrlImg() {
                return urlImg;
            }

            public void setUrlImg(String urlImg) {
                this.urlImg = urlImg;
            }

            public String getUrlVideo() {
                return urlVideo;
            }

            public void setUrlVideo(String urlVideo) {
                this.urlVideo = urlVideo;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

       public  class UserInfoListBean {
            private String address;
            private int followersCount;
            private int friendsCount;
            private String hobby;
            private String hometowm;
            private String imgUrl;
            private String nickName;
            private String note;
            private String registerTime;
            private String sex;
            private String trueName;
            private String userId;
            private String wechat;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getFollowersCount() {
                return followersCount;
            }

            public void setFollowersCount(int followersCount) {
                this.followersCount = followersCount;
            }

            public int getFriendsCount() {
                return friendsCount;
            }

            public void setFriendsCount(int friendsCount) {
                this.friendsCount = friendsCount;
            }

            public String getHobby() {
                return hobby;
            }

            public void setHobby(String hobby) {
                this.hobby = hobby;
            }

            public String getHometowm() {
                return hometowm;
            }

            public void setHometowm(String hometowm) {
                this.hometowm = hometowm;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getRegisterTime() {
                return registerTime;
            }

            public void setRegisterTime(String registerTime) {
                this.registerTime = registerTime;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getTrueName() {
                return trueName;
            }

            public void setTrueName(String trueName) {
                this.trueName = trueName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getWechat() {
                return wechat;
            }

            public void setWechat(String wechat) {
                this.wechat = wechat;
            }
        }


    }


}
