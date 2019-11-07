package com.road.road1971user.model;

import com.google.android.gms.maps.model.LatLng;

public class RentTruckData {
    private int totalTruck,labour;
    private LatLng source,destination;
    private TimeStamp timeStamp;
    private String vehicleType,vehicleSize,itemType,productDescription,loadLocation,unloadLocation;

    public RentTruckData() {
    }

    public RentTruckData(int totalTruck, int labour, LatLng source, LatLng destination, TimeStamp timeStamp, String vehicleType, String vehicleSize, String itemType, String productDescription, String loadLocation,String unloadLocation) {
        this.totalTruck = totalTruck;
        this.labour = labour;
        this.source = source;
        this.destination = destination;
        this.timeStamp = timeStamp;
        this.vehicleType = vehicleType;
        this.vehicleSize = vehicleSize;
        this.itemType = itemType;
        this.productDescription = productDescription;
        this.loadLocation=loadLocation;
        this.unloadLocation=unloadLocation;
    }

    public String getLoadLocation() {
        return loadLocation;
    }

    public void setLoadLocation(String loadLocation) {
        this.loadLocation = loadLocation;
    }

    public String getUnloadLocation() {
        return unloadLocation;
    }

    public void setUnloadLocation(String unloadLocation) {
        this.unloadLocation = unloadLocation;
    }

    public int getLabour() {
        return labour;
    }

    public void setLabour(int labour) {
        this.labour = labour;
    }

    public int getTotalTruck() {
        return totalTruck;
    }

    public void setTotalTruck(int totalTruck) {
        this.totalTruck = totalTruck;
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

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleSize() {
        return vehicleSize;
    }

    public void setVehicleSize(String vehicleSize) {
        this.vehicleSize = vehicleSize;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
