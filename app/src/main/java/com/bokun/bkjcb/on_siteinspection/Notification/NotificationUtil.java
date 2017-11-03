package com.bokun.bkjcb.on_siteinspection.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by DengShuai on 2017/6/8.
 */

public class NotificationUtil {
    private Context context;
    private static NotificationUtil util;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private Notification notification;
    private PendingIntent pendingIntent;

    private NotificationUtil() {
        this.context = JCApplication.getContext();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        /*Intent intent = new Intent(context, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);*/
    }

    public static NotificationUtil newInstance() {
        if (util == null) {
            util = new NotificationUtil();
        }
        return util;
    }

    public void Notify(int count, int progress) {
        builder.setSmallIcon(R.mipmap.check_app);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.upload);
        builder.setLargeIcon(largeIcon);
        builder.setContentText("已完成" + count + "个");
        builder.setAutoCancel(true);
        if (progress == -1) {
            builder.setContentTitle("上传失败");
        } else if (progress == 0) {
            builder.setContentTitle("上传进度");
            builder.setProgress(100, progress, true);
        } else {
            builder.setContentTitle("上传进度");
            builder.setProgress(100, progress, false);
        }
        builder.setWhen(System.currentTimeMillis());
        //builder.setContentIntent(pendingIntent);
        notification = builder.build();
        manager.notify(1, notification);
    }

    public void NotifyFail(int count) {
        builder.setSmallIcon(R.mipmap.check_app);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.check_app);
        builder.setLargeIcon(largeIcon);
        builder.setContentTitle("上传失败");
        builder.setContentText("已完成" + count + "个");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        notification = builder.build();
        manager.notify(1, notification);
    }
}
