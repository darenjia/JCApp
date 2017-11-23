package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Adapter.PagerAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Domain.FinishedPlan;
import com.bokun.bkjcb.on_siteinspection.Fragment.CheckItemFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.CheckPlanFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.LastFragment;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.FileUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.bokun.bkjcb.on_siteinspection.View.AlertGuidBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class SecurityCheckActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ImageView btn_forward;
    private ImageView btn_next;
    private TextView page_num;
    private ArrayList<Fragment> fragments;
    private List<String> contents;
    private ArrayList<CheckResult> results;
    private CheckPlan plan;
    private boolean isChecked;
    private PagerAdapter pagerAdapter;
    private AlertDialog dialog;
    private int contentSize;
    private ArrayList<CheckResult> backup;
    private boolean isTemp;
    private String aq_lh_id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        plan = (CheckPlan) getIntent().getSerializableExtra("checkplan");
        isTemp = getIntent().getBooleanExtra("isTemp", false);
        aq_lh_id = getIntent().getStringExtra("aq_lh_id");
        setContentView(R.layout.activity_securitycheck);
        toolbar = (Toolbar) findViewById(R.id.toolbar_secAct);
//        toolbar.setTitle("安全检查");
        toolbar.setTitle("安全检查" + "(行业管理方面)");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
    }

    @Override
    protected void findView() {
        viewPager = (ViewPager) findViewById(R.id.check_viewpager);
        btn_forward = (ImageView) findViewById(R.id.btn_forward);
        btn_next = (ImageView) findViewById(R.id.btn_next);
        page_num = (TextView) findViewById(R.id.txt_page);

             /*判断该检查是否检查*/
        isChecked = DataUtil.queryCheckPlanState(this, plan.getIdentifier()) != 0;

    }

    private void initFragments() {
        LoadTask task = new LoadTask();
        task.execute();
    }

    @Override
    protected void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        viewPager.addOnPageChangeListener(this);
        btn_next.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        page_num.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        checkIsChecked();
    }

    private void checkIsChecked() {
        if (isChecked) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("检测到该项检查已经执行过，是否继续执行？")
                    .setPositiveButton("继续检查", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            results = DataUtil.readData(context, plan.getIdentifier(),aq_lh_id);
                            backup = new ArrayList<>(results);
                            initFragments();
                        }
                    })
                    .setNegativeButton("重新检查", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LogUtil.logI("重新检查，清空数据");
                            results = new ArrayList<>();
                            DataUtil.cleanData(context, plan.getIdentifier());
                            initFragments();
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();

        } else {
            results = new ArrayList<>();
            initFragments();
        }
    }

    public static void ComeToSecurityCheckActivity(Activity Context, CheckPlan plan, boolean isTemp,String aqId) {
        Intent intent = new Intent(Context, SecurityCheckActivity.class);
        intent.putExtra("checkplan", plan);
        intent.putExtra("isTemp", isTemp);
        intent.putExtra("aq_lh_id", aqId);
        Context.startActivityForResult(intent,0);
    }

    public List<String> getCheckItems() {
        List checkItems = new ArrayList<>();
        JSONObject object = null;
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = getAssets().open("ChecItemskDetail");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            object = new JSONObject(builder.toString());
            JSONArray array = object.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                checkItems.add(obj.getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contentSize = checkItems.size();
        return checkItems;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.logI("fragment position:" + position);
        /*if (mMenu != null) {
            if (position != 14) {
                mMenu.findItem(R.id.btn_submit).setVisible(false);
            }
        }
        LogUtil.logI("position:" + mMenu.hasVisibleItems());*/
        page_num.setText((position + 1) + "/" + fragments.size());
        if (position < 15) {
            toolbar.setTitle("安全检查" + "(行业管理方面)");
        } else {
            toolbar.setTitle("安全检查" + "(民防设施方面)");

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_submit_btn) {
            if (viewPager.getCurrentItem() != contentSize) {
                viewPager.setCurrentItem(contentSize);
            } else {
                ((LastFragment) fragments.get(contentSize)).submit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == btn_forward.getId()) {
            viewPager.arrowScroll(View.FOCUS_LEFT);
//            LogUtil.logI("click forward");
        } else if (id == btn_next.getId()) {
            viewPager.arrowScroll(View.FOCUS_RIGHT);
//            LogUtil.logI("click forward");
        } else if (id == page_num.getId()) {
            List<String> strings = new ArrayList<>();
            strings.addAll(contents);
            strings.add("处理意见");
           /* String[] title = new String[contentSize + 1];
            contents.toArray(title);
            title[contentSize] = "处理意见";
            new AlertView(null, null, "取消", null,
                    title, this, AlertView.Style.ActionSheet,
                    new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            viewPager.setCurrentItem(position, false);
                        }
                    }).show();*/
            new AlertGuidBuilder(strings, this, new AlertGuidBuilder.OnClickListener() {
                @Override
                public void onClick(int position) {
                    viewPager.setCurrentItem(position, false);
                }
            }, viewPager.getCurrentItem()).builder();
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("检查还未完成，是否退出？")
                .setCancelable(true)
                .setNegativeButton("保存退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData(1, null);
                        setResultData(1);
                        finish();
                    }
                })
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("直接退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGarbage();
                        saveData(1, null);
                        setResultData(1);
                        finish();
                    }
                })
                .show();
    }

    private void setResultData(int state) {
        if (!isTemp) {
            Intent old = getIntent();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("groupPosition", old.getExtras().getInt("groupPosition"));
            bundle.putInt("childPosition", old.getExtras().getInt("childPosition"));
            bundle.putInt("state", state);
            intent.putExtras(bundle);
            setResult(CheckPlanFragment.DATA_CHANGED, intent);
        } else {
            setResult(state);
        }
    }

    private boolean saveData(int state, String time) {
        plan.setState(state);
        if (time != null) {
            FinishedPlan finishedPlan = new FinishedPlan();
            finishedPlan.setFinishedTime(time);
            finishedPlan.setUsername(Utils.getUserName());
            finishedPlan.setSysID(plan.getSysId());
            finishedPlan.setSysGcxxdjh(plan.getIdentifier());
            finishedPlan.setAQ_LH_ID(aq_lh_id);
            DataUtil.saveFinishedPlan(finishedPlan);
        }
        DataUtil.updateCheckPlanState(this, plan);
        return DataUtil.saveData(this, results);
    }

    private void deleteGarbage() {
        if (results.size() > 0) {
            FileUtils.deleteFile(results, backup);
        }
    }

    private class LoadTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            fragments = new ArrayList<>();
            contents = getCheckItems();
            for (int i = 0; i < contents.size() + 1; i++) {
                Fragment fragment;
                Bundle bundle = new Bundle();
                if (i < contents.size()) {
                    fragment = new CheckItemFragment();
                    bundle.putString("content", contents.get(i));
                    bundle.putInt("id", i);
                } else {
                    LastFragment lastFragment = new LastFragment();
                    lastFragment.setClickListener(new LastFragment.OnClick() {
                        @Override
                        public void onClick(String time) {
                            boolean is = saveData(2, time);
                            Toast.makeText(SecurityCheckActivity.this, "数据已储存", Toast.LENGTH_SHORT).show();
                            setResultData(2);
                            finish();
                        }
                    });
                    fragment = lastFragment;
                }
                CheckResult result;
                if (isChecked && results.size() == contentSize + 1) {
                    result = results.get(i);
                } else {
                    result = new CheckResult();
                    result.setIdentifier(plan.getIdentifier());
                    result.setAq_lh_id(aq_lh_id);
                    result.setNum(i + 1);
                    results.add(result);
                }
                bundle.putSerializable("result", result);
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            viewPager.setAdapter(pagerAdapter);
            page_num.setText(String.format("%d/%d", 1, contents.size() + 1));
            View parent = (View) page_num.getParent();
            parent.setVisibility(View.VISIBLE);
        }
    }
}
