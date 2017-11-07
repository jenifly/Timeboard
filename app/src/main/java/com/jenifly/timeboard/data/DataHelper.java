package com.jenifly.timeboard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.jenifly.timeboard.info.BaseInfo;
import com.jenifly.timeboard.info.StmpInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class DataHelper {
    // 数据库名称
    private static String DB_NAME = "timeboard.db";
    // 数据库版本
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION );
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public ArrayList<BaseInfo> getBaseInfoList() {
        ArrayList<BaseInfo> baseInfos = new ArrayList<BaseInfo>();
        Cursor cursor = db.rawQuery("select * from "+SqliteHelper.TB_NAME+" ORDER BY "+ BaseInfo.TBID, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BaseInfo baseInfo = new BaseInfo(Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                    Integer.parseInt(cursor.getString(3)), Long.parseLong(cursor.getString(4)),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getLong(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13));
            baseInfos.add(baseInfo);
            cursor.moveToNext();
        }
        cursor.close();
        return baseInfos;
    }

    private ArrayList<BaseInfo> getmBaseInfoList() {
        ArrayList<BaseInfo> baseInfos = new ArrayList<BaseInfo>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null , null, null,
                null, BaseInfo.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BaseInfo baseInfo = new BaseInfo(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),
                    Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),
                    Long.parseLong(cursor.getString(4)), cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getLong(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13));
            baseInfos.add(baseInfo);
            cursor.moveToNext();
        }
        cursor.close();
        return baseInfos;
    }

    // 判断users表中的是否包含某个UserID的记录
    public void haveBaseInfo(String tbId) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, BaseInfo.TBID
                + "=?", new String[]{tbId}, null, null, null );
        b = cursor.moveToFirst();
        cursor.close();
    }

    public BaseInfo getBaseInfoById(int tbId) {
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, BaseInfo.TBID
                + "=?", new String[]{String.valueOf(tbId)}, null, null, null );
        if(cursor.moveToFirst()){
          return   new BaseInfo(Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                  Integer.parseInt(cursor.getString(3)), Long.parseLong(cursor.getString(4)),
                  cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                  cursor.getLong(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13));
        }
        cursor.close();
        return null;
    }

    public BaseInfo getBaseInfoByMainKey() {
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, BaseInfo.ISMAIN
                + "=?", new String[]{"1"}, null, null, null );
        if(cursor.moveToFirst()){
            return   new BaseInfo(Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                    Integer.parseInt(cursor.getString(3)), Long.parseLong(cursor.getString(4)),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getLong(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13));
        }
        cursor.close();
        return null;
    }

    public int updateBaseInfo(int tbId, ArrayList<StmpInfo> stmpInfos) {
        ContentValues values = new ContentValues();
        for(StmpInfo stmpInfo :stmpInfos ){
            values.put(stmpInfo.key, stmpInfo.value);
        }
        int id = db.update(SqliteHelper. TB_NAME, values, BaseInfo.TBID + "=?" , new String[]{String.valueOf(tbId)});
        return id;
    }

    public void updateBaseInfo(int tbId, StmpInfo stmpInfo) {
        ContentValues values = new ContentValues();
        values.put(stmpInfo.key, stmpInfo.value);
        db.update(SqliteHelper.TB_NAME, values, BaseInfo.TBID + "=?" , new String[]{String.valueOf(tbId)});
    }

    public void updateBaseInfoById(int Id, StmpInfo stmpInfo) {
        ContentValues values = new ContentValues();
        values.put(stmpInfo.key, stmpInfo.value);
        db.update(SqliteHelper.TB_NAME, values, BaseInfo.ID + "=?" , new String[]{String.valueOf(Id)});
    }

    // 更新users表的记录
    public int updateBaseInfo(BaseInfo baseInfo) {
        ContentValues values = new ContentValues();
        values.put(BaseInfo.TBID, baseInfo.getTBID());
        values.put(BaseInfo.ISMAIN, baseInfo.getIsMain());
        values.put(BaseInfo.TYPR, baseInfo.getType());
        values.put(BaseInfo.TIMESTAMP, String.valueOf(baseInfo.getTimestamp()));
        values.put(BaseInfo.TEXT1, baseInfo.getText1());
        values.put(BaseInfo.TEXT2, baseInfo.getText2());
        values.put(BaseInfo.TEXT3, baseInfo.getText3());
        values.put(BaseInfo.TBGB, baseInfo.getbBg());
        values.put(BaseInfo.BACK_BG, baseInfo.getBack_bg());
        values.put(BaseInfo.BGCOLOR, baseInfo.getBGCOLOR());
        values.put(BaseInfo.BGCOLOR_LIGHT, baseInfo.getBGCOLOR_LIGHT());
        values.put(BaseInfo.BGCOLOR_DARK, baseInfo.getBGCOLOR_DARK());
        values.put(BaseInfo.BGCOLOR_PRESS, baseInfo.getBGCOLOR_PRESS());
        return db.update(SqliteHelper. TB_NAME, values, BaseInfo.TBID + "="
                + baseInfo.getTBID(), null);
    }

    public boolean updateMainKey(int id) {
        ContentValues values = new ContentValues();
        if(getBaseInfoByMainKey().getTBID() != id) {
            values.put(BaseInfo.ISMAIN, 0);
            db.update(SqliteHelper.TB_NAME, values, BaseInfo.ISMAIN + "=?", new String[]{"1"});
            values.put(BaseInfo.ISMAIN, 1);
            db.update(SqliteHelper.TB_NAME, values, BaseInfo.TBID + "=?", new String[]{String.valueOf(id)});
            return true;
        } else return false;
    }

    // 添加users表的记录
    public void addBaseInfo(BaseInfo baseInfo) {
        for (BaseInfo b : getmBaseInfoList()){
            if(baseInfo.getTBID() <= b.getTBID())
                updateBaseInfoById(b.getId(),new StmpInfo(BaseInfo.TBID,String.valueOf(b.getTBID()+1)));
        }
        ContentValues values = new ContentValues();
        values.put(BaseInfo.TBID, baseInfo.getTBID());
        values.put(BaseInfo.ISMAIN, baseInfo.getIsMain());
        values.put(BaseInfo.TYPR, baseInfo.getType());
        values.put(BaseInfo.TIMESTAMP, String.valueOf(baseInfo.getTimestamp()));
        values.put(BaseInfo.TEXT1, baseInfo.getText1());
        values.put(BaseInfo.TEXT2, baseInfo.getText2());
        values.put(BaseInfo.TEXT3, baseInfo.getText3());
        values.put(BaseInfo.TBGB, baseInfo.getbBg());
        values.put(BaseInfo.BACK_BG, baseInfo.getBack_bg());
        values.put(BaseInfo.BGCOLOR, baseInfo.getBGCOLOR());
        values.put(BaseInfo.BGCOLOR_LIGHT, baseInfo.getBGCOLOR_LIGHT());
        values.put(BaseInfo.BGCOLOR_DARK, baseInfo.getBGCOLOR_DARK());
        values.put(BaseInfo.BGCOLOR_PRESS, baseInfo.getBGCOLOR_PRESS());
        db.insert(SqliteHelper.TB_NAME, null, values);
    }

    // 删除users表的记录
    public void delBaseInfo(int tbId) {
        db.delete(SqliteHelper. TB_NAME, BaseInfo.TBID + "=?", new String[]{String.valueOf(tbId)});
        for (BaseInfo b : getmBaseInfoList()){
            if(tbId < b.getTBID())
                updateBaseInfoById(b.getId(),new StmpInfo(BaseInfo.TBID,String.valueOf(b.getTBID()-1)));
        }
    }

    public static BaseInfo getBaseByID(String tbId,List<BaseInfo> baseInfos){
        BaseInfo baseInfo = null;
        int size = baseInfos.size();
        for( int i=0;i<size;i++){
            if(tbId.equals(baseInfos.get(i).getTBID())){
                baseInfo = baseInfos.get(i);
                break;
            }
        }
        return baseInfo;
    }
}
