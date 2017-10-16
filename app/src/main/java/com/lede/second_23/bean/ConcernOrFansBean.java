package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/4/25.
 */

public class ConcernOrFansBean {


    /**
     * data : {"hasNextPage":false,"list":[{"friend":false,"imgUrl":"http://my-photo.lacoorent.com/20170627174315328616589.jpg","nickName":"达先生","trueName":"0","userId":"793922342ae3495b8ae3a8292f5c1e44"},{"friend":false,"imgUrl":"http://my-photo.lacoorent.com/20170627174315328616589.jpg","nickName":"达先生","trueName":"0","userId":"793922342ae3495b8ae3a8292f5c1e44"},{"friend":true,"imgUrl":"http://my-photo.lacoorent.com/20170903120905063447292.jpg","nickName":"TWENTY-SEVEN","trueName":"0","userId":"ae162a6fbffc4689b34fbc05d2a1ce38"}],"nextPage":0,"pageNum":1,"pageSize":3,"total":3}
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
         * hasNextPage : false
         * list : [{"friend":false,"imgUrl":"http://my-photo.lacoorent.com/20170627174315328616589.jpg","nickName":"达先生","trueName":"0","userId":"793922342ae3495b8ae3a8292f5c1e44"},{"friend":false,"imgUrl":"http://my-photo.lacoorent.com/20170627174315328616589.jpg","nickName":"达先生","trueName":"0","userId":"793922342ae3495b8ae3a8292f5c1e44"},{"friend":true,"imgUrl":"http://my-photo.lacoorent.com/20170903120905063447292.jpg","nickName":"TWENTY-SEVEN","trueName":"0","userId":"ae162a6fbffc4689b34fbc05d2a1ce38"}]
         * nextPage : 0
         * pageNum : 1
         * pageSize : 3
         * total : 3
         */

        private boolean hasNextPage;
        private int nextPage;
        private int pageNum;
        private int pageSize;
        private int total;
        private List<ListBean> list;

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * friend : false
             * imgUrl : http://my-photo.lacoorent.com/20170627174315328616589.jpg
             * nickName : 达先生
             * trueName : 0
             * userId : 793922342ae3495b8ae3a8292f5c1e44
             */

            private boolean friend;
            private String imgUrl;
            private String nickName;
            private String trueName;
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

            public String getTrueName() {
                return trueName;
            }

            public void setTrueName(String trueName) {
                this.trueName = trueName;
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
