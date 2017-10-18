package com.bokun.bkjcb.on_siteinspection.Utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/Bokun/formats/";
    public static String SDPATH1 = Environment.getExternalStorageDirectory()
            + "/Bokun/tempImage/";

    public static void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createSDDir(String fileName) throws IOException {
        File dir = new File(fileName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File parent = new File(SDPATH);
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(path); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static void deleteFile(ArrayList<CheckResult> results) {
        final ArrayList<CheckResult> checkResults = new ArrayList<>();
        checkResults.addAll(results);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> paths = new ArrayList<>();
                for (CheckResult result : checkResults) {
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
                for (String path : paths) {
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }

            }
        }).start();
    }

    public static void deleteFile(ArrayList<CheckResult> results, final ArrayList<CheckResult> backup) {
        final ArrayList<CheckResult> checkResults = new ArrayList<>();
        if (backup == null) {
            deleteFile(results);
            return;
        }
        checkResults.addAll(results);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> paths = new ArrayList<>();
                ArrayList<String> oldpaths = new ArrayList<>();
                for (CheckResult result : checkResults) {
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
                for (CheckResult result : backup) {
                    if (result.getImageUrls() != null && result.getImageUrls().size() != 0) {
                        oldpaths.addAll(result.getImageUrls());
                    }
                    if (result.getAudioUrls() != null && result.getAudioUrls().size() != 0) {
                        oldpaths.addAll(result.getAudioUrls());
                    }
                    if (result.getVideoUrls() != null && result.getVideoUrls().size() != 0) {
                        oldpaths.addAll(result.getVideoUrls());
                    }
                }
                for (String path : paths) {
                    if (!oldpaths.contains(path)) {
                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        }).start();
    }
}
