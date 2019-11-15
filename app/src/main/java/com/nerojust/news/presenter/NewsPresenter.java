package com.nerojust.news.presenter;

import com.nerojust.news.contract.MainContract;
import com.nerojust.news.model.NewsResponse;
import com.nerojust.news.services.NewsResponseInterface;
import com.nerojust.news.services.WebServiceRequestMaker;

public class NewsPresenter implements MainContract.NewsPresenterInterface {

    private MainContract.NewsViewInterface newsViewInterface;

    public NewsPresenter(MainContract.NewsViewInterface newsViewInterface) {
        this.newsViewInterface = newsViewInterface;
    }

    @Override
    public void performNewsSearch() {
        WebServiceRequestMaker webServiceRequestMaker = new WebServiceRequestMaker();
        webServiceRequestMaker.getNewsFeed(new NewsResponseInterface() {
            @Override
            public void onSuccess(NewsResponse newsResponse) {
                newsViewInterface.onNewsResponseSuccess(newsResponse);
            }

            @Override
            public void onError(String error) {
                newsViewInterface.onNewsResponseError(error);
            }
        });
    }
}
