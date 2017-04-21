package com.bokun.bkjcb.on_siteinspection.Activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Utils.AppManager;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    private long time = 0;
    private static final int PERMISSIONS_REQUEST = 0;
    private String[] premissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private List<String> hasPremissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        AppManager.getAppManager().addActivity(this);
        context = this;
        initView();
        findView();
        loadData();
        setListener();
        if (isFirst()) {
            checkPermissions();
        }


    }

    //设置页面layout
    protected abstract void initView();

    //初始化控件
    protected abstract void findView();

    //设置控件监听
    protected abstract void setListener();

    //获取数据
    protected abstract void loadData();

    @Override
    protected void onDestroy() {

        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time > 1000) {
            time = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            AppManager.getAppManager().finishActivity();
        }
    }

    public void checkPermissions() {
        ActivityCompat.requestPermissions(this,
                premissions,
                PERMISSIONS_REQUEST);
        writeToSharedPreferences("isFirst", 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        hasPremissions = new ArrayList<>();
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    hasPremissions.add(permissions[i]);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void writeToSharedPreferences(String key, int value) {
        SharedPreferences preferences = getSharedPreferences("default", MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean isFirst() {
        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
        int is = preferences.getInt("isFirst", 1);
        LogUtil.logI("is :" + is);
        return is == 1;
    }

}
