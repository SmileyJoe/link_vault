package com.smileyjoedev.webstore.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    public static final String EXTRA_SEARCH_TEXT = "search_text";

    public static Intent getIntentWithSearch(Context context, String text){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_SEARCH_TEXT, text);
        return intent;
    }

    @Bind(R.id.recycler_url)
    RecyclerView mRecyclerUrl;

    @Bind(R.id.layout_empty)
    LinearLayout mLayoutEmpty;

    private UrlListAdapter mUrlListAdapter;
    private SearchView mSearchView;

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
        handleExtras(getIntent());
    }

    private void handleExtras(Intent intent){
        Bundle extras = intent.getExtras();

        if(extras != null){
            if(extras.containsKey(MainActivity.EXTRA_SEARCH_TEXT)){
                String searchText = extras.getString(MainActivity.EXTRA_SEARCH_TEXT);

                if(mSearchView != null){
//                    String newQuery = mSearchView.getQuery().toString().trim() + " " + searchText;
                    mSearchView.setQuery(searchText, true);
                    mSearchView.setIconified(false);
                    mSearchView.clearFocus();
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleExtras(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = null;
        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(new SearchTextWatcher());
        }

        return super.onCreateOptionsMenu(menu);
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

    private class SearchTextWatcher implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mUrlListAdapter.getFilter().filter(newText);
            return false;
        }
    }
}
