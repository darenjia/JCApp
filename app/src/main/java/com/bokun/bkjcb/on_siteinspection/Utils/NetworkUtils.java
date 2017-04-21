package com.bokun.bkjcb.on_siteinspection.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by BKJCB on 2017/3/16.
 * 网络工具
 * 判断网络是否可用
 * 判断网络状态
 */

public class NetworkUtils {
    /***
     *
     * 此方法描述的是： 判断网络状态是否可用
     *
     * @return boolean 返回是否可用，true可用，false不可用
     */
    public static boolean isEnable(Context context) {
        // 网络获得管理器
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        // 判断网络管理器是否为null
        if (manager == null) {
            return false;
        }
        // 获得网络连接信息
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // 判断 netwokInfo是否为null networkInfo是否可用
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }
        // 网络状态可用
        return true;
    }
    public static int getTypeName(Context context) {
        // 网络获得管理器
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        // 判断网络管理器是否为null
        if (manager == null) {
            return 0;
        }
        // 获得网络连接信息
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // 判断 netwokInfo是否为null networkInfo是否可用
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return 0;
        }
        // 返回网络状态 WIFI or MOBILE
        if (networkInfo.getTypeName().equals("WIFI")){
            return Constants.NETWORK_WIFI;
        }
        return Constants.NETWORK_MOBILE;
    }

}
