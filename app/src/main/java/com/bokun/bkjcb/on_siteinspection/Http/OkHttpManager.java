package com.bokun.bkjcb.on_siteinspection.Http;

import android.content.Context;
import android.text.TextUtils;

import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by BKJCB on 2017/3/16.
 * 网络请求管理
 */

public class OkHttpManager implements Runnable {
    private Context context;
    private RequestListener listener;
    private Thread currentRequest = null;
    OkHttpClient client = null;
    private HttpRequestVo requestVo;
    private static final String ENCODING = "UTF-8";
    private static final int TIME = 40 * 1000;
    public static final int GET_MOTHOD = 1;
    public static final int POST_MOTHOD = 2;
    /**
     * 1： get请求 2： post请求
     */
    private int requestStatus = 1;

    public OkHttpManager(Context mContext, RequestListener mListener,
                         HttpRequestVo vo, int mRequeststatus) {

        this.context = mContext;
        this.listener = mListener;
        this.requestVo = vo;
        this.requestStatus = mRequeststatus;
    }

    public OkHttpManager(Context mContext, RequestListener mListener,
                         HttpRequestVo vo) {
        this.context = mContext;
        this.listener = mListener;
        this.requestVo = vo;
    }

    public void postRequest() {
        requestStatus = 2;
        currentRequest = new Thread(this);
        currentRequest.start();
    }

    public void getRequeest() {
        requestStatus = 1;
        currentRequest = new Thread(this);
        currentRequest.start();
    }

    /**
     * 对请求的字符串进行编码
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String requestEncodeStr(String requestStr)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(requestStr, ENCODING);
    }

    private void sendGetRequest() {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(requestVo.requestUrl);
            if (requestVo.requestDataMap != null) {
                buf.append("?");
                HashMap<String, String> map = requestVo.requestDataMap;
                int i = 1;
                int size = map.size();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (i == size) {
                        if (TextUtils.isEmpty(entry.getValue())) {
                            buf.append(entry.getKey() + "=");
                        } else {
                            buf.append(entry.getKey() + "=" + requestEncodeStr(entry.getValue()));
                        }
                    } else {
                        if (TextUtils.isEmpty(entry.getValue())) {
                            buf.append(entry.getKey() + "=" + "&");
                        } else {
                            buf.append(entry.getKey() + "=" + requestEncodeStr(entry.getValue())
                                    + "&");
                        }

                    }
                    i++;
                }
            }

            URL url = new URL(buf.toString());
            LogUtil.logI("URL", buf.toString());
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            // if (isUserProxy) {
            // conn.setRequestProperty("X-Online-Host", host);
            // }
            if (response.isSuccessful()) {
                listener.action(RequestListener.EVENT_GET_DATA_SUCCESS,
                        response.body().string());

            } else {
                listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
        }
    }

    /**
     * post请求
     *
     * @return
     */
    private void sendPostRequest() {
        try {
            URL url = new URL(requestVo.requestUrl);
            MediaType JSON = MediaType.parse("application/soap+xml; charset=utf-8");
            client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, requestVo.requestJson);
            Request request = new Request.Builder()
                    .post(body)
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                listener.action(RequestListener.EVENT_GET_DATA_SUCCESS,
                        response.body().string());
            } else {
                listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
        }
    }

    public boolean isRunning() {
        if (currentRequest != null && currentRequest.isAlive()) {
            return true;
        }
        return false;
    }


    /**
     * 取消当前HTTP连接处理
     */
    public void cancelHttpRequest() {
        if (currentRequest != null && currentRequest.isAlive()) {
            if (client != null) {
                try {
                    client.dispatcher().cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            client = null;
            // currentRequest.stop();
            currentRequest = null;
            System.gc();
        }
    }

    public void run() {
        // 0：无网络 1：WIFI 2：CMWAP 3：CMNET
        boolean isEnable = NetworkUtils.isEnable(context);
        if (isEnable) {
            if (requestStatus == 1) {
                sendGetRequest();
            } else {
                sendPostRequest();
            }
        } else {
            listener.action(RequestListener.EVENT_NOT_NETWORD, null);
        }
    }
}


