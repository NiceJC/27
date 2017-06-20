package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/5/31.
 */

public class BilateralBean {

    /**
     * data : {"hasNextPage":false,"list":[{"imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170419154730870978056.jpg","nickName":"番茄炒蛋","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}],"nextPage":0,"pageNum":1,"pageSize":100,"total":1}
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
         * list : [{"imgUrl":"http://7xr1tb.com1.z0.glb.clouddn.com/20170419154730870978056.jpg","nickName":"番茄炒蛋","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}]
         * nextPage : 0
         * pageNum : 1
         * pageSize : 100
         * total : 1
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
             * imgUrl : http://7xr1tb.com1.z0.glb.clouddn.com/20170419154730870978056.jpg
             * nickName : 番茄炒蛋
             * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
             */

            private String imgUrl;
            private String nickName;
            private String userId;

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
}
