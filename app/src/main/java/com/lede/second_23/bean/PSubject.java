package com.lede.second_23.bean;

import java.io.Serializable;
import java.util.Date;

public class PSubject implements Serializable {

    public PSubject(Long uuid, String photoName, String pOne) {
        this.uuid = uuid;
        this.photoName = photoName;
        this.pOne = pOne;
    }

    public PSubject(Long uuid, String photoName) {
        this.uuid = uuid;
        this.photoName = photoName;
    }

    private Integer id;

    //主题ID（时间戳加6位随机数）
    private Long uuid;

    //不填
    private String photo;

    //主题名字
    private String photoName;

    //排序（不填）
    private Integer subOrder;

    private Integer type;

    private Integer stauts;

    private String desp;

    //图片名字（时间戳加6位随机数）

    private String pOne;

    private String pTwo;

    private String pThree;

    private String pFour;

    private Date creatTime;




}