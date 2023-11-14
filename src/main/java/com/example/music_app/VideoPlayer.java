package com.example.music_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import java.io.File;


public class VideoPlayer extends AppCompatActivity {
    FloatingActionMenu fb;
    FloatingActionButton fab1,fab2;
    WebView webView;
    String id;
    TextView dialog_title;
    ImageView imageView;
    ProgressDialog progressDialog;
    String title;
    String image;
    String webViewUrl;
    String VideoUrl=null;
    String type="ts";
    WebChromeClient webChromeClient;
    private static final int REQUEST_PERMISSIONS = 100;
    String path= Environment.getExternalStorageDirectory()+"/DailymotionApp/";
    String userAgent="Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";



    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();

    }
    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        webView= findViewById(R.id.webView);
        fb=   findViewById(R.id.floatingActionButton1);
        fab1= findViewById(R.id.floatingActionButton2);
        fab2= findViewById(R.id.floatingActionButton3);

        id=getIntent().getExtras().getString("id");
        title=getIntent().getExtras().getString("Title");
        image=getIntent().getExtras().getString("Image");
        webViewUrl= "https://www.dailymotion.com/video/"+id;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url.startsWith("https://")&&url.endsWith(".ts")){
                    VideoUrl=url;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webChromeClient=new WebChromeClient() {
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
                }
        };
        webView.setWebChromeClient(webChromeClient);
        webView.getSettings().setUserAgentString(userAgent);
        webView.loadUrl(webViewUrl);



        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                intent.putExtra(Intent.EXTRA_TEXT, "This is my text");
                startActivity(Intent.createChooser(intent, "choose one"));
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoUrl == null) {
                    Toast.makeText(VideoPlayer.this, "Please wait", Toast.LENGTH_SHORT).show();
                } else {
                    progress_dialog();
                    downloadDialog();

                }
            }
        });
    }

    private void progress_dialog() {
        progressDialog = new ProgressDialog(VideoPlayer.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void downloadDialog() {
        final Dialog dialog=new Dialog(VideoPlayer.this);
        dialog.setContentView(R.layout.download_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageView=(ImageView)dialog.findViewById(R.id.imageView3);
        dialog_title=(TextView) dialog.findViewById(R.id.title_text_view);
        TextView download=dialog.findViewById(R.id.download_dialog);
        TextView cancel= dialog.findViewById(R.id.cancel_download_dialog);
        Glide.with(VideoPlayer.this)
                .load(image)
                .into(imageView);

        dialog_title.setText(title);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderCheckPermission();
                if (VideoUrl!=null) {
                    VideoUrl=VideoUrl.replaceAll("(frag\\()+(\\d+)+(\\))", "FRAGMENT");
                    Intent intent = new Intent(VideoPlayer.this, DownloadManager.class);
                    intent.putExtra("name", title);
                    intent.putExtra("link", VideoUrl);
                    intent.putExtra("type", type);
                    intent.putExtra("website", "dailymotion.com");
                    intent.putExtra("chunked", true);
                    startService(intent);
                    notification_dialog();
                }else{
                    Toast.makeText(VideoPlayer.this, "Video link is not found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        progressDialog.dismiss();

    }
    TextView notificationText;
    private void notification_dialog() {
        final Dialog dialog=new Dialog(VideoPlayer.this);
        dialog.setContentView(R.layout.notification_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       notificationText=(TextView) dialog.findViewById(R.id.notification_text);
        TextView okButton=dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void folderCheckPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(VideoPlayer.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(VideoPlayer.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(VideoPlayer.this,
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
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        video();
                    } else {
                        Toast.makeText(VideoPlayer.this,
                                "The app was not allowed to read or write to your storage. " +
                                        "Hence, it cannot function properly. Please consider granting it this permission",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }


}
