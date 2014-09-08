package com.elementum.yw.services.network;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class CachingCallback<T> implements Callback<T> {
    private Callback<T> wrapped;
    private String mTerm, mLocation;

    public CachingCallback(Callback<T> callback, String term, String location){
        wrapped = callback;
        mTerm = term;
        mLocation = location;
    }

    @Override
    public void success(T t, Response response) {
        save(t, mTerm, mLocation);
        wrapped.success(t, response);
    }

    @Override
    public void failure(RetrofitError error) {
        wrapped.failure(error);
    }

    public abstract void save(T result, String term, String location);
}
