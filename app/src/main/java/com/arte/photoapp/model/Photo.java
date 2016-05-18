package com.arte.photoapp.model;


import java.io.Serializable;

public class Photo {

    private String id;
    private String title;
    private int farm;
    private int serverId;
    private String secret;
    private String url;
    private String thumbnailUrl;

    public Photo() {

    }

    public void generateUrls() {
        setUrl("https://farm" + getFarm() + ".staticflickr.com/" + getServerId() + "/" + getId() + "_" + getSecret() + ".jpg");
        setThumbnailUrl("https://farm" + getFarm() + ".staticflickr.com/" + getServerId() + "/" + getId() + "_" + getSecret() + "_s.jpg");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
