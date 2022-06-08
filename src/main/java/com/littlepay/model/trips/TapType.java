package com.littlepay.model.trips;

import com.google.common.base.Enums;

public enum TapType {
    ON,
    OFF;

    public static TapType getIfPresent(String tapType) {
        return Enums.getIfPresent(TapType.class, tapType).orNull();
    }
}
