package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    private final String serviceName = "com.bokun.bkjcb.on_siteinspection.Service.UploadService";
    private ListView listView;
    private LinearLayout layout;
    private ListAdapter adapter;
    private ArrayList<ProjectPlan> projectPlans;
    private ProjectPlan task;
    private boolean finished;
    private Button progress;
    private Button startAll;
    private BroadcastReceiver receiver;
    private LocalBroadcastManager manager;
    private LoadData loadTask;
    private NotificationUtil util;
    private int count = 0;
    private UpLoadFragment.OnDataChangeListener listener;

    @Override
    public View initView() {
        View view = View.inflate(getContext(), R.layout.unfinished_upload_view, null);
        listView = (ListView) view.findViewById(R.id.upload_listview);
        layout = (LinearLayout) view.findViewById(R.id.upload_btn);
        startAll = (Button) view.findViewById(R.id.upload_start_all);
        return view;
    }

    @Override
    public void initData() {
        if (finished) {
            startAll.setText("清空已完成");
        } else {
            startAll.setText("全部开始");
        }
        startAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finished) {
                    for (int i = 0; i < projectPlans.size(); i++) {
                        ProjectPlan projectPlan = projectPlans.get(i);
                        if (projectPlan.getState_upload() == 0) {
                            projectPlan.setState_upload(1);
                        }
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    DataUtil.deleteFinishedProjectPlan();
                    projectPlans.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        if (finished) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ProjectPlan plan = projectPlans.get(position);
                    plan.setAq_jctz_zt("等待上传");
                    DataUtil.changeProjectState(plan);
                    return true;
                }
            });
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final int flg = position;
            final ProjectPlan projectPlan = projectPlans.get(flg);
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.upload_item_view, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.task_title);
                viewHolder.state = (TextView) convertView.findViewById(R.id.task_state);
                viewHolder.button = (Button) convertView.findViewById(R.id.task_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(projectPlan.getAq_lh_jcmc());
            viewHolder.state.setText(getStateText(projectPlan.getState_upload()));
            if (position == 0 && projectPlan.getState_upload() == 1 || projectPlan.getState_upload() == 2) {
                viewHolder.state.setText("正在上传");
                progress = viewHolder.button;
                if (task == null) {
                    task = projectPlan;
                    viewHolder.button.setText("0%");
                    LogUtil.logI("开始任务");
                    startUpload(projectPlan);
                }
            }
            if (!finished) {
                viewHolder.button.setText(getState(projectPlan.getState_upload()));
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int flag = flg;
                        if (((Button) v).getText().equals("上传")) {
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
                            return;
                        } else {
                            ((Button) v).setText("上传");
                            projectPlan.setState_upload(0);
                            if (flag != (projectPlans.size() - 1)) {
                                projectPlans.remove(flag);
                                projectPlans.add(projectPlan);
                            }
                            stopUpload();
                            task = null;
                            notifyDataSetChanged();
                        }
                    }

                });
            } else

            {
                viewHolder.state.setVisibility(View.GONE);
                viewHolder.button.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView title;
            TextView state;
            Button button;
        }
    }

    private void startUpload(ProjectPlan plan) {
        util = NotificationUtil.newInstance();

        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        intent.putExtra("plan", plan.getAq_lh_id());
        getContext().startService(intent);
        openBroadCast();
    }

    private void stopUpload() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        if (ServiceUtil.isServiceRunning(serviceName)) {
            getContext().stopService(intent);
        }
    }

    class LoadData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!finished) {
                projectPlans = DataUtil.getProjectByState("等待上传");
            } else {
                projectPlans = DataUtil.getProjectByState("上传完成");
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
        LogUtil.logI("开启广播");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int flag = intent.getExtras().getInt("precent");
                LogUtil.logI("百分比" + flag);
                util.Notify(count, flag);
                if (flag == -1) {
                    Toast.makeText(context, "上传失败,稍后重试！", Toast.LENGTH_SHORT).show();
                    ProjectPlan projectPlan = projectPlans.get(0);
                    projectPlan.setState_upload(3);
                    projectPlans.remove(0);
                    projectPlans.add(projectPlan);
                    task = null;
                    adapter.notifyDataSetChanged();
                    return;
                }
                progress.setText(flag + "%");
                if (flag == 100) {
                    if (projectPlans.size() > 0) {
                        ProjectPlan projectPlan = projectPlans.get(0);
                        projectPlan.setAq_jctz_zt("上传完成");
                        DataUtil.changeProjectState(projectPlan);
                        projectPlans.remove(0);
                        task = null;
                        LogUtil.logI("上传完成" + projectPlan.getAq_lh_jcmc());
                        adapter.notifyDataSetChanged();
                        util.Notify(++count, flag);
                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                        listener.onDateChange();
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
            return "上传";
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
            return "上传失败，请重试";
        } else {
            return "上传完成";
        }
    }

    public void setListenter(UpLoadFragment.OnDataChangeListener listener) {
        this.listener = listener;
    }
}
