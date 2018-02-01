package com.lede.second_23.bean;

import java.io.Serializable;
import java.util.Date;

public class PNotes implements Serializable {



    public PNotes(String userId, Long uuid, Long uuidBySub, String businessName, String detailsName, String detailsAll, String cityName, String labelName, String pOne, String pTwo, String pThree, String pFour, String pFive, String pSix) {
        this.userId = userId;
        this.uuid = uuid;
        this.uuidBySub = uuidBySub;
        this.businessName = businessName;
        this.detailsName = detailsName;
        this.detailsAll = detailsAll;
        this.cityName = cityName;
        this.labelName = labelName;
        this.pOne = pOne;
        this.pTwo = pTwo;
        this.pThree = pThree;
        this.pFour = pFour;
        this.pFive = pFive;
        this.pSix = pSix;
    }
    public PNotes(String userId, Long uuid, Long uuidBySub, String businessName, String detailsName, String detailsAll, String cityName, String labelName) {
        this.userId = userId;
        this.uuid = uuid;
        this.uuidBySub = uuidBySub;
        this.businessName = businessName;
        this.detailsName = detailsName;
        this.detailsAll = detailsAll;
        this.cityName = cityName;
        this.labelName = labelName;

    }




    public PNotes(String userId, Long uuid, Long uuidBySub, String businessName, String detailsName, String detailsAll, String cityName, String labelName, String pOne, String pTwo, String pThree, String pFour, String pFive, String pSix,String pSeven,String pEight) {
        this.userId = userId;
        this.uuid = uuid;
        this.uuidBySub = uuidBySub;
        this.businessName = businessName;
        this.detailsName = detailsName;
        this.detailsAll = detailsAll;
        this.cityName = cityName;
        this.labelName = labelName;
        this.pOne = pOne;
        this.pTwo = pTwo;
        this.pThree = pThree;
        this.pFour = pFour;
        this.pFive = pFive;
        this.pSix = pSix;
        this.nSeven=pSeven;
        this.nEight=pEight;
    }
    private Integer id;

    //创建主题的用户商家ID
    private String userId;

    //对应的主题id
    private Long uuid;

    //主题模块ID(时间戳加6位随机数)
    private Long uuidBySub;

    //商家名称
    private String businessName;

    //名称
    private String detailsName;

    //简介
    private String detailsAll;

    //城市
    private String cityName;

    //标签
    private String labelName;

    //用户商家头像（不填写）
    private String userImg;

    //板块排序（不填写）
    private Integer notesOrder;

    //第一张图片（时间戳加6位随机数）

    private String pOne;

    //第二张图片（时间戳加6位随机数）

    private String pTwo;

    //第三张图片（时间戳加6位随机数）

    private String pThree;

    //第四张图片（时间戳加6位随机数）

    private String pFour;

    //第五张图片（时间戳加6位随机数）

    private String pFive;

    //第六张图片（时间戳加6位随机数）

    private String pSix;

    //视频第一张图片名字（时间戳加6位随机数）

    private String nSeven;

    //视频名字（时间戳加6位随机数）

    private String nEight;

    private Integer type;

    private Integer status;

    private Date creatTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getUuidBySub() {
        return uuidBySub;
    }

    public void setUuidBySub(Long uuidBySub) {
        this.uuidBySub = uuidBySub;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDetailsName() {
        return detailsName;
    }

    public void setDetailsName(String detailsName) {
        this.detailsName = detailsName;
    }

    public String getDetailsAll() {
        return detailsAll;
    }

    public void setDetailsAll(String detailsAll) {
        this.detailsAll = detailsAll;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Integer getNotesOrder() {
        return notesOrder;
    }

    public void setNotesOrder(Integer notesOrder) {
        this.notesOrder = notesOrder;
    }

    public String getpOne() {
        return pOne;
    }

    public void setpOne(String pOne) {
        this.pOne = pOne;
    }

    public String getpTwo() {
        return pTwo;
    }

    public void setpTwo(String pTwo) {
        this.pTwo = pTwo;
    }

    public String getpThree() {
        return pThree;
    }

    public void setpThree(String pThree) {
        this.pThree = pThree;
    }

    public String getpFour() {
        return pFour;
    }

    public void setpFour(String pFour) {
        this.pFour = pFour;
    }

    public String getpFive() {
        return pFive;
    }

    public void setpFive(String pFive) {
        this.pFive = pFive;
    }

    public String getpSix() {
        return pSix;
    }

    public void setpSix(String pSix) {
        this.pSix = pSix;
    }

    public String getnSeven() {
        return nSeven;
    }

    public void setnSeven(String nSeven) {
        this.nSeven = nSeven;
    }

    public String getnEight() {
        return nEight;
    }

    public void setnEight(String nEight) {
        this.nEight = nEight;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
}