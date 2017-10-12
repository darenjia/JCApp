package com.bokun.bkjcb.on_siteinspection.Http;

import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.TableKeys;
import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        LogUtil.logI(content);
        if (content.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                result.success = jsonObject.getBoolean("success");
                result.message = jsonObject.getString("message");
                String s;
                try {
                    s = jsonObject.getString("data");
                } catch (JSONException e) {
                    s = "";
                }
                if (s.equals("")) {
                    s = "{}";
                }
                result.resData = s;
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
                    checkPlan.setSysID(con.getInt("SysId"));
                } catch (JSONException e) {
                    checkPlan.setSysID(0);
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
            LogUtil.logI(projectPlan.getAQ_JCTZ_sfjc() + "检查状态");
        }
        return results;
    }

    public static User getUserInfo(String json) {
        User result = new User();
        try {
            JSONObject jsonObject = new JSONObject(json);
            result.setQuxian(jsonObject.getString("quxian"));
            result.setRole(jsonObject.getString("roles"));
            result.setRealName(jsonObject.getString("sys_realname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<TableKeys> getKeysItems(Context context) {
        ArrayList list = new ArrayList<>();
        JSONObject object = null;
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = context.getAssets().open("TableDetail");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            object = new JSONObject(builder.toString());
            JSONArray array = object.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                TableKeys keys = new TableKeys();
                keys.setId(Integer.parseInt(obj.getString("id")));
                keys.setTitle(obj.getString("title"));
                keys.setType(obj.getInt("type"));
                keys.setUnit(obj.getString("unit"));
                list.add(keys);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
