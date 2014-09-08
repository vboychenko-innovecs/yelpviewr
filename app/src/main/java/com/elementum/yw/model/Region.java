package com.elementum.yw.model;

import com.google.gson.annotations.SerializedName;

public class Region {
    Center center;
    Span span;

    public static class Center{
        double latitude;
        double longitude;
    }

    public static class Span{
        @SerializedName("latitude_delta")
        double latitudeDelta;

        @SerializedName("longitude_delta")
        double longitudeDelta;
    }
}
