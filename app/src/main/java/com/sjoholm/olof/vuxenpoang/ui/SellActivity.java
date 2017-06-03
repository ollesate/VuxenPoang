package com.sjoholm.olof.vuxenpoang.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjoholm.olof.vuxenpoang.Injection;
import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SellActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        ExpenseGridPagerAdapter.OnClickListener {

    private TextView currentPageText;
    private ExpenseGridPagerAdapter gridAdapter;
    private ReceiptView receiptView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Expense> expenses = Injection.getRepository(this).fetchExpenses();
        expenses.add(new Expense("Olof1", 25));
        expenses.add(new Expense("Olof2", 25));
        expenses.add(new Expense("Olof3", 25));
        expenses.add(new Expense("Olof4", 25));
        expenses.add(new Expense("Olof5", 25));
        expenses.add(new Expense("Olof6", 25));

        gridAdapter = new ExpenseGridPagerAdapter(expenses, this);
        viewPager.setAdapter(gridAdapter);
        viewPager.addOnPageChangeListener(this);

        currentPageText = (TextView) findViewById(R.id.pager_progress);
        currentPageText.setText(pagerProgressText(1, gridAdapter.getCount()));

        receiptView = new ReceiptView((ViewGroup) findViewById(R.id.receipt));
    }

    public static class ReceiptView implements MyCustomListView.ItemPopulator<Expense>,
            MyCustomListView.OnItemClicked<Expense> {
        private final TextView textViewBalance;
        private final TextView textViewTotal;
        private final MyCustomListView<Expense> receiptItems;
        private int total;

        public ReceiptView(ViewGroup receiptView) {
            ViewGroup receiptItemsView = (ViewGroup) receiptView.findViewById(R.id.receipt_items);
            receiptItems = new MyCustomListView<>(receiptItemsView, R.layout.receipt_item, this);
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

    public static class MyCustomListView <T> implements View.OnClickListener {
        private List<Pair<T>> items = new ArrayList<>();
        private ViewGroup holder;
        private final int itemLayout;
        private final ItemPopulator<T> populator;
        private OnItemClicked<T> clickListener;

        public interface ItemPopulator <T> {

            void populate(View view, T item);

        }
        public interface OnItemClicked <T> {

            void onClick(T t);
        }

        private static class Pair <T> {
            public final T t;
            public final View view;

            public Pair(T t, View view) {
                this.t = t;
                this.view = view;
            }
        }

        public MyCustomListView(ViewGroup holder, @LayoutRes int itemLayout,
                                ItemPopulator<T> populator) {
            this.holder = holder;
            this.itemLayout = itemLayout;
            this.populator = populator;
        }

        public void setClickListener(OnItemClicked<T> clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick((T) view.getTag());
            }
        }

        public void add(T t) {
            View view = LayoutInflater.from(holder.getContext()).inflate(itemLayout, holder, false);
            holder.addView(view);
            populator.populate(view, t);
            items.add(new Pair<T>(t, view));

            if (clickListener != null) {
                view.setTag(t);
                view.setOnClickListener(this);
            }
        }

        public void remove(T t) {
            Pair<T> removeItem = null;
            for (Pair<T> item : items) {
                if (item.t.equals(t)) {
                    removeItem = item;
                }
            }
            if (removeItem != null) {
                items.remove(removeItem);
                holder.removeView(removeItem.view);
            }
        }

        public void clear() {
            items.clear();
            holder.removeAllViews();
        }
    }

    private String pagerProgressText(int currentPage, int totalPages) {
        return String.format("%s/%s", String.valueOf(currentPage), String.valueOf(totalPages));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPageText.setText(pagerProgressText(position + 1, gridAdapter.getCount()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(Expense expense) {
        receiptView.add(expense);
    }
}
