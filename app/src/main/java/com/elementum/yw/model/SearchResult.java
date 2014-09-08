package com.elementum.yw.model;

import java.util.List;

public class SearchResult {
    List<Business> businesses;
    Region region;
    int total;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public Region getRegion() {
        return region;
    }

    public int getTotal() {
        return total;
    }
}
