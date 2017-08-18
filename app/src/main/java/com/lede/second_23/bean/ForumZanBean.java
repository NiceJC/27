package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/16.
 */

public class ForumZanBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"createTime":"2017-08-16 15:01:57","forumId":1502447914158093876,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":[{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},{"imgUrl":"http://my-photo.lacoorent.com/20170814162138783656842.jpg","nickName":"msong27","userId":"328de4b3f7304bbca5967766c1001d22"},{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}]}],"nextPage":0,"pageNum":1,"pageSize":100,"total":3}}
     * msg : 微博点赞列表
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
         * simple : {"hasNextPage":false,"list":[{"createTime":"2017-08-16 15:01:57","forumId":1502447914158093876,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":[{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},{"imgUrl":"http://my-photo.lacoorent.com/20170814162138783656842.jpg","nickName":"msong27","userId":"328de4b3f7304bbca5967766c1001d22"},{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}]}],"nextPage":0,"pageNum":1,"pageSize":100,"total":3}
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
             * list : [{"createTime":"2017-08-16 15:01:57","forumId":1502447914158093876,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":[{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},{"imgUrl":"http://my-photo.lacoorent.com/20170814162138783656842.jpg","nickName":"msong27","userId":"328de4b3f7304bbca5967766c1001d22"},{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}]}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 100
             * total : 3
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
                 * createTime : 2017-08-16 15:01:57
                 * forumId : 1502447914158093876
                 * userId : 9e7a060b521049bb990dedc6055b7886
                 * userInfo : [{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},{"imgUrl":"http://my-photo.lacoorent.com/20170814162138783656842.jpg","nickName":"msong27","userId":"328de4b3f7304bbca5967766c1001d22"},{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}]
                 */

                private String createTime;
                private long forumId;
                private String userId;
                private List<UserInfoBean> userInfo;

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

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public List<UserInfoBean> getUserInfo() {
                    return userInfo;
                }

                public void setUserInfo(List<UserInfoBean> userInfo) {
                    this.userInfo = userInfo;
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
