package com.dulich.toudulich.enums;

public enum TourDiscountStatus {
    ACTIVE,
    INACTIVE;
    public static TourDiscountStatus fromString(String status) {
        for (TourDiscountStatus tourDiscountStatus : TourDiscountStatus.values()) {
            if (tourDiscountStatus.name().equalsIgnoreCase(status)) {
                return tourDiscountStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
