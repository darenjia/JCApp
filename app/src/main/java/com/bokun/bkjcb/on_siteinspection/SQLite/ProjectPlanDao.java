package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.ProjectPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.User;

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

    public boolean save(ProjectPlan result, int userId) {
        ContentValues values = new ContentValues();
        values.put("aq_lh_yyh", result.getAq_lh_yyh());
        values.put("aq_lh_seqid", result.getAq_lh_seqid());
        values.put("aq_lh_id", result.getAq_lh_id());
        values.put("aq_lh_jcrq", result.getAq_lh_jcrq());
        values.put("aq_lh_allbj", result.getAq_lh_allbj());
        values.put("aq_lh_jcmc", result.getAq_lh_jcmc());
        values.put("aq_lh_szqx", result.getAq_lh_szqx());
//        values.put("aq_lh_jchy", result.getAq_lh_jchy().toString());
        values.put("aq_lh_qxjd", result.getAq_lh_qxjd());
        values.put("aq_jctype", result.getAq_jctype());
        values.put("aq_sysid", result.getAq_sysid());
        values.put("aq_jctz_zt", result.getAq_jctz_zt());
        values.put("AQ_JCTZ_sfjc", result.getAQ_JCTZ_sfjc());
        values.put("userId", userId);
        long isSuccess = database.insert("constructioninfo", "id", values);
        return isSuccess != -1;
    }

    public ArrayList<ProjectPlan> query(String state, int userId) {
        ArrayList<ProjectPlan> list = new ArrayList<>();
        ProjectPlan result = null;
        Cursor cursor = database.query("constructioninfo", null, "aq_jctz_zt=? and userId = ?", new String[]{state, String.valueOf(userId)}, null, null, null);
        while (cursor.moveToNext()) {
            result = new ProjectPlan();
            result.setAq_lh_id(cursor.getString(cursor.getColumnIndex("aq_lh_id")));
            result.setAq_lh_jcmc(cursor.getString(cursor.getColumnIndex("aq_lh_jcmc")));
            result.setAq_jctz_zt(cursor.getString(cursor.getColumnIndex("aq_jctz_zt")));
            result.setAq_sysid(cursor.getString(cursor.getColumnIndex("aq_sysid")));
            result.setAq_lh_seqid(cursor.getString(cursor.getColumnIndex("aq_lh_seqid")));
            result.setAq_lh_szqx(cursor.getString(cursor.getColumnIndex("aq_lh_szqx")));
            result.setAQ_JCTZ_sfjc(cursor.getInt(cursor.getColumnIndex("AQ_JCTZ_sfjc")));
            list.add(result);
        }
        return list;
    }

    public ProjectPlan queryById(String id) {
        ProjectPlan result = null;
        Cursor cursor = database.query("constructioninfo", null, "aq_lh_id=?", new String[]{id}, null, null, null);
        if (cursor.moveToNext()) {
            result = new ProjectPlan();
            result.setAq_lh_id(cursor.getString(cursor.getColumnIndex("aq_lh_id")));
            result.setAq_lh_jcmc(cursor.getString(cursor.getColumnIndex("aq_lh_jcmc")));
            result.setAq_jctz_zt(cursor.getString(cursor.getColumnIndex("aq_jctz_zt")));
            result.setAq_sysid(cursor.getString(cursor.getColumnIndex("aq_sysid")));
            result.setAq_lh_seqid(cursor.getString(cursor.getColumnIndex("aq_lh_seqid")));
            result.setAQ_JCTZ_sfjc(cursor.getInt(cursor.getColumnIndex("AQ_JCTZ_sfjc")));
        }
        return result;
    }

    public ArrayList<ProjectPlan> queryNo(String state) {
        ArrayList<ProjectPlan> list = new ArrayList<>();
        ProjectPlan result = null;
        Cursor cursor = database.query("constructioninfo", null, "aq_jctz_zt != ?", new String[]{state}, null, null, null);
        while (cursor.moveToNext()) {
            result = new ProjectPlan();
            result.setAq_lh_id(cursor.getString(cursor.getColumnIndex("aq_lh_id")));
            result.setAq_lh_jcmc(cursor.getString(cursor.getColumnIndex("aq_lh_jcmc")));
            result.setAq_jctz_zt(cursor.getString(cursor.getColumnIndex("aq_jctz_zt")));
            result.setAq_sysid(cursor.getString(cursor.getColumnIndex("aq_sysid")));
            result.setAq_lh_szqx(cursor.getString(cursor.getColumnIndex("aq_lh_szqx")));
            result.setAq_lh_jcrq(cursor.getString(cursor.getColumnIndex("aq_lh_jcrq")));
            result.setAQ_JCTZ_sfjc(cursor.getInt(cursor.getColumnIndex("AQ_JCTZ_sfjc")));
            list.add(result);
        }
        return list;
    }

    public ArrayList<ProjectPlan> queryNo(String state, int userId) {
        ArrayList<ProjectPlan> list = new ArrayList<>();
        ProjectPlan result = null;
        Cursor cursor = database.query("constructioninfo", null, "aq_jctz_zt != ? and userId = ?", new String[]{state, String.valueOf(userId)}, null, null, null);
        while (cursor.moveToNext()) {
            result = new ProjectPlan();
            result.setAq_lh_id(cursor.getString(cursor.getColumnIndex("aq_lh_id")));
            result.setAq_lh_jcmc(cursor.getString(cursor.getColumnIndex("aq_lh_jcmc")));
            result.setAq_jctz_zt(cursor.getString(cursor.getColumnIndex("aq_jctz_zt")));
            result.setAq_sysid(cursor.getString(cursor.getColumnIndex("aq_sysid")));
            result.setAq_lh_szqx(cursor.getString(cursor.getColumnIndex("aq_lh_szqx")));
            result.setAq_lh_jcrq(cursor.getString(cursor.getColumnIndex("aq_lh_jcrq")));
            result.setAQ_JCTZ_sfjc(cursor.getInt(cursor.getColumnIndex("AQ_JCTZ_sfjc")));
            if (result.getAQ_JCTZ_sfjc() == 2 || result.getAQ_JCTZ_sfjc() == 3) {
                list.add(result);
            }
        }
        return list;
    }

    public boolean update(String id, String state) {
        ContentValues values = new ContentValues();
        values.put("aq_jctz_zt", state);
        int result = database.update("constructioninfo", values, "aq_lh_id=?", new String[]{id});

        return result > 0;
    }

    public boolean update(String id, ProjectPlan result, User user) {
        ContentValues values = new ContentValues();
        values.put("aq_lh_yyh", result.getAq_lh_yyh());
        values.put("aq_lh_seqid", result.getAq_lh_seqid());
        values.put("aq_lh_id", result.getAq_lh_id());
        values.put("aq_lh_jcrq", result.getAq_lh_jcrq());
        values.put("aq_lh_allbj", result.getAq_lh_allbj());
        values.put("aq_lh_jcmc", result.getAq_lh_jcmc());
        values.put("aq_lh_szqx", result.getAq_lh_szqx());
//        values.put("aq_lh_jchy", result.getAq_lh_jchy().toString());
        values.put("aq_lh_qxjd", result.getAq_lh_qxjd());
        values.put("aq_jctype", result.getAq_jctype());
        values.put("aq_sysid", result.getAq_sysid());
        values.put("AQ_JCTZ_sfjc", result.getAQ_JCTZ_sfjc());
        values.put("userId", user.getId());
        int flag = database.update("constructioninfo", values, "id = ? ", new String[]{id});
        return flag > 0;
    }

    public String issaved(String id) {
        Cursor cursor = database.query("constructioninfo", new String[]{"id"}, "aq_lh_seqid = ?", new String[]{id}, null, null, null);
        while (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return null;
    }

    public void delete(String id) {
        database.delete("constructioninfo", "aq_lh_id=?", new String[]{id});
    }

    public void close() {
        if (database.isOpen()) {
            database.close();
        }
    }
}
