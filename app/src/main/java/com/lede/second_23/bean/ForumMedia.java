package com.lede.second_23.bean;

/**
 * Created by ld on 17/8/11.
 */

public class ForumMedia {
    private Long mediaId;//（时间戳+3位随机数）
    private Long forumId;//（同上）
    private String mediaPath;//（视频名字时间戳+6位随机数）
    private String mediaPicturePath;//（视频第一张名字时间戳+6位随机数）
    private String userId;

    public ForumMedia(Long mediaId, Long forumId, String mediaPath, String mediaPicturePath, String userId) {
        this.mediaId = mediaId;
        this.forumId = forumId;
        this.mediaPath = mediaPath;
        this.mediaPicturePath = mediaPicturePath;
        this.userId = userId;
    }
}
