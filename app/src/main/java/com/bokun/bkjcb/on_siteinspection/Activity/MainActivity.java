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

import com.bokun.bkjcb.on_siteinspection.Fragment.CheckPlanFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.MapFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.TestFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.UpLoadFragment;
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
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewMap = new HashMap<>();
        manager = getSupportFragmentManager();
    }

    @Override
    protected void findView() {
        contentView = (ConstraintLayout) findViewById(R.id.content_main);
        loadView("检查计划", "first");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.action_refresh_map);
//        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh_map) {
            MapFragment.newInstance().refreshMap();
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
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadView(String title, String key) {
        toolbar.setTitle(title);
        if (menuItem != null) {
            menuItem.setVisible(false);
        }
        Fragment currentFragment = viewMap.get(key);
        hideAllFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment == null) {
            if (key.equals("first")) {
                currentFragment = new CheckPlanFragment();
            } else if (key.equals("third")) {
                currentFragment = MapFragment.newInstance();
                menuItem.setVisible(true);
            } else if (key.equals("forth")) {
                currentFragment = new UpLoadFragment();
            } else {
                currentFragment = new TestFragment();
            }
            transaction.add(contentView.getId(), currentFragment);
            viewMap.put(key, currentFragment);
        } else {
            transaction.show(currentFragment);
        }
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

    public static void ComeToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showLocationButton(boolean is) {
        menuItem.setVisible(is);
    }
}
