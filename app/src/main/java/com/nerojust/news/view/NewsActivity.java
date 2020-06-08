package com.nerojust.news.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nerojust.news.R;
import com.nerojust.news.Utils;
import com.nerojust.news.adapter.NewsAdapter;
import com.nerojust.news.contract.MainContract;
import com.nerojust.news.model.NewsResponse;
import com.nerojust.news.presenter.NewsPresenter;

public class NewsActivity extends AppCompatActivity implements MainContract.NewsViewInterface {
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initAds();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        executeOperation();
        initViews();
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        executeOperation();

                    }
                }
        );
    }

    private void initAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView adView = findViewById(R.id.adView);
        AdView adView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);
    }

    private void executeOperation() {
        NewsPresenter newsPresenter = new NewsPresenter(this);
        newsPresenter.performNewsSearch();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        if (Utils.getRotation(this).equalsIgnoreCase("portrait")) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        } else if (Utils.getRotation(this).equalsIgnoreCase("landscape")) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

    }


    @Override
    public void onNewsResponseError(String errorMessage) {
        //Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        if (errorMessage.contains("failed to"))
            showDialog();
    }

    @Override
    public void onNewsResponseSuccess(NewsResponse body) {
        NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, body);
        recyclerView.setAdapter(newsAdapter);
        progressDialog.dismiss();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = this.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_retry, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(v -> {
            executeOperation();
        });

        btnNo.setOnClickListener(v -> {
            alertDialog.dismiss();
            finishAffinity();
        });

        alertDialog.show();
    }
}