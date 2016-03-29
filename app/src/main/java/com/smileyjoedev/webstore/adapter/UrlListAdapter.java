package com.smileyjoedev.webstore.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.stetho.common.ArrayListAccumulator;
import com.smileyjoedev.autocomplete.AutoCompleteRecyclerAdapter;
import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.activity.ViewUrlActivity;
import com.smileyjoedev.webstore.object.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cody on 2016/03/21.
 */
public class UrlListAdapter extends AutoCompleteRecyclerAdapter<UrlListAdapter.ViewHolder, Url> {

    public static interface Listener{
        public void onItemClick(Url url);
    }

    private Listener mListener;

    public UrlListAdapter(List<Url> items) {
        super(items);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_url, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.render(getItem(position), mListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextTitle;
        private TextView mTextNote;
        private TextView mTextUrl;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextNote = (TextView) itemView.findViewById(R.id.text_note);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextUrl = (TextView) itemView.findViewById(R.id.text_url);
        }

        private void render(final Url url, final Listener listener){
            mTextUrl.setText(url.getUrl());
            mTextTitle.setText(url.getTitle());
            mTextNote.setText(url.getNoteSpannable(itemView.getContext()));
            mTextNote.setMovementMethod(LinkMovementMethod.getInstance());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClick(url);
                    }
                }
            });
        }
    }
}
