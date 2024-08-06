package com.pnuppp.pplusplus;

import java.util.Objects;

public class RSSItem {
    private String title;
    private String link;
    private String pubDate;
    private String author;
    private String category;
    private String description;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RSSItem rssItem = (RSSItem) o;
        return title.equals(rssItem.title) && link.equals(rssItem.link) && pubDate.equals(rssItem.pubDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, pubDate);
    }
}
