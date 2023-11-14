package com.example.music_app;

public class DownloadData {
    private String thumbnail_url,downloadTitle,downloadSize,downloadDateTime,videoPath;



    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setDownloadTitle(String downloadTitle) {
        this.downloadTitle = downloadTitle;
    }

    public void setDownloadSize(String downloadSize) {
        this.downloadSize = downloadSize;
    }

    public void setDownloadDateTime(String downloadDateTime) {
        this.downloadDateTime = downloadDateTime;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getDownloadTitle() {
        return downloadTitle;
    }

    public String getDownloadSize() {
        return downloadSize;
    }

    public String getDownloadDateTime() {
        return downloadDateTime;
    }
}
