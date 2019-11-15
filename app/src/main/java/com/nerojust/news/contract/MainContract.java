package com.nerojust.news.contract;

import com.nerojust.news.model.NewsResponse;

import retrofit2.Response;

public interface MainContract {


    interface NewsPresenterInterface {
        void performNewsSearch();
    }

    interface NewsViewInterface {
        void onNewsResponseError(String errorMessage);

        void onNewsResponseSuccess(NewsResponse body);
    }
}
