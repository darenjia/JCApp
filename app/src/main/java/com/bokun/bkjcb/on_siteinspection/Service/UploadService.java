package com.bokun.bkjcb.on_siteinspection.Service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Notification.NotificationUtil;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.UpLoad.UIProgressListener;
import com.bokun.bkjcb.on_siteinspection.UpLoad.UploadHelper;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.elvishew.xlog.XLog;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/19.
 */

public class UploadService extends Service {

    UploadHelper helper;
    ArrayList<CheckPlan> checkPlans;
    private NotificationUtil util;
    private ProjectPlan projectPlan;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.logI("开始上传服务");
        helper = new UploadHelper(this);
        util = NotificationUtil.newInstance();
        util.Notify(0, 0);
        if (intent == null) {
            return flags;
        }
        String id = intent.getStringExtra("plan");
        projectPlan = DataUtil.queryProjectPlanById(id);
        UIProgressListener listener = new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                Intent broadcast = new Intent("com.bokun.jcapp.UPDATE_PROGRESS");
                if (done) {
                    LogUtil.logI("contentLength" + contentLength + "===currentBytes" + currentBytes);
                    double precent = (double) currentBytes / (double) contentLength;
                    LogUtil.logI(precent + "百分比");
                    broadcast.putExtra("precent", (int) (precent * 100));
                } else {
                    broadcast.putExtra("precent", -1);
                }
                LocalBroadcastManager.getInstance(UploadService.this).sendBroadcast(broadcast);
            }
        };
//        checkPlans = (ArrayList<CheckPlan>) intent.getSerializableExtra("checkplan");
        helper.startTask(projectPlan, listener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (projectPlan != null) {
            XLog.i("上传服务结束");
            projectPlan = DataUtil.queryProjectPlanById(projectPlan.getAq_lh_id());
            if (projectPlan.getAq_jctz_zt().equals("正在上传")) {
                XLog.i("修改未完成任务状态");
                projectPlan.setAq_jctz_zt("等待上传");
                DataUtil.updateProjectState(projectPlan);
            }
        }
        if (helper != null) {
            helper.onStop();
        }
    }
}
