package com.appsaga.provizo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperUser extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = " Userinfo.db";
    public static final String TABLE_NAME = "User_Details";
    public static final String COL_1 = " email";
    public static final String COL_2 = " name";
    public static final String COL_3 = " number";
    public static final String COL_4 = " sex";
    public static final String COL_5 = " dob";

    public DatabaseHelperUser(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (email TEXT ,name TEXT,number TEXT,sex TEXT,dob TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String email, String name, String number, String sex, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, email);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, number);
        contentValues.put(COL_4, sex);
        contentValues.put(COL_5, dob);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public Cursor GetTwoData(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where email=?", new String[]{email});
        return res;

    }



  /*  public boolean updateData(String name, String dob, String address, String number, String passnum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, dob);
        contentValues.put(COL_3, address);
        contentValues.put(COL_4, number);
        db.update(TABLE_NAME, contentValues, "number = ?", new String[]{passnum});
        return true;
    }
*/
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

}