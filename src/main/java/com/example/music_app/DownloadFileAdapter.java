package com.example.music_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class DownloadFileAdapter extends RecyclerView.Adapter<DownloadFileAdapter.DownloadFileViewHolder> {
    Context context;
    ArrayList<DownloadData> arrayList;
    public DownloadFileAdapter(@NonNull Context context, ArrayList<DownloadData> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public DownloadFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item,parent,false);
        return new DownloadFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DownloadFileViewHolder holder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getThumbnail_url())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView);
        holder.title.setText(arrayList.get(position).getDownloadTitle());
        holder.size.setText(arrayList.get(position).getDownloadSize());
        holder.datetime.setText(arrayList.get(position).getDownloadDateTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("What you want to do with video?");
                builder1.setMessage("Play or delete or share with friends");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Play",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String videoPath=arrayList.get(position).getVideoPath();
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
                                intent.setDataAndType(Uri.parse(videoPath),"video/mp4");
                                context.startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String file_path=arrayList.get(position).getVideoPath();
                                File file=new File(file_path);
                                if (file.exists()){
                                    file.delete();
                                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                                }
                                arrayList.remove(position);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                builder1.setNeutralButton(
                        "Share",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "DailyMotion_App");
                                intent.putExtra(Intent.EXTRA_TEXT, "Share with Friends");
                                context.startActivity(Intent.createChooser(intent, "choose one"));
                                dialog.cancel();

                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DownloadFileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,size,datetime;
        public DownloadFileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.downloadImageView);
            title=itemView.findViewById(R.id.title_text1);
            size=itemView.findViewById(R.id.size_text2);
            datetime=itemView.findViewById(R.id.datetime_text_view);

        }
    }
}
