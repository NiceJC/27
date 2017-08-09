package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/4.
 */

public class GetReplyBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"allForum":{"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150181243649585,"creatTime":"2017-08-04 10:08:44","forumId":1501744695686205589,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":92,"noticeText":"回复123","noticeType":2,"replyId":92,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150181243649585,"creatTime":"2017-08-04 10:07:17","forumId":1501744695686205589,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":90,"noticeText":"123456","noticeType":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":0,"forumId":1501232407681475146,"forumText":"发布第一次测试","latitude":"1321321","likeCount":1,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123243252187.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150123903134411,"creatTime":"2017-07-28 18:54:10","forumId":1501232407681475146,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":24,"noticeText":"回复微博评论。","noticeType":1,"replyId":30,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":4,"forumId":1501232688047399147,"forumText":"发布微博，第二次测试。9张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"9e7a060b521049bb990dedc6055b7886"},"allRecords":[{"recordOrder":7,"url":"http://my-photo.lacoorent.com/150123275346391.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/150123275303054.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":4,"url":"http://my-photo.lacoorent.com/150123275325146.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150123275300172.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":6,"url":"http://my-photo.lacoorent.com/150123275341320.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123275297627.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":8,"url":"http://my-photo.lacoorent.com/15012327535127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/150123275322779.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":5,"url":"http://my-photo.lacoorent.com/150123275327465.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150123899876379,"creatTime":"2017-07-28 18:53:14","forumId":1501232688047399147,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":23,"noticeText":"评论微博评论","noticeType":1,"replyId":29,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}}],"nextPage":0,"pageNum":1,"pageSize":20,"total":13}}
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
         * simple : {"hasNextPage":false,"list":[{"allForum":{"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150181243649585,"creatTime":"2017-08-04 10:08:44","forumId":1501744695686205589,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":92,"noticeText":"回复123","noticeType":2,"replyId":92,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150181243649585,"creatTime":"2017-08-04 10:07:17","forumId":1501744695686205589,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":90,"noticeText":"123456","noticeType":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":0,"forumId":1501232407681475146,"forumText":"发布第一次测试","latitude":"1321321","likeCount":1,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123243252187.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150123903134411,"creatTime":"2017-07-28 18:54:10","forumId":1501232407681475146,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":24,"noticeText":"回复微博评论。","noticeType":1,"replyId":30,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":4,"forumId":1501232688047399147,"forumText":"发布微博，第二次测试。9张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"9e7a060b521049bb990dedc6055b7886"},"allRecords":[{"recordOrder":7,"url":"http://my-photo.lacoorent.com/150123275346391.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/150123275303054.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":4,"url":"http://my-photo.lacoorent.com/150123275325146.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150123275300172.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":6,"url":"http://my-photo.lacoorent.com/150123275341320.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123275297627.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":8,"url":"http://my-photo.lacoorent.com/15012327535127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/150123275322779.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":5,"url":"http://my-photo.lacoorent.com/150123275327465.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150123899876379,"creatTime":"2017-07-28 18:53:14","forumId":1501232688047399147,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":23,"noticeText":"评论微博评论","noticeType":1,"replyId":29,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}}],"nextPage":0,"pageNum":1,"pageSize":20,"total":13}
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
             * list : [{"allForum":{"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150181243649585,"creatTime":"2017-08-04 10:08:44","forumId":1501744695686205589,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":92,"noticeText":"回复123","noticeType":2,"replyId":92,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150181243649585,"creatTime":"2017-08-04 10:07:17","forumId":1501744695686205589,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":90,"noticeText":"123456","noticeType":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":0,"forumId":1501232407681475146,"forumText":"发布第一次测试","latitude":"1321321","likeCount":1,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"allRecords":[{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123243252187.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150123903134411,"creatTime":"2017-07-28 18:54:10","forumId":1501232407681475146,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":24,"noticeText":"回复微博评论。","noticeType":1,"replyId":30,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}},{"allForum":{"clickCount":4,"forumId":1501232688047399147,"forumText":"发布微博，第二次测试。9张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"9e7a060b521049bb990dedc6055b7886"},"allRecords":[{"recordOrder":7,"url":"http://my-photo.lacoorent.com/150123275346391.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":2,"url":"http://my-photo.lacoorent.com/150123275303054.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":4,"url":"http://my-photo.lacoorent.com/150123275325146.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150123275300172.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":6,"url":"http://my-photo.lacoorent.com/150123275341320.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150123275297627.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":8,"url":"http://my-photo.lacoorent.com/15012327535127.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/150123275322779.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":5,"url":"http://my-photo.lacoorent.com/150123275327465.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"commentId":150123899876379,"creatTime":"2017-07-28 18:53:14","forumId":1501232688047399147,"nUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","noticeId":23,"noticeText":"评论微博评论","noticeType":1,"replyId":29,"toUserId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 20
             * total : 13
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
                 * allForum : {"clickCount":3,"forumId":1501744695686205589,"forumText":"表情1张","latitude":"1321321","likeCount":0,"longitude":"4654231","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}
                 * allRecords : [{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150174471097474378.jpeg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}]
                 * commentId : 150181243649585
                 * creatTime : 2017-08-04 10:08:44
                 * forumId : 1501744695686205589
                 * nUserId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
                 * noticeId : 92
                 * noticeText : 回复123
                 * noticeType : 2
                 * replyId : 92
                 * toUserId : 9e7a060b521049bb990dedc6055b7886
                 * userInfo : {"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag"}
                 */

                private AllForumBean allForum;
                private long commentId;
                private String creatTime;
                private long forumId;
                private String nUserId;
                private int noticeId;
                private String noticeText;
                private int noticeType;
                private int replyId;
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

                public int getReplyId() {
                    return replyId;
                }

                public void setReplyId(int replyId) {
                    this.replyId = replyId;
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
                     * clickCount : 3
                     * forumId : 1501744695686205589
                     * forumText : 表情1张
                     * latitude : 1321321
                     * likeCount : 0
                     * longitude : 4654231
                     * type : 0
                     * userId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
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
                     * imgUrl : http://my-photo.lacoorent.com/20170627160236786746479.jpg
                     * nickName : Ag
                     */

                    private String imgUrl;
                    private String nickName;

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
                }

                public static class AllRecordsBean {
                    /**
                     * recordOrder : 0
                     * url : http://my-photo.lacoorent.com/150174471097474378.jpeg
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
