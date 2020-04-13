package com.nerojust.news.services;

import com.nerojust.news.BuildConfig;
import com.nerojust.news.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetServiceInterface {
    @GET("/v2/top-headlines?country=ng&apiKey=" + BuildConfig.ApiKey)
    Call<NewsResponse> getNewsFeed();

}
