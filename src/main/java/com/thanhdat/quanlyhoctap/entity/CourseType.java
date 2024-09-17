package com.thanhdat.quanlyhoctap.entity;

public enum CourseType {
    SPECIALIZE {
        @Override
        public String toString() {
            return "Chuyên ngành";
        }
    }, GENERAL {
        @Override
        public String toString() {
            return "Đại cương";
        }
    }
}
