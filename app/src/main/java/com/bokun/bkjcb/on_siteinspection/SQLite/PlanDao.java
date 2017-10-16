package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bokun.bkjcb.on_siteinspection.Domain.TableKeys;
import com.bokun.bkjcb.on_siteinspection.Http.JsonParser;
import com.bokun.bkjcb.on_siteinspection.Utils.Constants;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class PlanDao {
    private SQLiteDatabase database;
    private Context context;

    public PlanDao(Context context) {
        this.context = context;
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    public int checkPlanInfoTable() throws SQLiteException {
        int flag = 0;
        Cursor cursor = database.rawQuery("select count(id) from infokey", null);
        if (cursor.moveToNext()) {
            flag = cursor.getInt(0);
        }
        return flag;
    }

    public void createTable() {
        database.execSQL(Constants.CREATE_TABLE_INFO);
    }

    public void saveTableKey() {
        ArrayList<TableKeys> list = JsonParser.getKeysItems(context);
        for (int i = 0; i < list.size(); i++) {
            save(list.get(i));
        }
    }

    private boolean save(TableKeys keys) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("title", keys.getTitle());
        values.put("type", keys.getType());
        values.put("unit", keys.getUnit());
        values.put("typeid", keys.getId());
        flag = database.insert("infokey", "id", values);
        return flag > 0;
    }

    public TableKeys query(int typeId) {
        TableKeys keys = new TableKeys();
        Cursor cursor = database.query("infokey", null, "typeid=?", new String[]{String.valueOf(typeId)}, null, null, null);
        if (cursor.moveToNext()) {
            keys.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            keys.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
        }
        return keys;
    }

    public void close() {
        database.close();
    }
}
