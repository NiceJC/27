package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/4/24.
 */

public class HomePagerBean {


    /**
     * data : {"forumList":[{"createTime":"2017-05-25 10:37:11","forumId":182,"forumMedia":{"forumId":182,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170525103720827819165.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170525103720827819165.png","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"},"imgs":[{"forumId":182,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}],"like":false,"text":"哈哈","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"},{"createTime":"2017-05-19 17:15:40","forumId":180,"forumMedia":{"forumId":180,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/null","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/null","userId":"9e7a060b521049bb990dedc6055b7886"},"imgs":[{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540476340118.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540499727845.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540514422383.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540598347194.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540636443121.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540664948954.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540714530203.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540745688347.webp","userId":"9e7a060b521049bb990dedc6055b7886"},{"forumId":180,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/20170519171540807138669.webp","userId":"9e7a060b521049bb990dedc6055b7886"}],"like":false,"text":"表情","userId":"9e7a060b521049bb990dedc6055b7886"}]}
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
        private List<ForumListBean> forumList;

        public List<ForumListBean> getForumList() {
            return forumList;
        }

        public void setForumList(List<ForumListBean> forumList) {
            this.forumList = forumList;
        }

        public static class ForumListBean {
            /**
             * createTime : 2017-05-25 10:37:11
             * forumId : 182
             * forumMedia : {"forumId":182,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170525103720827819165.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170525103720827819165.png","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}
             * imgs : [{"forumId":182,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}]
             * like : false
             * text : 哈哈
             * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
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
                 * forumId : 182
                 * path : http://7xr1tb.com1.z0.glb.clouddn.com/20170525103720827819165.mp4
                 * pic : http://7xr1tb.com1.z0.glb.clouddn.com/20170525103720827819165.png
                 * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
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
                 * forumId : 182
                 * url : http://7xr1tb.com1.z0.glb.clouddn.com/null
                 * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
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
}
