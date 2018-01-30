package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Activity.MainActivity;
import com.bokun.bkjcb.on_siteinspection.Activity.SecurityCheckActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.ExpandableListViewAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Download.DownloadManager;
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
import com.bokun.bkjcb.on_siteinspection.Utils.MD5Util;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.bokun.bkjcb.on_siteinspection.View.ConstructionDetailView;
import com.elvishew.xlog.XLog;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.IOException;
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
    private CacheUtil cacheUtil;
    private String key = "sad1ee213124c1";
    private TextView errorView;
    private TextView nullView;
    private boolean planFlag = true;//判断是获取工程还是计划
    private StringBuilder sysIds;
    private ArrayList<String> paths;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(context, R.layout.content_plan, null);
        cacheUtil = new CacheUtil();
        try {
            cacheUtil.getCache();
        } catch (IOException e) {
            cacheUtil.getDiskCacheDir().delete();
        }
        initPlanLayout(view);
        return view;
    }

    private void initPlanLayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperlayout);
        listview = (ExpandableListView) view.findViewById(R.id.plan_list);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRecycler));
        errorView = (TextView) view.findViewById(R.id.error_view);
        nullView = (TextView) view.findViewById(R.id.null_view);
        getDateFromNet();
        refreshLayout.setRefreshing(true);
        checkPlans = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (projectPlans != null) {
                    projectPlans.clear();
                    projectPlans.addAll(DataUtil.queryProjectPlan("上传完成", MainActivity.user.getId()));
                }
                planFlag = true;
                getDateFromNet();
            }
        });
    }

    private void getDateFromNet() {
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("quxian", JCApplication.user.getQuxian());
        requestVo.getRequestDataMap().put("user", Utils.getUserName());
        requestVo.setMethodName("GetJianChaJiHua");
        HttpManager manager = new HttpManager(context, this, requestVo);
        manager.postRequest();
    }

    private void getCheckPlanFromNet() {
        sysIds = new StringBuilder();
        if (projectPlans != null && projectPlans.size() > 0) {
            for (int i = 0; i < projectPlans.size(); i++) {
                String ids = projectPlans.get(i).getAq_sysid();
                if (ids != null && !ids.equals("")) {
                    sysIds.append(ids);
                }
                if (i < projectPlans.size() - 1) {
                    sysIds.append(",");
                }
            }
            LogUtil.logI(sysIds.toString());
            key = MD5Util.encode(sysIds.toString());
            HttpRequestVo requestVo = new HttpRequestVo();
            requestVo.getRequestDataMap().put("quxian", JCApplication.user.getQuxian());
            requestVo.getRequestDataMap().put("sysids", sysIds.toString());
            requestVo.setMethodName("GetXxclSc");
            HttpManager manager = new HttpManager(context, this, requestVo);
            manager.postRequest();
        } else {
            refreshLayout.setRefreshing(false);
            nullView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void getDataSucceed(JsonResult object) {
        errorView.setVisibility(View.GONE);
        LogUtil.logI("获取数据成功");
        setExpandableListView();
        paths = new ArrayList<>();
        for (int i = 0; i < checkPlans.size(); i++) {
            paths.add(checkPlans.get(i).getUrl());
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            new Thread(new DownloadManager(paths)).start();
        } else {
            Snackbar.make(getView(), R.string.mis_error_no_permission_sdcard, Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    getActivity().startActivity(intent);
                }
            }).show();
        }
    }

    @Override
    protected void getDataFailed() {
        super.getDataFailed();
        LogUtil.logI("获取数据失败,从缓存读数据");
        //setExpandableListView();
//        errorView.setVisibility(View.VISIBLE);
        projectPlans = DataUtil.queryProjectPlan("上传完成", MainActivity.user.getId());
        if (projectPlans == null) {
//            errorView.setVisibility(View.VISIBLE);
        }
        setConstuctions();
        setExpandableListView();

    }

    private void setExpandableListView() {
        if (projectPlans.size() == 0) {
            nullView.setVisibility(View.VISIBLE);
            return;
        } else {
            nullView.setVisibility(View.GONE);
        }
        adapter = new ExpandableListViewAdapter(context, projectPlans, constuctions);
        listview.setAdapter(adapter);
        listview.setGroupIndicator(null);
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
            if (projectPlans == null || planFlag) {//|| projectPlans.size() == 0
                projectPlans = JsonParser.getProjectData(result.resData);
                /*有个问题，如果返回数据的SysId发生变化，则此方法要改*/
                if (cacheUtil.cache != null && result.resData != null) {
                    cacheUtil.saveData(Constants.CAAHE_KEY, result.resData);
                }
                boolean flag = DataUtil.saveProjectPlan(projectPlans, MainActivity.user);
                if (flag) {
                    planFlag = false;
                    getCheckPlanFromNet();
                    projectPlans.clear();
                    //projectPlans.addAll(DataUtil.queryProjectPlan("等待上传"));
                    projectPlans.addAll(DataUtil.queryProjectPlan("上传完成", MainActivity.user.getId()));
                    //projectPlans.addAll(DataUtil.queryProjectPlan("需办事项"));
                } else {
                    i = RequestListener.EVENT_GET_DATA_SUCCESS;
                    Message msg = new Message();
                    msg.what = i;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
                return;
            }
            XLog.i("返回数据结果：" + result.resData);
            String cacheStr = null;
            if (cacheUtil.cache != null) {
                cacheStr = cacheUtil.getData(key);
            }
            if (cacheStr == null) {
                if (cacheUtil.cache != null) {
                    cacheUtil.saveData(key, result.resData);
                }
                checkPlans = JsonParser.getJSONData(result.resData);
                XLog.i(checkPlans.size() + "");
                DataUtil.insertCheckPlans(context, checkPlans);
            } else {
                if (!cacheStr.equals(result.resData)) {
                    checkPlans = JsonParser.getJSONData(result.resData);
                    XLog.i(checkPlans.size() + "");
                    DataUtil.insertCheckPlans(context, checkPlans);
                }
            }
            setConstuctions();
        } else {
            LogUtil.logI((object == null) + "");
            i = RequestListener.EVENT_NOT_NETWORD;
        }

        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    private void setConstuctions() {
        if (constuctions == null) {
            constuctions = new ArrayList<>();
        } else {
            constuctions.clear();
        }
        for (ProjectPlan plan : projectPlans) {
            String sysIDs = plan.getAq_sysid();
            String[] ids = {};
            if (sysIDs != null) {
                ids = sysIDs.split(",");
            }
            constuctions.add(DataUtil.getCheckPlan(ids));
        }
    }

    private void createDailog(final CheckPlan checkPlan, final int groupPosition, final int childPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        projectPlans.get(1).setAQ_JCTZ_sfjc(2);
//        LogUtil.logI(projectPlans.get(groupPosition).getAQ_JCTZ_sfjc() + "AQ_JCTZ_sfjc");
        boolean flag = projectPlans.get(groupPosition).getAQ_JCTZ_sfjc() == 1;
        ConstructionDetailView constructionDetailView = ConstructionDetailView.getConstructionView(context);
        View view = constructionDetailView.setData(checkPlan, flag, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_check) {
                    ProjectPlan projectPlan = DataUtil.queryProjectPlanById(projectPlans.get(groupPosition).getAq_lh_id());
                    if (projectPlan.getAq_jctz_zt().equals("正在上传")) {
                        Toast.makeText(context, "该计划正在上传，无法修改", Toast.LENGTH_SHORT).show();
                        return;
                    }
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
        }, projectPlans.get(groupPosition).getAq_lh_jcmc());
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
        if (cacheUtil != null && cacheUtil.cache != null) {
            cacheUtil.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.logI("返回" + requestCode + "|" + resultCode);
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
        LogUtil.logI("计划状态题改变，刷新列表数据" + state);
        constuctions.get(groupPosition).get(childPosition).setState(state);
        checkState(groupPosition);
        adapter.notifyDataSetChanged();
        listview.collapseGroup(groupPosition);
        listview.expandGroup(groupPosition);
    }

    private void checkState(int groupPosition) {
        String state = "等待上传";
        for (CheckPlan plan : constuctions.get(groupPosition)) {
            if (plan.getState() == 2) {
                continue;
            } else {
                state = "需办事项";
                break;
            }
        }
        ProjectPlan projectPlan = projectPlans.get(groupPosition);
        projectPlan.setAq_jctz_zt(state);
        if (state.equals("等待上传")) {
            DataUtil.changeProjectState1(projectPlan);
        }
        //只要修改就该保存状态
//        DataUtil.changeProjectState1(projectPlan);
    }

    public void dateHasChange() {
        projectPlans.clear();
        projectPlans.addAll(DataUtil.queryProjectPlan("上传完成", MainActivity.user.getId()));
        //projectPlans.addAll(DataUtil.queryProjectPlan("需办事项"));

        if (projectPlans.size() == 0) {
            nullView.setVisibility(View.VISIBLE);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            return;
        }
        setConstuctions();
//        adapter.notifyDataSetChanged();
        adapter = new ExpandableListViewAdapter(context, projectPlans, constuctions);
        listview.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getContext()).showMenu(false);
    }
}
