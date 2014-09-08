package com.elementum.yw.services.network;

import com.elementum.yw.model.SearchResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RestService {
    @GET("/search")
    public void search(@Query("term") String term, @Query("location") String location, Callback<SearchResult> callback);
}
