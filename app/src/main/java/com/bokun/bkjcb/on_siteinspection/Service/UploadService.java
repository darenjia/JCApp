package com.bokun.bkjcb.on_siteinspection.Service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
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
        helper = new UploadHelper(this);
        CheckPlan checkPlan = (CheckPlan) intent.getExtras().getSerializable("checkplan");
        UIProgressListener listener = new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                int precent = (int) (contentLength / contentLength) * 100;
                LogUtil.logI(precent + "百分比");
                Intent broadcast = new Intent("com.bokun.jcapp.UPDATE_PROGRESS");
                broadcast.putExtra("precent", precent);
                LocalBroadcastManager.getInstance(UploadService.this).sendBroadcast(broadcast);
            }
        };
//        checkPlans = (ArrayList<CheckPlan>) intent.getSerializableExtra("checkplan");
        helper.startTask(checkPlan, listener);
        return super.onStartCommand(intent, flags, startId);
    }

}
