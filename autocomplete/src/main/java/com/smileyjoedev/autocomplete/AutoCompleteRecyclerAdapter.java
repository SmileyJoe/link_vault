package com.smileyjoedev.autocomplete;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cody on 2016/03/28.
 */
public abstract class AutoCompleteRecyclerAdapter<T extends RecyclerView.ViewHolder, U extends AutoCompleteInterface> extends RecyclerView.Adapter<T> implements Filterable {

    private List<U> mItems;
    private List<U> mOriginalItems;
    private ArrayFilter mFilter;

    public AutoCompleteRecyclerAdapter(List<U> items) {
        setItems(items);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<U> getItems() {
        return mItems;
    }

    public U getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    public void setItems(List<U> items) {
        mOriginalItems = items;
        mItems = new ArrayList<>();
        mItems.addAll(mOriginalItems);
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalItems == null) {
                mOriginalItems = new ArrayList<U>(mItems);
            }

            if (TextUtils.isEmpty(prefix)) {
                ArrayList<U> list = new ArrayList<U>(
                        mOriginalItems);
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString();

                List<U> values = mOriginalItems;
                int count = values.size();

                List<U> newValues = new ArrayList<U>(count);

                for (int i = 0; i < count; i++) {
                    U item = values.get(i);
                    String text = item.getAutoCompleteText();
                    if (text.contains(prefixString)) {
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
                mItems = (ArrayList<U>) results.values;
            } else {
                mItems = new ArrayList<U>();
            }

            notifyDataSetChanged();
        }
    }
}
