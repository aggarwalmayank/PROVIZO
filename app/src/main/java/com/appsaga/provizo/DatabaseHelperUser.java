package com.appsaga.provizo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperUser extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = " card.db";
    public static final String TABLE_NAME = "user";
    public static final String COL_1 = "email";
    public static final String COL_2 = "name";
    public static final String COL_3 = "number";
    public static final String COL_4 = "dob";
    public static final String COL_5 = "gender";
    public static final String COL_6 = "verified";
    public static final String COL_7 = "GST";

    public DatabaseHelperUser(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (email TEXT primary key ,name TEXT ,number TEXT  ,dob TEXT,gender TEXT,verified TEXT,GST text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String email, String name, String number, String dob, String gender, String verified,String gst) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, email);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, number);
        contentValues.put(COL_4, dob);
        contentValues.put(COL_5, gender);
        contentValues.put(COL_6, verified);
        contentValues.put(COL_7, gst);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public boolean updateData(String email, String name, String number, String dob, String gender, String verified, String gst,String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, email);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, number);
        contentValues.put(COL_4, dob);
        contentValues.put(COL_5, gender);
        contentValues.put(COL_6, verified);
        contentValues.put(COL_7, gst);
        db.update(TABLE_NAME, contentValues, "email = ?", new String[]{a});
        return true;
    }
    public boolean updateSingleCol( String colname, String newdata ,String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colname, newdata);
        db.update(TABLE_NAME, contentValues, "email = ?", new String[]{a});
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor GetOneData(String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where email=?", new String[]{num});
        return res;

    }

}