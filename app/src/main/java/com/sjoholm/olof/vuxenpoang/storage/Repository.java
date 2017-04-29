package com.sjoholm.olof.vuxenpoang.storage;

import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.List;

public interface Repository {

    void syncExpenses(@NonNull List<Expense> expenses);

    List<Expense> fetchExpenses();
}
