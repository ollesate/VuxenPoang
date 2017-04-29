package com.sjoholm.olof.vuxenpoang.ui.expense;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

class EditExpenseDialog implements ExpenseDialog.ExpenseDialogListener {
    private final ExpenseDialog expenseDialog;
    private final DialogListener listener;

    interface DialogListener {

        void onExpenseEdited(@NonNull String name, int cost);
    }

    EditExpenseDialog(@NonNull Expense expense, @NonNull DialogListener listener) {
        this.listener = listener;
        expenseDialog = ExpenseDialog.newInstance(R.string.expense_dialog_edit, expense);
        expenseDialog.setOnAcceptListener(this);
    }

    void show(@NonNull FragmentManager fragmentManager) {
        expenseDialog.show(fragmentManager, null);
    }

    @Override
    public void onAccept(@NonNull Expense item) {
        listener.onExpenseEdited(item.name, item.cost);
    }
}
