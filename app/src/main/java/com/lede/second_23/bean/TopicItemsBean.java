package com.lede.second_23.bean;

import java.util.List;

/**
 * 版块查询类
 * Created by ld on 18/1/23.
 */

public class TopicItemsBean {


    private DataBean data;
    private String msg;
    private int result;

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static class DataBean{

        private SimpleBean simple;

        public SimpleBean getSimple() {
            return simple;
        }

        public void setSimple(SimpleBean simple) {
            this.simple = simple;
        }

        public static class SimpleBean{

            private List<ListBean> list;
            private boolean hasNextPage;
            private int nextPage;
            private int pageNum;
            private int pageSize;
            private int total;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public boolean isHasNextPage() {
                return hasNextPage;
            }

            public void setHasNextPage(boolean hasNextPage) {
                this.hasNextPage = hasNextPage;
            }

            public int getNextPage() {
                return nextPage;
            }

            public void setNextPage(int nextPage) {
                this.nextPage = nextPage;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public static class ListBean{
                private String businessName;
                private String cityName;
                private String creatTime;
                private String detailsAll;
                private String detailsName;
                private int id;
                private String labelName;
                private int status;
                private int type;
                private String urlFive;
                private String urlFour;
                private String urlOne;
                private String urlSix;
                private String urlThree;
                private String urlTwo;
                private String urlnEight;
                private String urlnSeven;
                private String userId;
                private long uuid;
                private long uuidBySub;

                public String getBusinessName() {
                    return businessName;
                }

                public void setBusinessName(String businessName) {
                    this.businessName = businessName;
                }

                public String getCityName() {
                    return cityName;
                }

                public void setCityName(String cityName) {
                    this.cityName = cityName;
                }

                public String getCreatTime() {
                    return creatTime;
                }

                public void setCreatTime(String creatTime) {
                    this.creatTime = creatTime;
                }

                public String getDetailsAll() {
                    return detailsAll;
                }

                public void setDetailsAll(String detailsAll) {
                    this.detailsAll = detailsAll;
                }

                public String getDetailsName() {
                    return detailsName;
                }

                public void setDetailsName(String detailsName) {
                    this.detailsName = detailsName;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getLabelName() {
                    return labelName;
                }

                public void setLabelName(String labelName) {
                    this.labelName = labelName;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getUrlFive() {
                    return urlFive;
                }

                public void setUrlFive(String urlFive) {
                    this.urlFive = urlFive;
                }

                public String getUrlFour() {
                    return urlFour;
                }

                public void setUrlFour(String urlFour) {
                    this.urlFour = urlFour;
                }

                public String getUrlOne() {
                    return urlOne;
                }

                public void setUrlOne(String urlOne) {
                    this.urlOne = urlOne;
                }

                public String getUrlSix() {
                    return urlSix;
                }

                public void setUrlSix(String urlSix) {
                    this.urlSix = urlSix;
                }

                public String getUrlThree() {
                    return urlThree;
                }

                public void setUrlThree(String urlThree) {
                    this.urlThree = urlThree;
                }

                public String getUrlTwo() {
                    return urlTwo;
                }

                public void setUrlTwo(String urlTwo) {
                    this.urlTwo = urlTwo;
                }

                public String getUrlnEight() {
                    return urlnEight;
                }

                public void setUrlnEight(String urlnEight) {
                    this.urlnEight = urlnEight;
                }

                public String getUrlnSeven() {
                    return urlnSeven;
                }

                public void setUrlnSeven(String urlnSeven) {
                    this.urlnSeven = urlnSeven;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public long getUuid() {
                    return uuid;
                }

                public void setUuid(long uuid) {
                    this.uuid = uuid;
                }

                public long getUuidBySub() {
                    return uuidBySub;
                }

                public void setUuidBySub(long uuidBySub) {
                    this.uuidBySub = uuidBySub;
                }
            }

        }
    }


}
