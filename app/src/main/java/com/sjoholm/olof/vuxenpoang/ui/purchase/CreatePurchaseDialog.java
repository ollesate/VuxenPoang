package com.sjoholm.olof.vuxenpoang.ui.purchase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.sjoholm.olof.vuxenpoang.Injection;
import com.sjoholm.olof.vuxenpoang.model.Expense;
import com.sjoholm.olof.vuxenpoang.model.Purchase;
import com.sjoholm.olof.vuxenpoang.model.Sale;
import com.sjoholm.olof.vuxenpoang.ui.expense.CreateExpenseDialog;

public class CreatePurchaseDialog implements CreateExpenseDialog.DialogListener {
    private Context context;
    private DialogListener listener;

    public interface DialogListener {

        void onSaleCreated(@NonNull Purchase purchase);

    }
    public CreatePurchaseDialog(@NonNull Context context, @NonNull DialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void show(FragmentManager fragmentManager) {
        new CreateExpenseDialog(this).show(fragmentManager);
    }

    @Override
    public void onExpenseCreated(@NonNull Expense expense) {
        Purchase purchase =
                Injection.getRepository(context).createPurchase(expense.name, expense.cost);
        listener.onSaleCreated(purchase);
    }
}
