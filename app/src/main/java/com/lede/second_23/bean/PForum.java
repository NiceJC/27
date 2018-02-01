package com.lede.second_23.bean;

import java.io.Serializable;
import java.util.Date;

public class PForum implements Serializable {

    public PForum(String userId, Long forumId, Long uuidBySub, Long uuid, String forumName, String pOne,String desp) {
        this.userId = userId;
        this.forumId = forumId;
        this.uuidBySub = uuidBySub;
        this.uuid = uuid;
        this.forumName = forumName;
        this.pOne = pOne;
        this.desp=desp;
    }
    public PForum(String userId, Long forumId, Long uuidBySub, Long uuid, String forumName,String desp) {
        this.userId = userId;
        this.forumId = forumId;
        this.uuidBySub = uuidBySub;
        this.uuid = uuid;
        this.forumName = forumName;
        this.desp=desp;
    }


    private Integer id;

    //发布的用户ID
    private String userId;

    //不填
    private String userName;

    //时间戳加6位随机数
    private Long forumId;

    //对应的主题模块ID
    private Long uuidBySub;

    //对应的主题ID
    private Long uuid;

    //发布内容
    private String forumName;

    //图片
    private String pOne;

    private String pTwo;

    private String pThree;

    private String pFour;

    private Integer type;

    private Integer status;

    //是否当前城市（0-是，1-不是）
    private String desp;

    private Date creatTime;


}