package com.road.road1971user.model;

public class FareModel {
    private int base, perKm;

    public FareModel() {
    }

    public FareModel(int base, int perKm) {
        this.base = base;
        this.perKm = perKm;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getPerKm() {
        return perKm;
    }

    public void setPerKm(int perKm) {
        this.perKm = perKm;
    }
}
