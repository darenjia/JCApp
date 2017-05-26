package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bokun.bkjcb.on_siteinspection.Domain.ManagerInfo;
import com.bokun.bkjcb.on_siteinspection.Fragment.CheckPlanFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.MapFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.TestFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.UpLoadFragment;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.AppManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
* MianActivity
* */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ConstraintLayout contentView;
    private HashMap<String, Fragment> viewMap;
    private FragmentManager manager;
    private Toolbar toolbar;
    private Menu menu;
    private Fragment testFragment;
    private Fragment lastFragment;
    private CheckPlanFragment checkPlanFragment;
    private MapFragment mapFragment;
    private UpLoadFragment upLoadFragment;
    public static String quxian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewMap = new HashMap<>();
        manager = getSupportFragmentManager();
    }

    @Override
    protected void findView() {
        contentView = (ConstraintLayout) findViewById(R.id.content_main);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {
//        checkPlanFragment = new CheckPlanFragment();
//        mapFragment = MapFragment.newInstance();
//        upLoadFragment = new UpLoadFragment();
//        testFragment = new TestFragment();
        ManagerInfo managerInfo = JsonParser.getUserInfo(getIntent().getStringExtra("quxian"));
        quxian = managerInfo.quxian;
        loadView("检查计划", "first");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        menu.setGroupVisible(R.id.menu_map, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh_map) {
            MapFragment.newInstance().refreshMap();
            return true;
        } else if (id == R.id.action_search_map) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("point", MapFragment.newInstance().mStartPoint);
            MapActivity.ComeToMapActivity(this, bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_check_plan) {
            loadView("检查计划", "first");
        } else if (id == R.id.nav_info_check) {
            loadView("工程信息查询", "second");
        } else if (id == R.id.nav_map) {
            loadView("地图", "third");
        } /*else if (id == R.id.nav_details) {
            loadView("工程信息详情", "forth");
        } */ else if (id == R.id.nav_update_result) {
            loadView("上传进度", "forth");
        } else if (id == R.id.nav_update_result) {
            LoginActivity.comeToLoginActivity(this);
        } else if (id == R.id.nav_exit) {
            AppManager.getAppManager().finishAllActivity();
//            AppManager.getAppManager().AppExit(this);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadView(String title, String key) {
        toolbar.setTitle(title);
        if (menu != null) {
            menu.setGroupVisible(R.id.menu_map, false);
        }
        Fragment currentFragment = viewMap.get(key);
        hideAllFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment == null) {
            if (key.equals("first")) {
                currentFragment = new CheckPlanFragment();
            } else if (key.equals("third")) {
                currentFragment = MapFragment.newInstance();
                menu.setGroupVisible(R.id.menu_map, true);
            } else if (key.equals("forth")) {
                currentFragment = new UpLoadFragment();
            } else {
                currentFragment = new TestFragment();
            }
            transaction.add(contentView.getId(), currentFragment);
            viewMap.put(key, currentFragment);
        } else {
            if (key.equals("third")) {
                menu.setGroupVisible(R.id.menu_map, true);
            }
            transaction.show(currentFragment);
        }
       /* FragmentTransaction transaction = manager.beginTransaction();
        if (lastFragment == null) {
            transaction.add(contentView.getId(), checkPlanFragment);
            lastFragment = checkPlanFragment;
        } else {
            if (key.equals("first")) {
                transaction.replace(contentView.getId(), checkPlanFragment);
            } else if (key.equals("third")) {
                transaction.replace(contentView.getId(), mapFragment);
            } else if (key.equals("forth")) {
                transaction.replace(contentView.getId(), upLoadFragment);
            } else {
                transaction.replace(contentView.getId(), testFragment);
            }
        }*/
        transaction.commit();
    }

    private void hideAllFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (viewMap.size() != 0) {
            Iterator iter = viewMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Fragment fragment = (Fragment) entry.getValue();
                transaction.hide(fragment);
            }
            transaction.commit();
        }
    }

    public static void ComeToMainActivity(Activity activity, String string) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("quxian", string);
        activity.startActivity(intent);
        activity.finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showMenu(boolean is) {
        menu.setGroupVisible(R.id.menu_map, is);
    }
}
