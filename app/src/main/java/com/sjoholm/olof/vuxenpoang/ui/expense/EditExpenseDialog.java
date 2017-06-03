package com.sjoholm.olof.vuxenpoang.ui.expense;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

public class EditExpenseDialog implements ExpenseDialog.ExpenseDialogListener {
    private final ExpenseDialog expenseDialog;
    private final DialogListener listener;

    public interface DialogListener {

        void onExpenseEdited(@NonNull String name, int cost);
    }

    public EditExpenseDialog(@NonNull String name, int cost, @NonNull DialogListener listener) {
        this.listener = listener;
        expenseDialog = ExpenseDialog.newInstance(R.string.expense_dialog_edit, name, cost);
        expenseDialog.setOnAcceptListener(this);
    }

    public void show(@NonNull FragmentManager fragmentManager) {
        expenseDialog.show(fragmentManager, null);
    }

    @Override
    public void onAccept(@NonNull Expense item) {
        listener.onExpenseEdited(item.name, item.cost);
    }
}
