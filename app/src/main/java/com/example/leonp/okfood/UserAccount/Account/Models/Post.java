package com.example.leonp.okfood.UserAccount.Account.Models;

/**
 * Created by leonp on 4/22/2018.
 */

public class Post {

    private String title;
    private String upvotes;
    private String numberOfComments;
    private String user_id;
    private String post_id;
    private String image_path;
    private String date_created;
    private String post_description;
    private String position;

    public Post(String title, String upvotes, String numberOfComments, String user_id,
                String post_id, String image_path, String date_created, String post_description, String position) {
        this.title = title;
        this.upvotes = upvotes;
        this.numberOfComments = numberOfComments;
        this.user_id = user_id;
        this.post_id = post_id;
        this.image_path = image_path;
        this.date_created = date_created;
        this.post_description = post_description;
        this.position = position;
    }

    public Post() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public String getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(String numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", upvotes='" + upvotes + '\'' +
                ", numberOfComments='" + numberOfComments + '\'' +
                ", user_id='" + user_id + '\'' +
                ", post_id='" + post_id + '\'' +
                ", image_path='" + image_path + '\'' +
                ", date_created='" + date_created + '\'' +
                ", post_description='" + post_description + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
