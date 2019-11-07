package com.road.road1971user.model;

import com.google.android.gms.maps.model.LatLng;

public class RentBikeData {
    private LatLng source,destination;
    private String sourceName,destinationName,hoursType,additional;
    private TimeStamp timeStamp;
    private int bikeRequired;

    public RentBikeData() {
    }

    public RentBikeData(LatLng source, LatLng destination, String sourceName, String destinationName, String hoursType, String additional, TimeStamp timeStamp, int bikeRequired) {
        this.source = source;
        this.destination = destination;
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.hoursType = hoursType;
        this.additional = additional;
        this.timeStamp = timeStamp;
        this.bikeRequired = bikeRequired;
    }

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getBikeRequired() {
        return bikeRequired;
    }

    public void setBikeRequired(int bikeRequired) {
        this.bikeRequired = bikeRequired;
    }
}
