package com.road.road1971user.model;

public class PromoCodeModel {
    private int discountPercent, maxDiscount, usingLimit;
    private String promoCode, promoName;
    private boolean valid;

    public PromoCodeModel() {
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getUsingLimit() {
        return usingLimit;
    }

    public void setUsingLimit(int usingLimit) {
        this.usingLimit = usingLimit;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }
}
