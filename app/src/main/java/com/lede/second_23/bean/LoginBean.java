package com.lede.second_23.bean;

/**
 * Created by ld on 17/3/29.
 */

public class LoginBean {


    /**
     * data : {"access_token":"nMYui4ElfK3n9G5R01cmXS3gvBXdTj1u","expires_in":604800000,"user":{"id":"9e7a060b521049bb990dedc6055b7886","password":"pzy6352019","phone":"15720503385","time":1492676787201,"token":"nMYui4ElfK3n9G5R01cmXS3gvBXdTj1u","userParamId":1}}
     * result : 10000
     */

    private DataBean data;
    private int result;
    private String msg;

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static class DataBean {
        /**
         * access_token : nMYui4ElfK3n9G5R01cmXS3gvBXdTj1u
         * expires_in : 604800000
         * user : {"id":"9e7a060b521049bb990dedc6055b7886","password":"pzy6352019","phone":"15720503385","time":1492676787201,"token":"nMYui4ElfK3n9G5R01cmXS3gvBXdTj1u","userParamId":1}
         */

        private String access_token;
        private int expires_in;
        private UserBean user;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 9e7a060b521049bb990dedc6055b7886
             * password : pzy6352019
             * phone : 15720503385
             * time : 1492676787201
             * token : nMYui4ElfK3n9G5R01cmXS3gvBXdTj1u
             * userParamId : 1
             */

            private String id;
            private String password;
            private String phone;
            private long time;
            private String token;
            private int userParamId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getUserParamId() {
                return userParamId;
            }

            public void setUserParamId(int userParamId) {
                this.userParamId = userParamId;
            }
        }
    }
}
