package com.thanhdat.quanlyhoctap.entity;

public enum OutlineStatus {
    DOING {
        @Override
        public String toString() {
            return "Đang thực hiện";
        }
    },
    COMPLETED {
        @Override
        public String toString() {
            return "Đã biên soạn xong";
        }
    },
    PUBLISHED {
        @Override
        public String toString() {
            return "Đã công khai";
        }
    };
}
