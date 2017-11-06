package com.lede.second_23.bean;

/**
 * Created by ld on 17/11/2.
 */

public class IfConcernBean  {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        boolean collect;

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }
    }
}
