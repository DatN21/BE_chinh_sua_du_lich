package com.dulich.toudulich.enums;

public enum TourStatus {
    ACTIVE,
    INACTIVE;
    public static TourStatus fromString(String status) {
        for (TourStatus tourStatus : TourStatus.values()) {
            if (tourStatus.name().equalsIgnoreCase(status)) {
                return tourStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
