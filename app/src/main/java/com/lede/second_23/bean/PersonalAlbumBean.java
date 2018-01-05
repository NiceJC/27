package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/10/27.
 */

public class PersonalAlbumBean {

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

        private SimpleBean simple;

        private List<UserInfo> userInfo;
        private int friendsCount;
        private int followersCount;

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

        public SimpleBean getSimple() {
            return simple;
        }

        public void setSimple(SimpleBean simple) {
            this.simple = simple;
        }

        public List<UserInfo> getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(List<UserInfo> userInfo) {
            this.userInfo = userInfo;
        }

        public class UserInfo{

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
        public class SimpleBean{

            private boolean hasNextPage;
            private List<UserPhotoBean> list;
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

            public List<UserPhotoBean> getList() {
                return list;
            }

            public void setList(List<UserPhotoBean> list) {
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

            public class UserPhotoBean {
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

        }

        }
}
