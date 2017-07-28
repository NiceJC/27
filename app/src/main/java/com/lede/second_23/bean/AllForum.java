package com.lede.second_23.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by ld on 17/7/13.
 */

public class AllForum {
    public AllForum(String token, Long forumId,String userId, String forumText, String latitude, String longitude,  List<AllRecord> allRecords) {
        this.token = token;
        this.forumId = forumId;
        this.forumText = forumText;
        this.userId=userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.allRecords = allRecords;
    }

    private Integer id;

    private String token;

    private Long forumId;

    private String userId;

    private String forumText;

    private String latitude;

    private String longitude;

    private String description;

    private Integer clickCount;

    private Integer type;

    private Integer classification;

    private Integer likeCount;

    private Boolean status;

    private Date creatTime;

    private List<AllRecord> allRecords;
}
