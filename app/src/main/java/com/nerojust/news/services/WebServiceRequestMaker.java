package com.nerojust.news.services;

import com.nerojust.news.contract.MainContract;
import com.nerojust.news.model.NewsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WebServiceRequestMaker {

    private final Retrofit retrofit = MyApplication.getInstance().getInstanceOfRetrofit();


    public void getNewsFeed(final NewsResponseInterface newsResponseInterface) {
        GetServiceInterface getServiceInterface = retrofit.create(GetServiceInterface.class);
        Call<NewsResponse> call = getServiceInterface.getNewsFeed();
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        newsResponseInterface.onSuccess(response.body());
                    } else {
                        newsResponseInterface.onError(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                try {
                    newsResponseInterface.onError(t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
