package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/30.
 */

public class BannerBean {

    /**
     * data : {"allBannerList":[{"bannerId":26,"createTime":"2017-08-29 18:44:57","img":"banner20170829646.png","sort":0,"urlPic":"http://my-photo.lacoorent.com/banner20170829646.png"}]}
     * msg : Banner
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
        private List<AllBannerListBean> allBannerList;

        public List<AllBannerListBean> getAllBannerList() {
            return allBannerList;
        }

        public void setAllBannerList(List<AllBannerListBean> allBannerList) {
            this.allBannerList = allBannerList;
        }

        public static class AllBannerListBean {
            /**
             * bannerId : 26
             * createTime : 2017-08-29 18:44:57
             * img : banner20170829646.png
             * sort : 0
             * urlPic : http://my-photo.lacoorent.com/banner20170829646.png
             */

            private int bannerId;
            private String createTime;
            private String img;
            private int sort;
            private String urlPic;

            public int getBannerId() {
                return bannerId;
            }

            public void setBannerId(int bannerId) {
                this.bannerId = bannerId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getUrlPic() {
                return urlPic;
            }

            public void setUrlPic(String urlPic) {
                this.urlPic = urlPic;
            }
        }
    }
}
