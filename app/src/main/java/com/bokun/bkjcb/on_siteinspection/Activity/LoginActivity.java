package com.bokun.bkjcb.on_siteinspection.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bokun.bkjcb.on_siteinspection.Http.HttpManager;
import com.bokun.bkjcb.on_siteinspection.Http.HttpRequestVo;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Http.RequestListener;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RequestListener.EVENT_NOT_NETWORD:
                    Snackbar.make(mCardView, "", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                    break;
                case RequestListener.EVENT_CLOSE_SOCKET:
                    Snackbar.make(mCardView, "网络错误！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_NETWORD_EEEOR:
                    Snackbar.make(mCardView, "请确认网络是否可用！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_EEEOR:
                    Snackbar.make(mCardView, "服务器错误，请稍后再试！", Snackbar.LENGTH_LONG).show();
                    break;
                case RequestListener.EVENT_GET_DATA_SUCCESS:
                    JsonResult result = (JsonResult) msg.obj;
                    if (result.success) {
                        MainActivity.ComeToMainActivity(LoginActivity.this, result.resData);
                    } else {
                        Snackbar.make(mCardView, result.message, Snackbar.LENGTH_LONG).show();
                    }
                    break;

            }
            noLogining();
        }
    };
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
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//
            // String requestJson = Constants.GetUser.replace("UserName", userName).replace("UserPwd", password);
            // HttpRequestVo request = new HttpRequestVo(Constants.GetUserURL, requestJson);
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

    @Override
    protected void loadData() {
        String flag = getFormPrefer("isRemberPass");
        isRemberPass = flag.equals("true");
        if (isRemberPass) {
            String username = getFormPrefer("UserName");
            String password = getFormPrefer("PassWord");
            mUserName.setText(username);
            mPassword.setText(password);
            mRembPass.setChecked(true);
        }
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

    private String getFormPrefer(String key) {
        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean flag = mRembPass.isChecked();
        if (flag) {
            String username = mUserName.getText().toString();
            String password = mPassword.getText().toString();
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                writeToSharedPreferences("UserName", username);
                writeToSharedPreferences("PassWord", password);
                writeToSharedPreferences("isRemberPass", "true");
            }
        }
    }

    public void writeToSharedPreferences(String key, String value) {
        SharedPreferences preferences = getSharedPreferences("default", MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
