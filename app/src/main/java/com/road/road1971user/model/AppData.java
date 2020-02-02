package com.road.road1971user.model;

public class AppData {
    private static RentTruckData rentTruckData;
    private static RentMicroData rentMicroData;
    private static RentBikeData rentBikeData;
    private static CarRentData carRentData;


    public static RentTruckData getRentTruckData() {
        return rentTruckData;
    }

    public static void setRentTruckData(RentTruckData rentTruckData) {
        AppData.rentTruckData = rentTruckData;
    }

    public static RentMicroData getRentMicroData() {
        return rentMicroData;
    }

    public static void setRentMicroData(RentMicroData rentMicroData) {
        AppData.rentMicroData = rentMicroData;
    }

    public static RentBikeData getRentBikeData() {
        return rentBikeData;
    }

    public static void setRentBikeData(RentBikeData rentBikeData) {
        AppData.rentBikeData = rentBikeData;
    }

    public static CarRentData getCarRentData() {
        return carRentData;
    }

    public static void setCarRentData(CarRentData carRentData) {
        AppData.carRentData = carRentData;
    }
}
