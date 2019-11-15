package com.nerojust.news.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nerojust.news.R;
import com.nerojust.news.adapter.NewsAdapter;
import com.nerojust.news.contract.MainContract;
import com.nerojust.news.model.NewsResponse;
import com.nerojust.news.presenter.NewsPresenter;

public class NewsActivity extends AppCompatActivity implements MainContract.NewsViewInterface {
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


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

    private void executeOperation() {
        NewsPresenter newsPresenter = new NewsPresenter(this);
        newsPresenter.performNewsSearch();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public void onNewsResponseError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    @Override
    public void onNewsResponseSuccess(NewsResponse body) {
        NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, body);
        recyclerView.setAdapter(newsAdapter);
        progressDialog.dismiss();
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}