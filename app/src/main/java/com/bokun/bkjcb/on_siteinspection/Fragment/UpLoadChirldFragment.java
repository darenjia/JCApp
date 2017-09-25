package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.Notification.NotificationUtil;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Service.ServiceUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.SPUtils;

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
    private TextView speed;
    private Button startAll;
    private BroadcastReceiver receiver;
    private LocalBroadcastManager manager;
    private LoadData loadTask;
    private NotificationUtil util;
    private int count = 0;
    private UpLoadFragment.OnDataChangeListener listener;
    private Intent intent;
    private GetURLSpeedThread speedThread;
    private boolean isStop;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String spd = "正在上传\t\t" + msg.obj;
                speed.setText(spd);
            }
        }
    };

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
                    DataUtil.deleteFinishedProjectPlan(JCApplication.user);
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
                    DataUtil.changeProjectState1(plan);
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
                speed = viewHolder.state;
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

    private void startUpload(final ProjectPlan plan) {
        //仅在wifi下上传
        boolean isUpload = (boolean) SPUtils.get(getContext(), "setting", true);
        if (NetworkUtils.isEnable(getContext())) {
            intent = new Intent();
            intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
            intent.setPackage(getContext().getPackageName());
            intent.putExtra("plan", plan.getAq_lh_id());
            if (!isUpload || NetworkUtils.getTypeName(getContext()) == Constants.NETWORK_WIFI) {
                util = NotificationUtil.newInstance();
                getContext().startService(intent);
                openBroadCast();
            } else {
                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("当前网络为移动网络，该操作会消耗较多数据流量，是否继续操作？")
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                util = NotificationUtil.newInstance();
                                getContext().startService(intent);
                                openBroadCast();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                tipDialog.setCanceledOnTouchOutside(false);
                tipDialog.show();
            }
        } else {
            Snackbar.make(startAll, "无网络连接，请检查网络", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    getContext().startActivity(intent);
                }
            }).show();
        }
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
                projectPlans = DataUtil.getProjectByState("等待上传", JCApplication.user);
            } else {
                projectPlans = DataUtil.getProjectByState("上传完成", JCApplication.user);
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
        speedThread = new GetURLSpeedThread();
        isStop = true;
        speedThread.start();
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
//                        speedThread.interrupt();
                        isStop = false;
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
            projectPlans = DataUtil.getProjectByState(state, JCApplication.user);
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

    class GetURLSpeedThread extends Thread {
        String spd;

        @Override
        public void run() {
            super.run();
            while (isStop) {
               /* if (this.isInterrupted()) {
                    break;
                }*/
                long pre = TrafficStats.getTotalTxBytes();
                try {
                    Thread.sleep(1000);
                    long speed = TrafficStats.getTotalTxBytes() - pre;
                    long sizeM = speed / (1000 * 1000);
                    long sizeK = speed / 1000;
                    if (sizeM > 0.1) {
                        spd = sizeM + "M/s";
                    } else if (sizeK > 0.1) {
                        spd = sizeK + "K/s";
                    } else {
                        spd = speed + "B/s";
                    }
                    LogUtil.logI(spd);
                    Message message = new Message();
                    message.obj = spd;
                    message.what = 0;
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
