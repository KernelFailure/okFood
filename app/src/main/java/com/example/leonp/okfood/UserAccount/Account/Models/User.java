package com.example.leonp.okfood.UserAccount.Account.Models;

/**
 * Created by leonp on 4/21/2018.
 */

public class User {

    private String user_id;
    private String username;
    private String email;
    private String profile_image_path;

    public User(String user_id, String username, String email, String profile_image_path) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.profile_image_path = profile_image_path;
    }

    public User() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public String getProfile_image_path() {
        return profile_image_path;
    }

    public void setProfile_image_path(String profile_image_path) {
        this.profile_image_path = profile_image_path;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profile_image_path='" + profile_image_path + '\'' +
                '}';
    }
}
