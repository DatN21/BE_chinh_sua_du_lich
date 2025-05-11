package com.dulich.toudulich.enums;

public enum TourScheduleStatus {
    ACTIVE,
    COMPLETED;
    public static TourScheduleStatus fromString(String status) {
        for (TourScheduleStatus tourScheduleStatus : TourScheduleStatus.values()) {
            if (tourScheduleStatus.name().equalsIgnoreCase(status)) {
                return tourScheduleStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
