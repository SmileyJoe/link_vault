package com.smileyjoedev.webstore.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.object.Url;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cody on 2016/03/22.
 */
public class ViewUrlActivity extends BaseActivity {

    public static final String EXTRA_URL_ID = "url_id";
    public static final int REQUEST_EDIT_URL = 1;

    @Bind(R.id.text_note)
    TextView mTextNote;

    @Bind(R.id.text_url)
    TextView mTextUrl;

    @OnClick(R.id.text_url) void urlClick(){
        open();
    }

    private Url mUrl;
    private boolean mHasEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_url);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        mTextNote.setText(mUrl.getNoteSpannable(getBaseContext()));
        mTextNote.setMovementMethod(LinkMovementMethod.getInstance());

        mTextUrl.setText(mUrl.getUrl());

        getSupportActionBar().setTitle(mUrl.getTitle());
        getSupportActionBar().setSubtitle(mUrl.getUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_url, menu);
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
                builder.startActivities();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(getBaseContext(), NewUrlActivity.class);
                intent.putExtra(NewUrlActivity.EXTRA_URL_ID, mUrl.getId());
                startActivityForResult(intent, REQUEST_EDIT_URL);
                return true;
            case R.id.action_delete:
                confirmDelete();
                return true;
            case R.id.action_open:
                open();
                return true;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mUrl.getShareText());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void open(){
        Intent openIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl.getUrl()));
        startActivity(openIntent);
    }

    private void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_confirm_delete, mUrl.getTitle()))
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean success = mUrl.delete();
                        if(success){
                            Toast.makeText(getBaseContext(), R.string.delete_url_success, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.delete_url_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.text_no, null)
        .show();
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
