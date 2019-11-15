package com.nerojust.news.services;

import com.nerojust.news.model.NewsResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetServiceInterface {
    @GET("/v2/top-headlines?country=ng&apiKey=a55a978f2cc34820a4b259853acb7f10")
    Call<NewsResponse> getNewsFeed();

}
