package com.smileyjoedev.autocomplete;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.smileyjoedev.autocomplete.AutoCompleteInterface;

public abstract class AutoCompleteListAdapter<T extends AutoCompleteInterface> extends BaseAdapter implements
        Filterable {

    private ArrayList<T> mFullList;
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;
    private ArrayList<T> mItems;

    protected abstract View populateView(T item, int position, View convertView, ViewGroup parent);

    public AutoCompleteListAdapter(Context context,
                                   ArrayList<T> items) {
        this.mItems = items;
        ArrayList<T> temp = new ArrayList<T>();

        for (int i = 0; i < this.mItems.size(); i++) {
            temp.add(this.mItems.get(i));
        }

        mFullList = (ArrayList<T>) temp;
        mOriginalValues = new ArrayList<T>(mFullList);

    }

    @Override
    public int getCount() {
        return mFullList.size();
    }

    @Override
    public T getItem(int position) {
        return mFullList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return populateView(getItem(position), position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<T>(mFullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<T> list = new ArrayList<T>(
                            mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<T> values = mOriginalValues;
                int count = values.size();

                ArrayList<T> newValues = new ArrayList<T>(count);

                for (int i = 0; i < count; i++) {
                    T item = values.get(i);
                    String text = item.getAutoCompleteText();
                    if (text.toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.values != null) {
                mFullList = (ArrayList<T>) results.values;
            } else {
                mFullList = new ArrayList<T>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }
}