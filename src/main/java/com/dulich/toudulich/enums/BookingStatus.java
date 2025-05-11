package com.dulich.toudulich.enums;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    REFUNDED;

    public static BookingStatus fromString(String status) {
        for (BookingStatus bookingStatus : BookingStatus.values()) {
            if (bookingStatus.name().equalsIgnoreCase(status)) {
                return bookingStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
