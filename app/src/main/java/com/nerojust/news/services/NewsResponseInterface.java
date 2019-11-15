package com.nerojust.news.services;

import com.nerojust.news.model.NewsResponse;

public interface NewsResponseInterface {

    void onSuccess(NewsResponse newsResponse);

    void onError (String error);
}
