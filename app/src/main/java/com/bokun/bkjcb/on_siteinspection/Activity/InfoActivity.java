package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.elvishew.xlog.XLog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoActivity extends BaseActivity implements OnErrorListener {

    private Toolbar toolbar;
    private String fileName;
    private PDFView pdfView;
    private File file;
    private LoadData loadData;
    private boolean flag;
    private CheckPlan checkPlan;
    private ProjectPlan plan;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_info);
    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        toolbar.setTitle("基本信息登记表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //mmp true:来自服务器，可发起临时检查
        flag = getIntent().getBooleanExtra("isFromNet", false);
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void loadData() {
        fileName = getIntent().getStringExtra("url");
        checkPlan = (CheckPlan) getIntent().getSerializableExtra("plan");
        if (TextUtils.isEmpty(fileName) && checkPlan != null) {
            fileName = checkPlan.getUrl();
        }
//        new LoadData().execute();
//        photoView.setImageBitmap();
       /* pdfView.fromAsset("上海市地下工程基本信息登记表.pdf")
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .onError(this)// in dp
                .load();*/
        if (fileName == null || fileName.equals("")) {
            Toast.makeText(this, "该工程暂无信息登记表！", Toast.LENGTH_SHORT).show();
            return;
        }
        XLog.i("打开pdf文件：" + fileName);
        setPDFView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (flag) {
            getMenuInflater().inflate(R.menu.menu_info, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //发起临时检查
        if (item.getItemId() == R.id.info_check) {
            plan = new ProjectPlan();
            plan.setAq_lh_id("SH" + checkPlan.getSysId());//必须id
            plan.setAq_lh_jcmc(Utils.getDate("yy-MM-dd") + checkPlan.getName() + "临时检查");//生成名称
            plan.setAq_sysid(String.valueOf(checkPlan.getSysId()));
            DataUtil.saveProjectPlan(plan, JCApplication.user);
            SecurityCheckActivity.ComeToSecurityCheckActivity(this, checkPlan, true, plan.getAq_lh_id());
        }
        return true;
    }

    private void setPDFView() {
        file = getFile(fileName);
        pdfView.useBestQuality(true);
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .enableAntialiasing(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onError(this)
                .load();
    }


    public static void ComeInfoActivity(Context context, String url) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void ComeInfoActivity(Context context, CheckPlan plan, boolean flag) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("plan", plan);
        intent.putExtra("isFromNet", flag);
        context.startActivity(intent);
    }

    @Override
    public void onError(Throwable throwable) {
        XLog.i("error" + fileName);
        if (loadData == null) {
            if (NetworkUtils.isEnable(this)) {
                loadData = new LoadData();
                loadData.execute();
            }
        } else {
            Toast.makeText(this, "打开文件失败，请在有网的环境下稍后再试！", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if (file.exists()) {
                file.delete();
            }
            try {
                URL url = new URL(Constants.URL + fileName);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Utils.saveFile(inputStream, file);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            setPDFView();
        }
    }

    private File getFile(String fileName) {
        String parent = Constants.FILE_PATH;
        File file = null;
        if (fileName != null && !fileName.equals("")) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + parent, fileName);
        }
        return file;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 2) {
            if (plan != null) {
                plan.setAq_jctz_zt("等待上传");
                DataUtil.changeProjectState1(plan);
            }
        }
    }
}
//临时检查：
/*
* 1、会不会与安全检查id重复
* 2、会不会重复检查
* 3、aq_lh_id的值怎么办
* 4、seq_id文件夹命名没有
* */
