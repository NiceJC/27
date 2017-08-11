package com.lede.second_23.bean;

import java.util.List;

/**
 * Created by ld on 17/8/11.
 */

public class Forum {
    private String token;
    private Long forumId;//（时间戳+6位随机数）
    private String userId;
    private Integer type;
    private String text;
    private List<ForumImg> imgs;
    private ForumMedia forumMedia;

    public Forum(String token,Long forumId, String userId, Integer type, String text, List<ForumImg> imgs) {
        this.token=token;
        this.forumId = forumId;
        this.userId = userId;
        this.type = type;
        this.text = text;
        this.imgs = imgs;
    }

    public Forum(String token,Long forumId, String userId, Integer type, String text, ForumMedia forumMedia) {
        this.token=token;
        this.forumId = forumId;
        this.userId = userId;
        this.type = type;
        this.text = text;
        this.forumMedia = forumMedia;
    }
}

