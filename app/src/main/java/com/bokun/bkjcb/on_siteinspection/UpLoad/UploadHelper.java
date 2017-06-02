package com.bokun.bkjcb.on_siteinspection.UpLoad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Domain.FinishedPlan;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.OkHttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/19.
 */

public class UploadHelper {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RequestListener.EVENT_GET_DATA_SUCCESS) {
                prePareFile();
                uploadFile();
            } else {
                Toast.makeText(context, "上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Context context;
    private ArrayList<CheckResult> results;
    private UploadManager manager;
    private ArrayList<String> paths;
    private ArrayList<UpLoadTask> tasks;
    private long fileSize;
    private long uploadSize;
    private ProgressListener listener;

    public UploadHelper(Context context) {
        this.context = context;
    }

    public void startTask(CheckPlan checkPlan, ProgressListener listener) {
        this.listener = listener;
        LogUtil.logI(checkPlan.getName() + " 任务开始");
        FinishedPlan plan = DataUtil.getFinishedPlan(String.valueOf(checkPlan.getSysId()));
        results = DataUtil.readData(context, checkPlan.getIdentifier());
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(results);
        plan.setResult(element);
        String str = gson.toJson(plan);
        LogUtil.logI(str);
        OkHttpManager okHttpManager = new OkHttpManager(context, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                LogUtil.logI("请求上传，请求结果：" + i);
                mHandler.sendEmptyMessage(5);
//                mHandler.sendEmptyMessage(i);
            }
        }, new HttpRequestVo("", str), 2);
        okHttpManager.postRequest();
    }

    private void prePareFile() {
        tasks = new ArrayList<>();
        manager = UploadManager.newInstance();
        paths = new ArrayList<>();
        for (CheckResult result : results) {
            if (result.getImageUrls() != null && result.getImageUrls().size() != 0) {
                paths.addAll(result.getImageUrls());
            }
            if (result.getAudioUrls() != null && result.getAudioUrls().size() != 0) {
                paths.addAll(result.getAudioUrls());
            }
            if (result.getVideoUrls() != null && result.getVideoUrls().size() != 0) {
                paths.addAll(result.getVideoUrls());
            }
        }
        for (int i = 0; i < paths.size(); i++) {
            File file = new File(paths.get(i));
            fileSize += file.length();
        }

    }

    private void uploadFile() {
        LogUtil.logI("任务数量：" + paths.size());
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            File file = new File(path);
            if (file.length() == 0) {
                continue;
            }
            URL url = null;
            try {
                url = new URL("");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            UpLoadTask task = new UpLoadTask(file, url, new UpLoadTask.OnTaskFinished() {
                @Override
                public void finished(UpLoadTask task) {
                    tasks.remove(task);
                    uploadSize += task.getSize();
                    listener.onUpdate(uploadSize, fileSize, manager.getIsFinishedAll());
                    LogUtil.logI("上传文件大小" + uploadSize);
                }
            });
            tasks.add(task);
            manager.addTask(task);
        }

    }

    public void pauseTask() {
        manager.pauseAllTask();
    }
}
