package com.sjoholm.olof.vuxenpoang.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.List;

class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ItemViewHolder>
        implements View.OnLongClickListener{
    private ClickListener clickListener;
    private final List<Expense> expenses;

    interface ClickListener {

        void onItemLongClick(Expense expense);
    }

    ExpenseAdapter(@NonNull ClickListener clickListener, @NonNull List<Expense> expenses) {
        this.clickListener = clickListener;
        this.expenses = expenses;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.expense, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.textViewName.setText(expense.name);
        holder.textViewCost.setText(String.valueOf(expense.cost));
        holder.itemView.setTag(expense);
        holder.itemView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        Expense expense = (Expense) view.getTag();
        clickListener.onItemLongClick(expense);
        return true;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewCost;

        ItemViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.tvName);
            textViewCost = (TextView) view.findViewById(R.id.tvCost);
        }
    }
}
