package com.smileyjoedev.webstore.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.smileyjoedev.webstore.R;
import com.smileyjoedev.webstore.object.Url;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cody on 2016/03/21.
 */
public class NewUrlActivity extends BaseActivity {

    @Bind(R.id.edit_title)
    EditText mEditTitle;

    @Bind(R.id.edit_note)
    EditText mEditNote;

    @Bind(R.id.edit_url)
    EditText mEditUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_url, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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

        Url url = new Url();
        url.setTitle(title);
        url.setNote(note);
        url.setUrl(urlString);

        long dbId = url.save();

        if(dbId > 0){
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, R.string.error_new_url_save_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
