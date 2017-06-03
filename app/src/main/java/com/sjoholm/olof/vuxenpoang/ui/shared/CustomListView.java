package com.sjoholm.olof.vuxenpoang.ui.shared;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CustomListView<T> implements View.OnClickListener {
    private List<Pair<T>> items = new ArrayList<>();
    private ViewGroup holder;
    private final int itemLayout;
    private final ItemPopulator<T> populator;
    private OnItemClicked<T> clickListener;

    public interface ItemPopulator<T> {

        void populate(View view, T item);
    }

    public interface OnItemClicked<T> {

        void onClick(T t);
    }

    private static class Pair<T> {
        final T t;
        final View view;

        public Pair(T t, View view) {
            this.t = t;
            this.view = view;
        }
    }

    public CustomListView(ViewGroup holder, @LayoutRes int itemLayout,
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
