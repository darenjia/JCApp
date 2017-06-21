package com.bokun.bkjcb.on_siteinspection.Http;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ManagerInfo;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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
        JsonResult result = new JsonResult();
        if (object == null) {
            return result;
        }
        LogUtil.logI(object.toString());
        SoapObject detail = (SoapObject) object.getProperty(0);
        String content = XmlParser.parseSoapObject(detail);
        if (content.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                result.success = jsonObject.getBoolean("success");
                result.message = jsonObject.getString("message");
                try {
                    result.resData = jsonObject.getString("data");
                } catch (JSONException e) {
                    result.resData = "";
                }
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
        LogUtil.logI(json);
        ArrayList<CheckPlan> results = new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
        try {
            //将JSON的String 转成一个JsonArray对象
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                CheckPlan checkPlan = new CheckPlan();
                JSONObject con = jsonArray.optJSONObject(i);
                try {
                    checkPlan.setIdentifier(con.getInt("SysGcxxdjh"));
                } catch (JSONException e) {
                    checkPlan.setIdentifier(0);
                }
                try {
                    checkPlan.setSysId(con.getInt("SysId"));
                } catch (JSONException e) {
                    checkPlan.setSysId(0);
                }

                try {
                    checkPlan.setAddress(con.getString("ScJbGcdz"));
                } catch (JSONException e) {
                    checkPlan.setAddress("");
                }
                try {
                    checkPlan.setArea(con.getString("ScXzJzmj"));
                } catch (JSONException e) {
                    checkPlan.setArea("");
                }
                try {
                    checkPlan.setType(con.getString("SysSsmc"));
                } catch (Exception e) {
                    checkPlan.setType("");
                }
                try {
                    checkPlan.setName(con.getString("ScJbGcmc"));
                } catch (JSONException e) {
                    checkPlan.setName("");
                }
                try {
                    checkPlan.setQuxian(con.getString("SysQuXian"));
                } catch (JSONException e) {
                    checkPlan.setQuxian("");
                }
                results.add(checkPlan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.logI(results.size() + "");
        return results;
    }

    public static ArrayList<ProjectPlan> getProjectData(String json) {
        LogUtil.logI(json);
        ArrayList<ProjectPlan> results = new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            ProjectPlan projectPlan = gson.fromJson(element, ProjectPlan.class);
            if (projectPlan.getAq_sysid() == null || projectPlan.getAq_sysid().equals("")) {
                continue;
            }
            results.add(projectPlan);
        }
        return results;
    }

    public static ManagerInfo getUserInfo(String json) {
        ManagerInfo result = new ManagerInfo();
        try {
            JSONObject jsonObject = new JSONObject(json);
            result.quxian = jsonObject.getString("quxian");
            result.roles = jsonObject.getString("roles");
            result.username = jsonObject.getString("sys_realname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
