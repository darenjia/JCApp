package com.bokun.bkjcb.on_siteinspection.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;


public class DriveRouteDetailActivity extends AppCompatActivity {
	private DrivePath mDrivePath;
	private DriveRouteResult mDriveRouteResult;
	private TextView mTitleDriveRoute, mDesDriveRoute;
	private ListView mDriveSegmentList;
	private DriveSegmentListAdapter mDriveSegmentListAdapter;
	private Toolbar toolbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		init();
	}

	private void init() {
		mTitleDriveRoute = (TextView) findViewById(R.id.firstline);
		mDesDriveRoute = (TextView) findViewById(R.id.secondline);
		String dur = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
		String dis = AMapUtil.getFriendlyLength((int) mDrivePath
				.getDistance());
		mTitleDriveRoute.setText(dur + "(" + dis + ")");
		int taxiCost = (int) mDriveRouteResult.getTaxiCost();
		mDesDriveRoute.setText("打车约"+taxiCost+"元");
		mDesDriveRoute.setVisibility(View.VISIBLE);
		configureListView();
		initToolBar();
	}

	private void configureListView() {
		mDriveSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mDriveSegmentListAdapter = new DriveSegmentListAdapter(
				this.getApplicationContext(), mDrivePath.getSteps());
		mDriveSegmentList.setAdapter(mDriveSegmentListAdapter);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mDrivePath = intent.getParcelableExtra("drive_path");
		mDriveRouteResult = intent.getParcelableExtra("drive_result");
	}

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar_BusRouteAct);
		toolbar.setTitle("驾车路线详情");
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
