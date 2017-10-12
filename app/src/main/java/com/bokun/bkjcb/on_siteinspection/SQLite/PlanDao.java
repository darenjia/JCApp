package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
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

    public void checkPlanInfoTable() throws SQLiteException {
        database.execSQL("select count(id) from infokey");
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
        flag = database.insert("infokey", "id", values);
        return flag > 0;
    }
    public void close(){
        database.close();
    }
}
