package com.bokun.bkjcb.on_siteinspection.Http;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ManagerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

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

    public static JsonResult parseSoap(SoapObject object) {
        if (object == null) {
            return null;
        }
        SoapObject detail = (SoapObject) object.getProperty(0);
        JsonResult result = new JsonResult();
        String content = XmlParser.parseSoapObject(detail);
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
        try {
            //将JSON的String 转成一个JsonArray对象
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < 10; i++) {
                CheckPlan checkPlan = new CheckPlan();
                JSONObject con = jsonArray.optJSONObject(i);
                checkPlan.setIdentifier(con.getInt("SysGcxxdjh"));
                checkPlan.setSysId(con.getInt("SysId"));
                checkPlan.setAddress(con.getString("ScJbGcdz"));
                checkPlan.setArea(con.getString("ScXzJzmj"));
                checkPlan.setName(con.getString("ScJbGcmc"));
                checkPlan.setType(con.getString("SysSsmc"));
                checkPlan.setQuxian(con.getString("SysQuXian"));
                results.add(checkPlan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public static ManagerInfo getUserInfo(String json) {
        ManagerInfo result = new ManagerInfo();
        try {
            JSONObject jsonObject = new JSONObject(json);
            result.quxian = jsonObject.getString("quxian");
            result.roles = jsonObject.getString("roles");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
