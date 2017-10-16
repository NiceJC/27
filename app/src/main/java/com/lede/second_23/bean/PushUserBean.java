package com.lede.second_23.bean;

import java.util.List;

/**
 * 系统推送的用户
 * Created by ld on 17/7/31.
 */

public class PushUserBean {


    /**
     * data : {"userInfoList":[{"address":"浙江省 杭州市","followersCount":2,"friendsCount":1,"hobby":"22","imgUrl":"http://my-photo.lacoorent.com/20170705172341353536142.jpg","nickName":"ozil","note":"：）","registerTime":"2017-06-26 20:39:01","sex":"女","trueName":"0","userId":"b5c0fb6330864dcbb51fa24803e395de","wechat":"钢琴","qq":""},{"address":"浙江省 杭州市","followersCount":4,"friendsCount":1,"hobby":"23","imgUrl":"http://my-photo.lacoorent.com/20170703015205640156848.jpg","nickName":"歪星星对抗全世界","note":"Take me to the space！","registerTime":"2017-07-03 01:52:07","sex":"男","trueName":"0","userId":"31579150f0804695a2f8a060305303c1","wechat":"钢琴"},{"address":"浙江省 台州市","followersCount":1,"friendsCount":0,"hobby":"18","imgUrl":"http://my-photo.lacoorent.com/20170706233637732213792.jpg","nickName":"池糖糖糖糖糖","registerTime":"2017-07-06 23:36:38","sex":"女","trueName":"0","userId":"36eaffb02b644fd9b9860f0592780179","wechat":"dancing"},{"address":"浙江省 杭州市","followersCount":7,"friendsCount":-2,"hobby":"23","imgUrl":"http://my-photo.lacoorent.com/20170908154906500359706.jpg","nickName":"RSTSXX","note":"halo！！！","qq":"","registerTime":"2017-06-13 18:09:24","sex":"男","trueName":"0","userId":"80cb649c9dab48088802a340ce23ee0b","wechat":"音乐"}]}
     * msg : 请求成功
     * result : 10000
     */

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

    public static class DataBean {
        private List<UserInfoListBean> userInfoList;

        public List<UserInfoListBean> getUserInfoList() {
            return userInfoList;
        }

        public void setUserInfoList(List<UserInfoListBean> userInfoList) {
            this.userInfoList = userInfoList;
        }

        public static class UserInfoListBean {
            /**
             * address : 浙江省 杭州市
             * followersCount : 2
             * friendsCount : 1
             * hobby : 22
             * imgUrl : http://my-photo.lacoorent.com/20170705172341353536142.jpg
             * nickName : ozil
             * note : ：）
             * registerTime : 2017-06-26 20:39:01
             * sex : 女
             * trueName : 0
             * userId : b5c0fb6330864dcbb51fa24803e395de
             * wechat : 钢琴
             * qq :
             */

            private String address;
            private int followersCount;
            private int friendsCount;
            private String hobby;
            private String imgUrl;
            private String nickName;
            private String note;
            private String registerTime;
            private String sex;
            private String trueName;
            private String userId;
            private String wechat;
            private String qq;

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

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }
        }
    }
}
