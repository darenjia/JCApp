package com.bokun.bkjcb.on_siteinspection.Domain;

/**
 * Created by DengShuai on 2017/4/20.
 */

public class UploadTask {

    private long finished;
    private long size;
    private int percent;
    private boolean isDone;

    public UploadTask(long finished, long size, boolean isDone) {
        this.finished = finished;
        this.size = size;
        this.isDone = isDone;
    }

    public UploadTask() {

    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
