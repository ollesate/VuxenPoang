package com.sjoholm.olof.vuxenpoang.ui.sale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Sale;

import java.util.List;

public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.ItemViewHolder>
        implements View.OnLongClickListener{
    private final ClickListener clickListener;
    private final List<Sale> sales;

    public interface ClickListener {

        void onItemLongClick(Sale sale);
    }

    public SaleItemAdapter(@NonNull ClickListener clickListener, @NonNull List<Sale> sales) {
        this.clickListener = clickListener;
        this.sales = sales;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.expense_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Sale expense = sales.get(position);
        holder.textViewName.setText(expense.name);
        holder.textViewCost.setText(String.valueOf(expense.cost));
        holder.itemView.setTag(expense);
        holder.itemView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        Sale expense = (Sale) view.getTag();
        clickListener.onItemLongClick(expense);
        return true;
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewName;
        final TextView textViewCost;

        ItemViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.tvName);
            textViewCost = (TextView) view.findViewById(R.id.tvCost);
        }
    }
}
