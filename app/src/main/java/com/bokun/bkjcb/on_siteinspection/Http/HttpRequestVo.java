package com.bokun.bkjcb.on_siteinspection.Http;

import java.util.HashMap;

/**
 * Created by BKJCB on 2017/3/16.
 */

public class HttpRequestVo {

    public String requestUrl;
    public HashMap<String, String> requestDataMap;
    public String requestJson;
    public JsonParser parser;
    public String methodName;

    public HttpRequestVo(String requestUrl, String requestJson) {
        this.requestUrl = requestUrl;
        this.requestJson = requestJson;
    }

    public HttpRequestVo() {
        this.requestDataMap = new HashMap<>();
    }

    public HttpRequestVo(String requestJson, String requestUrl, JsonParser parser) {
        this.requestJson = requestJson;
        this.requestUrl = requestUrl;
        this.parser = parser;
    }

    public HttpRequestVo(String requestUrl, HashMap<String, String> requestDataMap, JsonParser parser) {
        this.requestUrl = requestUrl;
        this.requestDataMap = requestDataMap;
        this.parser = parser;
    }

    public HttpRequestVo(HashMap<String, String> requestDataMap, String methodName) {
        this.requestDataMap = requestDataMap;
        this.methodName = methodName;
    }

    public HashMap<String, String> getRequestDataMap() {
        return requestDataMap;
    }

    public void setRequestDataMap(HashMap<String, String> requestDataMap) {
        this.requestDataMap = requestDataMap;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
