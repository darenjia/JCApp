package com.bokun.bkjcb.on_siteinspection.Service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.UpLoad.UIProgressListener;
import com.bokun.bkjcb.on_siteinspection.UpLoad.UploadHelper;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/19.
 */

public class UploadService extends Service {

    UploadHelper helper;
    ArrayList<CheckPlan> checkPlans;

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
        ProjectPlan projectPlan = (ProjectPlan) intent.getExtras().getSerializable("plan");
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

}
