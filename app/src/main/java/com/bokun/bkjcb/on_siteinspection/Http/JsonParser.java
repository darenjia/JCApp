package com.bokun.bkjcb.on_siteinspection.Http;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/16.
 */

public class JsonParser {

    public static JsonResult parseJSON(String json) {
        JsonResult result = new JsonResult();
        String content = XmlParser.parseJSON(json);
        if (content.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                result.success = jsonObject.getBoolean("success");
                result.message = jsonObject.getString("message");
                result.resData = jsonObject.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            result.success = false;
            result.message = "请求出错，请稍后再试！";
        }
        return result;
    }

    public static ArrayList<CheckPlan> getJSONData(String json) {
        ArrayList<CheckPlan> results = new ArrayList<>();
        String content = XmlParser.parseJSON(json);
        if (content.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
        return results;
    }
}
