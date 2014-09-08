package com.elementum.yw.services.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.elementum.yw.model.SearchResult;
import com.google.gson.Gson;

import retrofit.Callback;

public class YelpRestService {
    public static final String PREFERENCES = "user_preferences";
    public static final String TERM_CACHED = "cached_term";
    public static final String LOCATION_CACHED = "cached_location";
    public static final String JSON_CACHED = "cached_json";

    private RestService mRestService;
    private SharedPreferences prefs;

    public YelpRestService(Context context, RestService restService) {
        mRestService = restService;
        prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void search(String term, String location, Callback<SearchResult> callback) {
        String encodedTerm = term.trim().replace(" ", "+"); //Replacing whitespace inside term, i.e. Driving school => Driving+school

        String encodedLocation = location.trim().replaceAll("[,]{1}\\s*", ","); //Remove all whitespaces between city and state
        encodedLocation = encodedLocation.replace(" ", "+"); //Replacing whitespace inside city name, i.e. New York => New+York

        mRestService.search(encodedTerm, encodedLocation, new SearchCallback(prefs, term, location, callback));
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();
    }

    public boolean hasOfflineData() {
        return prefs.contains(TERM_CACHED) && prefs.contains(LOCATION_CACHED) && prefs.contains(JSON_CACHED);
    }

    public Cache getOfflineData() {
        if (!hasOfflineData())
            return null;
        String term = prefs.getString(TERM_CACHED, "");
        String location = prefs.getString(LOCATION_CACHED, "");
        String cachedJson = prefs.getString(JSON_CACHED, "");

        SearchResult cachedResult = null;
        try {
            cachedResult = new Gson().fromJson(cachedJson, SearchResult.class);
        } catch (Exception e) {
            return null;
        }

        return new Cache(term, location, cachedResult);
    }


    private static class SearchCallback extends CachingCallback<SearchResult> {
        SharedPreferences mPrefs;
        Gson gson = new Gson();

        public SearchCallback(SharedPreferences prefs, String term, String location, Callback<SearchResult> callback) {
            super(callback, term, location);
            mPrefs = prefs;
        }

        @Override
        public void save(SearchResult result, String term, String location) {
            mPrefs.edit().putString(TERM_CACHED, term).
                    putString(LOCATION_CACHED, location).
                    putString(JSON_CACHED, gson.toJson(result)).
                    commit();
        }
    }


}
