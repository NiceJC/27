package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/12/7.
 */

public class MatingVerifyBean {
    private String msg;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{


        private List<UserMarryBean> userMarrie;

        public List<UserMarryBean> getUserMarrie() {
            return userMarrie;
        }

        public void setUserMarrie(List<UserMarryBean> userMarrie) {
            this.userMarrie = userMarrie;
        }

        public class UserMarryBean{

            private String creatTime;
            private int id;
            private String marryDesp;
            private int type;
            private String updateTime;
            private String userId;

            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMarryDesp() {
                return marryDesp;
            }

            public void setMarryDesp(String marryDesp) {
                this.marryDesp = marryDesp;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
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
