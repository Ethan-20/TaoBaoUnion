package com.example.taobaounion.model.domain;

public class TicketParams {
    private String url;

    public TicketParams(String url, String title) {
        this.url = url;
        this.title = title;
    }

    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
