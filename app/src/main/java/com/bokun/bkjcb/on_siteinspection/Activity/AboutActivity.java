package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.SearchedWordDao;
import com.bokun.bkjcb.on_siteinspection.Utils.CacheUitl;
import com.bokun.bkjcb.on_siteinspection.Utils.SPUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private AlertDialog dialog;
    private Button button;
    private TextView message;
    private ProgressBar pro_c;
    private ProgressBar pro_h;

    private static class MyHandler extends Handler {
        private final WeakReference<AboutActivity> mActivity;

        public MyHandler(AboutActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AboutActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case RequestListener.EVENT_NOT_NETWORD:
                        break;
                    case RequestListener.EVENT_CLOSE_SOCKET:
                        break;
                    case RequestListener.EVENT_NETWORD_EEEOR:
                        break;
                    case RequestListener.EVENT_GET_DATA_EEEOR:
                        break;
                    case RequestListener.EVENT_GET_DATA_SUCCESS:
                        activity.pro_c.setVisibility(View.GONE);
                        activity.message.setText("当前版本已是最新版");
                        //activity.button.setVisibility(View.VISIBLE);
                     /*   JsonResult result = (JsonResult) msg.obj;
                        if (result.success) {
                            MainActivity.ComeToMainActivity(activity, result.resData);
                        } else {
                        }*/
                        break;

                }
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);
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
        version.setOnClickListener(this);
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
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_clean_cache:
                CacheUitl cacheUitl = new CacheUitl();
                try {
                    cacheUitl.getCache();
                    cacheUitl.clean();
                    cacheUitl.close();
                } catch (IOException e) {
                    cacheUitl.getDiskCacheDir().delete();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = View.inflate(this, R.layout.update_dialog_view, null);
                button = (Button) view.findViewById(R.id.update_btn);
                message = (TextView) view.findViewById(R.id.update_message);
                pro_c = (ProgressBar) view.findViewById(R.id.update_proBar_c);
                pro_h = (ProgressBar) view.findViewById(R.id.update_proBar_h);
                message.setText("正在检查");
                mHandler.sendEmptyMessageDelayed(5, 2000);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
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
        CacheUitl cacheUitl = new CacheUitl();
        try {
            cacheUitl.getCache();
        } catch (IOException e) {
            cacheUitl.getDiskCacheDir().delete();
            return 0 + "K";
        }
        long size = cacheUitl.getSize();
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
