package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/11/24.
 */

public class NearbyPhotoBean {

    private int result;
    private String msg;
    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

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

    public class DataBean{
        private int userPhotoListSize;
        private List<UserPhotoBean> userPhotoList;

        public int getUserPhotoListSize() {
            return userPhotoListSize;
        }

        public void setUserPhotoListSize(int userPhotoListSize) {
            this.userPhotoListSize = userPhotoListSize;
        }

        public List<UserPhotoBean> getUserPhotoList() {
            return userPhotoList;
        }

        public void setUserPhotoList(List<UserPhotoBean> userPhotoList) {
            this.userPhotoList = userPhotoList;
        }

        public class UserPhotoBean{
            String creatTime;
            int id;
            String photoId;
            String urlFirst;
            String urlHead;
            String urlImg;
            String urlVideo;
            String userId;

            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }


            public String getPhotoId() {
                return photoId;
            }

            public void setPhotoId(String photoId) {
                this.photoId = photoId;
            }

            public String getUrlFirst() {
                return urlFirst;
            }

            public void setUrlFirst(String urlFirst) {
                this.urlFirst = urlFirst;
            }

            public String getUrlHead() {
                return urlHead;
            }

            public void setUrlHead(String urlHead) {
                this.urlHead = urlHead;
            }

            public String getUrlImg() {
                return urlImg;
            }

            public void setUrlImg(String urlImg) {
                this.urlImg = urlImg;
            }

            public String getUrlVideo() {
                return urlVideo;
            }

            public void setUrlVideo(String urlVideo) {
                this.urlVideo = urlVideo;
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
