package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Notification.NotificationUtil;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Service.ServiceUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/17.
 */

public class UpLoadChirldFragment extends BaseFragment {

    private ListView listView;
    private LinearLayout layout;
    private ListAdapter adapter;
    private ArrayList<ProjectPlan> projectPlans;
    private boolean finished;
    private CheckBox progress;
    private BroadcastReceiver receiver;
    private LocalBroadcastManager manager;
    private LoadData loadTask;
    private NotificationUtil util;
    private int count = 0;

    @Override
    public View initView() {
        View view = View.inflate(getContext(), R.layout.unfinished_upload_view, null);
        listView = (ListView) view.findViewById(R.id.upload_listview);
        layout = (LinearLayout) view.findViewById(R.id.upload_btn);
        return view;
    }

    @Override
    public void initData() {
        if (finished) {
            layout.setVisibility(View.GONE);
        }
    }

    class ListAdapter extends BaseAdapter {
        ListAdapter.ViewHolder viewHolder;

        @Override
        public int getCount() {
            return projectPlans.size();
        }

        @Override
        public Object getItem(int position) {
            return projectPlans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //final CheckPlan checkPlan = checkPlans.get(position);
            final ProjectPlan projectPlan = projectPlans.get(position);
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.upload_item_view, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.task_title);
                viewHolder.state = (TextView) convertView.findViewById(R.id.task_state);
                viewHolder.button = (CheckBox) convertView.findViewById(R.id.task_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(projectPlan.getAq_lh_jcmc());
            viewHolder.state.setText(getStateText(projectPlan.getState_upload()));
            if (!finished) {
                viewHolder.button.setText(getState(projectPlan.getState_upload()));
                if (projectPlan.getState_upload() == 2 || projectPlan.getState_upload() == 1) {
                    //LogUtil.logI("设置计划状态" + checkPlan.getName());
                    viewHolder.button.setChecked(true);
                } else {
                    viewHolder.button.setChecked(false);
                }
                viewHolder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int flag = position;
                        if (isChecked) {
                            while (flag != 0 && projectPlans.get(flag - 1).getState_upload() == 0) {
                                projectPlans.remove(flag);
                                projectPlans.add(flag - 1, projectPlan);
                                flag--;
                            }
                            if (flag != 0) {
                                projectPlan.setState_upload(1);
                            } else {
                                projectPlan.setState_upload(2);
                            }
                            notifyDataSetChanged();
                            if (position == 0) {
                                startUpload(projectPlan);
                                progress = (CheckBox) buttonView;
                                //viewHolder.state.setText("正在上传");
                                buttonView.setText("0%");
                            } else {
                                buttonView.setText("等待");
                            }
                        } else {
                            if (!buttonView.getText().equals("上传")) {
                                buttonView.setText("上传");
                                projectPlan.setState_upload(0);
                                if (flag != (projectPlans.size() - 1)) {
                                    projectPlans.remove(flag);
                                    projectPlans.add(projectPlan);
                                }
                                stopUpload();
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

            } else {
                viewHolder.state.setVisibility(View.GONE);
                viewHolder.button.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView title;
            TextView state;
            CheckBox button;
        }
    }

    private void startUpload(ProjectPlan plan) {
        util = NotificationUtil.newInstance();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        intent.putExtra("plan", plan);
        getContext().startService(intent);
        openBroadCast();
    }

    private void stopUpload() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        if (ServiceUtil.isServiceRunning("com.bokun.bkjcb.on_siteinspection.Service.UploadService")) {
            getContext().stopService(intent);
        }
    }

    class LoadData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!finished) {
                //checkPlans = DataUtil.queryCheckPlanCanUpLoad(getContext());
                projectPlans = DataUtil.getProjectByState("等待上传");
            } else {
                projectPlans = DataUtil.getProjectByState("上传完成");
                //checkPlans = DataUtil.queryCheckPlanFinished(getContext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (adapter == null) {
                adapter = new ListAdapter();
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public void onStart() {
        LogUtil.logI("onStart");
        if (projectPlans == null) {
            loadTask = new LoadData();
            loadTask.execute();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void openBroadCast() {
        if (receiver != null) {
            return;
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int flag = intent.getExtras().getInt("precent");
                util.Notify(count, flag);
                if (flag == -1) {
                    ProjectPlan projectPlan = projectPlans.get(0);
                    projectPlan.setState_upload(0);
                    adapter.notifyDataSetChanged();
                    return;
                }
                progress.setText(flag + "%");
                if (flag == 100) {
                    ProjectPlan projectPlan = projectPlans.get(0);
                    projectPlan.setAq_jctz_zt("上传完成");
                    DataUtil.changeProjectState(projectPlan);
                    projectPlans.remove(0);
                    LogUtil.logI(projectPlans.size() + "");
                    adapter.notifyDataSetChanged();
                    util.Notify(++count, flag);
                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                    if (finished) {
                        loadTask.execute();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.bokun.jcapp.UPDATE_PROGRESS");
        manager = LocalBroadcastManager.getInstance(getContext());
        manager.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (manager != null && receiver != null) {
            manager.unregisterReceiver(receiver);
        }
    }

    public void refresh(String state) {
        LogUtil.logI("refresh");
        if (loadTask.getStatus() == AsyncTask.Status.FINISHED) {
            //checkPlans = DataUtil.queryCheckPlanFinished(getContext());
            projectPlans = DataUtil.getProjectByState(state);
            adapter.notifyDataSetChanged();
        }
    }

    private String getState(int state) {
        if (state == 0) {
            return "上传";
        } else if (state == 1) {
            return "等待";
        } else if (state == 2) {
            return "0%";
        } else if (state == 3) {
            return "重新上传";
        } else {
            return "完成";
        }
    }

    private String getStateText(int state) {
        if (state == 0) {
            return "等待上传";
        } else if (state == 1) {
            return "等待";
        } else if (state == 2) {
            return "正在上传";
        } else if (state == 3) {
            return "上传已取消";
        } else {
            return "上传完成";
        }
    }
}
