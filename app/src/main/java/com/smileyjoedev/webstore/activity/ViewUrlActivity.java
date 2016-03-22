package com.smileyjoedev.webstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.object.Url;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cody on 2016/03/22.
 */
public class ViewUrlActivity extends BaseActivity {

    public static final String EXTRA_URL_ID = "url_id";
    public static final int REQUEST_EDIT_URL = 1;

    @Bind(R.id.text_title)
    TextView mTextTitle;

    @Bind(R.id.text_note)
    TextView mTextNote;

    @Bind(R.id.text_url)
    TextView mTextUrl;

    private Url mUrl;
    private boolean mHasEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_url);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        handleExtras();
    }

    private void handleExtras(){
        Intent intent = getIntent();

        if(intent != null){
            Bundle extras = intent.getExtras();

            if(extras != null){
                if(extras.containsKey(EXTRA_URL_ID)){
                    long id = extras.getLong(EXTRA_URL_ID);
                    mUrl = Url.findById(Url.class, id);
                    populate();
                }
            }
        }
    }

    private void populate(){
        mTextNote.setText(mUrl.getNote());
        mTextTitle.setText(mUrl.getTitle());
        mTextUrl.setText(mUrl.getUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_url, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                Intent intent = new Intent(getBaseContext(), NewUrlActivity.class);
                intent.putExtra(NewUrlActivity.EXTRA_URL_ID, mUrl.getId());
                startActivityForResult(intent, REQUEST_EDIT_URL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_EDIT_URL:
                if(resultCode == RESULT_OK){
                    mUrl = Url.findById(Url.class, mUrl.getId());
                    mHasEdit = true;
                    populate();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mHasEdit){
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}
