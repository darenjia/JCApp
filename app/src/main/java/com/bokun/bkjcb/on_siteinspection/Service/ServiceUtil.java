package com.bokun.bkjcb.on_siteinspection.Service;

import android.app.ActivityManager;
import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.JCApplication;

import java.util.List;

/**
 * Created by DengShuai on 2017/6/7.
 */

public class ServiceUtil {

    /**
     * 校验某个服务是否还存在
     */
    public static boolean isServiceRunning(String serviceName) {
        // 校验服务是否还存在
        ActivityManager am = (ActivityManager) JCApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : services) {
            // 得到所有正在运行的服务的名称
            String name = info.service.getClassName();
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }


}
