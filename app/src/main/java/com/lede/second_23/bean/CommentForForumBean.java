package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/7/27.
 */

public class CommentForForumBean {

    /**
     * data : {"pushUserId":["0c264a82887746e4a35b3b2c4006891c"]}
     * msg : 评论微博成功
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
        private List<String> pushUserId;

        public List<String> getPushUserId() {
            return pushUserId;
        }

        public void setPushUserId(List<String> pushUserId) {
            this.pushUserId = pushUserId;
        }
    }
}
