package com.data_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "NUMBER";
    public static final String COL_4 = "_DATE";
    public static final String COL_5 = "REMARK";
    public static final String DATABASE_NAME = "Employee.db";
    public static final String TABLE_NAME = "EMP_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table EMP_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,NUMBER TEXT,_DATE TEXT,REMARK TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS EMP_table");
        onCreate(db);
    }

    public boolean insertData(String name, String number, String date, String remark) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, number);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, remark);
        if (db.insert(TABLE_NAME, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public Cursor getAllData() {
        return getWritableDatabase().rawQuery("select * from EMP_table", null);
    }

    public boolean updateData(String id, String name, String number, String date, String remark) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, number);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, remark);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        return Integer.valueOf(getWritableDatabase().delete(TABLE_NAME, "ID = ?", new String[]{id}));
    }
}
