package com.http200ok.finbuddy.autotransfer.domain;

public enum AutoTransferStatus {
    ACTIVE, // 자동이체 진행중
    INACTIVE, // 비활성화됨 (일시정지)
    CANCELLED // 취소됨
}
