package com.bokun.bkjcb.on_siteinspection.Domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.SQLite.SQLiteOpenUtil;

import java.util.ArrayList;
import java.util.List;

public class LogQuickSearch {
    private SQLiteDatabase database;
    private static LogQuickSearch search;

    public LogQuickSearch(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    public static List<String> all(Context context, int type) {
        search = new LogQuickSearch(context);
        List<String> list = new ArrayList<>();
        Cursor cursor = search.database.rawQuery("select name from searchhistory where type=" + type + " ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
            if (list.size() >= 5) break;
        }
        return list;
    }

    public static void save(Context context, String string, int type) {
        search = new LogQuickSearch(context);
        search.database.execSQL("insert into searchhistory (name,type) values('" + string + "'," + type + ")");
        search.database.close();
    }

}
