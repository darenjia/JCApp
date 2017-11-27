package com.bokun.bkjcb.on_siteinspection.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class CacheUtil {
    private Context context;
    public DiskLruCache cache;
    private final int valueCount = 1;
    private final long max_size = 50 * 1024 * 1024;
    private final String fileName = "jianchajihua";

    public CacheUtil() {
        this.context = JCApplication.getContext();
    }

    //主动抛出异常
    public void getCache() throws IOException {
        cache = DiskLruCache.open(getDiskCacheDir(), getAppVersion(), valueCount, max_size);
    }

    public File getDiskCacheDir() {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + fileName);
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

    public boolean saveData(String key, String str) {
        try {
            DiskLruCache.Editor editor = cache.edit(key);
            editor.set(0, str);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveData(String key, InputStream is) {
        try {
            DiskLruCache.Editor editor = cache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            reader(is, outputStream);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getData(String key) {
        String str = null;
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot != null) {
                str = snapshot.getString(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public InputStream getInputStreamData(String key) {
        InputStream is = null;
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot != null) {
                is = snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public Long getSize() {
        return cache.size();
    }

    public boolean clean() {
        try {
            cache.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        try {
            cache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reader(InputStream is, OutputStream os) throws IOException {
        int length = 0;
        byte[] temp = new byte[4 * 1024];
        while ((length = is.read(temp)) != -1) {
            os.write(temp, 0, length);
        }
    }
}
