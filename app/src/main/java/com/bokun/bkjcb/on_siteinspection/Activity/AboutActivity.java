package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.SearchedWordDao;
import com.bokun.bkjcb.on_siteinspection.Utils.CacheUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.CheckUpUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.SPUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.io.IOException;

import ezy.boost.update.UpdateUtil;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Switch auto_upload;
    private TextView version, intr, cache_size, clean_search, update, feedback;
    private LinearLayout clean_cache;
    private int resultCode;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);

        version = (TextView) findViewById(R.id.about_version);
        intr = (TextView) findViewById(R.id.about_intr);
        cache_size = (TextView) findViewById(R.id.about_cache_size);
        clean_search = (TextView) findViewById(R.id.about_clean_search);
        update = (TextView) findViewById(R.id.about_update);
        feedback = (TextView) findViewById(R.id.about_comment);
        auto_upload = (Switch) findViewById(R.id.about_auto_upload);
        clean_cache = (LinearLayout) findViewById(R.id.about_clean_cache);
        auto_upload.setChecked(getRecord());
        cache_size.setText(setSize());
    }

    @Override
    protected void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(resultCode);
                finish();
            }
        });
        intr.setOnClickListener(this);
        clean_search.setOnClickListener(this);
        clean_cache.setOnClickListener(this);
        update.setOnClickListener(this);
        feedback.setOnClickListener(this);
        auto_upload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    record(true);
                } else {
                    record(false);
                }
                showToast("设置已生效");
            }
        });
        String versionStr = Utils.getVersion(this);
        if (versionStr != null) {
            version.setText("V" + versionStr + "\t地下空间检查");
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_clean_cache:
                CacheUtil cacheUtil = new CacheUtil();
                try {
                    cacheUtil.getCache();
                    cacheUtil.clean();
                    cacheUtil.close();
                    UpdateUtil.clean(this);
                } catch (IOException e) {
                    cacheUtil.getDiskCacheDir().delete();
                } finally {
                    cache_size.setText("0B");
                    showToast("缓存已清空");
                }
                break;
            case R.id.about_clean_search:
                SearchedWordDao.clean();
                resultCode = 2;
                showToast("记录已清除");
                break;
            case R.id.about_comment:
                Intent intent = new Intent(AboutActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.about_intr:
                Intent intent1 = new Intent(AboutActivity.this, GuideActivity.class);
                startActivity(intent1);
                break;
            case R.id.about_update:
                new CheckUpUtil(this).checkUpadte(true, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(resultCode);
        finish();
    }

    public static void toAboutActiivty(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivityForResult(intent, 1);
        activity.overridePendingTransition(R.anim.in_right, R.anim.in);
    }

    protected void record(boolean flag) {
        SPUtils.put(this, "auto", flag);
    }

    protected boolean getRecord() {
        return (boolean) SPUtils.get(this, "auto", true);
    }

    private String setSize() {
        CacheUtil cacheUtil = new CacheUtil();
        try {
            cacheUtil.getCache();
        } catch (IOException e) {
            cacheUtil.getDiskCacheDir().delete();
            return 0 + "K";
        }
        long size = cacheUtil.getSize();
        long sizeM = size / (1000 * 1000);
        long sizeK = size / 1000;
        if (sizeM > 0.1) {
            return sizeM + "M";
        } else if (sizeK > 0.1) {
            return sizeK + "K";
        } else {
            return size + "B";
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in, R.anim.out_right);
    }
}
