package com.lede.second_23.bean;

import java.util.List;

/**
 * 系统推送的用户
 * Created by ld on 17/7/31.
 */

public class PushUserBean {

    /**
     * data : {"userInfos":[{"imgUrl":"http://my-photo.lacoorent.com/20170718161150916819835.jpg","nickName":"测试账号4","userId":"15541fc2790e496491f40793de09dee1"},{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"哈喽","userId":"1fa48078987d405baeefc6d94e4fc216"},{"imgUrl":"http://my-photo.lacoorent.com/20170622152734253454155.jpg","nickName":"某宇","userId":"40a0f4aef97e4f7f8ff2e86220e8bfd2"},{"imgUrl":"http://my-photo.lacoorent.com/20170718161150916819835.jpg","nickName":"不爱吃草的坏兔子","userId":"0c264a82887746e4a35b3b2c4006891c"},{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"测试账号9","userId":"ee59fb2659654db69352fd34f85d642c"},{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"测试账号5","userId":"5d428075744c4307866f99de2f1cc199"},{"imgUrl":"http://my-photo.lacoorent.com/20170612180233020679146.jpg","nickName":"msong27","userId":"328de4b3f7304bbca5967766c1001d22"},{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"测试账号2","userId":"5b4e38090d2943ee865c56a60a514131"},{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"番茄炒蛋ggg","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"},{"imgUrl":"http://my-photo.lacoorent.com/20170627160236786746479.jpg","nickName":"Ag","userId":"9e7a060b521049bb990dedc6055b7886"}]}
     * msg : 推送用户
     */

    private DataBean data;
    private String msg;

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

    public static class DataBean {
        private List<UserInfosBean> userInfos;

        public List<UserInfosBean> getUserInfos() {
            return userInfos;
        }

        public void setUserInfos(List<UserInfosBean> userInfos) {
            this.userInfos = userInfos;
        }

        public static class UserInfosBean {
            /**
             * imgUrl : http://my-photo.lacoorent.com/20170718161150916819835.jpg
             * nickName : 测试账号4
             * userId : 15541fc2790e496491f40793de09dee1
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
