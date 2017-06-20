package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/5/5.
 */

public class PersonUtilsBean {
    private List<DataBean> list;

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public static class DataBean{
        private String createTime;
//        private List<ListBean> beanList;
        private List<PersonBean.DataBean.SimpleBean.ListBean> beanList;

        public List<PersonBean.DataBean.SimpleBean.ListBean> getBeanList() {
            return beanList;
        }

        public void setBeanList(List<PersonBean.DataBean.SimpleBean.ListBean> beanList) {
            this.beanList = beanList;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

//        public List<ListBean> getBeanList() {
//            return beanList;
//        }
//
//        public void setBeanList(List<ListBean> beanList) {
//            this.beanList = beanList;
//        }
//
//        public static class ListBean {
//            /**
//             * createTime : 2017-05-03 12:38:35
//             * forumId : 134
//             * forumMedia : {"forumId":134,"path":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4","pic":"http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg"}
//             * imgs : [{"forumId":134,"url":"http://7xr1tb.com1.z0.glb.clouddn.com/null"}]
//             * like : false
//             * text : 乐可快乐出行2
//             */
//
//            private String createTime;
//            private int forumId;
//            private ForumMediaBean forumMedia;
//            private boolean like;
//            private String text;
//            private List<ImgsBean> imgs;
//
//            public String getCreateTime() {
//                return createTime;
//            }
//
//            public void setCreateTime(String createTime) {
//                this.createTime = createTime;
//            }
//
//            public int getForumId() {
//                return forumId;
//            }
//
//            public void setForumId(int forumId) {
//                this.forumId = forumId;
//            }
//
//            public ForumMediaBean getForumMedia() {
//                return forumMedia;
//            }
//
//            public void setForumMedia(ForumMediaBean forumMedia) {
//                this.forumMedia = forumMedia;
//            }
//
//            public boolean isLike() {
//                return like;
//            }
//
//            public void setLike(boolean like) {
//                this.like = like;
//            }
//
//            public String getText() {
//                return text;
//            }
//
//            public void setText(String text) {
//                this.text = text;
//            }
//
//            public List<ImgsBean> getImgs() {
//                return imgs;
//            }
//
//            public void setImgs(List<ImgsBean> imgs) {
//                this.imgs = imgs;
//            }
//
//            public static class ForumMediaBean {
//                /**
//                 * forumId : 134
//                 * path : http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.mp4
//                 * pic : http://7xr1tb.com1.z0.glb.clouddn.com/20170503123840268183357.jpg
//                 */
//
//                private int forumId;
//                private String path;
//                private String pic;
//
//                public int getForumId() {
//                    return forumId;
//                }
//
//                public void setForumId(int forumId) {
//                    this.forumId = forumId;
//                }
//
//                public String getPath() {
//                    return path;
//                }
//
//                public void setPath(String path) {
//                    this.path = path;
//                }
//
//                public String getPic() {
//                    return pic;
//                }
//
//                public void setPic(String pic) {
//                    this.pic = pic;
//                }
//            }
//
//            public static class ImgsBean {
//                /**
//                 * forumId : 134
//                 * url : http://7xr1tb.com1.z0.glb.clouddn.com/null
//                 */
//
//                private int forumId;
//                private String url;
//
//                public int getForumId() {
//                    return forumId;
//                }
//
//                public void setForumId(int forumId) {
//                    this.forumId = forumId;
//                }
//
//                public String getUrl() {
//                    return url;
//                }
//
//                public void setUrl(String url) {
//                    this.url = url;
//                }
//            }
//        }
    }
}
