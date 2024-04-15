package com.example.ecommerce.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DbHandler extends SQLiteOpenHelper {

    Context context;

    public DbHandler(Context context) {
        super(context, VariableBag.DB_NAME, null,VariableBag.DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + VariableBag.TABLE_NAME + "(" +
                VariableBag.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VariableBag.EVENT_NAME + " TEXT," +
                VariableBag.EVENT_TIME + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }
    public void addUser(String event_name,String time)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(VariableBag.EVENT_NAME,event_name);
        values.put(VariableBag.EVENT_TIME,time);

        sqLiteDatabase.insert(VariableBag.TABLE_NAME,null,values);
        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
