package com.bokun.bkjcb.on_siteinspection;

import android.app.Application;
import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.Domain.ManagerInfo;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class JCApplication extends Application {
    private static Context context;
    public static ManagerInfo user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
