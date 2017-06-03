package com.sjoholm.olof.vuxenpoang.ui;

import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

import java.util.List;

class ExpenseGridPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private final List<Expense> list;
    private OnClickListener listener;

    public interface OnClickListener {

        void onClick(Expense expense);
    }

    public ExpenseGridPagerAdapter(List<Expense> list, OnClickListener onClickListener) {
        this.list = list;
        listener = onClickListener;
    }

    @Override
    public int getCount() {
        int size = list.size();
        if (size == 0) {
            return 1;
        }
        return size % 4 == 0 ? size / 4 : size / 4 + 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        GridLayout gridLayout = new GridLayout(context);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        gridLayout.setLayoutParams(params);
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(2);
        container.addView(gridLayout);

        Expense[] expenses = getExpensesInGrid(position);
        for (int i = 0; i < 4; i ++) {
            Expense expense = i < expenses.length ? expenses[i] : null;
            final View view;
            if (expense != null) {
                view = createChild(inflater, gridLayout);
                populateChild(view, expense);
            } else {
                view = createChild(inflater, gridLayout);
            }
            gridLayout.addView(view);
        }

        return gridLayout;
    }

    private View createChild(LayoutInflater inflater, GridLayout gridLayout) {
        View view = inflater.inflate(R.layout.sell_available_item, gridLayout, false);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        view.setLayoutParams(params);
        return view;
    }

    private void populateChild(View view, Expense expense) {
        ((TextView) view.findViewById(R.id.title)).setText(expense.name);
        ((TextView) view.findViewById(R.id.cost)).setText(String.valueOf(expense.cost));
        view.setOnClickListener(this);
        view.setTag(expense);
    }

    @Override
    public void onClick(View v) {
        Expense expense = (Expense) v.getTag();
        listener.onClick(expense);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private Expense[] getExpensesInGrid(int position) {
        int start = position * 4;
        int end = Math.min(start + 4, list.size());
        Expense[] expenses = new Expense[end - start];
        for (int i = start; i < end; i++) {
            expenses[i - start] = list.get(i);
        }
        return expenses;
    }
}
