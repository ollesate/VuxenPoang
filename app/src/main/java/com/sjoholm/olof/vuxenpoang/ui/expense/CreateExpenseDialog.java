package com.sjoholm.olof.vuxenpoang.ui.expense;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

class CreateExpenseDialog implements ExpenseDialog.ExpenseDialogListener {
    private final ExpenseDialog expenseDialog;
    private final DialogListener listener;

    interface DialogListener {

        void onExpenseCreated(@NonNull Expense expense);
    }

    CreateExpenseDialog(@NonNull DialogListener listener) {
        this.listener = listener;
        expenseDialog = ExpenseDialog.newInstance(R.string.expense_dialog_create, null);
        expenseDialog.setOnAcceptListener(this);
    }

    void show(@NonNull FragmentManager fragmentManager) {
        expenseDialog.show(fragmentManager, null);
    }

    @Override
    public void onAccept(@NonNull Expense item) {
        listener.onExpenseCreated(item);
    }
}
