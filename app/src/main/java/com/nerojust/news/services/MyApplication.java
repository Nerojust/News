package com.nerojust.news.services;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static Retrofit retrofit;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //.client(okHttpClientNetwork)
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public Retrofit getInstanceOfRetrofit() {

        return retrofit;
    }
}
