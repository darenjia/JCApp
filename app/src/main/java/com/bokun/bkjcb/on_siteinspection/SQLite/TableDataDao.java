package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.TableData;

import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class TableDataDao {
    private SQLiteDatabase database;
    private Context context;

    public TableDataDao(Context context) {
        this.context = context;
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }


    public void save(TableData tableData) {
        for (TableData.JingGao jingGao : tableData.getJinggao()) {
            saveJG(jingGao);
        }
        for (TableData.BiaogeInfo info : tableData.getBiaogeInfo()) {
            savebginfo(info);
        }
        for (TableData.ShiYongYongTu yongTu : tableData.getScShiyong()) {
            saveYT(yongTu);
        }
        for (TableData.ShiYongDW dw : tableData.getScShiyongdw()) {
            saveDW(dw);
        }
        for (TableData.ShengChan1 shengChan1 : tableData.getShengchan01()) {
            saveShengChan(shengChan1);
        }
        for (TableData.ShengChan2 shengChan2 : tableData.getShengchan02()) {
            saveShengChan(shengChan2);
        }
        close();
    }

    public TableData query(String sysId) {
        TableData data = new TableData();
        data.setBiaogeInfo(queryBG(sysId));
        data.setJinggao(queryJG(sysId));
        data.setScShiyong(queryYT(sysId));
        data.setScShiyongdw(queryDW(sysId));
        data.setShengchan01(querySC1(sysId));
        data.setShengchan02(querySC2(sysId));
        close();
        return data;
    }

    public void delete(String sysId){
        database.delete("shengchan01","SysId=?",new String[]{sysId});
        database.delete("shengchan02","SysId=?",new String[]{sysId});
        database.delete("jinggao","SysId=?",new String[]{sysId});
        database.delete("biaogeinfo","SysId=?",new String[]{sysId});
        database.delete("shiyongyt","SysId=?",new String[]{sysId});
        database.delete("shiyongdw","SysId=?",new String[]{sysId});
    }
    public boolean hasSaved(String sysId) {
        Cursor cursor = database.query("shengchan01", new String[]{"id"}, "SysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    private boolean saveJG(TableData.JingGao jc) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("SysId", jc.getSysId());
        values.put("ScGgSzlc", jc.getScGgSzlc());
        values.put("ScGgLcjg", jc.getScGgLcjg());
        flag = database.insert("jinggao", "id", values);
        return flag > 0;
    }

    private boolean savebginfo(TableData.BiaogeInfo info) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("SysId", info.getSysId());
        values.put("ScGgGdBgFzr", info.getScGgGdBgFzr());
        values.put("ScGgGdBgTbr", info.getScGgGdBgTbr());
        values.put("ScGgGdBgLxdh", info.getScGgGdBgLxdh());
        values.put("ScGgGdBgBcrq", info.getScGgGdBgBcrq());
        flag = database.insert("biaogeinfo", "id", values);
        return flag > 0;
    }

    private boolean saveYT(TableData.ShiYongYongTu info) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("SysId", info.getSysId());
        values.put("SyScSsmc", info.getSyScSsmc());
        values.put("SyScJzmj", info.getSyScJzmj());
        values.put("SyScCws", info.getSyScCws());
        values.put("SyScQtyt", info.getSyScQtyt());
        flag = database.insert("shiyongyt", "id", values);
        return flag > 0;
    }

    private boolean saveDW(TableData.ShiYongDW info) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("SysId", info.getSysId());
        values.put("SydwMc", info.getSydwMc());
        values.put("SydwFddbr", info.getSydwFddbr());
        values.put("SydwLxr", info.getSydwLxr());
        values.put("SydwLxdh", info.getSydwLxdh());
        values.put("SydwLx", info.getSydwLx());
        values.put("SydwSyfs", info.getSydwSyfs());
        values.put("SydwGsdj", info.getSydwGsdj());
        values.put("SydwZaxk", info.getSydwZaxk());
        values.put("SydwWsxk", info.getSydwWsxk());
        values.put("SydwXfxk", info.getSydwXfxk());
        flag = database.insert("shiyongdw", "id", values);
        return flag > 0;
    }

    private boolean saveShengChan(TableData.ShengChan1 info) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("SysId", info.getSysId());
        values.put("SysGcxxdjh", info.getSysGcxxdjh());
        values.put("SysMfgcbh", info.getSysMfgcbh());
        values.put("ScJbGcmc", info.getScJbGcmc());
        values.put("ScJbGcdz", info.getScJbGcdz());
        values.put("ScJbSzqx", info.getScJbSzqx());
        values.put("ScJbJdxz", info.getScJbJdxz());
        values.put("ScJbBz", info.getScJbBz());
        values.put("ScXzJcnf", info.getScXzJcnf());
        values.put("ScXzJzmj", info.getScXzJzmj());
        values.put("ScXzSjyt", info.getScXzSjyt());
        values.put("ScXzMfjzmj", info.getScXzMfjzmj());
        values.put("ScXzMzsd", info.getScXzMzsd());
        values.put("ScXzKbs", info.getScXzKbs());
        values.put("ScXzDxcs", info.getScXzDxcs());
        values.put("ScXzJgys", info.getScXzJgys());
        values.put("ScXzGcxs", info.getScXzGcxs());
        values.put("ScXzJglb", info.getScXzJglb());
        values.put("ScXzGclb", info.getScXzGclb());
        values.put("ScXzGcxz", info.getScXzGcxz());
        values.put("ScXzYllt", info.getScXzYllt());
        values.put("ScXzLtqtgc", info.getScXzLtqtgc());
        values.put("ScXzLtdxZb", info.getScXzLtdxZb());
        values.put("ScXzLtdxDt", info.getScXzLtdxDt());
        values.put("ScXzDmjz", info.getScXzDmjz());
        values.put("ScXzDmjzQt", info.getScXzDmjzQt());
        values.put("ScXzSsgc", info.getScXzSsgc());
        flag = database.insert("shengchan01", "id", values);
        return flag > 0;
    }

    private boolean saveShengChan(TableData.ShengChan2 info) {
        long flag;
        ContentValues values = new ContentValues();
        values.put("SysId", info.getSysId());
        values.put("ScJszDw", info.getScJszDw());
        values.put("ScJszSjzg", info.getScJszSjzg());
        values.put("ScJszFddbr", info.getScJszFddbr());
        values.put("ScJszLxr", info.getScJszLxr());
        values.put("ScJszLxdh", info.getScJszLxdh());
        values.put("ScJszDwxz", info.getScJszDwxz());
        values.put("ScJszCqz", info.getScJszCqz());
        values.put("ScSyzMc", info.getScSyzMc());
        values.put("ScSyzCqz", info.getScSyzCqz());
        values.put("ScSyzFddbr", info.getScSyzFddbr());
        values.put("ScSyzLxr", info.getScSyzLxr());
        values.put("ScSyzLxdh", info.getScSyzLxdh());
        values.put("ScSyzLx", info.getScSyzLx());
        values.put("ScGlzMc", info.getScGlzMc());
        values.put("ScGlzSjzg", info.getScGlzSjzg());
        values.put("ScGlzFddbr", info.getScGlzFddbr());
        values.put("ScGlzLxr", info.getScGlzLxr());
        values.put("ScGlzLxdh", info.getScGlzLxdh());
        values.put("ScGlzLx", info.getScGlzLx());
        flag = database.insert("shengchan02", "id", values);
        return flag > 0;
    }

    private ArrayList<TableData.JingGao> queryJG(String sysId) {
        ArrayList<TableData.JingGao> list = new ArrayList<>();
        TableData.JingGao result;
        Cursor cursor = database.query("jinggao", null, "SysId=?", new String[]{sysId}, null, null, "ScGgSzlc ASC");
        while (cursor.moveToNext()) {
            result = new TableData.JingGao();
            result.setSysId(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setScGgLcjg(cursor.getString(cursor.getColumnIndex("ScGgLcjg")));
            result.setScGgSzlc(cursor.getInt(cursor.getColumnIndex("ScGgSzlc")));
            list.add(result);
        }
        return list;
    }

    private ArrayList<TableData.BiaogeInfo> queryBG(String sysId) {
        ArrayList<TableData.BiaogeInfo> list = new ArrayList<>();
        TableData.BiaogeInfo result;
        Cursor cursor = database.query("biaogeinfo", null, "SysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            result = new TableData.BiaogeInfo();
            result.setSysId(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setScGgGdBgFzr(cursor.getString(cursor.getColumnIndex("ScGgGdBgFzr")));
            result.setScGgGdBgTbr(cursor.getString(cursor.getColumnIndex("ScGgGdBgTbr")));
            result.setScGgGdBgLxdh(cursor.getString(cursor.getColumnIndex("ScGgGdBgLxdh")));
            result.setScGgGdBgBcrq(cursor.getString(cursor.getColumnIndex("ScGgGdBgBcrq")));
            list.add(result);
        }
        return list;
    }

    private ArrayList<TableData.ShiYongYongTu> queryYT(String sysId) {
        ArrayList<TableData.ShiYongYongTu> list = new ArrayList<>();
        TableData.ShiYongYongTu result;
        Cursor cursor = database.query("shiyongyt", null, "SysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            result = new TableData.ShiYongYongTu();
            result.setSysId(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setSyScSsmc(cursor.getString(cursor.getColumnIndex("SyScSsmc")));
            result.setSyScJzmj(cursor.getString(cursor.getColumnIndex("SyScJzmj")));
            result.setSyScCws(cursor.getInt(cursor.getColumnIndex("SyScCws")));
            result.setSyScQtyt(cursor.getString(cursor.getColumnIndex("SyScQtyt")));
            list.add(result);
        }
        return list;
    }

    private ArrayList<TableData.ShiYongDW> queryDW(String sysId) {
        ArrayList<TableData.ShiYongDW> list = new ArrayList<>();
        TableData.ShiYongDW result;
        Cursor cursor = database.query("shiyongdw", null, "SysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            result = new TableData.ShiYongDW();
            result.setSysId(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setSydwMc(cursor.getString(cursor.getColumnIndex("SydwMc")));
            result.setSydwFddbr(cursor.getString(cursor.getColumnIndex("SydwFddbr")));
            result.setSydwLxr(cursor.getString(cursor.getColumnIndex("SydwLxr")));
            result.setSydwLxdh(cursor.getString(cursor.getColumnIndex("SydwLxdh")));
            result.setSydwLx(cursor.getString(cursor.getColumnIndex("SydwLx")));
            result.setSydwSyfs(cursor.getString(cursor.getColumnIndex("SydwSyfs")));
            result.setSydwGsdj(cursor.getInt(cursor.getColumnIndex("SydwGsdj")));
            result.setSydwZaxk(cursor.getInt(cursor.getColumnIndex("SydwZaxk")));
            result.setSydwWsxk(cursor.getInt(cursor.getColumnIndex("SydwWsxk")));
            result.setSydwXfxk(cursor.getInt(cursor.getColumnIndex("SydwXfxk")));
            list.add(result);
        }
        return list;
    }

    private ArrayList<TableData.ShengChan1> querySC1(String sysId) {
        ArrayList<TableData.ShengChan1> list = new ArrayList<>();
        TableData.ShengChan1 result;
        Cursor cursor = database.query("shengchan01", null, "SysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            result = new TableData.ShengChan1();
            result.setSysId(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setSysGcxxdjh(cursor.getInt(cursor.getColumnIndex("SysGcxxdjh")));
            result.setSysMfgcbh(cursor.getString(cursor.getColumnIndex("SysMfgcbh")));
            result.setScJbGcmc(cursor.getString(cursor.getColumnIndex("ScJbGcmc")));
            result.setScJbGcdz(cursor.getString(cursor.getColumnIndex("ScJbGcdz")));
            result.setScJbSzqx(cursor.getString(cursor.getColumnIndex("ScJbSzqx")));
            result.setScJbJdxz(cursor.getString(cursor.getColumnIndex("ScJbJdxz")));
            result.setScJbBz(cursor.getString(cursor.getColumnIndex("ScJbBz")));
            result.setScXzJcnf(cursor.getString(cursor.getColumnIndex("ScXzJcnf")));
            result.setScXzJzmj(cursor.getString(cursor.getColumnIndex("ScXzJzmj")));
            result.setScXzSjyt(cursor.getString(cursor.getColumnIndex("ScXzSjyt")));
            result.setScXzMfjzmj(cursor.getString(cursor.getColumnIndex("ScXzMfjzmj")));
            result.setScXzMzsd(cursor.getString(cursor.getColumnIndex("ScXzMzsd")));
            result.setScXzKbs(cursor.getInt(cursor.getColumnIndex("ScXzKbs")));
            result.setScXzDxcs(cursor.getInt(cursor.getColumnIndex("ScXzDxcs")));
            result.setScXzJgys(cursor.getInt(cursor.getColumnIndex("ScXzJgys")));
            result.setScXzGcxs(cursor.getString(cursor.getColumnIndex("ScXzGcxs")));
            result.setScXzJglb(cursor.getString(cursor.getColumnIndex("ScXzJglb")));
            result.setScXzGclb(cursor.getString(cursor.getColumnIndex("ScXzGclb")));
            result.setScXzGcxz(cursor.getInt(cursor.getColumnIndex("ScXzGcxz")));
            result.setScXzYllt(cursor.getInt(cursor.getColumnIndex("ScXzYllt")));
            result.setScXzLtqtgc(cursor.getInt(cursor.getColumnIndex("ScXzLtqtgc")));
            result.setScXzLtdxZb(cursor.getInt(cursor.getColumnIndex("ScXzLtdxZb")));
            result.setScXzLtdxDt(cursor.getString(cursor.getColumnIndex("ScXzLtdxDt")));
            result.setScXzDmjz(cursor.getString(cursor.getColumnIndex("ScXzDmjz")));
            result.setScXzDmjzQt(cursor.getString(cursor.getColumnIndex("ScXzDmjzQt")));
            result.setScXzSsgc(cursor.getInt(cursor.getColumnIndex("ScXzSsgc")));
            list.add(result);
        }
        return list;
    }

    private ArrayList<TableData.ShengChan2> querySC2(String sysId) {
        ArrayList<TableData.ShengChan2> list = new ArrayList<>();
        TableData.ShengChan2 result;
        Cursor cursor = database.query("shengchan02", null, "SysId=?", new String[]{sysId}, null, null, null);
        while (cursor.moveToNext()) {
            result = new TableData.ShengChan2();
            result.setSysId(cursor.getInt(cursor.getColumnIndex("SysId")));
            result.setScJszDw(cursor.getString(cursor.getColumnIndex("ScJszDw")));
            result.setScJszSjzg(cursor.getString(cursor.getColumnIndex("ScJszSjzg")));
            result.setScJszFddbr(cursor.getString(cursor.getColumnIndex("ScJszFddbr")));
            result.setScJszLxr(cursor.getString(cursor.getColumnIndex("ScJszLxr")));
            result.setScJszLxdh(cursor.getString(cursor.getColumnIndex("ScJszLxdh")));
            result.setScJszDwxz(cursor.getString(cursor.getColumnIndex("ScJszDwxz")));
            result.setScJszCqz(cursor.getInt(cursor.getColumnIndex("ScJszCqz")));
            result.setScSyzMc(cursor.getString(cursor.getColumnIndex("ScSyzMc")));
            result.setScSyzCqz(cursor.getInt(cursor.getColumnIndex("ScSyzCqz")));
            result.setScSyzFddbr(cursor.getString(cursor.getColumnIndex("ScSyzFddbr")));
            result.setScSyzLxr(cursor.getString(cursor.getColumnIndex("ScSyzLxr")));
            result.setScSyzLxdh(cursor.getString(cursor.getColumnIndex("ScSyzLxdh")));
            result.setScSyzLx(cursor.getString(cursor.getColumnIndex("ScSyzLx")));
            result.setScGlzMc(cursor.getString(cursor.getColumnIndex("ScGlzMc")));
            result.setScGlzSjzg(cursor.getString(cursor.getColumnIndex("ScGlzSjzg")));
            result.setScGlzFddbr(cursor.getString(cursor.getColumnIndex("ScGlzFddbr")));
            result.setScGlzLxr(cursor.getString(cursor.getColumnIndex("ScGlzLxr")));
            result.setScGlzLxdh(cursor.getString(cursor.getColumnIndex("ScGlzLxdh")));
            result.setScGlzLx(cursor.getString(cursor.getColumnIndex("ScGlzLx")));
            list.add(result);
        }
        return list;
    }

    public void close() {
        if (database.isOpen()) {
            database.close();
        }
    }
}
