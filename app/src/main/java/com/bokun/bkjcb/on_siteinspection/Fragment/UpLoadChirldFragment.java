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
import android.widget.ListView;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/4/17.
 */

public class UpLoadChirldFragment extends BaseFragment {

    private ListView listView;
    private ListAdapter adapter;
    private ArrayList<CheckPlan> checkPlans;
    private boolean finished;
    private CheckBox progress;
    private BroadcastReceiver receiver;
    private LocalBroadcastManager manager;
    private LoadData loadTask;

    @Override
    public View initView() {
        View view = View.inflate(getContext(), R.layout.unfinished_upload_view, null);
        listView = (ListView) view.findViewById(R.id.upload_listview);
        return view;
    }

    @Override
    public void initData() {

    }

    class ListAdapter extends BaseAdapter {
        ListAdapter.ViewHolder viewHolder;

        @Override
        public int getCount() {
            return checkPlans.size();
        }

        @Override
        public Object getItem(int position) {
            return checkPlans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CheckPlan checkPlan = checkPlans.get(position);
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
            viewHolder.title.setText(checkPlan.getName());
            if (!finished) {
                viewHolder.button.setText(getState(checkPlan.getState_upload()));
                viewHolder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int flag = position;
                        if (isChecked) {
                            checkPlan.setState_upload(2);
                            while (flag != 0 && checkPlans.get(flag - 1).getState_upload() == 0) {
                                checkPlans.remove(flag);
                                LogUtil.logI("改变计划状态" + checkPlan.getName());
                                checkPlans.add(flag - 1, checkPlan);
                                flag--;
                            }
                            notifyDataSetChanged();
                            if (position == 0) {
                                startUpload(checkPlan);
                                progress = viewHolder.button;
                                viewHolder.state.setText("正在上传");
                            } else {
                                buttonView.setText("等待");
                            }
                        } else {
                            if (!buttonView.getText().equals("上传")) {
                                buttonView.setText("继续");
                            }
                        }
                    }
                });
                if (checkPlan.getState_upload() == 2) {
                    LogUtil.logI("设置计划状态" + checkPlan.getName());
                    viewHolder.button.setChecked(true);
                } else {
                    viewHolder.button.setChecked(false);
                }
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

    private void startUpload(CheckPlan checkPlan) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        intent.putExtra("checkplan", checkPlan);
        getContext().startService(intent);
        openBroadCast();
    }

    class LoadData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!finished) {
                checkPlans = DataUtil.queryCheckPlanCanUpLoad(getContext());
            } else {
                checkPlans = DataUtil.queryCheckPlanFinished(getContext());
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
        if (checkPlans == null) {
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
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int flag = intent.getExtras().getInt("precent");
                progress.setText(flag + "%");
                if (flag == 100) {
                    CheckPlan checkPlan = checkPlans.get(0);
                    checkPlan.setState(3);
                    DataUtil.updateCheckPlanState(getContext(), checkPlan);
                    checkPlans.remove(0);
                    LogUtil.logI(checkPlans.size() + "");
                    adapter.notifyDataSetChanged();
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

    private String getState(int state) {
        if (state == 0) {
            return "上传";
        } else if (state == 1) {
            return "等待";
        } else if (state == 2) {
            return "0%";
        } else if (state == 3) {
            return "继续";
        } else {
            return "完成";
        }
    }
}
