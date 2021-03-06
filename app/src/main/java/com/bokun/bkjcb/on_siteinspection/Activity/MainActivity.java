package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.bokun.bkjcb.on_siteinspection.Fragment.CheckPlanFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.MapFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.PlanProgressFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.SearchFragment;
import com.bokun.bkjcb.on_siteinspection.Fragment.UpLoadFragment;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.JCApplication;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.Service.ServiceUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.AppManager;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
* MianActivity
* */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, UpLoadFragment.OnDataChangeListener {

    private DrawerLayout drawerLayout;
    private ConstraintLayout contentView;
    private HashMap<String, Fragment> viewMap;
    private FragmentManager manager;
    private Toolbar toolbar;
    private Menu menu;
    private ImageView userImg;
    private TextView userName, userMessage;
    public static User user;
    private Fragment currentFragment;
    private boolean opened = false;
    private int count = 0;
    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userImg = (ImageView) headerLayout.findViewById(R.id.user_img);
        userMessage = (TextView) headerLayout.findViewById(R.id.user_message);
        userName = (TextView) headerLayout.findViewById(R.id.user_name);
        viewMap = new HashMap<>();
        manager = getSupportFragmentManager();
    }

    @Override
    protected void findView() {
        contentView = (ConstraintLayout) findViewById(R.id.content_main);
        contentView.removeAllViews();
    }

    @Override
    protected void setListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                opened = false;
                InputMethodManager manager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                boolean isOpen = manager.isActive();
                if (isOpen) {//因为是在fragment下，所以用了getView()获取view，也可以用findViewById（）来获取父控件
                    drawerLayout.requestFocus();//使其它view获取焦点.这里因为是在fragment下,所以便用了getView(),可以指定任意其它view
                    manager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                count++;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void loadData() {
        String s = getIntent().getStringExtra("info");
        if (s == null) {
            user = (User) getIntent().getExtras().get("user");
        } else {
            user = JsonParser.getUserInfo(s);
            user.setUserName((String) SPUtils.get(this, "UserName", ""));
            user.setPassword((String) SPUtils.get(this, "PassWord", ""));
            DataUtil.insertUser(user);
        }
        JCApplication.user = user;
        userMessage.setText(user.getQuxian());
        userName.setText(user.getRealName());
        loadView("实地检查", "first");

        if (!ServiceUtil.isServiceRunning("com.bokun.bkjcb.on_siteinspection.Service.UploadService")) {
            ArrayList<ProjectPlan> pre_plans_uploading = DataUtil.getProjectByState("正在上传", JCApplication.user);
            if (pre_plans_uploading.size() > 0) {
                for (int i = 0; i < pre_plans_uploading.size(); i++) {
                    pre_plans_uploading.get(i).setAq_jctz_zt("等待上传");
                    DataUtil.updateProjectState(pre_plans_uploading.get(i));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        LogUtil.logI("back count" + count);
        CheckPlanFragment fragment = (CheckPlanFragment) viewMap.get("first");
        if (!drawerLayout.isDrawerOpen(GravityCompat.START) && !opened) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START) && !opened) {
            drawerLayout.closeDrawer(GravityCompat.START);
            opened = true;
        } else if (opened && currentFragment != fragment) {
            loadView("实地检查", "first");
        } else {

            if (ServiceUtil.isServiceRunning("com.bokun.bkjcb.on_siteinspection.Service.UploadService")) {
                exitCheck();
                       /* .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
                                intent.setPackage(getPackageName());
                                MainActivity.this.stopService(intent);
                                dialog.dismiss();
                                loadView("上传进度", "forth");
                            }
                        })*/

            } else {
                super.onBackPressed();
            }
//            super.onBackPressed();
        }
    }

    private void exitCheck() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("有上传任务正在进行，请停止任务后再退出程序？")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.STARTUPLOAD");//你定义的service的action
                        intent.setPackage(getPackageName());
                        MainActivity.this.stopService(intent);
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_check_plan) {
            loadView("实地检查", "first");
        } else if (id == R.id.nav_info_check) {
            loadView("临时检查", "second");
        } else if (id == R.id.nav_map) {
            loadView("地图", "third");
        } else if (id == R.id.nav_info_progress) {
            loadView("检查进度", "fifth");
        } else if (id == R.id.nav_update_result) {
            loadView("上传进度", "forth");
        } else if (id == R.id.nav_app_introduce) {
            AboutActivity.toAboutActiivty(this);
        } else if (id == R.id.nav_exit) {
            if (ServiceUtil.isServiceRunning("com.bokun.bkjcb.on_siteinspection.Service.UploadService")) {
                exitCheck();
            } else {
//                AppManager.getAppManager().finishAllActivity();
                AppManager.getAppManager().AppExit(this);
            }
        } else if (id == R.id.nav_logout) {
            LoginActivity.comeToLoginActivity(this);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadView(String title, String key) {
        setLeftChecked(key);
        toolbar.setTitle(title);
        if (menu != null) {
            menu.setGroupVisible(R.id.menu_map, false);
        }
        currentFragment = viewMap.get(key);
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
                ((UpLoadFragment) currentFragment).setListener(this);
            } else if (key.equals("second")) {
                currentFragment = new SearchFragment();
            }else if(key.equals("fifth")){
                //
                currentFragment = new PlanProgressFragment();
            }
            transaction.add(contentView.getId(), currentFragment);
            viewMap.put(key, currentFragment);
        } else {
            if (key.equals("third")) {
                menu.setGroupVisible(R.id.menu_map, true);
            }
            if (key.equals("forth")) {
                ((UpLoadFragment) currentFragment).refresh();
            }
//            transaction.add(contentView.getId(),currentFragment);
            transaction.show(currentFragment);
        }
        transaction.commit();
    }

    private void setLeftChecked(String key) {
        switch (key) {
            case "first":
                navigationView.setCheckedItem(R.id.nav_check_plan);
                break;
            case "second":
                navigationView.setCheckedItem(R.id.nav_info_check);
                break;
            case "third":
                navigationView.setCheckedItem(R.id.nav_map);
                break;
            case "forth":
                navigationView.setCheckedItem(R.id.nav_update_result);
                break;
        }
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
        intent.putExtra("info", string);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void ComeToMainActivity(Activity activity, User user) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFragment == viewMap.get("third")) {
            toolbar.getMenu().setGroupVisible(R.id.menu_map, true);
        } else {
            toolbar.getMenu().setGroupVisible(R.id.menu_map, false);
        }
    }

    public void showMenu(boolean is) {
       /* if (menu != null) {
            menu.setGroupVisible(R.id.menu_map, is);
        } else {
            toolbar.getMenu().setGroupVisible(R.id.menu_map, is);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.logI("返回MainActivity");
        if (requestCode == 1 && resultCode == 2) {
            SearchFragment fragment = (SearchFragment) viewMap.get("second");
            if (fragment != null) {
                fragment.cleanHistory();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateChange() {
        LogUtil.logI("数据已改变");
        CheckPlanFragment fragment = (CheckPlanFragment) viewMap.get("first");
        if (fragment != null) {
            fragment.dateHasChange();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
