package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bokun.bkjcb.on_siteinspection.Utils.Constants;
import com.elvishew.xlog.XLog;

/**
 * Created by DengShuai on 2017/3/20.
 * Description : 数据库打开工具类
 */

public class SQLiteOpenUtil extends SQLiteOpenHelper {

    public SQLiteOpenUtil(Context context) {
        super(context, "User.db", null, 5);
    }

    public SQLiteOpenUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_CHECK_RESULT);
        db.execSQL(Constants.CREATE_USER_TABLE);
        db.execSQL(Constants.CREATE_CHECK_PLAN_TABLE);
        db.execSQL(Constants.CREATE_SEARCH_HISTORY);
        db.execSQL(Constants.CREATE_FINISHED_PALN);
        db.execSQL(Constants.CREATE_CONSTRUCTION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 3:
                db.execSQL(Constants.CREATE_TEMP_TABLE);
                db.execSQL(Constants.CREATE_NEW_CHECKPALN);
                db.execSQL(Constants.INSERT_DATA_CHECKPLAN);
                db.execSQL(Constants.DROP_TEMP);
                XLog.i("修改");
//                break;
            case 4:
                //修改CheckResult表，添加aq_lh_id字段，区分检查结果所属计划
                db.execSQL(Constants.CREATE_TEMP_CHECK_RESULT);
                db.execSQL(Constants.CREATE_NEW_CHECK_RESULT);
                db.execSQL(Constants.INSERT_DATA_CHECK_RESULT);
                db.execSQL(Constants.DROP_TEMP_CHECK_RESULT);

                //修改finishedPlan表，添加Type字段，区分完成计划类型
                db.execSQL(Constants.CREATE_TEMP_FINISHED_PALN);
                db.execSQL(Constants.CREATE_NEW_FINISHED_PALN);
                db.execSQL(Constants.INSERT_DATA_FINISHED_PALN);
                db.execSQL(Constants.DROP_TEMP_FINISHED_PALN);

                //修改checkPlan表，添加plan_type字段，区分plan所属类型
                db.execSQL(Constants.CREATE_TEMP_TABLE1);
                db.execSQL(Constants.CREATE_NEW_CHECKPALN1);
                db.execSQL(Constants.INSERT_DATA_CHECKPLAN1);
                db.execSQL(Constants.DROP_TEMP1);
                break;
        }
    }
}
