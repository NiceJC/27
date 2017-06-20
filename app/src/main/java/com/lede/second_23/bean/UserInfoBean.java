package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/4/13.
 */

public class UserInfoBean {

    /**
     * data : {"work":[],"edu":[],"info":{"address":"浙江省 杭州市","followersCount":0,"friendsCount":0,"hobby":"23","hometown":"感情","imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170412150441607474112.jpg","nickName":"昵称","note":"签名123","qq":"双鱼座","registerTime":"2017-04-12 15:04:45","sex":"男","userId":"9e7a060b521049bb990dedc6055b7886","wechat":"UI设计师"}}
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
        /**
         * work : []
         * edu : []
         * info : {"address":"浙江省 杭州市","followersCount":0,"friendsCount":0,"hobby":"23","hometown":"感情","imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170412150441607474112.jpg","nickName":"昵称","note":"签名123","qq":"双鱼座","registerTime":"2017-04-12 15:04:45","sex":"男","userId":"9e7a060b521049bb990dedc6055b7886","wechat":"UI设计师"}
         */

        private InfoBean info;
        private List<?> work;
        private List<?> edu;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public List<?> getWork() {
            return work;
        }

        public void setWork(List<?> work) {
            this.work = work;
        }

        public List<?> getEdu() {
            return edu;
        }

        public void setEdu(List<?> edu) {
            this.edu = edu;
        }

        public static class InfoBean {
            /**
             * address : 浙江省 杭州市
             * followersCount : 0
             * friendsCount : 0
             * hobby : 23
             * hometown : 感情
             * imgUrl : http://7xr1tb.com1.z0.glb.clouddn.com/20170412150441607474112.jpg
             * nickName : 昵称
             * note : 签名123
             * qq : 双鱼座
             * registerTime : 2017-04-12 15:04:45
             * sex : 男
             * userId : 9e7a060b521049bb990dedc6055b7886
             * wechat : UI设计师
             */

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
