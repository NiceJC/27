package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/4.
 */

public class GetReplyBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"allForum":{"clickCount":1,"forumId":1504173616513071187,"forumText":"西湖","latitude":"30.303847","likeCount":1,"longitude":"120.369066","type":0,"userId":"aaad1802f1874e70a226961e2cace340"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/1504173548265001561.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/1504173549218014261.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/1504173548823043684.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/1504173549751085127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":1505227562884034,"creatTime":"2017-09-12 22:46:03","forumId":1504173616513071187,"nUserId":"aaad1802f1874e70a226961e2cace340","noticeId":72,"noticeText":"美","noticeType":0,"toUserId":"ae162a6fbffc4689b34fbc05d2a1ce38","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170903120905063447292.jpg","nickName":"TWENTY-SEVEN","trueName":"0"}}],"nextPage":0,"pageNum":1,"pageSize":20,"total":4}}
     * msg : 评论列表
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
         * simple : {"hasNextPage":false,"list":[{"allForum":{"clickCount":1,"forumId":1504173616513071187,"forumText":"西湖","latitude":"30.303847","likeCount":1,"longitude":"120.369066","type":0,"userId":"aaad1802f1874e70a226961e2cace340"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/1504173548265001561.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/1504173549218014261.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/1504173548823043684.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/1504173549751085127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":1505227562884034,"creatTime":"2017-09-12 22:46:03","forumId":1504173616513071187,"nUserId":"aaad1802f1874e70a226961e2cace340","noticeId":72,"noticeText":"美","noticeType":0,"toUserId":"ae162a6fbffc4689b34fbc05d2a1ce38","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170903120905063447292.jpg","nickName":"TWENTY-SEVEN","trueName":"0"}}],"nextPage":0,"pageNum":1,"pageSize":20,"total":4}
         */

        private SimpleBean simple;

        public SimpleBean getSimple() {
            return simple;
        }

        public void setSimple(SimpleBean simple) {
            this.simple = simple;
        }

        public static class SimpleBean {
            /**
             * hasNextPage : false
             * list : [{"allForum":{"clickCount":1,"forumId":1504173616513071187,"forumText":"西湖","latitude":"30.303847","likeCount":1,"longitude":"120.369066","type":0,"userId":"aaad1802f1874e70a226961e2cace340"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/1504173548265001561.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/1504173549218014261.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/1504173548823043684.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/1504173549751085127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":1505227562884034,"creatTime":"2017-09-12 22:46:03","forumId":1504173616513071187,"nUserId":"aaad1802f1874e70a226961e2cace340","noticeId":72,"noticeText":"美","noticeType":0,"toUserId":"ae162a6fbffc4689b34fbc05d2a1ce38","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170903120905063447292.jpg","nickName":"TWENTY-SEVEN","trueName":"0"}}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 20
             * total : 4
             */

            private boolean hasNextPage;
            private int nextPage;
            private int pageNum;
            private int pageSize;
            private int total;
            private List<ListBean> list;

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

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * allForum : {"clickCount":1,"forumId":1504173616513071187,"forumText":"西湖","latitude":"30.303847","likeCount":1,"longitude":"120.369066","type":0,"userId":"aaad1802f1874e70a226961e2cace340"}
                 * allRecords : [{"recordOrder":0,"url":"http://my-photo.lacoorent.com/1504173548265001561.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/1504173549218014261.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/1504173548823043684.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/1504173549751085127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}]
                 * commentId : 1505227562884034
                 * creatTime : 2017-09-12 22:46:03
                 * forumId : 1504173616513071187
                 * nUserId : aaad1802f1874e70a226961e2cace340
                 * noticeId : 72
                 * noticeText : 美
                 * noticeType : 0
                 * toUserId : ae162a6fbffc4689b34fbc05d2a1ce38
                 * userInfo : {"imgUrl":"http://my-photo.lacoorent.com/20170903120905063447292.jpg","nickName":"TWENTY-SEVEN","trueName":"0"}
                 */

                private AllForumBean allForum;
                private long commentId;
                private String creatTime;
                private long forumId;
                private String nUserId;
                private int noticeId;
                private String noticeText;
                private int noticeType;
                private String toUserId;
                private UserInfoBean userInfo;
                private List<AllRecordsBean> allRecords;

                public AllForumBean getAllForum() {
                    return allForum;
                }

                public void setAllForum(AllForumBean allForum) {
                    this.allForum = allForum;
                }

                public long getCommentId() {
                    return commentId;
                }

                public void setCommentId(long commentId) {
                    this.commentId = commentId;
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

                public String getNUserId() {
                    return nUserId;
                }

                public void setNUserId(String nUserId) {
                    this.nUserId = nUserId;
                }

                public int getNoticeId() {
                    return noticeId;
                }

                public void setNoticeId(int noticeId) {
                    this.noticeId = noticeId;
                }

                public String getNoticeText() {
                    return noticeText;
                }

                public void setNoticeText(String noticeText) {
                    this.noticeText = noticeText;
                }

                public int getNoticeType() {
                    return noticeType;
                }

                public void setNoticeType(int noticeType) {
                    this.noticeType = noticeType;
                }

                public String getToUserId() {
                    return toUserId;
                }

                public void setToUserId(String toUserId) {
                    this.toUserId = toUserId;
                }

                public UserInfoBean getUserInfo() {
                    return userInfo;
                }

                public void setUserInfo(UserInfoBean userInfo) {
                    this.userInfo = userInfo;
                }

                public List<AllRecordsBean> getAllRecords() {
                    return allRecords;
                }

                public void setAllRecords(List<AllRecordsBean> allRecords) {
                    this.allRecords = allRecords;
                }

                public static class AllForumBean {
                    /**
                     * clickCount : 1
                     * forumId : 1504173616513071187
                     * forumText : 西湖
                     * latitude : 30.303847
                     * likeCount : 1
                     * longitude : 120.369066
                     * type : 0
                     * userId : aaad1802f1874e70a226961e2cace340
                     */

                    private int clickCount;
                    private long forumId;
                    private String forumText;
                    private String latitude;
                    private int likeCount;
                    private String longitude;
                    private int type;
                    private String userId;

                    public int getClickCount() {
                        return clickCount;
                    }

                    public void setClickCount(int clickCount) {
                        this.clickCount = clickCount;
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

                    public String getUserId() {
                        return userId;
                    }

                    public void setUserId(String userId) {
                        this.userId = userId;
                    }
                }

                public static class UserInfoBean {
                    /**
                     * imgUrl : http://my-photo.lacoorent.com/20170903120905063447292.jpg
                     * nickName : TWENTY-SEVEN
                     * trueName : 0
                     */

                    private String imgUrl;
                    private String nickName;
                    private String trueName;

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

                    public String getTrueName() {
                        return trueName;
                    }

                    public void setTrueName(String trueName) {
                        this.trueName = trueName;
                    }
                }

                public static class AllRecordsBean {
                    /**
                     * recordOrder : 0
                     * url : http://my-photo.lacoorent.com/1504173548265001561.jpg
                     * urlThree : http://my-photo.lacoorent.com/null
                     * urlTwo : http://my-photo.lacoorent.com/null
                     */

                    private int recordOrder;
                    private String url;
                    private String urlThree;
                    private String urlTwo;

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
                }
            }
        }
    }
}
