package com.smileyjoedev.webstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.object.Url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cody on 2016/03/21.
 */
public class NewUrlActivity extends BaseActivity {

    public static final String EXTRA_URL_ID = "url_id";

    @Bind(R.id.edit_title)
    EditText mEditTitle;

    @Bind(R.id.edit_note)
    EditText mEditNote;

    @Bind(R.id.edit_url)
    EditText mEditUrl;

    private boolean mIsEdit = false;
    private Url mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        handleEditIntent();
        handleShareIntent();
        mEditUrl.setOnEditorActionListener(new OnDoneClick());
    }

    private void handleEditIntent(){
        Intent intent = getIntent();
        boolean hasUrlId = false;

        if(intent != null){
            Bundle extras = intent.getExtras();

            if(extras != null){
                if(extras.containsKey(EXTRA_URL_ID)){
                    long id = extras.getLong(EXTRA_URL_ID, -1);
                    hasUrlId = true;
                    mUrl = Url.findById(Url.class, id);
                    setTitle(R.string.activity_title_edit_url);
                    populate();
                    mIsEdit = true;
                }
            }
        }

        if(!hasUrlId){
            mUrl = new Url();
            mIsEdit = false;
        }
    }

    private void populate(){
        mEditUrl.setText(mUrl.getUrl());
        mEditNote.setText(mUrl.getNote());
        mEditTitle.setText(mUrl.getTitle());
    }

    private void handleShareIntent(){
        Intent intent = getIntent();

        if(intent != null){
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(intent.getAction()) && type != null) {
                if ("text/plain".equals(type)) {
                    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

                    if(!TextUtils.isEmpty(sharedText)){
                        mEditUrl.setText(sharedText);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_url, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                TaskStackBuilder builder = TaskStackBuilder.create(this)
                        .addNextIntent(mainIntent);

                if(mIsEdit){
                    Intent viewIntent = new Intent(getBaseContext(), ViewUrlActivity.class);
                    viewIntent.putExtra(EXTRA_URL_ID, mUrl.getId());
                    builder.addNextIntent(viewIntent);
                }

                builder.startActivities();

                return true;
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        String title = mEditTitle.getText().toString();
        String urlString = mEditUrl.getText().toString();
        String note = mEditNote.getText().toString();

        mUrl.setTitle(title);
        mUrl.setNote(note);
        mUrl.setUrl(urlString);

        long dbId = mUrl.save();

        if(dbId > 0){
            setResult(RESULT_OK);
            Toast.makeText(this, R.string.error_new_url_save_success, Toast.LENGTH_SHORT).show();
            Ion.with(getBaseContext()).load(mUrl.getUrl()).asString().setCallback(new UrlRetrieved(mUrl));
            finish();
        } else {
            Toast.makeText(this, R.string.error_new_url_save_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private class UrlRetrieved implements FutureCallback<String>{

        private Url mUrl;

        public UrlRetrieved(Url url) {
            mUrl = url;
        }

        @Override
        public void onCompleted(Exception e, String result) {
            Document doc = Jsoup.parse(result);
            mUrl.setContent(doc.body().text());
            mUrl.save();
        }
    }

    private class OnDoneClick implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                save();
                return true;
            } else {
                return false;
            }
        }
    }
}
