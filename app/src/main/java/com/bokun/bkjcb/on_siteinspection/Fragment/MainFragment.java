package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;

/**
 * Created by DengShuai on 2017/4/7.
 */

public class MainFragment extends Fragment {

    public ConstraintLayout contentView;
    public SwipeRefreshLayout refreshLayout;
    public Context context;
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RequestListener.EVENT_NOT_NETWORD:
                    Snackbar.make(contentView, "无网络连接，请检查网络", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                    break;
                case RequestListener.EVENT_CLOSE_SOCKET:
                    Snackbar.make(contentView, "网络错误！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_NETWORD_EEEOR:
                    Snackbar.make(contentView, "请确认网络是否可用！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_EEEOR:
                    Snackbar.make(contentView, "服务器错误，请稍后再试！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_SUCCESS:
                    JsonResult result = (JsonResult) msg.obj;
                    if (result.success) {
                        getDataSucceed(result);
                    } else {
                        Snackbar.make(contentView, "服务器错误，请稍后再试！", Snackbar.LENGTH_LONG).show();
                    }
                    break;
            }
            if (msg.obj == null) {
                getDataFailed();
            }
            if (refreshLayout != null) {
                refreshLayout.setRefreshing(false);
            }
            return true;
        }
    });

    public void showView() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = (ConstraintLayout) container.findViewById(R.id.content_main);
        context = getContext();
        View view = initView(inflater);
        initData();
        return view;
    }

    protected void initData() {
    }

    protected View initView(LayoutInflater inflater) {
        return null;
    }

    protected void getDataSucceed(JsonResult object) {

    }

    protected void getDataFailed() {

    }
}
