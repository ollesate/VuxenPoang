package com.sjoholm.olof.vuxenpoang.model;

import android.support.annotation.NonNull;

public class Sale extends Expense {
    public final long id;

    public Sale(@NonNull String name, int cost, long id) {
        super(name, cost);
        this.id = id;
    }
}
