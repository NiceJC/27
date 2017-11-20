package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/11/16.
 */

public class SameCityUserBean {

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
        private UserInfoList userInfoList;


        public UserInfoList getUserInfoList() {
            return userInfoList;
        }

        public void setUserInfoList(UserInfoList userInfoList) {
            this.userInfoList = userInfoList;
        }

        public class UserInfoList{

            private boolean hasNextPage;
            private List<UserInfoListBean> list;
            private int nextPage;
            private int pageNum;
            private int pageSize;
            private int total;

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public List<UserInfoListBean> getList() {
                return list;
            }

            public void setList(List<UserInfoListBean> list) {
                this.list = list;
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

            public  class UserInfoListBean {
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
}
