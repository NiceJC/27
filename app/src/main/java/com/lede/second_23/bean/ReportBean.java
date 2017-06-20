package com.lede.second_23.bean;

/**
 * Created by ld on 17/6/6.
 */

public class ReportBean {

    /**
     * data : {"userReport":{"creatTime":"2017-06-06 17:26:12","forumId":"192","id":2,"reportText":"123","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}}
     * msg : 请求成功
     * result : 10000
     */

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

    public static class DataBean {
        /**
         * userReport : {"creatTime":"2017-06-06 17:26:12","forumId":"192","id":2,"reportText":"123","userId":"84ba77bc08ea4e1d8c03c06f6f6c79e5"}
         */

        private UserReportBean userReport;

        public UserReportBean getUserReport() {
            return userReport;
        }

        public void setUserReport(UserReportBean userReport) {
            this.userReport = userReport;
        }

        public static class UserReportBean {
            /**
             * creatTime : 2017-06-06 17:26:12
             * forumId : 192
             * id : 2
             * reportText : 123
             * userId : 84ba77bc08ea4e1d8c03c06f6f6c79e5
             */

            private String creatTime;
            private String forumId;
            private int id;
            private String reportText;
            private String userId;

            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public String getForumId() {
                return forumId;
            }

            public void setForumId(String forumId) {
                this.forumId = forumId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getReportText() {
                return reportText;
            }

            public void setReportText(String reportText) {
                this.reportText = reportText;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
