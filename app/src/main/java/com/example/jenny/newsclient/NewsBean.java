package com.example.jenny.newsclient;

public class NewsBean {
    private String imageUrl;
    private String title;
    private String author;
    private String date;
    private String detailUrl;
    private String category;

    public String getDetail() {
        return detailUrl;
    }

    public void setDetail(String detail) {
        this.detailUrl = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public NewsBean(String imageUrl, String title, String author, String date, String detailUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.author = author;
        this.date = date;
        this.detailUrl = detailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
