package com.dulich.toudulich.enums;

public enum RoleType {
    USER(1),
    ADMIN(2);

    private final int id;

    RoleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RoleType fromId(int id) {
        for (RoleType role : RoleType.values()) {
            if (role.getId() == id) return role;
        }
        throw new IllegalArgumentException("Invalid Role ID: " + id);
    }
}

