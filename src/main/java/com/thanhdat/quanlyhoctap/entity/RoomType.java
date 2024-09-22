package com.thanhdat.quanlyhoctap.entity;

public enum RoomType {
    LAB_ROOM("Lý thuyết"),
    CLASS_ROOM("Thực hành")
    ;
    private final String description;

    RoomType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
