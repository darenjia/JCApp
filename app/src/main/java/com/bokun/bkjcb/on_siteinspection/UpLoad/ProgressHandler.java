package com.bokun.bkjcb.on_siteinspection.UpLoad;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bokun.bkjcb.on_siteinspection.Domain.UploadTask;

import java.lang.ref.WeakReference;

/**
 * Created by DengShuai on 2017/4/20.
 */

public abstract class ProgressHandler extends Handler {
    public static final int UPDATE = 0x01;
    public static final int START = 0x02;
    public static final int FINISH = 0x03;
    //弱引用
    private final WeakReference<UIProgressListener> mUpLoadListenerWeakReference;

    public ProgressHandler(UIProgressListener upLoadListener) {
        super(Looper.getMainLooper());
        mUpLoadListenerWeakReference = new WeakReference<>(upLoadListener);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE: {
                UIProgressListener uiProgessListener = mUpLoadListenerWeakReference.get();
                if (uiProgessListener != null) {
                    //获得进度实体类
                    UploadTask progressModel = (UploadTask) msg.obj;
                    //回调抽象方法
                    progress(uiProgessListener, progressModel.getFinished(), progressModel.getSize(), progressModel.isDone());
                }
                break;
            }
            case START: {
                UIProgressListener uiProgressListener = mUpLoadListenerWeakReference.get();
                if (uiProgressListener != null) {
                    //获得进度实体类
                    UploadTask progressModel = (UploadTask) msg.obj;
                    //回调抽象方法
                    start(uiProgressListener, progressModel.getFinished(), progressModel.getSize(), progressModel.isDone());

                }
                break;
            }
            case FINISH: {
                UIProgressListener uiProgressListener = mUpLoadListenerWeakReference.get();
                if (uiProgressListener != null) {
                    //获得进度实体类
                    UploadTask progressModel = (UploadTask) msg.obj;
                    //回调抽象方法
                    finish(uiProgressListener, progressModel.getFinished(), progressModel.getSize(), progressModel.isDone());
                }
                break;
            }
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public abstract void start(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done);

    public abstract void progress(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done);

    public abstract void finish(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done);
}