package com.bokun.bkjcb.on_siteinspection;

import android.app.Application;
import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.facebook.stetho.Stetho;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class JCApplication extends Application {
    private static Context context;
    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return context;
    }
}
