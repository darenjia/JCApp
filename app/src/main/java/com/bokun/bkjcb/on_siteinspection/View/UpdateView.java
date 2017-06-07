package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Service.ServiceUtil;

/**
 * Created by DengShuai on 2017/6/7.
 */

public class UpdateView extends LinearLayout {
    private ProjectPlan plan;
    private Context context;
    private TextView title;
    private TextView state;
    private CheckBox box;
    private View mView;
    private LayoutInflater mInflater;
    private int taskState = 0;//0未开始，1等待,2正在进行，3停止，4完成
    private final String serviceName = "com.bokun.bkjcb.on_siteinspection.Service.UploadService";

    public UpdateView(Context context, ProjectPlan plan) {
        super(context, null);
    }

    public UpdateView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public UpdateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) getChildAt(0);
        state = (TextView) getChildAt(1);
        box = (CheckBox) getChildAt(2);
    }

    private void initView() {
        if (mView == null) {
            //初始化
            mInflater = LayoutInflater.from(getContext());
            //添加布局文件
            mView = mInflater.inflate(R.layout.upload_item_view, null);

            //绑定控件
            title = (TextView) mView.findViewById(R.id.task_title);
            state = (TextView) mView.findViewById(R.id.task_state);
            box = (CheckBox) mView.findViewById(R.id.task_btn);

            title.setText(plan.getAq_lh_jcmc());
            state.setText("等待上传");
            box.setChecked(false);
            box.setText("上传");
            setButtonCheck();
            //然后使用LayoutParams把控件添加到子view中
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public void setButtonCheck() {
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (taskState == 0) {
                        taskState = 1;
                        box.setText("等待");
                        if (!ServiceUtil.isServiceRunning(serviceName)) {
                            taskState = 2;
                            box.setText("0%");
                            statService();
                        }
                    }
                } else {
                    if (taskState == 2) {
                        cancleService();
                    }
                }
            }
        });
    }

    private void statService() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        intent.putExtra("plan", plan);
        getContext().startService(intent);
    }

    private void cancleService() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
        intent.setPackage(getContext().getPackageName());
        getContext().stopService(intent);
    }

    public void setButtonText(String text) {
        box.setText(text);
    }
}
