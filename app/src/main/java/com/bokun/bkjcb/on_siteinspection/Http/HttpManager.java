package com.bokun.bkjcb.on_siteinspection.Http;

import android.content.Context;
import android.text.TextUtils;

import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BKJCB on 2017/3/16.
 * 网络请求管理
 */

public class HttpManager implements Runnable{
    private Context context;
    private RequestListener listener;
    private Thread currentRequest = null;
    HttpURLConnection conn = null;
    InputStream input = null;
    private HttpRequestVo requestVo;
    private static final String ENCODING = "UTF-8";
    private static final int TIME = 40 * 1000;
    public static final int GET_MOTHOD = 1;
    public static final int POST_MOTHOD = 2;
    /**
     * 1： get请求 2： post请求
     */
    private int requestStatus = 1;

    public HttpManager(Context mContext, RequestListener mListener,
                       HttpRequestVo vo, int mRequeststatus) {

        this.context = mContext;
        this.listener = mListener;
        this.requestVo = vo;
        this.requestStatus = mRequeststatus;
    }

    public HttpManager(Context mContext, RequestListener mListener,
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
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // if (isUserProxy) {
            // conn.setRequestProperty("X-Online-Host", host);
            // }
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIME);
            conn.setReadTimeout(TIME);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(RequestListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }

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

            String requestStr = requestVo.requestJson.toString();
            LogUtil.logI("request", requestStr);
            byte[] data = requestStr.getBytes();
            URL url = new URL(requestVo.requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME);
            conn.setReadTimeout(TIME);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不使用Cache
            conn.setRequestProperty("Charset", ENCODING);
            conn.setRequestProperty("Content-Length",
                    String.valueOf(data.length));
            conn.setRequestProperty("Content-Type", "text/json;charset=utf-8");
            conn.setRequestMethod("POST");

            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            outStream.write(data);
            outStream.flush();
            outStream.close();
            if(conn==null){
                return;
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(RequestListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }
            } else if (responseCode == 404) {
                input = conn.getErrorStream();
                if (input != null) {
                    listener.action(RequestListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                } else {
                    listener.action(RequestListener.EVENT_NETWORD_EEEOR,
                            null);
                }
            } else {
                listener.action(RequestListener.EVENT_NETWORD_EEEOR, null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(RequestListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            LogUtil.logI("404");
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
     * 读取数据
     *
     * @param inStream
     *            输入流
     * @return
     * @throws Exception
     */
    private Object readStream(InputStream inStream) throws Exception {
        String result;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        result = new String(outStream.toByteArray(), ENCODING);
        LogUtil.logI("response", result);
        outStream.close();
        inStream.close();
        if(requestVo.parser == null) {
            return new HashMap<String, Object>();
        }
        return requestVo.parser.parseJSON(result);
    }

    /**
     * 取消当前HTTP连接处理
     */
    public void cancelHttpRequest() {
        if (currentRequest != null && currentRequest.isAlive()) {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            input = null;
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn = null;
            // currentRequest.stop();
            currentRequest = null;
            System.gc();
        }
    }

    public void run() {
        // 0：无网络 1：WIFI 2：CMWAP 3：CMNET
        boolean isEnable = NetworkUtils.isEnable(context);
        if (isEnable) {
                sendPostRequest();
        } else {
            listener.action(RequestListener.EVENT_NOT_NETWORD, null);
        }
    }
}


