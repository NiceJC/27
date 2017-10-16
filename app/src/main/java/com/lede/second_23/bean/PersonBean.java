package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/5/4.
 */

public class PersonBean {

    /**
     * data : {"simple":{"hasNextPage":true,"list":[{"createTime":"2017-06-27 14:10:48","forumId":339,"forumMedia":{"forumId":339,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048100790220.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048142714168.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"分享","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-26 17:37:03","forumId":338,"forumMedia":{"forumId":338,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":338,"url":"http://my-photo.lacoorent.com/20170626173703270954736.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"好气","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-19 18:32:32","forumId":321,"forumMedia":{"forumId":321,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":321,"url":"http://my-photo.lacoorent.com/20170619183232353563892.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:41:32","forumId":295,"forumMedia":{"forumId":295,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132067565334.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132270719109.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132227986548.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114133849859429.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:26:59","forumId":291,"forumMedia":{"forumId":291,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112710052793489.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112710342909913.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112711925352531.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112712683582611.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112713134504880.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112714734750292.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112715074111143.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112716496218541.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112716625888191.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:18:45","forumId":287,"forumMedia":{"forumId":287,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":287,"url":"http://my-photo.lacoorent.com/20170615111845151919510.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":287,"url":"http://my-photo.lacoorent.com/20170615111845256672275.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-14 18:47:06","forumId":264,"forumMedia":{"forumId":264,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":264,"url":"http://my-photo.lacoorent.com/20170614184706617148547.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"nextPage":2,"pageNum":1,"pageSize":20,"total":43},"friendsCount":0,"followersCount":3,"user":{"address":"浙江省 杭州市","followersCount":13,"friendsCount":-7,"hobby":"21","imgUrl":"http://my-photo.lacoorent.com/20170614154446256454050.jpg","nickName":"Axe","note":"halo","qq":"狮子座","registerTime":"2017-06-13 15:29:15","sex":"男","trueName":"0","userId":"a9460f73af0c4cb9b2f9d514b5934563","wechat":"篮球"},"totalTwo":10}
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
         * simple : {"hasNextPage":true,"list":[{"createTime":"2017-06-27 14:10:48","forumId":339,"forumMedia":{"forumId":339,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048100790220.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048142714168.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"分享","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-26 17:37:03","forumId":338,"forumMedia":{"forumId":338,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":338,"url":"http://my-photo.lacoorent.com/20170626173703270954736.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"好气","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-19 18:32:32","forumId":321,"forumMedia":{"forumId":321,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":321,"url":"http://my-photo.lacoorent.com/20170619183232353563892.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:41:32","forumId":295,"forumMedia":{"forumId":295,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132067565334.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132270719109.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132227986548.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114133849859429.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:26:59","forumId":291,"forumMedia":{"forumId":291,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112710052793489.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112710342909913.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112711925352531.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112712683582611.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112713134504880.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112714734750292.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112715074111143.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112716496218541.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112716625888191.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:18:45","forumId":287,"forumMedia":{"forumId":287,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":287,"url":"http://my-photo.lacoorent.com/20170615111845151919510.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":287,"url":"http://my-photo.lacoorent.com/20170615111845256672275.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-14 18:47:06","forumId":264,"forumMedia":{"forumId":264,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":264,"url":"http://my-photo.lacoorent.com/20170614184706617148547.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"nextPage":2,"pageNum":1,"pageSize":20,"total":43}
         * friendsCount : 0
         * followersCount : 3
         * user : {"address":"浙江省 杭州市","followersCount":13,"friendsCount":-7,"hobby":"21","imgUrl":"http://my-photo.lacoorent.com/20170614154446256454050.jpg","nickName":"Axe","note":"halo","qq":"狮子座","registerTime":"2017-06-13 15:29:15","sex":"男","trueName":"0","userId":"a9460f73af0c4cb9b2f9d514b5934563","wechat":"篮球"}
         * totalTwo : 10
         */

        private SimpleBean simple;
        private int friendsCount;
        private int followersCount;
        private UserBean user;
        private int totalTwo;

        public SimpleBean getSimple() {
            return simple;
        }

        public void setSimple(SimpleBean simple) {
            this.simple = simple;
        }

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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public int getTotalTwo() {
            return totalTwo;
        }

        public void setTotalTwo(int totalTwo) {
            this.totalTwo = totalTwo;
        }

        public static class SimpleBean {
            /**
             * hasNextPage : true
             * list : [{"createTime":"2017-06-27 14:10:48","forumId":339,"forumMedia":{"forumId":339,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048100790220.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":339,"url":"http://my-photo.lacoorent.com/20170627141048142714168.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"分享","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-26 17:37:03","forumId":338,"forumMedia":{"forumId":338,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":338,"url":"http://my-photo.lacoorent.com/20170626173703270954736.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"好气","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-19 18:32:32","forumId":321,"forumMedia":{"forumId":321,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":321,"url":"http://my-photo.lacoorent.com/20170619183232353563892.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:41:32","forumId":295,"forumMedia":{"forumId":295,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132067565334.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132270719109.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114132227986548.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":295,"url":"http://my-photo.lacoorent.com/20170615114133849859429.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:26:59","forumId":291,"forumMedia":{"forumId":291,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112710052793489.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112710342909913.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112711925352531.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112712683582611.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112713134504880.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112714734750292.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112715074111143.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112716496218541.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":291,"url":"http://my-photo.lacoorent.com/20170615112716625888191.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-15 11:18:45","forumId":287,"forumMedia":{"forumId":287,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":287,"url":"http://my-photo.lacoorent.com/20170615111845151919510.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"forumId":287,"url":"http://my-photo.lacoorent.com/20170615111845256672275.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"},{"createTime":"2017-06-14 18:47:06","forumId":264,"forumMedia":{"forumId":264,"path":"http://my-photo.lacoorent.com/null","pic":"http://my-photo.lacoorent.com/null","userId":"a9460f73af0c4cb9b2f9d514b5934563"},"imgs":[{"forumId":264,"url":"http://my-photo.lacoorent.com/20170614184706617148547.jpg","userId":"a9460f73af0c4cb9b2f9d514b5934563"}],"like":false,"text":"","userId":"a9460f73af0c4cb9b2f9d514b5934563"}]
             * nextPage : 2
             * pageNum : 1
             * pageSize : 20
             * total : 43
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

        public static class UserBean {
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
    }
}
