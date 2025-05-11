package com.dulich.toudulich.enums;

public enum Gender {
    NAM,
    NU;

    public static Gender fromString(String status) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(status)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}


