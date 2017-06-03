package com.sjoholm.olof.vuxenpoang.model;

import android.support.annotation.NonNull;

public class Purchase extends Expense {

    public Purchase(@NonNull String name, int cost) {
        super(name, cost);
    }
}
