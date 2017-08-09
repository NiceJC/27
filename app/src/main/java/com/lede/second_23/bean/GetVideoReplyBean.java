package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/4.
 */

public class GetVideoReplyBean {

    /**
     * data : {"simplePageInfo":{"hasNextPage":false,"list":[{"allRecord":{"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"creatTime":"2017-08-01 15:24:14","forumId":1501232407681475146,"id":2,"replyCount":2,"type":0,"urlVideoPic":"http://my-photo.lacoorent.com/150157234085067.jpg","urlVideoRecord":"http://my-photo.lacoorent.com/150157234054867.mp4","userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},"videoId":1501572347019070849,"videoPic":"150157234085067.jpg","videoRecord":"150157234054867.mp4"},{"allRecord":{"forumId":1501813912319041093,"id":167,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/1501813912710033251.png","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"0c264a82887746e4a35b3b2c4006891c"},"creatTime":"2017-08-07 10:56:51","forumId":1501813912319041093,"id":3,"replyCount":1,"type":0,"urlVideoPic":"http://my-photo.lacoorent.com/1502074608753785371.jpg","urlVideoRecord":"http://my-photo.lacoorent.com/150207460873520462.mp4","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"videoId":1502074612456875180,"videoPic":"1502074608753785371.jpg","videoRecord":"150207460873520462.mp4"}],"nextPage":0,"pageNum":1,"pageSize":2,"total":2}}
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
         * simplePageInfo : {"hasNextPage":false,"list":[{"allRecord":{"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"creatTime":"2017-08-01 15:24:14","forumId":1501232407681475146,"id":2,"replyCount":2,"type":0,"urlVideoPic":"http://my-photo.lacoorent.com/150157234085067.jpg","urlVideoRecord":"http://my-photo.lacoorent.com/150157234054867.mp4","userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},"videoId":1501572347019070849,"videoPic":"150157234085067.jpg","videoRecord":"150157234054867.mp4"},{"allRecord":{"forumId":1501813912319041093,"id":167,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/1501813912710033251.png","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"0c264a82887746e4a35b3b2c4006891c"},"creatTime":"2017-08-07 10:56:51","forumId":1501813912319041093,"id":3,"replyCount":1,"type":0,"urlVideoPic":"http://my-photo.lacoorent.com/1502074608753785371.jpg","urlVideoRecord":"http://my-photo.lacoorent.com/150207460873520462.mp4","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"videoId":1502074612456875180,"videoPic":"1502074608753785371.jpg","videoRecord":"150207460873520462.mp4"}],"nextPage":0,"pageNum":1,"pageSize":2,"total":2}
         */

        private SimplePageInfoBean simplePageInfo;

        public SimplePageInfoBean getSimplePageInfo() {
            return simplePageInfo;
        }

        public void setSimplePageInfo(SimplePageInfoBean simplePageInfo) {
            this.simplePageInfo = simplePageInfo;
        }

        public static class SimplePageInfoBean {
            /**
             * hasNextPage : false
             * list : [{"allRecord":{"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"creatTime":"2017-08-01 15:24:14","forumId":1501232407681475146,"id":2,"replyCount":2,"type":0,"urlVideoPic":"http://my-photo.lacoorent.com/150157234085067.jpg","urlVideoRecord":"http://my-photo.lacoorent.com/150157234054867.mp4","userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},"videoId":1501572347019070849,"videoPic":"150157234085067.jpg","videoRecord":"150157234054867.mp4"},{"allRecord":{"forumId":1501813912319041093,"id":167,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/1501813912710033251.png","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"0c264a82887746e4a35b3b2c4006891c"},"creatTime":"2017-08-07 10:56:51","forumId":1501813912319041093,"id":3,"replyCount":1,"type":0,"urlVideoPic":"http://my-photo.lacoorent.com/1502074608753785371.jpg","urlVideoRecord":"http://my-photo.lacoorent.com/150207460873520462.mp4","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"videoId":1502074612456875180,"videoPic":"1502074608753785371.jpg","videoRecord":"150207460873520462.mp4"}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 2
             * total : 2
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
                 * allRecord : {"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}
                 * creatTime : 2017-08-01 15:24:14
                 * forumId : 1501232407681475146
                 * id : 2
                 * replyCount : 2
                 * type : 0
                 * urlVideoPic : http://my-photo.lacoorent.com/150157234085067.jpg
                 * urlVideoRecord : http://my-photo.lacoorent.com/150157234054867.mp4
                 * userId : 9e7a060b521049bb990dedc6055b7886
                 * userInfo : {"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}
                 * videoId : 1501572347019070849
                 * videoPic : 150157234085067.jpg
                 * videoRecord : 150157234054867.mp4
                 */

                private AllRecordBean allRecord;
                private String creatTime;
                private long forumId;
                private int id;
                private int replyCount;
                private int type;
                private String urlVideoPic;
                private String urlVideoRecord;
                private String userId;
                private UserInfoBean userInfo;
                private long videoId;
                private String videoPic;
                private String videoRecord;

                public AllRecordBean getAllRecord() {
                    return allRecord;
                }

                public void setAllRecord(AllRecordBean allRecord) {
                    this.allRecord = allRecord;
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

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getReplyCount() {
                    return replyCount;
                }

                public void setReplyCount(int replyCount) {
                    this.replyCount = replyCount;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getUrlVideoPic() {
                    return urlVideoPic;
                }

                public void setUrlVideoPic(String urlVideoPic) {
                    this.urlVideoPic = urlVideoPic;
                }

                public String getUrlVideoRecord() {
                    return urlVideoRecord;
                }

                public void setUrlVideoRecord(String urlVideoRecord) {
                    this.urlVideoRecord = urlVideoRecord;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public UserInfoBean getUserInfo() {
                    return userInfo;
                }

                public void setUserInfo(UserInfoBean userInfo) {
                    this.userInfo = userInfo;
                }

                public long getVideoId() {
                    return videoId;
                }

                public void setVideoId(long videoId) {
                    this.videoId = videoId;
                }

                public String getVideoPic() {
                    return videoPic;
                }

                public void setVideoPic(String videoPic) {
                    this.videoPic = videoPic;
                }

                public String getVideoRecord() {
                    return videoRecord;
                }

                public void setVideoRecord(String videoRecord) {
                    this.videoRecord = videoRecord;
                }

                public static class AllRecordBean {
                    /**
                     * forumId : 1501232407681475146
                     * id : 26
                     * recordOrder : 0
                     * type : 0
                     * url : http://my-photo.lacoorent.com/15012324325501.jpg
                     * urlThree : http://my-photo.lacoorent.com/null
                     * urlTwo : http://my-photo.lacoorent.com/null
                     * userId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
                     */

                    private long forumId;
                    private int id;
                    private int recordOrder;
                    private int type;
                    private String url;
                    private String urlThree;
                    private String urlTwo;
                    private String userId;

                    public long getForumId() {
                        return forumId;
                    }

                    public void setForumId(long forumId) {
                        this.forumId = forumId;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getRecordOrder() {
                        return recordOrder;
                    }

                    public void setRecordOrder(int recordOrder) {
                        this.recordOrder = recordOrder;
                    }

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
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

                public static class UserInfoBean {
                    /**
                     * imgUrl : http://my-photo.lacoorent.com/20170627160236786746479.jpg
                     * nickName : Ag
                     * userId : 9e7a060b521049bb990dedc6055b7886
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
            }
        }
    }
}
