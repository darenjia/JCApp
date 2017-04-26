package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.eugene.fithealth.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealth.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealth.SearchListView.Item;
import com.eugene.fithealth.SearchListView.SearchAdapter;
import com.eugene.fithealth.Utilities.InitiateSearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DengShuai on 2017/4/24.
 */

public class MapFragment extends MainFragment implements LocationSource {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private MapView mapView;
    private com.amap.api.maps.AMap aMap;
    private static MapFragment fragment;
    private OnLocationChangedListener mListener = null;//定位监听器
    private NetWorkChangeReciver reciver;
    private String cityCode;

    public static MapFragment newInstance() {
        if (fragment == null) {
            synchronized (MapFragment.class) {
                if (fragment == null) {
                    fragment = new MapFragment();

                }
            }
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // 调用 onCreate方法 对 MapView LayoutParams 设置
        mapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    protected void initData() {
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        /*myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.vector_drawable_location));
        aMap.setMyLocationStyle(myLocationStyle);*/

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
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
                        LogUtil.logI("定位结果码：" + aMapLocation.getErrorCode() + "经纬度：" + aMapLocation.getLatitude() + aMapLocation.getLongitude());
                        //可在其中解析amapLocation获取相应内容。
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                        //将地图移动到定位点
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(aMapLocation);
                        //添加图钉
//                            aMap.addMarker(getMarkerOptions(aMapLocation));
                        //获取定位信息
                        cityCode = aMapLocation.getCityCode();
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getProvince() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                        Toast.makeText(getContext().getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//                        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
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
        mLocationClient = new AMapLocationClient(getContext().getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        if (Utils.gpsIsOpen(getContext()) && NetworkUtils.isEnable(getContext())) {
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        } else if (NetworkUtils.isEnable(getContext())) {
            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        } else {
            //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            Toast.makeText(getContext(), "当前网络不可用，无法定位，请打开网络后重试", Toast.LENGTH_SHORT).show();
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
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.map_fragment_view, null);
        mapView = (MapView) view.findViewById(R.id.fragment_mapview);
        initSearchView(view);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(reciver);
        if (mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.vector_drawable_location));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        //options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
        options.period(60);
        return options;
    }

    private void setBroadCastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        reciver = new NetWorkChangeReciver();
        getContext().registerReceiver(reciver, filter);
    }

    public void refreshMap() {
        LogUtil.logI("刷新定位" + mLocationClient.isStarted());
        if (NetworkUtils.isEnable(getContext())) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stopLocation();
            }
            mLocationClient.startLocation();
        }

//        initData();
    }

    class NetWorkChangeReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshMap();
        }
    }

    public void setSearchView(String keyWord) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", cityCode);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(getContext(), query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    //搜索框设置
    private InitiateSearch initiateSearch;
    private View line_divider, toolbar_shadow;
    private RelativeLayout view_search;
    private CardView card_search;
    private ImageView image_search_back, clearSearch;
    private EditText edit_text_search;
    private ListView listView, listContainer;
    private LogQuickSearchAdapter logQuickSearchAdapter;
    private Set<String> set;
    private ArrayList<Item> mItem;
    private SearchAdapter searchAdapter;
    private ProgressBar marker_progress;
    private AsyncTask<String, String, String> mAsyncTask;
    private ImageButton imageButton;

    private void initSearchView(View view) {
        initiateSearch = new InitiateSearch();
        imageButton = (ImageButton) view.findViewById(R.id.search_button);
        view_search = (RelativeLayout) view.findViewById(R.id.view_search);
        line_divider = view.findViewById(R.id.line_divider);
        toolbar_shadow = view.findViewById(R.id.toolbar_shadow);
        edit_text_search = (EditText) view.findViewById(R.id.edit_text_search);
        card_search = (CardView) view.findViewById(R.id.card_search);
        image_search_back = (ImageView) view.findViewById(R.id.image_search_back);
        clearSearch = (ImageView) view.findViewById(R.id.clearSearch);
        listView = (ListView) view.findViewById(R.id.listView);
        listContainer = (ListView) view.findViewById(R.id.listContainer);
        marker_progress = (ProgressBar) view.findViewById(R.id.marker_progress);
        marker_progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"),//Pink color
                android.graphics.PorterDuff.Mode.MULTIPLY);
        logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.all());
        mItem = new ArrayList<>();
        searchAdapter = new SearchAdapter(context, mItem);
        listView.setAdapter(logQuickSearchAdapter);
        listContainer.setAdapter(searchAdapter);
        set = new HashSet<>();
        SetTypeFace();
        InitiateToolbarTabs();
        InitiateSearch();
        HandleSearch();
        IsAdapterEmpty();
    }

    private void InitiateToolbarTabs() {
    }

    private void InitiateSearch() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsAdapterEmpty();
                initiateSearch.handleToolBar(context, card_search, view_search, listView, edit_text_search, line_divider);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogQuickSearch logQuickSearch = logQuickSearchAdapter.getItem(position);
                edit_text_search.setText(logQuickSearch.getName());
                listView.setVisibility(View.GONE);
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
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
                if (edit_text_search.getText().toString().length() == 0) {
                    logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.all());
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setImageResource(com.eugene.fithealth.R.mipmap.ic_keyboard_voice);
                    IsAdapterEmpty();
                } else {
                    logQuickSearchAdapter = new LogQuickSearchAdapter(context, 0, LogQuickSearch.FilterByName(edit_text_search.getText().toString()));
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setImageResource(com.eugene.fithealth.R.mipmap.ic_close);
                    IsAdapterEmpty();
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
                    mAsyncTask.cancel(true);
                    edit_text_search.setText("");
                    listView.setVisibility(View.VISIBLE);
                    clearItems();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    IsAdapterEmpty();
                }
            }
        });
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
                initiateSearch.handleToolBar(context, card_search, view_search, listView, edit_text_search, line_divider);
                listContainer.setVisibility(View.GONE);
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
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
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

    private void SetTypeFace() {
        Typeface roboto_regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        edit_text_search.setTypeface(roboto_regular);
    }

    /**
     * Handle FatSecret Search
     */

    private void clearItems() {
        listContainer.setVisibility(View.GONE);
        mItem.clear();
        searchAdapter.notifyDataSetChanged();
    }


    private void search(final String item, final int page_num) {
        mAsyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                marker_progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... arg0) {

                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                marker_progress.setVisibility(View.GONE);
                searchAdapter.notifyDataSetChanged();
                if (mItem.size() > 0) {
                    toolbar_shadow.setVisibility(View.GONE);
                    TranslateAnimation slide = new TranslateAnimation(0, 0, listContainer.getHeight(), 0);
                    slide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            listContainer.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    slide.setDuration(300);
                    listContainer.startAnimation(slide);
                    listContainer.setVerticalScrollbarPosition(0);
                    listContainer.setSelection(0);
                } else {
                    toolbar_shadow.setVisibility(View.VISIBLE);
                    listContainer.setVisibility(View.GONE);
                }

            }

            @Override
            protected void onCancelled() {
                marker_progress.setVisibility(View.GONE);
            }

        };
        mAsyncTask.execute();
    }
}
