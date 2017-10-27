package com.bokun.bkjcb.on_siteinspection;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.LogBuilder;
import com.orhanobut.logger.Logger;

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
        Logger.initialize(
                new LogBuilder()
                        .showMethodLink(true)
                        .showThreadInfo(true)
                        .globalTag("dengshuai")
                        .methodOffset(0)
//                        .logPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
                        .logPriority(Log.VERBOSE)
                        .build()
        );
    }

    public static Context getContext() {
        return context;
    }
}
