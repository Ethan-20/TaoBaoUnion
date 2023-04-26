package com.example.taobaounion.model.domain;

import java.util.List;

public class SearchRecommend  {
    /**
     * success : true
     * code : 10000
     * message : 获取成功
     * data : [{"id":"1250359232734244864","keyword":"iPhone","createTime":"2020-04-15 17:44"},{"id":"965250162605686784","keyword":"祛痘","createTime":"2022-04-17 13:59"},{"id":"965250200148901888","keyword":"零食","createTime":"2022-04-17 13:59"},{"id":"965250214866714624","keyword":"笔记本","createTime":"2022-04-17 13:59"},{"id":"965250293396668416","keyword":"手机壳","createTime":"2022-04-17 14:00"},{"id":"965250307606970368","keyword":"爆米花","createTime":"2022-04-17 14:00"},{"id":"965250324459683840","keyword":"薯片","createTime":"2022-04-17 14:00"},{"id":"965250339596926976","keyword":"充电线","createTime":"2022-04-17 14:00"},{"id":"965250352527966208","keyword":"耳机","createTime":"2022-04-17 14:00"},{"id":"965250372320886784","keyword":"牛仔裤","createTime":"2022-04-17 14:00"}]
     */

    private Boolean success;
    private Integer code;
    private String message;
    private List<DataBean> data;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1250359232734244864
         * keyword : iPhone
         * createTime : 2020-04-15 17:44
         */

        private String id;
        private String keyword;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
