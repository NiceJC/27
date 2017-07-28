package com.lede.second_23.bean;

/**
 * Created by ld on 17/7/17.
 */

public class QiNiuTokenBean {

    /**
     * data : {"uptoken":"jAq-YTjRDzMLN9J4wTeGZb3ikbBUE5n7F2xpOw6E:vju7XBROpcVOkwCrcbo-E1G1OuA=:eyJzY29wZSI6Imxkd2Fsa2VybWFuIiwiZGVhZGxpbmUiOjE1MDAyNzc0NjZ9"}
     * msg : 七牛token
     */

    private DataBean data;
    private String msg;

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

    public static class DataBean {
        /**
         * uptoken : jAq-YTjRDzMLN9J4wTeGZb3ikbBUE5n7F2xpOw6E:vju7XBROpcVOkwCrcbo-E1G1OuA=:eyJzY29wZSI6Imxkd2Fsa2VybWFuIiwiZGVhZGxpbmUiOjE1MDAyNzc0NjZ9
         */

        private String uptoken;

        public String getUptoken() {
            return uptoken;
        }

        public void setUptoken(String uptoken) {
            this.uptoken = uptoken;
        }
    }
}
