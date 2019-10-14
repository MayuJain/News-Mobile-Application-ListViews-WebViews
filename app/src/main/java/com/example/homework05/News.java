package com.example.homework05;

public class News {
    String Title;
    String publishedAt;
    String author;
    String urlToImage;
    String url;

    public News(String title, String publishedAt, String author, String urlToImage, String url) {
        this.Title = title;
        this.url = url;
        this.publishedAt = publishedAt;
        this.author = author;
        this.urlToImage = urlToImage;
    }
}