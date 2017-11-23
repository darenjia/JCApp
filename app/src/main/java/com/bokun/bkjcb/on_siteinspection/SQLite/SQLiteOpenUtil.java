package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bokun.bkjcb.on_siteinspection.Utils.Constants;

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
//                break;
            case 4:
                db.execSQL(Constants.CREATE_TEMP_CHECK_RESULT);
                db.execSQL(Constants.CREATE_NEW_CHECK_RESULT);
                db.execSQL(Constants.INSERT_DATA_CHECK_RESULT);
                db.execSQL(Constants.DROP_TEMP_CHECK_RESULT);
                break;
        }
    }
}
