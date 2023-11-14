package com.example.music_app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class News extends Fragment {
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    ArrayList<Data> arrayList=new ArrayList<Data>();
    RequestQueue requestQueue;
    String url="https://api.dailymotion.com/channel/news/videos?fields=id,title,channel,owner.screenname,thumbnail_url&limit=20";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news, container, false);



        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestQueue= Volley.newRequestQueue(getActivity());

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject list=jsonArray.getJSONObject(i);
                                String id =list.getString("id");
                                String thumbnail_url =list.getString("thumbnail_url");
                                String title = list.getString("title");
                                String channel = list.getString("channel");
                                String owner = list.getString("owner.screenname");
                                arrayList.add(new Data(thumbnail_url, title, channel, owner,id));
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                        dataAdapter=new DataAdapter(getActivity(),arrayList);
                        recyclerView.setAdapter(dataAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
        return view;
    }
}
