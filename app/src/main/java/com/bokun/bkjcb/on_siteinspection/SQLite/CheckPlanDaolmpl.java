package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/31.
 */

public class CheckPlanDaolmpl extends CheckPlanDao {
    private SQLiteDatabase db;

    public CheckPlanDaolmpl(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        this.db = util.getWritableDatabase();
    }

    @Override
    public void insertCheckPlan(CheckPlan plan) {
        ContentValues values = new ContentValues();
        values.put("identifier", plan.getIdentifier());
        values.put("sysId", plan.getSysId());
        values.put("name", plan.getName());
        values.put("state", plan.getState());
        values.put("address", plan.getAddress());
        values.put("quxian", plan.getQuxian());
        values.put("area", plan.getArea());
        values.put("manager", plan.getManager());
        values.put("user", plan.getUser());
        values.put("type", plan.getType());
        values.put("tel", plan.getTel());
        values.put("url", plan.getUrl());
        values.put("plan_type", plan.getPlan_type());
        long i = db.insert("checkplan", null, values);
//        LogUtil.logI("插入plan" + i);
    }

    @Override
    public boolean updateCheckPlan(CheckPlan plan) {
        CheckPlan checkPlan = queryCheckPlan(plan.getIdentifier());
        if (checkPlan == null) {
            return false;
        }
        ContentValues values = new ContentValues();
       /* values.put("name", plan.getName());
        values.put("state", plan.getState());*/
        values.put("url", plan.getUrl());
        int isSuccess = db.update("checkplan", values, "identifier = ?", new String[]{String.valueOf(plan.getIdentifier())});

        return isSuccess != 0;
    }

