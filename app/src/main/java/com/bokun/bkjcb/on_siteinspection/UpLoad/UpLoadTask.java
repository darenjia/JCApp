package com.bokun.bkjcb.on_siteinspection.UpLoad;

import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by DengShuai on 2017/4/18.
 */

public class UpLoadTask implements Runnable {
    public final static String IMAGE_NAME = "image";
    public final static String AVDIO_NAME = "audio";
    public final static String VIDEO_NAME = "video";
    private File file;
    private URL url;
    private ProgressListener progressListener;
    private UpLoadListener loadListener;
    private String name = "file";
    private long size;
    private OnTaskFinished onFinished;

    interface OnTaskFinished {
        void finished(UpLoadTask task);
    }

    public UpLoadTask(File file, URL url, ProgressListener progressListener, UpLoadListener loadListener, String name) {
        this.file = file;
        this.url = url;
        this.progressListener = progressListener;
        this.loadListener = loadListener;
        this.name = name;
    }

    public UpLoadTask(File file, URL url, OnTaskFinished finished) {
        this.file = file;
        this.url = url;
        this.onFinished = finished;
    }

    /* private void uploadMultiFile() {
                final String url = "upload url";
                File file = new File("fileDir", "test.jpg");
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", "test.jpg", fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();


                final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
                OkHttpClient okHttpClient  = httpBuilder
                        //设置超时
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "uploadMultiFile() e=" + e);
                    }


                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG, "uploadMultiFile() response=" + response.body().string());
                    }
                });
            }*/
    /*测试上传任务
    * 让线程休眠3秒
    * */
    public void run() {
       /* String filename = LocalTools.getFileName(file);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(name, filename, fileBody)
                .build();
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, progressListener);

        Request request = new Request.Builder()
                .url(url)
                .post(progressRequestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            loadListener.onFail();
        }
        if (response.isSuccessful()) {
            loadListener.onSuccess();
        } else {
            loadListener.onFail();
        }*/
        try {
            Thread.sleep(1000);
            LogUtil.logI("一个小任务完成");
            onFinished.finished(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class ProgressRequestBody extends RequestBody {

        private final RequestBody requestBody;
        private final ProgressListener progressListener;
        private BufferedSink bufferedSink;

        public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
            this.requestBody = requestBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                //包装
                bufferedSink = Okio.buffer(sink(sink));
            }
            //写入
            requestBody.writeTo(bufferedSink);
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();
        }

        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                long contentLength;
                long bytesWritten;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    if (contentLength == 0) {
                        //获得contentLength的值，后续不再调用
                        contentLength = contentLength();
                    }
                    //增加当前写入的字节数
                    bytesWritten += byteCount;
                    //回调
                    progressListener.onUpdate(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            };
        }
    }

    public long getSize() {
        return file.length();
    }
}
