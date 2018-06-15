package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Activity.ProgressDetailActivity;
import com.bokun.bkjcb.on_siteinspection.Adapter.ProgressAdapter;
import com.bokun.bkjcb.on_siteinspection.Domain.District;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectProgress;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LocalTools;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;

import org.angmarch.views.NiceSpinner;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DengShuai on 2018/4/3.
 * Description :
 */

public class PlanProgressFragment extends MainFragment implements RequestListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, View.OnClickListener {


    private ListView listView;
    private ArrayList<ProjectProgress> results;
    private ProgressAdapter adapter;
    private HttpManager manager;
    private TextView startTime;
    private TextView endTime;
    private Date startDate;
    private NiceSpinner spinner;
    private ArrayList<District> districts;
    private Button reset;
    private Button confirm;
    private LinearLayout conditionsView;
    private ImageView down;
    private ImageView up;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.progress_fragment, null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_progress);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRecycler));
        listView = (ListView) view.findViewById(R.id.list_progress);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        spinner = view.findViewById(R.id.nice_spinner);
        reset = view.findViewById(R.id.btn_reset);
        confirm = view.findViewById(R.id.btn_con);
        conditionsView = view.findViewById(R.id.condition);
        down = view.findViewById(R.id.condition_down);
        up = view.findViewById(R.id.condition_up);
        setListener();
        return view;
    }

    @Override
    protected void initData() {
        startDate = new Date(System.currentTimeMillis());
        getData("", "", "");
    }

    private void getDistrict() {
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("qu", JCApplication.user.getQuxian());
        requestVo.setMethodName("Getqujiedaolist");
        manager = new HttpManager(context, this, requestVo);
        manager.postRequest();
    }

    private void getData(String start, String end, String discrit) {
        refreshLayout.setRefreshing(true);
        HttpRequestVo requestVo = new HttpRequestVo();
        requestVo.getRequestDataMap().put("sys_userid", JCApplication.user.getUserID());
        requestVo.getRequestDataMap().put("riqiStart", start);
        requestVo.getRequestDataMap().put("riqiEnd", end);
        requestVo.getRequestDataMap().put("quyu1", JCApplication.user.getQuxian());
        requestVo.getRequestDataMap().put("quyu2", discrit);
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
        if ((results == null && districts == null) || (results != null && districts != null)) {
            results = JsonParser.getProgressData(object.resData);
            if (adapter != null) {
                adapter.refreshData(results);
                adapter.notifyDataSetChanged();
            } else {
                adapter = new ProgressAdapter(getContext(), results);
                listView.setAdapter(adapter);
            }
            if (districts == null) {
                getDistrict();
            }
        } else {
            districts = JsonParser.getDistrict(object.resData);
            districts.add(0, new District("全部", ""));
            List<String> dataSet = new ArrayList<>();
            for (int i = 0; i < districts.size(); i++) {
                dataSet.add(districts.get(i).getName());
            }
            spinner.attachDataSource(dataSet);
        }
    }

    @Override
    protected void getDataFailed() {
        refreshLayout.setRefreshing(false);
        districts = new ArrayList<>();
        districts.add(0, new District("", ""));
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setSelectedIndex(0);
                startTime.setText("起始时间");
                endTime.setText("截止时间");
                getData("", "", "");
                hideAnimate();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = startTime.getText().equals("起始时间") ? "" : startTime.getText().toString();
                String end = endTime.getText().equals("截止时间") ? "" : endTime.getText().toString();
                getData(start, end, districts.get(spinner.getSelectedIndex()).getVal());
                hideAnimate();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnimate();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAnimate();
            }
        });
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

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        DatePickDialog dialog = new DatePickDialog(getContext());
        dialog.setStartDate(startDate);
        //设置上下年分限制
        dialog.setYearLimt(3);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                String strDate = Utils.getDate("yyyy-MM-dd", date);
                if (id == R.id.start_time) {
                    startTime.setText(strDate);
                    startDate = date;
                } else {
                    endTime.setText(strDate);
                }
            }
        });

        dialog.show();
    }

    private void hideAnimate() {
        int height = LocalTools.dip2px(context, 200);
        Animation animation = new TranslateAnimation(0, 0, 0, -height);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                conditionsView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        conditionsView.startAnimation(animation);
    }

    private void showAnimate() {
        int height = LocalTools.dip2px(context, 200);
        conditionsView.setVisibility(View.VISIBLE);
        Animation animation = new TranslateAnimation(0, 0, -height, 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(500);
        conditionsView.startAnimation(animation);
    }

    public void switchCondition() {
        if (conditionsView.getVisibility() == View.VISIBLE) {
            hideAnimate();
        } else {
            showAnimate();
        }
    }
}
