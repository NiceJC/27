package com.lede.second_23.bean;

import java.util.List;

/**
 *
 * Created by ld on 17/4/25.
 */

public class ConcernUserInfoBean {

    /**
     * data : {"firend":false,"forumList":[{"createTime":"2017-06-27 14:10:48","forumId":339,"forumMedia":{"forumId":339,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048100790220.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048142714168.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"分享","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"end":false,"dynamic":true,"info":{"address":"浙江省 杭州市","followersCount":13,"friendsCount":-7,"hobby":"21","imgUrl":"http://my-photo.lacoorent.com/20170614154446256454050.jpg","nickName":"Axe","note":"halo","qq":"狮子座","registerTime":"2017-06-13 15:29:15","sex":"男","trueName":"0","userId":"a9460f73af0c4cb9b2f9d514b5934563","wechat":"篮球"}}
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
         * firend : false
         * forumList : [{"createTime":"2017-06-27 14:10:48","forumId":339,"forumMedia":{"forumId":339,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048100790220.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048142714168.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"分享","userId":"a9460f73af0c4cb9b2f9d514b5934563"}]
         * end : false
         * dynamic : true
         * info : {"address":"浙江省 杭州市","followersCount":13,"friendsCount":-7,"hobby":"21","imgUrl":"http://my-photo.lacoorent.com/20170614154446256454050.jpg","nickName":"Axe","note":"halo","qq":"狮子座","registerTime":"2017-06-13 15:29:15","sex":"男","trueName":"0","userId":"a9460f73af0c4cb9b2f9d514b5934563","wechat":"篮球"}
         */

        private boolean firend;
        private boolean end;
        private boolean dynamic;
        private InfoBean info;
        private List<ForumListBean> forumList;

        public boolean isFirend() {
            return firend;
        }

        public void setFirend(boolean firend) {
            this.firend = firend;
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }

        public boolean isDynamic() {
            return dynamic;
        }

        public void setDynamic(boolean dynamic) {
            this.dynamic = dynamic;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public List<ForumListBean> getForumList() {
            return forumList;
        }

        public void setForumList(List<ForumListBean> forumList) {
            this.forumList = forumList;
        }

        public static class InfoBean {
            /**
             * address : 浙江省 杭州市
             * followersCount : 13
             * friendsCount : -7
             * hobby : 21
             * imgUrl : http://my-photo.lacoorent.com/20170614154446256454050.jpg
             * nickName : Axe
             * note : halo
             * qq : 狮子座
             * registerTime : 2017-06-13 15:29:15
             * sex : 男
             * trueName : 0
             * userId : a9460f73af0c4cb9b2f9d514b5934563
             * wechat : 篮球
             */

            private String address;
            private int followersCount;
            private int friendsCount;
            private String hobby;
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

        public static class ForumListBean {
            /**
             * createTime : 2017-06-27 14:10:48
             * forumId : 339
             * forumMedia : {"forumId":339,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"}
             * imgs : [{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048100790220.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048142714168.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}]
             * like : false
             * text : 分享
             * userId : a9460f73af0c4cb9b2f9d514b5934563
             */

            private String createTime;
            private int forumId;
            private ForumMediaBean forumMedia;
            private boolean like;
            private String text;
            private String userId;
            private List<ImgsBean> imgs;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getForumId() {
                return forumId;
            }

            public void setForumId(int forumId) {
                this.forumId = forumId;
            }

            public ForumMediaBean getForumMedia() {
                return forumMedia;
            }

            public void setForumMedia(ForumMediaBean forumMedia) {
                this.forumMedia = forumMedia;
            }

            public boolean isLike() {
                return like;
            }

            public void setLike(boolean like) {
                this.like = like;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public List<ImgsBean> getImgs() {
                return imgs;
            }

            public void setImgs(List<ImgsBean> imgs) {
                this.imgs = imgs;
            }

            public static class ForumMediaBean {
                /**
                 * forumId : 339
                 * path : http://my-photo.lacoorent.com/null
                 * pic : http://my-photo.lacoorent.com/null
                 * userId : a9460f73af0c4cb9b2f9d514b5934563
                 */

                private int forumId;
                private String path;
                private String pic;
                private String userId;

                public int getForumId() {
                    return forumId;
                }

                public void setForumId(int forumId) {
                    this.forumId = forumId;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }
            }

            public static class ImgsBean {
                /**
                 * forumId : 339
                 * url : http://my-photo.lacoorent.com/20170627141048100790220.jpg
                 * userId : a9460f73af0c4cb9b2f9d514b5934563
                 */

                private int forumId;
                private String url;
                private String userId;

                public int getForumId() {
                    return forumId;
                }

                public void setForumId(int forumId) {
                    this.forumId = forumId;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
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
