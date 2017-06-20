package com.lede.second_23.bean;

/**
 * Created by ld on 17/4/26.
 */

public class UploadTextBean {

    /**
     * data : {"createTime":"2017-04-26 10:25:26","forumId":124,"geo":{"forumId":124,"geoId":123},"like":false,"likeCount":0,"text":"乐可快乐出行0123456789asdzxcvbhfgrty"}
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
         * createTime : 2017-04-26 10:25:26
         * forumId : 124
         * geo : {"forumId":124,"geoId":123}
         * like : false
         * likeCount : 0
         * text : 乐可快乐出行0123456789asdzxcvbhfgrty
         */

        private String createTime;
        private int forumId;
        private GeoBean geo;
        private boolean like;
        private int likeCount;
        private String text;

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

        public GeoBean getGeo() {
            return geo;
        }

        public void setGeo(GeoBean geo) {
            this.geo = geo;
        }

        public boolean isLike() {
            return like;
        }

        public void setLike(boolean like) {
            this.like = like;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public static class GeoBean {
            /**
             * forumId : 124
             * geoId : 123
             */

            private int forumId;
            private int geoId;

            public int getForumId() {
                return forumId;
            }

            public void setForumId(int forumId) {
                this.forumId = forumId;
            }

            public int getGeoId() {
                return geoId;
            }

            public void setGeoId(int geoId) {
                this.geoId = geoId;
            }
        }
    }
}
