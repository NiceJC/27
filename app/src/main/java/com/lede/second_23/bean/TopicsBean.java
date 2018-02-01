package com.lede.second_23.bean;

import java.util.List;

/**
 * 主题查询类
 * Created by ld on 18/1/22.
 */

public class TopicsBean {


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

                private String creatTime;
                private int id;
                private String photoName;
                private int stauts;
                private int type;
                private String urlOne;
                private long uuid;

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

                public String getPhotoName() {
                    return photoName;
                }

                public void setPhotoName(String photoName) {
                    this.photoName = photoName;
                }

                public int getStauts() {
                    return stauts;
                }

                public void setStauts(int stauts) {
                    this.stauts = stauts;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getUrlOne() {
                    return urlOne;
                }

                public void setUrlOne(String urlOne) {
                    this.urlOne = urlOne;
                }

                public long getUuid() {
                    return uuid;
                }

                public void setUuid(long uuid) {
                    this.uuid = uuid;
                }
            }

        }
    }



}
