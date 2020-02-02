package com.road.road1971user.model;

import com.google.gson.annotations.SerializedName;

public class LiveLocationModel {
    @SerializedName("0")
    private double lat;
    @SerializedName("1")
    private double lng;

    public LiveLocationModel() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
