package com.bokun.bkjcb.on_siteinspection.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.bokun.bkjcb.on_siteinspection.Map.AMapUtil;
import com.bokun.bkjcb.on_siteinspection.Map.BusResultListAdapter;
import com.bokun.bkjcb.on_siteinspection.Map.DriveRouteDetailActivity;
import com.bokun.bkjcb.on_siteinspection.Map.DrivingRouteOverLay;
import com.bokun.bkjcb.on_siteinspection.Map.WalkRouteDetailActivity;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LocalTools;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.ToastUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.eugene.fithealth.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealth.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealth.Utilities.InitiateSearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MapActivity extends AppCompatActivity implements LocationSource, PoiSearch.OnPoiSearchListener,
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener, RouteSearch.OnRouteSearchListener,
        View.OnClickListener {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener = null;//定位监听器
    private NetWorkChangeReciver reciver;
    private String cityCode;
    private myPoiOverlay poiOverlay;
    private Toolbar toolbar;
    //搜索框设置
    private View line_divider, toolbar_shadow;
    private RelativeLayout view_search, mPoiDetail;
    private LinearLayout view_gohere;
    private CardView card_search;
    private ImageView image_search_back, clearSearch;
    private AutoCompleteTextView edit_text_search;
    private ListView listView;
    //    private ListView listContainer;//搜索结果列表
    private LogQuickSearchAdapter logQuickSearchAdapter;//搜索历史适配器
    private Set<String> set;//判断有没有重复的搜索历史，重复的话不再保存
    private ArrayList<String> mItem;
    private ArrayList<PoiItem> poiItems;
    private ProgressBar marker_progress;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    //    private StringAdapter resultAdapter;
    private Button location;
    private ListPopupWindow listPopupWindow;
    private TextView mPoiName, mPoiAddress;
    private Marker mlastMarker, detailMarker;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;
    private LinearLayout mBusResultLayout;
    private RelativeLayout mBottomLayout, mRouteView;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ImageView mBus;
    private ImageView mDrive;
    private ImageView mWalk;
    private ListView mBusResultList;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private boolean isSearch = true;
    private TextView keywordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initToolbar();
        mapView = (MapView) findViewById(R.id.activity_mapview);
        mapView.onCreate(savedInstanceState);

        initView();
        initData();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_mapAct);
        toolbar.setTitle("地图");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Utils.initSystemBar(this, toolbar);
    }

    private void initData() {
        LatLonPoint point = getIntent().getExtras().getParcelable("point");
        initLocation();
        if (point == null) {
            LogUtil.logI("point为空");
            //启动定位
            mLocationClient.startLocation();
        } else {
            //将地图移动到定位点
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(AMapUtil.convertToLatLng(point)));
        }
    }

    private void initLocation() {
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        LogUtil.logI("MapActivity定位：" + aMapLocation.getErrorCode() + "经纬度：" + aMapLocation.getLatitude() + aMapLocation.getLongitude());
                        //可在其中解析amapLocation获取相应内容。
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                        //将地图移动到定位点
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(aMapLocation);
                        //获取定位信息
                        cityCode = aMapLocation.getCityCode();
                        mStartPoint = AMapUtil.convertToLatLonPoint(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                        if (isSearch) {
                            startSearch();
                            isSearch = false;
                        }
//                        StringBuffer buffer = new StringBuffer();
//                        buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getProvince() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
//                        ToastUtil.showShortToast(getApplicationContext(), buffer.toString());
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        if (Utils.gpsIsOpen(this) && NetworkUtils.isEnable(this)) {
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        } else if (NetworkUtils.isEnable(this)) {
            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        } else {
            //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            Toast.makeText(this, "当前网络不可用，无法定位，请打开网络后重试", Toast.LENGTH_SHORT).show();
            return;
        }
//        mLocationOption.setInterval(5000);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiScan(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(10000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    protected void initView() {
        initSearchView();
        aMap = mapView.getMap();
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
        aMap.setOnMapClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        MenuItem item = menu.findItem(R.id.search);
        keywordView = (TextView) item.getActionView().findViewById(R.id.map_keyword);
        keywordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            startSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(reciver);
        LogUtil.logI("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.logI("onDestory");
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        mapView.onDestroy();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        setBroadCastReciver();
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    private void setBroadCastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        reciver = new NetWorkChangeReciver();
        registerReceiver(reciver, filter);
    }

    public void refreshMap() {
        LogUtil.logI("刷新定位" + mLocationClient.isStarted());
        if (NetworkUtils.isEnable(this)) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stopLocation();
            }
            mLocationClient.startLocation();
        }
    }

    public void startSearch() {
        location.setVisibility(View.GONE);
        if (mBottomLayout.getVisibility() != View.GONE) {
            mBottomLayout.setVisibility(View.GONE);
        }
        IsAdapterEmpty();
        InitiateSearch.handleToolBar(this, card_search, view_search, listView, edit_text_search, line_divider);
    }

    private int[] markers = {R.drawable.poi_marker_1,
            R.drawable.poi_marker_2,
            R.drawable.poi_marker_3,
            R.drawable.poi_marker_4,
            R.drawable.poi_marker_5,
            R.drawable.poi_marker_6,
            R.drawable.poi_marker_7,
            R.drawable.poi_marker_8,
            R.drawable.poi_marker_9,
            R.drawable.poi_marker_10
    };

    private void whetherToShowDetailInfo(boolean isToShow) {
        if (isToShow) {
            mPoiDetail.setVisibility(View.VISIBLE);

        } else {
            mPoiDetail.setVisibility(View.GONE);

        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView textView = new TextView(this);
        textView.setText(marker.getTitle());
        return textView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LogUtil.logI("MarkerInfoContent" + marker.getSnippet());
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getObject() != null) {
            whetherToShowDetailInfo(true);
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                location.setText(mCurrentPoi.getTitle());
                if (mlastMarker == null) {
                    mlastMarker = marker;
                } else {
                    // 将之前被点击的marker置为原来的状态
                    resetlastmarker();
                    mlastMarker = marker;
                }
                detailMarker = marker;
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.poi_marker_pressed)));
                setPoiItemDisplayContent(mCurrentPoi);
                mEndPoint = mCurrentPoi.getLatLonPoint();
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            whetherToShowDetailInfo(false);
            resetlastmarker();
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        whetherToShowDetailInfo(false);
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        mBottomLayout.setVisibility(View.GONE);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(this, mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(this, "未查询到结果");
                }
            } else {
                ToastUtil.show(this, "未查询到结果");
            }
        } else {
            ToastUtil.show(this, "未知错误");
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约" + taxiCost + "元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MapActivity.this,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(this, "对不起，没有搜索到相关数据！");
                }

            } else {
                ToastUtil.show(this, "对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtil.show(this, "对不起，没有搜索到相关数据！");
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MapActivity.this,
                                    WalkRouteDetailActivity.class);
                            intent.putExtra("walk_path", walkPath);
                            intent.putExtra("walk_result",
                                    mWalkRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(this, "对不起，没有搜索到相关数据！");
                }

            } else {
                ToastUtil.show(this, "对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtil.show(this, "对不起，没有搜索到相关数据！");
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onClick(View v) {
        LogUtil.logI(v.getId() + "v");
        switch (v.getId()) {
            case R.id.route_bus:
                searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BUS_DEFAULT);
                mDrive.setImageResource(R.drawable.route_drive_normal);
                mBus.setImageResource(R.drawable.route_bus_select);
                mWalk.setImageResource(R.drawable.route_walk_normal);
                mapView.setVisibility(View.GONE);
                mBusResultLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.route_drive:
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DRIVING_SINGLE_DEFAULT);
                mDrive.setImageResource(R.drawable.route_drive_select);
                mBus.setImageResource(R.drawable.route_bus_normal);
                mWalk.setImageResource(R.drawable.route_walk_normal);
                mapView.setVisibility(View.VISIBLE);
                mBusResultLayout.setVisibility(View.GONE);
                break;
            case R.id.route_walk:
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.BUS_DEFAULT);
                mDrive.setImageResource(R.drawable.route_drive_normal);
                mBus.setImageResource(R.drawable.route_bus_normal);
                mWalk.setImageResource(R.drawable.route_walk_select);
                mapView.setVisibility(View.VISIBLE);
                mBusResultLayout.setVisibility(View.GONE);
                break;
            /*case R.id.route_CrosstownBus:
                searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BUS_DEFAULT);
                mDrive.setImageResource(R.drawable.route_drive_normal);
                mBus.setImageResource(R.drawable.route_bus_normal);
                mWalk.setImageResource(R.drawable.route_walk_normal);
                mapView.setVisibility(View.GONE);
                mBusResultLayout.setVisibility(View.VISIBLE);
                break;*/
        }
    }

    class NetWorkChangeReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshMap();
        }
    }

    private void initSearchView() {
        view_search = (RelativeLayout) findViewById(R.id.view_search);
        line_divider = findViewById(R.id.line_divider);
        toolbar_shadow = findViewById(R.id.toolbar_shadow);
        edit_text_search = (AutoCompleteTextView) findViewById(R.id.edit_text_search);
        card_search = (CardView) findViewById(R.id.card_search);
        image_search_back = (ImageView) findViewById(R.id.image_search_back);
        clearSearch = (ImageView) findViewById(R.id.clearSearch);
        listView = (ListView) findViewById(R.id.listView);
//        listContainer = (ListView) findViewById(R.id.listContainer);
        marker_progress = (ProgressBar) findViewById(R.id.marker_progress);
        location = (Button) findViewById(R.id.location);
        mPoiDetail = (RelativeLayout) findViewById(R.id.poi_detail);
        mPoiName = (TextView) findViewById(R.id.poi_name);
        mPoiAddress = (TextView) findViewById(R.id.poi_address);
        view_gohere = (LinearLayout) findViewById(R.id.poi_go);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mDrive = (ImageView) findViewById(R.id.route_drive);
        mBus = (ImageView) findViewById(R.id.route_bus);
        mWalk = (ImageView) findViewById(R.id.route_walk);
        mBusResultList = (ListView) findViewById(R.id.bus_result_list);
        mRouteView = (RelativeLayout) findViewById(R.id.routemap_header);
        marker_progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"),//Pink color
                android.graphics.PorterDuff.Mode.MULTIPLY);
        set = new HashSet<>();
        SetTypeFace();
        logQuickSearchAdapter = new LogQuickSearchAdapter(this, 0, LogQuickSearch.all());
        mItem = new ArrayList<>();
        listView.setAdapter(logQuickSearchAdapter);
