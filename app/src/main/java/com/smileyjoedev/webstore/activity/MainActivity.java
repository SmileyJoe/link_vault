package com.smileyjoedev.webstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smileyjoedev.webstore.R;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_NEW_URL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        findViewById(R.id.fab_new_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewUrlActivity.class);
                startActivityForResult(intent, REQUEST_NEW_URL);
            }
        });
    }
}
