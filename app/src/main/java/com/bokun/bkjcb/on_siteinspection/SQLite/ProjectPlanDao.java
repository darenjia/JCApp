package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/6/2.
 */

public class ProjectPlanDao {
    private SQLiteDatabase database;

    public ProjectPlanDao(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    public boolean save(ProjectPlan result) {
        ContentValues values = new ContentValues();
        values.put("aq_lh_yyh", result.getAq_lh_yyh());
        values.put("aq_lh_seqid", result.getAq_lh_seqid());
        values.put("aq_lh_id", result.getAq_lh_id());
        values.put("aq_lh_jcrq", result.getAq_lh_jcrq());
        values.put("aq_lh_allbj", result.getAq_lh_allbj());
        values.put("aq_lh_jcmc", result.getAq_lh_jcmc());
        values.put("aq_lh_szqx", result.getAq_lh_szqx());
        values.put("aq_lh_jchy", result.getAq_lh_jchy().toString());
        values.put("aq_lh_qxjd", result.getAq_lh_qxjd());
        values.put("aq_jctype", result.getAq_jctype());
        values.put("aq_sysid", result.getAq_sysid());
        values.put("aq_jctz_zt", result.getAq_jctz_zt());
        long isSuccess = database.insert("constructioninfo", "id", values);
        return isSuccess != -1;
    }

    public ArrayList<ProjectPlan> query(String state) {
        ArrayList<ProjectPlan> list = new ArrayList<>();
        ProjectPlan result = null;
        Cursor cursor = database.query("constructioninfo", null, "aq_jctz_zt=?", new String[]{state}, null, null, null);
        while (cursor.moveToNext()) {
            result = new ProjectPlan();
            result.setAq_lh_id(cursor.getString(cursor.getColumnIndex("aq_lh_id")));
            result.setAq_lh_jcmc(cursor.getString(cursor.getColumnIndex("aq_lh_jcmc")));
            result.setAq_jctz_zt(cursor.getString(cursor.getColumnIndex("aq_jctz_zt")));
            result.setAq_sysid(cursor.getString(cursor.getColumnIndex("aq_sysid")));
            list.add(result);
        }
        return list;
    }

    public ArrayList<ProjectPlan> queryNo(String[] state) {
        ArrayList<ProjectPlan> list = new ArrayList<>();
        ProjectPlan result = null;
        Cursor cursor = database.query("constructioninfo", null, "aq_jctz_zt in(?,?)", state, null, null, null);
        while (cursor.moveToNext()) {
            result = new ProjectPlan();
            result.setAq_lh_id(cursor.getString(cursor.getColumnIndex("aq_lh_id")));
            result.setAq_lh_jcmc(cursor.getString(cursor.getColumnIndex("aq_lh_jcmc")));
            result.setAq_jctz_zt(cursor.getString(cursor.getColumnIndex("aq_jctz_zt")));
            result.setAq_sysid(cursor.getString(cursor.getColumnIndex("aq_sysid")));
            list.add(result);
        }
        return list;
    }

    public boolean update(String id, String state) {
        ContentValues values = new ContentValues();
        values.put("aq_jctz_zt", state);
        int result = database.update("constructioninfo", values, "aq_lh_id=?", new String[]{id});

        return result > 0;
    }

    public boolean issaved(String aq_lh_id) {
        Cursor cursor = database.query("constructioninfo", new String[]{"id"}, "aq_lh_id = ?", new String[]{aq_lh_id}, null, null, null);
        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public void close() {
        database.close();
    }
}
