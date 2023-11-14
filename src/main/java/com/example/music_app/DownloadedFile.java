package com.example.music_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DownloadedFile extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100;
    private RecyclerView recyclerView;
    ArrayList<DownloadData> arrayList=new ArrayList<DownloadData>();
    LinearLayoutManager layoutManager;
    DownloadFileAdapter downloadFileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_file);
       recyclerView=(RecyclerView) findViewById(R.id.download_file_recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        checkPermission();

    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(DownloadedFile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(DownloadedFile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(DownloadedFile.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            Log.e("Else","Else");
            video();
        }
    }

    private void video() {
        String path=Environment.getExternalStorageDirectory()+"/DailymotionApp/";
        File directory=new File(path);

        if (!directory.exists()){
            directory.mkdirs();
        }
        File[] file = directory.listFiles();

        for (File value : file) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
            Date date = new Date(value.lastModified());
            String current_date=dateFormat.format(date);
            String sizeInMB= Formatter.formatShortFileSize(this,value.length());
            DownloadData downloadData =new DownloadData();
            downloadData.setThumbnail_url(value.getAbsolutePath());
            downloadData.setDownloadTitle(value.getName());
            downloadData.setDownloadSize(sizeInMB);
            downloadData.setDownloadDateTime(current_date);
            downloadData.setVideoPath(value.getAbsolutePath());
            arrayList.add(downloadData);


        }
        downloadFileAdapter=new DownloadFileAdapter(this,arrayList);
        recyclerView.setAdapter(downloadFileAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSIONS:{
                for (int i=0 ; i<grantResults.length ; i++){
                    if (grantResults.length>0&&grantResults[i]==PackageManager.PERMISSION_GRANTED){
                        video();
                    }else {
                        Toast.makeText(DownloadedFile.this,
                                "The app was not allowed to read or write to your storage. " +
                                        "Hence, it cannot function properly. Please consider granting it this permission",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }
}

