package com.sjoholm.olof.vuxenpoang.ui.purchase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sjoholm.olof.vuxenpoang.Injection;
import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;
import com.sjoholm.olof.vuxenpoang.model.Purchase;
import com.sjoholm.olof.vuxenpoang.ui.expense.CreateExpenseDialog;
import com.sjoholm.olof.vuxenpoang.ui.expense.EditExpenseDialog;

import java.util.List;

public class PurchaseItemsActivity extends AppCompatActivity implements PurchaseItemAdapter.ClickListener {
    private List<Purchase> purchases;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_main);

        findViewById(R.id.btn_create).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                showCreateDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        purchases = Injection.getRepository(this).fetchPurchases();
        PurchaseItemAdapter purchaseItemAdapter = new PurchaseItemAdapter(this, purchases);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(purchaseItemAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Injection.getRepository(this).syncPurchases(purchases);
    }

    private void showCreateDialog() {
        CreatePurchaseDialog createExpenseDialog = new CreatePurchaseDialog(this,
                new CreatePurchaseDialog.DialogListener() {
                    @Override
                    public void onSaleCreated(@NonNull Purchase purchase) {
                        purchases.add(purchase);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
        createExpenseDialog.show(getSupportFragmentManager());
    }

    private void showEditDialog(final Purchase edit) {
        String name = edit.name;
        int cost = edit.cost;
        EditExpenseDialog editDialog = new EditExpenseDialog(name, cost,
                new EditExpenseDialog.DialogListener() {
            @Override
            public void onExpenseEdited(@NonNull String name, int cost) {
                edit.name = name;
                edit.cost = cost;
                recyclerView.getAdapter().notifyItemChanged(purchases.indexOf(edit));
            }
        });
        editDialog.show(getSupportFragmentManager());
    }

    @Override
    public void onItemLongClick(final Purchase expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(expense.name);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        purchases.remove(expense);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        showEditDialog(expense);
                        break;
                }
                dialogInterface.dismiss();
            }
        };
        builder.setNegativeButton(R.string.dialog_delete, onClickListener);
        builder.setPositiveButton(R.string.dialog_edit, onClickListener);
        builder.show();
    }
}
