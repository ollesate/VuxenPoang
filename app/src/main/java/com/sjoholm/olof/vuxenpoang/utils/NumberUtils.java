package com.sjoholm.olof.vuxenpoang.utils;

import android.support.annotation.Nullable;

public class NumberUtils {

    private NumberUtils() {
    }

    public static int valueOfInt(@Nullable String intString, int defValue) {
        int value;
        try {
            value = Integer.valueOf(intString);
        } catch (NumberFormatException ignored) {
            value = defValue;
        }
        return value;
    }
}
