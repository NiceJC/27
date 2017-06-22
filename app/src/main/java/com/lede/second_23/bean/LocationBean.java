package com.lede.second_23.bean;

/**
 * Created by ld on 17/6/2.
 */

public class LocationBean {


    /**
     * data : {"userAmap":{"lat":"30.303972","lon":"120.369257","type":"0","userId":"9e7a060b521049bb990dedc6055b7886"}}
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
         * userAmap : {"lat":"30.303972","lon":"120.369257","type":"0","userId":"9e7a060b521049bb990dedc6055b7886"}
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
             * lat : 30.303972
             * lon : 120.369257
             * type : 0
             * userId : 9e7a060b521049bb990dedc6055b7886
             */

            private String lat;
            private String lon;
            private String type;
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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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
