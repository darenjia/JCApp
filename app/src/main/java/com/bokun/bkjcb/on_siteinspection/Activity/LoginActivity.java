package com.bokun.bkjcb.on_siteinspection.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.SQLite.DataUtil;
import com.bokun.bkjcb.on_siteinspection.SQLite.PlanDao;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.MD5Util;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.SPUtils;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;

/**
 * Created by BKJCB on 2017/3/17.
 */

public class LoginActivity extends BaseActivity implements RequestListener {
    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private Button mChangeUser;
    private LinearLayout mLoginView;
    private boolean isRemberPass;
    private String passWord;
    private String ip;

    private static class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        public MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final LoginActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case RequestListener.EVENT_NOT_NETWORD:
                        Snackbar.make(activity.mCardView, "无网络连接，请检查网络", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                activity.startActivity(intent);
                            }
                        }).show();
                        break;
                    case RequestListener.EVENT_CLOSE_SOCKET:
                        Snackbar.make(activity.mCardView, "网络错误！", Snackbar.LENGTH_LONG).show();
                        break;
                    case RequestListener.EVENT_NETWORD_EEEOR:
                        Snackbar.make(activity.mCardView, "请确认网络是否可用！", Snackbar.LENGTH_LONG).show();
                        break;
                    case RequestListener.EVENT_GET_DATA_EEEOR:
                        Snackbar.make(activity.mCardView, "服务器错误，请稍后再试！", Snackbar.LENGTH_LONG).show();
                        break;
                    case RequestListener.EVENT_GET_DATA_SUCCESS:
                        JsonResult result = (JsonResult) msg.obj;
                        if (result.success) {
                            MainActivity.ComeToMainActivity(activity, result.resData);
                        } else {
                            Snackbar.make(activity.mCardView, result.message, Snackbar.LENGTH_LONG).show();
                        }
                        break;

                }
                activity.noLogining();
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);
    private HttpManager httpManager;
    private CheckBox mRembPass;
    private CardView mCardView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mylogin);
    }

    @Override
    protected void findView() {
        mChangeUser = (Button) findViewById(R.id.shift_user_button);
        mLogin = (Button) findViewById(R.id.login_button);
        mPassword = (EditText) findViewById(R.id.input_password);
        mUserName = (EditText) findViewById(R.id.input_username);
        mLoginView = (LinearLayout) findViewById(R.id.logining_view);
        mRembPass = (CheckBox) findViewById(R.id.login_pass_remb);
        mCardView = (CardView) findViewById(R.id.login_cardview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        Utils.initSystemBar(this, toolbar);
    }

    @Override
    protected void setListener() {
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity.ComeToMainActivity(LoginActivity.this);
                attemptLogin();
            }
        });
        mChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpManager != null && httpManager.isRunning()) {
                    httpManager.cancelHttpRequest();
                }
                mUserName.setText("");
                mPassword.setText("");
                noLogining();
                mUserName.requestFocus();
            }
        });
        mRembPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRemberPass = true;
                } else {
                    isRemberPass = false;
                }
            }
        });
    }

    private void noLogining() {
        mLoginView.setVisibility(View.GONE);
        mCardView.setVisibility(View.VISIBLE);
    }

    /*
    * 判断用户名是否可用*/
    private boolean isUserNameValid(String name) {
        return name.length() > 0;
    }

    /*
    * 判断密码长度是否符合要求*/
    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    private void attemptLogin() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLogin.getWindowToken(), 0);
        // Reset errors.
        mUserName.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            mUserName.setError(getString(R.string.error_invalid_username));
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.g
            focusView.requestFocus();
        } else {
            User user = DataUtil.getUser(userName);
            if (!NetworkUtils.isEnable(this) && user != null) {
                LogUtil.logI(MD5Util.encode(password) + "  " + passWord);
                if (MD5Util.encode(password).equals(passWord)) {
                    saveInfo();
                    MainActivity.ComeToMainActivity(this, user);
                } else {
                    mPassword.setError("密码错误");
                }
            } else {
                HttpRequestVo request = new HttpRequestVo();
                request.getRequestDataMap().put("user", userName);
                request.getRequestDataMap().put("password", password);
                request.setMethodName("GetUser");
                httpManager = new HttpManager(this, this, request, 2);
                httpManager.postRequest();
                mLoginView.setVisibility(View.VISIBLE);
                mCardView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void loadData() {
        String flag = (String) SPUtils.get(this, "isRemberPass", "false");
        isRemberPass = flag.equals("true");
        String username = (String) SPUtils.get(this, "UserName", "");
        passWord = (String) SPUtils.get(this, "PassWord", "");
        if (isRemberPass) {
            mUserName.setText(username);
//            mPassword.setText(password);
            mRembPass.setChecked(true);
        }
//        changeIp(null);
        //检查字典表是否存在，写入字典表内容
        checkTable();
    }

    @Override
    public void action(int i, Object object) {
        if (object != null) {
            LogUtil.logI(object.toString());
        }
        JsonResult result = JsonParser.parseSoap((SoapObject) object);
        Message msg = new Message();
        msg.what = i;
        msg.obj = result;
        if (i == RequestListener.EVENT_GET_DATA_SUCCESS && result.success) {
            saveInfo();
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onBackPressed() {
        if (httpManager != null && httpManager.isRunning()) {
            httpManager.cancelHttpRequest();
            Snackbar.make(mCardView, "登录已取消！", Snackbar.LENGTH_LONG).show();
            noLogining();
        } else {
            super.onBackPressed();
        }

    }

    public static void comeToLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void saveInfo() {
        boolean flag = mRembPass.isChecked();
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            SPUtils.put(this, "UserName", username);
            SPUtils.put(this, "PassWord", MD5Util.encode(password));
            SPUtils.put(this, "isRemberPass", flag ? "true" : "false");
        }

    }

    private void checkTable() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                PlanDao dao = new PlanDao(context);
                try {
                    if (dao.checkPlanInfoTable() == 0) {
                        dao.saveTableKey();
                        dao.close();
                    }
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    dao.createTable();
                    dao.saveTableKey();
                    dao.close();
                }
            }
        }).start();

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.info_setting) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText tv = (EditText) getLayoutInflater().inflate(R.layout.edit_ip, null);
            String ip = (String) SPUtils.get(this, "IP", "");
            tv.setHint(ip);
            builder.setView(tv);
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String address = tv.getText().toString();
                    if (!address.equals("")) {
                        SPUtils.put(LoginActivity.this, "IP", address);
                        changeIp(address);
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
        return true;
    }*/

//    private void changeIp(String address) {
//        String ip;
//        if (address == null) {
//            ip = (String) SPUtils.get(this, "IP", "");
//        } else {
//            ip = address;
//        }
//        if (ip.equals("")) {
//            ip = "101.231.52.50";
//        }
//        Constants.HTTPURL = Constants.HTTPURL_temple.replace("IP", ip);
//        Constants.FTP_HOST_DEFAULT = ip;
//    }
}
