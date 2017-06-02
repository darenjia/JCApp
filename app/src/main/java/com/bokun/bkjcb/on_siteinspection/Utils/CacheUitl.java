package com.bokun.bkjcb.on_siteinspection.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class CacheUitl {
    private Context context;
    private DiskLruCache cache;
    private final int valueCount = 0;
    private final long max_size = 50 * 1024 * 1024;

    public CacheUitl(String fileName) {
        this.context = JCApplication.getContext();
        try {
            cache = DiskLruCache.open(getDiskCacheDir(fileName), getAppVersion(), valueCount,max_size );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
