package com.bokun.bkjcb.on_siteinspection.Http;

import android.content.Context;
import android.text.TextUtils;

import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.NetworkUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
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

public class HttpManager implements Runnable {
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
        HttpTransportSE ht;
        boolean again = true;
        try {

            String NAMESPACE = "http://zgzxjk/";
            String METHOD_NAME = requestVo.methodName;
            String URL = "http://192.168.137.1:1856/zgzxjkWebService.asmx";
            // 新建 SoapObject 对象
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            HashMap<String, String> map = requestVo.requestDataMap;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                rpc.addProperty(entry.getKey(), entry.getValue());
            }
            // 创建 HttpTransportSE 对象,并指定 WebService 的 WSDL 文档的 URL
            ht = new HttpTransportSE(URL);
            // 设置 debug 模式
            ht.debug = true;
            // 获得序列化的 envelope
            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER12);
            // 设置 bodyOut 属性的值为 SoapObject 对象 rpc
            envelope.bodyOut = rpc;
            // 指定 webservice 的类型为 dotNet
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            // 使用 call 方法调用 WebService 方法
            ht.call(null, envelope);
            // 获取返回结果
            SoapObject result = (SoapObject) envelope.bodyIn;
           /* // 使用 getResponse 方法获得 WebService 方法的返回结果
            SoapObject detail = (SoapObject) result.getProperty("GetUserResult");
            // 解析返回的数据信息为 SoapObject 对象,对其进行解析
            String date = detail.getProperty("success").toString();
            String date1 = detail.getProperty("message").toString();*/
            if (result != null) {
                listener.action(RequestListener.EVENT_GET_DATA_SUCCESS, result);
            } else {
                ht.call(null, envelope);
                result = (SoapObject) envelope.bodyIn;
                listener.action(RequestListener.EVENT_GET_DATA_SUCCESS, result);
            }
        } catch (ProtocolException e) {
            listener.action(RequestListener.EVENT_GET_DATA_EEEOR, null);
        } catch (SocketTimeoutException e) {
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
     * @param inStream 输入流
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
        if (requestVo.parser == null) {
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


