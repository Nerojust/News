package com.nerojust.news.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nerojust.news.R;
import com.nerojust.news.model.NewsResponse;
import com.nerojust.news.view.DetailNewsActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private NewsResponse newsResponseList;
    private Date date;

    public NewsAdapter(Context context, NewsResponse newsResponseList) {
        this.context = context;
        this.newsResponseList = newsResponseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_row_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra("title", newsResponseList.getArticles().get(position).getTitle());
                intent.putExtra("published", newsResponseList.getArticles().get(position).getPublishedAt());
                intent.putExtra("author", newsResponseList.getArticles().get(position).getAuthor());
                intent.putExtra("description", newsResponseList.getArticles().get(position).getDescription());
                intent.putExtra("image", newsResponseList.getArticles().get(position).getUrlToImage());
                intent.putExtra("details", newsResponseList.getArticles().get(position).getUrl());
                context.startActivity(intent);
            }
        });

        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));


        holder.newsTitle.setText(newsResponseList.getArticles().get(position).getTitle());
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = formatter.parse(newsResponseList.getArticles().get(position).getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat showformatter = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        String finalDtTm = showformatter.format(date);


        holder.newsPublish.setText("Time : " + finalDtTm);
        if (newsResponseList.getArticles().get(position).getAuthor() == null ||
                newsResponseList.getArticles().get(position).getAuthor() == "") {
            holder.newsAuthor.setText("Author: Unknown");
        } else {
            holder.newsAuthor.setText("Author: " + newsResponseList.getArticles().get(position).getAuthor());
        }


        holder.newsDescription.setText("Story: " + newsResponseList.getArticles().get(position).getDescription());
        Glide.with(context)
                .load(newsResponseList.getArticles().get(position).getUrlToImage())
                .placeholder(R.drawable.load)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return newsResponseList.getArticles().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout container;
        ImageView imageView;
        TextView newsTitle;
        TextView newsAuthor;
        TextView newsPublish;
        TextView newsDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.newsIcon);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsPublish = itemView.findViewById(R.id.newsYear);
            newsAuthor = itemView.findViewById(R.id.newsAuthor);
            newsDescription = itemView.findViewById(R.id.newsDescription);

        }
    }
}
