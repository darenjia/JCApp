package com.bokun.bkjcb.on_siteinspection.UpLoad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
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
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DengShuai on 2017/4/19.
 */

public class UploadHelper {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RequestListener.EVENT_GET_DATA_SUCCESS) {
                JsonResult result = (JsonResult) msg.obj;
                if (result.success || (result.message.startsWith("Duplicate") && result.message.endsWith("\'AQ_ID\'"))) {
                    prePareFile();
                    uploadFile();
                } else {
                    listener.onUpdate(0, 0, false);
//                    listener.onUpdate(1, 1, true);
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show();
                }
            } else {
                listener.onUpdate(0, 0, false);
                Toast.makeText(context, "上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Context context;
    private ArrayList<CheckResult> results;
    private ArrayList<ArrayList<CheckResult>> resultList;
    private UploadManager manager;
    private ArrayList<String> paths;
    private HashMap<Integer, ArrayList<String>> pathMap;
    private ArrayList<String> remotePaths;
    private String projectId;
    private ArrayList<UpLoadTask> tasks;
    //    private List<File> files;
    private long fileSize;
    private long uploadSize;
    private ProgressListener listener;
    private ArrayList<FinishedPlan> finishedPlans;
    private ArrayList<CheckPlan> checkPlans;
    private ArrayList<String> jsons;
    private String reqStr;
    private int size;
    private int flag = 0;
    private FtpUploadTask task;
    private int type;//检查类型

    public interface OnFinishedListener {
        void finish();

        void updateProgress();

        void failed();
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
        String s = projectPlan.getAq_sysid();
        LogUtil.logI(projectPlan.getAq_lh_jcmc() + " 任务开始：" + s);
        projectId = projectPlan.getAq_lh_seqid();
        String[] strings = s.split(",");
        FinishedPlan plan;
        for (String str : strings) {
            plan = DataUtil.getFinishedPlan(str);
            type = plan.getType();
            CheckPlan checkPlan = DataUtil.queryCheckPlan(context, plan.getSysGcxxdjh());
            remotePaths.add(String.valueOf(checkPlan.getSysId()));
            results = DataUtil.readData(context, checkPlan.getIdentifier(), projectPlan.getAq_lh_id());
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
        sendData();
    }

    private void sendData() {
        if (flag >= size) {
            return;
        }
        String json = jsons.get(flag);
        json = json.replaceAll("[^\\u0000-\\uFFFF]", "");
        LogUtil.logI(json);
        final HttpRequestVo request = new HttpRequestVo();
        request.getRequestDataMap().put("jcnr", json);
        if (type == 0){
            request.setMethodName("GetJianChaShuJu");
        }else {
            request.setMethodName("GetlsJianChaShuJu");
        }
        HttpManager manager = new HttpManager(context, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                JsonResult result = JsonParser.parseSoap((SoapObject) object);
//                if (result.success) {
                 /* jsons.remove(0);
                    listener.onUpdate(jsons.size(), finishedPlans.size(), true);
                    sendData(jsons.get(0));
                    return;*/
                Message message = new Message();
                message.what = i;
                message.obj = result;
                mHandler.sendMessage(message);
//                } else {
//                    listener.onUpdate(0, 0, false);
//                }
            }
        }, request);
        manager.postRequest();
        //测试
//        prePareFile();
//        uploadFile();
    }

    private void prePareFile() {
        //tasks = new ArrayList<>();
        pathMap = new HashMap<>();
//        files = new ArrayList<>();
        ArrayList<CheckResult> list = resultList.get(flag);
        for (CheckResult result : list) {
            paths = new ArrayList<>();
            if (result.getImageUrls() != null && result.getImageUrls().size() != 0) {
                paths.addAll(result.getImageUrls());
            }
            if (result.getAudioUrls() != null && result.getAudioUrls().size() != 0) {
                paths.addAll(result.getAudioUrls());
            }
            if (result.getVideoUrls() != null && result.getVideoUrls().size() != 0) {
                paths.addAll(result.getVideoUrls());
            }
            if (paths.size() > 0) {
                pathMap.put(result.getNum(), paths);
            }
        }
       /* for (int i = 0; i < paths.size(); i++) {
            File file = new File(paths.get(i));
            files.add(file);
            fileSize += file.length();
        }*/

    }

    private void uploadFile() {
        LogUtil.logI("任务数量：" + pathMap.size());
        if (pathMap.size() == 0) {
            flag++;
            listener.onUpdate(flag, size, true);
            sendData();
            return;
        }
        String path;
        if (JCApplication.isDebug()) {
            path = "Test/" + projectId + "/" + remotePaths.get(flag);
        } else {
            path = "downpdf/" + projectId + "/" + remotePaths.get(flag);
        }
        LogUtil.logI("上传路径：" + path);
        task = new FtpUploadTask().newInstance(pathMap, path, new OnFinishedListener() {
            @Override
            public void finish() {
                // listener.onUpdate((flag + 1), size, true);
                flag++;
                sendData();
            }

            @Override
            public void updateProgress() {
                listener.onUpdate((flag + 1), size, true);
            }

            @Override
            public void failed() {
                listener.onUpdate(flag, size, false);
            }
        });
        task.execute();
    }

    public void pauseTask() {
        manager.pauseAllTask();
    }

    public void onStop() {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
    }
}