//        resultAdapter = new StringAdapter(mItem);
//        listContainer.setAdapter(resultAdapter);
        listPopupWindow = new ListPopupWindow(this);
//        listPopupWindow.setAdapter(resultAdapter);
        listPopupWindow.setAnchorView(location);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setModal(true);
        InitiateSearch();
        HandleSearch();
        IsAdapterEmpty();
    }

    private void InitiateSearch() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogQuickSearch logQuickSearch = logQuickSearchAdapter.getItem(position);
                edit_text_search.setText(logQuickSearch.getName());
                listView.setVisibility(View.GONE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                toolbar_shadow.setVisibility(View.GONE);
                search(logQuickSearch.getName(), 0);
            }
        });
        edit_text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = edit_text_search.getText().toString();
                if (newText.length() == 0) {
                    logQuickSearchAdapter = new LogQuickSearchAdapter(MapActivity.this, 0, LogQuickSearch.all());
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setImageResource(com.eugene.fithealth.R.mipmap.ic_keyboard_voice);
                    IsAdapterEmpty();
                } else {
                    /*logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.FilterByName(edit_text_search.getText().toString()));
                    listView.setAdapter(logQuickSearchAdapter);*/
                    clearSearch.setImageResource(com.eugene.fithealth.R.mipmap.ic_close);
                    IsAdapterEmpty();
                    InputtipsQuery inputquery = new InputtipsQuery(newText, cityCode);
                    Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
                    inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                        @Override
                        public void onGetInputtips(List<Tip> list, int rCode) {
                            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                                List<String> listString = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    listString.add(list.get(i).getName());
                                }
                                ArrayAdapter<String> aAdapter = new ArrayAdapter<>(
                                        MapActivity.this,
                                        R.layout.expandable_child_item_view, listString);
                                edit_text_search.setAdapter(aAdapter);
                            } else {
                            }
                        }
                    });
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_text_search.getText().toString().length() == 0) {

                } else {
                    edit_text_search.setText("");
                    listView.setVisibility(View.VISIBLE);
                    clearItems();
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    IsAdapterEmpty();
                }
            }
        });
        /*listContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (location.getVisibility() != View.VISIBLE) {
                    location.setVisibility(View.VISIBLE);
                }
                InitiateSearch.handleToolBar(MapActivity.this, card_search, view_search, listView, edit_text_search, line_divider);
                listContainer.setVisibility(View.GONE);
                moveToTarget(position);
            }
        });
       location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToTarget(position);
                listPopupWindow.dismiss();
            }
        });*/
        view_gohere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRouteView.setVisibility(View.VISIBLE);
                location.setVisibility(View.GONE);
                whetherToShowDetailInfo(false);
                setfromandtoMarker();
            }
        });
        mWalk.setOnClickListener(this);
        mDrive.setOnClickListener(this);
        mBus.setOnClickListener(this);
    }

    private void moveToTarget(int position) {
        location.setText(mItem.get(position));
        PoiItem poiItem = poiItems.get(position);
        LatLonPoint latLonPoint = poiItem.getLatLonPoint();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude())));
    }


    private void UpdateQuickSearch(String item) {
        for (int i = 0; i < logQuickSearchAdapter.getCount(); i++) {
            LogQuickSearch ls = logQuickSearchAdapter.getItem(i);
            String name = ls.getName();
            set.add(name.toUpperCase());
        }
        if (set.add(item.toUpperCase())) {
            LogQuickSearch recentLog = new LogQuickSearch();
            recentLog.setName(item);
            recentLog.setDate(new Date());
            recentLog.save();
            logQuickSearchAdapter.addLog(recentLog);
            logQuickSearchAdapter.notifyDataSetChanged();
        }
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiateSearch.handleToolBar(MapActivity.this, card_search, view_search, listView, edit_text_search, line_divider);
//                listContainer.setVisibility(View.GONE);
                toolbar_shadow.setVisibility(View.VISIBLE);
                clearItems();
            }
        });
        edit_text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LogUtil.logI("action:" + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edit_text_search.getText().toString().trim().length() > 0) {
                        clearItems();
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                        UpdateQuickSearch(edit_text_search.getText().toString());
                        listView.setVisibility(View.GONE);
                        search(edit_text_search.getText().toString(), 0);
                        toolbar_shadow.setVisibility(View.GONE);
                    }
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_NONE) {

                }
                return false;
            }
        });

    }

    private void IsAdapterEmpty() {
        if (logQuickSearchAdapter.getCount() == 0) {
            line_divider.setVisibility(View.GONE);
        } else {
            line_divider.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置字体
     **/
    private void SetTypeFace() {
        Typeface roboto_regular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        edit_text_search.setTypeface(roboto_regular);
    }

    /**
     * 情况搜索历史
     */

    private void clearItems() {
//        listContainer.setVisibility(View.GONE);
        mItem.clear();
//        resultAdapter.notifyDataSetChanged();
    }

    /**
     * Handle  Search
     */
    private void search(String item, int page_num) {
        marker_progress.setVisibility(View.VISIBLE);
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(item, "", cityCode);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);
        keywordView.setText(item);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 搜索成功的回调
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        LogUtil.logI("mItem" + mItem);
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiItem poiItem = poiItems.get(i);
                            mItem.add(poiItem.getTitle());
                        }
                        //清除POI信息显示
                        whetherToShowDetailInfo(false);
                        //并还原点击marker样式
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //清理之前搜索结果的marker
                        if (poiOverlay != null) {
                            poiOverlay.removeFromMap();
                        }
                        aMap.clear();// 清理之前的图标
                        poiOverlay = new myPoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
                    } else {

                    }
                }
            } else {
            }
            marker_progress.setVisibility(View.GONE);