    @Override
    public CheckPlan queryCheckPlan(int identifier) {
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, "identifier=?", new String[]{String.valueOf(identifier)}, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
        }
        cursor.close();
        //LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return plan;
    }

    @Override
    public CheckPlan queryCheckPlan(String sysID) {
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, "sysId=?", new String[]{String.valueOf(sysID)}, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            plan.setArea(cursor.getString(cursor.getColumnIndex("area")));
            plan.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            plan.setType(cursor.getString(cursor.getColumnIndex("type")));
            plan.setQuxian(cursor.getString(cursor.getColumnIndex("quxian")));
            plan.setManager(cursor.getString(cursor.getColumnIndex("manager")));
            plan.setUser(cursor.getString(cursor.getColumnIndex("user")));
            plan.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            plan.setPlan_type(cursor.getInt(cursor.getColumnIndex("plan_type")));
        }
        cursor.close();
        //LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return plan;
    }

    @Override
    public String queryCheckPlanIsNull(int identifier) {
        Cursor cursor = db.query("checkplan", null, "identifier=?", new String[]{String.valueOf(identifier)}, null, null, null);
        String fileName = null;
        if (cursor.moveToNext()) {
            fileName = cursor.getString(cursor.getColumnIndex("url"));
        } else {
            return null;//数据库未记录，重新插入
        }
        if (fileName == null || fileName.equals("")) {
            return "";//数据库已有记录，文件地址为空，更新信息
        }
        cursor.close();
        return fileName;//不用处理
    }

    @Override
    public ArrayList<CheckPlan> queryCheckPlan() {
        ArrayList<CheckPlan> list = new ArrayList<>();
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setArea(cursor.getString(cursor.getColumnIndex("area")));
            plan.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            plan.setType(cursor.getString(cursor.getColumnIndex("type")));
            plan.setQuxian(cursor.getString(cursor.getColumnIndex("quxian")));
            plan.setManager(cursor.getString(cursor.getColumnIndex("manager")));
            plan.setUser(cursor.getString(cursor.getColumnIndex("user")));
            plan.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            plan.setPlan_type(cursor.getInt(cursor.getColumnIndex("plan_type")));
            list.add(plan);
        }
        //LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return list;
    }

    @Override
    public ArrayList<CheckPlan> query(String name) {
        ArrayList<CheckPlan> list = new ArrayList<>();
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, "name like ?", new String[]{"%" + name + "%"}, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setArea(cursor.getString(cursor.getColumnIndex("area")));
            plan.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            plan.setType(cursor.getString(cursor.getColumnIndex("type")));
            plan.setQuxian(cursor.getString(cursor.getColumnIndex("quxian")));
            plan.setManager(cursor.getString(cursor.getColumnIndex("manager")));
            plan.setUser(cursor.getString(cursor.getColumnIndex("user")));
            plan.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            plan.setPlan_type(cursor.getInt(cursor.getColumnIndex("plan_type")));
            list.add(plan);
        }
        //LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return list;
    }

    @Override
    public ArrayList<CheckPlan> queryFinishedCheckPlan() {
        ArrayList<CheckPlan> list = new ArrayList<>();
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, "state = ?", new String[]{String.valueOf(3)}, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            list.add(plan);
        }
        cursor.close();
        //LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return list;
    }

    @Override
    public ArrayList<CheckPlan> queryCanUpLoadCheckPlan() {
        ArrayList<CheckPlan> list = new ArrayList<>();
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, "state = ?", new String[]{String.valueOf(2)}, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setArea(cursor.getString(cursor.getColumnIndex("area")));
            plan.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            plan.setType(cursor.getString(cursor.getColumnIndex("type")));
            plan.setQuxian(cursor.getString(cursor.getColumnIndex("quxian")));
            plan.setManager(cursor.getString(cursor.getColumnIndex("manager")));
            plan.setUser(cursor.getString(cursor.getColumnIndex("user")));
            plan.setPlan_type(cursor.getInt(cursor.getColumnIndex("plan_type")));
            list.add(plan);
        }
        cursor.close();
        LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return list;
    }

    public int queryCheckPlanState(int identifier, int type) {
        CheckPlan plan = null;
        int i = 0;
        Cursor cursor = db.query("checkplan", new String[]{"state"}, "identifier=? and plan_type = ?", new String[]{String.valueOf(identifier), String.valueOf(type)}, null, null, null);
        while (cursor.moveToNext()) {
            i = cursor.getInt(cursor.getColumnIndex("state"));
        }
        cursor.close();
        return i;
    }
    public ArrayList<CheckPlan> queryByCheckPlanState() {
        CheckPlan plan = null;
        ArrayList<CheckPlan> checkPlans = new ArrayList<>();
        Cursor cursor = db.query("checkplan",null, "state = 1 or state = 2", null, null, null, null);
        while (cursor.moveToNext()) {
           plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            checkPlans.add(plan);
        }
//        cursor.close();
        return checkPlans;
    }

    public String queryCheckPlanFileUrl(String sysId) {
        String url = null;
        Cursor cursor = db.query("checkplan", new String[]{"url"}, "sysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            url = cursor.getString(cursor.getColumnIndex("url"));
        }
        cursor.close();
        colseDateBase();
        return url;
    }

    @Override
    public boolean updateCheckPlanState(CheckPlan plan) {
        int state = queryCheckPlanState(plan.getIdentifier(), plan.getPlan_type());
        if (state == -1) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("state", plan.getState());
        int isSuccess = db.update("checkplan", values, "identifier = ?", new String[]{String.valueOf(plan.getIdentifier())});

        return isSuccess != 0;
    }

    @Override
    public void delete(String sysID) {
        db.delete("checkplan", "sysId=?", new String[]{sysID});
    }
    public void delete(String sysID,String type) {
        db.delete("checkplan", "sysId=? and plan_type = ?", new String[]{sysID,type});
    }

    public boolean updateCheckPlanState(String id, int newstate) {
        int state = queryCheckPlanState(Integer.valueOf(id), 0);
        if (state == -1) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("state", newstate);
        int isSuccess = db.update("checkplan", values, "sysId = ? and plan_type=0", new String[]{id});

        return isSuccess != 0;
    }

    public void colseDateBase() {
        if (db.isOpen()) {
            db.close();
        }
    }

}
