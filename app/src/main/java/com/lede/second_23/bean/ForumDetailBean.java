package com.lede.second_23.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ld on 17/7/27.
 */

public class ForumDetailBean {

    /**
     * data : {"simple":{"hasNextPage":false,"list":[{"allReplies":[{"commentId":150114068057991,"creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216"}],"commentId":150114068057991,"commentLike":0,"commentText":"评论微博，第二次测试","creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"id":32,"replyCount":4,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽"}},{"allReplies":[{"commentId":150114052704921,"creatTime":"2017-07-27 15:27:21","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"commentId":150114052704921,"commentLike":0,"commentText":"评论微博，第一次测试","creatTime":"2017-07-27 15:27:21","forumId":1500457954715536282,"id":31,"replyCount":2,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇"}}],"nextPage":0,"pageNum":1,"pageSize":2,"total":2}}
     * msg : 显示评论的回复
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
         * simple : {"hasNextPage":false,"list":[{"allReplies":[{"commentId":150114068057991,"creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216"}],"commentId":150114068057991,"commentLike":0,"commentText":"评论微博，第二次测试","creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"id":32,"replyCount":4,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽"}},{"allReplies":[{"commentId":150114052704921,"creatTime":"2017-07-27 15:27:21","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"commentId":150114052704921,"commentLike":0,"commentText":"评论微博，第一次测试","creatTime":"2017-07-27 15:27:21","forumId":1500457954715536282,"id":31,"replyCount":2,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇"}}],"nextPage":0,"pageNum":1,"pageSize":2,"total":2}
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
             * list : [{"allReplies":[{"commentId":150114068057991,"creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216"}],"commentId":150114068057991,"commentLike":0,"commentText":"评论微博，第二次测试","creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"id":32,"replyCount":4,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽"}},{"allReplies":[{"commentId":150114052704921,"creatTime":"2017-07-27 15:27:21","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"}],"commentId":150114052704921,"commentLike":0,"commentText":"评论微博，第一次测试","creatTime":"2017-07-27 15:27:21","forumId":1500457954715536282,"id":31,"replyCount":2,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2","userInfo":{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇"}}]
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

            public static class ListBean implements Serializable {
                /**
                 * allReplies : [{"commentId":150114068057991,"creatTime":"2017-07-27 15:29:55","forumId":1500457954715536282,"toUserId":"0c264a82887746e4a35b3b2c4006891c","type":0,"userId":"1fa48078987d405baeefc6d94e4fc216"}]
                 * commentId : 150114068057991
                 * commentLike : 0
                 * commentText : 评论微博，第二次测试
                 * creatTime : 2017-07-27 15:29:55
                 * forumId : 1500457954715536282
                 * id : 32
                 * replyCount : 4
                 * toUserId : 0c264a82887746e4a35b3b2c4006891c
                 * type : 0
                 * userId : 1fa48078987d405baeefc6d94e4fc216
                 * userInfo : {"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽"}
                 */

                private long commentId;
                private int commentLike;
                private String commentText;
                private String creatTime;
                private long forumId;
                private int id;
                private int replyCount;
                private String toUserId;
                private int type;
                private String userId;
                private UserInfoBean userInfo;
                private List<AllRepliesBean> allReplies;

                public long getCommentId() {
                    return commentId;
                }

                public void setCommentId(long commentId) {
                    this.commentId = commentId;
                }

                public int getCommentLike() {
                    return commentLike;
                }

                public void setCommentLike(int commentLike) {
                    this.commentLike = commentLike;
                }

                public String getCommentText() {
                    return commentText;
                }

                public void setCommentText(String commentText) {
                    this.commentText = commentText;
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

                public String getToUserId() {
                    return toUserId;
                }

                public void setToUserId(String toUserId) {
                    this.toUserId = toUserId;
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

                public List<AllRepliesBean> getAllReplies() {
                    return allReplies;
                }

                public void setAllReplies(List<AllRepliesBean> allReplies) {
                    this.allReplies = allReplies;
                }

                public static class UserInfoBean implements Serializable{
                    /**
                     * imgUrl : http://my-photo.lacoorent.com/20170627160236786746479.jpg
                     * nickName : 哈喽
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

                public static class AllRepliesBean implements Serializable {
                    /**
                     * commentId : 150114068057991
                     * creatTime : 2017-07-27 15:29:55
                     * forumId : 1500457954715536282
                     * toUserId : 0c264a82887746e4a35b3b2c4006891c
                     * type : 0
                     * userId : 1fa48078987d405baeefc6d94e4fc216
                     */

                    private long commentId;
                    private String creatTime;
                    private long forumId;
                    private String toUserId;
                    private int type;
                    private String userId;

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

                    public String getToUserId() {
                        return toUserId;
                    }

                    public void setToUserId(String toUserId) {
                        this.toUserId = toUserId;
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
            }
        }
    }
}
