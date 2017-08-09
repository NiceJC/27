package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/1.
 */

public class ShowAllForumByVideoBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"allRecords":[{"forumId":1501557976274093267,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"}],"clickCount":0,"creatTime":"2017-08-01 11:26:18","forumId":1501557976274093267,"forumText":"视频测试","latitude":"30.303741","likeCount":0,"longitude":"120.369082","user":{"imgUrl":"http://my-photo.lacoorent.com/null","userId":"1fa48078987d405baeefc6d94e4fc216"},"userId":"1fa48078987d405baeefc6d94e4fc216"}],"nextPage":0,"pageNum":1,"pageSize":20,"total":1}}
     * msg : 全国视频微博
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
         * simple : {"hasNextPage":false,"list":[{"allRecords":[{"forumId":1501557976274093267,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"}],"clickCount":0,"creatTime":"2017-08-01 11:26:18","forumId":1501557976274093267,"forumText":"视频测试","latitude":"30.303741","likeCount":0,"longitude":"120.369082","user":{"imgUrl":"http://my-photo.lacoorent.com/null","userId":"1fa48078987d405baeefc6d94e4fc216"},"userId":"1fa48078987d405baeefc6d94e4fc216"}],"nextPage":0,"pageNum":1,"pageSize":20,"total":1}
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
             * list : [{"allRecords":[{"forumId":1501557976274093267,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"}],"clickCount":0,"creatTime":"2017-08-01 11:26:18","forumId":1501557976274093267,"forumText":"视频测试","latitude":"30.303741","likeCount":0,"longitude":"120.369082","user":{"imgUrl":"http://my-photo.lacoorent.com/null","userId":"1fa48078987d405baeefc6d94e4fc216"},"userId":"1fa48078987d405baeefc6d94e4fc216"}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 20
             * total : 1
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
                 * allRecords : [{"forumId":1501557976274093267,"url":"http://my-photo.lacoorent.com/null","urlThree":"http://my-photo.lacoorent.com/1501557976420060152.png","urlTwo":"http://my-photo.lacoorent.com/1501557976769070144.mp4","userId":"1fa48078987d405baeefc6d94e4fc216"}]
                 * clickCount : 0
                 * creatTime : 2017-08-01 11:26:18
                 * forumId : 1501557976274093267
                 * forumText : 视频测试
                 * latitude : 30.303741
                 * likeCount : 0
                 * longitude : 120.369082
                 * user : {"imgUrl":"http://my-photo.lacoorent.com/null","userId":"1fa48078987d405baeefc6d94e4fc216"}
                 * userId : 1fa48078987d405baeefc6d94e4fc216
                 */

                private int clickCount;
                private String creatTime;
                private long forumId;
                private String forumText;
                private String latitude;
                private int likeCount;
                private String longitude;
                private UserBean user;
                private String userId;
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

                public List<AllRecordsBean> getAllRecords() {
                    return allRecords;
                }

                public void setAllRecords(List<AllRecordsBean> allRecords) {
                    this.allRecords = allRecords;
                }

                public static class UserBean {
                    /**
                     * imgUrl : http://my-photo.lacoorent.com/null
                     * userId : 1fa48078987d405baeefc6d94e4fc216
                     */

                    private String imgUrl;
                    private String userId;

                    public String getImgUrl() {
                        return imgUrl;
                    }

                    public void setImgUrl(String imgUrl) {
                        this.imgUrl = imgUrl;
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
                     * forumId : 1501557976274093267
                     * url : http://my-photo.lacoorent.com/null
                     * urlThree : http://my-photo.lacoorent.com/1501557976420060152.png
                     * urlTwo : http://my-photo.lacoorent.com/1501557976769070144.mp4
                     * userId : 1fa48078987d405baeefc6d94e4fc216
                     */

                    private long forumId;
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
}
