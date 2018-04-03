package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.CacheUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.elvishew.xlog.XLog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class InfoActivity extends BaseActivity implements OnErrorListener, RequestListener {

    private Toolbar toolbar;
    private String fileName;
    private PDFView pdfView;
    private File file;
    private LoadData loadData;
    private boolean flag;
    private CheckPlan checkPlan;
    private ProjectPlan plan;
    private RelativeLayout errorLayout;
    private Button tryAgain;


    private class MyHandler extends Handler {
        private final WeakReference<InfoActivity> mActivity;

        public MyHandler(InfoActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final InfoActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case RequestListener.EVENT_NOT_NETWORD:
                        Snackbar.make(activity.pdfView, "无网络连接，请检查网络", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                activity.startActivity(intent);
                            }
                        }).show();
                        openError();
                        break;
                    case RequestListener.EVENT_CLOSE_SOCKET:
                    case RequestListener.EVENT_NETWORD_EEEOR:
                        Snackbar.make(activity.pdfView, "请确认网络是否可用！", Snackbar.LENGTH_LONG).show();
                        openError();
                        break;
                    case RequestListener.EVENT_GET_DATA_EEEOR:
                        Snackbar.make(activity.pdfView, "该工程信息登记表打开错误！", Snackbar.LENGTH_LONG).show();
                        openError();
                        break;
                    case RequestListener.EVENT_GET_DATA_SUCCESS:
                        JsonResult result = (JsonResult) msg.obj;
                        if (result.success) {
                            getUrlSuccess(result.resData);
                        } else {
                            Snackbar.make(activity.pdfView, result.message, Snackbar.LENGTH_LONG).show();
                            openError();
                        }
                        break;

                }
            }
        }


    }

    private void openError() {
        errorLayout.setVisibility(View.VISIBLE);
    }
    private void hideError(){
        errorLayout.setVisibility(View.GONE);
    }
    private InfoActivity.MyHandler mHandler = new InfoActivity.MyHandler(this);

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
        errorLayout = (RelativeLayout) findViewById(R.id.error_layout);
        tryAgain = (Button) findViewById(R.id.try_again);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        flag = getIntent().getBooleanExtra("isFromNet", false);
    }

    @Override
    protected void setListener() {
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequestVo request = new HttpRequestVo();
                request.getRequestDataMap().put("SysId", String.valueOf(checkPlan.getSysId()));
                request.setMethodName("Getpdf");
                HttpManager httpManager = new HttpManager(InfoActivity.this, InfoActivity.this, request, 2);
                httpManager.postRequest();
            }
        });
    }


    @Override
    protected void loadData() {
        fileName = getIntent().getStringExtra("url");
        checkPlan = (CheckPlan) getIntent().getSerializableExtra("plan");
        if (TextUtils.isEmpty(fileName) && checkPlan != null) {
//            fileName = checkPlan.getUrl();
            HttpRequestVo request = new HttpRequestVo();
            request.getRequestDataMap().put("SysId", String.valueOf(checkPlan.getSysId()));
            request.setMethodName("Getpdf");
            HttpManager httpManager = new HttpManager(this, this, request, 2);
            httpManager.postRequest();
            return;
        }
        if (fileName == null || fileName.equals("")) {
            Toast.makeText(this, "该工程暂无信息登记表！", Toast.LENGTH_SHORT).show();
            return;
        }
//        XLog.i("打开pdf文件：" + fileName);
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
            isCanCheck();
        }
        return true;
    }

    private void isCanCheck() {
        checkPlan.setPlan_type(1);//1表示临时检查
        ProjectPlan projectPlan = DataUtil.queryCheckPlanIsFinished(checkPlan);
        plan = new ProjectPlan();
        if (projectPlan == null) {
            String py = Utils.getQuxianPY(JCApplication.user.getQuxian());
            XLog.i("区县：" + py);
            plan.setAq_lh_id(py + Utils.getDate("yyyyMMddhhmmss"));//必须id
            plan.setAq_lh_jcmc(Utils.getDate("yy-MM-dd") + checkPlan.getName() + "临时检查");//生成名称
            plan.setAq_sysid(String.valueOf(checkPlan.getSysId()));
            plan.setAq_lh_seqid(plan.getAq_lh_id());
            plan.setAq_jctz_zt("无需办事项");
            plan.setAq_lh_jcrq(Utils.getDate("yyyy/MM/dd"));
            DataUtil.saveProjectPlan(plan, JCApplication.user);
            DataUtil.insertCheckPlan(context, checkPlan);
            SecurityCheckActivity.ComeToSecurityCheckActivity(this, checkPlan, true, plan.getAq_lh_id());
        } else {
            Date date = new Date(projectPlan.getAq_lh_jcrq());
            boolean isOverTime = System.currentTimeMillis() - date.getTime() > (24 * 60 * 60 * 1000);
            if ((projectPlan.getAq_jctz_zt().equals("上传完成") && isOverTime)||(!projectPlan.getAq_jctz_zt().equals("正在上传")&&!projectPlan.getAq_jctz_zt().equals("上传完成"))) {
                plan.setAq_lh_id(projectPlan.getAq_lh_id());//必须id
                plan.setAq_lh_jcmc(projectPlan.getAq_lh_jcmc());//生成名称
                plan.setAq_sysid(String.valueOf(checkPlan.getSysId()));
                plan.setAq_lh_seqid(projectPlan.getAq_lh_id());
                plan.setAq_lh_jcrq(projectPlan.getAq_lh_jcrq());
                SecurityCheckActivity.ComeToSecurityCheckActivity(this, checkPlan, true, plan.getAq_lh_id());
            } else {
                StyledDialog.context = context;
                StyledDialog.buildIosAlertVertical("提示", "当前工程近期已被检查过，暂无法再次发起临时检查！", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }

                    @Override
                    public void onThird() {

                    }

                }).setBtnText("取消").show();
            }
        }
    }

    private void setPDFView() {
        file = getFile(fileName);
        pdfView.useBestQuality(true);
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableSwipe(true)
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
        XLog.i("直接打开pdf error:" + fileName);
        if (loadData == null) {
            if (NetworkUtils.isEnable(this)) {
                loadData = new LoadData();
                loadData.execute();
            }
        } else {
            Toast.makeText(this, "打开文件失败，请连接网络再试！", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUrlSuccess(String url) {
        if (NetworkUtils.isEnable(this)) {
            fileName = JsonParser.getURL(url);
//            XLog.i(fileName);
            LoadData loadData = new LoadData();
            loadData.execute();
        }
    }

    private class LoadData extends AsyncTask<Void, Void, InputStream> {

        @Override
        protected InputStream doInBackground(Void... voids) {
            if (file != null && file.exists()) {
                file.delete();
            }
            InputStream inputStream = null;
            CacheUtil cacheUtil = new CacheUtil();
            try {

                String str = JCApplication.isDebug() ? Constants.TEST_URL : Constants.URL;
                URL url = new URL(str + fileName);
                try {
                    cacheUtil.getCache();
                    inputStream = cacheUtil.getInputStreamData(getCacheName(fileName));
                } catch (IOException e) {
                    inputStream = null;
                }
                if (inputStream == null) {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    inputStream = urlConnection.getInputStream();
//                    Utils.saveInputStreamCache(url, getCacheName(fileName));
                    LogUtil.logI("联网获取pdf");
                } else {
                    LogUtil.logI("从缓存获取pdf");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream o) {
           hideError();
            pdfView.fromStream(o).load();
        }
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = JsonParser.parseSoap((SoapObject) object);
        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    private File getFile(String fileName) {
        String parent = Constants.FILE_PATH;
        File file = null;
        if (fileName != null && !fileName.equals("")) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + parent, fileName);
        }
        return file;
    }

    private String getCacheName(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
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

