package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bokun.bkjcb.on_siteinspection.Utils.Constants;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class SQLiteOpenUtil extends SQLiteOpenHelper {

    public SQLiteOpenUtil(Context context) {
        super(context, "User.db", null, 2);
    }

    public SQLiteOpenUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_CHECK_RESULT);
        db.execSQL(Constants.CREATE_CHECK_PLAN_TABLE);
        db.execSQL(Constants.CREATE_SEARCH_HISTORY);
        db.execSQL(Constants.CREATE_FINISHED_PALN);
        db.execSQL(Constants.CREATE_CONSTRUCTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
