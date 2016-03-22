package com.smileyjoedev.webstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.adapter.UrlListAdapter;
import com.smileyjoedev.webstore.object.Url;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.recycler_url)
    RecyclerView mRecyclerUrl;

    @Bind(R.id.layout_empty)
    LinearLayout mLayoutEmpty;

    private UrlListAdapter mUrlListAdapter;

    private static final int REQUEST_NEW_URL = 1;
    private static final int REQUEST_VIEW_URL = 2;

    @OnClick(R.id.fab_new_url) void newUrl(){
        Intent intent = new Intent(this, NewUrlActivity.class);
        startActivityForResult(intent, REQUEST_NEW_URL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        populateList();
        setupRecycler();
    }

    private void setupRecycler(){
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        mRecyclerUrl.setLayoutManager(manager);
        mRecyclerUrl.setAdapter(mUrlListAdapter);
    }

    private void populateList(){
        List<Url> items = Url.listAll(Url.class, "M_TITLE ASC");

        if(mUrlListAdapter == null){
            mUrlListAdapter = new UrlListAdapter(items);
            mUrlListAdapter.setListener(new UrlListAdapter.Listener() {
                @Override
                public void onItemClick(Url url) {
                    Intent intent = new Intent(getBaseContext(), ViewUrlActivity.class);
                    intent.putExtra(ViewUrlActivity.EXTRA_URL_ID, url.getId());
                    startActivityForResult(intent, REQUEST_VIEW_URL);
                }
            });
        } else {
            mUrlListAdapter.setItems(items);
            mUrlListAdapter.notifyDataSetChanged();
        }

        if(items.size() <= 0){
            mLayoutEmpty.setVisibility(View.VISIBLE);
            mRecyclerUrl.setVisibility(View.GONE);
        } else {
            mLayoutEmpty.setVisibility(View.GONE);
            mRecyclerUrl.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_NEW_URL:
                if(resultCode == RESULT_OK){
                    populateList();
                }
                break;
            case REQUEST_VIEW_URL:
                if(resultCode == RESULT_OK){
                    populateList();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
