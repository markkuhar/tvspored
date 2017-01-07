package com.kuhar.kos.tvspored;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnector extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Spored.db";
    public static final String TABLE_NAME = "favorites";
    public static final String COL2 = "ime";
    public static final String COL3 = "fav";


    public DatabaseConnector(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE_NAME + " (IME TEXT PRIMARY KEY, FAV INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String name, int fav){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,fav);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME,null);
        return res;
    }

    public Cursor getData(String ime){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        res = db.rawQuery("select fav from " + TABLE_NAME + " WHERE ime = '" + ime + "'",null);
        return res;
    }
    public Cursor getAllFavorites(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ime from " + TABLE_NAME + " WHERE fav = 1",null);
        return res;
    }

    public boolean updateData(String ime, int val){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COL2,ime);
        contentValue.put(COL3,val);
        db.update(TABLE_NAME,contentValue, "ime = ?",new String []{ime});
        return true;
    }
}