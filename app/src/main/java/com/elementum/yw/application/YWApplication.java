package com.elementum.yw.application;

import android.app.Application;

import com.elementum.yw.R;
import com.elementum.yw.services.network.RestService;
import com.elementum.yw.services.network.YelpRestService;
import com.elementum.yw.services.network.signpost.retrofit.RetrofitHttpOAuthConsumer;
import com.elementum.yw.services.network.signpost.retrofit.SigningOkClient;
import com.squareup.picasso.Picasso;

import retrofit.RestAdapter;

public class YWApplication extends Application {
    private YelpRestService restService;
    private static YWApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Picasso.with(this).setIndicatorsEnabled(true);
        restService = new YelpRestService(this, createRestService());
    }

    public static YWApplication getInstance(){
        return instance;
    }

    public YelpRestService getRestService(){
        return restService;
    }

    private RestService createRestService(){
        RetrofitHttpOAuthConsumer oAuthConsumer =
                new RetrofitHttpOAuthConsumer(getString(R.string.yelp_consumer_key), getString(R.string.yelp_consumer_secret));

        oAuthConsumer.setTokenWithSecret(getString(R.string.yelp_token), getString(R.string.yelp_token_secret));

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.yelp_server_host))
                .setClient(new SigningOkClient(oAuthConsumer)).build();
        return restAdapter.create(RestService.class);
    }
}
