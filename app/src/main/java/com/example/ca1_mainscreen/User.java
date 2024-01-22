package com.example.ca1_mainscreen;

public class User {

    private String imageUrl;

    public User(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
