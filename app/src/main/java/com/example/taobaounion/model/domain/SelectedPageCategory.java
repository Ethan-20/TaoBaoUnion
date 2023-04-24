package com.example.taobaounion.model.domain;

import java.util.List;

public class SelectedPageCategory {

    /**
     * success : true
     * code : 10000
     * message : 获取精选分类成功.
     * data : [{"type":1,"favorites_id":19876595,"favorites_title":"程序员必备"},{"type":1,"favorites_id":19876636,"favorites_title":"办公室零食"},{"type":1,"favorites_id":19876637,"favorites_title":"上班族早餐"},{"type":1,"favorites_id":19876649,"favorites_title":"日用品"},{"type":1,"favorites_id":19902751,"favorites_title":"电脑周边"},{"type":1,"favorites_id":19903201,"favorites_title":"秋天必备"}]
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
         * type : 1
         * favorites_id : 19876595
         * favorites_title : 程序员必备
         */

        private Integer type;
        private Integer favorites_id;
        private String favorites_title;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getFavorites_id() {
            return favorites_id;
        }

        public void setFavorites_id(Integer favorites_id) {
            this.favorites_id = favorites_id;
        }

        public String getFavorites_title() {
            return favorites_title;
        }

        public void setFavorites_title(String favorites_title) {
            this.favorites_title = favorites_title;
        }
    }
}
