package com.sjoholm.olof.vuxenpoang.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Expense implements Serializable {
    public String name;
    public int cost;

    public Expense(@NonNull String name, int cost) {
        this.name = name;
        this.cost = cost;
    }
}
