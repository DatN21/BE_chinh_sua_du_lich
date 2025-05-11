package com.dulich.toudulich.enums;

public enum UserStatus {
    ACTIVE,
    INACTIVE;
    public static UserStatus fromString(String status) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.name().equalsIgnoreCase(status)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
