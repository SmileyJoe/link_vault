package com.smileyjoedev.webstore;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orm.SugarContext;

/**
 * Created by cody on 2016/03/21.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        SugarContext.init(this);
    }
}
