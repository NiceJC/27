package com.lede.second_23.bean;

import java.util.Date;

/**
 * Created by ld on 17/7/13.
 */

public class AllRecord {

    public AllRecord(Integer type, Integer recordOrder, String videoFirst, String videoName, String photoName, String userId, Long forumId,Date creatTime) {
        this.type = type;
        this.recordOrder = recordOrder;
        this.videoFirst = videoFirst;
        this.videoName = videoName;
        this.photoName = photoName;
        this.userId = userId;
        this.forumId = forumId;
        this.creatTime=creatTime;
    }

    private Integer id;

    private Long forumId;

    private String userId;

//    @JSONField(serialize = false)
    private String photoName;

//    @JSONField(serialize = false)
    private String videoName;

//    @JSONField(serialize = false)
    private String videoFirst;

    private Integer recordOrder;

    private Integer type;

    private Date creatTime;
}