//            resultAdapter.notifyDataSetChanged();
            if (mItem.size() > 0) {
                toolbar_shadow.setVisibility(View.GONE);
                InitiateSearch.handleToolBar(MapActivity.this, card_search, view_search, listView, edit_text_search, line_divider);
              /*  listContainer.setVisibility(View.VISIBLE);
                listContainer.setVerticalScrollbarPosition(0);
                listContainer.setSelection(0);*/
            } else {
                toolbar_shadow.setVisibility(View.VISIBLE);
//                listContainer.setVisibility(View.GONE);
            }
        } else {
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        } else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
        }
        mlastMarker = null;

    }

    /**
     * 设置底部描述
     */
    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        mPoiName.setText(mCurrentPoi.getTitle());
        mPoiAddress.setText(mCurrentPoi.getSnippet());
    }

    /**
     * listpopWindow适配器
     */
    class StringAdapter extends BaseAdapter {
        List<String> list;

        public StringAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(MapActivity.this, R.layout.expandable_child_item_view, null);
            textView.setText(list.get(position));
            return textView;
        }
    }

    /**
     * 自定义PoiOverlay，显示带数字的图标
     */
    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public myPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         *
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         *
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         *
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            } else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight));
                return icon;
            }
        }


    }

    private void setfromandtoMarker() {
        aMap.clear();
        aMap.addMarker(new MarkerOptions()
                .position(LocalTools.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.startpoint)));
        aMap.addMarker(new MarkerOptions()
                .position(LocalTools.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.endpoint)));
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(this, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(this, "终点未设置");
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    cityCode, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        } else if (routeType == ROUTE_TYPE_CROSSTOWN) {
            RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(
                    mStartPoint, mEndPoint);
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo_bus, mode,
                    "呼和浩特市", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            query.setCityd("农安县");
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }

    public static void ComeToMapActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}

