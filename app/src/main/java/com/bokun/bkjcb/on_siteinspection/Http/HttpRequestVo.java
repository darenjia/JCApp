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

    public HttpRequestVo(String requestUrl, String requestJson) {
        this.requestUrl = requestUrl;
        this.requestJson = requestJson;
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
}
