package com.nerojust.news.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nerojust.news.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailNewsActivity extends AppCompatActivity {
    ImageView imageView;
    TextView titleTextView, authorTextView, timeTextView, descriptionTextView;
    private String title;
    private String published;
    private String author;
    private String description;
    private String imageUrl;
    private ImageView share;
    private Date date;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //finish();
        }
        initViews();

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        published = intent.getStringExtra("published");
        author = intent.getStringExtra("author");
        description = intent.getStringExtra("description");
        imageUrl = intent.getStringExtra("image");
        details = intent.getStringExtra("details");

        bindViews();

        // Toast.makeText(this, title + "\n" + published + "\n" + author + "\n" + description + "\n", Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.load)
                .into(imageView);

    }

    private void bindViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);

        descriptionTextView.setText(description);

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = formatter.parse(published);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat showformatter = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        String finalDtTm = showformatter.format(date);
        timeTextView.setText(finalDtTm);
    }

    private void initViews() {
        imageView = findViewById(R.id.image);
        titleTextView = findViewById(R.id.newsTitle);
        authorTextView = findViewById(R.id.newsAuthor);
        timeTextView = findViewById(R.id.newsPublished);
        descriptionTextView = findViewById(R.id.newsDescription);
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, details);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
