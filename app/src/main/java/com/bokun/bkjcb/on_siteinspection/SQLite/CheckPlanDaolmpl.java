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
        long i = db.insert("checkplan", null, values);
        LogUtil.logI("插入plan" + i);
    }

    @Override
    public boolean updateCheckPlan(CheckPlan plan) {
        CheckPlan checkPlan = queryCheckPlan(plan.getIdentifier());
        if (checkPlan == null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("name", plan.getName());
        values.put("state", plan.getState());
        int isSuccess = db.update("checkplan", values, "indentifier = ?", new String[]{String.valueOf(plan.getIdentifier())});

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
           /* values.put("sysId", plan.getSysId());
            values.put("name", plan.getName());
            values.put("state", plan.getState());
            values.put("address", plan.getAddress());
            values.put("quxian", plan.getQuxian());
            values.put("area", plan.getArea());
            values.put("manager", plan.getManager());
            values.put("user", plan.getUser());
            values.put("type", plan.getType());
            values.put("tel", plan.getTel());*/
            plan.setArea(cursor.getString(cursor.getColumnIndex("area")));
            plan.setAddress(cursor.getString(cursor.getColumnIndex("address")));
//            plan.setSysId(cursor.getInt(cursor.getColumnIndex("sysId")));
            plan.setType(cursor.getString(cursor.getColumnIndex("type")));
            plan.setQuxian(cursor.getString(cursor.getColumnIndex("quxian")));
            plan.setManager(cursor.getString(cursor.getColumnIndex("manager")));
            plan.setUser(cursor.getString(cursor.getColumnIndex("user")));
        }
        cursor.close();
        //LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return plan;
    }

    @Override
    public boolean queryCheckPlanIsNull(int identifier) {
        Cursor cursor = db.query("checkplan", null, "identifier=?", new String[]{String.valueOf(identifier)}, null, null, null);
        boolean isNull = cursor.moveToNext();
        cursor.close();
        return isNull;
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
            list.add(plan);
        }
        cursor.close();
        LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return list;
    }

    public int queryCheckPlanState(int identifier) {
        CheckPlan plan = null;
        int i = 0;
        Cursor cursor = db.query("checkplan", new String[]{"state"}, "identifier=?", new String[]{String.valueOf(identifier)}, null, null, null);
        LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        while (cursor.moveToNext()) {
            LogUtil.logI("查询计划状态");
            i = cursor.getInt(cursor.getColumnIndex("state"));
        }
        cursor.close();
        return i;
    }

    @Override
    public boolean updateCheckPlanState(CheckPlan plan) {
        int state = queryCheckPlanState(plan.getIdentifier());
        if (state == -1) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("state", plan.getState());
        int isSuccess = db.update("checkplan", values, "identifier = ?", new String[]{String.valueOf(plan.getIdentifier())});

        return isSuccess != 0;
    }

    public boolean updateCheckPlanState(String id, int newstate) {
        int state = queryCheckPlanState(Integer.valueOf(id));
        if (state == -1) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("state", newstate);
        int isSuccess = db.update("checkplan", values, "sysId = ?", new String[]{id});

        return isSuccess != 0;
    }

    public void colseDateBase() {
        db.close();
    }

}
