package com.bokun.bkjcb.on_siteinspection.Utils;

import android.util.Log;

/**
 * Created by BKJCB on 2017/3/16.
 */

public class LogUtil {

    private static final String AUTHOR = "dengshuai";
    private static boolean isLog = Constants.ISLOG;

    public static boolean isLog() {
        return isLog;
    }

    public static void setIsLog(boolean isLog) {
        LogUtil.isLog = isLog;
    }

    public static void logD(String tag,String msg){
        if (isLog){
            Log.d(tag,msg);
        }
    }
    public static void logE(String tag,String msg){
        if (isLog){
            Log.e(tag,msg);
        }
    }
    public static void logV(String tag,String msg){
        if (isLog){
            Log.v(tag,msg);
        }
    }
    public static void logI(String tag,String msg){
        if (isLog){
            Log.i(tag,msg);
        }
    }
    public static void logI(String msg){
        if (isLog){
            Log.i(AUTHOR,msg);
        }
    }

}
