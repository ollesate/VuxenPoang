package com.sjoholm.olof.vuxenpoang.ui.shared;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

public class ReceiptView implements CustomListView.ItemPopulator<Expense>,
        CustomListView.OnItemClicked<Expense> {
    private final TextView textViewBalance;
    private final TextView textViewTotal;
    private final CustomListView<Expense> receiptItems;
    private int total;

    public ReceiptView(ViewGroup receiptView) {
        ViewGroup receiptItemsView = (ViewGroup) receiptView.findViewById(R.id.receipt_items);
        receiptItems = new CustomListView<>(receiptItemsView, R.layout.receipt_item, this);
        receiptItems.setClickListener(this);
        textViewBalance = (TextView) receiptView.findViewById(R.id.receipt_balance);
        textViewTotal = (TextView) receiptView.findViewById(R.id.receipt_total);
    }

    public void setBalance(int balance) {
        textViewBalance.setText(String.valueOf(balance));
    }

    public void clear() {
        receiptItems.clear();
        total = 0;
        textViewTotal.setText(String.valueOf(total));
    }

    public void add(Expense expense) {
        receiptItems.add(expense);
        total += expense.cost;
        textViewTotal.setText(String.valueOf(total));
    }

    public void remove(Expense expense) {
        receiptItems.remove(expense);
        total -= expense.cost;
        textViewTotal.setText(String.valueOf(total));
    }

    public int getTotal() {
        return total;
    }

    @Override
    public void populate(View view, Expense item) {
        ((TextView) view.findViewById(R.id.item_name)).setText(item.name);
        ((TextView) view.findViewById(R.id.item_cost)).setText(String.valueOf(item.cost));
    }

    @Override
    public void onClick(Expense expense) {
        remove(expense);
    }
}
