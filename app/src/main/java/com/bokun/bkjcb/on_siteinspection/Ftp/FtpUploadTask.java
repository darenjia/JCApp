package com.bokun.bkjcb.on_siteinspection.Ftp;

import android.os.AsyncTask;

import com.bokun.bkjcb.on_siteinspection.UpLoad.UploadHelper;
import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DengShuai on 2017/6/6.
 */

public class FtpUploadTask extends AsyncTask<Object, Integer, Object> {

    private FtpUploadTask mUploadPicAsyncTask;
    private FtpUploadTask mUploadFailAsyncTask;
    private List<File> fileList;
    private HashMap<Integer,ArrayList<String>> pathMap;
    private String folderPath;
    private List<File> fileUploadFailList = new ArrayList<>();
    private UploadHelper.OnFinishedListener finishedListener;

    public FtpUploadTask(HashMap<Integer,ArrayList<String>> pathMap, String folderPath, UploadHelper.OnFinishedListener listener) {
        this.pathMap = pathMap;
        this.folderPath = folderPath;
        this.finishedListener = listener;
    }

    @Override
    protected Object doInBackground(final Object... params) {
        try {
            //多文件上传
            new FtpUtils().uploadMultiFile(pathMap, folderPath, new FtpUtils.UploadProgressListener() {
                //上传进度
                int result = 0;

                @Override
                public void onUploadProgress(String currentStep, long uploadSize, File file) {
                    LogUtil.logI("onUploadProgress: " + currentStep);
                    if (currentStep.equals(Constants.FTP_CONNECT_FAIL)) {
                        //连接失败，取消任务
                        if (mUploadFailAsyncTask != null && mUploadFailAsyncTask.getStatus() == Status.RUNNING) {
                            mUploadFailAsyncTask.cancel(true);
                        }
                        if (mUploadPicAsyncTask != null && mUploadPicAsyncTask.getStatus() == Status.RUNNING) {
                            mUploadPicAsyncTask.cancel(true);
                        }
                        finishedListener.failed();
                        LogUtil.logI("onUploadProgress: " + "FTP_CONNECT_FAIL");
                    } else {
                        if (currentStep.equals(Constants.FTP_UPLOAD_SUCCESS)) {
                            result = 100;
                        } else if (currentStep.equals(Constants.FTP_UPLOAD_LOADING)) {
                            long fize = file.length();
                            float num = (float) uploadSize / (float) fize;
                            result = (int) (num * 100);
                        } else if (currentStep.equals(Constants.FTP_UPLOAD_FAIL)) {
                            fileUploadFailList.add(file);
                            return;
                        }
                        if (file != null) {
                            publishProgress(result, file.hashCode());
                        }
                    }
                    if (isCancelled()) {
                        return;
                    }
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fileUploadFailList != null && fileUploadFailList.size() > 0) {
                //处理上传失败
                for (File file : fileUploadFailList) {
                    LogUtil.logI("onUploadProgress: " + file.getPath());
                }
                if (mUploadFailAsyncTask != null && mUploadFailAsyncTask.getStatus() == Status.RUNNING) {
                    mUploadFailAsyncTask.cancel(true);
                }
                if (mUploadPicAsyncTask != null && mUploadPicAsyncTask.getStatus() == Status.RUNNING) {
                    mUploadPicAsyncTask.cancel(true);
                }
                if (isCancelled()) {
                    return fileUploadFailList;
                }
                return fileUploadFailList;
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        LogUtil.logI("onProgressUpdate: " + values[0]);
        if (values[0] == 100) {
            finishedListener.updateProgress();
        }
        if (isCancelled()) {
            LogUtil.logI("onProgressUpdate: " + "isCancelled");
            return;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object preAlarmMap) {
        LogUtil.logI("Jason", "onPostExecute: " + "完成");
        //上传完成后，讲任务对象置空
        if (mUploadFailAsyncTask != null && mUploadFailAsyncTask.getStatus() == Status.FINISHED) {
            mUploadFailAsyncTask = null;
        }
        if (mUploadPicAsyncTask != null && mUploadPicAsyncTask.getStatus() == Status.FINISHED) {
            mUploadPicAsyncTask = null;
        }
        if (finishedListener != null) {
            finishedListener.finish();
        }
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        ArrayList<File> fileUploadFailList = (ArrayList<File>) o;
       /* if (fileUploadFailList != null && fileUploadFailList.size() > 0) {
            //多张图片在上传中，出现失败，关闭上次任务，然后进行重新上传
            LogUtil.logI("onCancelled: " + "I onCancelled=====");
            mUploadFailAsyncTask = (FtpUploadTask) new FtpUploadTask(fileUploadFailList, folderPath, null).execute();
            Toast.makeText(JCApplication.getContext(), "图片上传失败,正在重新上传", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JCApplication.getContext(), Constants.FTP_CONNECT_FAIL, Toast.LENGTH_SHORT).show();
        }*/
    }
}
