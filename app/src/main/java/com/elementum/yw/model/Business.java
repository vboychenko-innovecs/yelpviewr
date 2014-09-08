package com.elementum.yw.model;

import android.content.res.Resources;

import com.elementum.yw.R;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Business {

    private String id;
    private String name;

    @SerializedName("display_phone")
    private String phone;

    private double rating;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("mobile_url")
    private String mobileUrl;

    private Location location;

    private String staticMapUrl;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean hasPhone() {
        return phone != null;
    }

    public String getPhone() {
        return phone;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public String getStaticMapUrl(Resources res) {
        if (staticMapUrl == null) {
            String mapUrl = res.getString(R.string.google_static_map_base_url);
            String addr = location.getAddress().size() > 0 ? location.getAddress().get(0) + "," : "";
            String coord = addr + location.getCity() + "," + location.getStateCode();
            String width = "" + res.getDisplayMetrics().widthPixels;
            staticMapUrl = mapUrl.replace("[place]", coord).replace("[width]", width).replace("|", "%7C").replace(" ", "%20");
        }
        return staticMapUrl;
    }

    public Location getLocation() {
        return location;
    }

    public static class Location {
        private List<String> address;

        @SerializedName("display_address")
        private List<String> displayAddress;

        @SerializedName("country_code")
        private String countryCode;

        @SerializedName("state_code")
        private String stateCode;

        private String city;


        public List<String> getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public String getStateCode() {
            return stateCode;
        }

        public boolean hasDisplayAddress() {
            return displayAddress != null && displayAddress.size() > 0;
        }

        public String getDisplayAddress() {
            if (!hasDisplayAddress()) return "";
            StringBuilder builder = new StringBuilder();

            boolean isFirst = true;
            for (String s : displayAddress) {
                if (!isFirst) builder.append("\n");
                else isFirst = false;

                builder.append(s);
            }
            return builder.toString();
        }

    }
}
