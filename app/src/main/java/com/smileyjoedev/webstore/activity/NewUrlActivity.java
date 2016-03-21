package com.smileyjoedev.webstore.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.smileyjoedev.webstore.R;

/**
 * Created by cody on 2016/03/21.
 */
public class NewUrlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
