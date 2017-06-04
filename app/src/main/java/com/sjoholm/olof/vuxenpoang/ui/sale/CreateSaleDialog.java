package com.sjoholm.olof.vuxenpoang.ui.sale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.sjoholm.olof.vuxenpoang.Injection;
import com.sjoholm.olof.vuxenpoang.model.Expense;
import com.sjoholm.olof.vuxenpoang.model.Sale;
import com.sjoholm.olof.vuxenpoang.ui.expense.CreateExpenseDialog;

public class CreateSaleDialog implements CreateExpenseDialog.DialogListener {
    private Context context;
    private DialogListener listener;

    public interface DialogListener {

        void onSaleCreated(@NonNull Sale sale);

    }
    public CreateSaleDialog(@NonNull Context context, @NonNull DialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void show(FragmentManager fragmentManager) {
        new CreateExpenseDialog(this).show(fragmentManager);
    }

    @Override
    public void onExpenseCreated(@NonNull Expense expense) {
        Sale sale = Injection.getRepository(context).createSale(expense.name, expense.cost);
        listener.onSaleCreated(sale);
    }
}
