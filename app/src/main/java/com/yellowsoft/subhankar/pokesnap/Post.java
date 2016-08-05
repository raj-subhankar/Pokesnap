package com.yellowsoft.subhankar.pokesnap;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by subhankar on 7/17/2016.
 */
public class Post {

    @SerializedName("userName")
    private String userName;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("postBody")
    private String postBody;
    @SerializedName("_id")
    private String _id;
    @SerializedName("team")
    private String team;
    @SerializedName("likesCount")
    private Integer likesCount;
    @SerializedName("likes")
    private ArrayList<String> likes;

    public Post(String userName, String imageUrl, String postBody, String _id, Integer likesCount) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.postBody = postBody;
        this._id = _id;
        this.likesCount = likesCount;


    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public void addLike(String user) {likes.add(user);}

    public void removeLike(String user) {likes.remove(user);}

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public void increaseLike() { likesCount = likesCount + 1;}

    public void decreaseLike() { likesCount = likesCount - 1;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
