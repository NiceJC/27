package com.lede.second_23.bean;

/**
 * Created by ld on 17/6/2.
 */

public class LocationBean {

    /**
     * data : {"userAmap":{"lat":"30.303799","lon":"120.369146","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}}
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
         * userAmap : {"lat":"30.303799","lon":"120.369146","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}
         */

        private UserAmapBean userAmap;

        public UserAmapBean getUserAmap() {
            return userAmap;
        }

        public void setUserAmap(UserAmapBean userAmap) {
            this.userAmap = userAmap;
        }

        public static class UserAmapBean {
            /**
             * lat : 30.303799
             * lon : 120.369146
             * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
             */

            private String lat;
            private String lon;
            private String userId;

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
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
