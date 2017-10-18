package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.FinishedPlan;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class FinishedPlanDao {
    private SQLiteDatabase database;

    public FinishedPlanDao(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    public boolean save(FinishedPlan result) {
//      database.execSQL("insert into checkresult values(null,2133,12312,1,'sadasdas','dasdas','wad','sad')");
        ContentValues values = new ContentValues();
        values.put("SysId", result.getSysID());
        values.put("SysGcxxdjh", result.getSysGcxxdjh());
        values.put("AQ_LH_ID", result.getAQ_LH_ID());
        values.put("FinishedTime", result.getFinishedTime());
        values.put("Username", result.getUsername());
        long isSuccess = database.insert("finishedplan", "id", values);
        return isSuccess != -1;
    }

    public boolean update(FinishedPlan result) {
//      database.execSQL("insert into checkresult values(null,2133,12312,1,'sadasdas','dasdas','wad','sad')");
        ContentValues values = new ContentValues();
        values.put("SysId", result.getSysID());
        values.put("SysGcxxdjh", result.getSysGcxxdjh());
        values.put("AQ_LH_ID", result.getAQ_LH_ID());
        values.put("FinishedTime", result.getFinishedTime());
        values.put("Username", result.getUsername());
        long isSuccess = database.update("finishedplan", values, "SysId=?", new String[]{String.valueOf(result.getSysID())});
        return isSuccess != 0;
    }

    public ArrayList<FinishedPlan> queryAll() {
        ArrayList<FinishedPlan> list = new ArrayList<>();
        FinishedPlan result;
        Cursor cursor = database.query("finishedplan", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            result = new FinishedPlan();
            result.setSysID(cursor.getInt(cursor.getColumnIndex("SysID")));
            result.setSysGcxxdjh(cursor.getInt(cursor.getColumnIndex("SysGcxxdjh")));
            result.setAQ_LH_ID(cursor.getString(cursor.getColumnIndex("AQ_LH_ID")));
            result.setFinishedTime(cursor.getString(cursor.getColumnIndex("FinishedTime")));
            result.setUsername(cursor.getString(cursor.getColumnIndex("Username")));
            list.add(result);
        }
        LogUtil.logI("已经检查" + list.size() + "条");
        return list;
    }

    public FinishedPlan query(String SysID) {
        FinishedPlan result = null;
        Cursor cursor = database.query("finishedplan", null, "SysId=?", new String[]{SysID}, null, null, null);
        while (cursor.moveToNext()) {
            result = new FinishedPlan();
            result.setSysID(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setSysGcxxdjh(cursor.getInt(cursor.getColumnIndex("SysGcxxdjh")));
            result.setAQ_LH_ID(cursor.getString(cursor.getColumnIndex("AQ_LH_ID")));
            result.setFinishedTime(cursor.getString(cursor.getColumnIndex("FinishedTime")));
            result.setUsername(cursor.getString(cursor.getColumnIndex("Username")));
        }
        return result;
    }

    public boolean queryById(String SysID) {
        Cursor cursor = database.query("finishedplan", null, "SysId=?", new String[]{SysID}, null, null, null);
        while (cursor.moveToNext()) {
            return false;
        }
        return true;
    }

    public void deleteFinished(String SysID) {
        database.delete("finishedplan", "SysId=?", new String[]{SysID});
    }

    public void close() {
        if (database.isOpen()) {
            database.close();
        }
    }
}
