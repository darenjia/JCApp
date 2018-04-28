package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bokun.bkjcb.on_siteinspection.Activity.ProgressDetailActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.ProgressAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectProgress;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2018/4/3.
 * Description :
 */

public class PlanProgressFragment extends MainFragment implements RequestListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {


    private ListView listView;
    private ArrayList<ProjectProgress> results;
    private ProgressAdapter adapter;
    private HttpManager manager;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.progress_fragment, null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_progress);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRecycler));
        listView = (ListView) view.findViewById(R.id.list_progress);
        setListener();
        return view;
    }

    @Override
    protected void initData() {
        refreshLayout.setRefreshing(true);
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("sys_userid", JCApplication.user.getUserID());
        requestVo.setMethodName("GetAqJcJH");
        if (manager != null) {
            manager.cancelHttpRequest();
        }
        manager = new HttpManager(context, this, requestVo);
        manager.postRequest();
    }

    @Override
    protected void getDataSucceed(JsonResult object) {
        refreshLayout.setRefreshing(false);
        results = JsonParser.getProgressData(object.resData);
        if (adapter != null) {
            adapter.refreshData(results);
        } else {
            adapter = new ProgressAdapter(getContext(), results);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void getDataFailed() {
        refreshLayout.setRefreshing(false);

    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    @Override
    public void action(int i, Object object) {
        JsonResult result = JsonParser.parseSoap((SoapObject) object);
        Message message = new Message();
        message.what = i;
        message.obj = result;
        mHandler.sendMessage(message);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProgressDetailActivity.comeToProgressDetailAcicity(getContext(), results.get(position).getAq_lh_seqid(), results.get(position).getAq_lh_jcmc());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        boolean enable = false;
        if (listView != null && listView.getChildCount() > 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        refreshLayout.setEnabled(enable);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
