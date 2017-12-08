package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/12/6.
 */

public class NewMatingUserBean {


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
        private List<UserInfoListBean>  userInfoList;
        private List<UserMarried> userMarries;

        public List<UserMarried> getUserMarries() {
            return userMarries;
        }

        public void setUserMarries(List<UserMarried> userMarries) {
            this.userMarries = userMarries;
        }

        public List<UserInfoListBean> getUserInfoList() {
            return userInfoList;
        }


        public void setUserInfoList(List<UserInfoListBean> userInfoList) {
            this.userInfoList = userInfoList;


        }

        public class  UserMarried{

            private String creatTime;
            private int id;
            private String marryDesp;
            private int type;
            private String updateTime;
            private String userId;


            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMarryDesp() {
                return marryDesp;
            }

            public void setMarryDesp(String marryDesp) {
                this.marryDesp = marryDesp;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

        public class UserInfoListBean{
            private String address;
            private int followersCount;
            private int friendsCount;
            private String hobby;
            private String hometown;
            private String imgUrl;
            private String nickName;
            private String note;
            private String qq;
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

            public String getHometown() {
                return hometown;
            }

            public void setHometown(String hometown) {
                this.hometown = hometown;
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

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
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
