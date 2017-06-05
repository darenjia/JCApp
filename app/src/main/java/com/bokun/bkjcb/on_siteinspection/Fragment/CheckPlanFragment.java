package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bokun.bkjcb.on_siteinspection.Activity.MainActivity;
import com.bokun.bkjcb.on_siteinspection.Activity.SecurityCheckActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.ExpandableListViewAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.CacheUitl;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.bokun.bkjcb.on_siteinspection.View.ConstructionDetailView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/7.
 */

public class CheckPlanFragment extends MainFragment implements RequestListener {


    private ArrayList<CheckPlan> checkPlans;
    private ArrayList<ProjectPlan> projectPlans;
    private ArrayList<ArrayList<CheckPlan>> constuctions;
    private ExpandableListView listview;
    private ExpandableListViewAdapter adapter;
    private AlertDialog dialog;
    public static int DATA_CHANGED = 1;
    public static int DATA_UNCHANGED = 0;
    private Intent intent;
    private CacheUitl cacheUitl;
    private final String key = "sad1ee213124c1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(context, R.layout.content_plan, null);
        initPlanLayout(view);
        return view;
    }

    private void initPlanLayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperlayout);
        listview = (ExpandableListView) view.findViewById(R.id.plan_list);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRecycler));
        //HttpRequestVo requestVo = new HttpRequestVo(Constants.GetXxclScURL, Constants.GetXxclSc.replace("quxian", MainActivity.quxian));
        //OkHttpManager manager = new OkHttpManager(context, this, requestVo);
        getDateFromNet();
        refreshLayout.setRefreshing(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getDateFromNet();
            }
        });
    }

    private void getDateFromNet() {
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("quxian", MainActivity.user.quxian);
        requestVo.getRequestDataMap().put("user", Utils.getUserName());
        requestVo.setMethodName("GetJianChaJiHua");
        HttpManager manager = new HttpManager(context, this, requestVo);
        manager.postRequest();
    }

    private void getCheckPlanFromNet() {
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("quxian", MainActivity.user.quxian);
        requestVo.setMethodName("GetXxclSc");
        HttpManager manager = new HttpManager(context, this, requestVo);
        manager.postRequest();
    }

    @Override
    protected void getDataSucceed(JsonResult object) {

        //new LodingAsyncTask().execute();
        setExpandableListView();
    }

    @Override
    protected void getDataFailed() {
        super.getDataFailed();
        LogUtil.logI(MainActivity.user.quxian);
        setExpandableListView();
    }

    private void setExpandableListView() {
        int width = Utils.getWindowWidthOrHeight(context, "Width");
        int left = width - (width / 10);
        int right = width - (width / 10) + (width / 20);
        listview.setIndicatorBounds(left, right);
        adapter = new ExpandableListViewAdapter(context, projectPlans, constuctions);
        listview.setAdapter(adapter);
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                createDailog(constuctions.get(groupPosition).get(childPosition), groupPosition, childPosition);
                return true;
            }
        });
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = null;
        if (object != null) {
            result = JsonParser.parseSoap((SoapObject) object);
            if (projectPlans == null || projectPlans.size() == 0) {
                projectPlans = JsonParser.getProjectData(result.resData);
                DataUtil.saveProjectPlan(projectPlans);
                getCheckPlanFromNet();
                return;
            }
            cacheUitl = new CacheUitl();
            String cacheStr = cacheUitl.getData(key);
            if (cacheStr == null) {
                cacheUitl.saveData(key, result.resData);
                checkPlans = JsonParser.getJSONData(result.resData);
                LogUtil.logI(checkPlans.size() + "");
                DataUtil.insertCheckPlans(context, checkPlans);
            } else {
                if (!cacheStr.equals(result.resData)) {
                    checkPlans = JsonParser.getJSONData(result.resData);
                    LogUtil.logI(checkPlans.size() + "");
                    DataUtil.insertCheckPlans(context, checkPlans);
                }
            }

            constuctions = new ArrayList<>();
            for (ProjectPlan plan : projectPlans) {
                String sysIDs = plan.getAq_sysid();
                String[] ids = {};
                if (sysIDs != null) {
                    ids = sysIDs.split(",");
                }
                constuctions.add(DataUtil.getCheckPlan(ids));
            }
        } else {
            i = RequestListener.EVENT_GET_DATA_EEEOR;
        }
        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    private void createDailog(final CheckPlan checkPlan, final int groupPosition, final int childPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        ConstructionDetailView constructionDetailView = ConstructionDetailView.getConstructionView(context);
        View view = constructionDetailView.setData(checkPlan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_check) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("checkplan", checkPlan);
                    bundle.putInt("groupPosition", groupPosition);
                    bundle.putInt("childPosition", childPosition);
                    bundle.putInt("state", checkPlan.getState());
                    bundle.putString("aq_lh_id", projectPlans.get(groupPosition).getAq_lh_id());
//                    SecurityCheckActivity.ComeToSecurityCheckActivity(context, bundle);
                    Intent intent = new Intent(context, SecurityCheckActivity.class);
                    intent.putExtras(bundle);
                    CheckPlanFragment.this.startActivityForResult(intent, 1);
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onStart() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cacheUitl != null) {
            cacheUitl.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == DATA_CHANGED) {
                refreshData(data);
            }
        }
    }

    private void refreshData(Intent data) {
        int groupPosition = data.getExtras().getInt("groupPosition");
        int childPosition = data.getExtras().getInt("childPosition");
        int state = data.getExtras().getInt("state");
        if (state == 2) {
            checkSate(groupPosition);
        }
        LogUtil.logI("计划状态题改变，刷新列表数据" + state);
        constuctions.get(groupPosition).get(childPosition).setState(state);
        adapter.notifyDataSetChanged();
        listview.collapseGroup(groupPosition);
        listview.expandGroup(groupPosition);
    }

    private void checkSate(int groupPosition) {
        for (CheckPlan plan : constuctions.get(groupPosition)) {
            if (plan.getState() == 2) {
                continue;
            } else {
                return;
            }
        }
        ProjectPlan projectPlan = projectPlans.get(groupPosition);
        projectPlan.setAq_jctz_zt("等待上传");
        DataUtil.changeProjectState(projectPlan);
    }

    class LodingAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("数据加载中...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void[] objects) {

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {

            }
            dialog.dismiss();
        }
    }
}
