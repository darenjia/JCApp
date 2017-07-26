package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.User;
import com.bokun.bkjcb.on_siteinspection.JCApplication;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class UserDao {

    private SQLiteDatabase database;

    public UserDao() {
        SQLiteOpenUtil util = new SQLiteOpenUtil(JCApplication.getContext());
        database = util.getWritableDatabase();
    }

    public boolean addUser(User user) {

        ContentValues values = new ContentValues();
        values.put("name", user.getUserName());
        values.put("password", user.getPassword());
        values.put("quxian", user.getQuxian());
        values.put("sys_realname", user.getRealName());
        values.put("roles", user.getRole());
        long flag = database.insert("UserInfo", "id", values);
        if (flag != -1) {
            user.setId((int) flag);
        }
        return flag > 0;

    }

    public User getUser(String name) {
        User user = null;
        Cursor cursor = database.query("UserInfo", null, "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setQuxian(cursor.getString(cursor.getColumnIndex("quxian")));
            user.setRealName(cursor.getString(cursor.getColumnIndex("sys_realname")));
            user.setRole(cursor.getString(cursor.getColumnIndex("roles")));
        }
        return user;
    }

    public boolean getUserIs(String name) {
        Cursor cursor = database.query("UserInfo", null, "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
