package com.bokun.bkjcb.on_siteinspection.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

public class BusRouteDetailActivity extends AppCompatActivity implements OnMapLoadedListener,
        OnMapClickListener, InfoWindowAdapter, OnInfoWindowClickListener, OnMarkerClickListener {
    private AMap aMap;
    private MapView mapView;
    private BusPath mBuspath;
    private BusRouteResult mBusRouteResult;
    private TextView mTitleBusRoute, mDesBusRoute;
    private ListView mBusSegmentList;
    private BusSegmentListAdapter mBusSegmentListAdapter;
    private LinearLayout mBuspathview;
    private BusRouteOverlay mBusrouteOverlay;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        getIntentData();
        init();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBuspath = intent.getParcelableExtra("bus_path");
            mBusRouteResult = intent.getParcelableExtra("bus_result");
        }
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mTitleBusRoute = (TextView) findViewById(R.id.firstline);
        mDesBusRoute = (TextView) findViewById(R.id.secondline);
        String dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mBuspath.getDistance());
        mTitleBusRoute.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mBusRouteResult.getTaxiCost();
        mDesBusRoute.setText("打车约" + taxiCost + "元");
        mDesBusRoute.setVisibility(View.VISIBLE);
        mBuspathview = (LinearLayout) findViewById(R.id.bus_path);
        configureListView();
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_BusRouteAct);
        toolbar.setTitle("公交路线详情");
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

    private void registerListener() {
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    private void configureListView() {
        mBusSegmentList = (ListView) findViewById(R.id.bus_segment_list);
        mBusSegmentListAdapter = new BusSegmentListAdapter(
                this.getApplicationContext(), mBuspath.getSteps());
        mBusSegmentList.setAdapter(mBusSegmentListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_route_map) {
            if (mapView.getVisibility() != View.GONE) {
                mBuspathview.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
                return true;
            }
            onMapClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMapClick() {
        mBuspathview.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        aMap.clear();// 清理地图上的所有覆盖物
        mBusrouteOverlay = new BusRouteOverlay(this, aMap,
                mBuspath, mBusRouteResult.getStartPos(),
                mBusRouteResult.getTargetPos());
        mBusrouteOverlay.removeFromMap();

    }

    @Override
    public void onMapLoaded() {
        if (mBusrouteOverlay != null) {
            mBusrouteOverlay.addToMap();
            mBusrouteOverlay.zoomToSpan();
        }
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }


}
