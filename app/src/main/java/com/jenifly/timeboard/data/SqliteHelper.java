package com.jenifly.timeboard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jenifly.timeboard.info.BaseInfo;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    //用来保存UserID、Access Token、Access Secret的表名
    public static final String TB_NAME= "baseinfo";
    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                TB_NAME+ "("+
                BaseInfo.ID + " integer primary key autoincrement," +
                BaseInfo.TBID + " varchar," +
                BaseInfo.ISMAIN + " varchar," +
                BaseInfo.TYPR + " varchar," +
                BaseInfo.TIMESTAMP + " varchar," +
                BaseInfo.TEXT1 + " varchar," +
                BaseInfo.TEXT2 + " varchar," +
                BaseInfo.TEXT3 + " varchar," +
                BaseInfo.TBGB + " varchar," +
                BaseInfo.BACK_BG + " varchar," +
                BaseInfo.BGCOLOR + " varchar," +
                BaseInfo.BGCOLOR_LIGHT + " varchar," +
                BaseInfo.BGCOLOR_DARK + " varchar," +
                BaseInfo.BGCOLOR_PRESS + " varchar)"
        );
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME );
        onCreate(db);
    }

    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
        try{
            db.execSQL( "ALTER TABLE " + TB_NAME + " CHANGE " + oldColumn + " "+ newColumn + " " + typeColumn);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
