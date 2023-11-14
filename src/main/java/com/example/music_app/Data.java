package com.example.music_app;

public class Data {
    private String thumbnail_url,title,channel,owner,id;


    public Data(String thumbnail_url, String title, String channel, String owner, String id) {
        this.thumbnail_url = thumbnail_url;
        this.title = title;
        this.channel = channel;
        this.owner = owner;
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getThumbnail_url() {
        return thumbnail_url;
    }
    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public String getOwner() {
        return owner;
    }
}
