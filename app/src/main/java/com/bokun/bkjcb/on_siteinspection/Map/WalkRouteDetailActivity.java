package com.bokun.bkjcb.on_siteinspection.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.WalkPath;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

public class WalkRouteDetailActivity extends AppCompatActivity {
    private WalkPath mWalkPath;
    private TextView mTitleWalkRoute;
    private ListView mWalkSegmentList;
    private WalkSegmentListAdapter mWalkSegmentListAdapter;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        initToolBar();
        getIntentData();
        mTitleWalkRoute = (TextView) findViewById(R.id.firstline);
        String dur = AMapUtil.getFriendlyTime((int) mWalkPath.getDuration());
        String dis = AMapUtil
                .getFriendlyLength((int) mWalkPath.getDistance());
        mTitleWalkRoute.setText(dur + "(" + dis + ")");
        mWalkSegmentList = (ListView) findViewById(R.id.bus_segment_list);
        mWalkSegmentListAdapter = new WalkSegmentListAdapter(
                this.getApplicationContext(), mWalkPath.getSteps());
        mWalkSegmentList.setAdapter(mWalkSegmentListAdapter);

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mWalkPath = intent.getParcelableExtra("walk_path");
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_BusRouteAct);
        toolbar.setTitle("步行路线详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Utils.initSystemBar(this, toolbar);
    }
}
