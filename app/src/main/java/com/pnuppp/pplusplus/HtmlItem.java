package com.pnuppp.pplusplus;

public class HtmlItem {
    private String number;
    private String title;
    private String date;
    private String attachmentCount;
    private String views;
    private String url; // URL 필드 추가

    // Constructor
    public HtmlItem(String number, String title, String date, String attachmentCount, String views) {
        this.number = number;
        this.title = title;
        this.date = date;
        this.attachmentCount = attachmentCount;
        this.views = views;
        this.url = url;
    }

    // Getters and setters
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getAttachmentCount() { return attachmentCount; }
    public void setAttachmentCount(String attachmentCount) { this.attachmentCount = attachmentCount; }

    public String getViews() { return views; }
    public void setViews(String views) { this.views = views; }
}
