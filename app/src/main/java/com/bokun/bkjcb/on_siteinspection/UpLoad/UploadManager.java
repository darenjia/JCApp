package com.bokun.bkjcb.on_siteinspection.UpLoad;

import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DengShuai on 2017/4/18.
 */

public class UploadManager {

    public static UploadManager manager;
    private ExecutorService service;

    private UploadManager() {
//        service = Executors.newFixedThreadPool(10);
        service = Executors.newSingleThreadExecutor();
    }

    public static UploadManager newInstance() {
        if (manager == null) {
            manager = new UploadManager();
        }
        return manager;
    }

    public void addTask(UpLoadTask task) {
        LogUtil.logI("开始一个任务");
        service.execute(task);
    }

    public void stopAllTask() {
        if (!service.isShutdown()) {
            service.shutdown();
        }
    }

    public List<Runnable> pauseAllTask() {
        return service.shutdownNow();
    }

    public boolean getIsFinishedAll() {
        return service.isTerminated();
    }
}
