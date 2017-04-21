package com.bokun.bkjcb.on_siteinspection.UpLoad;

/**
 * Created by DengShuai on 2017/4/18.
 */

interface ProgressListener {
    void onUpdate(long bytesWritten, long contentLength, boolean done);
}