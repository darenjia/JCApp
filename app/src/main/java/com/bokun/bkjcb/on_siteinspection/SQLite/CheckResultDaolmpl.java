package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Utils.LocalTools;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/23.
 */

public class CheckResultDaolmpl extends CheckResultDao {
    private SQLiteDatabase database;

    public CheckResultDaolmpl(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    @Override
    public boolean insertCheckResult(CheckResult result) {
//      database.execSQL("insert into checkresult values(null,2133,12312,1,'sadasdas','dasdas','wad','sad')");
        ContentValues values = new ContentValues();
        values.put("identifier", result.getIdentifier());
        values.put("num", result.getNum());
        values.put("checkresult", result.getResult());
        values.put("comment", result.getComment());
        values.put("audio", LocalTools.changeToString(result.getAudioUrls()));
        values.put("image", LocalTools.changeToString(result.getImageUrls()));
        values.put("video", LocalTools.changeToString(result.getVideoUrls()));
        long isSuccess = database.insert("checkresult", "id", values);
        return isSuccess != -1;
    }

    @Override
    public ArrayList<CheckResult> queryCheckResult(int Identifier) {
        ArrayList<CheckResult> list = new ArrayList<>();
        CheckResult result;
        Cursor cursor = database.query("checkresult", null, "identifier = ?", new String[]{String.valueOf(Identifier)}, null, null, "num ASC");
        while (cursor.moveToNext()) {
            result = new CheckResult();
            result.setId(cursor.getInt(cursor.getColumnIndex("id")));
            result.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            result.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            result.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            result.setResult(cursor.getInt(cursor.getColumnIndex("checkresult")));
            result.setImageUrls(LocalTools.changeToList(cursor.getString(cursor.getColumnIndex("image"))));
            result.setAudioUrls(LocalTools.changeToList(cursor.getString(cursor.getColumnIndex("audio"))));
            result.setVideoUrls(LocalTools.changeToList(cursor.getString(cursor.getColumnIndex("video"))));
            list.add(result);
        }
        LogUtil.logI("已经检查" + list.size() + "条");
        return list;
    }

    @Override
    public boolean queryById(int Identifier) {
        Cursor cursor = database.query("checkresult", null, "identifier = ?", new String[]{String.valueOf(Identifier)}, null, null, "num ASC");
        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCheckResult(CheckResult result) {
        ContentValues values = new ContentValues();
        values.put("identifier", result.getIdentifier());
        values.put("num", result.getNum());
        values.put("checkresult", result.getResult());
        values.put("comment", result.getComment());
        values.put("audio", LocalTools.changeToString(result.getAudioUrls()));
        values.put("image", LocalTools.changeToString(result.getImageUrls()));
        values.put("video", LocalTools.changeToString(result.getVideoUrls()));
        long isSuccess = database.update("checkresult", values, "id = ?", new String[]{String.valueOf(result.getId())});
        return isSuccess != 0;
    }

    @Override
    public void changeCheckResult(CheckResult result) {

    }

    @Override
    public void clean(int id) {
        database.delete("checkresult", "identifier=?", new String[]{String.valueOf(id)});
    }


    public void closeDatabase() {
        database.close();
    }

}
