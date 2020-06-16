package com.nerojust.news.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nerojust.news.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailNewsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView, authorTextView, timeTextView, descriptionTextView;
    private String title;
    private String published;
    private String author;
    private String description;
    private String imageUrl;
    private ImageView share;
    private TextView readMore;
    private Date date;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //finish();
        }
        initAds();
        initViews();
        initListeners();

        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            published = intent.getStringExtra("published");
            author = intent.getStringExtra("author");
            description = intent.getStringExtra("description");
            imageUrl = intent.getStringExtra("image");
            details = intent.getStringExtra("details");

            bindViews();
        }
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.load)
                .into(imageView);

    }

    private void initListeners() {
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(details);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
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

    private void initAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void bindViews() {
        titleTextView.setText(title);

        if (author == null || author.equalsIgnoreCase("")) {
            authorTextView.setText("Author: Unknown");
        } else {
            authorTextView.setText("Author: " + author);
        }

        if (description == null || description.equalsIgnoreCase("")) {
            descriptionTextView.setText("Story: No Story yet");
        } else {
            descriptionTextView.setText(description);
        }

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = formatter.parse(published);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat showformatter = new SimpleDateFormat("MMM. dd,yyyy ; hh:mm a");
        String finalDtTm = showformatter.format(date);
        timeTextView.setText("Time: " + finalDtTm);
    }

    private void initViews() {
        imageView = findViewById(R.id.image);
        titleTextView = findViewById(R.id.newsTitle);
        authorTextView = findViewById(R.id.newsAuthor);
        timeTextView = findViewById(R.id.newsPublished);
        readMore = findViewById(R.id.readMoreButton);
        descriptionTextView = findViewById(R.id.newsDescription);
        share = findViewById(R.id.share);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
