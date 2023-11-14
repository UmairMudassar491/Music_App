package com.example.music_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;


public class AllVideos extends AppCompatActivity {
    EditText editText;
    Button clear,paste,download;
    private static final int REQUEST_PERMISSIONS = 100;
    public static final String CHANNEL_ID ="Channel";
    private String thumbnail;
    private String title;
    private String duration;
    String url;
    ProgressDialog progressDialog1,progressDialog2;
    int downloadId;
    int id=0;
    String path= Environment.getExternalStorageDirectory()+"/DailymotionApp/";
    NotificationManager notificationManager;
    NotificationCompat.Builder notification;
    NotificationChannel notificationChannel;
    String youtubeUrl = "youtube";
    String youtubeUrl1="youtu.be";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_videos);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            notificationChannel=new NotificationChannel(
                    CHANNEL_ID,"channel 1",NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }

        editText=findViewById(R.id.emptyEditText);
        clear=findViewById(R.id.clearButton);
        paste=findViewById(R.id.pasteButton);
        download=findViewById(R.id.downloadButton);

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text=clipboardManager.getText().toString();
                if (!text.isEmpty()) {
                    editText.setText(text);
                }else {
                    Toast.makeText(AllVideos.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=editText.getText().toString();
                if (text.contains(youtubeUrl1)||text.contains(youtubeUrl)){
                    Toast.makeText(AllVideos.this,"Youtube videos cannot download",Toast.LENGTH_SHORT).show();
                }else if (text.isEmpty()){
                       Toast.makeText(AllVideos.this, "Text is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    new Download().execute(text);
                }
                /*if (text.isEmpty()){
                    Toast.makeText(AllVideos.this, "Text is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    new Download().execute(text);
                }*/
            }
        });
    }
    private class Download extends AsyncTask<String,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1 = new ProgressDialog(AllVideos.this);
            progressDialog1.setTitle("Please wait");
            progressDialog1.setMessage("Loading...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.setCancelable(false);
            progressDialog1.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url("https://getvideo.p.rapidapi.com/?url="+ URLEncoder.encode(strings[0]))
                    .get()
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36")
                    .addHeader("x-rapidapi-host", "getvideo.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "7ef4e3d14cmsh30e3a3c34370373p130f03jsnbd4afd92a795")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                //Log.i("Response","response"+response.body().string());
                JSONObject object=new JSONObject(response.body().string());
                thumbnail =object.optString("thumbnail");
                title =object.getString("title");
                duration=object.getString("duration");
                //Log.i("Thumbnail","thumbnail"+thumbnail);
                JSONArray jsonArray=object.getJSONArray("streams");
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                url=jsonObject.getString("url");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog1 != null && progressDialog1.isShowing()) {
                progressDialog1.dismiss();
            }
            final Dialog dialog=new Dialog(AllVideos.this);
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageView imageView=dialog.findViewById(R.id.thumbnail);
            final TextView name = dialog.findViewById(R.id.downloadTitle);
            TextView time_duration =dialog.findViewById(R.id.downloadDuration);
            TextView download=dialog.findViewById(R.id.download);
            TextView cancel= dialog.findViewById(R.id.cancel);

            Glide.with(AllVideos.this)
                    .load(thumbnail)
                    .into(imageView);
            name.setText(title);
            time_duration.setText(duration);
            download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderCheckPermission();
                Downloader();
            }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
            });
            dialog.show();
        }
    }

    private void Downloader() {

        downloadId=PRDownloader.download(url,path,title+".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        progressDialog();
                        downloadNotificationManager();
                        Toast.makeText(AllVideos.this,"Download start",Toast.LENGTH_SHORT).show();

                    }
                }).setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {


                    }
                }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                    }
                }).setOnProgressListener(new OnProgressListener() {
                    int prev=0;
                    int progress2=0;
                    @Override
                    public void onProgress(Progress progress) {

                        float pr =  (((float)progress.currentBytes/progress.totalBytes)*100);
                        progress2=(int)pr;
                        if (progress2!=prev){
                            progressDialog2.setProgress(progress2);
                            notification.setProgress(100,progress2,false);
                            notificationManager.notify(id,notification.build());
                            prev=progress2;
                            //Log.i("progress2","progress2   "+progress2);
                        }


                    }
                }).start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        progressDialog2.dismiss();
                        notification.setContentText("Download completed");
                        notification.setProgress(0,0,false);
                        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        notificationManager.notify(id, notification.build());
                        Toast.makeText(AllVideos.this,"Download complete",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(AllVideos.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void downloadNotificationManager() {
        notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification=new NotificationCompat.Builder(AllVideos.this,CHANNEL_ID);
        notification.setOngoing(true);
        notification.setContentTitle(title);
        notification.setContentText("Download in progress");
        notification.setPriority(Notification.PRIORITY_HIGH);
        notification.setSmallIcon(R.drawable.ic_cloud_download_white_24dp);
    }

    private void progressDialog() {
        progressDialog2 = new ProgressDialog(AllVideos.this);
        progressDialog2.setMax(100);
        progressDialog2.setMessage("Please wait...");
        progressDialog2.setTitle("Download");
        progressDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog2.show();
        progressDialog2.setCancelable(false);
    }

    private void folderCheckPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(AllVideos.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(AllVideos.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(AllVideos.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            Log.e("Else","Else");
            video();
        }
    }

    private void video() {
        File directory=new File(path);
        if (!directory.exists()){
            directory.mkdirs();
        }
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
                        Toast.makeText(AllVideos.this,
                                "The app was not allowed to read or write to your storage. " +
                                        "Hence, it cannot function properly. Please consider granting it this permission",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        }

    }
}
