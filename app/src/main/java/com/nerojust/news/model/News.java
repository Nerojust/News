package com.nerojust.news.model;

public class News {

    private String title;
    private String publishedAt;
    private String description;
    private String urlToImage;
    private String author;
    private String url;

    public News(String title, String publishedAt, String description, String urlToImage, String author, String url) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.description = description;
        this.urlToImage = urlToImage;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
