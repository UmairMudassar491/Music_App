package com.example.music_app;

import java.io.Serializable;

public class DownloadVideo implements Serializable {
    public String size, type, link, name, page, website;
    public boolean chunked;
}
