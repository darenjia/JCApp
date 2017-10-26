package com.bokun.bkjcb.on_siteinspection.Download;

import android.os.Environment;

import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by DengShuai on 2017/10/26.
 * Description :pdf文件下载
 */

public class DownloadManager implements Runnable {

    private List<String> names;

    public DownloadManager(List<String> urls) {
        this.names = urls;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection;
        for (String fileName : names) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FILE_PATH, fileName);
            if (file.exists()) {
                continue;
            }
            try {
                url = new URL(Constants.URL + fileName);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Utils.saveFile(inputStream, file);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
