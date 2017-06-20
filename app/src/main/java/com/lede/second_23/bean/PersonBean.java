package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/5/4.
 */

public class PersonBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"createTime":"2017-05-03 12:38:35","forumId":134,"forumMedia":{"forumId":134,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg"},"imgs":[{"forumId":134,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}],"like":false,"text":"乐可快乐出行2"},{"createTime":"2017-05-03 12:about:33","forumId":131,"forumMedia":{"forumId":131,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503122823707305766.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503122823707305766.jpg"},"imgs":[{"forumId":131,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}],"like":false,"text":"乐可快乐出行1"},{"createTime":"2017-04-26 10:46:39","forumId":125,"forumMedia":{"forumId":125,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/null","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/null"},"imgs":[{"forumId":125,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170426104639811394633.jpg"},{"forumId":125,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170426111254346137269.jpg"}],"like":false,"text":"乐可快乐出行0123456789asdzxcvbhfgrty"}],"nextPage":0,"pageNum":1,"pageSize":20,"total":4},"user":{"address":"浙江省 杭州市","followersCount":8,"friendsCount":14,"hobby":"21","hometown":"感情","imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170413181819026917745.jpg","nickName":"axe","note":"qwer123","qq":"狮子座","registerTime":"2017-04-12 15:04:45","sex":"男","userId":"9e7a060b521049bb990dedc6055b7886","wechat":"软件工程师"}}
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
         * simple : {"hasNextPage":false,"list":[{"createTime":"2017-05-03 12:38:35","forumId":134,"forumMedia":{"forumId":134,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg"},"imgs":[{"forumId":134,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}],"like":false,"text":"乐可快乐出行2"},{"createTime":"2017-05-03 12:about:33","forumId":131,"forumMedia":{"forumId":131,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503122823707305766.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503122823707305766.jpg"},"imgs":[{"forumId":131,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}],"like":false,"text":"乐可快乐出行1"},{"createTime":"2017-04-26 10:46:39","forumId":125,"forumMedia":{"forumId":125,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/null","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/null"},"imgs":[{"forumId":125,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170426104639811394633.jpg"},{"forumId":125,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170426111254346137269.jpg"}],"like":false,"text":"乐可快乐出行0123456789asdzxcvbhfgrty"}],"nextPage":0,"pageNum":1,"pageSize":20,"total":4}
         * user : {"address":"浙江省 杭州市","followersCount":8,"friendsCount":14,"hobby":"21","hometown":"感情","imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170413181819026917745.jpg","nickName":"axe","note":"qwer123","qq":"狮子座","registerTime":"2017-04-12 15:04:45","sex":"男","userId":"9e7a060b521049bb990dedc6055b7886","wechat":"软件工程师"}
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
             * hasNextPage : false
             * list : [{"createTime":"2017-05-03 12:38:35","forumId":134,"forumMedia":{"forumId":134,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg"},"imgs":[{"forumId":134,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}],"like":false,"text":"乐可快乐出行2"},{"createTime":"2017-05-03 12:about:33","forumId":131,"forumMedia":{"forumId":131,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503122823707305766.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503122823707305766.jpg"},"imgs":[{"forumId":131,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}],"like":false,"text":"乐可快乐出行1"},{"createTime":"2017-04-26 10:46:39","forumId":125,"forumMedia":{"forumId":125,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/null","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/null"},"imgs":[{"forumId":125,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170426104639811394633.jpg"},{"forumId":125,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170426111254346137269.jpg"}],"like":false,"text":"乐可快乐出行0123456789asdzxcvbhfgrty"}]
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
                 * createTime : 2017-05-03 12:38:35
                 * forumId : 134
                 * forumMedia : {"forumId":134,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg"}
                 * imgs : [{"forumId":134,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}]
                 * like : false
                 * text : 乐可快乐出行2
                 */

                private String createTime;
                private int forumId;
                private ForumMediaBean forumMedia;
                private boolean like;
                private String text;
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

                public List<ImgsBean> getImgs() {
                    return imgs;
                }

                public void setImgs(List<ImgsBean> imgs) {
                    this.imgs = imgs;
                }

                public static class ForumMediaBean {
                    /**
                     * forumId : 134
                     * path : http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4
                     * pic : http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg
                     */

                    private int forumId;
                    private String path;
                    private String pic;

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
                }

                public static class ImgsBean {
                    /**
                     * forumId : 134
                     * url : http://7xr1tb.com1.z0.glb.clouddn.com/null
                     */

                    private int forumId;
                    private String url;

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
                }
            }
        }

        public static class UserBean {
            /**
             * address : 浙江省 杭州市
             * followersCount : 8
             * friendsCount : 14
             * hobby : 21
             * hometown : 感情
             * imgUrl : http://7xr1tb.com1.z0.glb.clouddn.com/20170413181819026917745.jpg
             * nickName : axe
             * note : qwer123
             * qq : 狮子座
             * registerTime : 2017-04-12 15:04:45
             * sex : 男
             * userId : 9e7a060b521049bb990dedc6055b7886
             * wechat : 软件工程师
             */

            private String address;
            private int followersCount;
            private int friendsCount;
            private String hobby;
            private String hometown;
            private String imgUrl;
            private String nickName;
            private String note;
            private String qq;
            private String registerTime;
            private String sex;
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

            public String getHometown() {
                return hometown;
            }

            public void setHometown(String hometown) {
                this.hometown = hometown;
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
