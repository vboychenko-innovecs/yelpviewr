package com.elementum.yw.services.network;

import com.elementum.yw.model.SearchResult;

public class Cache {
    String mTerm;
    String mLocation;
    SearchResult mSearchResult;

    public Cache(String queriedTerm, String queriedLocation, SearchResult searchResult) {
        mTerm = queriedTerm;
        mLocation = queriedLocation;
        mSearchResult = searchResult;
    }

    public String getTerm() {
        return mTerm;
    }

    public String getLocation() {
        return mLocation;
    }

    public SearchResult getSearchResult() {
        return mSearchResult;
    }
}
