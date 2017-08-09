package com.lede.second_23.bean;

/**
 * Created by ld on 17/7/31.
 */

public class ForumLikeBean {

    /**
     * data : {"current_like":true}
     * msg : 点赞/取消点赞成功
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
         * current_like : true
         */

        private boolean current_like;

        public boolean isCurrent_like() {
            return current_like;
        }

        public void setCurrent_like(boolean current_like) {
            this.current_like = current_like;
        }
    }
}
