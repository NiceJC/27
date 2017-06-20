package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/6/13.
 */

public class TestBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[],"nextPage":0,"pageNum":2,"pageSize":20,"total":17},"friendsCount":1,"followersCount":0,"user":{"address":"浙江省 杭州市","followersCount":0,"friendsCount":1,"hobby":"24","imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170612180233020679146.jpg","nickName":"msong27","note":"hello！","registerTime":"2016-11-22 09:22:39","sex":"男","userId":"328de4b3f7304bbca5967766c1001d22","wechat":"钢琴"},"totalTwo":3}
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
         * simple : {"hasNextPage":false,"list":[],"nextPage":0,"pageNum":2,"pageSize":20,"total":17}
         * friendsCount : 1
         * followersCount : 0
         * user : {"address":"浙江省 杭州市","followersCount":0,"friendsCount":1,"hobby":"24","imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170612180233020679146.jpg","nickName":"msong27","note":"hello！","registerTime":"2016-11-22 09:22:39","sex":"男","userId":"328de4b3f7304bbca5967766c1001d22","wechat":"钢琴"}
         * totalTwo : 3
         */

        private SimpleBean simple;
        private int friendsCount;
        private int followersCount;
        private UserBean user;
        private int totalTwo;

        public SimpleBean getSimple() {
            return simple;
        }

        public void setSimple(SimpleBean simple) {
            this.simple = simple;
        }

        public int getFriendsCount() {
            return friendsCount;
        }

        public void setFriendsCount(int friendsCount) {
            this.friendsCount = friendsCount;
        }

        public int getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(int followersCount) {
            this.followersCount = followersCount;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public int getTotalTwo() {
            return totalTwo;
        }

        public void setTotalTwo(int totalTwo) {
            this.totalTwo = totalTwo;
        }

        public static class SimpleBean {
            /**
             * hasNextPage : false
             * list : []
             * nextPage : 0
             * pageNum : 2
             * pageSize : 20
             * total : 17
             */

            private boolean hasNextPage;
            private int nextPage;
            private int pageNum;
            private int pageSize;
            private int total;
            private List<?> list;

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }

        public static class UserBean {
            /**
             * address : 浙江省 杭州市
             * followersCount : 0
             * friendsCount : 1
             * hobby : 24
             * imgUrl : http://7xr1tb.com1.z0.glb.clouddn.com/20170612180233020679146.jpg
             * nickName : msong27
             * note : hello！
             * registerTime : 2016-11-22 09:22:39
             * sex : 男
             * userId : 328de4b3f7304bbca5967766c1001d22
             * wechat : 钢琴
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
