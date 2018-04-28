package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Adapter.PagerAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProgressDetail;
import com.bokun.bkjcb.on_siteinspection.Fragment.ProgressDetailItem;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ProgressDetailActivity extends BaseActivity implements RequestListener {

    private Toolbar toolbar;
    private RelativeLayout error_view;
    private LinearLayout pro_view;
    private ViewPager viewPager;
    private TextView title;
    private String seqId;
    private String name;
    private int pageSize;
    private TextView pageNum;

    private static class MyHandler extends Handler {
        private final WeakReference<ProgressDetailActivity> mActivity;

        public MyHandler(ProgressDetailActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ProgressDetailActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case RequestListener.EVENT_NOT_NETWORD:

                    case RequestListener.EVENT_CLOSE_SOCKET:

                    case RequestListener.EVENT_NETWORD_EEEOR:

                    case RequestListener.EVENT_GET_DATA_EEEOR:
                        activity.error_view.setVisibility(View.VISIBLE);
                        break;
                    case RequestListener.EVENT_GET_DATA_SUCCESS:
                        JsonResult result = (JsonResult) msg.obj;
                        if (result.success) {
                            activity.getDataSuccess(result);
                        } else {
                            activity.error_view.setVisibility(View.VISIBLE);
                        }
                        break;

                }
            }
        }
    }

    private ProgressDetailActivity.MyHandler mHandler = new ProgressDetailActivity.MyHandler(this);
    private HttpManager httpManager;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_progress_detail);
    }

    @Override
    protected void findView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("检查进度详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
        error_view = (RelativeLayout) findViewById(R.id.error_tip);
        pro_view = (LinearLayout) findViewById(R.id.pro_view);
        viewPager = (ViewPager) findViewById(R.id.pro_content);
        title = (TextView) findViewById(R.id.pro_title);
        pageNum = (TextView) findViewById(R.id.pro_page);

        seqId = getIntent().getStringExtra("seqid");
        name = getIntent().getStringExtra("name");
    }

    @Override
    protected void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageNum.setText((position + 1) + "/" + pageSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void loadData() {
        getData();
    }

    protected void getDataSuccess(JsonResult result) {
        error_view.setVisibility(View.GONE);
        pro_view.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        ArrayList<ProgressDetail> list = JsonParser.getProgressDetail(result.resData);
        List<android.support.v4.app.Fragment> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ProgressDetailItem item = new ProgressDetailItem();
            item.setData(list.get(i));
            pages.add(item);
        }
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        pageSize = list.size();
        title.setText(name);
        pageNum.setText("1/" + pageSize);
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = JsonParser.parseSoap((SoapObject) object);
        Message message = new Message();
        message.what = i;
        message.obj = result;
        mHandler.sendMessage(message);
    }

    private void getData() {
        HttpRequestVo request = new HttpRequestVo();
        request.getRequestDataMap().put("aq_lh_seqid", seqId);
        request.setMethodName("GetAqJcGC");
        httpManager = new HttpManager(this, this, request);
        httpManager.postRequest();
        pro_view.setVisibility(View.VISIBLE);
        error_view.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

    public static void comeToProgressDetailAcicity(Context context, String seqid, String name) {
        Intent intent = new Intent(context, ProgressDetailActivity.class);
        intent.putExtra("seqid", seqid);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
