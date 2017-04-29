package com.sjoholm.olof.vuxenpoang.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.List;

public class LocalRepository implements Repository {
    private final ExpensesTable expensesTable;

    public LocalRepository(@NonNull Context context) {
        Database database = new Database(context);
        expensesTable = database.getItemTable();
    }

    @Override
    public void syncExpenses(@NonNull List<Expense> expenses) {
        expensesTable.update(expenses);
    }

    @Override
    public List<Expense> fetchExpenses() {
        return expensesTable.getItems();
    }
}
