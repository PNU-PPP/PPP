package com.pnuppp.pplusplus;

public class HtmlItem {
    private String title;
    private String date;
    private String author; // 작성자명 필드 추가
    private String url; // URL 필드 추가

    // Constructor
    public HtmlItem(String title, String date, String author, String url) {
        this.title = title;
        this.date = date;
        this.author = author; // 추가된 필드 초기화
        this.url = url;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getAuthor() { return author; } // 작성자명 getter
    public void setAuthor(String author) { this.author = author; } // 작성자명 setter

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
