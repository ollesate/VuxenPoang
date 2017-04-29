package com.sjoholm.olof.vuxenpoang.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.database.Database;
import com.sjoholm.olof.vuxenpoang.database.ItemTable;
import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.ClickListener {
    private List<Expense> expenses;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.btn_create).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                showCreateDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Database database = new Database(this);
        ItemTable itemTable = database.getItemTable();
        expenses = itemTable.getItems();
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(this, expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Database database = new Database(this);
        ItemTable itemTable = database.getItemTable();
        itemTable.update(expenses);
    }

    private void showCreateDialog() {
        CreateDialog dialogFragment = new CreateDialog();
        dialogFragment.setOnAcceptListener(new CreateDialog.Listener() {
            @Override
            public void onAccept(Expense item) {
                expenses.add(item);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void showEditDialog(final Expense edit) {
        EditDialog editDialog = EditDialog.newInstance(edit);
        editDialog.setOnAcceptListener(new CreateDialog.Listener() {
            @Override
            public void onAccept(Expense result) {
                edit.name = result.name;
                edit.cost = result.cost;
                recyclerView.getAdapter().notifyItemChanged(expenses.indexOf(edit));
            }
        });
        editDialog.show(getSupportFragmentManager(), null);
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
        builder.setNegativeButton(R.string.delete, onClickListener);
        builder.setPositiveButton(R.string.edit, onClickListener);
        builder.show();
    }
}
