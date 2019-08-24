package com.tbh.WebView.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tbh.WebView.model.NotificationModel;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "THB";
    private static final String TABLE_NAME = "THBTable";
    private static final String ID = "Id";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT,"+ DESCRIPTION + " TEXT"  + ")";

    /*Create Database*/
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*create Table*/
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /*Drop Table Table*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean addNotification(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues valuesExpense = new ContentValues();
            valuesExpense.put(TITLE, title);
            valuesExpense.put(DESCRIPTION, description);


            // Inserting Row
            long result = db.insert(TABLE_NAME, null, valuesExpense);
            if (result == -1) {
                db.close(); // Closing database connection
                return false;
            } else {
                db.close(); // Closing database connection
                return true;
            }

    }

    public ArrayList<NotificationModel> getNotification()
    {
        ArrayList<NotificationModel> monthlyReportModelArrayList=new ArrayList<>();

        String selectQuery = "SELECT  * FROM  THBTable";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationModel notificationModel=new NotificationModel();
                notificationModel.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                notificationModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                monthlyReportModelArrayList.add(notificationModel);
            } while (cursor.moveToNext());
        }
        return  monthlyReportModelArrayList;
    }



}
