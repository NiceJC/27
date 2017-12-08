package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/12/4.
 */

public class MatchedUserBean {

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

        private UserInfoList userInfoList;

        private List<UserMarryBean> userMarry;

        public UserInfoList getUserInfoList() {
            return userInfoList;
        }

        public void setUserInfoList(UserInfoList userInfoList) {
            this.userInfoList = userInfoList;
        }

        public List<UserMarryBean> getUserMarry() {
            return userMarry;
        }

        public void setUserMarry(List<UserMarryBean> userMarry) {
            this.userMarry = userMarry;
        }

        public class UserInfoList{

            private boolean hasNextPage;
            private List<NewMatingUserBean.DataBean.UserInfoListBean> list;
            private int nextPage;
            private int pageNum;
            private int pageSize;
            private int total;

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }


            public List<NewMatingUserBean.DataBean.UserInfoListBean> getList() {
                return list;
            }

            public void setList(List<NewMatingUserBean.DataBean.UserInfoListBean> list) {
                this.list = list;
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
