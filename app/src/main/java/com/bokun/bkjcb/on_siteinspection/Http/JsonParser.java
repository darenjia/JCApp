package com.bokun.bkjcb.on_siteinspection.Http;

import android.text.TextUtils;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.JsonResult;
import com.bokun.bkjcb.on_siteinspection.Domain.ProgressDetail;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.ProjectProgress;
import com.bokun.bkjcb.on_siteinspection.Domain.User;
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
                if (TextUtils.isEmpty(s)) {
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
       /* //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            CheckPlan checkPlan = gson.fromJson(element, CheckPlan.class);
            if (checkPlan.getIdentifier() == 0 || checkPlan.getSysId() == 0) {
                continue;
            }
            results.add(checkPlan);
            LogUtil.logI(checkPlan.getName());
        }*/
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
                try {
                    checkPlan.setUrl(con.getString("URLPDF"));
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

    public static ArrayList<ProjectProgress> getProgressData(String json) {
        LogUtil.logI(json);
        ArrayList<ProjectProgress> results = new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
       /* //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            CheckPlan checkPlan = gson.fromJson(element, CheckPlan.class);
            if (checkPlan.getIdentifier() == 0 || checkPlan.getSysId() == 0) {
                continue;
            }
            results.add(checkPlan);
            LogUtil.logI(checkPlan.getName());
        }*/
        try {
            //将JSON的String 转成一个JsonArray对象
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                ProjectProgress object = new ProjectProgress();
                JSONObject con = jsonArray.optJSONObject(i);
                try {
                    object.setAq_lh_jcmc(con.getString("aq_lh_jcmc"));
                } catch (JSONException e) {
                    object.setAq_lh_jcmc("");
                }
                try {
                    object.setAq_lh_qxjd(con.getString("aq_lh_qxjd"));
                } catch (JSONException e) {
                    object.setAq_lh_qxjd("");
                }

                try {
                    object.setAq_lh_szqx(con.getString("aq_lh_szqx"));
                } catch (JSONException e) {
                    object.setAq_lh_szqx("");
                }
                try {
                    object.setQx(con.getString("qx"));
                } catch (JSONException e) {
                    object.setQx("");
                }
                try {
                    object.setAq_lh_jcrq(con.getString("aq_lh_jcrq"));
                } catch (Exception e) {
                    object.setAq_lh_jcrq("");
                }
                try {
                    object.setAq_jctype(con.getString("aq_jctype"));
                } catch (JSONException e) {
                    object.setAq_jctype("");
                }
                try {
                    object.setBjqk(con.getString("bjqk"));
                } catch (JSONException e) {
                    object.setBjqk("");
                }
                try {
                    object.setAq_lh_seqid(con.getString("aq_lh_seqid"));
                } catch (JSONException e) {
                    object.setAq_lh_seqid("");
                }

                results.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static String getURL(String json) {
        String url = "";
        if (json.equals("{}")) {
            return "";
        }
        try {
            //将JSON的String 转成一个JsonArray对象
            JSONObject object = new JSONObject(json);
            try {
                url = object.getString("URLPDF");
            } catch (JSONException e) {
                url = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
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
            result.setUserID(jsonObject.getString("sys_userid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static JsonResult getDataItems(SoapObject object) {
        SoapObject soapObject = (SoapObject) object.getProperty("BanbenhaoResult");
        String data = soapObject.getPropertyAsString("data");
        String success = soapObject.getPropertyAsString("success");
        String message = soapObject.getPropertyAsString("message");
        JsonResult jsonResult = new JsonResult();
        jsonResult.success = Boolean.parseBoolean(success);
        jsonResult.resData = data.trim();
        jsonResult.message = message;
//        XLog.i(success + message + data);

        return jsonResult;
    }
    public static ArrayList<ProgressDetail> getProgressDetail(String json){
        ArrayList<ProgressDetail> results= new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            ProgressDetail detail = gson.fromJson(element, ProgressDetail.class);
            results.add(detail);
        }
        return results;
    }

    private void  getData(String json, Class c) {
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {

        }
    }
}
