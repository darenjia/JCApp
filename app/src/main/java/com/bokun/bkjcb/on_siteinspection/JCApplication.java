package com.bokun.bkjcb.on_siteinspection;

import android.app.Application;
import android.content.Context;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class JCApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
