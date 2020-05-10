package com.road.road1971user.model;

import java.util.ArrayList;

public class FixTripDetailsModel {
    private double sourceLat,sourceLng,desLat,desLng,distance;
    private int fare;
    private String requestorUid,driverId,type;
    private boolean accept = false;
    private ArrayList<String> driversFound;
    public FixTripDetailsModel() {

    }

    public FixTripDetailsModel(double sourceLat, double sourceLng, double desLat, double desLng, double distance, int fare, String requestorUid, String type) {
        this.sourceLat = sourceLat;
        this.sourceLng = sourceLng;
        this.desLat = desLat;
        this.desLng = desLng;
        this.distance = distance;
        this.fare = fare;
        this.requestorUid = requestorUid;
        this.type = type;
        this.accept = false;
    }

    public ArrayList<String> getDriversFound() {
        return driversFound;
    }

    public void setDriversFound(ArrayList<String> driversFound) {
        this.driversFound = driversFound;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getRequestorUid() {
        return requestorUid;
    }

    public void setRequestorUid(String requestorUid) {
        this.requestorUid = requestorUid;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSourceLng() {
        return sourceLng;
    }

    public void setSourceLng(double sourceLng) {
        this.sourceLng = sourceLng;
    }

    public double getDesLat() {
        return desLat;
    }

    public void setDesLat(double desLat) {
        this.desLat = desLat;
    }

    public double getDesLng() {
        return desLng;
    }

    public void setDesLng(double desLng) {
        this.desLng = desLng;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }
}
