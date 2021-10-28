package com.example.myapplication.User.LearnWord.history.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.example.myapplication.User.LearnWord.history.HistoryItem;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistorySqliteDataHelper extends SQLiteOpenHelper {

    Context context;
    final static String  DATABASE_NAME = "History.db";
    public static int DATABASE_VERSION =1;

    String table_name = "history";
    String column_id = "id";
    String column_word = "word";
    String column_date ="time";


    public HistorySqliteDataHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+table_name+
                " (" + column_id + " TEXT, "+
                column_word + " TEXT, "+
                column_date + " TEXT); ";
        db.execSQL(query);
        Log.e("TAG", "onCreate: create success" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    public void addHistory(HistoryItem historyItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(column_id, historyItem.getId());
        cv.put(column_word,historyItem.getWord());

        cv.put(column_date,historyItem.getDateTimeNow());


        long result = db.insert(table_name,null,cv);
        if(result== -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public void fetchData(ArrayList<HistoryItem> list){
        list.clear();


        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        //add a column if it not exist

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+table_name +" LIMIT 0,50", null, null);

        if(cursor != null){


            while (cursor.moveToNext()) {
                boolean checked = false;

                HistoryItem addItem = new HistoryItem(cursor.getString(0),
                        cursor.getString(1).toString(),
                        cursor.getString(2).toString());
                for (HistoryItem w:list
                ) {
                    if( w.getId() == addItem.getId()){
                        checked = true;
                    }
                }
                if(checked==true){
                }
                else{
                    list.add(addItem);
                }
            } }
        cursor.close();
        sqLiteDatabase.close();
    }
}
