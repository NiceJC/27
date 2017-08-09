package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/4.
 */

public class GetZanBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"allRecord":{"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"countLike":1,"createTime":"2017-07-31 15:01:02","forumId":1501232407681475146,"likeId":6,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501557976274093267,"id":64,"recordOrder":0,"type":1,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"},"countLike":1,"createTime":"2017-08-01 14:19:21","forumId":1501557976274093267,"likeId":7,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501655042170529503,"id":71,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/150165507344125374.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"countLike":1,"createTime":"2017-08-02 14:34:27","forumId":1501655042170529503,"likeId":9,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501747824854094489,"id":164,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/1501747826028048434.png","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"9e7a060b521049bb990dedc6055b7886"},"countLike":1,"createTime":"2017-08-04 16:49:50","forumId":1501747824854094489,"likeId":13,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}}],"nextPage":0,"pageNum":1,"pageSize":4,"total":4}}
     * msg : 个人点赞通知列表
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
         * simple : {"hasNextPage":false,"list":[{"allRecord":{"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"countLike":1,"createTime":"2017-07-31 15:01:02","forumId":1501232407681475146,"likeId":6,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501557976274093267,"id":64,"recordOrder":0,"type":1,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"},"countLike":1,"createTime":"2017-08-01 14:19:21","forumId":1501557976274093267,"likeId":7,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501655042170529503,"id":71,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/150165507344125374.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"countLike":1,"createTime":"2017-08-02 14:34:27","forumId":1501655042170529503,"likeId":9,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501747824854094489,"id":164,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/1501747826028048434.png","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"9e7a060b521049bb990dedc6055b7886"},"countLike":1,"createTime":"2017-08-04 16:49:50","forumId":1501747824854094489,"likeId":13,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}}],"nextPage":0,"pageNum":1,"pageSize":4,"total":4}
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
             * list : [{"allRecord":{"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"countLike":1,"createTime":"2017-07-31 15:01:02","forumId":1501232407681475146,"likeId":6,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501557976274093267,"id":64,"recordOrder":0,"type":1,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"},"countLike":1,"createTime":"2017-08-01 14:19:21","forumId":1501557976274093267,"likeId":7,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501655042170529503,"id":71,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/150165507344125374.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"countLike":1,"createTime":"2017-08-02 14:34:27","forumId":1501655042170529503,"likeId":9,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"allRecord":{"forumId":1501747824854094489,"id":164,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/1501747826028048434.png","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"9e7a060b521049bb990dedc6055b7886"},"countLike":1,"createTime":"2017-08-04 16:49:50","forumId":1501747824854094489,"likeId":13,"status":true,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 4
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
                 * allRecord : {"forumId":1501232407681475146,"id":26,"recordOrder":0,"type":0,"url":"http://my-photo.lacoorent.com/15012324325501.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}
                 * countLike : 1
                 * createTime : 2017-07-31 15:01:02
                 * forumId : 1501232407681475146
                 * likeId : 6
                 * status : true
                 * userId : 40a0f4aef97e4f7f8ff2e86220e8bfd2
                 * userInfo : {"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}
                 */

                private AllRecordBean allRecord;
                private int countLike;
                private String createTime;
                private long forumId;
                private int likeId;
                private boolean status;
                private String userId;
                private UserInfoBean userInfo;

                public AllRecordBean getAllRecord() {
                    return allRecord;
                }

                public void setAllRecord(AllRecordBean allRecord) {
                    this.allRecord = allRecord;
                }

                public int getCountLike() {
                    return countLike;
                }

                public void setCountLike(int countLike) {
                    this.countLike = countLike;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public long getForumId() {
                    return forumId;
                }

                public void setForumId(long forumId) {
                    this.forumId = forumId;
                }

                public int getLikeId() {
                    return likeId;
                }

                public void setLikeId(int likeId) {
                    this.likeId = likeId;
                }

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
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
            }
        }
    }
}
