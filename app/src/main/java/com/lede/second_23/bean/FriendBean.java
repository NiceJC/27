package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/5/31.
 */

public class FriendBean {


    /**
     * data : [{"friend":true,"imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170419154730870978056.jpg","nickName":"番茄炒蛋","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}]
     * msg : 请求成功
     * result : 10000
     */

    private String msg;
    private int result;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * friend : true
         * imgUrl : http://7xr1tb.com1.z0.glb.clouddn.com/20170419154730870978056.jpg
         * nickName : 番茄炒蛋
         * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
         */

        private boolean friend;
        private String imgUrl;
        private String nickName;
        private String userId;

        public boolean isFriend() {
            return friend;
        }

        public void setFriend(boolean friend) {
            this.friend = friend;
        }

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
