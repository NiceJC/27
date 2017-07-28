package com.lede.second_23.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ld on 17/7/26.
 */

public class AllForumBean {

    /**
     * data : {"simple":{"hasNextPage":true,"list":[{"allRecords":[{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":0,"creatTime":"2017-07-25 17:51:58","forumId":1500457954715536281,"forumText":"通知测试","likeCount":0,"user":{"imgUrl":"http://my-photo.lacoorent.com/20170612160832047592568.jpg","nickName":"不爱吃草的坏兔子","userId":"0c264a82887746e4a35b3b2c4006891c"},"userId":"0c264a82887746e4a35b3b2c4006891c","latitude":"1321321","longitude":"4654231"},{"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150045797159028.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":4,"creatTime":"2017-07-19 17:51:58","forumId":1500457954715536280,"forumText":"第5次测试","latitude":"1321321","likeCount":0,"longitude":"4654231","user":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"},"userId":"1fa48078987d405baeefc6d94e4fc216"},{"allRecords":[{"recordOrder":2,"url":"http://my-photo.lacoorent.com/150027678011141.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":7,"url":"http://my-photo.lacoorent.com/150027678067767.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150027678008751.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":8,"url":"http://my-photo.lacoorent.com/150027678073268.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/150027678035566.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":5,"url":"http://my-photo.lacoorent.com/15002767803747.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":4,"url":"http://my-photo.lacoorent.com/150027678030831.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":16,"creatTime":"2017-07-17 15:33:19","forumId":1500276726235233882,"forumText":"第4次测试","latitude":"1321321","likeCount":0,"longitude":"4654231","user":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"nextPage":2,"pageNum":1,"pageSize":10,"total":20}}
     * msg : 全国微博
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
         * simple : {"hasNextPage":true,"list":[{"allRecords":[{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":0,"creatTime":"2017-07-25 17:51:58","forumId":1500457954715536281,"forumText":"通知测试","likeCount":0,"user":{"imgUrl":"http://my-photo.lacoorent.com/20170612160832047592568.jpg","nickName":"不爱吃草的坏兔子","userId":"0c264a82887746e4a35b3b2c4006891c"},"userId":"0c264a82887746e4a35b3b2c4006891c","latitude":"1321321","longitude":"4654231"},{"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150045797159028.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":4,"creatTime":"2017-07-19 17:51:58","forumId":1500457954715536280,"forumText":"第5次测试","latitude":"1321321","likeCount":0,"longitude":"4654231","user":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"},"userId":"1fa48078987d405baeefc6d94e4fc216"},{"allRecords":[{"recordOrder":2,"url":"http://my-photo.lacoorent.com/150027678011141.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":7,"url":"http://my-photo.lacoorent.com/150027678067767.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150027678008751.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":8,"url":"http://my-photo.lacoorent.com/150027678073268.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/150027678035566.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":5,"url":"http://my-photo.lacoorent.com/15002767803747.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":4,"url":"http://my-photo.lacoorent.com/150027678030831.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":16,"creatTime":"2017-07-17 15:33:19","forumId":1500276726235233882,"forumText":"第4次测试","latitude":"1321321","likeCount":0,"longitude":"4654231","user":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"nextPage":2,"pageNum":1,"pageSize":10,"total":20}
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
             * hasNextPage : true
             * list : [{"allRecords":[{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":0,"creatTime":"2017-07-25 17:51:58","forumId":1500457954715536281,"forumText":"通知测试","likeCount":0,"user":{"imgUrl":"http://my-photo.lacoorent.com/20170612160832047592568.jpg","nickName":"不爱吃草的坏兔子","userId":"0c264a82887746e4a35b3b2c4006891c"},"userId":"0c264a82887746e4a35b3b2c4006891c"},{"allRecords":[{"recordOrder":0,"url":"http://my-photo.lacoorent.com/150045797159028.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":4,"creatTime":"2017-07-19 17:51:58","forumId":1500457954715536280,"forumText":"第5次测试","latitude":"1321321","likeCount":0,"longitude":"4654231","user":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"},"userId":"1fa48078987d405baeefc6d94e4fc216"},{"allRecords":[{"recordOrder":2,"url":"http://my-photo.lacoorent.com/150027678011141.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":7,"url":"http://my-photo.lacoorent.com/150027678067767.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150027678008751.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":8,"url":"http://my-photo.lacoorent.com/150027678073268.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":3,"url":"http://my-photo.lacoorent.com/150027678035566.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":5,"url":"http://my-photo.lacoorent.com/15002767803747.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"},{"recordOrder":4,"url":"http://my-photo.lacoorent.com/150027678030831.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}],"clickCount":16,"creatTime":"2017-07-17 15:33:19","forumId":1500276726235233882,"forumText":"第4次测试","latitude":"1321321","likeCount":0,"longitude":"4654231","user":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}]
             * nextPage : 2
             * pageNum : 1
             * pageSize : 10
             * total : 20
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

            public static class ListBean implements Serializable {
                /**
                 * allRecords : [{"recordOrder":1,"url":"http://my-photo.lacoorent.com/150045797160951.jpg","urlThree":"http://my-photo.lacoorent.com/null","urlTwo":"http://my-photo.lacoorent.com/null"}]
                 * clickCount : 0
                 * creatTime : 2017-07-25 17:51:58
                 * forumId : 1500457954715536281
                 * forumText : 通知测试
                 * likeCount : 0
                 * user : {"imgUrl":"http://my-photo.lacoorent.com/20170612160832047592568.jpg","nickName":"不爱吃草的坏兔子","userId":"0c264a82887746e4a35b3b2c4006891c"}
                 * userId : 0c264a82887746e4a35b3b2c4006891c
                 * latitude : 1321321
                 * longitude : 4654231
                 */

                private int clickCount;
                private String creatTime;
                private long forumId;
                private String forumText;
                private int likeCount;
                private UserBean user;
                private String userId;
                private String latitude;
                private String longitude;
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

                public int getLikeCount() {
                    return likeCount;
                }

                public void setLikeCount(int likeCount) {
                    this.likeCount = likeCount;
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

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public List<AllRecordsBean> getAllRecords() {
                    return allRecords;
                }

                public void setAllRecords(List<AllRecordsBean> allRecords) {
                    this.allRecords = allRecords;
                }

                public static class UserBean implements Serializable {
                    /**
                     * imgUrl : http://my-photo.lacoorent.com/20170612160832047592568.jpg
                     * nickName : 不爱吃草的坏兔子
                     * userId : 0c264a82887746e4a35b3b2c4006891c
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

                public static class AllRecordsBean implements Serializable {
                    /**
                     * recordOrder : 1
                     * url : http://my-photo.lacoorent.com/150045797160951.jpg
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
