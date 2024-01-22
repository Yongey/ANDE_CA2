package com.example.ca1_mainscreen;

public class User {

    private String imageUrl;
    private String username;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User( String username, String email,String imageUrl) {
        this.imageUrl = imageUrl;
        this.username = username;
        this.email = email;
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
