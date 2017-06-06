package com.bokun.bkjcb.on_siteinspection.UpLoad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Domain.FinishedPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Ftp.FtpUploadTask;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
               /* JsonResult result = (JsonResult) msg.obj;
                LogUtil.logI(result.message);
                ToastUtil.show(context, result.message);
                if (result.success) {
                    listener.onUpdate(100, 100, true);
                } else {
                    listener.onUpdate(100, 100, false);
                }*/
            } else {
                Toast.makeText(context, "上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Context context;
    private ArrayList<CheckResult> results;
    private ArrayList<ArrayList<CheckResult>> resultList;
    private UploadManager manager;
    private ArrayList<String> paths;
    private ArrayList<String> remotePaths;
    private ArrayList<UpLoadTask> tasks;
    private List<File> files;
    private long fileSize;
    private long uploadSize;
    private ProgressListener listener;
    private ArrayList<FinishedPlan> finishedPlans;
    private ArrayList<CheckPlan> checkPlans;
    private ArrayList<String> jsons;
    private String reqStr;
    private int size;
    private int flag = 0;

    public interface OnFinishedListener {
        void finish();
    }

    public UploadHelper(Context context) {
        this.context = context;
    }

    public void startTask(ProjectPlan projectPlan, ProgressListener listener) {
        this.listener = listener;
        finishedPlans = new ArrayList<>();
        checkPlans = new ArrayList<>();
        resultList = new ArrayList<>();
        jsons = new ArrayList<>();
        remotePaths = new ArrayList<>();
        LogUtil.logI(projectPlan.getAq_lh_jcmc() + " 任务开始");
        String s = projectPlan.getAq_sysid();
        String[] strings = s.split(",");
        for (String str : strings) {
            FinishedPlan plan = DataUtil.getFinishedPlan(str);
            CheckPlan checkPlan = DataUtil.queryCheckPlan(context, plan.getSysGcxxdjh());
            remotePaths.add(String.valueOf(checkPlan.getIdentifier()));
            results = DataUtil.readData(context, checkPlan.getIdentifier());
            resultList.add(results);
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(results);
            plan.setResult(element);
            String json = gson.toJson(plan);
            jsons.add(json);
            size = jsons.size();
            finishedPlans.add(plan);
            checkPlans.add(checkPlan);
        }



      /*  OkHttpManager okHttpManager = new OkHttpManager(context, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                LogUtil.logI("请求上传，请求结果：" + i);
                mHandler.sendEmptyMessage(5);
//                mHandler.sendEmptyMessage(i);
            }
        }, new HttpRequestVo("", str), 2);
        okHttpManager.postRequest();*/
        sendData();
    }

    private void sendData() {
        String json = jsons.get(flag);
        LogUtil.logI(json);
        HttpRequestVo request = new HttpRequestVo();
        request.getRequestDataMap().put("jcnr", json);
        request.setMethodName("GetJianChaShuJu");
        HttpManager manager = new HttpManager(context, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                JsonResult result = JsonParser.parseSoap((SoapObject) object);
                if (result.success) {
                  /*  jsons.remove(0);
                    listener.onUpdate(jsons.size(), finishedPlans.size(), true);
                    sendData(jsons.get(0));
                    return;*/
                    flag++;
                    Message message = new Message();
                    message.what = i;
                    message.obj = result;
                    mHandler.sendMessage(message);
                }
            }
        }, request);
        manager.postRequest();
    }

    private void prePareFile() {
        //tasks = new ArrayList<>();
        paths = new ArrayList<>();
        files = new ArrayList<>();
        ArrayList<CheckResult> list = resultList.get(flag);
        for (CheckResult result : list) {
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
            files.add(file);
            fileSize += file.length();
        }

    }

    private void uploadFile() {
        LogUtil.logI("任务数量：" + paths.size());
        String path = "UploadFlie/" + remotePaths.get(flag);
       /* for (int i = 0; i < paths.size(); i++) {
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
        }*/
        FtpUploadTask task = new FtpUploadTask(files, path, new OnFinishedListener() {
            @Override
            public void finish() {
                sendData();
            }
        });
        task.execute();

    }

    public void pauseTask() {
        manager.pauseAllTask();
    }
}
