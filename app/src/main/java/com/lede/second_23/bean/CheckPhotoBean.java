package com.lede.second_23.bean;

/**
 * Created by ld on 17/11/9.
 */

public class CheckPhotoBean {

    private String msg;
    private int result;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        private boolean isExist;

        public boolean isExist() {
            return isExist;
        }

        public void setExist(boolean exist) {
            isExist = exist;
        }
    }
}
