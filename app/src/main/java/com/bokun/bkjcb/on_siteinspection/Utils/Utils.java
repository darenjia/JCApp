package com.bokun.bkjcb.on_siteinspection.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bokun.bkjcb.on_siteinspection.JCApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BKJCB on 2017/3/24.
 */

public class Utils {

    private String version;

    public static Bitmap compressBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight = 800;
        int reqWidth = 480;
        if (height < reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap compressBitmap(String path, int w, int h) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight = 800;
        int reqWidth = 480;
        if (height < reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
        mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, w, h);
        return mBitmap;
    }

    public static Bitmap compressBitmap(Bitmap bitmap) {
        Bitmap bm;
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        int length = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();
//        int length = 150;
        LogUtil.logI("width:" + length);
        bm = Bitmap.createBitmap(bitmap, 0, 0, length,
                length, matrix, true);
        return bm;
    }

    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void copyFile(String oldPath, File newFlie) {
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                if (oldfile.isFile()) {
                   /* if (!newFlie.exists()) {
                        newFlie.mkdirs();
                        newFlie.createNewFile();
                    }*/
                    InputStream inStream = new FileInputStream(oldPath); //读入原文件
                    FileOutputStream fs = new FileOutputStream(newFlie);
                    byte[] buffer = new byte[4 * 1024];
                    int length;
                    while ((length = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, length);
                    }
                    inStream.close();
                    oldfile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void initSystemBar(Activity activity, Toolbar mToolbar) {
        compat6(activity);
        initToolBar(activity, mToolbar);
    }

    /**
     * 如果布局文件里面没添加android:fitsSystemWindows="true"的话,就需要调用该方法
     * 该方法是让toolbar加上statubar高度,然后顶上去
     *
     * @param activity
     * @param toolbar
     */

    private static void initToolBar(Activity activity, Toolbar toolbar) {
        int mStatuBarHeight = getStatusBarHeight(activity);
//        int mToolBarHeight = (int) (getResources().getDimension(R.dimen.home_toolbar_hei) + mStatuBarHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams param4 = toolbar.getLayoutParams();
            param4.height = param4.height + mStatuBarHeight;
            toolbar.setLayoutParams(param4);
            toolbar.setPadding(0, mStatuBarHeight, 0, 0);
        }
    }

    public static void compat6(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getNavigationBarHeight(activity) == 0) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setStatusBarColor(activity.getResources().getColor(R.color.blue));
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取虚拟按键栏高度
     *
     * @param context
     * @return
     */

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 判断底部navigator是否已经显示
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasNavBar(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static int getWindowWidthOrHeight(Context context, String type) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        if (type.equals("Height")) {
            return metrics.heightPixels;
        } else {
            return metrics.widthPixels;
        }
    }

    public static boolean gpsIsOpen(Context context) {
        LocationManager alm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    public static String getUserName() {
        String username = (String) SPUtils.get(JCApplication.getContext(), "UserName", "");
        return username;
    }

    public static String getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        String version = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static boolean saveFile(InputStream in, File file) {
//        File file = new File(path, fileName);
        try {
            FileOutputStream stream = new FileOutputStream(file);
            int length;
            byte[] buffer = new byte[4 * 1024];
            while ((length = in.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

    public static boolean deleteFile(String fileName) {
        LogUtil.logI("删除文件：" + fileName);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FILE_PATH, fileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public String getLocalVersion(Context context) {
        String version = "";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取当前日期
     */
    public static String getDate(String template) {
        Date date = new Date(System.currentTimeMillis());
        return new SimpleDateFormat(template, Locale.CHINA).format(date);
    }

    //流转字符串
    public static String getStreamString(InputStream tInputStream) {
        if (tInputStream != null) {
            try {
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
                StringBuilder tStringBuffer = new StringBuilder();
                String sTempOneLine;
                while ((sTempOneLine = tBufferedReader.readLine()) != null) {
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将一个字符串转化为输入流
     */
    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null &&
                !sInputString.trim().equals("")) {
            try {
                return new ByteArrayInputStream(sInputString.getBytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 缓存流文件
     */
    public static void saveInputStreamCache(URL url, String name) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            CacheUtil cacheUtil = new CacheUtil();
            cacheUtil.getCache();
            cacheUtil.saveData(name, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.logI("写入缓存失败");
        }

    }
}

