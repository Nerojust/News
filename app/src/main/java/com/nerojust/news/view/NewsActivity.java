package com.nerojust.news.view;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nerojust.news.R;
import com.nerojust.news.Utils;
import com.nerojust.news.adapter.NewsAdapter;
import com.nerojust.news.contract.MainContract;
import com.nerojust.news.model.NewsResponse;
import com.nerojust.news.presenter.NewsPresenter;

import java.util.HashMap;

public class NewsActivity extends AppCompatActivity implements MainContract.NewsViewInterface {
    private static final String LATEST_APP_VERSION_KEY = "latest_app_version";
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initAds();
        initCheckConfigVersionSettings();

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

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_want_to_exit))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, id) -> {
                    finishAffinity();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * This checks if the there is a current version available on play store and show a dialog
     */
    private void initCheckConfigVersionSettings() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        HashMap<String, Object> firebaseDefaults = new HashMap<>();
        firebaseDefaults.put(LATEST_APP_VERSION_KEY, getCurrentVersionCode());

        mFirebaseRemoteConfig.setDefaults(firebaseDefaults);
        mFirebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                // .setMinimumFetchIntervalInSeconds(3600)
                .build());

        mFirebaseRemoteConfig.fetch().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mFirebaseRemoteConfig.activateFetched();
                checkForVersionUpdate();
            } else {
                Toast.makeText(this, "Error getting update", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void checkForVersionUpdate() {
        int retrievedVersionCodeFromFirebase = (int) mFirebaseRemoteConfig.getDouble((LATEST_APP_VERSION_KEY));
        if (getCurrentVersionCode() < retrievedVersionCodeFromFirebase) {
            new AlertDialog.Builder(this).setPositiveButton("UPDATE", (dialog, which) -> {

                final String appPackageName = getPackageName(); // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.playstore_link_one) + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.playstore_link_two) + appPackageName)));
                }
            }).setTitle("Update Application").setNegativeButton("NO, THANKS", (dialog, which) -> dialog.dismiss())
                    .setMessage(getResources().getString(R.string.update_message))
                    .setCancelable(false).show();
        } else {
            Toast.makeText(this, "App is up to date", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCurrentVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}