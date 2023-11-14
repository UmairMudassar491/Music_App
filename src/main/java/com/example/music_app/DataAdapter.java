package com.example.music_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {


    Context context;
    ArrayList<Data> arrayList;
    public DataAdapter(@NonNull Context context, ArrayList<Data> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getThumbnail_url())
                .into(holder.imageView);
        holder.title.setText(arrayList.get(position).getTitle());
        holder.channel.setText(arrayList.get(position).getChannel());
        holder.owner.setText(arrayList.get(position).getOwner());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,VideoPlayer.class);
                intent.putExtra("id",arrayList.get(position).getId());
                intent.putExtra("Title",arrayList.get(position).getTitle());
                intent.putExtra("Image",arrayList.get(position).getThumbnail_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView title,channel,owner;
        ImageView imageView;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView2);
            title=itemView.findViewById(R.id.text_view1);
            channel=itemView.findViewById(R.id.text_view3);
            owner=itemView.findViewById(R.id.text_view4);
        }
    }
}
