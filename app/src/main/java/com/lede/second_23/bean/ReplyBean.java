package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/7/27.
 */

public class ReplyBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"commentId":150036835905474,"creatTime":"2017-07-19 14:51:00","forumId":1500276726235233882,"id":3,"replyLike":0,"replyText":"回复微博评论 来自3386","spare":"3386名字","status":0,"type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag"},{"commentId":150036835905474,"creatTime":"2017-07-19 15:09:54","forumId":1500276726235233882,"id":4,"replyLike":0,"replyText":"回复微博评论 来自3386","spare":"3386名字","status":0,"type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:12:50","forumId":1500276726235233882,"id":6,"replyLike":0,"replyText":"回复微博评论的用户 来自张泽","spare":"张泽","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:12:50","forumId":1500276726235233882,"id":8,"replyLike":0,"replyText":"回复微博评论的用户 来自张泽","spare":"张泽","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"}},{"commentId":150036835905474,"creatTime":"2017-07-20 15:12:50","forumId":1500276726235233882,"id":10,"replyLike":0,"replyText":"回复微博评论 来自博主","spare":"3385名字","status":0,"toUserId":"","toUserName":"","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"commentId":150036835905474,"creatTime":"2017-07-22 15:12:50","forumId":1500276726235233882,"id":11,"replyLike":0,"replyText":"回复微博评论的用户 来自博主","spare":"3385名字","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"commentId":150036835905474,"creatTime":"2017-07-22 15:14:50","forumId":1500276726235233882,"id":12,"replyLike":0,"replyText":"回复微博评论的用户 来自3386","spare":"3386名字","status":0,"toUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","toUserName":"3385","type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}}],"nextPage":0,"pageNum":1,"pageSize":20,"total":7}}
     * msg : 显示用户回复评论
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
         * simple : {"hasNextPage":false,"list":[{"commentId":150036835905474,"creatTime":"2017-07-19 14:51:00","forumId":1500276726235233882,"id":3,"replyLike":0,"replyText":"回复微博评论 来自3386","spare":"3386名字","status":0,"type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"},"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag"},{"commentId":150036835905474,"creatTime":"2017-07-19 15:09:54","forumId":1500276726235233882,"id":4,"replyLike":0,"replyText":"回复微博评论 来自3386","spare":"3386名字","status":0,"type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:12:50","forumId":1500276726235233882,"id":6,"replyLike":0,"replyText":"回复微博评论的用户 来自张泽","spare":"张泽","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:12:50","forumId":1500276726235233882,"id":8,"replyLike":0,"replyText":"回复微博评论的用户 来自张泽","spare":"张泽","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"}},{"commentId":150036835905474,"creatTime":"2017-07-20 15:12:50","forumId":1500276726235233882,"id":10,"replyLike":0,"replyText":"回复微博评论 来自博主","spare":"3385名字","status":0,"toUserId":"","toUserName":"","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"commentId":150036835905474,"creatTime":"2017-07-22 15:12:50","forumId":1500276726235233882,"id":11,"replyLike":0,"replyText":"回复微博评论的用户 来自博主","spare":"3385名字","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"commentId":150036835905474,"creatTime":"2017-07-22 15:14:50","forumId":1500276726235233882,"id":12,"replyLike":0,"replyText":"回复微博评论的用户 来自3386","spare":"3386名字","status":0,"toUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","toUserName":"3385","type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}}],"nextPage":0,"pageNum":1,"pageSize":20,"total":7}
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
             * list : [{"commentId":150036835905474,"creatTime":"2017-07-19 14:51:00","forumId":1500276726235233882,"id":3,"replyLike":0,"replyText":"回复微博评论 来自3386","spare":"3386名字","status":0,"type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:09:54","forumId":1500276726235233882,"id":4,"replyLike":0,"replyText":"回复微博评论 来自3386","spare":"3386名字","status":0,"type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:12:50","forumId":1500276726235233882,"id":6,"replyLike":0,"replyText":"回复微博评论的用户 来自张泽","spare":"张泽","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"}},{"commentId":150036835905474,"creatTime":"2017-07-19 15:12:50","forumId":1500276726235233882,"id":8,"replyLike":0,"replyText":"回复微博评论的用户 来自张泽","spare":"张泽","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"}},{"commentId":150036835905474,"creatTime":"2017-07-20 15:12:50","forumId":1500276726235233882,"id":10,"replyLike":0,"replyText":"回复微博评论 来自博主","spare":"3385名字","status":0,"toUserId":"","toUserName":"","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"commentId":150036835905474,"creatTime":"2017-07-22 15:12:50","forumId":1500276726235233882,"id":11,"replyLike":0,"replyText":"回复微博评论的用户 来自博主","spare":"3385名字","status":0,"toUserId":"9e7a060b521049bb990dedc6055b7886","toUserName":"Ag","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}},{"commentId":150036835905474,"creatTime":"2017-07-22 15:14:50","forumId":1500276726235233882,"id":12,"replyLike":0,"replyText":"回复微博评论的用户 来自3386","spare":"3386名字","status":0,"toUserId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","toUserName":"3385","type":0,"userId":"9e7a060b521049bb990dedc6055b7886","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}}]
             * nextPage : 0
             * pageNum : 1
             * pageSize : 20
             * total : 7
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
                 * commentId : 150036835905474
                 * creatTime : 2017-07-19 14:51:00
                 * forumId : 1500276726235233882
                 * id : 3
                 * replyLike : 0
                 * replyText : 回复微博评论 来自3386
                 * spare : 3386名字
                 * status : 0
                 * type : 0
                 * userId : 9e7a060b521049bb990dedc6055b7886
                 * userInfo : {"imgUrl":"http://my-photo.lacoorent.com/20170622152429976191800.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}
                 * toUserId : 9e7a060b521049bb990dedc6055b7886
                 * toUserName : Ag
                 */

                private long commentId;
                private String creatTime;
                private long forumId;
                private int id;
                private int replyLike;
                private String replyText;
                private String spare;
                private int status;
                private int type;
                private String userId;
                private UserInfoBean userInfo;
                private String toUserId;
                private String toUserName;

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

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getReplyLike() {
                    return replyLike;
                }

                public void setReplyLike(int replyLike) {
                    this.replyLike = replyLike;
                }

                public String getReplyText() {
                    return replyText;
                }

                public void setReplyText(String replyText) {
                    this.replyText = replyText;
                }

                public String getSpare() {
                    return spare;
                }

                public void setSpare(String spare) {
                    this.spare = spare;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
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

                public UserInfoBean getUserInfo() {
                    return userInfo;
                }

                public void setUserInfo(UserInfoBean userInfo) {
                    this.userInfo = userInfo;
                }

                public String getToUserId() {
                    return toUserId;
                }

                public void setToUserId(String toUserId) {
                    this.toUserId = toUserId;
                }

                public String getToUserName() {
                    return toUserName;
                }

                public void setToUserName(String toUserName) {
                    this.toUserName = toUserName;
                }

                public static class UserInfoBean {
                    /**
                     * imgUrl : http://my-photo.lacoorent.com/20170622152429976191800.jpg
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
