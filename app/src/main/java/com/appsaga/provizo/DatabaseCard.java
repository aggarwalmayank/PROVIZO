package com.appsaga.provizo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCard extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = " card.db";
    public static final String TABLE_NAME = "debit_card";
    public static final String COL_1 = "card_number";
    public static final String COL_2 = "holder";
    public static final String COL_3 = "exp";
    public static final String COL_4 = "cvv";

    public DatabaseCard(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (card_number TEXT primary key ,holder TEXT ,exp TEXT ,cvv TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String num, String name, String exp, String cvv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, num);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, exp);
        contentValues.put(COL_4, cvv);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public boolean updateData(String num, String name, String exp, String cvv,String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, num);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, exp);
        contentValues.put(COL_4, cvv);
        db.update(TABLE_NAME, contentValues, "card_number = ?", new String[]{a});
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }


    public Cursor GetOneData(String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where card_number=?", new String[]{num});
        return res;

    }
}