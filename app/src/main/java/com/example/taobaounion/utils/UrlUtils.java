package com.example.taobaounion.utils;

public class UrlUtils {
    public static String createHomePagerUrl(int materialId,int page){
        return "discovery/"+materialId+"/"+page;
    }

    public static String getCoverPath(String url,int size) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url+"_"+size+"x"+size+".jpg";
        }
        else {
            return "https:" + url+"_"+size+"x"+size+".jpg";
        }
    }

    public static String getCoverPath(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        }
        else {
            return "https:" + url;
        }
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        }
        else {
            return "https:" + url;
        }
    }

    public static String getSelectedPageContentUrl(Integer categoryId) {
        return "recommend/"+categoryId;
    }

    public static String getOnSellPageUrl(int currentPage) {
        return "onSell/"+currentPage;
    }
}
