package com.sjoholm.olof.vuxenpoang.ui.shared;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class GridPagerAdapter<T> extends PagerAdapter
        implements View.OnClickListener {
    private final List<T> list;

    public GridPagerAdapter(List<T> list) {
        this.list = list;
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

        List<T> expenses = getExpensesInGrid(position);
        for (int i = 0; i < 4; i ++) {
            T expense = i < expenses.size() ? expenses.get(i) : null;
            final View view;
            if (expense != null) {
                view = createChild(inflater, gridLayout);
                view.setTag(expense);
                view.setOnClickListener(this);
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

    public abstract void onItemClicked(T item);

    public abstract void populateChild(View view, T item);

    @Override
    public void onClick(View v) {
        onItemClicked((T) v.getTag());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private List<T> getExpensesInGrid(int position) {
        int start = position * 4;
        int end = Math.min(start + 4, list.size());
        List<T> expenses = new ArrayList<>(end - start);
        for (int i = start; i < end; i++) {
            expenses.add(list.get(i));
        }
        return expenses;
    }
}
