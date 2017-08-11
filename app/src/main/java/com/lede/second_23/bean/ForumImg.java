package com.lede.second_23.bean;

/**
 * Created by ld on 17/8/11.
 */

public class ForumImg {
    private Long imgId;//（时间戳+3位随机数）
    private Long forumId;//（同上）
    private String userId;



    private String name; //（图片名字时间戳+6位随机数）
    public ForumImg(Long imgId, Long forumId, String userId, String name) {
        this.imgId = imgId;
        this.forumId = forumId;
        this.userId = userId;
        this.name = name;
    }
}
