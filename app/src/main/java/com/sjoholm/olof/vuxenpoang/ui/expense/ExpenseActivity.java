package com.sjoholm.olof.vuxenpoang.ui.expense;

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

import java.util.List;

public class ExpenseActivity extends AppCompatActivity implements ExpenseAdapter.ClickListener {
    private List<Expense> expenses;
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
        expenses = Injection.getRepository(this).fetchExpenses();
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(this, expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Injection.getRepository(this).syncExpenses(expenses);
    }

    private void showCreateDialog() {
        CreateExpenseDialog createExpenseDialog = new CreateExpenseDialog(
                new CreateExpenseDialog.DialogListener() {
            @Override
            public void onExpenseCreated(@NonNull Expense expense) {
                expenses.add(expense);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        createExpenseDialog.show(getSupportFragmentManager());
    }

    private void showEditDialog(final Expense edit) {
        EditExpenseDialog editDialog = new EditExpenseDialog(edit,
                new EditExpenseDialog.DialogListener() {
            @Override
            public void onExpenseEdited(@NonNull String name, int cost) {
                edit.name = name;
                edit.cost = cost;
                recyclerView.getAdapter().notifyItemChanged(expenses.indexOf(edit));
            }
        });
        editDialog.show(getSupportFragmentManager());
    }

    @Override
    public void onItemLongClick(final Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(expense.name);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        expenses.remove(expense);
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
