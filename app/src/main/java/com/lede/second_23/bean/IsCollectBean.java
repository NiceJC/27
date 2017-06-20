package com.lede.second_23.bean;

/**
 * Created by ld on 17/5/31.
 */

public class IsCollectBean {

    /**
     * data : {"collect":false}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * collect : false
         */

        private boolean collect;

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }
    }
}
