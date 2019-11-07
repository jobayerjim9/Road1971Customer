package com.road.road1971user.model;

import com.google.android.gms.maps.model.LatLng;

public class CarRentData {
    private LatLng source,destination;
    private String sourceName,destinationName,hoursRequired,carType,details;
    private TimeStamp timeStamp;
    private int carRequired;

    public CarRentData() {
    }

    public CarRentData(LatLng source, LatLng destination, String sourceName, String destinationName, String hoursRequired, String carType, String details, TimeStamp timeStamp, int carRequired) {
        this.source = source;
        this.destination = destination;
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.hoursRequired = hoursRequired;
        this.carType = carType;
        this.details = details;
        this.timeStamp = timeStamp;
        this.carRequired = carRequired;
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

    public String getHoursRequired() {
        return hoursRequired;
    }

    public void setHoursRequired(String hoursRequired) {
        this.hoursRequired = hoursRequired;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCarRequired() {
        return carRequired;
    }

    public void setCarRequired(int carRequired) {
        this.carRequired = carRequired;
    }
}
