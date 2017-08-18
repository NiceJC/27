package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/8.
 */

public class ForumDetailHeadBean {

    /**
     * data : {"allForum":{"allRecords":[{"dspe":"0","forumId":1501232407681475146,"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123243252187.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},{"forumId":1501232407681475146,"recordOrder":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"clickCount":0,"creatTime":"2017-07-28 16:59:06","forumId":1501232407681475146,"forumText":"发布第一次测试","latitude":"1321321","like":true,"likeCount":1,"longitude":"4654231","type":0,"user":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","videoCount":2}}
     * msg : 通用微博查询
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
         * allForum : {"allRecords":[{"dspe":"0","forumId":1501232407681475146,"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123243252187.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},{"forumId":1501232407681475146,"recordOrder":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"clickCount":0,"creatTime":"2017-07-28 16:59:06","forumId":1501232407681475146,"forumText":"发布第一次测试","latitude":"1321321","like":true,"likeCount":1,"longitude":"4654231","type":0,"user":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","videoCount":2}
         */

        private AllForumBean allForum;

        public AllForumBean getAllForum() {
            return allForum;
        }

        public void setAllForum(AllForumBean allForum) {
            this.allForum = allForum;
        }

        public static class AllForumBean {
            /**
             * allRecords : [{"dspe":"0","forumId":1501232407681475146,"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123243252187.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},{"forumId":1501232407681475146,"recordOrder":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}]
             * clickCount : 0
             * creatTime : 2017-07-28 16:59:06
             * forumId : 1501232407681475146
             * forumText : 发布第一次测试
             * latitude : 1321321
             * like : true
             * likeCount : 1
             * longitude : 4654231
             * type : 0
             * user : {"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}
             * userId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
             * videoCount : 2
             */

            private int clickCount;
            private String creatTime;
            private long forumId;
            private String forumText;
            private String latitude;
            private boolean like;
            private int likeCount;
            private String longitude;
            private int type;
            private UserBean user;
            private String userId;
            private int videoCount;
            private List<AllRecordsBean> allRecords;

            public int getClickCount() {
                return clickCount;
            }

            public void setClickCount(int clickCount) {
                this.clickCount = clickCount;
            }

            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public long getForumId() {
                return forumId;
            }

            public void setForumId(long forumId) {
                this.forumId = forumId;
            }

            public String getForumText() {
                return forumText;
            }

            public void setForumText(String forumText) {
                this.forumText = forumText;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public boolean isLike() {
                return like;
            }

            public void setLike(boolean like) {
                this.like = like;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(int videoCount) {
                this.videoCount = videoCount;
            }

            public List<AllRecordsBean> getAllRecords() {
                return allRecords;
            }

            public void setAllRecords(List<AllRecordsBean> allRecords) {
                this.allRecords = allRecords;
            }

            public static class UserBean {
                /**
                 * imgUrl : http://my-photo.lacoorent.com/20170622152734253454155.jpg
                 * nickName : 某宇
                 * userId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
                 */

                private String imgUrl;
                private String nickName;
                private String userId;

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

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }
            }

            public static class AllRecordsBean {
                /**
                 * dspe : 0
                 * forumId : 1501232407681475146
                 * recordOrder : 1
                 * url : http://my-photo.lacoorent.com/150123243252187.jpg
                 * urlThree : http://my-photo.lacoorent.com/null
                 * urlTwo : http://my-photo.lacoorent.com/null
                 * userId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
                 */

                private String dspe;
                private long forumId;
                private int recordOrder;
                private String url;
                private String urlThree;
                private String urlTwo;
                private String userId;

                public String getDspe() {
                    return dspe;
                }

                public void setDspe(String dspe) {
                    this.dspe = dspe;
                }

                public long getForumId() {
                    return forumId;
                }

                public void setForumId(long forumId) {
                    this.forumId = forumId;
                }

                public int getRecordOrder() {
                    return recordOrder;
                }

                public void setRecordOrder(int recordOrder) {
                    this.recordOrder = recordOrder;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getUrlThree() {
                    return urlThree;
                }

                public void setUrlThree(String urlThree) {
                    this.urlThree = urlThree;
                }

                public String getUrlTwo() {
                    return urlTwo;
                }

                public void setUrlTwo(String urlTwo) {
                    this.urlTwo = urlTwo;
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
