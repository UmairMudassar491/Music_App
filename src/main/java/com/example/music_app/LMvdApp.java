package com.example.music_app;

import android.app.Application;
import android.content.Intent;

public class LMvdApp extends Application {
    private static LMvdApp instance;
    private Intent downloadService;
    //private LMvdActivity.OnBackPressedListener onBackPressedListener;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        downloadService = new Intent(getApplicationContext(), DownloadManager.class);
    }

    public Intent getDownloadService() {
        return downloadService;
    }

    public static LMvdApp getInstance() {
        return instance;
    }

    /*public LMvdActivity.OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }

    public void setOnBackPressedListener(LMvdActivity.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }*/
}