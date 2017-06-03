package com.sjoholm.olof.vuxenpoang.ui.sale;

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
import com.sjoholm.olof.vuxenpoang.model.Sale;
import com.sjoholm.olof.vuxenpoang.ui.expense.CreateExpenseDialog;
import com.sjoholm.olof.vuxenpoang.ui.expense.EditExpenseDialog;

import java.util.List;

public class SalesItemsActivity extends AppCompatActivity implements SaleItemAdapter.ClickListener {
    private List<Sale> sales;
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
        sales = Injection.getRepository(this).fetchSales();
        SaleItemAdapter adapter = new SaleItemAdapter(this, sales);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Injection.getRepository(this).syncSales(sales);
    }

    private void showCreateDialog() {
        CreateExpenseDialog createExpenseDialog = new CreateExpenseDialog(
                new CreateExpenseDialog.DialogListener() {
                    @Override
                    public void onExpenseCreated(@NonNull Expense expense) {
                        sales.add(new Sale(expense.name, expense.cost));
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
        createExpenseDialog.show(getSupportFragmentManager());
    }

    private void showEditDialog(final Sale edit) {
        String name = edit.name;
        int cost = edit.cost;
        EditExpenseDialog editDialog = new EditExpenseDialog(name, cost,
                new EditExpenseDialog.DialogListener() {
            @Override
            public void onExpenseEdited(@NonNull String name, int cost) {
                edit.name = name;
                edit.cost = cost;
                recyclerView.getAdapter().notifyItemChanged(sales.indexOf(edit));
            }
        });
        editDialog.show(getSupportFragmentManager());
    }

    @Override
    public void onItemLongClick(final Sale sale) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(sale.name);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        sales.remove(sale);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        showEditDialog(sale);
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
